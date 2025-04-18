package com.ZandhiDokkie.minuet.service;

import com.ZandhiDokkie.minuet.domain.LessonSlot;
import com.ZandhiDokkie.minuet.repository.memory.MemoryLessonSlotRepository;
import org.junit.jupiter.api.*;

import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

public class LessonSlotServiceTest {
    private LessonSlotService lessonSlotService;
    LessonSlot slot1;
    LessonSlot slot2;

    @BeforeEach
    void beforeEach() {
        lessonSlotService = new LessonSlotService(new MemoryLessonSlotRepository());
        slot1 = new LessonSlot(null, 1L, 1L, 1, LocalTime.of(13, 0));
        slot2 = new LessonSlot(null, 2L, 2L, 3, LocalTime.of(15, 30));
    }

    @AfterEach
    void afterEach() {
        lessonSlotService.clearAllLessonSlots();
    }

    @Test
    void getLessonSlotCountsTest() {
        lessonSlotService.registerLessonSlot(slot1);
        lessonSlotService.registerLessonSlot(slot2);
        Assertions.assertEquals(2, lessonSlotService.getLessonSlotCounts());
    }

    @Test
    void getLessonSlotDetailTest() {
        lessonSlotService.registerLessonSlot(slot1);
        LessonSlot found = lessonSlotService.getLessonSlotDetail(slot1.getId());
        Assertions.assertEquals(slot1.toString(), found.toString());
    }

    @Test
    void getLessonSlotDetail_shouldThrowException_whenNotFound() {
        NoSuchElementException e = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> lessonSlotService.getLessonSlotDetail(999L)
        );
        Assertions.assertEquals("해당 id의 레슨 슬롯이 존재하지 않습니다.", e.getMessage());
    }

    @Test
    void getAllLessonSlotsTest() {
        lessonSlotService.registerLessonSlot(slot1);
        lessonSlotService.registerLessonSlot(slot2);
        Assertions.assertEquals(2, lessonSlotService.getAllLessonSlots().size());
    }

    @Test
    void registerLessonSlotTest() {
        lessonSlotService.registerLessonSlot(slot1);
        LessonSlot found = lessonSlotService.getLessonSlotDetail(slot1.getId());
        Assertions.assertEquals(slot1.toString(), found.toString());
    }

    @Test
    void editLessonSlotTest() {
        lessonSlotService.registerLessonSlot(slot1);
        slot1.setDay(5);
        LessonSlot updated = lessonSlotService.editLessonSlot(slot1);
        Assertions.assertEquals(5, updated.getDay());
    }

    @Test
    void editLessonSlot_shouldThrowException_whenNotFound() {
        NoSuchElementException e = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> lessonSlotService.editLessonSlot(slot1)
        );
        Assertions.assertEquals("해당 id의 레슨 슬롯이 존재하지 않습니다.", e.getMessage());
    }

    @Test
    void withdrawLessonSlotTest() {
        lessonSlotService.registerLessonSlot(slot1);
        lessonSlotService.withdrawLessonSlot(slot1.getId());
        Assertions.assertEquals(0, lessonSlotService.getLessonSlotCounts());
    }

    @Test
    void withdrawLessonSlot_shouldThrowException_whenNotFound() {
        NoSuchElementException e = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> lessonSlotService.withdrawLessonSlot(999L)
        );
        Assertions.assertEquals("해당 id의 레슨 슬롯이 존재하지 않습니다.", e.getMessage());
    }

    @Test
    void getLessonSlotsByStudentIdTest() {
        lessonSlotService.registerLessonSlot(slot1);
        lessonSlotService.registerLessonSlot(slot2);
        List<LessonSlot> list = lessonSlotService.getLessonSlotsByStudentId(1L);
        list.stream().findAny()
                .ifPresentOrElse(
                        s->Assertions.assertEquals(slot1.toString(),
                                s.toString()),()->Assertions.fail("studentId로 Slot 찾기에 실패했습니다.")
                );
    }

    @Test
    void getLessonSlotsByTeacherIdTest() {
        lessonSlotService.registerLessonSlot(slot1);
        lessonSlotService.registerLessonSlot(slot2);
        List<LessonSlot> list = lessonSlotService.getLessonSlotsByTeacherId(1L);
        list.stream().findAny()
                .ifPresentOrElse(
                        s->Assertions.assertEquals(slot1.toString(),
                                s.toString()),()->Assertions.fail("teacherId로 Slot 찾기에 실패했습니다.")
                );
    }
}
