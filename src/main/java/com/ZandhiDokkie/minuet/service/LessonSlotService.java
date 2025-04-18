package com.ZandhiDokkie.minuet.service;

import com.ZandhiDokkie.minuet.domain.LessonSlot;
import com.ZandhiDokkie.minuet.repository.interfaces.LessonSlotRepository;

import java.util.List;
import java.util.NoSuchElementException;

public class LessonSlotService {
    private final LessonSlotRepository lessonSlotRepository;

    public LessonSlotService(LessonSlotRepository lessonSlotRepository){
        this.lessonSlotRepository = lessonSlotRepository;
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
        return lessonSlotRepository.createLessonSlot(lessonSlot)
                .orElseThrow(() -> new IllegalStateException("레슨 슬롯을 생성하지 못했습니다."));
    }

    public LessonSlot editLessonSlot(LessonSlot lessonSlot){
        return lessonSlotRepository.updateLessonSlot(lessonSlot)
                .orElseThrow(() -> new NoSuchElementException("해당 id의 레슨 슬롯이 존재하지 않습니다."));
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
