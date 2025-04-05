package com.ZandhiDokkie.minuet.repository.interfaces;
import com.ZandhiDokkie.minuet.domain.LessonSlot;

import java.util.List;
import java.util.Optional;

public interface LessonSlotRepository {
    Integer getLength();
    Optional<LessonSlot> getLessonSlot();
    List<LessonSlot> getLessonSlots();
    Optional<LessonSlot> createLessonSlot(LessonSlot teacher);
    Optional<LessonSlot> updateLessonSlot(LessonSlot teacher);
    void deleteLessonSlot(Integer lessonSlotId);
    void clearStore();

}

