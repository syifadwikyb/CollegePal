package com.example.collegepal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.collegepal.database.DatabaseCP;
import com.example.collegepal.util.PasswordHashing;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;


public class SignUp extends AppCompatActivity {
    private TextInputEditText fullName;
    private TextInputEditText email;
    private TextInputEditText password;
    private TextInputEditText confirmPassword;
    private Button signUp;

    private void initComponents() {
        fullName = findViewById(R.id.fullnameCardView).
                findViewById(R.id.fullnameInputLayout).
                findViewById(R.id.fullnameInputEditText);
        email = findViewById(R.id.emailCardView).
                findViewById(R.id.emailInputLayout).
                findViewById(R.id.emailInputEditText);
        password = findViewById(R.id.passwordCardView).
                findViewById(R.id.passwordInputLayout).
                findViewById(R.id.passwordInputEditText);
        confirmPassword = findViewById(R.id.confirmPasswordCardView).
                findViewById(R.id.confirmPasswordInputLayout).
                findViewById(R.id.confirmPasswordInputEditText);
        signUp = findViewById(R.id.signUpAccount);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initComponents();

        signUp.setOnClickListener(v -> {
            String dataFullName = Objects.requireNonNull(fullName.getText()).toString();
            String dataEmail = Objects.requireNonNull(email.getText()).toString();
            String dataPassword = Objects.requireNonNull(password.getText()).toString();
            String dataConfirmPassword = Objects.requireNonNull(confirmPassword.getText()).toString();

            if (dataFullName.isEmpty() || dataEmail.isEmpty() || dataPassword.isEmpty()) {
                Toast.makeText(SignUp.this, "Data pengguna tidak boleh kosong", Toast.LENGTH_LONG).show();
            } else {
                String hashedPassword = PasswordHashing.hashPassword(dataPassword);
                if (dataPassword.equals(dataConfirmPassword)) {
                    DatabaseCP databaseCP = new DatabaseCP(SignUp.this);
                    databaseCP.signUpAccount(dataFullName, dataEmail, hashedPassword);
                    databaseCP.close();

                    Intent intent = new Intent (SignUp.this, Login.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(SignUp.this, "Harap konfirmasi password dengan benar", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
