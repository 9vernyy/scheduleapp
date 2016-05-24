package kz.alisher.scheduleapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alisher Kozhabay on 21.05.2016.
 */
public class StudentViewAdapter extends RecyclerView.Adapter<StudentViewAdapter.ViewHolder> {
    List<Student> contents;
    Context ctx;

    public StudentViewAdapter(List<Student> contents, Context ctx) {
        this.ctx = ctx;
        this.contents = contents;
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Student item = contents.get(position);
        ArrayList<String> st = new ArrayList<>();
        st.add("Был");
        st.add("Не был");
        st.add("Болеет");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, st);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.status.setAdapter(dataAdapter);
        holder.name.setText(item.getlName() + " " + item.getfName());
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public Spinner status;

        public ViewHolder(View itemView) {
            super(itemView);
            status = (Spinner) itemView.findViewById(R.id.status_student);
            name = (TextView) itemView.findViewById(R.id.student_name);
        }
    }
}
