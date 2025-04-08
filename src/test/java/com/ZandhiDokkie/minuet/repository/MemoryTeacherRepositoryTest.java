package com.ZandhiDokkie.minuet.repository;
import com.ZandhiDokkie.minuet.repository.memory.MemoryTeacherRepository;
import com.ZandhiDokkie.minuet.domain.Teacher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MemoryTeacherRepositoryTest {
    MemoryTeacherRepository repo;
    Teacher teacher1;
    Teacher teacher2;

    @BeforeEach
    public void beforeEach(){
        this.repo = new MemoryTeacherRepository();
        this.teacher1 = new Teacher(1L, "조동휘", "math");
        this.teacher2 = new Teacher(2L, "송미서", "piano");
    }

    @AfterEach
    public void afterEach(){
        repo.clearStore();
    }

    // Implements
    @Test
    void getLengthTest(){
        Assertions.assertEquals(0, repo.getLength());

    }

    @Test
    void createTeacherTest(){
        repo.createTeacher(teacher1);
        repo.createTeacher(teacher2);
        Assertions.assertEquals(2,repo.getLength());
    }


}
