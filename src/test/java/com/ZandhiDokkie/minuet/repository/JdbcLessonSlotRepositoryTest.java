package com.ZandhiDokkie.minuet.repository;

import com.ZandhiDokkie.minuet.repository.jdbc.JdbcLessonSlotRepository;
import com.ZandhiDokkie.minuet.domain.LessonSlot;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class JdbcLessonSlotRepositoryTest {
    
    @Autowired
    JdbcLessonSlotRepository lessonSlotRepository;
    
    LessonSlot lessonSlot1;
    LessonSlot lessonSlot2;

    @BeforeEach
    void beforeEach(){
        lessonSlot1 = new LessonSlot(null, 1L, 2L, 0, LocalTime.of(13, 0));
        lessonSlot2 = new LessonSlot(null, 1L, 2L, 2, LocalTime.of(13, 0));
    }

    @AfterEach
    void afterEach(){
        lessonSlotRepository.clearStore();
    }

    @Test
    void getLengthTest(){
        lessonSlotRepository.createLessonSlot(lessonSlot1);
        lessonSlotRepository.createLessonSlot(lessonSlot2);
        
        Integer length = lessonSlotRepository.getLength();
        Assertions.assertEquals(2, length);
    }

    @Test
    void getLessonSlotTest(){
        Optional<LessonSlot> created = lessonSlotRepository.createLessonSlot(lessonSlot1);
        Assertions.assertTrue(created.isPresent());
        
        Optional<LessonSlot> found = lessonSlotRepository.getLessonSlot(created.get().getId());
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(lessonSlot1.getTeacherId(), found.get().getTeacherId());
        Assertions.assertEquals(lessonSlot1.getStudentId(), found.get().getStudentId());
        Assertions.assertEquals(lessonSlot1.getDay(), found.get().getDay());
        Assertions.assertEquals(lessonSlot1.getTime(), found.get().getTime());
    }

    @Test
    void getLessonSlotsTest(){
        lessonSlotRepository.createLessonSlot(lessonSlot1);
        lessonSlotRepository.createLessonSlot(lessonSlot2);
        
        List<LessonSlot> lessonSlots = lessonSlotRepository.getLessonSlots();
        Assertions.assertEquals(2, lessonSlots.size());
    }

    @Test
    void createLessonSlotTest(){
        Optional<LessonSlot> created1 = lessonSlotRepository.createLessonSlot(lessonSlot1);
        Optional<LessonSlot> created2 = lessonSlotRepository.createLessonSlot(lessonSlot2);
        
        Assertions.assertTrue(created1.isPresent());
        Assertions.assertTrue(created2.isPresent());
        Assertions.assertNotNull(created1.get().getId());
        Assertions.assertNotNull(created2.get().getId());
        Assertions.assertEquals(2, lessonSlotRepository.getLength());
    }

    @Test
    void updateLessonSlotTest(){
        Optional<LessonSlot> created = lessonSlotRepository.createLessonSlot(lessonSlot1);
        Assertions.assertTrue(created.isPresent());
        
        LessonSlot toUpdate = created.get();
        toUpdate.setDay(5);
        toUpdate.setTime(LocalTime.of(15, 30));
        
        Optional<LessonSlot> updated = lessonSlotRepository.updateLessonSlot(toUpdate);
        Assertions.assertTrue(updated.isPresent());
        Assertions.assertEquals(5, updated.get().getDay());
        Assertions.assertEquals(LocalTime.of(15, 30), updated.get().getTime());
    }

    @Test
    void deleteLessonSlotTest(){
        Optional<LessonSlot> created = lessonSlotRepository.createLessonSlot(lessonSlot1);
        Assertions.assertTrue(created.isPresent());
        
        Optional<LessonSlot> deleted = lessonSlotRepository.deleteLessonSlot(created.get().getId());
        Assertions.assertTrue(deleted.isPresent());
        Assertions.assertEquals(0, lessonSlotRepository.getLength());
    }

    @Test
    void clearStoreTest(){
        lessonSlotRepository.createLessonSlot(lessonSlot1);
        lessonSlotRepository.createLessonSlot(lessonSlot2);
        Assertions.assertEquals(2, lessonSlotRepository.getLength());
        
        lessonSlotRepository.clearStore();
        Assertions.assertEquals(0, lessonSlotRepository.getLength());
    }

    @Test
    void findByTeacherIdTest(){
        lessonSlotRepository.createLessonSlot(lessonSlot1);
        
        List<LessonSlot> lessonSlots = lessonSlotRepository.findByTeacherId(lessonSlot1.getTeacherId());
        Assertions.assertEquals(1, lessonSlots.size());
        Assertions.assertEquals(lessonSlot1.getTeacherId(), lessonSlots.get(0).getTeacherId());
    }

    @Test
    void findByStudentIdTest(){
        lessonSlotRepository.createLessonSlot(lessonSlot1);
        
        List<LessonSlot> lessonSlots = lessonSlotRepository.findByStudentId(lessonSlot1.getStudentId());
        Assertions.assertEquals(1, lessonSlots.size());
        Assertions.assertEquals(lessonSlot1.getStudentId(), lessonSlots.get(0).getStudentId());
    }

}