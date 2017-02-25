package com.asaewing.healthimprover.Others;


import java.io.Serializable;
import java.util.Date;

public class HiMsgItem implements Serializable {

    private static final long serialVersionUID = -3465930416189461897L;

    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SENT = 1;
    public static final int TYPE_MID_DAY = 2;

    private String content;

    private int type;

    private Date date;

    public HiMsgItem(String content, int type, Date date) {
        this.content = content;
        this.type = type;
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }

}