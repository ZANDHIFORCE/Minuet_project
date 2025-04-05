package com.ZandhiDokkie.minuet.repository.interfaces;
import com.ZandhiDokkie.minuet.domain.Teacher;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository {
    Integer getLength();
    Optional<Teacher> getTeacher();
    List<Teacher> getTeachers();
    Optional<Teacher> createTeacher();
    Optional<Teacher> updateTeacher();
    void deleteTeacher();
    void clearStore();
}