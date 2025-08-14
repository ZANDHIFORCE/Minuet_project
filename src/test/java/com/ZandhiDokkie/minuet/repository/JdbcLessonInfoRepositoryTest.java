package com.ZandhiDokkie.minuet.repository;

import com.ZandhiDokkie.minuet.repository.jdbc.JdbcLessonInfoRepository;
import com.ZandhiDokkie.minuet.repository.interfaces.TeacherRepository;
import com.ZandhiDokkie.minuet.repository.interfaces.StudentRepository;
import com.ZandhiDokkie.minuet.domain.LessonInfo;
import com.ZandhiDokkie.minuet.domain.Teacher;
import com.ZandhiDokkie.minuet.domain.Student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class JdbcLessonInfoRepositoryTest {
    
    @Autowired
    JdbcLessonInfoRepository lessonInfoRepository;
    
    @Autowired
    TeacherRepository teacherRepository;
    
    @Autowired
    StudentRepository studentRepository;
    
    LessonInfo lessonInfo1;
    LessonInfo lessonInfo2;
    Teacher createdTeacher1;
    Teacher createdTeacher2;
    Student createdStudent1;
    Student createdStudent2;

    @BeforeEach
    void beforeEach(){
        // Create test teachers
        Teacher teacher1 = new Teacher(null, "Test Teacher 1", "Math");
        Teacher teacher2 = new Teacher(null, "Test Teacher 2", "English");
        createdTeacher1 = teacherRepository.createTeacher(teacher1).orElseThrow();
        createdTeacher2 = teacherRepository.createTeacher(teacher2).orElseThrow();
        
        // Create test students
        Student student1 = new Student(null, "Test Student 1", 0, 0);
        Student student2 = new Student(null, "Test Student 2", 0, 0);
        createdStudent1 = studentRepository.createStudent(student1).orElseThrow();
        createdStudent2 = studentRepository.createStudent(student2).orElseThrow();
        
        lessonInfo1 = new LessonInfo(null, createdTeacher1.getId(), createdStudent1.getId(), LocalDateTime.of(2025,4,10,13,0), false);
        lessonInfo2 = new LessonInfo(null, createdTeacher2.getId(), createdStudent2.getId(), LocalDateTime.of(2025,4,10,14,0), false);
    }

    @AfterEach
    void afterEach(){
        lessonInfoRepository.clearStore();
        teacherRepository.clearStore();
        studentRepository.clearStore();
    }

    @Test
    void getLengthTest(){
        Assertions.assertEquals(0, lessonInfoRepository.getLength());
    }

    @Test
    void getLessonInfoTest(){
        Optional<LessonInfo> created = lessonInfoRepository.createLessonInfo(lessonInfo1);
        Assertions.assertTrue(created.isPresent());
        
        Optional<LessonInfo> found = lessonInfoRepository.getLessonInfo(created.get().getId());
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(lessonInfo1.getStudentId(), found.get().getStudentId());
        Assertions.assertEquals(lessonInfo1.getTeacherId(), found.get().getTeacherId());
        Assertions.assertEquals(lessonInfo1.getDateTime(), found.get().getDateTime());
        Assertions.assertEquals(lessonInfo1.isCompleted(), found.get().isCompleted());
    }

    @Test
    void getLessonInfos(){
        lessonInfoRepository.createLessonInfo(lessonInfo1);
        lessonInfoRepository.createLessonInfo(lessonInfo2);
        
        List<LessonInfo> lessonInfoList = lessonInfoRepository.getLessonInfos();
        Assertions.assertEquals(2, lessonInfoList.size());
    }

    @Test
    void createLessonInfoTest(){
        Optional<LessonInfo> created1 = lessonInfoRepository.createLessonInfo(lessonInfo1);
        Optional<LessonInfo> created2 = lessonInfoRepository.createLessonInfo(lessonInfo2);
        
        Assertions.assertTrue(created1.isPresent());
        Assertions.assertTrue(created2.isPresent());
        Assertions.assertNotNull(created1.get().getId());
        Assertions.assertNotNull(created2.get().getId());
        Assertions.assertEquals(2, lessonInfoRepository.getLength());
    }

    @Test
    void updateLessonInfo(){
        Optional<LessonInfo> created = lessonInfoRepository.createLessonInfo(lessonInfo1);
        Assertions.assertTrue(created.isPresent());
        
        LessonInfo toUpdate = created.get();
        toUpdate.setCompleted(true);
        
        Optional<LessonInfo> updated = lessonInfoRepository.updateLessonInfo(toUpdate);
        Assertions.assertTrue(updated.isPresent());
        Assertions.assertTrue(updated.get().isCompleted());
    }

    @Test
    void deleteLessonInfoTest(){
        Optional<LessonInfo> created = lessonInfoRepository.createLessonInfo(lessonInfo1);
        Assertions.assertTrue(created.isPresent());
        
        Optional<LessonInfo> deleted = lessonInfoRepository.deleteLessonInfo(created.get().getId());
        Assertions.assertTrue(deleted.isPresent());
        Assertions.assertEquals(0, lessonInfoRepository.getLength());
    }

    @Test
    void clearStoreTest(){
        lessonInfoRepository.createLessonInfo(lessonInfo1);
        lessonInfoRepository.createLessonInfo(lessonInfo2);
        Assertions.assertEquals(2, lessonInfoRepository.getLength());
        
        lessonInfoRepository.clearStore();
        Assertions.assertEquals(0, lessonInfoRepository.getLength());
    }

    @Test
    void findByStudentIdTest(){
        lessonInfoRepository.createLessonInfo(lessonInfo1);
        
        List<LessonInfo> lessonInfoList = lessonInfoRepository.findByStudentId(lessonInfo1.getStudentId());
        Assertions.assertEquals(1, lessonInfoList.size());
        Assertions.assertEquals(lessonInfo1.getStudentId(), lessonInfoList.get(0).getStudentId());
    }

    @Test
    void findByTeacherIdTest(){
        lessonInfoRepository.createLessonInfo(lessonInfo1);
        
        List<LessonInfo> lessonInfoList = lessonInfoRepository.findByTeacherId(lessonInfo1.getTeacherId());
        Assertions.assertEquals(1, lessonInfoList.size());
        Assertions.assertEquals(lessonInfo1.getTeacherId(), lessonInfoList.get(0).getTeacherId());
    }
}