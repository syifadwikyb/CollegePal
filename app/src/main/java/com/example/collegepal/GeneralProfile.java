package com.example.collegepal;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.collegepal.database.DatabaseCP;
import com.example.collegepal.database.entity.UserModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class GeneralProfile extends AppCompatActivity {
    TextView email;
    TextView fullName;
    TextView number;
    TextView birth;
    Button cancelButton;
    Button saveButton;
    AtomicReference<String> newName = new AtomicReference<>();
    AtomicReference<String> newNumber = new AtomicReference<>();
    AtomicReference<String> newBirth = new AtomicReference<>();

    private void initComponents() {
        email = findViewById(R.id.generalProfileEmail);
        fullName = findViewById(R.id.editFullname);
        number = findViewById(R.id.editNumber);
        birth = findViewById(R.id.editBirth);
        cancelButton = findViewById(R.id.generalProfileCancelButton);
        saveButton = findViewById(R.id.generalProfileSaveButton);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_profile);

        DatabaseCP databaseCP = new DatabaseCP(GeneralProfile.this);
        initComponents();

        email.setText(databaseCP.getUserEmail(UserModel.getId()));
        fullName.setText(databaseCP.getUserFullName(UserModel.getId()));

        if (databaseCP.getUserNumber(UserModel.getId()) == null) {
            number.setText(getResources().getString(R.string.null_data));
        } else {
            number.setText(databaseCP.getUserNumber(UserModel.getId()));
        }

        if (databaseCP.getUserBirth(UserModel.getId()) == null) {
            birth.setText(getResources().getString(R.string.null_data));
        } else {
            birth.setText(databaseCP.getUserBirth(UserModel.getId()));
        }

        fullName.setOnClickListener(v -> showPopUpEditName());
        number.setOnClickListener(v -> showPopUpEditNumber());
        birth.setOnClickListener(v -> showPopUpEditBirth());

        cancelButton.setOnClickListener(v -> {
            Intent intent = new Intent (GeneralProfile.this, UserProfile.class);
            startActivity(intent);
        });
        saveButton.setOnClickListener(v -> {
            if (newName.get() != null && newNumber.get() != null && newBirth.get() != null) {
                databaseCP.updateUserFullName(UserModel.getId(), newName.get());
                databaseCP.updateUserNumber(UserModel.getId(), newNumber.get());
                databaseCP.updateUserBirth(UserModel.getId(), newBirth.get());
            } else if (newName.get() != null && newNumber.get() != null && newBirth.get() == null) {
                databaseCP.updateUserFullName(UserModel.getId(), newName.get());
                databaseCP.updateUserNumber(UserModel.getId(), newNumber.get());
            } else if (newName.get() != null && newNumber.get() == null && newBirth.get() != null) {
                databaseCP.updateUserFullName(UserModel.getId(), newName.get());
                databaseCP.updateUserBirth(UserModel.getId(), newBirth.get());
            } else if (newName.get() == null && newNumber.get() != null && newBirth.get() != null) {
                databaseCP.updateUserNumber(UserModel.getId(), newNumber.get());
                databaseCP.updateUserBirth(UserModel.getId(), newBirth.get());
            } else if (newName.get() != null && newNumber.get() == null && newBirth.get() == null) {
                databaseCP.updateUserFullName(UserModel.getId(), newName.get());
            } else if (newName.get() == null && newNumber.get() != null && newBirth.get() == null) {
                databaseCP.updateUserNumber(UserModel.getId(), newNumber.get());
            } else if (newName.get() == null && newNumber.get() == null && newBirth.get() != null) {
                databaseCP.updateUserBirth(UserModel.getId(), newBirth.get());
            }

            Intent intent = new Intent (GeneralProfile.this, UserProfile.class);
            startActivity(intent);
        });

        databaseCP.close();
    }

    private void showPopUpEditName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_edit_profile_name_template, null);
        builder.setView(view);

        DatabaseCP databaseCP = new DatabaseCP(GeneralProfile.this);
        TextInputEditText dataEdit = view.findViewById(R.id.editProfileNameCardView).findViewById(R.id.editProfileNameInputEditText);
        Button cancelButton = view.findViewById(R.id.editProfileNameCancelButton);
        Button okButton = view.findViewById(R.id.editProfileNameOkButton);

        dataEdit.setText(databaseCP.getUserFullName(UserModel.getId()));

        AlertDialog dialog = builder.create();

        cancelButton.setOnClickListener(v -> dialog.dismiss());
        okButton.setOnClickListener(v -> {
            String dataFullName = Objects.requireNonNull(dataEdit.getText()).toString();

            if (dataFullName.isEmpty()) {
                Toast.makeText(GeneralProfile.this, "Nama pengguna tidak boleh kosong", Toast.LENGTH_LONG).show();
            } else {
                fullName.setText(dataFullName);
                newName.set(dataFullName);
                dialog.dismiss();
            }
        });

        databaseCP.close();
        dialog.show();
    }

    private void showPopUpEditNumber() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_edit_profile_number_template, null);
        builder.setView(view);

        DatabaseCP databaseCP = new DatabaseCP(GeneralProfile.this);
        TextInputEditText dataEdit = view.findViewById(R.id.editProfileNumberCardView).findViewById(R.id.editProfileNumberInputEditText);
        Button cancelButton = view.findViewById(R.id.editProfileNumberCancelButton);
        Button okButton = view.findViewById(R.id.editProfileNumberOkButton);

        dataEdit.setText(databaseCP.getUserNumber(UserModel.getId()));

        AlertDialog dialog = builder.create();

        cancelButton.setOnClickListener(v -> dialog.dismiss());
        okButton.setOnClickListener(v -> {
            String dataNumber = Objects.requireNonNull(dataEdit.getText()).toString();

            number.setText(dataNumber);
            newNumber.set(dataNumber);
            dialog.dismiss();
        });

        databaseCP.close();
        dialog.show();
    }

    private void showPopUpEditBirth() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_edit_profile_birth_template, null);
        builder.setView(view);

        DatabaseCP databaseCP = new DatabaseCP(GeneralProfile.this);
        TextInputEditText dataEdit = view.findViewById(R.id.editProfileBirthInputLayout).findViewById(R.id.editProfileBirthCardView).findViewById(R.id.editProfileBirthInputEditText);
        ImageView calendar = view.findViewById(R.id.birthCalendarIcon);
        Button cancelButton = view.findViewById(R.id.editProfileBirthCancelButton);
        Button okButton = view.findViewById(R.id.editProfileBirthOkButton);

        dataEdit.setText(databaseCP.getUserBirth(UserModel.getId()));

        AlertDialog dialog = builder.create();

        calendar.setOnClickListener(v -> showCalendar(dataEdit));
        cancelButton.setOnClickListener(v -> dialog.dismiss());
        okButton.setOnClickListener(v -> {
            String dataBirth = Objects.requireNonNull(dataEdit.getText()).toString();

            birth.setText(dataBirth);
            newBirth.set(dataBirth);
            dialog.dismiss();
        });

        databaseCP.close();
        dialog.show();
    }

    private void showCalendar(TextInputEditText birth) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(GeneralProfile.this, (view, setYear, setMonth, setDay) -> {
            String selectedDate = getResources().getString(R.string.selected_date, setYear, setMonth + 1, setDay);
            birth.setText(selectedDate);
        }, year, month, day);

        datePickerDialog.show();
    }
}
