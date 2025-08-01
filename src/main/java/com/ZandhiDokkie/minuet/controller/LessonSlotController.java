package com.ZandhiDokkie.minuet.controller;

import com.ZandhiDokkie.minuet.domain.LessonSlot;
import com.ZandhiDokkie.minuet.service.LessonSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalTime;
import java.util.List;

@Controller
public class LessonSlotController {
    public final LessonSlotService lessonSlotService;

    @Autowired
    public LessonSlotController(LessonSlotService lessonSlotService){
        this.lessonSlotService = lessonSlotService;
    }


    @GetMapping("/LessonSlot/new")
    public String crateLessonSlotForm(){
        return "LessonSlot/createLessonSlotForm";
    }

    @PostMapping("/LessonSlot/new")
    public String registerLessonSlotForm(@RequestParam Long teacherId,
                                         @RequestParam Long studentId,
                                         @RequestParam Integer day,
                                         @RequestParam LocalTime time){
        try{
            LessonSlot lessonSlot = new LessonSlot(null, teacherId, studentId, day, time);
            lessonSlotService.registerLessonSlot(lessonSlot);
            return "redirect:/";
        }catch (Exception e){
            System.out.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
            return "LessonSlot/createLessonSlotForm";
        }

    }

    @GetMapping("LessonSlot/list")
    public String lessonSlotList(Model model){
        List<LessonSlot> lessonSlotList = lessonSlotService.getAllLessonSlots();
        model.addAttribute("lessonSlotList", lessonSlotList);
        return "LessonSlot/lessonSlotList";
    }



}
