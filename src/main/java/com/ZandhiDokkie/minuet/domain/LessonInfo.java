package com.ZandhiDokkie.minuet.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class LessonInfo {
    private Long id;
    private Long teacherId;
    private Long studentId;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateTime;
    private boolean completed;

    public LessonInfo(){
    }

    public LessonInfo(Long id, Long teacherId, Long studentId, LocalDateTime dateTime, boolean completed){
        this.id = id;
        this.teacherId = teacherId;
        this.studentId = studentId;
        this.dateTime = dateTime;
        this.completed = completed;
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String toString(){
        return """
                Lesson_info{
                id = %d,
                teacherId = %d,
                studentId = %d,
                dateTime = %s
                }
                """.formatted(id,teacherId,studentId,dateTime);
    }
}
