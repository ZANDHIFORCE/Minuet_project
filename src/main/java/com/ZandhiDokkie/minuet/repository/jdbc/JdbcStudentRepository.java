package com.ZandhiDokkie.minuet.repository.jdbc;

import com.ZandhiDokkie.minuet.domain.Student;
import com.ZandhiDokkie.minuet.repository.interfaces.StudentRepository;
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

public class JdbcStudentRepository implements StudentRepository {

    private final JdbcTemplate jdbcTemplate;

    private RowMapper<Student> studentRowMapper(){
        return new RowMapper<Student>() {
            @Override
            public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
                Student student = new Student();
                student.setId(rs.getLong("id"));
                student.setName(rs.getString("name"));
                student.setProgressSessions(rs.getInt("progressSessions"));
                student.setTotalSessions(rs.getInt("totalSessions"));
                return student;
            }
        };
    }

    public JdbcStudentRepository(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public Integer getLength() {
        String sql = "SELECT COUNT(*) FROM student";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public Optional<Student> getStudent(Long studentId) {
        String sql = "SELECT * FROM student WHERE id = ?";
        return jdbcTemplate.query(sql, studentRowMapper(), studentId).stream().findAny();
    }

    @Override
    public List<Student> getStudents() {
        String sql = "SELECT * FROM student";
        return jdbcTemplate.query(sql, studentRowMapper());
    }

    @Override
    public Optional<Student> createStudent(Student student) {
        try{
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            jdbcInsert.withTableName("student").usingGeneratedKeyColumns("id");

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("name", student.getName());
            parameters.put("progressSessions", student.getProgressSessions());
            parameters.put("totalSessions", student.getTotalSessions());

            Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
            student.setId(key.longValue());
            return Optional.of(student);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Student> updateStudent(Student student) {
        String sql = "UPDATE student SET name = ?, progressSessions = ?, totalSessions = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, student.getName(), student.getProgressSessions(), student.getTotalSessions(), student.getId());
        if(rowsAffected>0){
            return Optional.of(student);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Student> deleteStudent(Long studentId) {
        Optional<Student> student = getStudent(studentId);
        if(student.isPresent()){
            String sql = "DELETE FROM student WHERE id = ?";
            int rowsAffected = jdbcTemplate.update(sql, studentId);
            if(rowsAffected>0){
                return student;
            }
        }
        return Optional.empty();
    }

    @Override
    public void clearStore() {
        String sql = "DELETE FROM student";
        jdbcTemplate.update(sql);
    }

    @Override
    public Optional<Student> findByName(String studentName) {
        String sql = "SELECT * FROM student WHERE name = ?";
        return jdbcTemplate.query(sql, studentRowMapper(), studentName).stream().findAny();
    }
}
