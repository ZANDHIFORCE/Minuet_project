package com.zandhidokkie.minuet.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.Objects;

public class LessonSlot {
    private Long id;
    private Long teacherId;
    private Long studentId;
    private Integer day;
    @JsonFormat(pattern="HH:mm")
    @DateTimeFormat(pattern="HH:mm")
    private LocalTime time;
    private Integer durationMinutes = 60;

    public LessonSlot() {}

    public LessonSlot(Long id, Long teacherId, Long studentId, Integer day, LocalTime time){
        this(id, teacherId, studentId, day, time, 60);
    }

    public LessonSlot(Long id, Long teacherId, Long studentId, Integer day, LocalTime time, Integer durationMinutes){
        this.id = id;
        this.teacherId =teacherId;
        this.studentId = studentId;
        this.day = day;
        this.time = time;
        this.durationMinutes = durationMinutes;
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

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public int startMinute() {
        return time.getHour() * 60 + time.getMinute();
    }

    public int endMinute() {
        return startMinute() + durationMinutes;
    }

    @JsonIgnore
    public String getEndTimeLabel() {
        return "%02d:%02d".formatted(endMinute() / 60, endMinute() % 60);
    }

    public boolean overlapsWith(LessonSlot other) {
        // 종료 시각에 바로 시작하는 수업은 겹치지 않음
        return Objects.equals(day, other.day)
                && (Objects.equals(teacherId, other.teacherId) || Objects.equals(studentId, other.studentId))
                && startMinute() < other.endMinute() && other.startMinute() < endMinute();
    }

    public String toString(){
        return """
                LessonSlot{
                    id: %d
                    teacherId: %d
                    studentId: %d
                    day: %d
                    time: %s
                    durationMinutes: %d
                }
                """.formatted(id, teacherId, studentId, day, time, durationMinutes);
    }
}
