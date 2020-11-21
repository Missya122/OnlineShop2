package com.example.onlineshop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private Button CreateAccountButton;
    private EditText InputName, InputPhoneNumber, InputPassword;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CreateAccountButton = (Button) findViewById(R.id.register_btn);
        InputName = (EditText) findViewById(R.id.register_username_input);
        InputPassword = (EditText) findViewById(R.id.register_password_input);
        InputPhoneNumber = (EditText) findViewById(R.id.register_phone_number_input);
        loadingBar = new ProgressDialog(this);

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount()
    {
        String name = InputName.getText().toString();
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this,"Proszę wpisać nazwę użytkownika!", Toast.LENGTH_SHORT);
        }
        else if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this,"Proszę wpisać numer telefonu!", Toast.LENGTH_SHORT);
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Proszę wpisać hasło!", Toast.LENGTH_SHORT);
        }
        else
        {
            loadingBar.setTitle("Stwórz konto");
            loadingBar.setMessage("Proszę czekać, trwa weryfikcja");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatePhoneNumber(name, phone, password);
        }
    }

    private void ValidatePhoneNumber(String name, String phone, String password) {
       final DatabaseReference RootRef;
       RootRef = FirebaseDatabase.getInstance().getReference();
    }

}