package com.ZandhiDokkie.minuet.repository.interfaces;
import com.ZandhiDokkie.minuet.domain.LessonInfo;
import com.ZandhiDokkie.minuet.domain.LessonSlot;

import java.util.List;
import java.util.Optional;

public interface LessonInfoRepository {
    Integer getLength();
    Optional<LessonInfo> getLessonInfo();
    List<LessonInfo> getLessonsInfo();
    Optional<LessonInfo> createLessonInfo(LessonInfo lessonInfo);
    Optional<LessonInfo> updateLessonInfo(LessonInfo lessonInfo);
    void deleteLessonInfo(Integer lessonInfoId);
}