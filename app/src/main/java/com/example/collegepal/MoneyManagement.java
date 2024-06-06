package com.example.collegepal;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.collegepal.database.DatabaseCP;
import com.example.collegepal.database.entity.MoneyModel;
import com.example.collegepal.database.entity.UserModel;
import com.example.collegepal.util.NumberingFormat;
import com.example.collegepal.util.adapter.AdapterMoneyManagement;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class MoneyManagement extends AppCompatActivity {
    private AdapterMoneyManagement adapterMoneyManagement;
    private TextView remainingMoney;
    private TextView totalOutcome;
    private Button allMoney;
    private Button incomeMoney;
    private Button outcomeMoney;
    private ImageButton assignment;
    private ImageButton addMoney;
    private ImageButton schedule;
    private ImageButton profile;
    private ListView listMoney;
    private final AtomicInteger menuId = new AtomicInteger(1);

    private void initComponents() {
        remainingMoney = findViewById(R.id.remainingMoney);
        totalOutcome = findViewById(R.id.totalOutcome);
        allMoney = findViewById(R.id.showAllMoney);
        incomeMoney = findViewById(R.id.showIncomeMoney);
        outcomeMoney = findViewById(R.id.showOutcomeMoney);
        assignment = findViewById(R.id.assignmentButtonMoneyManagement);
        addMoney = findViewById(R.id.addMoney);
        schedule = findViewById(R.id.scheduleButtonMoneyManagement);
        profile = findViewById(R.id.profileButtonMoneyManagement);
        listMoney = findViewById(R.id.listMoneyManagement);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_management);

        initComponents();
        DatabaseCP databaseCP = new DatabaseCP(MoneyManagement.this);

        remainingMoney.setText(getResources().getString(R.string.formatted_money_value, NumberingFormat.ThousandSeparator(databaseCP.remainingMoney(UserModel.getId()))));
        totalOutcome.setText(getResources().getString(R.string.formatted_money_value, NumberingFormat.ThousandSeparator(databaseCP.totalOutcomeMoney(UserModel.getId()))));

        adapterMoneyManagement = new AdapterMoneyManagement(MoneyManagement.this, databaseCP.selectDataIncomeOutcome(UserModel.getId()));
        adapterMoneyManagement.setOnEditMoneyListener(position -> showPopUpEditMoney());
        adapterMoneyManagement.setOnDeleteMoneyListener(position -> showPupUpDeleteMoney());
        listMoney.setAdapter(adapterMoneyManagement);
        allMoney.setBackgroundColor(ContextCompat.getColor(MoneyManagement.this, R.color.orange));
        incomeMoney.setBackgroundColor(ContextCompat.getColor(MoneyManagement.this, R.color.grey));
        outcomeMoney.setBackgroundColor(ContextCompat.getColor(MoneyManagement.this, R.color.grey));

        allMoney.setOnClickListener(v -> {
            adapterMoneyManagement = new AdapterMoneyManagement(MoneyManagement.this, databaseCP.selectDataIncomeOutcome(UserModel.getId()));
            adapterMoneyManagement.setOnEditMoneyListener(position -> showPopUpEditMoney());
            adapterMoneyManagement.setOnDeleteMoneyListener(position -> showPupUpDeleteMoney());
            listMoney.setAdapter(adapterMoneyManagement);
            allMoney.setBackgroundColor(ContextCompat.getColor(MoneyManagement.this, R.color.orange));
            incomeMoney.setBackgroundColor(ContextCompat.getColor(MoneyManagement.this, R.color.grey));
            outcomeMoney.setBackgroundColor(ContextCompat.getColor(MoneyManagement.this, R.color.grey));
            menuId.set(1);
        });
        incomeMoney.setOnClickListener(v -> {
            adapterMoneyManagement = new AdapterMoneyManagement(MoneyManagement.this, databaseCP.selectDataIncome(UserModel.getId()));
            adapterMoneyManagement.setOnEditMoneyListener(position -> showPopUpEditMoney());
            adapterMoneyManagement.setOnDeleteMoneyListener(position -> showPupUpDeleteMoney());
            listMoney.setAdapter(adapterMoneyManagement);
            allMoney.setBackgroundColor(ContextCompat.getColor(MoneyManagement.this, R.color.grey));
            incomeMoney.setBackgroundColor(ContextCompat.getColor(MoneyManagement.this, R.color.green));
            outcomeMoney.setBackgroundColor(ContextCompat.getColor(MoneyManagement.this, R.color.grey));
            menuId.set(2);
        });
        outcomeMoney.setOnClickListener(v -> {
            adapterMoneyManagement = new AdapterMoneyManagement(MoneyManagement.this, databaseCP.selectDataOutcome(UserModel.getId()));
            adapterMoneyManagement.setOnEditMoneyListener(position -> showPopUpEditMoney());
            adapterMoneyManagement.setOnDeleteMoneyListener(position -> showPupUpDeleteMoney());
            listMoney.setAdapter(adapterMoneyManagement);
            allMoney.setBackgroundColor(ContextCompat.getColor(MoneyManagement.this, R.color.grey));
            incomeMoney.setBackgroundColor(ContextCompat.getColor(MoneyManagement.this, R.color.grey));
            outcomeMoney.setBackgroundColor(ContextCompat.getColor(MoneyManagement.this, R.color.red));
            menuId.set(3);
        });

        addMoney.setOnClickListener(v -> showPopUpAddMoney());
        assignment.setOnClickListener(v -> {
            Intent intent = new Intent (MoneyManagement.this, Assignment.class);
            startActivity(intent);
        });
        schedule.setOnClickListener(v -> {
            Intent intent = new Intent (MoneyManagement.this, Schedule.class);
            startActivity(intent);
        });
        profile.setOnClickListener(v -> {
            Intent intent = new Intent (MoneyManagement.this, UserProfile.class);
            startActivity(intent);
        });

        databaseCP.close();
    }

    private void showPopUpAddMoney() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_money_template, null);
        builder.setView(view);

        Calendar calendarToday = Calendar.getInstance();
        int year = calendarToday.get(Calendar.YEAR);
        int month = calendarToday.get(Calendar.MONTH);
        int day = calendarToday.get(Calendar.DAY_OF_MONTH);

        TextInputEditText title = view.findViewById(R.id.moneyTitleCardView).findViewById(R.id.moneyTitleInputEditText);
        TextInputEditText date = view.findViewById(R.id.moneyDateInputLayout).findViewById(R.id.moneyDateCardView).findViewById(R.id.moneyDateInputEditText);
        TextInputEditText amount = view.findViewById(R.id.moneyAmountCardView).findViewById(R.id.moneyAmountInputEditText);
        RadioGroup type = view.findViewById(R.id.moneyTypeRadioGroup);
        ImageView calendar = view.findViewById(R.id.calendarIconMoneyManagement);
        Button cancelButton = view.findViewById(R.id.cancelButtonMoneyManagement);
        Button okButton = view.findViewById(R.id.okButtonMoneyManagement);

        AlertDialog dialog = builder.create();

        String selectedDate = getResources().getString(R.string.selected_date, year, month + 1, day);
        date.setText(selectedDate);

        calendar.setOnClickListener(v -> showCalendar(date));

        okButton.setOnClickListener(v -> {
            DatabaseCP databaseCP = new DatabaseCP(MoneyManagement.this);

            String dataTitle = Objects.requireNonNull(title.getText()).toString();
            String dataDate = Objects.requireNonNull(date.getText()).toString();
            String checkAmount = Objects.requireNonNull(amount.getText()).toString();
            String dataType;

            if (type.getCheckedRadioButtonId() != -1 && !dataTitle.isEmpty() && !checkAmount.isEmpty()) {
                RadioButton moneyType = view.findViewById(type.getCheckedRadioButtonId());
                dataType = moneyType.getText().toString();
                int dataAmount = Integer.parseInt(checkAmount);

                databaseCP.insertDataMoney(UserModel.getId(), dataTitle, dataDate, dataAmount, dataType);
                adapterMoneyManagement.clear();
                adapterMoneyManagement.addAll(databaseCP.selectDataIncomeOutcome(UserModel.getId()));
                adapterMoneyManagement.notifyDataSetChanged();

                remainingMoney.setText(getResources().getString(R.string.formatted_money_value, NumberingFormat.ThousandSeparator(databaseCP.remainingMoney(UserModel.getId()))));
                totalOutcome.setText(getResources().getString(R.string.formatted_money_value, NumberingFormat.ThousandSeparator(databaseCP.totalOutcomeMoney(UserModel.getId()))));

                databaseCP.close();
                dialog.dismiss();
            }
            else if (type.getCheckedRadioButtonId() != -1 && dataTitle.isEmpty()) {
                Toast.makeText(MoneyManagement.this, "Harap masukan keterangan transaksi", Toast.LENGTH_SHORT).show();
            }
            else if (type.getCheckedRadioButtonId() != -1 && checkAmount.isEmpty()) {
                Toast.makeText(MoneyManagement.this, "Harap masukan jumlah uang", Toast.LENGTH_SHORT).show();
            }
            else if (type.getCheckedRadioButtonId() == -1) {
                Toast.makeText(MoneyManagement.this, "Harap pilih salah satu antara Pemasukan dan Pengeluaran", Toast.LENGTH_SHORT).show();
            }
        });
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void showPopUpEditMoney() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_money_template, null);
        builder.setView(view);

        DatabaseCP databaseCP = new DatabaseCP(MoneyManagement.this);

        TextInputEditText title = view.findViewById(R.id.moneyTitleCardView).findViewById(R.id.moneyTitleInputEditText);
        TextInputEditText date = view.findViewById(R.id.moneyDateInputLayout).findViewById(R.id.moneyDateCardView).findViewById(R.id.moneyDateInputEditText);
        TextInputEditText amount = view.findViewById(R.id.moneyAmountCardView).findViewById(R.id.moneyAmountInputEditText);
        RadioGroup type = view.findViewById(R.id.moneyTypeRadioGroup);
        ImageView calendar = view.findViewById(R.id.calendarIconMoneyManagement);
        Button cancelButton = view.findViewById(R.id.cancelButtonMoneyManagement);
        Button okButton = view.findViewById(R.id.okButtonMoneyManagement);

        title.setText(databaseCP.getTitleMoney(MoneyModel.getStaticId()));
        date.setText(databaseCP.getDateMoney(MoneyModel.getStaticId()));
        amount.setText(String.valueOf(databaseCP.getAmountMoney(MoneyModel.getStaticId())));

        String typeMoney = databaseCP.getTypeMoney(MoneyModel.getStaticId());
        if (Objects.equals(typeMoney, "Pemasukan")) {
            type.check(R.id.incomeRadioButton);
        } else {
            type.check(R.id.outcomeRadioButton);
        }

        AlertDialog dialog = builder.create();

        calendar.setOnClickListener(v -> showCalendar(date));

        okButton.setOnClickListener(v -> {
            String dataTitle = Objects.requireNonNull(title.getText()).toString();
            String dataDate = Objects.requireNonNull(date.getText()).toString();
            String checkAmount = Objects.requireNonNull(amount.getText()).toString();
            String dataType;

            if (type.getCheckedRadioButtonId() != -1 && !dataTitle.isEmpty() && !checkAmount.isEmpty()) {
                RadioButton moneyType = view.findViewById(type.getCheckedRadioButtonId());
                dataType = moneyType.getText().toString();
                int dataAmount = Integer.parseInt(checkAmount);

                databaseCP.updateMoney(UserModel.getId(), MoneyModel.getStaticId(), dataTitle, dataDate, dataAmount, dataType);
                adapterMoneyManagement.clear();

                if (menuId.get() == 1) {
                    adapterMoneyManagement.addAll(databaseCP.selectDataIncomeOutcome(UserModel.getId()));
                    adapterMoneyManagement.notifyDataSetChanged();
                } else if (menuId.get() == 2) {
                    adapterMoneyManagement.addAll(databaseCP.selectDataIncome(UserModel.getId()));
                    adapterMoneyManagement.notifyDataSetChanged();
                } else if (menuId.get() == 3) {
                    adapterMoneyManagement.addAll(databaseCP.selectDataOutcome(UserModel.getId()));
                    adapterMoneyManagement.notifyDataSetChanged();
                }

                remainingMoney.setText(getResources().getString(R.string.formatted_money_value, NumberingFormat.ThousandSeparator(databaseCP.remainingMoney(UserModel.getId()))));
                totalOutcome.setText(getResources().getString(R.string.formatted_money_value, NumberingFormat.ThousandSeparator(databaseCP.totalOutcomeMoney(UserModel.getId()))));

                databaseCP.close();
                dialog.dismiss();
            }
            else if (type.getCheckedRadioButtonId() != -1 && dataTitle.isEmpty()) {
                Toast.makeText(MoneyManagement.this, "Harap masukan keterangan transaksi", Toast.LENGTH_SHORT).show();
            }
            else if (type.getCheckedRadioButtonId() != -1 && checkAmount.isEmpty()) {
                Toast.makeText(MoneyManagement.this, "Harap masukan jumlah uang", Toast.LENGTH_SHORT).show();
            }
            else if (type.getCheckedRadioButtonId() == -1) {
                Toast.makeText(MoneyManagement.this, "Harap pilih salah satu antara Pemasukan dan Pengeluaran", Toast.LENGTH_SHORT).show();
            }
        });
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        databaseCP.close();
        dialog.show();
    }

    private void showPupUpDeleteMoney() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MoneyManagement.this);

        DatabaseCP databaseCP = new DatabaseCP(MoneyManagement.this);

        dialog.setTitle("Data transaksi akan dihapus");
        dialog.setMessage("Apakah anda yakin ingin menghapus transaksi " + databaseCP.getTitleMoney(MoneyModel.getStaticId()) + "?");
        dialog.setPositiveButton("Ya", (dialog1, which) -> {
            databaseCP.deleteMoney(UserModel.getId(), MoneyModel.getStaticId());
            adapterMoneyManagement.clear();

            if (menuId.get() == 1) {
                adapterMoneyManagement.addAll(databaseCP.selectDataIncomeOutcome(UserModel.getId()));
                adapterMoneyManagement.notifyDataSetChanged();
            } else if (menuId.get() == 2) {
                adapterMoneyManagement.addAll(databaseCP.selectDataIncome(UserModel.getId()));
                adapterMoneyManagement.notifyDataSetChanged();
            } else if (menuId.get() == 3) {
                adapterMoneyManagement.addAll(databaseCP.selectDataOutcome(UserModel.getId()));
                adapterMoneyManagement.notifyDataSetChanged();
            }

            remainingMoney.setText(getResources().getString(R.string.formatted_money_value, NumberingFormat.ThousandSeparator(databaseCP.remainingMoney(UserModel.getId()))));
            totalOutcome.setText(getResources().getString(R.string.formatted_money_value, NumberingFormat.ThousandSeparator(databaseCP.totalOutcomeMoney(UserModel.getId()))));

            dialog1.dismiss();
        });
        dialog.setNegativeButton("Tidak", (dialog2, which) -> dialog2.dismiss());

        databaseCP.close();
        AlertDialog dialogCreate = dialog.create();
        dialogCreate.show();
    }

    private void showCalendar(TextInputEditText date) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(MoneyManagement.this, (view, setYear, setMonth, setDay) -> {
            String selectedDate = getResources().getString(R.string.selected_date, setYear, setMonth + 1, setDay);
            date.setText(selectedDate);
        }, year, month, day);

        datePickerDialog.show();
    }
}