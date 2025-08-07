package com.ZandhiDokkie.minuet.repository.jdbc;

import com.ZandhiDokkie.minuet.domain.LessonSlot;
import com.ZandhiDokkie.minuet.repository.interfaces.LessonSlotRepository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

/**
 * JDBC 기반 레슨 슬롯 리포지토리 구현체
 * TODO: 실제 데이터베이스 연동 로직 구현 필요
 */
public class JdbcLessonSlotRepository implements LessonSlotRepository {

    private DataSource dataSource;

    public JdbcLessonSlotRepository(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public Integer getLength() {
        return 0;
    }

    @Override
    public Optional<LessonSlot> getLessonSlot(Long lessonSlotId) {
        return Optional.empty();
    }

    @Override
    public List<LessonSlot> getLessonSlots() {
        return List.of();
    }

    @Override
    public Optional<LessonSlot> createLessonSlot(LessonSlot lessonSlot) {
        return Optional.empty();
    }

    @Override
    public Optional<LessonSlot> updateLessonSlot(LessonSlot lessonSlot) {
        return Optional.empty();
    }

    @Override
    public Optional<LessonSlot> deleteLessonSlot(Long lessonSlotId) {
        return Optional.empty();
    }

    @Override
    public void clearStore() {

    }

    @Override
    public List<LessonSlot> findByStudentId(Long studentId) {
        return List.of();
    }

    @Override
    public List<LessonSlot> findByTeacherId(Long teacherId) {
        return List.of();
    }
}
