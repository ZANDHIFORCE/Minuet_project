package com.ZandhiDokkie.minuet.repository.jdbc;

import com.ZandhiDokkie.minuet.domain.LessonSlot;
import com.ZandhiDokkie.minuet.exception.LessonSlotConflictException;
import com.ZandhiDokkie.minuet.repository.interfaces.LessonSlotRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JdbcLessonSlotRepository implements LessonSlotRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final TransactionTemplate transactionTemplate;
    private final RowMapper<LessonSlot> rowMapper = (rs, rowNum) -> new LessonSlot(
            rs.getLong("id"), rs.getLong("teacher_id"), rs.getLong("student_id"),
            rs.getInt("lesson_day"), rs.getTime("lesson_time").toLocalTime(), rs.getInt("duration_minutes"));

    public JdbcLessonSlotRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("lesson_slot").usingGeneratedKeyColumns("id");
        transactionTemplate = new TransactionTemplate(new DataSourceTransactionManager(dataSource));
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
    }

    @Override
    public Integer getLength() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM lesson_slot", Integer.class);
    }

    @Override
    public Optional<LessonSlot> getLessonSlot(Long lessonSlotId) {
        return jdbcTemplate.query("SELECT * FROM lesson_slot WHERE id=?", rowMapper, lessonSlotId).stream().findFirst();
    }

    @Override
    public List<LessonSlot> getLessonSlots() {
        return jdbcTemplate.query("SELECT * FROM lesson_slot", rowMapper);
    }

    @Override
    public Optional<LessonSlot> createLessonSlot(LessonSlot slot) {
        return transactionTemplate.execute(status -> {
            lockParticipants(slot);
            ensureAvailable(slot, null);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("teacher_id", slot.getTeacherId());
            parameters.put("student_id", slot.getStudentId());
            parameters.put("lesson_day", slot.getDay());
            parameters.put("lesson_time", Time.valueOf(slot.getTime()));
            parameters.put("duration_minutes", slot.getDurationMinutes());
            try {
                Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
                slot.setId(key.longValue());
                return Optional.of(slot);
            } catch (DuplicateKeyException e) {
                throw new LessonSlotConflictException(e);
            }
        });
    }

    @Override
    public Optional<LessonSlot> updateLessonSlot(LessonSlot slot) {
        return transactionTemplate.execute(status -> {
            lockParticipants(slot);
            if (getLessonSlot(slot.getId()).isEmpty()) {
                return Optional.empty();
            }
            ensureAvailable(slot, slot.getId());
            String sql = "UPDATE lesson_slot SET teacher_id=?, student_id=?, lesson_day=?, lesson_time=?, duration_minutes=? WHERE id=?";
            try {
                int rows = jdbcTemplate.update(sql, slot.getTeacherId(), slot.getStudentId(), slot.getDay(),
                        Time.valueOf(slot.getTime()), slot.getDurationMinutes(), slot.getId());
                return rows > 0 ? Optional.of(slot) : Optional.empty();
            } catch (DuplicateKeyException e) {
                throw new LessonSlotConflictException(e);
            }
        });
    }

    private void lockParticipants(LessonSlot slot) {
        // 같은 강사나 학생의 요청이 동시에 겹침 검사를 통과하지 않도록 잠금
        // 잠금 순서는 강사 -> 학생으로 통일
        jdbcTemplate.queryForList("SELECT id FROM teacher WHERE id=? FOR UPDATE", Long.class, slot.getTeacherId());
        jdbcTemplate.queryForList("SELECT id FROM student WHERE id=? FOR UPDATE", Long.class, slot.getStudentId());
    }

    private void ensureAvailable(LessonSlot slot, Long excludedId) {
        // 기존 시작 < 새 종료, 새 시작 < 기존 종료일 때만 겹침
        String sql = """
                SELECT COUNT(*) FROM lesson_slot
                WHERE lesson_day=? AND (teacher_id=? OR student_id=?)
                  AND (EXTRACT(HOUR FROM lesson_time) * 60 + EXTRACT(MINUTE FROM lesson_time)) < ?
                  AND (EXTRACT(HOUR FROM lesson_time) * 60 + EXTRACT(MINUTE FROM lesson_time) + duration_minutes) > ?
                """;
        List<Object> parameters = new ArrayList<>(List.of(
                slot.getDay(), slot.getTeacherId(), slot.getStudentId(), slot.endMinute(), slot.startMinute()));
        if (excludedId != null) {
            sql += " AND id <> ?";
            parameters.add(excludedId);
        }
        if (jdbcTemplate.queryForObject(sql, Integer.class, parameters.toArray()) > 0) {
            throw new LessonSlotConflictException();
        }
    }

    @Override
    public Optional<LessonSlot> deleteLessonSlot(Long lessonSlotId) {
        Optional<LessonSlot> slot = getLessonSlot(lessonSlotId);
        if (slot.isPresent() && jdbcTemplate.update("DELETE FROM lesson_slot WHERE id=?", lessonSlotId) > 0) {
            return slot;
        }
        return Optional.empty();
    }

    @Override
    public void clearStore() {
        jdbcTemplate.update("DELETE FROM lesson_slot");
    }

    @Override
    public List<LessonSlot> findByStudentId(Long studentId) {
        return jdbcTemplate.query("SELECT * FROM lesson_slot WHERE student_id=?", rowMapper, studentId);
    }

    @Override
    public List<LessonSlot> findByTeacherId(Long teacherId) {
        return jdbcTemplate.query("SELECT * FROM lesson_slot WHERE teacher_id=?", rowMapper, teacherId);
    }
}
