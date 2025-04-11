package com.ZandhiDokkie.minuet.repository.interfaces;
import com.ZandhiDokkie.minuet.domain.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {
    Integer getLength();
    Optional<Student> getStudent(Long studentId);
    List<Student> getStudents();
    Optional<Student> createStudent(Student student);
    Optional<Student> updateStudent(Student student);
    Optional<Student> deleteStudent(Long studentId);
    void clearStore();
}