package com.ZandhiDokkie.minuet.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public class LessonSlot {
    private Long id;
    private Long teacherId;
    private Long studentId;
    private Integer day;
    @JsonFormat(pattern="HH:mm")
    private LocalTime time;

    public LessonSlot() {}

    public LessonSlot(Long id, Long teacherId, Long studentId, Integer day, LocalTime time){
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

    public String toString(){
        return """
                LessonSlot{
                    id: %d
                    teacherId: %d
                    studentId: %d
                    day: %d
                    time: %s
                }
                """.formatted(id, teacherId, studentId, day, time);
    }
}
