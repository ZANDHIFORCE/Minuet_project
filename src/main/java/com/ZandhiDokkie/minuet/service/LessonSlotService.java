package com.ZandhiDokkie.minuet.service;

import com.ZandhiDokkie.minuet.domain.LessonSlot;
import com.ZandhiDokkie.minuet.repository.interfaces.LessonSlotRepository;
import com.ZandhiDokkie.minuet.repository.interfaces.StudentRepository;
import com.ZandhiDokkie.minuet.repository.interfaces.TeacherRepository;
import com.ZandhiDokkie.minuet.exception.InvalidLessonSlotException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;

public class LessonSlotService {
    private final LessonSlotRepository lessonSlotRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public LessonSlotService(LessonSlotRepository lessonSlotRepository,
                             TeacherRepository teacherRepository,
                             StudentRepository studentRepository){
        this.lessonSlotRepository = lessonSlotRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    public int getLessonSlotCounts(){
        return lessonSlotRepository.getLength();
    }

    public LessonSlot getLessonSlotDetail(Long lessonSlotId){
        return lessonSlotRepository.getLessonSlot(lessonSlotId)
                .orElseThrow(() -> new NoSuchElementException("해당 id의 레슨 슬롯이 존재하지 않습니다."));
    }

    public List<LessonSlot> getAllLessonSlots(){
        return lessonSlotRepository.getLessonSlots();
    }

    public LessonSlot registerLessonSlot(LessonSlot lessonSlot){
        validate(lessonSlot);
        return persist(lessonSlot, () -> lessonSlotRepository.createLessonSlot(lessonSlot))
                .orElseThrow(() -> new IllegalStateException("레슨 슬롯을 생성하지 못했습니다."));
    }

    public LessonSlot editLessonSlot(LessonSlot lessonSlot){
        if (lessonSlot == null || lessonSlot.getId() == null) {
            throw new NoSuchElementException("해당 id의 레슨 슬롯이 존재하지 않습니다.");
        }
        getLessonSlotDetail(lessonSlot.getId());
        validate(lessonSlot);
        return persist(lessonSlot, () -> lessonSlotRepository.updateLessonSlot(lessonSlot))
                .orElseThrow(() -> new NoSuchElementException("해당 id의 레슨 슬롯이 존재하지 않습니다."));
    }

    private void validate(LessonSlot slot) {
        Map<String, String> errors = new LinkedHashMap<>();
        if (slot == null) {
            throw new InvalidLessonSlotException(Map.of("teacherId", "레슨 정보를 입력해 주세요."));
        }
        if (slot.getTeacherId() == null || slot.getTeacherId() <= 0) {
            errors.put("teacherId", "선생님 ID는 양의 정수로 입력해 주세요.");
        } else if (teacherRepository.getTeacher(slot.getTeacherId()).isEmpty()) {
            errors.put("teacherId", "등록된 선생님을 찾을 수 없습니다. 선생님 목록을 확인해 주세요.");
        }
        if (slot.getStudentId() == null || slot.getStudentId() <= 0) {
            errors.put("studentId", "학생 ID는 양의 정수로 입력해 주세요.");
        } else if (studentRepository.getStudent(slot.getStudentId()).isEmpty()) {
            errors.put("studentId", "등록된 학생을 찾을 수 없습니다. 학생 목록을 확인해 주세요.");
        }
        if (slot.getDay() == null || slot.getDay() < 0 || slot.getDay() > 6) {
            errors.put("day", "월요일부터 일요일까지의 요일을 선택해 주세요.");
        }
        if (slot.getTime() == null) {
            errors.put("time", "레슨 시작 시각을 입력해 주세요.");
        } else if (slot.getTime().getSecond() != 0 || slot.getTime().getNano() != 0) {
            errors.put("time", "시작 시각은 분 단위(HH:mm)로 입력해 주세요.");
        }
        if (!errors.isEmpty()) {
            throw new InvalidLessonSlotException(errors);
        }
    }

    private Optional<LessonSlot> persist(LessonSlot slot, Supplier<Optional<LessonSlot>> operation) {
        try {
            return operation.get();
        } catch (DataIntegrityViolationException e) {
            // A parent can be deleted between validation and insert; the FK remains authoritative.
            validate(slot);
            throw e;
        }
    }

    public LessonSlot withdrawLessonSlot(Long lessonSlotId){
        return lessonSlotRepository.deleteLessonSlot(lessonSlotId)
                .orElseThrow(() -> new NoSuchElementException("해당 id의 레슨 슬롯이 존재하지 않습니다."));
    }

    public void clearAllLessonSlots(){
        lessonSlotRepository.clearStore();
    }

    public List<LessonSlot> getLessonSlotsByStudentId(Long studentId){
        return lessonSlotRepository.findByStudentId(studentId);
    }

    public List<LessonSlot> getLessonSlotsByTeacherId(Long teacherId){
        return lessonSlotRepository.findByTeacherId(teacherId);
    }
}
