package com.ZandhiDokkie.minuet.controller;

import com.ZandhiDokkie.minuet.domain.LessonSlot;
import com.ZandhiDokkie.minuet.exception.InvalidLessonSlotException;
import com.ZandhiDokkie.minuet.exception.LessonSlotConflictException;
import com.ZandhiDokkie.minuet.service.LessonSlotService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LessonSlotController {
    private static final String FORM = "LessonSlot/createLessonSlotForm";
    private final LessonSlotService lessonSlotService;

    public LessonSlotController(LessonSlotService lessonSlotService) {
        this.lessonSlotService = lessonSlotService;
    }

    @InitBinder("lessonSlot")
    public void configureBinding(WebDataBinder binder) {
        // Registration must never bind an existing ID from a submitted form.
        binder.setAllowedFields("teacherId", "studentId", "day", "time");
    }

    @GetMapping("/LessonSlot/new")
    public String createLessonSlotForm(Model model) {
        model.addAttribute("lessonSlot", new LessonSlot());
        return FORM;
    }

    @PostMapping("/LessonSlot/new")
    public String registerLessonSlotForm(@ModelAttribute("lessonSlot") LessonSlot lessonSlot,
                                         BindingResult errors,
                                         HttpServletResponse response,
                                         RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return FORM;
        }
        try {
            lessonSlotService.registerLessonSlot(lessonSlot);
        } catch (InvalidLessonSlotException e) {
            e.getFieldErrors().forEach((field, message) -> errors.rejectValue(field, "invalid", message));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return FORM;
        } catch (LessonSlotConflictException e) {
            errors.reject("conflict", e.getMessage());
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return FORM;
        }
        redirectAttributes.addFlashAttribute("successMessage", "레슨 시간표를 등록했습니다.");
        return "redirect:/LessonSlot/list";
    }

    @GetMapping("/LessonSlot/list")
    public String lessonSlotList(Model model) {
        model.addAttribute("lessonSlotList", lessonSlotService.getAllLessonSlots());
        return "LessonSlot/lessonSlotList";
    }
}
