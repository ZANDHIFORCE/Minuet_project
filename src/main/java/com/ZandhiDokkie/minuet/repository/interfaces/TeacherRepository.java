package com.ZandhiDokkie.minuet.repository.interfaces;
import com.ZandhiDokkie.minuet.domain.Teacher;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository {
    Integer getLength();
    Optional<Teacher> getTeacher(Long teacherId);
    List<Teacher> getTeachers();
    Optional<Teacher> createTeacher(Teacher teacher);
    Optional<Teacher> updateTeacher(Teacher teacher);
    Optional<Teacher> deleteTeacher(Long teacherID);
    void clearStore();
}