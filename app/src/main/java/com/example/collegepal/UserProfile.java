package com.example.collegepal;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.collegepal.database.DatabaseCP;
import com.example.collegepal.database.entity.UserModel;
import com.example.collegepal.util.NumberingFormat;
import com.example.collegepal.util.PasswordHashing;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class UserProfile extends AppCompatActivity {
    TextView userFullName;
    TextView userEmail;
    TextView totalIncome;
    TextView totalOutcome;
    Button editProfile;
    Button editPassword;
    Button logout;
    ImageButton assignment;
    ImageButton moneyManagement;
    ImageButton schedule;

    private void initComponents() {
        userFullName = findViewById(R.id.userFullName);
        userEmail = findViewById(R.id.userEmail);
        totalIncome = findViewById(R.id.totalIncomeUserProfile);
        totalOutcome = findViewById(R.id.totalOutcomeUserProfile);
        editProfile = findViewById(R.id.editProfile);
        editPassword = findViewById(R.id.changePassword);
        logout = findViewById(R.id.logout);
        assignment = findViewById(R.id.assignmentButtonUserProfile);
        moneyManagement = findViewById(R.id.moneyButtonUserProfile);
        schedule = findViewById(R.id.scheduleButtonUserProfile);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        DatabaseCP databaseCP = new DatabaseCP(UserProfile.this);
        initComponents();

        userFullName.setText(databaseCP.getUserFullName(UserModel.getId()));
        userEmail.setText(databaseCP.getUserEmail(UserModel.getId()));
        totalIncome.setText(getResources().getString(R.string.formatted_money_value, NumberingFormat.ThousandSeparator(databaseCP.totalIncomeMoney(UserModel.getId()))));
        totalOutcome.setText(getResources().getString(R.string.formatted_money_value, NumberingFormat.ThousandSeparator(databaseCP.totalOutcomeMoney(UserModel.getId()))));

        editProfile.setOnClickListener(v -> {
            Intent intent = new Intent (UserProfile.this, GeneralProfile.class);
            startActivity(intent);
        });
        editPassword.setOnClickListener(v -> showPopUpChangePassword());
        logout.setOnClickListener(v -> showPopUpLogOut());

        assignment.setOnClickListener(v -> {
            Intent intent = new Intent (UserProfile.this, Assignment.class);
            startActivity(intent);
        });
        moneyManagement.setOnClickListener(v -> {
            Intent intent = new Intent (UserProfile.this, MoneyManagement.class);
            startActivity(intent);
        });
        schedule.setOnClickListener(v -> {
            Intent intent = new Intent (UserProfile.this, Schedule.class);
            startActivity(intent);
        });
        databaseCP.close();
    }

    private void showPopUpChangePassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_edit_password_template, null);
        builder.setView(view);

        DatabaseCP databaseCP = new DatabaseCP(UserProfile.this);
        TextInputEditText oldPassword = view.findViewById(R.id.oldPasswordCardView).findViewById(R.id.oldPasswordInputEditText);
        TextInputEditText newPassword = view.findViewById(R.id.newPasswordCardView).findViewById(R.id.newPasswordInputEditText);
        TextInputEditText confirmNewPassword = view.findViewById(R.id.confirmNewPasswordCardView).findViewById(R.id.confirmNewPasswordInputEditText);
        Button cancelButton = view.findViewById(R.id.editPasswordCancelButton);
        Button okButton = view.findViewById(R.id.editPasswordOkButton);

        AlertDialog dialog = builder.create();

        cancelButton.setOnClickListener(v -> dialog.dismiss());
        okButton.setOnClickListener(v -> {
            String dataOldPassword = Objects.requireNonNull(oldPassword.getText()).toString();
            String dataNewPassword = Objects.requireNonNull(newPassword.getText()).toString();
            String dataConfirm = Objects.requireNonNull(confirmNewPassword.getText()).toString();

            if (dataOldPassword.isEmpty()) {
                Toast.makeText(UserProfile.this, "Password lama tidak boleh kosong", Toast.LENGTH_SHORT).show();
            } else if (dataNewPassword.isEmpty()) {
                Toast.makeText(UserProfile.this, "Password baru tidak boleh kosong", Toast.LENGTH_SHORT).show();
            } else if (dataConfirm.isEmpty()) {
                Toast.makeText(UserProfile.this, "Konfirmasi password baru tidak boleh kosong", Toast.LENGTH_SHORT).show();
            } else {
                String hashOldPassword = PasswordHashing.hashPassword(dataOldPassword);
                String hashNewPassword = PasswordHashing.hashPassword(dataNewPassword);
                String hashConfirm = PasswordHashing.hashPassword(dataConfirm);

                if (hashOldPassword.equals(databaseCP.getUserPassword(UserModel.getId())) && hashNewPassword.equals(hashConfirm)) {
                    databaseCP.updateUserPassword(UserModel.getId(), hashNewPassword);
                    dialog.dismiss();
                } else if (hashOldPassword.equals(databaseCP.getUserPassword(UserModel.getId())) && !hashNewPassword.equals(hashConfirm)) {
                    Toast.makeText(UserProfile.this, "Harap konfirmasi password baru dengan benar", Toast.LENGTH_SHORT).show();
                } else if (!hashOldPassword.equals(databaseCP.getUserPassword(UserModel.getId())) && hashNewPassword.equals(hashConfirm)) {
                    Toast.makeText(UserProfile.this, "Password lama anda yang masukan salah", Toast.LENGTH_SHORT).show();
                } else if (!hashOldPassword.equals(databaseCP.getUserPassword(UserModel.getId())) && !hashNewPassword.equals(hashConfirm)) {
                    Toast.makeText(UserProfile.this, "Password lama anda yang masukan salah", Toast.LENGTH_SHORT).show();
                }
            }
        });

        databaseCP.close();
        dialog.show();
    }

    private void showPopUpLogOut() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(UserProfile.this);

        dialog.setTitle("Anda akan logout");
        dialog.setMessage("Apakah anda yakin ingin logout?");
        dialog.setPositiveButton("Ya", (dialog1, which) -> {
            Intent intent = new Intent (UserProfile.this, Login.class);
            startActivity(intent);

            Toast.makeText(UserProfile.this, "Anda telah logout", Toast.LENGTH_LONG).show();
        });
        dialog.setNegativeButton("Tidak", (dialog2, which) -> dialog2.dismiss());

        AlertDialog dialogCreate = dialog.create();
        dialogCreate.show();
    }
}
