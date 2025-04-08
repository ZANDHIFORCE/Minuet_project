package com.ZandhiDokkie.minuet.repository.memory;
import com.ZandhiDokkie.minuet.domain.Teacher;
import com.ZandhiDokkie.minuet.repository.interfaces.TeacherRepository;

import javax.swing.text.html.Option;
import java.util.*;

public class MemoryTeacherRepository implements TeacherRepository{

    private final Map<Long, Teacher> teachers = new HashMap<>();

    // Implements
    @Override
    public Integer getLength() {
        return this.teachers.size();
    }

    @Override
    public Optional<Teacher> getTeacher(Long teacherId) {
        return Optional.ofNullable(this.teachers.get(teacherId));
    }

    @Override
    public List<Teacher> getTeachers() {
        return new ArrayList<Teacher>(this.teachers.values());
    }

    @Override
    public Optional<Teacher> createTeacher(Teacher teacher) {
        Long teacherId = teacher.getId();
        if (this.teachers.containsKey(teacherId)){
            return Optional.empty();
        }
        this.teachers.put(teacherId,teacher);
        return Optional.of(teacher);
    }

    @Override
    public Optional<Teacher> updateTeacher(Teacher teacher) {
        Long teacherId = teacher.getId();
        if(!this.teachers.containsKey(teacherId)){
            return Optional.empty();
        }
        this.teachers.put(teacherId,teacher);
        return Optional.of(teacher);
    }

    @Override
    public Optional<Teacher> deleteTeacher(Long teacherID) {
        Teacher removed = this.teachers.remove(teacherID);
        return Optional.ofNullable(removed);
    }

    @Override
    public void clearStore() {
        this.teachers.clear();
    }
}

