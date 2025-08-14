package com.ZandhiDokkie.minuet.repository.memory;
import com.ZandhiDokkie.minuet.domain.Teacher;
import com.ZandhiDokkie.minuet.repository.interfaces.TeacherRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Repository
public class MemoryTeacherRepository implements TeacherRepository{

    private final Map<Long, Teacher> teachers = new HashMap<>();
    private long nextId = 1L;

    public MemoryTeacherRepository() {
    }

    public void loadFromFile(){
        this.loadFromFile("src/main/resources/data/teachers.json");
    }

    public void loadFromFile(String pathname){
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(pathname);
        try{
            List<Teacher> teacherList = mapper.readValue(file, new TypeReference<List<Teacher>>(){});
            setNextIdFrom(teacherList);
            teachers.clear();
            for (Teacher teacher: teacherList) {
                this.teachers.put(teacher.getId(), teacher);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToFile() {
        saveToFile("src/main/resources/data/teachers.json");
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
    public void setNextIdFrom(List<Teacher> teacherList){
        long maxId = teacherList.stream()
                .mapToLong(t->t.getId())
                .max()
                .orElse(0L);
        nextId = ++maxId;
    }


    public long generateNextId(){
        return this.nextId++;
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
        teacher.setId(generateNextId());
        teachers.put(teacher.getId(), teacher);
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
        return Optional.ofNullable(this.teachers.remove(teacherID));
    }

    @Override
    public void clearStore() {
        this.teachers.clear();
        this.nextId = 1L;
    }

    @Override
    public Optional<Teacher> findByName(String teacherName) {
        return teachers.values().stream()
                .filter(t->t.getName().equals(teacherName))
                .findFirst();

//        for(Teacher teacher:teachers.values()) {
//            if(teacherName.equals(teacher.getName())){
//                return Optional.of(teacher);
//            }
//        }
//        return Optional.empty();

    }
}

