package com.example.olio_ht;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {
    private TextView name;
    private DatabaseHelp DB;
    private AreaManager am;
    private String areax;
    private String week;
    private String infections;
    private String vaccinations;
    private TextView textViewInfectionsVal1;
    private TextView textViewVaccinationsVal1;
    private TextView textViewArea1;
    private TextView textViewInfectionsVal2;
    private TextView textViewVaccinationsVal2;
    private TextView textViewArea2;
    private TextView textViewInfectionsVal3;
    private TextView textViewVaccinationsVal3;
    private TextView textViewArea3;
    private TextView textViewInfectionsVal = null;
    private TextView textViewVaccinationsVal = null;
    private TextView textViewArea = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        DB = new DatabaseHelp(this);
        DB.setUserLastActivity("MainActivity");
        am = AreaManager.getInstance();

        name = (TextView) findViewById(R.id.textViewUserName);
        textViewInfectionsVal1 = (TextView) findViewById(R.id.textViewInfVal);
        textViewVaccinationsVal1 = (TextView) findViewById(R.id.textViewVacVal);
        textViewArea1 = (TextView) findViewById(R.id.textViewArea);
        textViewInfectionsVal2 = (TextView) findViewById(R.id.textViewInfValMain2);
        textViewVaccinationsVal2 = (TextView) findViewById(R.id.textViewVacValMain2);
        textViewArea2 = (TextView) findViewById(R.id.textViewAreaMain2);
        textViewInfectionsVal3 = (TextView) findViewById(R.id.textViewInfValMain3);
        textViewVaccinationsVal3 = (TextView) findViewById(R.id.textViewVacValMain3);
        textViewArea3 = (TextView) findViewById(R.id.textViewAreaMain3);

        name.setText(DB.getUsername());

        for (int i=1; i<4; i++) {
            areax = DB.getArea(i);
            try {
                if (am.readInfectionJSON(areax) && am.readVaccinationJSON(areax)) {
                    am.getWeeks();
                    week = am.getLatestWeek();
                    infections = am.getInfection(week);
                    vaccinations = am.getVaccination(week);
                    if (i == 1) {
                        textViewArea = textViewArea1;
                        textViewInfectionsVal = textViewInfectionsVal1;
                        textViewVaccinationsVal = textViewVaccinationsVal1;
                    } else if (i == 2) {
                        textViewArea = textViewArea2;
                        textViewInfectionsVal = textViewInfectionsVal2;
                        textViewVaccinationsVal = textViewVaccinationsVal2;
                    } else if (i == 3) {
                        textViewArea = textViewArea3;
                        textViewInfectionsVal = textViewInfectionsVal3;
                        textViewVaccinationsVal = textViewVaccinationsVal3;
                    }
                    week = week.replace("Vuosi ", "");
                    week = week.replace("Viikko", getString(R.string.week));
                    week = week.replace("Kaikki ajat",getString(R.string.all_time));
                    textViewArea.setText(areax + ": " + week);
                    textViewInfectionsVal.setText(infections);
                    textViewVaccinationsVal.setText(vaccinations);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        DB.close();

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