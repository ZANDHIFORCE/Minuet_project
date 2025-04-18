package com.ZandhiDokkie.minuet.service;

import com.ZandhiDokkie.minuet.domain.LessonInfo;
import com.ZandhiDokkie.minuet.repository.memory.MemoryLessonInfoRepository;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

public class LessonInfoServiceTest {
    private LessonInfoService lessonInfoService;
    LessonInfo lesson1;
    LessonInfo lesson2;

    @BeforeEach
    void beforeEach() {
        lessonInfoService = new LessonInfoService(new MemoryLessonInfoRepository());
        lesson1 = new LessonInfo(null, 1L, 1L, LocalDateTime.of(2025, 4, 10, 13, 0), false);
        lesson2 = new LessonInfo(null, 2L, 1L, LocalDateTime.of(2025, 4, 12, 14, 0), true);
    }

    @AfterEach
    void afterEach() {
        lessonInfoService.clearAllLessonInfos();
    }

    @Test
    void getLessonInfoCountsTest() {
        lessonInfoService.registerLessonInfo(lesson1);
        lessonInfoService.registerLessonInfo(lesson2);
        Assertions.assertEquals(2, lessonInfoService.getLessonInfoCounts());
    }

    @Test
    void getLessonInfoDetailTest() {
        lessonInfoService.registerLessonInfo(lesson1);
        LessonInfo found = lessonInfoService.getLessonInfoDetail(lesson1.getId());
        Assertions.assertEquals(lesson1.toString(), found.toString());
    }

    @Test
    void getLessonInfoDetail_shouldThrowException_whenNotFound() {
        NoSuchElementException e = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> lessonInfoService.getLessonInfoDetail(999L)
        );
        Assertions.assertEquals("해당 id의 레슨 정보가 존재하지 않습니다.", e.getMessage());
    }

    @Test
    void getAllLessonInfosTest() {
        lessonInfoService.registerLessonInfo(lesson1);
        lessonInfoService.registerLessonInfo(lesson2);
        Assertions.assertEquals(2, lessonInfoService.getAllLessonInfos().size());
    }

    @Test
    void registerLessonInfoTest() {
        lessonInfoService.registerLessonInfo(lesson1);
        LessonInfo found = lessonInfoService.getLessonInfoDetail(lesson1.getId());
        Assertions.assertEquals(lesson1.toString(), found.toString());
    }

    @Test
    void editLessonInfoTest() {
        lessonInfoService.registerLessonInfo(lesson1);
        lesson1.setCompleted(true);
        LessonInfo updated = lessonInfoService.editLessonInfo(lesson1);
        Assertions.assertTrue(updated.isCompleted());
    }

    @Test
    void editLessonInfo_shouldThrowException_whenNotFound() {
        NoSuchElementException e = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> lessonInfoService.editLessonInfo(lesson1)
        );
        Assertions.assertEquals("해당 id의 레슨 정보가 존재하지 않습니다.", e.getMessage());
    }

    @Test
    void withdrawLessonInfoTest() {
        lessonInfoService.registerLessonInfo(lesson1);
        lessonInfoService.withdrawLessonInfo(lesson1.getId());
        Assertions.assertEquals(0, lessonInfoService.getLessonInfoCounts());
    }

    @Test
    void withdrawLessonInfo_shouldThrowException_whenNotFound() {
        NoSuchElementException e = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> lessonInfoService.withdrawLessonInfo(999L)
        );
        Assertions.assertEquals("해당 id의 레슨 정보가 존재하지 않습니다.", e.getMessage());
    }

    @Test
    void getLessonInfosByStudentIdTest() {
        lessonInfoService.registerLessonInfo(lesson1);
        lessonInfoService.registerLessonInfo(lesson2);
        List<LessonInfo> list = lessonInfoService.getLessonInfosByStudentId(1L);
        list.stream().findAny()
                .ifPresentOrElse(
                        l->Assertions.assertEquals(lesson1.toString(), l.toString()),
                        ()->Assertions.fail("studentId로 lessonInfo 검색에 실패했습니다.")
                );
    }

    @Test
    void getLessonInfosByTeacherIdTest() {
        lessonInfoService.registerLessonInfo(lesson1);
        lessonInfoService.registerLessonInfo(lesson2);
        List<LessonInfo> list = lessonInfoService.getLessonInfosByTeacherId(1L);
        list.stream().findAny()
                .ifPresentOrElse(
                        l->Assertions.assertEquals(lesson1.toString(), l.toString()),
                        ()->Assertions.fail("teacher 아이디로 lessonInfo 검색에 실패했습니다.")
                );
    }
}
