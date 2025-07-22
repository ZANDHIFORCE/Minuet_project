package com.ZandhiDokkie.minuet.repository.jdbc;

import com.ZandhiDokkie.minuet.domain.Teacher;
import com.ZandhiDokkie.minuet.repository.interfaces.TeacherRepository;
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

public class JdbcTeacherRepository implements TeacherRepository {
    private final JdbcTemplate jdbcTemplate;


    public JdbcTeacherRepository(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Integer getLength() {
        String sql = "SELECT COUNT(*) FROM teacher";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public Optional<Teacher> getTeacher(Long teacherId) {
        String sql = "SELECT * FROM teacher WHERE id = ?";
        return jdbcTemplate.query(sql, teacherRowMapper(), teacherId)
                .stream()
                .findAny();
    }

    @Override
    public List<Teacher> getTeachers() {
        String sql = "SELECT * FROM teacher";
        return jdbcTemplate.query(sql, teacherRowMapper());
    }

    @Override
    public Optional<Teacher> createTeacher(Teacher teacher) {
        try {
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            jdbcInsert.withTableName("teacher").usingGeneratedKeyColumns("id");

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("name", teacher.getName());
            parameters.put("subject", teacher.getSubject());

            Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
            teacher.setId(key.longValue());
            return Optional.of(teacher);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Teacher> updateTeacher(Teacher teacher) {
        String sql = "UPDATE teacher SET name = ?, subject = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, teacher.getName(), teacher.getSubject(), teacher.getId());
        
        if (rowsAffected > 0) {
            return Optional.of(teacher);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Teacher> deleteTeacher(Long teacherID) {
        Optional<Teacher> teacher = getTeacher(teacherID);
        if (teacher.isPresent()) {
            String sql = "DELETE FROM teacher WHERE id = ?";
            int rowsAffected = jdbcTemplate.update(sql, teacherID);
            if (rowsAffected > 0) {
                return teacher;
            }
        }
        return Optional.empty();
    }

    @Override
    public void clearStore() {
    }

    @Override
    public Optional<Teacher> findByName(String teacherName) {
        String sql = "SELECT * FROM teacher WHERE name = ?";
        List<Teacher> result = jdbcTemplate.query(sql, teacherRowMapper(), teacherName);
        return result.stream().findAny();
    }

    private RowMapper<Teacher> teacherRowMapper(){
        return new RowMapper<Teacher>() {
            @Override
            public Teacher mapRow(ResultSet rs, int rowNum) throws SQLException {
                Teacher teacher = new Teacher();
                teacher.setId(rs.getLong("id"));
                teacher.setName(rs.getString("name"));
                teacher.setSubject(rs.getString("subject"));
                return teacher;
            }
        };
    }
}
