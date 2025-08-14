package com.ZandhiDokkie.minuet.repository;

import com.ZandhiDokkie.minuet.repository.jdbc.JdbcTeacherRepository;
import com.ZandhiDokkie.minuet.domain.Teacher;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class JdbcTeacherRepositoryTest {
    
    @Autowired
    JdbcTeacherRepository teacherRepository;
    
    Teacher teacher1;
    Teacher teacher2;

    @BeforeEach
    void beforeEach(){
        teacher1 = new Teacher(null, "조선생", "피아노");
        teacher2 = new Teacher(null, "송선생", "첼로");
    }

    @AfterEach
    void afterEach(){
        teacherRepository.clearStore();
    }

    @Test
    void getLengthTest(){
        teacherRepository.createTeacher(teacher1);
        teacherRepository.createTeacher(teacher2);
        
        Integer length = teacherRepository.getLength();
        Assertions.assertEquals(2, length);
    }

    @Test
    void getTeacherTest(){
        Optional<Teacher> created = teacherRepository.createTeacher(teacher1);
        Assertions.assertTrue(created.isPresent());
        
        Optional<Teacher> found = teacherRepository.getTeacher(created.get().getId());
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(teacher1.getName(), found.get().getName());
        Assertions.assertEquals(teacher1.getSubject(), found.get().getSubject());
    }

    @Test
    void getTeachersTest(){
        teacherRepository.createTeacher(teacher1);
        teacherRepository.createTeacher(teacher2);
        
        List<Teacher> teachers = teacherRepository.getTeachers();
        Assertions.assertEquals(2, teachers.size());
    }

    @Test
    void createTeacherTest(){
        Optional<Teacher> created1 = teacherRepository.createTeacher(teacher1);
        Optional<Teacher> created2 = teacherRepository.createTeacher(teacher2);
        
        Assertions.assertTrue(created1.isPresent());
        Assertions.assertTrue(created2.isPresent());
        Assertions.assertNotNull(created1.get().getId());
        Assertions.assertNotNull(created2.get().getId());
        Assertions.assertEquals(2, teacherRepository.getLength());
    }

    @Test
    void updateTeacherTest(){
        Optional<Teacher> created = teacherRepository.createTeacher(teacher1);
        Assertions.assertTrue(created.isPresent());
        
        Teacher toUpdate = created.get();
        toUpdate.setName("변경된이름");
        toUpdate.setSubject("바이올린");
        
        Optional<Teacher> updated = teacherRepository.updateTeacher(toUpdate);
        Assertions.assertTrue(updated.isPresent());
        Assertions.assertEquals("변경된이름", updated.get().getName());
        Assertions.assertEquals("바이올린", updated.get().getSubject());
    }

    @Test
    void deleteTeacherTest(){
        Optional<Teacher> created = teacherRepository.createTeacher(teacher1);
        Assertions.assertTrue(created.isPresent());
        
        Optional<Teacher> deleted = teacherRepository.deleteTeacher(created.get().getId());
        Assertions.assertTrue(deleted.isPresent());
        Assertions.assertEquals(0, teacherRepository.getLength());
    }

    @Test
    void clearStoreTest(){
        teacherRepository.createTeacher(teacher1);
        teacherRepository.createTeacher(teacher2);
        Assertions.assertEquals(2, teacherRepository.getLength());
        
        teacherRepository.clearStore();
        Assertions.assertEquals(0, teacherRepository.getLength());
    }

    @Test
    void findByNameTest(){
        teacherRepository.createTeacher(teacher1);
        
        Optional<Teacher> found = teacherRepository.findByName(teacher1.getName());
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(teacher1.getName(), found.get().getName());
    }

    @Test
    void findByNameNotFoundTest(){
        Optional<Teacher> found = teacherRepository.findByName("존재하지않는이름");
        Assertions.assertTrue(found.isEmpty());
    }

}