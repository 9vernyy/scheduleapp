package kz.alisher.scheduleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * Created by Alisher Kozhabay on 21.05.2016.
 */
public class StudentAddActivity extends AppCompatActivity {
    private SQLiteHandler db;
    private MaterialEditText fName;
    private MaterialEditText lName;
    private TextView groupTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new SQLiteHandler(this);

        groupTitle = (TextView) findViewById(R.id.group_title);
        fName = (MaterialEditText) findViewById(R.id.first_name);
        lName = (MaterialEditText) findViewById(R.id.last_name);

//        groupTitle.setText(getIntent().getStringExtra("group_id"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_subject, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.save_reminder:
                if (fName.getText().toString().isEmpty() && lName.getText().toString().isEmpty())
                    Toast.makeText(this, "Поля не должны быть пустыми!", Toast.LENGTH_SHORT).show();
                else {
                    saveStudent();
                }
                return true;

            case R.id.discard_reminder:
                Toast.makeText(getApplicationContext(), "Отмена",
                        Toast.LENGTH_SHORT).show();

                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveStudent() {
        db.addStudent(fName.getText().toString().trim(), lName.getText().toString().trim(), getIntent().getStringExtra("group_id"));
        startActivity(new Intent(StudentAddActivity.this, GroupsActivity.class));
        finish();
    }
}
