package com.zandhidokkie.minuet.repository.memory;
import com.zandhidokkie.minuet.repository.LessonSlotRepository;
import com.zandhidokkie.minuet.domain.LessonSlot;
import com.zandhidokkie.minuet.exception.LessonSlotConflictException;
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
    }

    public synchronized void saveToFile(String pathname){
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

    public synchronized void loadFromFile(String pathname){
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        File file = new File(pathname);
        try{
            this.clearStore();
            List<LessonSlot> lessonSlotList = mapper.readValue(file, new TypeReference<List<LessonSlot>>() {
            });
            setNextIdFrom(lessonSlotList);
            for(LessonSlot l: lessonSlotList){
                lessonSlots.put(l.getId(), l);
            }
        }catch(IOException e){
           e.printStackTrace();
        }
    }

    public synchronized void setNextIdFrom(List<LessonSlot> lessonsLotList){
        long maxId = lessonsLotList.stream()
                .mapToLong(l->l.getId())
                .max().orElse(0L);
        nextId = ++maxId;
    }

    public synchronized long generateNextId(){
        return nextId++;
    }

    @Override
    public synchronized Integer getLength() {
        return lessonSlots.size();
    }

    @Override
    public synchronized Optional<LessonSlot> getLessonSlot(Long lessonSlotId) {
        return Optional.ofNullable(lessonSlots.get(lessonSlotId)).map(this::copy);
    }

    @Override
    public synchronized List<LessonSlot> getLessonSlots() {
        return lessonSlots.values().stream().map(this::copy).collect(Collectors.toList());
    }

    @Override
    public synchronized Optional<LessonSlot> createLessonSlot(LessonSlot lessonSlot) {
        ensureAvailable(lessonSlot, null);
        lessonSlot.setId(generateNextId());
        lessonSlots.put(lessonSlot.getId(), copy(lessonSlot));
        return Optional.of(copy(lessonSlot));
    }

    @Override
    public synchronized Optional<LessonSlot> updateLessonSlot(LessonSlot lessonSlot) {
        if(!lessonSlots.containsKey(lessonSlot.getId()))
            return Optional.empty();
        ensureAvailable(lessonSlot, lessonSlot.getId());
        lessonSlots.put(lessonSlot.getId(), copy(lessonSlot));
        return Optional.of(copy(lessonSlot));
    }

    @Override
    public synchronized Optional<LessonSlot> deleteLessonSlot(Long lessonSlotId) {
        return Optional.ofNullable(lessonSlots.remove(lessonSlotId)).map(this::copy);
    }

    @Override
    public synchronized void clearStore() {
        lessonSlots.clear();
        this.nextId = 1L;
    }

    @Override
    public synchronized List<LessonSlot> findByStudentId(Long studentId){
        return lessonSlots.values().stream()
                .filter(l->l.getStudentId().equals(studentId))
                .map(this::copy)
                .collect(Collectors.toList());
    }

    @Override
    public synchronized List<LessonSlot> findByTeacherId(Long teacherId){
        return lessonSlots.values().stream()
                .filter(l->l.getTeacherId().equals(teacherId))
                .map(this::copy)
                .collect(Collectors.toList());
    }

    private void ensureAvailable(LessonSlot candidate, Long excludedId) {
        boolean conflict = lessonSlots.values().stream()
                .filter(existing -> !Objects.equals(existing.getId(), excludedId))
                .anyMatch(candidate::overlapsWith);
        if (conflict) {
            throw new LessonSlotConflictException();
        }
    }

    private LessonSlot copy(LessonSlot source) {
        return new LessonSlot(source.getId(), source.getTeacherId(), source.getStudentId(), source.getDay(), source.getTime(), source.getDurationMinutes());
    }
}

