package com.ZandhiDokkie.minuet.domain;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Lesson_slot {
    private Long id;
    private Long teacherId;
    private Long studentId;
    private Integer day;
    private LocalTime time;

    public Lesson_slot(Long id, Long teacherId, Long studentId, Integer day, LocalTime time){
        this.id = id;
        this.teacherId =teacherId;
        this.studentId = studentId;
        this.day = day;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
