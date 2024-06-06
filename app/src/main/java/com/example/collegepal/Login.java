package com.example.collegepal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.collegepal.database.DatabaseCP;
import com.example.collegepal.database.entity.UserModel;
import com.example.collegepal.util.PasswordHashing;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class Login extends AppCompatActivity {
    private Button signIn;
    private TextView signUp;
    private TextInputEditText email;
    private TextInputEditText password;

    private void initComponents() {
        signIn = findViewById(R.id.signIn);
        signUp = findViewById(R.id.signUp);
        email = findViewById(R.id.emailLoginCardView).
                findViewById(R.id.emailLoginInputLayout).
                findViewById(R.id.emailLoginInputEditText);
        password = findViewById(R.id.passwordLoginCardView).
                findViewById(R.id.passwordLoginInputLayout).
                findViewById(R.id.passwordLoginInputEditText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComponents();

        signIn.setOnClickListener(v -> {
            String stringEmail = Objects.requireNonNull(email.getText()).toString();
            String stringPassword = Objects.requireNonNull(password.getText()).toString();

            if (stringEmail.isEmpty() || stringPassword.isEmpty()) {
                Toast.makeText(Login.this, "Data pengguna tidak boleh kosong", Toast.LENGTH_LONG).show();
            } else {
                String hashedPassword = PasswordHashing.hashPassword(stringPassword);
                DatabaseCP databaseCP = new DatabaseCP(Login.this);
                if (databaseCP.signInAccount(stringEmail, hashedPassword)) {
                    UserModel.setId(databaseCP.getUserId(stringEmail));
                    new UserModel(databaseCP.getUserFullName(UserModel.getId()), stringEmail, hashedPassword);
                    Intent intent = new Intent (Login.this, Assignment.class);
                    startActivity(intent);
                }
                databaseCP.close();
            }
        });

        signUp.setOnClickListener(v -> {
            Intent intent = new Intent (Login.this, SignUp.class);
            startActivity(intent);
        });
    }
}