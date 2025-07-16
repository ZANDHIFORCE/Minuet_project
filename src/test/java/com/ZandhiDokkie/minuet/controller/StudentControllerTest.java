package com.ZandhiDokkie.minuet.controller;

import com.ZandhiDokkie.minuet.domain.Student;
import com.ZandhiDokkie.minuet.service.StudentService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    void createStudentForm_ShouldReturnCorrectView() throws Exception {
        mockMvc.perform(get("/Student/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("Student/createStudentForm"));
    }

    @Test
    void registerStudent_ShouldRedirectToHome_WhenValid() throws Exception {
        mockMvc.perform(post("/Student/new")
                        .param("name", "조동휘")
                        .param("progressSessions", "5")
                        .param("totalSessions", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(studentService, times(1)).registerStudent(any(Student.class));
    }

    @Test
    void registerStudent_ShouldReturnToForm_WhenServiceThrowsException() throws Exception {
        doThrow(new RuntimeException("Test exception")).when(studentService).registerStudent(any(Student.class));

        mockMvc.perform(post("/Student/new")
                        .param("name", "조동휘")
                        .param("progressSessions", "5")
                        .param("totalSessions", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("Student/createStudentForm"));
    }

    @Test
    void studentList_ShouldReturnStudentListView() throws Exception {
        List<Student> students = Arrays.asList(
                new Student(1L, "조동휘", 5, 10),
                new Student(2L, "송미서", 8, 12)
        );
        when(studentService.getAllStudents()).thenReturn(students);

        mockMvc.perform(get("/Student/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("Student/studentList"))
                .andExpect(model().attribute("studentList", students));

        verify(studentService, times(1)).getAllStudents();
    }
}
