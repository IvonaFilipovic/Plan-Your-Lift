package com.ivonafyp.planyourlift;

public class Workoutlist {

   private String workoutname;
   private Workoutlist(){}
    private Workoutlist(String workoutname) {
        this.workoutname = workoutname;
    }
    public String getWorkoutname() {
        return workoutname;
   }
    public void setWorkoutname(String workoutname) {
       this.workoutname = workoutname;
    }


}
//Code may include some snippets from https://developer.android.com/ and https://firebase.google.com/docs
