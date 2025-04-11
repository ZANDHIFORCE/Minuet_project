package com.ZandhiDokkie.minuet.repository.memory;
import com.ZandhiDokkie.minuet.domain.Student;
import com.ZandhiDokkie.minuet.repository.interfaces.StudentRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MomoryStudentRepository implements StudentRepository {
    private  final Map<Long, Student> students = new HashMap<>();

    public void saveToFile(String pathname){
        File file = new File(pathname);
        ObjectMapper mapper = new ObjectMapper();
        try{
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, getStudents());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadFromFile(String pathname){
        File file = new File(pathname);
        ObjectMapper mapper = new ObjectMapper();
        try{
            List<Student> studentList = mapper.readValue(file, new TypeReference<List<Student>>() {
            });
            for(Student stuent:studentList){
                this.createStudent(stuent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Integer getLength() {
        return students.size();
    }

    @Override
    public Optional<Student> getStudent(Long studentId) {
        return Optional.ofNullable(students.get(studentId));
    }

    @Override
    public List<Student> getStudents() {
        return new ArrayList<Student>(students.values());
    }

    @Override
    public Optional<Student> createStudent(Student student) {
        if(students.containsKey(student.getId()))
            return Optional.empty();
        students.put(student.getId(), student);
        return Optional.of(student);
    }

    @Override
    public Optional<Student> updateStudent(Student student) {
        if(!students.containsKey(student.getId()))
            return Optional.empty();
        students.put(student.getId(), student);
        return Optional.of(student);
    }

    @Override
    public Optional<Student> deleteStudent(Long studentId) {
        return Optional.ofNullable(students.remove(studentId));
    }

    @Override
    public void clearStore() {
        students.clear();
    }
}
