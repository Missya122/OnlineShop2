package com.example.onlineshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineshop.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {

    private String check = "";
    private TextView pageTitle, titleQuestions;
    private EditText phoneNumber, question1, question2;
    private Button verifyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        check = getIntent().getStringExtra("check");

        pageTitle = findViewById(R.id.page_title);
        titleQuestions = findViewById(R.id.title_questions);
        phoneNumber = findViewById(R.id.find_phone_number);
        question1 = findViewById(R.id.question_1);
        question2 = findViewById(R.id.question_2);
        verifyButton = findViewById(R.id.verify_btn);


    }

    @Override
    protected void onStart() {
        super.onStart();

        phoneNumber.setVisibility(View.GONE);

        if(check.equals("settings"))
        {
            pageTitle.setText("Ustaw pytania");
            titleQuestions.setText("Ustaw odpowiedzi na poniższe pytania");

            verifyButton.setText("Ustaw");
            displayPreviousAnswers();
            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAnswers();


                }
            });

        }
        else if( check.equals("login"))
        {
            phoneNumber.setVisibility(View.VISIBLE);

            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verifyUser();
                }
            });

        }
    }

    private void setAnswers()
    {
        String answer1 = question1.getText().toString().toLowerCase();
        String answer2 = question2.getText().toString().toLowerCase();

        if(question1.equals("") && question2.equals("")) {
            Toast.makeText(ResetPasswordActivity.this,"Proszę odpowiedzieć na oba pytania",Toast.LENGTH_SHORT).show();

        }else {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(Prevalent.currentOnlineUser.getPhone());

            HashMap<String,Object> userdataMap = new HashMap<>();
            userdataMap.put("answer1",answer1);
            userdataMap.put("answer2",answer2);

            ref.child("SecurityQuestions").updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ResetPasswordActivity.this,"Zmiana poprawna", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ResetPasswordActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    private void displayPreviousAnswers()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(Prevalent.currentOnlineUser.getPhone());

        ref.child("SecurityQuestions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if( snapshot.exists() ) {
                    String ans1= snapshot.child("answer1").getValue().toString();
                    String ans2 = snapshot.child("answer2").getValue().toString();

                    question1.setText(ans1);
                    question2.setText(ans2);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void verifyUser()
    {
        String phone = phoneNumber.getText().toString();
        String answer1 = question1.getText().toString().toLowerCase();
        String answer2 = question2.getText().toString().toLowerCase();

        if(!phone.equals("") && !answer1.equals("") && !answer2.equals("")) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(phone);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if( snapshot.exists()) {
                        String mPhone = snapshot.child("phone").getValue().toString();

                        if(snapshot.hasChild("SecurityQuestions")){
                            String ans1= snapshot.child("SecurityQuestions").child("answer1").getValue().toString();
                            String ans2 = snapshot.child("SecurityQuestions").child("answer2").getValue().toString();

                            if(!ans1.equals(answer1) ){
                                Toast.makeText(ResetPasswordActivity.this,"Twoja pierwsza odpowiedź jest błędna",Toast.LENGTH_SHORT).show();

                            }
                            else if(!ans2.equals(answer2) ){
                                Toast.makeText(ResetPasswordActivity.this,"Twoja druga odpowiedź jest błędna",Toast.LENGTH_SHORT).show();

                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("Nowe hasło");

                                final EditText newPassword = new EditText(ResetPasswordActivity.this);
                                newPassword.setHint("Wpisz nowe hasło");
                                builder.setView(newPassword);
                                builder.setPositiveButton("Zmień", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(!newPassword.getText().toString().equals("")) {
                                            ref.child("password").setValue(newPassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()) {
                                                                Toast.makeText(ResetPasswordActivity.this,"Hasło zmienione poprawnie",Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(ResetPasswordActivity.this,LoginActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        }
                                                    });
                                        }else{

                                        }
                                    }
                                });
                                builder.setNegativeButton("Zamknij", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
                            }
                        }
                        else{
                            Toast.makeText(ResetPasswordActivity.this,"Nie ustawiłeś pytań bezpieczeństwa.",Toast.LENGTH_SHORT).show();;
                        }
                    }
                    else{
                        Toast.makeText(ResetPasswordActivity.this,"Ten numer telefonu nie istnieje",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            Toast.makeText(ResetPasswordActivity.this,"Proszę uzupełnić wszystkie pola.",Toast.LENGTH_SHORT).show();
        }


    }
}