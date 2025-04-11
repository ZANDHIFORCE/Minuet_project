package com.ZandhiDokkie.minuet.domain;

public class Student {
    private Long id;
    private String name;
    private Integer progressSessions;
    private Integer totalSessions;

    public Student(){}

    public Student(Long id, String name, Integer progressSessions, Integer totalSessions){
        this.id = id;
        this.name = name;
        this.progressSessions = progressSessions;
        this.totalSessions = totalSessions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getProgressSessions() {
        return progressSessions;
    }

    public void setProgressSessions(Integer progressSessions) {
        this.progressSessions = progressSessions;
    }

    public Integer getTotalSessions() {
        return totalSessions;
    }

    public void setTotalSessions(Integer totalSessions) {
        this.totalSessions = totalSessions;
    }

    public String toString(){
        return """
                Student {
                    id = %d,
                    name = %s,
                    progressSessions = %d,
                    totalSessions = %d,
                }
                """.formatted(id, name, progressSessions, totalSessions);
    }


}
