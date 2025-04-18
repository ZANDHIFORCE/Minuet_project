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
    MemoryLessonSlotRepository lessonSlotRepository;
    LessonSlot lessonSlot1;
    LessonSlot lessonSlot2;

    @BeforeEach
    void beforeEach(){
        lessonSlotRepository = new MemoryLessonSlotRepository();
        lessonSlot1 = new LessonSlot(null, 1L, 2L, 0, LocalTime.of(13,00));
        lessonSlot2 = new LessonSlot(null, 1L, 2L, 2, LocalTime.of(13,00));
    }

    @AfterEach
    void afterEach(){
        lessonSlotRepository.clearStore();
    }

    @Test
    void saveLoadTest(){
        //given
        String pathname = "src/test/resources/data/testLessonSlots.json";
        lessonSlotRepository.createLessonSlot(lessonSlot1);
        lessonSlotRepository.createLessonSlot(lessonSlot2);
        //when
        lessonSlotRepository.saveToFile(pathname);
        lessonSlotRepository.clearStore();
        lessonSlotRepository.loadFromFile(pathname);
        //then
        for(LessonSlot l: lessonSlotRepository.getLessonSlots()){
            if(l.getId().equals(lessonSlot1.getId()))
                Assertions.assertEquals(lessonSlot1.toString(), l.toString());
            else
                Assertions.assertEquals(lessonSlot2.toString(), l.toString());
        }
    }

    @Test
    void realJsonLoadSaveTest(){
        lessonSlotRepository.loadFromFile("src/main/resources/data/lessonSlots.json");
        List<LessonSlot> LessonSlotList1 = lessonSlotRepository.getLessonSlots();
        lessonSlotRepository.saveToFile("src/test/resources/data/lessonSlots.json");
        lessonSlotRepository.clearStore();
        lessonSlotRepository.loadFromFile("src/test/resources/data/lessonSlots.json");
        for(LessonSlot l1: LessonSlotList1){
            lessonSlotRepository.getLessonSlot(l1.getId())
                    .ifPresent(l2->{
                        Assertions.assertEquals(l1.toString(),l2.toString());
                    });
        }
    }

    //implementation
    @Test
    void getLengthTest(){
        Assertions.assertEquals(0, lessonSlotRepository.getLength());
    }

    @Test
    void getLessonSlotTest(){
        //given
        lessonSlotRepository.createLessonSlot(lessonSlot1);
        //when
        lessonSlotRepository.getLessonSlot(lessonSlot1.getId())
                .ifPresent(l->{
                    Assertions.assertEquals(lessonSlot1.toString(),l.toString());
                });
        //then
    }

    @Test
    void getLessonSlotsTest(){
        //given
        lessonSlotRepository.createLessonSlot(lessonSlot1);
        lessonSlotRepository.createLessonSlot(lessonSlot1);
        //when
        List<LessonSlot> lessonSlotList = lessonSlotRepository.getLessonSlots();
        //then
        for(LessonSlot l:lessonSlotList){
            if(l.getId().equals(lessonSlot1.getId()))
                Assertions.assertEquals(lessonSlot1.toString(), l.toString());
            else
                Assertions.assertEquals(lessonSlot1.toString(), l.toString());
        }
    }

    @Test
    void createLessonSlotTest(){
        //when
        Optional<LessonSlot> lessonSlotOptional = lessonSlotRepository.createLessonSlot(lessonSlot1);
        //then
        lessonSlot1.setId(
                lessonSlotOptional
                        .map(LessonSlot::getId)
                        .orElse(null)
                );
        lessonSlotRepository.getLessonSlot(lessonSlot1.getId())
                .ifPresentOrElse(l->{Assertions.assertEquals(lessonSlot1.toString(), l.toString());},
                        ()-> {Assertions.fail("해당 아이디를 찾을 수 없습니다.");});
    }

    @Test
    void updateLessonSlotTest(){
        //given
        long lessonSlot1Id = lessonSlotRepository.createLessonSlot(lessonSlot1)
                .map(LessonSlot::getId)
                .orElseThrow(()->new AssertionError("아이디에 null값이 반환되었습니다."));

        lessonSlot1.setId(lessonSlot1Id);
        lessonSlot1.setDay(6);
        //when
        lessonSlotRepository.updateLessonSlot(lessonSlot1);
        //then
        lessonSlotRepository.getLessonSlot(lessonSlot1.getId()).ifPresentOrElse(
                l->Assertions.assertEquals(lessonSlot1.toString() ,l.toString()),
                ()->Assertions.fail()
        );
    }

    @Test
    void deleteLessonSlotTest(){
        //given
        long createdID = lessonSlotRepository.createLessonSlot(lessonSlot1).map(LessonSlot::getId).orElseThrow(()->new AssertionError("반환된 아이디가 null입니다."));
        //when
        lessonSlotRepository.deleteLessonSlot(createdID);
        //then
        Assertions.assertEquals(0, lessonSlotRepository.getLength());
    }

    @Test
    void clearStoreTest(){
        //given
        lessonSlotRepository.createLessonSlot(lessonSlot1);
        //when
        lessonSlotRepository.clearStore();
        //then
        Assertions.assertEquals(0, lessonSlotRepository.getLength());
    }

    @Test
    void findByStudentIdTest(){
        //given
        lessonSlotRepository.createLessonSlot(lessonSlot1);
        //given
        List<LessonSlot> lessonSlotList = lessonSlotRepository.findByStudentId(lessonSlot1.getStudentId());
        //then
        Assertions.assertEquals(1, lessonSlotList.size());
    }

    @Test
    void findByTeacherIdTest(){
        //given
        lessonSlotRepository.createLessonSlot(lessonSlot1);
        //given
        List<LessonSlot> lessonSlotList = lessonSlotRepository.findByTeacherId(lessonSlot1.getTeacherId());
        //then
        Assertions.assertEquals(1, lessonSlotList.size());
    }

}
