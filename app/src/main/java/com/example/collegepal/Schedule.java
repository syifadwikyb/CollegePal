package com.example.collegepal;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.collegepal.database.DatabaseCP;
import com.example.collegepal.database.entity.ScheduleModel;
import com.example.collegepal.database.entity.UserModel;
import com.example.collegepal.util.TimeFormat;
import com.example.collegepal.util.adapter.AdapterDropdown;
import com.example.collegepal.util.adapter.AdapterSchedule;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class Schedule extends AppCompatActivity {
    private List<String> dayList;
    MaterialAutoCompleteTextView dayDropdown;
    AdapterDropdown adapterDropdown;
    AdapterSchedule adapterSchedule;
    ListView listSchedule;
    ImageButton addSchedule;
    ImageButton assignment;
    ImageButton moneyManagement;
    ImageButton profile;
    int hour = 0;
    int minute = 0;
    AtomicReference<String> dayString = new AtomicReference<>("Semua");

    @SuppressLint("WrongViewCast")
    private void initComponents() {
        dayDropdown = findViewById(R.id.dayScheduleInputLayout).findViewById(R.id.dayScheduleAutoComplete);
        listSchedule = findViewById(R.id.listSchedule);
        addSchedule = findViewById(R.id.addSchedule);
        assignment = findViewById(R.id.assignmentButtonSchedule);
        moneyManagement = findViewById(R.id.moneyButtonSchedule);
        profile = findViewById(R.id.profileButtonSchedule);
    }

    private void initList() {
        dayList = new ArrayList<>();
        dayList.add("Semua");
        dayList.add("Senin");
        dayList.add("Selasa");
        dayList.add("Rabu");
        dayList.add("Kamis");
        dayList.add("Jumat");
        dayList.add("Sabtu");
        dayList.add("Minggu");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        initComponents();
        initList();

        DatabaseCP databaseCP = new DatabaseCP(Schedule.this);

        adapterDropdown = new AdapterDropdown(Schedule.this, dayList);
        dayDropdown.setAdapter(adapterDropdown);
        dayDropdown.setOnItemClickListener((parent, view, position1, id) -> {
            String selectedItem = (String) parent.getItemAtPosition(position1);

            if (Objects.equals(selectedItem, "Semua")) {
                adapterSchedule = new AdapterSchedule(Schedule.this, databaseCP.selectAllDataSchedule(UserModel.getId()));
            } else {
                adapterSchedule = new AdapterSchedule(Schedule.this, databaseCP.selectDataScheduleByDay(UserModel.getId(), databaseCP.getDayId(selectedItem)));
            }
            listSchedule.setAdapter(adapterSchedule);
            dayString.set(selectedItem);
            adapterSchedule.setOnEditScheduleListener(position -> showPopUpEditSchedule());
            adapterSchedule.setOnDeleteScheduleListener(position -> showPupUpDeleteSchedule());
        });

        adapterSchedule = new AdapterSchedule(Schedule.this, databaseCP.selectAllDataSchedule(UserModel.getId()));
        listSchedule.setAdapter(adapterSchedule);
        adapterSchedule.setOnEditScheduleListener(position -> showPopUpEditSchedule());
        adapterSchedule.setOnDeleteScheduleListener(position -> showPupUpDeleteSchedule());

        addSchedule.setOnClickListener(v -> showPopUpAddSchedule());
        assignment.setOnClickListener(v -> {
            Intent intent = new Intent (Schedule.this, Assignment.class);
            startActivity(intent);
        });
        moneyManagement.setOnClickListener(v -> {
            Intent intent = new Intent (Schedule.this, MoneyManagement.class);
            startActivity(intent);
        });
        profile.setOnClickListener(v -> {
            Intent intent = new Intent (Schedule.this, UserProfile.class);
            startActivity(intent);
        });

        databaseCP.close();
    }

    private void showPopUpAddSchedule() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_schedule_template, null);
        builder.setView(view);

        initComponents();
        initList();
        dayList.remove(0);

        DatabaseCP databaseCP = new DatabaseCP(Schedule.this);
        AtomicReference<String> dayString = new AtomicReference<>("Senin");

        adapterDropdown = new AdapterDropdown(Schedule.this, dayList);
        MaterialAutoCompleteTextView dayDropdownPopUp = view.findViewById(R.id.daySchedulePopUpInputLayout).findViewById(R.id.daySchedulePopUpAutoComplete);
        dayDropdownPopUp.setAdapter(adapterDropdown);
        dayDropdownPopUp.setOnItemClickListener((parent, view1, position, id) -> dayString.set((String) parent.getItemAtPosition(position)));

        TextInputEditText subject = view.findViewById(R.id.subjectScheduleCardView).findViewById(R.id.subjectScheduleInputEditText);
        TextInputEditText start = view.findViewById(R.id.startTimeCardView).findViewById(R.id.startTimeInputEditText);
        TextInputEditText end = view.findViewById(R.id.endTimeCardView).findViewById(R.id.endTimeInputEditText);
        ImageView startCalendar = view.findViewById(R.id.startTimeCalendarIcon);
        ImageView endCalendar = view.findViewById(R.id.endTimeCalendarIcon);
        Button cancelButton = view.findViewById(R.id.scheduleCancelButton);
        Button okButton = view.findViewById(R.id.scheduleOkButton);

        AlertDialog dialog = builder.create();

        startCalendar.setOnClickListener(v -> showTimePicker(start));
        endCalendar.setOnClickListener(v -> showTimePicker(end));

        okButton.setOnClickListener(v -> {
            String dataDay = dayString.get();
            String dataSubject = Objects.requireNonNull(subject.getText()).toString();
            String dataStart = Objects.requireNonNull(start.getText()).toString().replace(":", "");
            String dataFinish = Objects.requireNonNull(end.getText()).toString().replace(":", "");

            if (dataDay.isEmpty() || dataSubject.isEmpty() || dataStart.isEmpty() || dataFinish.isEmpty()) {
                Toast.makeText(Schedule.this, "Data jadwal tidak boleh ada yang kosong", Toast.LENGTH_LONG).show();
            } else {
                databaseCP.insertDataSchedule(UserModel.getId(), dataDay, databaseCP.getDayId(dataDay), dataSubject, dataStart, dataFinish);
                adapterSchedule.clear();
                adapterSchedule.addAll(databaseCP.selectAllDataSchedule(UserModel.getId()));
                adapterSchedule.notifyDataSetChanged();

                dialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        databaseCP.close();
        dialog.show();
    }

    private void showPopUpEditSchedule() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_schedule_template, null);
        builder.setView(view);

        initComponents();
        initList();
        dayList.remove(0);

        DatabaseCP databaseCP = new DatabaseCP(Schedule.this);
        AtomicReference<String> dayString = new AtomicReference<>(databaseCP.getDaySchedule(ScheduleModel.getStaticId()));

        adapterDropdown = new AdapterDropdown(Schedule.this, dayList);
        MaterialAutoCompleteTextView dayDropdownPopUp = view.findViewById(R.id.daySchedulePopUpInputLayout).findViewById(R.id.daySchedulePopUpAutoComplete);
        dayDropdownPopUp.setText(databaseCP.getDaySchedule(ScheduleModel.getStaticId()));
        dayDropdownPopUp.setAdapter(adapterDropdown);
        dayDropdownPopUp.setOnItemClickListener((parent, view1, position, id) -> dayString.set((String) parent.getItemAtPosition(position)));

        TextInputEditText subject = view.findViewById(R.id.subjectScheduleCardView).findViewById(R.id.subjectScheduleInputEditText);
        TextInputEditText start = view.findViewById(R.id.startTimeCardView).findViewById(R.id.startTimeInputEditText);
        TextInputEditText end = view.findViewById(R.id.endTimeCardView).findViewById(R.id.endTimeInputEditText);
        ImageView startCalendar = view.findViewById(R.id.startTimeCalendarIcon);
        ImageView endCalendar = view.findViewById(R.id.endTimeCalendarIcon);
        Button cancelButton = view.findViewById(R.id.scheduleCancelButton);
        Button okButton = view.findViewById(R.id.scheduleOkButton);

        subject.setText(databaseCP.getSubjectSchedule(ScheduleModel.getStaticId()));
        start.setText(TimeFormat.TimeSeparator(databaseCP.getStartTimeSchedule(ScheduleModel.getStaticId())));
        end.setText(TimeFormat.TimeSeparator(databaseCP.getFinishTimeSchedule(ScheduleModel.getStaticId())));

        AlertDialog dialog = builder.create();

        startCalendar.setOnClickListener(v -> showTimePicker(start));
        endCalendar.setOnClickListener(v -> showTimePicker(end));

        okButton.setOnClickListener(v -> {
            String dataDay = dayString.get();
            String dataSubject = Objects.requireNonNull(subject.getText()).toString();
            String dataStart = Objects.requireNonNull(start.getText()).toString().replace(":", "");
            String dataFinish = Objects.requireNonNull(end.getText()).toString().replace(":", "");

            if (dataDay.isEmpty() || dataSubject.isEmpty() || dataStart.isEmpty() || dataFinish.isEmpty()) {
                Toast.makeText(Schedule.this, "Data jadwal tidak boleh ada yang kosong", Toast.LENGTH_LONG).show();
            } else {
                databaseCP.updateSchedule(UserModel.getId(), ScheduleModel.getStaticId(), dataDay, databaseCP.getDayId(dataDay), dataSubject, dataStart, dataFinish);
                adapterSchedule.clear();

                if (Objects.equals(this.dayString.get(), "Semua")) {
                    adapterSchedule.addAll(databaseCP.selectAllDataSchedule(UserModel.getId()));
                    adapterSchedule.notifyDataSetChanged();
                } else {
                    adapterSchedule.addAll(databaseCP.selectDataScheduleByDay(UserModel.getId(), databaseCP.getDayId(this.dayString.get())));
                    adapterSchedule.notifyDataSetChanged();
                }

                dialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        databaseCP.close();
        dialog.show();
    }

    private void showPupUpDeleteSchedule() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(Schedule.this);

        DatabaseCP databaseCP = new DatabaseCP(Schedule.this);

        dialog.setTitle("Data jadwal akan dihapus");
        dialog.setMessage("Apakah anda yakin ingin menghapus jadwal " + databaseCP.getSubjectSchedule(ScheduleModel.getStaticId()) + "?");
        dialog.setPositiveButton("Ya", (dialog1, which) -> {
            databaseCP.deleteSchedule(UserModel.getId(), ScheduleModel.getStaticId());
            adapterSchedule.clear();

            if (Objects.equals(this.dayString.get(), "Semua")) {
                adapterSchedule.addAll(databaseCP.selectAllDataSchedule(UserModel.getId()));
                adapterSchedule.notifyDataSetChanged();
            } else {
                adapterSchedule.addAll(databaseCP.selectDataScheduleByDay(UserModel.getId(), databaseCP.getDayId(this.dayString.get())));
                adapterSchedule.notifyDataSetChanged();
            }

            dialog1.dismiss();
        });
        dialog.setNegativeButton("Tidak", (dialog2, which) -> dialog2.dismiss());

        databaseCP.close();
        AlertDialog dialogCreate = dialog.create();
        dialogCreate.show();
    }

    private void showTimePicker(TextInputEditText time) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (view, hour, minute) -> {
            this.hour = hour;
            this.minute = minute;
            String selectedTime = getResources().getString(R.string.selected_time, this.hour, this.minute);
            time.setText(selectedTime);
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(Schedule.this, onTimeSetListener, this.hour, this.minute, true);
        timePickerDialog.show();
    }
}
