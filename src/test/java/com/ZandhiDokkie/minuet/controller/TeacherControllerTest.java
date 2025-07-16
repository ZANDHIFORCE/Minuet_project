package com.ZandhiDokkie.minuet.controller;

import com.ZandhiDokkie.minuet.domain.Teacher;
import com.ZandhiDokkie.minuet.service.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private TeacherController teacherController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();
    }

    @Test
    void createTeacherForm_ShouldReturnCorrectView() throws Exception {
        mockMvc.perform(get("/Teacher/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("Teacher/createTeacherForm"));
    }

    @Test
    void registerTeacher_ShouldRedirectToHome_WhenValid() throws Exception {
        mockMvc.perform(post("/Teacher/new")
                        .param("name", "김선생")
                        .param("subject", "피아노"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(teacherService, times(1)).registerTeacher(any(Teacher.class));
    }

    @Test
    void registerTeacher_ShouldReturnToForm_WhenServiceThrowsException() throws Exception {
        doThrow(new RuntimeException("Test exception")).when(teacherService).registerTeacher(any(Teacher.class));

        mockMvc.perform(post("/Teacher/new")
                        .param("name", "김선생")
                        .param("subject", "피아노"))
                .andExpect(status().isOk())
                .andExpect(view().name("Teacher/createTeacherForm"));
    }

    @Test
    void teacherList_ShouldReturnTeacherListView() throws Exception {
        List<Teacher> teachers = Arrays.asList(
                new Teacher(1L, "김선생", "피아노"),
                new Teacher(2L, "이선생", "바이올린")
        );
        when(teacherService.getAllTeachers()).thenReturn(teachers);

        mockMvc.perform(get("/Teacher/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("Teacher/teacherList"))
                .andExpect(model().attribute("teacherList", teachers));

        verify(teacherService, times(1)).getAllTeachers();
    }
}
