package com.asaewing.healthimprover.Others;

/**
 * Created by asaewing on 2017/1/27.
 */

public class RecordItem {

    private String number,time,text;
    private boolean IsInCloud = false;

    public RecordItem(String number,String time,String text,boolean IsInCloud){
        this.number = number;
        this.time = time;
        this.text = text;
        this.IsInCloud = IsInCloud;
    }

    public void setNumbers(String number){
        this.number = number;
    }

    public void setTimes(String time){
        this.time = time;
    }

    public void setTexts(String text){
        this.text = text;
    }

    public void setIsInCloud(boolean IsInCloud){
        this.IsInCloud = IsInCloud;
    }

    public String getNumbers(){
        return this.number;
    }

    public String getTimes(){
        return this.time;
    }

    public String getTexts(){
        return this.text;
    }

    public boolean getIsInCloud(){
        return this.IsInCloud;
    }

}
