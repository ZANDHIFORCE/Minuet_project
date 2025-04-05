package com.ZandhiDokkie.minuet.domain;

public class Teacher {
    private Long id;
    private String name;
    private String subject;

    public Teacher(Long id, String name, String subject){
        this.id = id;
        this.name = name;
        this.subject = subject;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String toString(){
        return """
                Teacher{
                    id = %d,
                    name = %s,
                    subject = %s,
                }
                """.formatted(id,name,subject);
    }

}
