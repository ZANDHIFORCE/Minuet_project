package com.zandhidokkie.minuet.controller;

import com.zandhidokkie.minuet.domain.Student;
import com.zandhidokkie.minuet.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService){
        this.studentService = studentService;
    }

    @GetMapping("/Student/new")
    public String createStudentForm() {
        return "students/create";
    }

    @PostMapping("/Student/new")
    public String registerStudent(@RequestParam String name,
                                 @RequestParam(defaultValue = "0") Integer progressSessions,
                                 @RequestParam(defaultValue = "0") Integer totalSessions) {
        try {
            Student student = new Student(null, name, progressSessions, totalSessions);
            studentService.registerStudent(student);
            return "redirect:/";
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
            return "students/create";
        }
    }

    @GetMapping("/Student/list")
    public String studentList(Model model){
        List<Student> studentList = studentService.getAllStudents();
        model.addAttribute("studentList", studentList);
        return "students/list";
    }

}
