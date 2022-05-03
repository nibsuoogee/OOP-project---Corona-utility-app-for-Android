package com.example.olio_ht;

public abstract class EntryWeek {
    private String indexKey;
    private String indexValue;
    private String valueKey;
    private String valueValue;
    private String labelValue;

    public String getLabelValue() {
        return labelValue;
    }

    public String getIndexKey() {
        return indexKey;
    }

    public String getIndexValue() {
        return indexValue;
    }

    public String getValueKey() {
        return valueKey;
    }

    public void setValueKey(String valk) {
        this.valueKey = valk;
    }

    public String getValueValue() {
        return valueValue;
    }

    public void setValueValue(String valv) {
        this.valueValue = valv;
    }

    public EntryWeek(String ke, String val, String lab) {
        this.indexKey = ke;
        this.indexValue = val;
        this.labelValue = lab;
    }
}
