package com.ZandhiDokkie.minuet.repository;

import com.ZandhiDokkie.minuet.repository.memory.MemoryLessonInfoRepository;
import com.ZandhiDokkie.minuet.domain.LessonInfo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class MemoryLessonInfoRepositoryTest {
    MemoryLessonInfoRepository repo;
    LessonInfo lessoninfo1;
    LessonInfo lessoninfo2;

    @BeforeEach
    void beforeEach(){
        repo = new MemoryLessonInfoRepository();
        lessoninfo1 = new LessonInfo(1L, 1L,1L, LocalDateTime.of(2025,4,10,13,0),false);
        lessoninfo2 = new LessonInfo(2L, 2L,2L, LocalDateTime.of(2025,4,10,14,0),false);
    }

    @AfterEach
    void afterEach(){
        repo.clearStore();
    }

    @Test
    void saveLoadTest(){
        //given
        repo.createLessonInfo(lessoninfo1);
        repo.createLessonInfo(lessoninfo2);
        String pathname = "src/test/resources/data/testLessonInfos.json";
        //when
        repo.saveToFile(pathname);
        repo.clearStore();
        repo.loadFromFile(pathname);
        //then
        for(LessonInfo lessonInfo: repo.getLessonInfos()){
            if (lessonInfo.getId() == 1L) {
                Assertions.assertEquals(lessoninfo1.toString(), lessonInfo.toString());
            }
            else{
                Assertions.assertEquals(lessoninfo2.toString(), lessonInfo.toString());
            }
        }
    }

    @Test
    void realJsonLoadSaveTest(){
        repo.loadFromFile("src/main/resources/data/lessonInfos.json");
        List<LessonInfo> LessonInfoList1 = repo.getLessonInfos();
        repo.saveToFile("src/test/resources/data/lessonInfos.json");
        repo.clearStore();
        repo.loadFromFile("src/test/resources/data/lessonInfos.json");
        for(LessonInfo l1: LessonInfoList1){
            repo.getLessonInfo(l1.getId())
                    .ifPresent(l2->{
                        Assertions.assertEquals(l1.toString(),l2.toString());
                    });
        }
    }

    @Test
    void getLengthTest(){
        Assertions.assertEquals(0, repo.getLength());
    }

    @Test
    void getLessonInfoTest(){
        //given
        repo.createLessonInfo(lessoninfo1);
        //when
        Optional<LessonInfo> lessonInfoOptional = repo.getLessonInfo(1L);
        //then
        lessonInfoOptional.ifPresent(l->{Assertions.assertEquals(lessoninfo1.toString(), l.toString());});
    }

    @Test
    void getLessonInfos(){
        //given
        repo.createLessonInfo(lessoninfo1);
        repo.createLessonInfo(lessoninfo2);
        //when
        List<LessonInfo> lessonInfoList = repo.getLessonInfos();
        //then
        for(LessonInfo lessonInfo: lessonInfoList){
            if (lessonInfo.getId() == 1L) {
                Assertions.assertEquals(lessoninfo1.toString(), lessonInfo.toString());
            }
            else{
                Assertions.assertEquals(lessoninfo2.toString(), lessonInfo.toString());
            }
        }
    }

    @Test
    void createLessonInfoTest(){
        //when
        repo.createLessonInfo(lessoninfo1);
        repo.createLessonInfo(lessoninfo2);
        //then
        Assertions.assertEquals(2, repo.getLength());
    }

    @Test
    void updateLessonInfo(){
        //given
        Optional<LessonInfo> LessonInfoOptional = repo.getLessonInfo(1L);
        LessonInfoOptional.ifPresent(l->{l.setTeacherId(2L);});
        //when
        LessonInfoOptional.ifPresent(l->{repo.updateLessonInfo(l);});
        //then
        repo.getLessonInfo(1L).ifPresent(l->{Assertions.assertEquals(2L,l.getTeacherId());});
    }

    @Test
    void deleteLessonInfoTest(){
        //given
        repo.createLessonInfo(lessoninfo1);
        //when
        repo.deleteLessonInfo(1L);
        //then
        Assertions.assertEquals(0,repo.getLength());
    }

    @Test
    void clearStoreTest(){
        //given
        repo.createLessonInfo(lessoninfo1);
        repo.createLessonInfo(lessoninfo2);
        //when
        repo.clearStore();
        //then
        Assertions.assertEquals(0,repo.getLength());
    }

}
