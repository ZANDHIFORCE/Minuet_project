package com.ZandhiDokkie.minuet.repository;

import com.ZandhiDokkie.minuet.domain.LessonInfo;
import com.ZandhiDokkie.minuet.repository.interfaces.LessonInfoRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 테스트용 메모리 레슨 정보 리포지토리
 * 파일 로딩 없이 순수 메모리만 사용
 */
public class TestMemoryLessonInfoRepository implements LessonInfoRepository {
    private final Map<Long, LessonInfo> lessonInfos = new HashMap<>();
    private long nextId = 1L;

    @Override
    public Integer getLength() {
        return lessonInfos.size();
    }

    @Override
    public Optional<LessonInfo> getLessonInfo(Long lessonInfoId) {
        return Optional.ofNullable(lessonInfos.get(lessonInfoId));
    }

    @Override
    public List<LessonInfo> getLessonInfos() {
        return new ArrayList<>(lessonInfos.values());
    }

    @Override
    public Optional<LessonInfo> createLessonInfo(LessonInfo lessonInfo) {
        if (lessonInfo.getId() == null) {
            lessonInfo.setId(nextId++);
        }
        lessonInfos.put(lessonInfo.getId(), lessonInfo);
        return Optional.of(lessonInfo);
    }

    @Override
    public Optional<LessonInfo> updateLessonInfo(LessonInfo lessonInfo) {
        if (!lessonInfos.containsKey(lessonInfo.getId())) {
            return Optional.empty();
        }
        lessonInfos.put(lessonInfo.getId(), lessonInfo);
        return Optional.of(lessonInfo);
    }

    @Override
    public Optional<LessonInfo> deleteLessonInfo(Long lessonInfoId) {
        return Optional.ofNullable(lessonInfos.remove(lessonInfoId));
    }

    @Override
    public void clearStore() {
        lessonInfos.clear();
        nextId = 1L;
    }

    @Override
    public List<LessonInfo> findByStudentId(Long studentId) {
        return lessonInfos.values().stream()
                .filter(lesson -> studentId.equals(lesson.getStudentId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<LessonInfo> findByTeacherId(Long teacherId) {
        return lessonInfos.values().stream()
                .filter(lesson -> teacherId.equals(lesson.getTeacherId()))
                .collect(Collectors.toList());
    }
}
