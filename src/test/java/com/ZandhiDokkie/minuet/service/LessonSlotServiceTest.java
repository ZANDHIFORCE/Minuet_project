package com.ZandhiDokkie.minuet.service;

import com.ZandhiDokkie.minuet.domain.LessonSlot;
import com.ZandhiDokkie.minuet.repository.memory.MemoryLessonSlotRepository;
import com.ZandhiDokkie.minuet.repository.memory.MemoryTeacherRepository;
import com.ZandhiDokkie.minuet.repository.memory.MemoryStudentRepository;
import com.ZandhiDokkie.minuet.domain.Teacher;
import com.ZandhiDokkie.minuet.domain.Student;
import com.ZandhiDokkie.minuet.exception.InvalidLessonSlotException;
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
        MemoryTeacherRepository teachers = new MemoryTeacherRepository();
        MemoryStudentRepository students = new MemoryStudentRepository();
        teachers.createTeacher(new Teacher(null, "Teacher 1", "Piano"));
        teachers.createTeacher(new Teacher(null, "Teacher 2", "Piano"));
        students.createStudent(new Student(null, "Student 1", 0, 8));
        students.createStudent(new Student(null, "Student 2", 0, 8));
        lessonSlotService = new LessonSlotService(new MemoryLessonSlotRepository(), teachers, students);
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
    @Test
    void rejectsMissingFieldsBeforeWriting() {
        InvalidLessonSlotException error = Assertions.assertThrows(InvalidLessonSlotException.class,
                () -> lessonSlotService.registerLessonSlot(new LessonSlot()));
        Assertions.assertEquals(4, error.getFieldErrors().size());
        Assertions.assertEquals(0, lessonSlotService.getLessonSlotCounts());
    }

    @Test
    void rejectsSecondsAndNanosecondsFromNonWebCallers() {
        slot1.setTime(LocalTime.of(13, 0, 1));
        Assertions.assertThrows(InvalidLessonSlotException.class, () -> lessonSlotService.registerLessonSlot(slot1));
        slot1.setTime(LocalTime.of(13, 0, 0, 1));
        Assertions.assertThrows(InvalidLessonSlotException.class, () -> lessonSlotService.registerLessonSlot(slot1));
        Assertions.assertEquals(0, lessonSlotService.getLessonSlotCounts());
    }
    @Test
    void rejectsInvalidLengthAndCrossingMidnightWithoutSaving() {
        slot1.setDurationMinutes(null);
        Assertions.assertThrows(InvalidLessonSlotException.class, () -> lessonSlotService.registerLessonSlot(slot1));
        slot1.setDurationMinutes(0);
        Assertions.assertThrows(InvalidLessonSlotException.class, () -> lessonSlotService.registerLessonSlot(slot1));
        slot1.setDurationMinutes(31);
        slot1.setTime(LocalTime.of(23, 30));
        Assertions.assertThrows(InvalidLessonSlotException.class, () -> lessonSlotService.registerLessonSlot(slot1));
        Assertions.assertEquals(0, lessonSlotService.getLessonSlotCounts());
    }
}
