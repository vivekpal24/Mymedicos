package com.medical.my_medicos.activities.pg.model;

import com.google.firebase.Timestamp;

import java.sql.Time;

public class QuizPGExam {
    private String title,title1,id,index;
    private boolean type;
    private Timestamp description;


    public QuizPGExam(String title, String title1, Timestamp to, String id,boolean type,String index) {
        this.title = title;

        this.id = id;
        this.type=type;
        this.title1=title1;
        this.description = to;
        this.index=index;
    }

    public String getTitle() {
        return title;
    }
    public boolean getType(){ return type;}
    public String getIndex(){ return index;}
    public String getTitle1() {
        return title1;
    }
    //    public Timestamp getTo() {
//        return description;
//    }
    public Timestamp getTo() {
        return description;
    }

    public void setTo(Timestamp to) {
        this.description= to;
    }

    public String getId() {
        return id;
    }
}
