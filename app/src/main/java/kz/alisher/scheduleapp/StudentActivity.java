package kz.alisher.scheduleapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alisher Kozhabay on 21.05.2016.
 */
public class StudentActivity extends AppCompatActivity {
    private StudentViewAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private List<Student> students;
    private SQLiteHandler db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Посещаемость");
        db = new SQLiteHandler(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.student_rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        getStudents();
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
    }

    private void getStudents() {
        students = new ArrayList<>();
        students = db.getStudentsByGroupId(getIntent().getStringExtra("group_id"));
        mAdapter = new StudentViewAdapter(students, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== android.R.id.home){
            onBackPressed();
        }
        return true;
    }
}
