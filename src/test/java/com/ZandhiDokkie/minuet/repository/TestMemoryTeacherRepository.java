package com.ZandhiDokkie.minuet.repository;

import com.ZandhiDokkie.minuet.domain.Teacher;
import com.ZandhiDokkie.minuet.repository.interfaces.TeacherRepository;

import java.util.*;

/**
 * 테스트용 메모리 선생님 리포지토리
 * 파일 로딩 없이 순수 메모리만 사용
 */
public class TestMemoryTeacherRepository implements TeacherRepository {
    private final Map<Long, Teacher> teachers = new HashMap<>();
    private long nextId = 1L;

    @Override
    public Integer getLength() {
        return teachers.size();
    }

    @Override
    public Optional<Teacher> getTeacher(Long teacherId) {
        return Optional.ofNullable(teachers.get(teacherId));
    }

    @Override
    public List<Teacher> getTeachers() {
        return new ArrayList<>(teachers.values());
    }

    @Override
    public Optional<Teacher> createTeacher(Teacher teacher) {
        if (teacher.getId() == null) {
            teacher.setId(nextId++);
        }
        teachers.put(teacher.getId(), teacher);
        return Optional.of(teacher);
    }

    @Override
    public Optional<Teacher> updateTeacher(Teacher teacher) {
        if (!teachers.containsKey(teacher.getId())) {
            return Optional.empty();
        }
        teachers.put(teacher.getId(), teacher);
        return Optional.of(teacher);
    }

    @Override
    public Optional<Teacher> deleteTeacher(Long teacherId) {
        return Optional.ofNullable(teachers.remove(teacherId));
    }

    @Override
    public void clearStore() {
        teachers.clear();
        nextId = 1L;
    }

    @Override
    public Optional<Teacher> findByName(String teacherName) {
        return teachers.values().stream()
                .filter(t -> teacherName.equals(t.getName()))
                .findFirst();
    }
}
