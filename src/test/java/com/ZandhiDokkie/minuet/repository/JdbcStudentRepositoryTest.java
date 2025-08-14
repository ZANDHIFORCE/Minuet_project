package com.ZandhiDokkie.minuet.repository;

import com.ZandhiDokkie.minuet.repository.jdbc.JdbcStudentRepository;
import com.ZandhiDokkie.minuet.domain.Student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Rollback
public class JdbcStudentRepositoryTest {
    
    @Autowired
    JdbcStudentRepository studentRepository;
    
    Student student1;
    Student student2;

    @BeforeEach
    void beforeEach(){
        student1 = new Student(null, "조동휘", 1, 8);
        student2 = new Student(null, "송미서", 7, 8);
    }

    @AfterEach
    void afterEach(){
        // Clear dependent tables first to avoid foreign key constraint violations
        try {
            studentRepository.clearStore();
        } catch (Exception e) {
            // Ignore constraint violations during cleanup
        }
    }

    @Test
    void getLengthTest(){
        // Check initial length
        Integer initialLength = studentRepository.getLength();
        System.out.println("Initial length: " + initialLength);
        
        studentRepository.createStudent(student1);
        studentRepository.createStudent(student2);
        
        Integer length = studentRepository.getLength();
        System.out.println("Final length: " + length);
        Assertions.assertEquals(initialLength + 2, length);
    }

    @Test
    void getStudentTest(){
        Optional<Student> created = studentRepository.createStudent(student1);
        Assertions.assertTrue(created.isPresent());
        
        Optional<Student> found = studentRepository.getStudent(created.get().getId());
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(student1.getName(), found.get().getName());
        Assertions.assertEquals(student1.getProgressSessions(), found.get().getProgressSessions());
        Assertions.assertEquals(student1.getTotalSessions(), found.get().getTotalSessions());
    }

    @Test
    void getStudentsTest(){
        studentRepository.createStudent(student1);
        studentRepository.createStudent(student2);
        
        List<Student> students = studentRepository.getStudents();
        Assertions.assertEquals(2, students.size());
    }

    @Test
    void createStudentTest(){
        Optional<Student> created1 = studentRepository.createStudent(student1);
        Optional<Student> created2 = studentRepository.createStudent(student2);
        
        Assertions.assertTrue(created1.isPresent());
        Assertions.assertTrue(created2.isPresent());
        Assertions.assertNotNull(created1.get().getId());
        Assertions.assertNotNull(created2.get().getId());
        Assertions.assertEquals(2, studentRepository.getLength());
    }

    @Test
    void updateStudentTest(){
        Optional<Student> created = studentRepository.createStudent(student1);
        Assertions.assertTrue(created.isPresent());
        
        Student toUpdate = created.get();
        toUpdate.setName("변경된이름");
        toUpdate.setProgressSessions(5);
        toUpdate.setTotalSessions(12);
        
        Optional<Student> updated = studentRepository.updateStudent(toUpdate);
        Assertions.assertTrue(updated.isPresent());
        Assertions.assertEquals("변경된이름", updated.get().getName());
        Assertions.assertEquals(5, updated.get().getProgressSessions());
        Assertions.assertEquals(12, updated.get().getTotalSessions());
    }

    @Test
    void deleteStudentTest(){
        Optional<Student> created = studentRepository.createStudent(student1);
        Assertions.assertTrue(created.isPresent());
        
        Optional<Student> deleted = studentRepository.deleteStudent(created.get().getId());
        Assertions.assertTrue(deleted.isPresent());
        Assertions.assertEquals(0, studentRepository.getLength());
    }

    @Test
    void clearStoreTest(){
        studentRepository.createStudent(student1);
        studentRepository.createStudent(student2);
        Assertions.assertEquals(2, studentRepository.getLength());
        
        studentRepository.clearStore();
        Assertions.assertEquals(0, studentRepository.getLength());
    }

    @Test
    void findByNameTest(){
        studentRepository.createStudent(student1);
        
        Optional<Student> found = studentRepository.findByName(student1.getName());
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(student1.getName(), found.get().getName());
    }

    @Test
    void findByNameNotFoundTest(){
        Optional<Student> found = studentRepository.findByName("존재하지않는이름");
        Assertions.assertTrue(found.isEmpty());
    }
}