package com.ZandhiDokkie.minuet.integration;

import com.ZandhiDokkie.minuet.domain.LessonSlot;
import com.ZandhiDokkie.minuet.domain.Student;
import com.ZandhiDokkie.minuet.domain.Teacher;
import com.ZandhiDokkie.minuet.exception.InvalidLessonSlotException;
import com.ZandhiDokkie.minuet.exception.LessonSlotConflictException;
import com.ZandhiDokkie.minuet.repository.interfaces.LessonInfoRepository;
import com.ZandhiDokkie.minuet.repository.interfaces.LessonSlotRepository;
import com.ZandhiDokkie.minuet.repository.interfaces.StudentRepository;
import com.ZandhiDokkie.minuet.repository.interfaces.TeacherRepository;
import com.ZandhiDokkie.minuet.service.LessonSlotService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:lesson-registration")
@ActiveProfiles("test")
@AutoConfigureMockMvc
class LessonSlotRegistrationTest {
    @Autowired MockMvc mvc;
    @Autowired LessonSlotService service;
    @Autowired @Qualifier("lessonSlotRepository") LessonSlotRepository slots;
    @Autowired @Qualifier("lessonInfoRepository") LessonInfoRepository lessons;
    @Autowired @Qualifier("teacherRepository") TeacherRepository teachers;
    @Autowired @Qualifier("studentRepository") StudentRepository students;
    @Autowired JdbcTemplate jdbc;

    Long teacherA;
    Long teacherB;
    Long studentA;
    Long studentB;

    @BeforeEach
    void prepareParents() {
        clearData();
        teacherA = teachers.createTeacher(new Teacher(null, "Teacher A", "Piano")).orElseThrow().getId();
        teacherB = teachers.createTeacher(new Teacher(null, "Teacher B", "Piano")).orElseThrow().getId();
        studentA = students.createStudent(new Student(null, "Student A", 0, 8)).orElseThrow().getId();
        studentB = students.createStudent(new Student(null, "Student B", 0, 8)).orElseThrow().getId();
    }

    @AfterEach
    void clearData() {
        slots.clearStore();
        lessons.clearStore();
        students.clearStore();
        teachers.clearStore();
    }

    private LessonSlot slot(Long teacher, Long student, int day, String time) {
        return new LessonSlot(null, teacher, student, day, LocalTime.parse(time));
    }

    private MultiValueMap<String, String> validForm() {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.set("teacherId", teacherA.toString());
        form.set("studentId", studentA.toString());
        form.set("day", "0");
        form.set("time", "13:00");
        return form;
    }

    @Test
    void rendersEmptyForm() throws Exception {
        mvc.perform(get("/LessonSlot/new"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("레슨 시간표 등록")))
                .andExpect(model().attributeExists("lessonSlot"));
    }

    @Test
    void savesAndRedirectsToListWithoutBindingSubmittedId() throws Exception {
        MultiValueMap<String, String> form = validForm();
        form.set("id", "999999");
        mvc.perform(post("/LessonSlot/new").params(form))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/LessonSlot/list"))
                .andExpect(flash().attribute("successMessage", "레슨 시간표를 등록했습니다."));
        LessonSlot saved = slots.getLessonSlots().get(0);
        assertNotEquals(999999L, saved.getId());
        assertEquals(teacherA, saved.getTeacherId());
        assertEquals(studentA, saved.getStudentId());
        assertEquals(LocalTime.of(13, 0), saved.getTime());
        mvc.perform(get("/LessonSlot/list")).andExpect(status().isOk())
                .andExpect(content().string(containsString("13:00")));
    }

    @Test
    void missingFieldsReturnFormErrorsWithoutSaving() throws Exception {
        mvc.perform(post("/LessonSlot/new"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("LessonSlot/createLessonSlotForm"))
                .andExpect(model().attributeHasFieldErrors("lessonSlot", "teacherId", "studentId", "day", "time"));
        assertEquals(0, slots.getLength());
    }

    @ParameterizedTest
    @CsvSource({"teacherId,abc", "studentId,1.5", "teacherId,9223372036854775808", "day,monday"})
    void malformedNumbersReturnFieldErrors(String field, String value) throws Exception {
        MultiValueMap<String, String> form = validForm();
        form.set(field, value);
        mvc.perform(post("/LessonSlot/new").params(form))
                .andExpect(status().isBadRequest())
                .andExpect(model().attributeHasFieldErrors("lessonSlot", field))
                .andExpect(content().string(containsString("입력 내용을 확인해 주세요.")));
        assertEquals(0, slots.getLength());
    }

    @ParameterizedTest
    @ValueSource(strings = {"24:00", "12:60", "25:00", "abc", "12:30:01"})
    void rejectsInvalidOrNonMinuteTimes(String time) throws Exception {
        MultiValueMap<String, String> form = validForm();
        form.set("time", time);
        mvc.perform(post("/LessonSlot/new").params(form))
                .andExpect(status().isBadRequest())
                .andExpect(model().attributeHasFieldErrors("lessonSlot", "time"));
        assertEquals(0, slots.getLength());
    }

    @Test
    void zeroSecondsNormalizeToTheSameMinuteAndCannotBypassConflict() throws Exception {
        MultiValueMap<String, String> form = validForm();
        form.set("time", "12:30:00");
        mvc.perform(post("/LessonSlot/new").params(form)).andExpect(status().is3xxRedirection());
        assertEquals(LocalTime.of(12, 30), slots.getLessonSlots().get(0).getTime());
        form.set("time", "12:30");
        mvc.perform(post("/LessonSlot/new").params(form)).andExpect(status().isConflict());
        assertEquals(1, slots.getLength());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 7})
    void rejectsInvalidWeekdays(int day) throws Exception {
        MultiValueMap<String, String> form = validForm();
        form.set("day", Integer.toString(day));
        mvc.perform(post("/LessonSlot/new").params(form)).andExpect(status().isBadRequest())
                .andExpect(model().attributeHasFieldErrors("lessonSlot", "day"));
        assertEquals(0, slots.getLength());
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1", "9223372036854775807"})
    void rejectsNonPositiveOrUnknownParents(String id) throws Exception {
        MultiValueMap<String, String> form = validForm();
        form.set("teacherId", id);
        form.set("studentId", id);
        mvc.perform(post("/LessonSlot/new").params(form)).andExpect(status().isBadRequest())
                .andExpect(model().attributeHasFieldErrors("lessonSlot", "teacherId", "studentId"));
        assertEquals(0, slots.getLength());
    }

    @ParameterizedTest
    @CsvSource({"0,00:00", "6,23:59"})
    void acceptsWeekAndTimeBoundaries(int day, String time) throws Exception {
        MultiValueMap<String, String> form = validForm();
        form.set("day", Integer.toString(day));
        form.set("time", time);
        mvc.perform(post("/LessonSlot/new").params(form)).andExpect(status().is3xxRedirection());
        assertEquals(1, slots.getLength());
        assertEquals(LocalTime.parse(time), slots.getLessonSlots().get(0).getTime());
    }

    @ParameterizedTest
    @ValueSource(strings = {"teacher", "student", "both"})
    void conflictReturns409AndPreservesFormWithoutInserting(String conflict) throws Exception {
        service.registerLessonSlot(slot(teacherA, studentA, 0, "13:00"));
        MultiValueMap<String, String> form = validForm();
        if (conflict.equals("teacher")) form.set("studentId", studentB.toString());
        if (conflict.equals("student")) form.set("teacherId", teacherB.toString());
        mvc.perform(post("/LessonSlot/new").params(form)).andExpect(status().isConflict())
                .andExpect(view().name("LessonSlot/createLessonSlotForm"))
                .andExpect(model().attributeHasErrors("lessonSlot"))
                .andExpect(content().string(containsString("이미 등록되어 있습니다.")))
                .andExpect(content().string(containsString("value=\"13:00\"")));
        assertEquals(1, slots.getLength());
    }

    @Test
    void allowsIndependentPeopleAndDifferentDaysOrStartTimes() {
        service.registerLessonSlot(slot(teacherA, studentA, 0, "13:00"));
        service.registerLessonSlot(slot(teacherB, studentB, 0, "13:00"));
        service.registerLessonSlot(slot(teacherA, studentA, 1, "13:00"));
        service.registerLessonSlot(slot(teacherA, studentA, 0, "13:01"));
        assertEquals(4, slots.getLength());
    }

    @Test
    void updateAllowsSelfButRejectsConflictWithoutChangingDatabase() {
        LessonSlot first = service.registerLessonSlot(slot(teacherA, studentA, 0, "13:00"));
        LessonSlot second = service.registerLessonSlot(slot(teacherA, studentB, 0, "14:00"));
        service.editLessonSlot(first);
        second.setTime(LocalTime.of(13, 0));
        assertThrows(LessonSlotConflictException.class, () -> service.editLessonSlot(second));
        assertEquals(LocalTime.of(14, 0), slots.getLessonSlot(second.getId()).orElseThrow().getTime());
        first.setDay(7);
        assertThrows(InvalidLessonSlotException.class, () -> service.editLessonSlot(first));
        assertEquals(0, slots.getLessonSlot(first.getId()).orElseThrow().getDay());
    }

    @Test
    void directSqlCannotBypassDayOrForeignKeyConstraints() {
        String sql = "INSERT INTO lesson_slot (teacher_id, student_id, lesson_day, lesson_time) VALUES (?, ?, ?, ?)";
        assertThrows(DataIntegrityViolationException.class,
                () -> jdbc.update(sql, teacherA, studentA, 7, Time.valueOf("13:00:00")));
        assertThrows(DataIntegrityViolationException.class,
                () -> jdbc.update(sql, Long.MAX_VALUE, studentA, 0, Time.valueOf("13:00:00")));
        assertEquals(0, slots.getLength());
    }

    @ParameterizedTest
    @ValueSource(strings = {"teacher", "student", "both"})
    void simultaneousRequestsSaveExactlyOneRow(String conflict) throws Exception {
        LessonSlot first = slot(teacherA, studentA, 0, "13:00");
        LessonSlot second = slot(conflict.equals("student") ? teacherB : teacherA,
                conflict.equals("teacher") ? studentB : studentA, 0, "13:00");
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);
        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            List<Future<Boolean>> results = List.of(
                    pool.submit(() -> registerTogether(first, ready, start)),
                    pool.submit(() -> registerTogether(second, ready, start)));
            assertTrue(ready.await(10, TimeUnit.SECONDS));
            start.countDown();
            int successes = 0;
            for (Future<Boolean> result : results) {
                if (result.get(10, TimeUnit.SECONDS)) successes++;
            }
            assertEquals(1, successes);
            assertEquals(1, slots.getLength());
        } finally {
            start.countDown();
            pool.shutdownNow();
            assertTrue(pool.awaitTermination(10, TimeUnit.SECONDS));
        }
    }

    private boolean registerTogether(LessonSlot slot, CountDownLatch ready, CountDownLatch start) throws Exception {
        ready.countDown();
        if (!start.await(10, TimeUnit.SECONDS)) throw new IllegalStateException("Start latch timed out");
        try {
            service.registerLessonSlot(slot);
            return true;
        } catch (LessonSlotConflictException expected) {
            return false;
        }
    }
}
