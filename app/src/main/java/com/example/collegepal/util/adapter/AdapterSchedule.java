package com.example.collegepal.util.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.collegepal.R;
import com.example.collegepal.database.entity.ScheduleModel;
import com.example.collegepal.util.TimeFormat;
import com.example.collegepal.util.listener.DeleteScheduleListener;
import com.example.collegepal.util.listener.EditScheduleListener;

import java.util.List;

public class AdapterSchedule extends ArrayAdapter<ScheduleModel> {
    private final Context context;
    private final List<ScheduleModel> listSchedule;
    private EditScheduleListener editScheduleListener;
    private DeleteScheduleListener deleteScheduleListener;

    public AdapterSchedule(Context context, List<ScheduleModel> listSchedule) {
        super(context, 0, listSchedule);
        this.context = context;
        this.listSchedule = listSchedule;
    }

    public void setOnEditScheduleListener(EditScheduleListener listener) {
        editScheduleListener = listener;
    }

    public void setOnDeleteScheduleListener(DeleteScheduleListener listener) {
        deleteScheduleListener = listener;
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listSchedule = convertView;
        if (listSchedule == null) {
            listSchedule = LayoutInflater.from(context).inflate(R.layout.listview_schedule_template, parent, false);
        }

        ScheduleModel currentSchedule = this.listSchedule.get(position);

        TextView day = listSchedule.findViewById(R.id.dayScheduleListView);
        TextView subject = listSchedule.findViewById(R.id.subjectScheduleListView);
        TextView start = listSchedule.findViewById(R.id.startTimeScheduleListView);
        TextView finish = listSchedule.findViewById(R.id.finishTimeScheduleListView);
        ImageButton editSchedule = listSchedule.findViewById(R.id.editSchedule);
        LinearLayout deleteArea1 = listSchedule.findViewById(R.id.scheduleDeleteArea1);
        RelativeLayout deleteArea2 = listSchedule.findViewById(R.id.scheduleDeleteArea2);

        day.setText(currentSchedule.getDay());
        subject.setText(currentSchedule.getSubject());
        start.setText(TimeFormat.TimeSeparator(currentSchedule.getStartTime()));
        finish.setText(TimeFormat.TimeSeparator(currentSchedule.getFinishTime()));

        editSchedule.setOnClickListener(v -> {
            ScheduleModel.setStaticId(currentSchedule.getId());

            if (editScheduleListener != null) {
                editScheduleListener.onEditScheduleClickListener(position);
            }
        });
        deleteArea1.setOnClickListener(v -> {
            ScheduleModel.setStaticId(currentSchedule.getId());

            if (deleteScheduleListener != null) {
                deleteScheduleListener.onDeleteScheduleClickListener(position);
            }
        });
        deleteArea2.setOnClickListener(v -> {
            ScheduleModel.setStaticId(currentSchedule.getId());

            if (deleteScheduleListener != null) {
                deleteScheduleListener.onDeleteScheduleClickListener(position);
            }
        });

        return listSchedule;
    }
}
