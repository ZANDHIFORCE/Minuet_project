package com.zandhidokkie.minuet.repository;
import com.zandhidokkie.minuet.domain.LessonSlot;
import com.zandhidokkie.minuet.repository.memory.MemoryLessonSlotRepository;
import com.zandhidokkie.minuet.exception.LessonSlotConflictException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
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
        lessonSlotRepository.createLessonSlot(lessonSlot2);
        //when
        List<LessonSlot> lessonSlotList = lessonSlotRepository.getLessonSlots();
        //then
        Assertions.assertEquals(2, lessonSlotList.size());
        Assertions.assertTrue(lessonSlotList.stream().anyMatch(l -> l.toString().equals(lessonSlot1.toString())));
        Assertions.assertTrue(lessonSlotList.stream().anyMatch(l -> l.toString().equals(lessonSlot2.toString())));
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

    @Test
    void rejectsTeacherAndStudentConflicts() {
        lessonSlotRepository.createLessonSlot(lessonSlot1);
        LessonSlot sameTeacher = new LessonSlot(null, 1L, 3L, 0, LocalTime.of(13, 0));
        LessonSlot sameStudent = new LessonSlot(null, 2L, 2L, 0, LocalTime.of(13, 0));
        Assertions.assertThrows(LessonSlotConflictException.class, () -> lessonSlotRepository.createLessonSlot(sameTeacher));
        Assertions.assertThrows(LessonSlotConflictException.class, () -> lessonSlotRepository.createLessonSlot(sameStudent));
        Assertions.assertEquals(1, lessonSlotRepository.getLength());
    }

    @Test
    void rejectedUpdateAndMutatedSnapshotsDoNotChangeStoredValues() {
        lessonSlotRepository.createLessonSlot(lessonSlot1);
        lessonSlotRepository.createLessonSlot(lessonSlot2);
        lessonSlot1.setDay(5);
        Assertions.assertEquals(0, lessonSlotRepository.getLessonSlot(lessonSlot1.getId()).orElseThrow().getDay());
        LessonSlot snapshot = lessonSlotRepository.getLessonSlot(lessonSlot2.getId()).orElseThrow();
        snapshot.setDay(0);
        Assertions.assertThrows(LessonSlotConflictException.class, () -> lessonSlotRepository.updateLessonSlot(snapshot));
        Assertions.assertEquals(2, lessonSlotRepository.getLessonSlot(lessonSlot2.getId()).orElseThrow().getDay());
        LessonSlot listItem = lessonSlotRepository.getLessonSlots().get(0);
        Integer originalDay = listItem.getDay();
        listItem.setDay(6);
        Assertions.assertEquals(originalDay, lessonSlotRepository.getLessonSlot(listItem.getId()).orElseThrow().getDay());
    }

    @Test
    void fileReloadPreservesIdsBeforeTheNextRegistration(@TempDir Path tempDir) {
        lessonSlotRepository.createLessonSlot(lessonSlot1);
        String file = tempDir.resolve("slots.json").toString();
        lessonSlotRepository.saveToFile(file);
        MemoryLessonSlotRepository reloaded = new MemoryLessonSlotRepository();
        reloaded.loadFromFile(file);
        reloaded.createLessonSlot(lessonSlot2);
        Assertions.assertNotEquals(lessonSlot1.getId(), lessonSlot2.getId());
        Assertions.assertEquals(2, reloaded.getLength());
        Assertions.assertEquals(0, reloaded.getLessonSlot(lessonSlot1.getId()).orElseThrow().getDay());
    }
    @Test
    void partialOverlapIsRejectedButAdjacentLessonIsAllowed() {
        lessonSlot1.setDurationMinutes(45);
        lessonSlotRepository.createLessonSlot(lessonSlot1);
        LessonSlot partial = new LessonSlot(null, 1L, 3L, 0, LocalTime.of(13, 30), 30);
        Assertions.assertThrows(LessonSlotConflictException.class, () -> lessonSlotRepository.createLessonSlot(partial));
        partial.setTime(LocalTime.of(13, 45));
        lessonSlotRepository.createLessonSlot(partial);
        LessonSlot updated = lessonSlotRepository.getLessonSlot(lessonSlot1.getId()).orElseThrow();
        updated.setDurationMinutes(46);
        Assertions.assertThrows(LessonSlotConflictException.class, () -> lessonSlotRepository.updateLessonSlot(updated));
        Assertions.assertEquals(45, lessonSlotRepository.getLessonSlot(lessonSlot1.getId()).orElseThrow().getDurationMinutes());
    }
}
