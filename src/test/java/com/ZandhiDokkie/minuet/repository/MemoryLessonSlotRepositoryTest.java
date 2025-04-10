package com.ZandhiDokkie.minuet.repository;
import com.ZandhiDokkie.minuet.domain.LessonSlot;
import com.ZandhiDokkie.minuet.repository.memory.MemoryLessonSlotRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class MemoryLessonSlotRepositoryTest {
    MemoryLessonSlotRepository repo;
    LessonSlot lessonSlot1;
    LessonSlot lessonSlot2;

    @BeforeEach
    void beforeEach(){
        repo = new MemoryLessonSlotRepository();
        lessonSlot1 = new LessonSlot(1L, 1L, 2L, 0, LocalTime.of(13,00));
        lessonSlot2 = new LessonSlot(2L, 1L, 2L, 2, LocalTime.of(13,00));
    }

    @AfterEach
    void afterEach(){
        repo.clearStore();
    }

    @Test
    void saveLoadTest(){
        //given
        String pathname = "src/test/resources/data/lessonSlots.json";
        repo.createLessonSlot(lessonSlot1);
        repo.createLessonSlot(lessonSlot2);
        //when
        repo.saveToFile(pathname);
        repo.clearStore();
        repo.loadFromFile(pathname);
        //then
        for(LessonSlot l: repo.getLessonSlots()){
            if(l.getId().equals(lessonSlot1.getId()))
                Assertions.assertEquals(lessonSlot1.toString(), l.toString());
            else
                Assertions.assertEquals(lessonSlot2.toString(), l.toString());
        }
    }

    //implementation
    @Test
    void getLengthTest(){
        Assertions.assertEquals(0, repo.getLength());
    }

    @Test
    void getLessonSlotTest(){
        //given
        repo.createLessonSlot(lessonSlot1);
        //when
        repo.getLessonSlot(1L)
                .ifPresent(l->{
                    Assertions.assertEquals(lessonSlot1.toString(),l.toString());
                });
        //then
    }

    @Test
    void getLessonSlotsTest(){
        //given
        repo.createLessonSlot(lessonSlot1);
        repo.createLessonSlot(lessonSlot1);
        //when
        List<LessonSlot> lessonSlotList = repo.getLessonSlots();
        //then
        for(LessonSlot l:lessonSlotList){
            if(l.getId() == lessonSlot1.getId())
                Assertions.assertEquals(lessonSlot1.toString(), l.toString());
            else
                Assertions.assertEquals(lessonSlot1.toString(), l.toString());
        }
    }

    @Test
    void createLessonSlotTest(){
        //given
        //when
        repo.createLessonSlot(lessonSlot1);
        //then
        repo.getLessonSlot(1L)
                .ifPresent(l->{Assertions.assertEquals(lessonSlot1.toString(),l.toString());});
    }

    @Test
    void updateLessonSlotTest(){
        //given
        repo.createLessonSlot(lessonSlot1);
        Optional<LessonSlot> lessonSlotOptional = repo.getLessonSlot(1L);
        //when
        lessonSlotOptional
                .ifPresent(l->{
                    l.setTime(LocalTime.of(15,0));
                    repo.updateLessonSlot(l);
                });
        //then
        repo.getLessonSlot(1L).ifPresent(l->{
            Assertions.assertEquals(LocalTime.of(15,0) ,l.getTime());
        });
    }

    @Test
    void deleteLessonSlotTest(){
        //given
        repo.createLessonSlot(lessonSlot1);
        //when
        repo.deleteLessonSlot(lessonSlot1.getId());
        //then
        Assertions.assertEquals(0,repo.getLength());
    }

    @Test
    void clearStoreTest(){
        //given
        repo.createLessonSlot(lessonSlot1);
        //when
        repo.clearStore();
        //then
        Assertions.assertEquals(0,repo.getLength());
    }

}
