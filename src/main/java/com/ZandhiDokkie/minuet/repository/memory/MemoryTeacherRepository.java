package com.ZandhiDokkie.minuet.repository.memory;
import com.ZandhiDokkie.minuet.domain.Teacher;
import com.ZandhiDokkie.minuet.repository.interfaces.TeacherRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.text.html.Option;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class MemoryTeacherRepository implements TeacherRepository{

    private final Map<Long, Teacher> teachers = new HashMap<>();

    public void loadFromFile(String pathname){
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(pathname);
        try{
            List<Teacher> teacherList = mapper.readValue(file, new TypeReference<List<Teacher>>(){});
            teachers.clear();
            for (Teacher teacher: teacherList) {
                this.teachers.put(teacher.getId(), teacher);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToFile() {
        ObjectMapper mapper = new  ObjectMapper();
        File file = new File("src/main/resources/data/teachers.json");
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, this.getTeachers());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void saveToFile(String pathname){
        ObjectMapper mapper = new  ObjectMapper();
        File file = new File(pathname);
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, this.getTeachers());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    // Implements
    @Override
    public Integer getLength() {
        return this.teachers.size();
    }

    @Override
    public Optional<Teacher> getTeacher(Long teacherId) {
        return Optional.ofNullable(this.teachers.get(teacherId));
    }

    @Override
    public List<Teacher> getTeachers() {
        return new ArrayList<Teacher>(this.teachers.values());
    }

    @Override
    public Optional<Teacher> createTeacher(Teacher teacher) {
        Long teacherId = teacher.getId();
        if (this.teachers.containsKey(teacherId)){
            return Optional.empty();
        }
        this.teachers.put(teacherId,teacher);
        return Optional.of(teacher);
    }

    @Override
    public Optional<Teacher> updateTeacher(Teacher teacher) {
        Long teacherId = teacher.getId();
        if(!this.teachers.containsKey(teacherId)){
            return Optional.empty();
        }
        this.teachers.put(teacherId,teacher);
        return Optional.of(teacher);
    }

    @Override
    public Optional<Teacher> deleteTeacher(Long teacherID) {
        Teacher removed = this.teachers.remove(teacherID);
        return Optional.ofNullable(removed);
    }

    @Override
    public void clearStore() {
        this.teachers.clear();
    }
}

