package com.ZandhiDokkie.minuet.integration;

import com.ZandhiDokkie.minuet.domain.Student;
import com.ZandhiDokkie.minuet.domain.Teacher;
import com.ZandhiDokkie.minuet.domain.LessonInfo;
import com.ZandhiDokkie.minuet.service.StudentService;
import com.ZandhiDokkie.minuet.service.TeacherService;
import com.ZandhiDokkie.minuet.service.LessonInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ErrorScenarioTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private LessonInfoService lessonInfoService;

    @BeforeEach
    void setUp() {
        studentService.clearAllStudents();
        teacherService.clearAllTeachers();
        lessonInfoService.clearAllLessonInfos();
    }

    @Test
    void getStudentDetail_WithNonExistentId_ShouldThrowException() {
        // when & then
        assertThrows(NoSuchElementException.class, () -> {
            studentService.getStudentDetail(999L);
        });
    }

    @Test
    void editStudent_WithNonExistentId_ShouldThrowException() {
        // given
        Student student = new Student(999L, "존재하지않는학생", 5, 10);

        // when & then
        assertThrows(NoSuchElementException.class, () -> {
            studentService.editStudentInfo(student);
        });
    }

    @Test
    void withdrawStudent_WithNonExistentId_ShouldThrowException() {
        // when & then
        assertThrows(NoSuchElementException.class, () -> {
            studentService.withdrawStudent(999L);
        });
    }

    @Test
    void getTeacherDetail_WithNonExistentId_ShouldThrowException() {
        // when & then
        assertThrows(NoSuchElementException.class, () -> {
            teacherService.getTeacherDetail(999L);
        });
    }

    @Test
    void editTeacher_WithNonExistentId_ShouldThrowException() {
        // given
        Teacher teacher = new Teacher(999L, "존재하지않는선생", "피아노");

        // when & then
        assertThrows(NoSuchElementException.class, () -> {
            teacherService.editTeacherInfo(teacher);
        });
    }

    @Test
    void withdrawTeacher_WithNonExistentId_ShouldThrowException() {
        // when & then
        assertThrows(NoSuchElementException.class, () -> {
            teacherService.withdrawTeacher(999L);
        });
    }

    @Test
    void getLessonInfo_WithNonExistentId_ShouldThrowException() {
        // when & then
        assertThrows(NoSuchElementException.class, () -> {
            lessonInfoService.getLessonInfoDetail(999L);
        });
    }

    @Test
    void editLessonInfo_WithNonExistentId_ShouldThrowException() {
        // given
        LessonInfo lessonInfo = new LessonInfo(999L, 1L, 1L, LocalDateTime.now(), false);

        // when & then
        assertThrows(NoSuchElementException.class, () -> {
            lessonInfoService.editLessonInfo(lessonInfo);
        });
    }

    @Test
    void deleteLessonInfo_WithNonExistentId_ShouldThrowException() {
        // when & then
        assertThrows(NoSuchElementException.class, () -> {
            lessonInfoService.withdrawLessonInfo(999L); // 실제 메서드명 사용
        });
    }

    @Test
    void getStudentByName_WithNonExistentName_ShouldThrowException() {
        // when & then
        assertThrows(NoSuchElementException.class, () -> {
            studentService.getStudentByName("존재하지않는이름");
        });
    }

    @Test
    void getTeacherByName_WithNonExistentName_ShouldThrowException() {
        // when & then
        assertThrows(NoSuchElementException.class, () -> {
            teacherService.getTeacherByName("존재하지않는선생");
        });
    }
}
