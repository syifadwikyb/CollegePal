package com.example.collegepal;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.collegepal.database.DatabaseCP;
import com.example.collegepal.database.entity.AssignmentModel;
import com.example.collegepal.database.entity.UserModel;
import com.example.collegepal.util.adapter.AdapterAssignmentDone;
import com.example.collegepal.util.adapter.AdapterAssignmentUndone;
import com.example.collegepal.util.listener.EditAssignmentListener;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Assignment extends AppCompatActivity {
    private AdapterAssignmentUndone adapterAssignmentUndone;
    private AdapterAssignmentDone adapterAssignmentDone;
    private TextView undoneText;
    private TextView doneText;
    private ImageButton addAssignment;
    private ImageButton moneyManagement;
    private ImageButton schedule;
    private ImageButton profile;
    private ListView listAssignment;
    private final AtomicInteger menuId = new AtomicInteger(1);

    private void initComponents() {
        undoneText = findViewById(R.id.undoneTextView);
        doneText = findViewById(R.id.doneTextView);
        addAssignment = findViewById(R.id.addAssignmentImageButton);
        moneyManagement = findViewById(R.id.moneyButtonAssignment);
        schedule = findViewById(R.id.scheduleButtonAssignment);
        profile = findViewById(R.id.profileButtonAssignment);
        listAssignment = findViewById(R.id.listAssignment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        initComponents();
        DatabaseCP databaseCP = new DatabaseCP(Assignment.this);

        adapterAssignmentUndone = new AdapterAssignmentUndone(Assignment.this, databaseCP.selectDataAssignmentActive(UserModel.getId()));
        listAssignment.setAdapter(adapterAssignmentUndone);
        adapterAssignmentUndone.setOnEditAssignmentListener(position -> showPopUpEditAssignment());
        adapterAssignmentUndone.setOnEditAssignmentListener(new EditAssignmentListener() {
            @Override
            public void onEditAssignmentClickListener(int position) {

            }
        });
        adapterAssignmentUndone.setOnDeleteAssignmentListener(position -> showPopUpDeleteAssignment());
        undoneText.setTextColor(ContextCompat.getColor(Assignment.this, R.color.green));
        doneText.setTextColor(ContextCompat.getColor(Assignment.this, R.color.purple));

        undoneText.setOnClickListener(v -> {
            adapterAssignmentUndone = new AdapterAssignmentUndone(Assignment.this, databaseCP.selectDataAssignmentActive(UserModel.getId()));
            adapterAssignmentUndone.setOnEditAssignmentListener(position -> showPopUpEditAssignment());
            adapterAssignmentUndone.setOnDeleteAssignmentListener(position -> showPopUpDeleteAssignment());
            listAssignment.setAdapter(adapterAssignmentUndone);
            undoneText.setTextColor(ContextCompat.getColor(Assignment.this, R.color.green));
            doneText.setTextColor(ContextCompat.getColor(Assignment.this, R.color.purple));
            menuId.set(1);
        });
        doneText.setOnClickListener(v -> {
            adapterAssignmentDone = new AdapterAssignmentDone(Assignment.this, databaseCP.selectDataAssignmentInactive(UserModel.getId()));
            adapterAssignmentDone.setOnDeleteAssignmentListener(position -> showPopUpDeleteAssignment());
            listAssignment.setAdapter(adapterAssignmentDone);
            undoneText.setTextColor(ContextCompat.getColor(Assignment.this, R.color.purple));
            doneText.setTextColor(ContextCompat.getColor(Assignment.this, R.color.green));
            menuId.set(2);
        });

        addAssignment.setOnClickListener(v -> showPopUpAddAssignment());
        moneyManagement.setOnClickListener(v -> {
            Intent intent = new Intent (Assignment.this, MoneyManagement.class);
            startActivity(intent);
        });
        schedule.setOnClickListener(v -> {
            Intent intent = new Intent (Assignment.this, Schedule.class);
            startActivity(intent);
        });
        profile.setOnClickListener(v -> {
            Intent intent = new Intent (Assignment.this, UserProfile.class);
            startActivity(intent);
        });

        databaseCP.close();
    }

    private void showPopUpAddAssignment() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_assignment_template, null);
        builder.setView(view);

        TextInputEditText subject = view.findViewById(R.id.subjectCardView).findViewById(R.id.subjectInputEditText);
        TextInputEditText deadline = view.findViewById(R.id.deadlineInputLayout).findViewById(R.id.deadlineCardView).findViewById(R.id.deadlineInputEditText);
        TextInputEditText description = view.findViewById(R.id.descriptionCardView).findViewById(R.id.descriptionInputEditText);
        ImageView calendar = view.findViewById(R.id.calendarIcon);
        Button cancelButton = view.findViewById(R.id.cancelButton);
        Button okButton = view.findViewById(R.id.okButton);

        AlertDialog dialog = builder.create();

        calendar.setOnClickListener(v -> showCalendar(deadline));

        okButton.setOnClickListener(v -> {
            DatabaseCP databaseCP = new DatabaseCP(Assignment.this);

            String dataSubject = Objects.requireNonNull(subject.getText()).toString();
            String dataDeadline = Objects.requireNonNull(deadline.getText()).toString();
            String dataDescription = Objects.requireNonNull(description.getText()).toString();

            if (dataSubject.isEmpty()) {
                Toast.makeText(Assignment.this, "Judul tidak boleh kosong", Toast.LENGTH_LONG).show();
            } else {
                databaseCP.insertDataAssignment(UserModel.getId(), dataSubject, dataDeadline, dataDescription);
                adapterAssignmentUndone.clear();
                adapterAssignmentUndone.addAll(databaseCP.selectDataAssignmentActive(UserModel.getId()));
                adapterAssignmentUndone.notifyDataSetChanged();

                databaseCP.close();
                dialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public void showPopUpEditAssignment() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_assignment_template, null);
        builder.setView(view);

        DatabaseCP databaseCP = new DatabaseCP(Assignment.this);

        TextInputEditText subject = view.findViewById(R.id.subjectCardView).findViewById(R.id.subjectInputEditText);
        TextInputEditText deadline = view.findViewById(R.id.deadlineInputLayout).findViewById(R.id.deadlineCardView).findViewById(R.id.deadlineInputEditText);
        TextInputEditText description = view.findViewById(R.id.descriptionCardView).findViewById(R.id.descriptionInputEditText);
        ImageView calendar = view.findViewById(R.id.calendarIcon);
        Button cancelButton = view.findViewById(R.id.cancelButton);
        Button okButton = view.findViewById(R.id.okButton);

        subject.setText(databaseCP.getSubjectAssignment(AssignmentModel.getStaticId()));
        deadline.setText(databaseCP.getDeadlineAssignment(AssignmentModel.getStaticId()));
        description.setText(databaseCP.getDescriptionAssignment(AssignmentModel.getStaticId()));

        AlertDialog dialog = builder.create();

        calendar.setOnClickListener(v -> showCalendar(deadline));
        okButton.setOnClickListener(v -> {
            String dataSubject = Objects.requireNonNull(subject.getText()).toString();
            String dataDeadline = Objects.requireNonNull(deadline.getText()).toString();
            String dataDescription = Objects.requireNonNull(description.getText()).toString();

            if (dataSubject.isEmpty()) {
                Toast.makeText(Assignment.this, "Judul tidak boleh kosong", Toast.LENGTH_LONG).show();
            } else {
                databaseCP.updateAssignment(UserModel.getId(), AssignmentModel.getStaticId(), dataSubject, dataDeadline, dataDescription);
                adapterAssignmentUndone.clear();
                adapterAssignmentUndone.addAll(databaseCP.selectDataAssignmentActive(UserModel.getId()));
                adapterAssignmentUndone.notifyDataSetChanged();

                dialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        databaseCP.close();
        dialog.show();
    }

    private void showPopUpDeleteAssignment() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(Assignment.this);

        DatabaseCP databaseCP = new DatabaseCP(Assignment.this);

        dialog.setTitle("Data tugas akan dihapus");
        dialog.setMessage("Apakah anda yakin ingin menghapus tugas " + databaseCP.getSubjectAssignment(AssignmentModel.getStaticId()) + "?");
        dialog.setPositiveButton("Ya", (dialog1, which) -> {
            databaseCP.deleteAssignment(UserModel.getId(), AssignmentModel.getStaticId());

            if (menuId.get() == 1) {
                adapterAssignmentUndone.clear();
                adapterAssignmentUndone.addAll(databaseCP.selectDataAssignmentActive(UserModel.getId()));
                adapterAssignmentUndone.notifyDataSetChanged();
            } else if (menuId.get() == 2) {
                adapterAssignmentDone.clear();
                adapterAssignmentDone.addAll(databaseCP.selectDataAssignmentInactive(UserModel.getId()));
                adapterAssignmentDone.notifyDataSetChanged();
            }

            dialog1.dismiss();
        });
        dialog.setNegativeButton("Tidak", (dialog2, which) -> dialog2.dismiss());

        databaseCP.close();
        AlertDialog dialogCreate = dialog.create();
        dialogCreate.show();
    }

    private void showCalendar(TextInputEditText deadline) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Assignment.this, (view, setYear, setMonth, setDay) -> {
            String selectedDate = getResources().getString(R.string.selected_date, setYear, setMonth + 1, setDay);
            deadline.setText(selectedDate);
        }, year, month, day);

        datePickerDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishAffinity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}