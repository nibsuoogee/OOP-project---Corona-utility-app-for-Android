package com.example.olio_ht;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
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
import com.github.mikephil.charting.formatter.ValueFormatter;
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
    private List<BarEntry> infectionList;
    private List<BarEntry> vaccinationsList;
    private AreaManager am;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapter1;
    private Spinner spinnerWeeks;
    private String area;
    private String week;
    private String infections;
    private String vaccinations;
    private TextView textViewInfectionsVal;
    private TextView textViewVaccinationsVal;
    private TextView textViewArea;
    private Context context = null;
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //context = activity_search.this;
        setTitle(getString(R.string.search));
        barChart = findViewById(R.id.chart1);

        textViewInfectionsVal = (TextView) findViewById(R.id.textViewInfectionsVal);
        textViewVaccinationsVal = (TextView) findViewById(R.id.textViewVaccinationsVal);
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

                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setValueFormatter(new LineChartXAxisValueFormatter());

                    BarDataSet barDataSet1 = new BarDataSet(infectionList, getString(R.string.infections));
                    BarDataSet barDataSet2 = new BarDataSet(vaccinationsList, getString(R.string.vaccinations));
                    barDataSet1.setColor(Color.RED);
                    barDataSet1.setBarBorderWidth(1f);
                    barDataSet1.setBarBorderColor(Color.RED);
                    barDataSet2.setColor(Color.CYAN);
                    barDataSet2.setBarBorderWidth(0.5f);
                    barDataSet2.setBarBorderColor(Color.CYAN);

                    BarData data = new BarData();
                    data.addDataSet(barDataSet2);
                    data.addDataSet(barDataSet1);

                    data.setBarWidth(4e8f);
                    data.setValueTextSize(10f);
                    data.setDrawValues(false);

                    barChart.getDescription().setEnabled(false);
                    barChart.setMaxVisibleValueCount(700);
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
                } else {
                    textViewInfectionsVal.setText(infections);
                }
                vaccinations = am.getVaccination(week);
                if ((vaccinations == null) || (vaccinations.equals(".."))) {
                    textViewVaccinationsVal.setText(getString(R.string.no_data));
                    return;
                } else {
                    textViewVaccinationsVal.setText(vaccinations);
                }

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