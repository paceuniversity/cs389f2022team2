package com.example.wellnesswatch;

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

    public WorkoutObject(String exercise, String amount, String type, int lbs) {
        this.exercise=exercise;
        this.amount=amount;
        this.type=type;
        this.lbs=lbs;
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
        if(this.lbs !=0)
            sb.append(" "+this.lbs+ " lbs");
        if(this.repAmount!= 0)
            sb.append(" of "+this.repAmount+", "+this.lbs+" lbs");
        return sb.toString();
    }
}