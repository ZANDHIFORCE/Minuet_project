package com.ZandhiDokkie.minuet.repository;

import com.ZandhiDokkie.minuet.domain.Student;
import com.ZandhiDokkie.minuet.repository.memory.MemoryStudentRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

public class MemoryStudentRepositoryTest {
    MemoryStudentRepository repo;
    Student student1;
    Student student2;

    @BeforeEach
    void beforeEach(){
        repo = new MemoryStudentRepository();
        student1 = new Student(1L, "조동휘", 1,8);
        student2 = new Student(2L, "송미서", 7,8);
    }

    @AfterEach
    void afterEach(){
        repo.clearStore();
    }

    @Test
    void SaveLoadTest(){
        //given
        repo.createStudent(student1);
        repo.createStudent(student2);
        String pathname= "src/test/resources/data/testStudents.json";
        List<Student> stduentList = repo.getStudents();
        //when
        repo.saveToFile(pathname);
        repo.clearStore();
        repo.loadFromFile(pathname);
        //
        for(Student student:stduentList){
            repo.getStudent(student.getId()).ifPresent(s->{
                Assertions.assertEquals(student.toString(), s.toString());
            });
        }
    }

    @Test
    void realJsonLoadSaveTest(){
        //given
        repo.loadFromFile("src/main/resources/data/students.json");
        List<Student> studentList = repo.getStudents();
        repo.saveToFile("src/test/resources/data/students.json");
        repo.clearStore();
        //when
        repo.loadFromFile("src/test/resources/data/students.json");
        //then
        for(Student student:studentList){
            repo.getStudent(student.getId()).ifPresent(s->{
                Assertions.assertEquals(student.toString(),s.toString());
            });
        }
    }

    @Test
    void getLengthTest(){
        //given
        repo.createStudent(student1);
        repo.createStudent(student2);
        //when
        Integer length = repo.getLength();
        //then
        Assertions.assertEquals(2,length);
    }

    @Test
    void getStudentTest(){
        //given
        repo.createStudent(student1);
        //when
        Optional<Student> studentOptional = repo.getStudent(student1.getId());
        //then
        studentOptional.ifPresent(s->{
            Assertions.assertEquals(student1.toString(), s.toString());
        });
    }

    @Test
    void getStudentsTest(){
        //given
        repo.createStudent(student1);
        repo.createStudent(student2);
        //when
        List<Student> studentList = repo.getStudents();
        //then
        for(Student student:studentList){
            repo.getStudent(student.getId()).ifPresent(s->{
                Assertions.assertEquals(s.toString(), student.toString());
            });
        }
    }

    @Test
    void createStudentTest(){
        //given
        //when
        repo.createStudent(student1);
        //then
        repo.getStudent(student1.getId())
                .ifPresent(s->{
                    Assertions.assertEquals(student1.toString(), s.toString());
                });
    }

    @Test
    void updateStudentTest(){
        //given
        repo.createStudent(student1);
        student1.setName("조동키");
        //when
        repo.updateStudent(student1);
        //then
        repo.getStudent(student1.getId())
                .ifPresent(s->{
                    Assertions.assertEquals("조동키", s.getName());
                });
    }

    @Test
    void deleteStudentTest(){
        //given
        repo.createStudent(student1);
        //when
        repo.deleteStudent(student1.getId());
        //then
        Assertions.assertEquals(0,repo.getLength());
    }

    @Test
    void clearStoreTest(){
        //given
        repo.createStudent(student1);
        repo.createStudent(student2);
        //when
        repo.clearStore();
        //then
        Assertions.assertEquals(0,repo.getLength());
    }


}
