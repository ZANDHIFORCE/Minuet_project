package com.ZandhiDokkie.minuet.repository.jdbc;

import com.ZandhiDokkie.minuet.domain.Student;
import com.ZandhiDokkie.minuet.repository.interfaces.StudentRepository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class JdbcStudentRepository implements StudentRepository {

    private DataSource dataSource;

    public JdbcStudentRepository(DataSource dataSource){
        this.dataSource = dataSource;
    }


    @Override
    public Integer getLength() {
        return 0;
    }

    @Override
    public Optional<Student> getStudent(Long studentId) {
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
