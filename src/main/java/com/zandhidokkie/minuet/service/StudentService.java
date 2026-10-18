package com.zandhidokkie.minuet.service;

import com.zandhidokkie.minuet.domain.Student;
import com.zandhidokkie.minuet.repository.StudentRepository;

import java.util.List;
import java.util.NoSuchElementException;

public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }

    public int getStudentCounts(){
        return studentRepository.getLength();
    }

    public Student getStudentDetail(Long studentId){
        return studentRepository.getStudent(studentId)
                .orElseThrow(()-> new NoSuchElementException("해당학생이 존재 하지 않습니다."));
    }

    public List<Student> getAllStudents(){
        return studentRepository.getStudents();
    }

    public Student registerStudent(Student student){
        return studentRepository.createStudent(student)
                .orElseThrow(()->new IllegalStateException("해당 아이디는 이미 존재합니다."));
    }

    public Student editStudentInfo(Student student){
        return studentRepository.updateStudent(student)
                .orElseThrow(()->new NoSuchElementException("해당 학생id는 존재 하지 않습니다."));
    }

    public Student withdrawStudent(Long studentId){
        return studentRepository.deleteStudent(studentId)
                .orElseThrow(()->new NoSuchElementException("해당 학생id는 존재 하지 않습니다."));
    }

    public void clearAllStudents(){
        studentRepository.clearStore();
    }

    public Student getStudentByName(String studentName){
        return studentRepository.findByName(studentName)
                .orElseThrow(()->{
                    return new NoSuchElementException("해당 학생의 이름는 존재 하지 않습니다.");
                });
    }

}
