package com.example.collegepal.util.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.collegepal.R;

import java.util.List;

public class AdapterDropdown extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> listDay;

    public AdapterDropdown(Context context, List<String> listDay) {
        super(context, 0, listDay);
        this.context = context;
        this.listDay = listDay;
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listDay = convertView;
        if (listDay == null) {
            listDay = LayoutInflater.from(context).inflate(R.layout.dropdown_schedule_template, parent, false);
        }

        TextView day = listDay.findViewById(R.id.daySchedule);
        day.setText(this.listDay.get(position));

        return listDay;
    }
}
