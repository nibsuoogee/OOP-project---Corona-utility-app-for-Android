package com.example.olio_ht;

import static com.example.olio_ht.HashFunction.createSalt;
import static com.example.olio_ht.HashFunction.getHash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Locale;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^" + "(?=.*[0-9])" + "(?=.*[a-z])" + "(?=.*[A-Z])" + "(?=.*[#@$%^=&+])" + ".{12,}" + "$");
    private EditText username, password, repassword;
    private Button register, signin;
    private DatabaseHelp DB;
    private Context context = null;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = RegisterActivity.this;
        username = (EditText) findViewById(R.id.editTextUsername);
        password = (EditText) findViewById(R.id.editTextPassword);
        repassword = (EditText) findViewById(R.id.editTextRePassword);
        register = (Button) findViewById(R.id.buttonRegister);
        signin = (Button) findViewById(R.id.buttonToLogin);
        DB = new DatabaseHelp(this);
        Spinner spinner = findViewById(R.id.spinner2);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();
                String salt;

                //context.deleteDatabase("Login.db");
                if (user.equals("")||pass.equals("")||repass.equals("")) {
                    Toast.makeText(RegisterActivity.this,getString(R.string.empty_fields), Toast.LENGTH_LONG).show();
                } else {
                    if (pass.equals(repass)) {
                        Boolean checkuser = DB.checkUsername(user);
                        if (checkuser == false) {
                            //if (PASSWORD_PATTERN.matcher(pass).matches()) {
                                salt = createSalt();
                                System.out.println("salt reg: "+salt);
                                pass = getHash(pass, salt);
                                System.out.println("pass reg: "+pass);
                                Boolean insert = DB.insertData(user, pass, "false", "Espoo", "Helsinki", "Vantaa", salt);
                                if (insert == true) {
                                    Toast.makeText(RegisterActivity.this,getString(R.string.successful_register), Toast.LENGTH_LONG).show();
                                    DB.makeCurrent(user, pass);
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                //} else {
                                //    Toast.makeText(RegisterActivity.this,getString(R.string.failed_register), Toast.LENGTH_LONG).show();
                                //}
                            } else {
                                password.setError(getString(R.string.weak_pass));
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this,getString(R.string.user_exists), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this,getString(R.string.password_no_match), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        CharSequence[] fillist = getResources().getStringArray(R.array.spinner_names);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(context,android.R.layout.simple_spinner_dropdown_item, fillist);
        spinner.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).toString().equals("suomi")) {
                    setLocale("fi");
                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(intent);
                } else if (adapterView.getItemAtPosition(i).toString().equals("englanti")) {
                    setLocale("en");
                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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