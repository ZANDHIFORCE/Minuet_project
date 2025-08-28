package com.ZandhiDokkie.minuet.repository.jdbc;

import com.ZandhiDokkie.minuet.domain.LessonSlot;
import com.ZandhiDokkie.minuet.repository.interfaces.LessonSlotRepository;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * JDBC 기반 레슨 슬롯 리포지토리 구현체
 * TODO: 실제 데이터베이스 연동 로직 구현 필요
 */
public class JdbcLessonSlotRepository implements LessonSlotRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcLessonSlotRepository(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private RowMapper<LessonSlot> lessonSlotRowMapper(){
        return new RowMapper<LessonSlot>(){
            @Override
            public LessonSlot mapRow(ResultSet rs, int rowNum) throws SQLException {
                LessonSlot lessonSlot = new LessonSlot();
                lessonSlot.setId(rs.getLong("id"));
                lessonSlot.setTeacherId(rs.getLong("teacher_id"));
                lessonSlot.setStudentId(rs.getLong("student_id"));
                lessonSlot.setDay(rs.getInt("day"));
                java.sql.Time time = rs.getTime("time");
                lessonSlot.setTime(time != null ? time.toLocalTime() : null);
                return lessonSlot;
            }
        };
    }

    @Override
    public Integer getLength() {
        String sql = "SELECT COUNT(*) FROM lesson_slot";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public Optional<LessonSlot> getLessonSlot(Long lessonSlotId) {
        String sql = "SELECT * FROM lesson_slot WHERE id=?";
        return jdbcTemplate.query(sql, lessonSlotRowMapper(), lessonSlotId).stream().findAny();
    }

    @Override
    public List<LessonSlot> getLessonSlots() {
        String sql = "SELECT * FROM lesson_slot";
        return jdbcTemplate.query(sql, lessonSlotRowMapper());
    }

    @Override
    public Optional<LessonSlot> createLessonSlot(LessonSlot lessonSlot) {
        try{
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            jdbcInsert.withTableName("lesson_slot").usingGeneratedKeyColumns("id");

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("teacher_id", lessonSlot.getTeacherId());
            parameters.put("student_id", lessonSlot.getStudentId());
            parameters.put("day", lessonSlot.getDay());
            parameters.put("time", Time.valueOf(lessonSlot.getTime()));

            Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
            lessonSlot.setId(key.longValue());
            return Optional.of(lessonSlot);
            
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<LessonSlot> updateLessonSlot(LessonSlot lessonSlot) {
        String sql = "UPDATE lesson_slot SET teacher_id=?, student_id=?, day=?, time=? WHERE id=?";
        int rowsAffected = jdbcTemplate.update(sql, lessonSlot.getTeacherId(), lessonSlot.getStudentId(), lessonSlot.getDay(), Time.valueOf(lessonSlot.getTime()), lessonSlot.getId());

        if(rowsAffected > 0){
            return Optional.of(lessonSlot);
        }
        return Optional.empty();
    }

    @Override
    public Optional<LessonSlot> deleteLessonSlot(Long lessonSlotId) {
        Optional<LessonSlot> lessonSlot = getLessonSlot(lessonSlotId);
        if(lessonSlot.isPresent()){
            String sql = "DELETE FROM lesson_slot WHERE id = ?";
            int rowsAffected = jdbcTemplate.update(sql, lessonSlotId);
            if(rowsAffected > 0){
                return lessonSlot;
            }
        }
        return Optional.empty();
    }

    @Override
    public void clearStore() {
        String sql = "DELETE FROM lesson_slot";
        jdbcTemplate.update(sql);
    }

    @Override
    public List<LessonSlot> findByStudentId(Long studentId) {
        String sql = "SELECT * FROM lesson_slot WHERE student_id = ?";
        return jdbcTemplate.query(sql, lessonSlotRowMapper(), studentId);
    }

    @Override
    public List<LessonSlot> findByTeacherId(Long teacherId) {
        String sql = "SELECT * FROM lesson_slot WHERE teacher_id = ?";
        return jdbcTemplate.query(sql, lessonSlotRowMapper(), teacherId);
    }
}
