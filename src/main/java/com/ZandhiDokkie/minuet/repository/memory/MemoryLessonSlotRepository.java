package com.ZandhiDokkie.minuet.repository.memory;
import com.ZandhiDokkie.minuet.repository.interfaces.LessonSlotRepository;
import com.ZandhiDokkie.minuet.domain.LessonSlot;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class MemoryLessonSlotRepository implements LessonSlotRepository {
    private final Map<Long, LessonSlot> lessonSlots = new HashMap<>();
    private long nextId = 1L;

    public MemoryLessonSlotRepository(){
        this.loadFromFile();
    }

    public void saveToFile(String pathname){
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        File file = new File(pathname);
        try{
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, getLessonSlots());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadFromFile()
    {
        this.loadFromFile("src/main/resources/data/lessonSlots.json");
    }

    public void loadFromFile(String pathname){
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        File file = new File(pathname);
        try{
            this.clearStore();
            List<LessonSlot> lessonSlotList = mapper.readValue(file, new TypeReference<List<LessonSlot>>() {
            });
            setNextIdFrom(lessonSlotList);
            clearStore();
            for(LessonSlot l: lessonSlotList){
                lessonSlots.put(l.getId(), l);
            }
        }catch(IOException e){
           e.printStackTrace();
        }
    }

    public void setNextIdFrom(List<LessonSlot> lessonsLotList){
        long maxId = lessonsLotList.stream()
                .mapToLong(l->l.getId())
                .max().orElse(0L);
        nextId = ++maxId;
    }

    public long generateNextId(){
        return nextId++;
    }

    @Override
    public Integer getLength() {
        return lessonSlots.size();
    }

    @Override
    public Optional<LessonSlot> getLessonSlot(Long lessonSlotId) {
        return Optional.ofNullable(lessonSlots.get(lessonSlotId));
    }

    @Override
    public List<LessonSlot> getLessonSlots() {
        return new ArrayList<LessonSlot>(lessonSlots.values());
    }

    @Override
    public Optional<LessonSlot> createLessonSlot(LessonSlot lessonSlot) {
        lessonSlot.setId(generateNextId());
        lessonSlots.put(lessonSlot.getId(), lessonSlot);
        return Optional.of(lessonSlot);
    }

    @Override
    public Optional<LessonSlot> updateLessonSlot(LessonSlot lessonSlot) {
        if(!lessonSlots.containsKey(lessonSlot.getId()))
            return Optional.empty();
        lessonSlots.put(lessonSlot.getId(), lessonSlot);
        return Optional.of(lessonSlot);
    }

    @Override
    public Optional<LessonSlot> deleteLessonSlot(Long lessonSlotId) {
        return Optional.ofNullable(lessonSlots.remove(lessonSlotId));
    }

    @Override
    public void clearStore() {
        lessonSlots.clear();
        this.nextId = 1L;
    }

    @Override
    public List<LessonSlot> findByStudentId(Long studentId){
        return lessonSlots.values().stream()
                .filter(l->l.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }

    @Override
    public List<LessonSlot> findByTeacherId(Long teacherId){
        return lessonSlots.values().stream()
                .filter(l->l.getTeacherId().equals(teacherId))
                .collect(Collectors.toList());
    }

}

