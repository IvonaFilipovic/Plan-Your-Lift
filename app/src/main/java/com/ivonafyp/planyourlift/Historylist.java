package com.ivonafyp.planyourlift;

public class Historylist {

    private String workout;
    private String time;
    private Historylist(){}
    private Historylist(String workout,String time) {

        this.workout = workout;
        this.time = time;
    }
    public String getWorkout() {

        return workout;
    }
    public void setWorkout(String workoutname) {

        this.workout = workoutname;
    }
    public String getTime() {

        return time;
    }
    public void setTime(String time) {

        this.time = time;
    }

}
//Code may include some snippets from https://developer.android.com/ and https://firebase.google.com/docs
