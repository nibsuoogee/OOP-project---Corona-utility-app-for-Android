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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
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
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.github.mikephil.charting.charts.BarChart;

public class Search extends AppCompatActivity {
    private ArrayList<String> weekList;
    private ArrayList<String> labelList;
    private List<Entry> infectionList;
    private List<Entry> vaccinationsList;
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
    private LineChart barChart;

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

                    //java.sql.Timestamp ts2 = java.sql.Timestamp.valueOf("2019-12-29 00:00:00.0");
                    //long referenceTimestamp = ts2.getTime();
                    //IndexAxisValueFormatter xAxisFormatter = new HourAxisValueFormatter(referenceTimestamp);
                    //XAxis xAxis = barChart.getXAxis();
                    //xAxis.setValueFormatter(xAxisFormatter);

                    LineDataSet barDataSet1 = new LineDataSet(infectionList, "Infections");
                    barDataSet1.setColor(Color.RED);
                    barDataSet1.setValueTextColor(Color.RED);
                    //barDataSet1.setBarBorderWidth(4f);
                    //barDataSet1.setBarBorderColor(Color.RED);
                    //barDataSet1.setValueTextSize(14f);
                    LineDataSet barDataSet2 = new LineDataSet(vaccinationsList, "Vaccinations");
                    barDataSet2.setColor(Color.BLUE);
                    //barDataSet2.setBarBorderWidth(2f);
                    //barDataSet2.setBarBorderColor(Color.BLUE);
                    barDataSet2.setValueTextColor(Color.BLUE);
                    //barDataSet2.setValueTextSize(8f);

                    List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                    dataSets.add(barDataSet1);
                    dataSets.add(barDataSet2);

                    //data.addDataSet(barDataSet1);
                    //data.addDataSet(barDataSet2);

                    LineData data = new LineData(dataSets);
                    barChart.setData(data);
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