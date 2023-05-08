package com.example.meme_dating;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Register extends AppCompatActivity {

    TextInputEditText textInputEditTextUsername, textInputEditTextPassword;
    Button buttonRegister;
    TextView textViewLogin;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textInputEditTextUsername = findViewById(R.id.username);
        textInputEditTextPassword = findViewById(R.id.password);
        buttonRegister = findViewById(R.id.buttonSignUp);
        textViewLogin = findViewById(R.id.loginText);
        progressBar = findViewById(R.id.progress);

        textViewLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username, password;
                username = String.valueOf(textInputEditTextUsername.getText());
                password = String.valueOf(textInputEditTextPassword.getText());

                if (!username.equals("") && !password.equals("")) {
                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[2];
                            field[0] = "username";
                            field[1] = "password";
                            String[] data = new String[2];
                            data[0] = username;
                            data[1] = password;
                            PutData putData = new PutData("https://meme-dating.one.pl/LoginRegister/signup.php", "POST", field, data);
                            if(putData == null){
                                Toast.makeText(getApplicationContext(), "Wrong IP Address", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
                                        progressBar.setVisibility(View.GONE);
                                        String result = putData.getResult();
                                        if (result.equals("Signed up")) {
                                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), CategoryMenuActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}