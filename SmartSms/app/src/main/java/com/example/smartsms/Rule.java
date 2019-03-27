package com.example.smartsms;

public class Rule {
    public String name;
    public String phrase;
    public String phoneNumber;
    public Priority priority;
    public Rule(String name,String phrase,String phoneNumber, Priority priority)
    {
        this.name= name;
        this.phrase=phrase;
        this.phoneNumber=phoneNumber;
        this.priority=priority;
    }
}
