package com.anna.stock;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

//Для layout-start. Регистрация и вход в Firebase
public class Start extends AppCompatActivity implements View.OnClickListener {

    EditText et_email, et_password;
    Button b_signin, b_registration;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        b_signin = findViewById(R.id.b_signin);
        b_signin.setOnClickListener(this);
        b_registration = findViewById(R.id.b_registration);
        b_registration.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.b_signin: //Войти
                signin(et_email.getText().toString(), et_password.getText().toString());
                break;
            case R.id.b_registration: //Зарегистрироваться
                registration(et_email.getText().toString(), et_password.getText().toString());
                break;
        }
    }

    //Вход в Firebase
    public void signin (String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(Start.this, MainActivity.class);
                    startActivity(intent);
                } else
                    Toast.makeText(Start.this, "Неверный логин или пароль", Toast.LENGTH_LONG).show();
            }
        });
    }

    //Регистрация в Firebase
    public void registration (String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Start.this, "Регистрация успешно завершена", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(Start.this, "Ошибка в регистрации", Toast.LENGTH_LONG).show();
            }

        });
    }
}
