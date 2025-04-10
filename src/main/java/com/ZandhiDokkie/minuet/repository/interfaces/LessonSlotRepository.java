package com.ZandhiDokkie.minuet.repository.interfaces;
import com.ZandhiDokkie.minuet.domain.LessonSlot;

import java.util.List;
import java.util.Optional;

public interface LessonSlotRepository {
    Integer getLength();
    Optional<LessonSlot> getLessonSlot(Long lessonSlotId);
    List<LessonSlot> getLessonSlots();
    Optional<LessonSlot> createLessonSlot(LessonSlot lessonSlot);
    Optional<LessonSlot> updateLessonSlot(LessonSlot lessonSlot);
    Optional<LessonSlot> deleteLessonSlot(Long lessonSlotId);
    void clearStore();

}

