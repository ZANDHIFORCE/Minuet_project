package com.ZandhiDokkie.minuet.service;

import com.ZandhiDokkie.minuet.domain.Teacher;
import com.ZandhiDokkie.minuet.repository.interfaces.TeacherRepository;

import java.util.List;
import java.util.NoSuchElementException;

public class TeacherService {
    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository){
        this.teacherRepository = teacherRepository;
    }

    public int getTeacherCounts(){
        return teacherRepository.getLength();
    }

    public Teacher getTeacherDetail(Long teacherID){
        return teacherRepository.getTeacher(teacherID)
                .orElseThrow(()->new NoSuchElementException("해당 아이디의 선생님은 없습니다."));
    }

    public List<Teacher> getAllTeachers(){
        return teacherRepository.getTeachers();
    }

    public Teacher registerTeacher(Teacher teacher){
        return teacherRepository.createTeacher(teacher)
                .orElseThrow(()->new IllegalStateException("teacher을 생성하지 못했습니다."));
    }

    public Teacher editTeacherInfo(Teacher teacher){
        return teacherRepository.updateTeacher(teacher)
                .orElseThrow(()->new NoSuchElementException("해당 id의 teacher은 존재하지 않습니다."));
    }

    public Teacher withdrawTeacher(Long teacherId){
        return teacherRepository.deleteTeacher(teacherId)
                .orElseThrow(()->new NoSuchElementException("해당 id의 taecher가 존재하지 않습니다."));
    }

    public void clearAllTeachers(){
        teacherRepository.clearStore();
    }

    public Teacher getTeacherByName(String teacherName){
        return teacherRepository.findByName(teacherName).orElseThrow(()->new NoSuchElementException("해당 이름의 taecher이 존재하지 않습니다."));
    }
}
