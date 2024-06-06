package com.example.collegepal.util.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.collegepal.R;
import com.example.collegepal.database.entity.MoneyModel;
import com.example.collegepal.util.NumberingFormat;
import com.example.collegepal.util.listener.DeleteMoneyListener;
import com.example.collegepal.util.listener.EditMoneyListener;

import java.util.List;
import java.util.Objects;

public class AdapterMoneyManagement extends ArrayAdapter<MoneyModel> {
    private final Context context;
    private final List<MoneyModel> listMoney;
    private EditMoneyListener editMoneyListener;
    private DeleteMoneyListener deleteMoneyListener;

    public AdapterMoneyManagement(Context context, List<MoneyModel> listMoney) {
        super(context, 0, listMoney);
        this.context = context;
        this.listMoney = listMoney;
    }

    public void setOnEditMoneyListener(EditMoneyListener listener) {
        editMoneyListener = listener;
    }

    public void setOnDeleteMoneyListener(DeleteMoneyListener listener) {
        deleteMoneyListener = listener;
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listMoney = convertView;
        if (listMoney == null) {
            listMoney = LayoutInflater.from(context).inflate(R.layout.listview_money_management_template, parent, false);
        }

        MoneyModel currentMoney = this.listMoney.get(position);

        TextView title = listMoney.findViewById(R.id.moneyTitle);
        TextView amount = listMoney.findViewById(R.id.moneyAmount);
        TextView date = listMoney.findViewById(R.id.moneyDate);
        ImageButton editMoney = listMoney.findViewById(R.id.editMoney);
        ImageView type = listMoney.findViewById(R.id.moneyType);

        String formattedAmount = NumberingFormat.ThousandSeparator(currentMoney.getAmount());
        title.setText(currentMoney.getTitle());
        amount.setText(context.getString(R.string.formatted_money_value, formattedAmount));
        date.setText(currentMoney.getDate());

        if (Objects.equals(currentMoney.getType(), "Pemasukan")) {
            amount.setTextColor(ContextCompat.getColor(context, R.color.green));
            type.setImageResource(R.drawable.arrow_green);
        } else {
            amount.setTextColor(ContextCompat.getColor(context, R.color.red));
            type.setImageResource(R.drawable.arrow_red);
        }

        editMoney.setOnClickListener(v -> {
            MoneyModel.setStaticId(currentMoney.getId());

            if (editMoneyListener != null) {
                editMoneyListener.onEditMoneyClickListener(position);
            }
        });
        title.setOnClickListener(v -> {
            MoneyModel.setStaticId(currentMoney.getId());

            if (deleteMoneyListener != null) {
                deleteMoneyListener.onDeleteMoneyClickListener(position);
            }
        });
        amount.setOnClickListener(v -> {
            MoneyModel.setStaticId(currentMoney.getId());

            if (deleteMoneyListener != null) {
                deleteMoneyListener.onDeleteMoneyClickListener(position);
            }
        });
        date.setOnClickListener(v -> {
            MoneyModel.setStaticId(currentMoney.getId());

            if (deleteMoneyListener != null) {
                deleteMoneyListener.onDeleteMoneyClickListener(position);
            }
        });

        return listMoney;
    }
}
