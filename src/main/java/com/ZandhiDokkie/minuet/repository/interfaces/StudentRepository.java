package com.ZandhiDokkie.minuet.repository.interfaces;
import com.ZandhiDokkie.minuet.domain.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {
    Integer getLength();
    Student getStudent();
    List<Student> getStudents();
    Optional<Student> createStudent();
    Optional<Student> updateStudent();
    void deleteStudent();
    void clearStore();
}