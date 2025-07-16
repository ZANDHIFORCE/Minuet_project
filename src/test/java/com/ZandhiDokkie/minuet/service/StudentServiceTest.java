package com.ZandhiDokkie.minuet.service;

import com.ZandhiDokkie.minuet.domain.Student;
import com.ZandhiDokkie.minuet.repository.memory.MemoryStudentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

public class StudentServiceTest {
    private StudentService studentService;
    Student student1;
    Student student2;

    @BeforeEach
    void beforeEach(){
        studentService = new StudentService(new MemoryStudentRepository());
        student1 = new Student(null,"조동휘", 8, 12);
        student2 = new Student(null, "송미서", 11, 12);
    }

    @AfterEach
    void afterEach(){
        studentService.clearAllStudents();
    }

    @Test
    void getStudentCountsTest(){
        //given
        studentService.registerStudent(student1);
        studentService.registerStudent(student2);
        //when
        int studentNumber = studentService.getStudentCounts();
        //then
        Assertions.assertEquals(2, studentNumber);
    }

    @Test
    void getStudentDetailTest(){
        //given
        studentService.registerStudent(student1);
        //when
        Student student = studentService.getStudentDetail(student1.getId());
        //then
        Assertions.assertEquals(student1.toString(), student.toString());
    }

    @Test
    void getStudentDetail_shouldThrowException_whenStudentDoesNotExist(){
        //when & then
        NoSuchElementException e = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> studentService.getStudentDetail(999L)
        );
        Assertions.assertEquals("해당학생이 존재 하지 않습니다.", e.getMessage());
    }

    @Test
    void getAllStudentsTest(){
        //given
        studentService.registerStudent(student1);
        studentService.registerStudent(student2);
        //when
        List<Student> studentList = studentService.getAllStudents();
        //then
        Assertions.assertEquals(2, studentService.getStudentCounts());
        for(Student student:studentList){
            Assertions.assertEquals(
                    studentService.getStudentDetail(student.getId()).toString(),
                    student.toString()
            );
        }
    }

    @Test
    void registerStudentTest(){
        //when
        studentService.registerStudent(student1);
        //then
        Assertions.assertEquals(
                student1.toString(),
                studentService.getStudentDetail(student1.getId()).toString()
        );
    }

    @Test
    void editStudentInfoTest(){
        //given
        studentService.registerStudent(student1);
        student1.setName("조동서");
        //when
        studentService.editStudentInfo(student1);
        //then
        Assertions.assertEquals(
                student1.toString(),
                studentService.getStudentDetail(student1.getId()).toString()
        );
    }

    @Test
    void editStudentInfo_shouldThrowException_whenStudentDoesNotExist(){
        NoSuchElementException e = Assertions.assertThrows(
                NoSuchElementException.class,
                ()-> studentService.editStudentInfo(student1)
        );
        Assertions.assertEquals("해당 학생id는 존재 하지 않습니다.", e.getMessage());
    }

    @Test
    void withdrawStudentTest(){
        //given
        studentService.registerStudent(student1);
        //when
        studentService.withdrawStudent(student1.getId());
        //then
        Assertions.assertEquals(0, studentService.getStudentCounts());
    }

    @Test
    void withdrawStudentTest_shouldThrowException_whenStudentDeosNotExist(){
        NoSuchElementException e= Assertions.assertThrows(
                NoSuchElementException.class,
                ()->studentService.withdrawStudent(1L)
        );
        Assertions.assertEquals("해당 학생id는 존재 하지 않습니다.", e.getMessage());
    }

    @Test
    void clearAllStudentsTest(){
        //given
        studentService.registerStudent(student1);
        studentService.registerStudent(student2);
        //when
        studentService.clearAllStudents();
        //then
        Assertions.assertEquals(0, studentService.getStudentCounts());
    }

    @Test
    void getStudentByNameTest(){
        //given
        studentService.registerStudent(student1);
        //when
        Student student = studentService.getStudentByName(student1.getName());
        //then
        Assertions.assertEquals(student1.toString(), student.toString());
    }

}
