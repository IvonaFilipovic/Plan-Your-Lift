package com.ivonafyp.planyourlift;


public class Exerciselist {

    private String exercisename;
    private int sets;
    private int reps;
    private int value;
    private String units;
    private int setcounter;


    private Exerciselist(){}
    private Exerciselist(String exercisename, int sets, int reps, int value, String units, int setcounter) {
        this.exercisename = exercisename;
        this.sets = sets;
        this.reps = reps;
        this.value = value;
        this.units = units;
        this.setcounter = setcounter;

    }
    public String getExercisename() {
        return exercisename;
    }
    public void setExercisename(String exercisename) {

        this.exercisename = exercisename;
    }
    public int getSets() {
        return sets;
    }
    public void setSets(int sets) {

        this.sets = sets;
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
    public int getSetcounter() {
        return
                setcounter;
    }
    public void setSetcounter(int setcounter) {

        this.setcounter = setcounter;
    }

}
//Code may include some snippets from https://developer.android.com/ and https://firebase.google.com/docs
