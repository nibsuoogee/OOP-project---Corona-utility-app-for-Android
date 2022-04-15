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
    private AreaManager am;
    private long referenceTimestamp;
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
                    am.readJSON(area);
                    weekList = am.getWeeks();
                    Collections.reverse(weekList);
                    adapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, weekList);
                    spinnerWeeks.setAdapter(adapter1);

                    infectionList = am.getInfections();

                    XAxis xAxis = barChart.getXAxis();
                    IndexAxisValueFormatter xAxisFormatter = new HourAxisValueFormatter(referenceTimestamp);
                    xAxis.setValueFormatter(xAxisFormatter);
                    /*xAxis.setValueFormatter(new DateValueFormatter() {
                        private final SimpleDateFormat mFormat = new SimpleDateFormat("yyyyww", Locale.ENGLISH);
                        @Override
                        public String getFormattedValue(float value, AxisBase axis) {

                            return mFormat.format(new Date(new Float(value).longValue()).toString());
                        }
                    });*/
                    BarDataSet barDataSet = new BarDataSet(infectionList, "Infections");
                    barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                    barDataSet.setValueTextColor(Color.RED);
                    barDataSet.setValueTextSize(16f);
                    BarData barData = new BarData(barDataSet);
                    barData.setBarWidth(20f);
                    barChart.setFitBars(true);
                    barChart.setData(barData);

                    ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                    dataSets.add(barDataSet);

                    BarData data = new BarData(dataSets);
                    data.setValueTextSize(10f);
                    data.setBarWidth(0.9f);

                    barChart.setData(data);
                    barChart.notifyDataSetChanged();

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