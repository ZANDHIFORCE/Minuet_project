package com.ZandhiDokkie.minuet.repository.memory;
import com.ZandhiDokkie.minuet.domain.Student;
import com.ZandhiDokkie.minuet.repository.interfaces.StudentRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Repository
public class MemoryStudentRepository implements StudentRepository {
    private  final Map<Long, Student> students = new HashMap<>();
    private long nextId = 1L;

    public MemoryStudentRepository(){
        this.loadFromFile();
    }

    public void saveToFile(String pathname){
        File file = new File(pathname);
        ObjectMapper mapper = new ObjectMapper();
        try{
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, getStudents());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadFromFile(){
        this.loadFromFile("src/main/resources/data/students.json");
    }

    public void loadFromFile(String pathname){
        File file = new File(pathname);
        ObjectMapper mapper = new ObjectMapper();
        try{
            List<Student> studentList = mapper.readValue(file, new TypeReference<List<Student>>() {
            });
            this.setNextIdFrom(studentList);
            for(Student student:studentList){
                students.put(student.getId(), student);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setNextIdFrom(List<Student> students){
        long maxId =  students.stream()
                .mapToLong(Student::getId)
                .max()
                .orElse(0L);
        this.nextId = maxId+1;
    }

    public long generateNextId(){
        return this.nextId++;
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
        if(student.getId() == null){
            student.setId(generateNextId());
        }
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
        this.nextId = 1L;
    }

    @Override
    public Optional<Student> findByName(String studentName) {
        for(Student student : this.students.values()){
            if (studentName.equals(student.getName())){
                return Optional.of(student);
            }
        }
        return Optional.empty();
    }
}
