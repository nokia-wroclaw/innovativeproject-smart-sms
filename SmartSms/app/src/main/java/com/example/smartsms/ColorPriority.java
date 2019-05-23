package com.example.smartsms;

public class ColorPriority implements Comparable {
    public String color;
    public int color_priority;
    public ColorPriority(String color,int color_priority)
    {
        this.color=color;
        this.color_priority=color_priority;
    }

    @Override
    public int compareTo(Object comparestu) {
        int compareage=((ColorPriority)comparestu).color_priority;

        return compareage-this.color_priority;


    }


}
