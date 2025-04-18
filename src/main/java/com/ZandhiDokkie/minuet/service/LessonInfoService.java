package com.ZandhiDokkie.minuet.service;

import com.ZandhiDokkie.minuet.domain.LessonInfo;
import com.ZandhiDokkie.minuet.repository.interfaces.LessonInfoRepository;

import java.util.List;
import java.util.NoSuchElementException;

public class LessonInfoService {
    private final LessonInfoRepository lessonInfoRepository;

    public LessonInfoService(LessonInfoRepository lessonInfoRepository){
        this.lessonInfoRepository = lessonInfoRepository;
    }

    public int getLessonInfoCounts(){
        return lessonInfoRepository.getLength();
    }

    public LessonInfo getLessonInfoDetail(Long lessonInfoId){
        return lessonInfoRepository.getLessonInfo(lessonInfoId)
                .orElseThrow(() -> new NoSuchElementException("해당 id의 레슨 정보가 존재하지 않습니다."));
    }

    public List<LessonInfo> getAllLessonInfos(){
        return lessonInfoRepository.getLessonInfos();
    }

    public LessonInfo registerLessonInfo(LessonInfo lessonInfo){
        return lessonInfoRepository.createLessonInfo(lessonInfo)
                .orElseThrow(() -> new IllegalStateException("레슨 정보를 생성하지 못했습니다."));
    }

    public LessonInfo editLessonInfo(LessonInfo lessonInfo){
        return lessonInfoRepository.updateLessonInfo(lessonInfo)
                .orElseThrow(() -> new NoSuchElementException("해당 id의 레슨 정보가 존재하지 않습니다."));
    }

    public LessonInfo withdrawLessonInfo(Long lessonInfoId){
        return lessonInfoRepository.deleteLessonInfo(lessonInfoId)
                .orElseThrow(() -> new NoSuchElementException("해당 id의 레슨 정보가 존재하지 않습니다."));
    }

    public void clearAllLessonInfos(){
        lessonInfoRepository.clearStore();
    }

    public List<LessonInfo> getLessonInfosByStudentId(Long studentId){
        return lessonInfoRepository.findByStudentId(studentId);
    }

    public List<LessonInfo> getLessonInfosByTeacherId(Long teacherId){
        return lessonInfoRepository.findByTeacherId(teacherId);
    }
}
