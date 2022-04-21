package com.example.olio_ht;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

public class Settings extends AppCompatActivity {

    private Context context = null;
    private View view;
    private Spinner spinner;
    private Button logoutbutton;
    private Switch switchDarkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        context = Settings.this;
        logoutbutton = (Button) findViewById(R.id.buttonLogout);
        switchDarkMode = (Switch) findViewById(R.id.switch1);

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.settings);

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
                    case R.id.settings:
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        Spinner spinner = findViewById(R.id.spinner);
        CharSequence[] fillist = getResources().getStringArray(R.array.spinner_names);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(context,android.R.layout.simple_spinner_dropdown_item, fillist);
        spinner.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).toString().equals("suomi")) {
                    setLocale("fi");
                    Intent intent = new Intent(getApplicationContext(), Settings.class);
                    startActivity(intent);
                } else if (adapterView.getItemAtPosition(i).toString().equals("englanti")) {
                    setLocale("en");
                    Intent intent = new Intent(getApplicationContext(), Settings.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO);
                    Intent intent = new Intent(getApplicationContext(), Settings.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void setLocale(String language) {
        Resources resources =getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = new Locale(language);
        resources.updateConfiguration(configuration,metrics);
        onConfigurationChanged(configuration);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}