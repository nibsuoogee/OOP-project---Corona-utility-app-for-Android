package com.example.olio_ht;

import static com.example.olio_ht.HashFunction.getHash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    private Button signin, register;
    private DatabaseHelp DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText) findViewById(R.id.editTextUsername1);
        password = (EditText) findViewById(R.id.editTextPassword1);
        signin = (Button) findViewById(R.id.buttonLogin1);
        register = (Button) findViewById(R.id.buttonToRegister);
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
                    System.out.println("salt log: "+salt);
                    pass = getHash(pass, salt);
                    System.out.println("pass log: "+pass);
                    Boolean checkuserpass = DB.checkUsernamePassword(user, pass);
                    if (checkuserpass == true) {
                        Toast.makeText(LoginActivity.this,getString(R.string.sign_in_success), Toast.LENGTH_LONG).show();
                        DB.makeCurrent(user, pass);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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