package com.ZandhiDokkie.minuet.controller;

import com.ZandhiDokkie.minuet.domain.LessonInfo;
import com.ZandhiDokkie.minuet.service.LessonInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class LessonInfoControllerTest {

    @Mock
    private LessonInfoService lessonInfoService;

    @InjectMocks
    private LessonInfoController lessonInfoController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(lessonInfoController).build();
    }

    @Test
    void createLessonInfoForm_ShouldReturnCorrectView() throws Exception {
        mockMvc.perform(get("/LessonInfo/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("LessonInfo/createLessonInfoForm"));
    }

    @Test
    void registerLessonInfo_ShouldRedirectToHome_WhenValid() throws Exception {
        mockMvc.perform(post("/LessonInfo/new")
                        .param("teacherId", "1")
                        .param("studentId", "1")
                        .param("dateTime", "2025-07-15T14:30"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(lessonInfoService, times(1)).registerLessonInfo(any(LessonInfo.class));
    }

    @Test
    void registerLessonInfo_ShouldReturnToForm_WhenServiceThrowsException() throws Exception {
        doThrow(new RuntimeException("Test exception")).when(lessonInfoService).registerLessonInfo(any(LessonInfo.class));

        mockMvc.perform(post("/LessonInfo/new")
                        .param("teacherId", "1")
                        .param("studentId", "1")
                        .param("dateTime", "2025-07-15T14:30"))
                .andExpect(status().isOk())
                .andExpect(view().name("LessonInfo/createLessonInfoForm"));
    }

    @Test
    void lessonInfoList_ShouldReturnLessonInfoListView() throws Exception {
        List<LessonInfo> lessonInfos = Arrays.asList(
                new LessonInfo(1L, 1L, 1L, LocalDateTime.of(2025, 7, 15, 14, 30), false),
                new LessonInfo(2L, 2L, 2L, LocalDateTime.of(2025, 7, 16, 15, 30), false)
        );
        when(lessonInfoService.getAllLessonInfos()).thenReturn(lessonInfos);

        mockMvc.perform(get("/LessonInfo/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("LessonInfo/lessonInfoList"))
                .andExpect(model().attribute("lessonInfoList", lessonInfos));

        verify(lessonInfoService, times(1)).getAllLessonInfos();
    }

    @Test
    void registerLessonInfo_WithInvalidDateTime_ShouldReturn400() throws Exception {
        // 실제 브라우저에서는 datetime-local input이 잘못된 형식을 보내지 않지만,
        // 직접 API 호출이나 비정상적인 요청에 대한 방어 테스트
        mockMvc.perform(post("/LessonInfo/new")
                        .param("teacherId", "1")
                        .param("studentId", "1")
                        .param("dateTime", "invalid-date"))
                .andExpect(status().isBadRequest());

        verify(lessonInfoService, never()).registerLessonInfo(any(LessonInfo.class));
    }
}
