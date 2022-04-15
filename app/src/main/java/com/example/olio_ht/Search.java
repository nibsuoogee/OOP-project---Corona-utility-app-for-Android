package com.example.olio_ht;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.github.mikephil.charting.charts.BarChart;

public class Search extends AppCompatActivity {
    private ArrayList<String> weekList;
    private ArrayList<String> labelList;
    private ArrayList<BarEntry> infectionList;
    private ArrayList<BarEntry> vaccinationsList;
    private AreaManager am;
    float reference_timestamp = 1.5775704e12f;
    Long referenceTimestamp;

    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter1;
    Spinner spinnerWeeks;
    String area;
    String week;
    String infections;
    TextView textViewInfectionsVal;
    TextView textViewArea;
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        barChart = findViewById(R.id.chart1);

        textViewInfectionsVal = (TextView) findViewById(R.id.textViewInfectionsVal);
        textViewArea = (TextView) findViewById(R.id.textViewArea);
        am = new AreaManager();
        weekList = new ArrayList<>();
        labelList = am.getLabels();
        Collections.reverse(labelList);

        Spinner spinnerAreas = (Spinner) findViewById(R.id.spinnerAreas);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, labelList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAreas.setAdapter(adapter);
        spinnerAreas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    area = adapterView.getItemAtPosition(i).toString();
                    am.readInfectionJSON(area);
                    am.readVaccinationJSON(area);
                    weekList = am.getWeeks();
                    Collections.reverse(weekList);
                    adapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, weekList);
                    spinnerWeeks.setAdapter(adapter1);

                    infectionList = am.getInfections();
                    vaccinationsList = am.getVaccinations();

                    java.sql.Timestamp ts2 = java.sql.Timestamp.valueOf("2019-12-29 00:00:00.0");
                    long referenceTimestamp = ts2.getTime();
                    IndexAxisValueFormatter xAxisFormatter = new HourAxisValueFormatter(referenceTimestamp);
                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setValueFormatter(xAxisFormatter);

                    BarDataSet barDataSet1 = new BarDataSet(infectionList, "Infections");
                    barDataSet1.setColors(Color.rgb(155,0,0));
                    barDataSet1.setValueTextColor(Color.RED);
                    barDataSet1.setValueTextSize(16f);
                    BarDataSet barDataSet2 = new BarDataSet(vaccinationsList, "Vaccinations");
                    barDataSet2.setColors(Color.rgb(0,0,155));
                    barDataSet2.setValueTextColor(Color.BLUE);
                    barDataSet2.setValueTextSize(16f);
                    ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                    dataSets.add(barDataSet1);
                    dataSets.add(barDataSet2);

                    BarData data = new BarData(dataSets);
                    data.setValueTextSize(10f);
                    data.setBarWidth(20f);

                    barChart.setData(data);
                    //barChart.notifyDataSetChanged();
                    barChart.invalidate();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinnerWeeks = (Spinner) findViewById(R.id.spinnerWeeks);
        adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, weekList);
        adapter1.setNotifyOnChange(true);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWeeks.setAdapter(adapter1);
        spinnerWeeks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                week = adapterView.getItemAtPosition(i).toString();
                infections = am.getInfection(week);
                textViewArea.setText(area + ": " + week);
                if (infections.equals("..")) {
                    textViewInfectionsVal.setText(getString(R.string.no_data));
                    return;
                }
                textViewInfectionsVal.setText(infections);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.search);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.search:
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(),Settings.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}