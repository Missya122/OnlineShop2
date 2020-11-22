package com.example.onlineshop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText InputNumber, InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton = (Button) findViewById(R.id.login_btn);
        InputPassword = (EditText) findViewById(R.id.login_password_input);
        InputNumber = (EditText) findViewById(R.id.login_phone_number_input);
        loadingBar = new ProgressDialog(this);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

    }

    private void loginUser() {
        String phone = InputNumber.getText().toString();
        String password = InputPassword.getText().toString();

         if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this,"Proszę wpisać numer telefonu!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Proszę wpisać hasło!", Toast.LENGTH_SHORT).show();
        }
        else {

            loadingBar.setTitle("Stwórz konto");
             loadingBar.setMessage("Proszę czekać, trwa weryfikcja");
             loadingBar.setCanceledOnTouchOutside(false);
             loadingBar.show();

             AllowAccessToAccount(phone,password);
         }
    }

    private void AllowAccessToAccount(String phone, String password) {


    }
}