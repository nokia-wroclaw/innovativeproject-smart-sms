package com.example.smartsms;

public class CapturedRule {
    public String nameRule;
    public String smsText;
    public int seed;

    public CapturedRule(String nameRule, String smsText, int seed) {
        this.nameRule = nameRule;
        this.smsText = smsText;
        this.seed = seed;
    }
}
