package com.ZandhiDokkie.minuet.config;

import com.ZandhiDokkie.minuet.repository.interfaces.LessonInfoRepository;
import com.ZandhiDokkie.minuet.repository.interfaces.LessonSlotRepository;
import com.ZandhiDokkie.minuet.repository.interfaces.StudentRepository;
import com.ZandhiDokkie.minuet.repository.interfaces.TeacherRepository;
import com.ZandhiDokkie.minuet.repository.memory.MemoryLessonInfoRepository;
import com.ZandhiDokkie.minuet.repository.memory.MemoryLessonSlotRepository;
import com.ZandhiDokkie.minuet.repository.memory.MemoryStudentRepository;
import com.ZandhiDokkie.minuet.repository.memory.MemoryTeacherRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public TeacherRepository TeacherRepository(){
        return new MemoryTeacherRepository();
    }

    @Bean
    public StudentRepository StudentRepository(){
        return new MemoryStudentRepository();
    }

    @Bean
    public LessonSlotRepository LessonSlotRepository(){
        return new MemoryLessonSlotRepository();
    }

    @Bean
    public LessonInfoRepository LessonInfoRepository(){
        return new MemoryLessonInfoRepository();
    }
}
