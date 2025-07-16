package com.ZandhiDokkie.minuet.repository;

import com.ZandhiDokkie.minuet.domain.Student;
import com.ZandhiDokkie.minuet.repository.interfaces.StudentRepository;

import java.util.*;

/**
 * 테스트용 메모리 학생 리포지토리
 * 파일 로딩 없이 순수 메모리만 사용
 */
public class TestMemoryStudentRepository implements StudentRepository {
    private final Map<Long, Student> students = new HashMap<>();
    private long nextId = 1L;

    @Override
    public Integer getLength() {
        return students.size();
    }

    @Override
    public Optional<Student> getStudent(Long studentId) {
        return Optional.ofNullable(students.get(studentId));
    }

    @Override
    public List<Student> getStudents() {
        return new ArrayList<>(students.values());
    }

    @Override
    public Optional<Student> createStudent(Student student) {
        if (student.getId() == null) {
            student.setId(nextId++);
        }
        students.put(student.getId(), student);
        return Optional.of(student);
    }

    @Override
    public Optional<Student> updateStudent(Student student) {
        if (!students.containsKey(student.getId())) {
            return Optional.empty();
        }
        students.put(student.getId(), student);
        return Optional.of(student);
    }

    @Override
    public Optional<Student> deleteStudent(Long studentId) {
        return Optional.ofNullable(students.remove(studentId));
    }

    @Override
    public void clearStore() {
        students.clear();
        nextId = 1L;
    }

    @Override
    public Optional<Student> findByName(String studentName) {
        return students.values().stream()
                .filter(s -> studentName.equals(s.getName()))
                .findFirst();
    }
}
