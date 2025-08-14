package com.ZandhiDokkie.minuet.controller;

import com.ZandhiDokkie.minuet.domain.LessonInfo;
import com.ZandhiDokkie.minuet.service.LessonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class LessonInfoController {
    public final LessonInfoService lessonInfoService;
    @Autowired
    public LessonInfoController(LessonInfoService lessonInfoService){
        this.lessonInfoService = lessonInfoService;
    }

    @GetMapping("/LessonInfo/new")
    public String createLessonInfoForm(){
        return "LessonInfo/createLessonInfoForm";
    }

    @PostMapping("/LessonInfo/new")
    public String registerLessonInfo(@RequestParam Long teacherId,
                                     @RequestParam Long studentId,
                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        try {
            LessonInfo lessonInfo = new LessonInfo(null, teacherId, studentId, dateTime, false);
            lessonInfoService.registerLessonInfo(lessonInfo);
            return "redirect:/";
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
            return "LessonInfo/createLessonInfoForm";
        }
    }
    @GetMapping("/LessonInfo/list")
    public String lessonInfoList(Model model){
        List<LessonInfo> lessonInfoList = this.lessonInfoService.getAllLessonInfos();
        model.addAttribute("lessonInfoList", lessonInfoList);
        return "LessonInfo/lessonInfoList";
    }

}
