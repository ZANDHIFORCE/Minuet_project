package com.ZandhiDokkie.minuet.repository.jdbc;

import com.ZandhiDokkie.minuet.domain.LessonInfo;
import com.ZandhiDokkie.minuet.repository.interfaces.LessonInfoRepository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class JdbcLessonInfoRepository implements LessonInfoRepository {
    private DataSource dataSource;

    public JdbcLessonInfoRepository(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public Integer getLength() {
        return 0;
    }

    @Override
    public Optional<LessonInfo> getLessonInfo(Long lessonInfoId) {
        return Optional.empty();
    }

    @Override
    public List<LessonInfo> getLessonInfos() {
        return List.of();
    }

    @Override
    public Optional<LessonInfo> createLessonInfo(LessonInfo lessonInfo) {
        return Optional.empty();
    }

    @Override
    public Optional<LessonInfo> updateLessonInfo(LessonInfo lessonInfo) {
        return Optional.empty();
    }

    @Override
    public Optional<LessonInfo> deleteLessonInfo(Long lessonInfoId) {
        return Optional.empty();
    }

    @Override
    public void clearStore() {

    }

    @Override
    public List<LessonInfo> findByStudentId(Long studentId) {
        return List.of();
    }

    @Override
    public List<LessonInfo> findByTeacherId(Long teacherId) {
        return List.of();
    }
}
