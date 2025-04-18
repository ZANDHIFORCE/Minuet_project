package com.ZandhiDokkie.minuet.service;

import com.ZandhiDokkie.minuet.domain.Teacher;
import com.ZandhiDokkie.minuet.repository.memory.MemoryTeacherRepository;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.NoSuchElementException;

public class TeacherServiceTest {
    private TeacherService teacherService;
    Teacher teacher1;
    Teacher teacher2;

    @BeforeEach
    void beforeEach() {
        teacherService = new TeacherService(new MemoryTeacherRepository());
        teacher1 = new Teacher(null, "조선생", "피아노");
        teacher2 = new Teacher(null, "송선생", "첼로");
    }

    @AfterEach
    void afterEach() {
        teacherService.clearAllTeachers();
    }

    @Test
    void getTeacherCountsTest() {
        teacherService.registerTeacher(teacher1);
        teacherService.registerTeacher(teacher2);
        Assertions.assertEquals(2, teacherService.getTeacherCounts());
    }

    @Test
    void getTeacherDetailTest() {
        teacherService.registerTeacher(teacher1);
        Teacher found = teacherService.getStudentDetail(teacher1.getId());
        Assertions.assertEquals(teacher1.toString(), found.toString());
    }

    @Test
    void getTeacherDetail_shouldThrowException_whenTeacherDoesNotExist() {
        NoSuchElementException e = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> teacherService.getStudentDetail(999L)
        );
        Assertions.assertEquals("해당 아이디의 선생님은 없습니다.", e.getMessage());
    }

    @Test
    void getAllTeachersTest() {
        teacherService.registerTeacher(teacher1);
        teacherService.registerTeacher(teacher2);
        List<Teacher> list = teacherService.getAllTeachers();
        Assertions.assertEquals(2, list.size());
    }

    @Test
    void registerTeacherTest() {
        teacherService.registerTeacher(teacher1);
        Teacher found = teacherService.getStudentDetail(teacher1.getId());
        Assertions.assertEquals(teacher1.toString(), found.toString());
    }

    @Test
    void editTeacherInfoTest() {
        teacherService.registerTeacher(teacher1);
        teacher1.setName("조선왕국");
        Teacher edited = teacherService.editTeacherInfo(teacher1);
        Assertions.assertEquals("조선왕국", edited.getName());
    }

    @Test
    void editTeacherInfo_shouldThrowException_whenTeacherDoesNotExist() {
        NoSuchElementException e = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> teacherService.editTeacherInfo(teacher1)
        );
        Assertions.assertEquals("해당 id의 teacher은 존재하지 않습니다.", e.getMessage());
    }

    @Test
    void withdrawTeacherTest() {
        teacherService.registerTeacher(teacher1);
        teacherService.withdrawTeacher(teacher1.getId());
        Assertions.assertEquals(0, teacherService.getTeacherCounts());
    }

    @Test
    void withdrawTeacher_shouldThrowException_whenTeacherDoesNotExist() {
        NoSuchElementException e = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> teacherService.withdrawTeacher(123L)
        );
        Assertions.assertEquals("해당 id의 taecher가 존재하지 않습니다.", e.getMessage());
    }

    @Test
    void clearAllTeachersTest() {
        teacherService.registerTeacher(teacher1);
        teacherService.registerTeacher(teacher2);
        teacherService.clearAllTeachers();
        Assertions.assertEquals(0, teacherService.getTeacherCounts());
    }

    @Test
    void getTeacherByNameTest() {
        teacherService.registerTeacher(teacher1);
        Teacher found = teacherService.getTeacherByName("조선생");
        Assertions.assertEquals(teacher1.toString(), found.toString());
    }

    @Test
    void getTeacherByName_shouldThrowException_whenTeacherDoesNotExist() {
        NoSuchElementException e = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> teacherService.getTeacherByName("없는이름")
        );
        Assertions.assertEquals("해당 이름의 taecher이 존재하지 않습니다.", e.getMessage());
    }
}
