package com.ZandhiDokkie.minuet.repository.memory;
import com.ZandhiDokkie.minuet.repository.interfaces.LessonInfoRepository;
import com.ZandhiDokkie.minuet.domain.LessonInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class MemoryLessonInfoRepository implements LessonInfoRepository {
    private final Map<Long, LessonInfo> lessonInfos = new HashMap<Long, LessonInfo>();
    private long nextId = 1L;

    public MemoryLessonInfoRepository(){}
    public void saveToFile(){
        saveToFile("src/main/resources/data/lessons_info.json");
    }

    public void saveToFile(String pathname){
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        File file = new File(pathname);
        try{
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(file, new ArrayList<LessonInfo>(lessonInfos.values()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile(){
        loadFromFile("src/main/resources/data/lessons_info.json");
    }

    public void loadFromFile(String pathname){
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        File file = new File(pathname);
        try{
            List<LessonInfo> lessonInfoList = mapper.readValue(file, new TypeReference<List<LessonInfo>>(){});
            setNextIdFrom(lessonInfoList);
            this.clearStore();
            for(LessonInfo l:lessonInfoList){
                lessonInfos.put(l.getId(), l);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setNextIdFrom(List<LessonInfo> lessonInfoList){
        long maxId = lessonInfoList.stream().mapToLong(LessonInfo::getId).max().orElse(0L);
        nextId = ++maxId;
    }

    public long generateNextId(){
        return nextId++;
    }

    @Override
    public Integer getLength() {
        return this.lessonInfos.size();
    }

    @Override
    public Optional<LessonInfo> getLessonInfo(Long lessonInfoId) {
        return Optional.ofNullable(this.lessonInfos.get(lessonInfoId));
    }

    @Override
    public List<LessonInfo> getLessonInfos() {
        return new ArrayList<LessonInfo>(this.lessonInfos.values());
    }

    @Override
    public Optional<LessonInfo> createLessonInfo(LessonInfo lessonInfo) {
        lessonInfo.setId(generateNextId());
        lessonInfos.put(lessonInfo.getId(), lessonInfo);
        return Optional.of(lessonInfo);
    }

    @Override
    public Optional<LessonInfo> updateLessonInfo(LessonInfo lessonInfo) {
        if(!this.lessonInfos.containsKey(lessonInfo.getId())){
            return Optional.empty();
        }
        else{
            this.lessonInfos.put(lessonInfo.getId(),lessonInfo);
            return Optional.of(lessonInfo);
        }
    }

    @Override
    public Optional<LessonInfo> deleteLessonInfo(Long lessonInfoId) {
        return Optional.ofNullable(this.lessonInfos.remove(lessonInfoId));
    }

    @Override
    public void clearStore() {
        this.lessonInfos.clear();
        nextId = 1L;
    }

    @Override
    public List<LessonInfo> findByStudentId(Long studentId){
        return lessonInfos.values().stream()
                .filter(l->l.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }

    @Override
    public List<LessonInfo> findByTeacherId(Long teacherId){
        return lessonInfos.values().stream()
                .filter(l->l.getTeacherId().equals(teacherId))
                .collect(Collectors.toList());
    }
}
