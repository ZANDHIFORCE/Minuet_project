package com.ZandhiDokkie.minuet.repository.jdbc;

import com.ZandhiDokkie.minuet.domain.Teacher;
import com.ZandhiDokkie.minuet.repository.interfaces.TeacherRepository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class JdbcTeacherRepository implements TeacherRepository {
    private final DataSource dataSource;


    public JdbcTeacherRepository(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public Integer getLength() {
        return 0;
    }

    @Override
    public Optional<Teacher> getTeacher(Long teacherId) {
        return Optional.empty();
    }

    @Override
    public List<Teacher> getTeachers() {
        return List.of();
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
}
