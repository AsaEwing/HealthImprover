package com.asaewing.healthimprover.app2.Others;

public class CT48 {

    private String time = "";
    private int hour = 0;
    private int min = 0;
    private double ct48 = 0;

    public CT48(String time){
        this.time = time;
        try {
            this.hour = Integer.parseInt(time.substring(0,2));
            this.min = Integer.parseInt(time.substring(3,5));
            this.ct48 = this.hour*2+Math.floor(this.min/3)/10;
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public double getCt48(){
        return ct48;
    }

    public int getHour() {
        return hour;
    }

    public String getTimeString() {
        return time;
    }

    public int getMin(){
        return min;
    }
}
