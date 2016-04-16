package com.example.adilet.schedule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

/**
 * Created by Adilet on 14.04.2016.
 */
public class SubjectEditActivity extends AppCompatActivity {

    private EditText subjectName;
    private TextView startTime;
    private TextView endTime;
    private TextView cabinet;
    private int mHour;
    private int mMinute;
    private String mTime;
    //    private int userId;
//    private int dayId;
    private int subjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_subject_add);

        subjectName = (EditText) findViewById(R.id.subject_title);
        startTime = (TextView) findViewById(R.id.set_start_time);
        endTime = (TextView) findViewById(R.id.set_end_time);
        cabinet = (TextView) findViewById(R.id.set_cabinet);

        Intent i = getIntent();
//        userId = i.getIntExtra("userId", 0);
//        dayId = i.getIntExtra("dayId", 0);
//        getSupportActionBar().setTitle("Edit Subject");
        subjectId = Integer.parseInt(i.getStringExtra("subjectId"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SQLiteHandler db = new SQLiteHandler(getApplicationContext());

        Subject subjectById = db.getSubjectById(subjectId);
        Log.d("SUBJECT", subjectById.toString());

        subjectName.setText(subjectById.getNameSubject());
        startTime.setText(subjectById.getStartTime());
        endTime.setText(subjectById.getEndTime());
        cabinet.setText(subjectById.getCabinet() + "");
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
                if (subjectName.getText().toString().length() == 0)
                    subjectName.setError("Название предмета не может быть пустым!");

                else {
                    updateSubject();
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

    private void updateSubject() {
        SQLiteHandler db = new SQLiteHandler(getApplicationContext());

        String name = subjectName.getText().toString().trim();
        String _startTime = startTime.getText().toString().trim();
        String _endTime = endTime.getText().toString().trim();
        Integer cab = Integer.parseInt(cabinet.getText().toString().trim());


        db.updateSubject(new Subject(subjectId, name, _startTime, _endTime, cab));
        Toast.makeText(this, "Обновлено", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void setTimeStart(View view) {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(new StartTimeListener(), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        tpd.setThemeDark(false);
        tpd.setAccentColor(getResources().getColor(R.color.primary));
        tpd.show(getFragmentManager(), "TimePickerDialog");
    }

    public void setTimeEnd(View view) {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(new EndTimeListener(), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        tpd.setThemeDark(false);
        tpd.setAccentColor(getResources().getColor(R.color.primary));
        tpd.show(getFragmentManager(), "TimePickerDialog");
    }

    public void setCabinet(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Введите номер кабинета");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);

        alert.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cabinet.setText(input.getText());
            }
        });

        alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alert.show();
    }

    private class StartTimeListener implements TimePickerDialog.OnTimeSetListener {
        @Override
        public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int seconds) {
            mHour = hourOfDay;
            mMinute = minute;
            if (minute < 10) {
                mTime = hourOfDay + ":" + "0" + minute;
            } else {
                mTime = hourOfDay + ":" + minute;
            }
            startTime.setText(mTime);
        }
    }

    private class EndTimeListener implements TimePickerDialog.OnTimeSetListener {
        @Override
        public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int seconds) {
            mHour = hourOfDay;
            mMinute = minute;
            if (minute < 10) {
                mTime = hourOfDay + ":" + "0" + minute;
            } else {
                mTime = hourOfDay + ":" + minute;
            }
            endTime.setText(mTime);
        }
    }

}