package com.ZandhiDokkie.minuet.repository.jdbc;

import com.ZandhiDokkie.minuet.domain.LessonInfo;
import com.ZandhiDokkie.minuet.repository.interfaces.LessonInfoRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * JDBC 기반 레슨 정보 리포지토리 구현체
 * TODO: 실제 데이터베이스 연동 로직 구현 필요
 */
public class JdbcLessonInfoRepository implements LessonInfoRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcLessonInfoRepository(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private RowMapper<LessonInfo> lessonInfoRowMapper(){
        return new RowMapper<LessonInfo>() {
            @Override
            public LessonInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                LessonInfo lessonInfo = new LessonInfo();
                lessonInfo.setId(rs.getLong("id"));
                lessonInfo.setTeacherId(rs.getLong("teacher_id"));
                lessonInfo.setStudentId(rs.getLong("student_id"));
                lessonInfo.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
                lessonInfo.setCompleted(rs.getBoolean("completed"));
                return lessonInfo;
            }
        };
    }

    @Override
    public Integer getLength() {
        String sql = "SELECT COUNT(*) FROM lesson_info";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public Optional<LessonInfo> getLessonInfo(Long lessonInfoId) {
        String sql = "SELECT * FROM lesson_info WHERE id = ?";
        return jdbcTemplate.query(sql, lessonInfoRowMapper(), lessonInfoId).stream().findAny();
    }

    @Override
    public List<LessonInfo> getLessonInfos() {
        String sql = "SELECT * FROM lesson_info";
        return jdbcTemplate.query(sql, lessonInfoRowMapper());
    }

    @Override
    public Optional<LessonInfo> createLessonInfo(LessonInfo lessonInfo) {
        try{
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            jdbcInsert.withTableName("lesson_info").usingGeneratedKeyColumns("id");

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("teacher_id", lessonInfo.getTeacherId());
            parameters.put("student_id", lessonInfo.getStudentId());
            parameters.put("date_time", lessonInfo.getDateTime());
            parameters.put("completed" ,lessonInfo.isCompleted());

            Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
            lessonInfo.setId(key.longValue());
            return Optional.of(lessonInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<LessonInfo> updateLessonInfo(LessonInfo lessonInfo) {
        String sql = "UPDATE lesson_info SET teacher_id = ?, student_id = ?, date_time = ?, completed = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, lessonInfo.getTeacherId(), lessonInfo.getStudentId(), lessonInfo.getDateTime(), lessonInfo.isCompleted(), lessonInfo.getId());
        if(rowsAffected > 0){
            return Optional.of(lessonInfo);
        }
        return Optional.empty();
    }

    @Override
    public Optional<LessonInfo> deleteLessonInfo(Long lessonInfoId) {
        Optional<LessonInfo> lessonInfo = getLessonInfo(lessonInfoId);
        if(lessonInfo.isPresent()){
            String sql = "DELETE FROM lesson_info WHERE id = ?";
            int rowsAffected = jdbcTemplate.update(sql, lessonInfoId);
            if(rowsAffected > 0){
                return lessonInfo;
            }
        }
        return Optional.empty();
    }

    @Override
    public void clearStore() {
        String sql = "DELETE FROM lesson_info";
        jdbcTemplate.update(sql);
    }

    @Override
    public List<LessonInfo> findByStudentId(Long studentId) {
        String sql = "SELECT * FROM lesson_info WHERE student_id = ?";
        return jdbcTemplate.query(sql, lessonInfoRowMapper(), studentId);
    }

    @Override
    public List<LessonInfo> findByTeacherId(Long teacherId) {
        String sql = "SELECT * FROM lesson_info WHERE teacher_id = ?";
        return jdbcTemplate.query(sql, lessonInfoRowMapper(), teacherId);
    }
}
