package com.example.olio_ht;

public class AreaCode {
    private String id;
    private String sid;
    private String label;

    public AreaCode(String i, String si, String lab) {
        this.id = i;
        this.sid = si;
        this.label = lab;
    }

    public String getId() {
        return id;
    }

    public String getSid() {
        return sid;
    }

    public String getLabel() {
        return label;
    }

}
