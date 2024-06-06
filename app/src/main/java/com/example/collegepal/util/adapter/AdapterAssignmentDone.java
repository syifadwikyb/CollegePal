package com.example.collegepal.util.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.collegepal.R;
import com.example.collegepal.database.DatabaseCP;
import com.example.collegepal.database.entity.AssignmentModel;
import com.example.collegepal.database.entity.UserModel;
import com.example.collegepal.util.listener.DeleteAssignmentListener;

import java.util.List;

public class AdapterAssignmentDone extends ArrayAdapter<AssignmentModel> {
    private final Context context;
    private final List<AssignmentModel> listAssignment;
    private DeleteAssignmentListener deleteAssignmentListener;

    public AdapterAssignmentDone(Context context, List<AssignmentModel> listAssignment) {
        super(context, 0, listAssignment);
        this.context = context;
        this.listAssignment = listAssignment;
    }

    public void setOnDeleteAssignmentListener (DeleteAssignmentListener listener) {
        deleteAssignmentListener = listener;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listAssignment = convertView;
        if (listAssignment == null) {
            listAssignment = LayoutInflater.from(context).inflate(R.layout.listview_done_assignment_template, parent, false);
        }

        AssignmentModel currentAssignment = this.listAssignment.get(position);

        TextView subject = listAssignment.findViewById(R.id.subjectDone);
        TextView description = listAssignment.findViewById(R.id.descriptionDone);
        TextView deadline = listAssignment.findViewById(R.id.deadlineDone);
        ImageButton checkBox = listAssignment.findViewById(R.id.checkboxDone);
        LinearLayout deleteArea1 = listAssignment.findViewById(R.id.doneDeleteArea1);
        LinearLayout deleteArea2 = listAssignment.findViewById(R.id.doneDeleteArea2);

        subject.setText(currentAssignment.getSubject());
        if (currentAssignment.getDeadline().isEmpty() && currentAssignment.getDescription().isEmpty()) {
            description.setText("Tidak ada deskripsi tugas");
            deadline.setText("-------------");
        } else if (currentAssignment.getDeadline().isEmpty()) {
            description.setText(currentAssignment.getDescription());
            deadline.setText("-------------");
        } else if (currentAssignment.getDescription().isEmpty()){
            description.setText("Tidak ada deskripsi tugas");
            deadline.setText(currentAssignment.getDeadline());
        } else {
            description.setText(currentAssignment.getDescription());
            deadline.setText(currentAssignment.getDeadline());
        }

        checkBox.setOnClickListener(v -> {
            currentAssignment.setStatus("ACTIVE");
            remove(currentAssignment);

            DatabaseCP databaseCP = new DatabaseCP(context);
            databaseCP.updateStatusAssignment(UserModel.getId(), currentAssignment.getId(), currentAssignment.getStatus());
            Toast.makeText(context, "Tugas berhasil dikembalikan", Toast.LENGTH_LONG).show();
            databaseCP.close();
        });
        deleteArea1.setOnClickListener(v -> {
            AssignmentModel.setStaticId(currentAssignment.getId());

            if (deleteAssignmentListener != null) {
                deleteAssignmentListener.onDeleteAssignmentListener(position);
            }
        });
        deleteArea2.setOnClickListener(v -> {
            AssignmentModel.setStaticId(currentAssignment.getId());

            if (deleteAssignmentListener != null) {
                deleteAssignmentListener.onDeleteAssignmentListener(position);
            }
        });

        return listAssignment;
    }
}
