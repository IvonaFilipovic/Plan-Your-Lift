package com.ivonafyp.planyourlift;

public class Setslist {

    private String exercisename;
    private int reps;
    private int value;
    private String units;
    private Integer setnum;


    private Setslist(){}
    private Setslist(String exercisename, int reps, int value, String units, Integer setnum) {
        this.exercisename = exercisename;
        this.reps = reps;
        this.value = value;
        this.units = units;
        this.setnum = setnum;

    }
    public String getExercisename() {
        return exercisename;
    }
    public void setExercisename(String exercisename) {

        this.exercisename = exercisename;
    }
    public int getReps() {
        return reps;
    }
    public void setReps(int reps) {

        this.reps = reps;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {

        this.value = value;
    }
    public String getunits() {
        return units;
    }
    public void setunits(String units) {

        this.units = units;
    }
    public Integer getsetnum() {
        return setnum;
    }
    public void setsetnum(Integer setnum) {

        this.setnum = setnum;
    }

}

//Code may include some snippets from https://developer.android.com/ and https://firebase.google.com/docs
