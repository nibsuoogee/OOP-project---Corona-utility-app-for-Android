package com.example.olio_ht;

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// Works as a year+week formatter for bar graph x-axis
public class LineChartXAxisValueFormatter extends IndexAxisValueFormatter {
    // reference_timestamp is equal to float form of timestamp for the first
    // date in data set, somewhere in 2020
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
