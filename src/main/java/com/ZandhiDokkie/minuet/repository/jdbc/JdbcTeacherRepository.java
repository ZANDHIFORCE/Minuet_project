package com.ZandhiDokkie.minuet.repository.jdbc;

import com.ZandhiDokkie.minuet.domain.Teacher;
import com.ZandhiDokkie.minuet.repository.interfaces.TeacherRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
        return Optional.empty();
    }

    @Override
    public Optional<Teacher> updateTeacher(Teacher teacher) {
        return Optional.empty();
    }

    @Override
    public Optional<Teacher> deleteTeacher(Long teacherID) {
        return Optional.empty();
    }

    @Override
    public void clearStore() {
    }

    @Override
    public Optional<Teacher> findByName(String teacherName) {
        return Optional.empty();
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
