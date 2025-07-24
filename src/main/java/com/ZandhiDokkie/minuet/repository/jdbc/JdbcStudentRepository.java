package com.ZandhiDokkie.minuet.repository.jdbc;

import com.ZandhiDokkie.minuet.domain.Student;
import com.ZandhiDokkie.minuet.repository.interfaces.StudentRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
        String sql = "SELECT * FROM teacher WHERE id = ?";
//        jdbcTemplate.query(sql, )
        return Optional.empty();
    }

    @Override
    public List<Student> getStudents() {
        return List.of();
    }

    @Override
    public Optional<Student> createStudent(Student student) {
        return Optional.empty();
    }

    @Override
    public Optional<Student> updateStudent(Student student) {
        return Optional.empty();
    }

    @Override
    public Optional<Student> deleteStudent(Long studentId) {
        return Optional.empty();
    }

    @Override
    public void clearStore() {

    }

    @Override
    public Optional<Student> findByName(String studentName) {
        return Optional.empty();
    }
}
