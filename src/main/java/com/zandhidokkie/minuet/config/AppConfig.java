package com.zandhidokkie.minuet.config;

import com.zandhidokkie.minuet.repository.LessonInfoRepository;
import com.zandhidokkie.minuet.repository.LessonSlotRepository;
import com.zandhidokkie.minuet.repository.StudentRepository;
import com.zandhidokkie.minuet.repository.TeacherRepository;
import com.zandhidokkie.minuet.repository.jdbc.JdbcLessonInfoRepository;
import com.zandhidokkie.minuet.repository.jdbc.JdbcLessonSlotRepository;
import com.zandhidokkie.minuet.repository.jdbc.JdbcStudentRepository;
import com.zandhidokkie.minuet.repository.jdbc.JdbcTeacherRepository;
import com.zandhidokkie.minuet.repository.memory.MemoryLessonInfoRepository;
import com.zandhidokkie.minuet.repository.memory.MemoryLessonSlotRepository;
import com.zandhidokkie.minuet.repository.memory.MemoryStudentRepository;
import com.zandhidokkie.minuet.repository.memory.MemoryTeacherRepository;
import com.zandhidokkie.minuet.service.LessonInfoService;
import com.zandhidokkie.minuet.service.LessonSlotService;
import com.zandhidokkie.minuet.service.StudentService;
import com.zandhidokkie.minuet.service.TeacherService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class AppConfig {
    private final DataSource dataSource;

    public AppConfig(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Bean
    public TeacherService teacherService(){
        return new TeacherService(teacherRepository());
    }
    @Bean
    public TeacherRepository teacherRepository(){
        return new JdbcTeacherRepository(dataSource);
        //return new MemoryTeacherRepository();
    }

    @Bean
    public StudentService studentService(){
        return new StudentService(studentRepository());
    }
    @Bean
    public StudentRepository studentRepository(){
        return new JdbcStudentRepository(dataSource);
        //return new MemoryStudentRepository();}
    }

    @Bean
    public LessonSlotService lessonSlotService()
    {
        return new LessonSlotService(lessonSlotRepository(), teacherRepository(), studentRepository());
    }
    @Bean
    public LessonSlotRepository lessonSlotRepository(){
        return new JdbcLessonSlotRepository(dataSource);
        //return new MemoryLessonSlotRepository();
    }

    @Bean
    public LessonInfoService lessonInfoService(){
        return new LessonInfoService(lessonInfoRepository());
    }
    @Bean
    public LessonInfoRepository lessonInfoRepository(){
        return new JdbcLessonInfoRepository(dataSource);
        //return new MemoryLessonInfoRepository();
    }
}
