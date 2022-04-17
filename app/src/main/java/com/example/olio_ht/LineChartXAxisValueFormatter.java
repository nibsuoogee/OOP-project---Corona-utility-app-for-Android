package com.example.olio_ht;

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LineChartXAxisValueFormatter extends IndexAxisValueFormatter {
    private float reference_timestamp = 1.5775704e12f;
    private DateFormat mDataFormat;
    private Date mDate;

    @Override
    public String getFormattedValue(float value) {
        this.mDataFormat = new SimpleDateFormat("yyyy ww", Locale.ENGLISH);
        this.mDate = new Date();
        long originalTime = ((long) (value + reference_timestamp));
        try{
            mDate.setTime(originalTime);
            return mDataFormat.format(mDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }
}
