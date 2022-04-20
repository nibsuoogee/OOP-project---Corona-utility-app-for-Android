package com.example.olio_ht;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    EditText username, password, repassword;
    Button register, signin;
    DatabaseHelp DB;
    Context context = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.editTextUsername);
        password = (EditText) findViewById(R.id.editTextPassword);
        repassword = (EditText) findViewById(R.id.editTextRePassword);
        register = (Button) findViewById(R.id.buttonRegister);
        signin = (Button) findViewById(R.id.buttonToLogin);
        DB = new DatabaseHelp(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();
                context = RegisterActivity.this;
                //context.deleteDatabase("Login.db");
                if (user.equals("")||pass.equals("")||repass.equals("")) {
                    Toast.makeText(RegisterActivity.this,"Empty fields detected", Toast.LENGTH_LONG).show();
                } else {
                    if (pass.equals(repass)) {
                        Boolean checkuser = DB.checkUsername(user);
                        if (checkuser == false) {
                            Boolean insert = DB.insertData(user, pass, "false", "Espoo", "Helsinki", "Vantaa");
                            if (insert == true) {
                                Toast.makeText(RegisterActivity.this,"Registered successfully!", Toast.LENGTH_LONG).show();
                                DB.makeCurrent(user, pass);
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(RegisterActivity.this,"Registration failed", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this,"User already exists. Please login", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this,"Passwords do not match", Toast.LENGTH_LONG).show();
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
    }
}