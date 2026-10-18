package com.zandhidokkie.minuet.controller;

import com.zandhidokkie.minuet.domain.Teacher;
import com.zandhidokkie.minuet.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TeacherController {
    private final TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService){
        this.teacherService = teacherService;
    }

    @GetMapping("/Teacher/new")
    public String createTeacherForm(){
        return "teachers/create";
    }

    @PostMapping("/Teacher/new")
    public String registerTeacher(@RequestParam String name,
                                  @RequestParam String subject, Model model) {
        try {
            Teacher teacher = new Teacher(null, name, subject);
            teacherService.registerTeacher(teacher);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "teachers/create";
        }
    }

    @GetMapping("/Teacher/list")
    public String teacherList(Model model){
        List<Teacher> teacherList = teacherService.getAllTeachers();
        model.addAttribute("teacherList", teacherList);
        return "teachers/list";
    }

}
