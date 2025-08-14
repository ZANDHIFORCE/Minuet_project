package com.ZandhiDokkie.minuet.repository;

import com.ZandhiDokkie.minuet.repository.memory.MemoryLessonInfoRepository;
import com.ZandhiDokkie.minuet.domain.LessonInfo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class MemoryLessonInfoRepositoryTest {
    MemoryLessonInfoRepository lessonInfoRepository;
    LessonInfo lessonInfo1;
    LessonInfo lessonInfo2;

    @BeforeEach
    void beforeEach(){
        lessonInfoRepository = new MemoryLessonInfoRepository();
        lessonInfo1 = new LessonInfo(null, 1L,1L, LocalDateTime.of(2025,4,10,13,0),false);
        lessonInfo2 = new LessonInfo(null, 2L,2L, LocalDateTime.of(2025,4,10,14,0),false);
    }

    @AfterEach
    void afterEach(){
        lessonInfoRepository.clearStore();
    }


    @Test
    void getLengthTest(){
        Assertions.assertEquals(0, lessonInfoRepository.getLength());
    }

    @Test
    void getLessonInfoTest(){
        //given
        lessonInfoRepository.createLessonInfo(lessonInfo1);
        //when
        Optional<LessonInfo> lessonInfoOptional = lessonInfoRepository.getLessonInfo(1L);
        //then
        lessonInfoOptional.ifPresent(l->{Assertions.assertEquals(lessonInfo1.toString(), l.toString());});
    }

    @Test
    void getLessonInfos(){
        //given
        lessonInfoRepository.createLessonInfo(lessonInfo1);
        lessonInfoRepository.createLessonInfo(lessonInfo2);
        //when
        List<LessonInfo> lessonInfoList = lessonInfoRepository.getLessonInfos();
        //then
        for(LessonInfo lessonInfo: lessonInfoList){
            if (lessonInfo.getId() == 1L) {
                Assertions.assertEquals(this.lessonInfo1.toString(), lessonInfo.toString());
            }
            else{
                Assertions.assertEquals(this.lessonInfo2.toString(), lessonInfo.toString());
            }
        }
    }

    @Test
    void createLessonInfoTest(){
        //when
        lessonInfoRepository.createLessonInfo(lessonInfo1);
        lessonInfoRepository.createLessonInfo(lessonInfo2);
        //then
        Assertions.assertEquals(2, lessonInfoRepository.getLength());
    }

    @Test
    void updateLessonInfo(){
        //given
        long createdId = lessonInfoRepository.createLessonInfo(lessonInfo1)
                .map(LessonInfo::getId)
                .orElseThrow(()->new AssertionError("craete 함수가 Optional.empty()를 반환헀습니다."));
        lessonInfo1.setId(createdId);
        lessonInfo1.setCompleted(true);
        //when
        Optional<LessonInfo> lessonInfoOptional = lessonInfoRepository.updateLessonInfo(lessonInfo1);
        //then
        lessonInfoOptional.ifPresentOrElse(
                l->Assertions.assertEquals(true, l.isCompleted()),
                ()->Assertions.fail()
                );
    }

    @Test
    void deleteLessonInfoTest(){
        //given
        long createdId = lessonInfoRepository.createLessonInfo(lessonInfo1)
                .map(LessonInfo::getId)
                .orElseThrow(()->new AssertionError("craete 함수가 Optional.empty()를 반환헀습니다."));
        //when
        lessonInfoRepository.deleteLessonInfo(createdId);
        //then
        Assertions.assertEquals(0, lessonInfoRepository.getLength());
    }

    @Test
    void clearStoreTest(){
        //given
        lessonInfoRepository.createLessonInfo(lessonInfo1);
        lessonInfoRepository.createLessonInfo(lessonInfo2);
        //when
        lessonInfoRepository.clearStore();
        //then
        Assertions.assertEquals(0, lessonInfoRepository.getLength());
    }

    @Test
    void findByStudentIdTest(){
        //given
        lessonInfoRepository.createLessonInfo(lessonInfo1);
        //when
        List<LessonInfo> lessonInfoList =  lessonInfoRepository.findByStudentId(lessonInfo1.getStudentId());
        //then
        lessonInfoList.stream().findAny().ifPresentOrElse(
                l->Assertions.assertEquals(lessonInfo1.toString(), l.toString()),
                ()->Assertions.fail("리스트에 아무 값도 없었습니다.")
        );

    }

    @Test
    void findByTeacherIdTest(){
        //given
        lessonInfoRepository.createLessonInfo(lessonInfo1);
        //when
        List<LessonInfo> lessonInfoList =  lessonInfoRepository.findByTeacherId(lessonInfo1.getTeacherId());
        //then
        lessonInfoList.stream().findAny().ifPresentOrElse(
                l->Assertions.assertEquals(lessonInfo1.toString(), l.toString()),
                ()->Assertions.fail("리스트에 아무 값도 없었습니다.")
        );
    }

}
