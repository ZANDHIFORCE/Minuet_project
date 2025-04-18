package com.ZandhiDokkie.minuet.repository;
import com.ZandhiDokkie.minuet.domain.LessonInfo;
import com.ZandhiDokkie.minuet.repository.memory.MemoryTeacherRepository;
import com.ZandhiDokkie.minuet.domain.Teacher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MemoryTeacherRepositoryTest {
    MemoryTeacherRepository repo;
    Teacher teacher1;
    Teacher teacher2;

    @BeforeEach
    public void beforeEach(){
        this.repo = new MemoryTeacherRepository();
        this.teacher1 = new Teacher(null, "조동휘", "math");
        this.teacher2 = new Teacher(null, "송미서", "piano");
    }

    @AfterEach
    public void afterEach(){
        repo.clearStore();
    }

    @Test
    void saveLoadTest() {
        //given
        this.repo.createTeacher(teacher1);
        this.repo.createTeacher(teacher2);
        String pathname = "src/test/resources/data/testTeachers.json";
        //when
        repo.saveToFile(pathname);
        repo.clearStore();
        repo.loadFromFile(pathname);
        //then
        for (Teacher teacher: repo.getTeachers()){
            if (Objects.equals(teacher.getId(), teacher1.getId())) {
                Assertions.assertEquals(teacher1.toString(), teacher.toString());
            }
            else{
                Assertions.assertEquals(teacher2.toString(), teacher.toString());
            }
        }
    }

    @Test
    void realJsonLoadSaveTest(){
        repo.loadFromFile("src/main/resources/data/teachers.json");
        List<Teacher> teacherList1 = repo.getTeachers();
        repo.saveToFile("src/test/resources/data/teachers.json");
        repo.clearStore();
        repo.loadFromFile("src/test/resources/data/teachers.json");
        for(Teacher t1: teacherList1){
            repo.getTeacher(t1.getId())
                    .ifPresent(t2->{
                        Assertions.assertEquals(t1.toString(),t2.toString());
                    });
        }
    }

    // Implements
    @Test
    void getLengthTest(){
        Assertions.assertEquals(0, repo.getLength());

    }

    @Test
    void getTeacherTest(){
        //given
        repo.createTeacher(teacher1);
        //when
        Optional<Teacher> object = repo.getTeacher(teacher1.getId());
        //then
        object.ifPresent(t->{Assertions.assertEquals(teacher1.toString(), t.toString());});
    }

    @Test
    void getTeachersTest(){
        //given
        repo.createTeacher(teacher1);
        repo.createTeacher(teacher2);
        //when
        List<Teacher> teachers = repo.getTeachers();
        //then
        for (Teacher teacher: teachers){
            if (Objects.equals(teacher.getId(), teacher1.getId())) {
                Assertions.assertEquals(teacher1.toString(), teacher.toString());
            }
            else{
                Assertions.assertEquals(teacher2.toString(), teacher.toString());
            }
        }
    }

    @Test
    void createTeacherTest(){
        //when
        repo.createTeacher(teacher1);
        repo.createTeacher(teacher2);
        //then
        Assertions.assertEquals(2,repo.getLength());
    }

    @Test
    void updateTeacherTest(){
        //given
        repo.createTeacher(teacher1);
        teacher1.setSubject("computer");
        //when
        repo.updateTeacher(teacher1);
        //then
        repo.getTeacher(teacher1.getId()).ifPresent(t -> {
            Assertions.assertEquals("computer", t.getSubject());
        });
    }

    @Test
    void deleteTeacherTest(){
        //given
        repo.createTeacher(teacher1);
        //when
        repo.deleteTeacher(teacher1.getId());
        //then
        Assertions.assertEquals(0,repo.getLength());
    }

    @Test
    void clearStoreTest(){
        //given
        repo.createTeacher(teacher1);
        repo.createTeacher(teacher2);
        //when
        repo.clearStore();
        //then
        Assertions.assertEquals(0,repo.getLength());
    }


}
