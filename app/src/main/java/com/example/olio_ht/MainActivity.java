package com.example.olio_ht;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    private TextView name;
    private DatabaseHelp DB;
    private AreaManager am;
    private String area;
    private String week;
    private String infections;
    private String vaccinations;
    private TextView textViewInfectionsVal;
    private TextView textViewVaccinationsVal;
    private TextView textViewArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        name = (TextView) findViewById(R.id.textViewUserName);
        textViewInfectionsVal = (TextView) findViewById(R.id.textViewInfValMain1);
        textViewVaccinationsVal = (TextView) findViewById(R.id.textViewVacValMain1);
        textViewArea = (TextView) findViewById(R.id.textViewAreaMain1);
        DB = new DatabaseHelp(this);
        name.setText(DB.getUsername());
        area = DB.getArea();
        am = AreaManager.getInstance();
        try {
            am.readInfectionJSON(area);
            am.readVaccinationJSON(area);
        } catch (JSONException e) {

        }
        am.getWeeks();
        week = am.getLatestWeek();
        infections = am.getInfection(week);
        vaccinations = am.getVaccination(week);
        textViewArea.setText(area + ": " + week);
        textViewInfectionsVal.setText(infections);
        textViewVaccinationsVal.setText(vaccinations);

/*
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                try {
                    am.readJSON();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Button btn1 = findViewById(R.id.buttonRead);
        btn1.setOnClickListener(listener);

        View.OnClickListener listener2 = new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                try {
                    am.readJSONid();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Button btn2 = findViewById(R.id.buttonRead2);
        btn2.setOnClickListener(listener2);
*/
        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(),Search.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
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