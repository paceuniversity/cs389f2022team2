package com.example.wellnesswatch;

import android.util.Log;

public class WorkoutObject {
    private String exercise;
    private String amount;
    private String type;
    private int lbs;
    public int repAmount;

    public WorkoutObject() {

    }

    public WorkoutObject(String exercise, String amount, String type) {
        this.exercise=exercise;
        this.amount=amount;
        this.type=type;
    }

    public WorkoutObject(String exercise, String amount, String type, int lbsOrRep, String diff) {
        this.exercise=exercise;
        this.amount=amount;
        this.type=type;
        //WIll check if lbs or rep amount is being passed in, and handle accordingly
        if(diff.equals("lbs")) {
            this.lbs=lbsOrRep;
        }else{
            this.repAmount=lbsOrRep;
        }

    }


    public WorkoutObject(String exercise, String amount, String type, int repAmount, int lbs) {
        this.exercise=exercise;
        this.amount=amount;
        this.type=type;
        this.repAmount=repAmount;
        this.lbs=lbs;
    }

    //Need to test
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.exercise + " - "+ this.amount + " "+this.type);
        if(this.repAmount!= 0)
            sb.append(" of "+this.repAmount);
         if(this.lbs != 0) {
            sb.append(" "+this.lbs+ " lbs");
        }
        return sb.toString();
    }
}
