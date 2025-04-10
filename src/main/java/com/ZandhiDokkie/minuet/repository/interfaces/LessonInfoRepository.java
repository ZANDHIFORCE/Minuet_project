package com.ZandhiDokkie.minuet.repository.interfaces;
import com.ZandhiDokkie.minuet.domain.LessonInfo;

import java.util.List;
import java.util.Optional;

public interface LessonInfoRepository {
    Integer getLength();
    Optional<LessonInfo> getLessonInfo(Long lessonInfoId);
    List<LessonInfo> getLessonInfos();
    Optional<LessonInfo> createLessonInfo(LessonInfo lessonInfo);
    Optional<LessonInfo> updateLessonInfo(LessonInfo lessonInfo);
    Optional<LessonInfo> deleteLessonInfo(Long lessonInfoId);
    void clearStore();
}