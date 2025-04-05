package com.ZandhiDokkie.minuet.repository.memory;
import com.ZandhiDokkie.minuet.domain.Teacher;
import com.ZandhiDokkie.minuet.repository.interfaces.TeacherRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MemoryTeacherRepository implements TeacherRepository{

    private Map<Long, Teacher> teachers;

    void loadFromFile(String path = "" );


    // Implements
    @Override
    public Integer getLength() {
        return 0;
    }

    @Override
    public Optional<Teacher> getTeacher() {
        return Optional.empty();
    }

    @Override
    public List<Teacher> getTeachers() {
        return List.of();
    }

    @Override
    public Optional<Teacher> createTeacher() {
        return Optional.empty();
    }

    @Override
    public Optional<Teacher> updateTeacher() {
        return Optional.empty();
    }

    @Override
    public void deleteTeacher() {

    }

    @Override
    public void clearStore() {

    }
}

