package com.medical.my_medicos.activities.pg.activites.internalfragments.Preprationindexing.Model.twgt;

public class Quiz {
    private String title;
    private String dueDate,index,Id,title1;
    boolean type;
    // Add other fields as needed

    // Default constructor required for calls to DataSnapshot.getValue(Quiz.class)
    public Quiz() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public boolean getType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }


    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
    public String getTitle1() {
        return title1;
    }

    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    // Add getters and setters for other fields
}
