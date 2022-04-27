package com.example.olio_ht;

import static com.example.olio_ht.HashFunction.getHash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    private Button signin, register;
    private DatabaseHelp DB;
    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText) findViewById(R.id.editTextUsername1);
        password = (EditText) findViewById(R.id.editTextPassword1);
        signin = (Button) findViewById(R.id.buttonLogin1);
        register = (Button) findViewById(R.id.buttonToRegister);
        progressbar = (ProgressBar) findViewById(R.id.progressBar);
        DB = new DatabaseHelp(this);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String salt;
                if (user.equals("")||pass.equals("")) {
                    Toast.makeText(LoginActivity.this,getString(R.string.empty_fields), Toast.LENGTH_LONG).show();
                } else {
                    salt = DB.getSalt(user);
                    pass = getHash(pass, salt);
                    Boolean checkuserpass = DB.checkUsernamePassword(user, pass);
                    if (checkuserpass == true) {
                        progressbar.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this,getString(R.string.sign_in_success), Toast.LENGTH_LONG).show();
                        DB.makeCurrent(user, pass);
                        Intent intent = null;
                        switch (DB.getUserLastActivity()) {
                            case "MainActivity": intent = new Intent(getApplicationContext(), MainActivity.class); break;
                            case "Search": intent = new Intent(getApplicationContext(), Search.class); break;
                            case "Settings": intent = new Intent(getApplicationContext(), Settings.class); break;
                        }

                        if (DB.getNightMode().equals("on")) {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        } else {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        }
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this,getString(R.string.invalid_credentials), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}