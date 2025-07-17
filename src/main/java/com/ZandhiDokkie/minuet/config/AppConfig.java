package com.ZandhiDokkie.minuet.config;

import com.ZandhiDokkie.minuet.repository.interfaces.LessonInfoRepository;
import com.ZandhiDokkie.minuet.repository.interfaces.LessonSlotRepository;
import com.ZandhiDokkie.minuet.repository.interfaces.StudentRepository;
import com.ZandhiDokkie.minuet.repository.interfaces.TeacherRepository;
import com.ZandhiDokkie.minuet.repository.jdbc.JdbcTeacherRepository;
import com.ZandhiDokkie.minuet.repository.memory.MemoryLessonInfoRepository;
import com.ZandhiDokkie.minuet.repository.memory.MemoryLessonSlotRepository;
import com.ZandhiDokkie.minuet.repository.memory.MemoryStudentRepository;
import com.ZandhiDokkie.minuet.repository.memory.MemoryTeacherRepository;
import com.ZandhiDokkie.minuet.service.LessonInfoService;
import com.ZandhiDokkie.minuet.service.LessonSlotService;
import com.ZandhiDokkie.minuet.service.StudentService;
import com.ZandhiDokkie.minuet.service.TeacherService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class AppConfig {
    private DataSource dataSource;

    public AppConfig(DataSource datasource){
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
    public StudentRepository studentRepository(){return new MemoryStudentRepository();}

    @Bean
    public LessonSlotService lessonSlotService()
    {
        return new LessonSlotService(lessonSlotRepository());
    }
    @Bean
    public LessonSlotRepository lessonSlotRepository(){
        return new MemoryLessonSlotRepository();
    }

    @Bean
    public LessonInfoService lessonInfoService(){
        return new LessonInfoService(lessonInfoRepository());
    }
    @Bean
    public LessonInfoRepository lessonInfoRepository(){
        return new MemoryLessonInfoRepository();
    }
}
