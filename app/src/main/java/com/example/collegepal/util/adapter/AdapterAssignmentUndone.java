package com.example.collegepal.util.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
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
import com.example.collegepal.util.listener.EditAssignmentListener;

import java.util.List;

public class AdapterAssignmentUndone extends ArrayAdapter<AssignmentModel> {
    private final Context context;
    private final List<AssignmentModel> listAssignment;
    private EditAssignmentListener editAssignmentListener;
    private DeleteAssignmentListener deleteAssignmentListener;

    public AdapterAssignmentUndone(Context context, List<AssignmentModel> listAssignment) {
        super(context, 0, listAssignment);
        this.context = context;
        this.listAssignment = listAssignment;
    }

    public void setOnEditAssignmentListener(EditAssignmentListener listener) {
        editAssignmentListener = listener;
    }

    public void setOnDeleteAssignmentListener (DeleteAssignmentListener listener) {
        deleteAssignmentListener = listener;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listAssignment = convertView;
        if (listAssignment == null) {
            listAssignment = LayoutInflater.from(context).inflate(R.layout.listview_undone_assignment_template, parent, false);
        }

        AssignmentModel currentAssignment = this.listAssignment.get(position);

        TextView subject = listAssignment.findViewById(R.id.subjectUndone);
        TextView description = listAssignment.findViewById(R.id.descriptionUndone);
        TextView deadline = listAssignment.findViewById(R.id.deadlineUndone);
        CheckBox checkBox = listAssignment.findViewById(R.id.checkboxUndone);
        ImageButton editAssignment = listAssignment.findViewById(R.id.editAssignment);
        LinearLayout deleteArea1 = listAssignment.findViewById(R.id.undoneDeleteArea1);
        LinearLayout deleteArea2 = listAssignment.findViewById(R.id.undoneDeleteArea2);

        subject.setText(currentAssignment.getSubject());
        if (currentAssignment.getDeadline().isEmpty() && currentAssignment.getDescription().isEmpty()) {
            description.setText("Tidak ada deskripsi tugas");
            deadline.setText("Tidak ada deadline");
        } else if (currentAssignment.getDeadline().isEmpty()) {
            description.setText(currentAssignment.getDescription());
            deadline.setText("Tidak ada deadline");
        } else if (currentAssignment.getDescription().isEmpty()){
            description.setText("Tidak ada deskripsi tugas");
            deadline.setText(currentAssignment.getDeadline());
        } else {
            description.setText(currentAssignment.getDescription());
            deadline.setText(currentAssignment.getDeadline());
        }

        checkBox.setOnClickListener(v -> {
            checkBox.setChecked(true);

            if (checkBox.isChecked()) {
                currentAssignment.setStatus("INACTIVE");
                remove(currentAssignment);
            }
            checkBox.setChecked(false);
            notifyDataSetChanged();

            DatabaseCP databaseCP = new DatabaseCP(context);
            databaseCP.updateStatusAssignment(UserModel.getId(), currentAssignment.getId(), currentAssignment.getStatus());
            Toast.makeText(context, "Tugas berhasil diselesaikan", Toast.LENGTH_LONG).show();
            databaseCP.close();
        });

        editAssignment.setOnClickListener(v -> {
            AssignmentModel.setStaticId(currentAssignment.getId());

            if (editAssignmentListener != null) {
                editAssignmentListener.onEditAssignmentClickListener(position);
            }
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
