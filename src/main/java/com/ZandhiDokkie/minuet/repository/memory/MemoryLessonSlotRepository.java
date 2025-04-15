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

@Repository
public class MemoryLessonSlotRepository implements LessonSlotRepository {
    private final Map<Long, LessonSlot> lessonSlots = new HashMap<>();
    public MemoryLessonSlotRepository(){}

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

    public void loadFromFile(String pathname){
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        File file = new File(pathname);
        try{
            this.clearStore();
            List<LessonSlot> lessonSlotList = mapper.readValue(file, new TypeReference<List<LessonSlot>>() {
            });
            for(LessonSlot l: lessonSlotList){
                this.createLessonSlot(l);
            }
        }catch(IOException e){
           e.printStackTrace();
        }
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
        if(lessonSlots.containsKey(lessonSlot.getId()))
            return Optional.empty();
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
    }
}

