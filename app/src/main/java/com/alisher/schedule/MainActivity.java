package com.alisher.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnDateSelectedListener, OnMonthChangedListener {
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

    MaterialCalendarView widget;
    private SQLiteHandler sqLiteHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        widget = (MaterialCalendarView) findViewById(R.id.calendarView);
        widget.setOnDateChangedListener(this);
        widget.setOnMonthChangedListener(this);
        widget.addDecorator(new OneDayDecorator());
        Calendar calendar = Calendar.getInstance();
        widget.setSelectedDate(calendar.getTime());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        sqLiteHandler = new SQLiteHandler(getApplicationContext());

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
        //int daysOfweek = date.getDay();
        String weekDay;
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);

        weekDay = dayFormat.format(date.getCalendar().getTime());
        Toast.makeText(this, "Current day: " + weekDay, Toast.LENGTH_SHORT).show();
        int dayId = 0;
        switch (weekDay){
            case "Monday":
                dayId = 1;
                break;
            case "Tuesday":
                dayId = 2;
                break;
            case "Wednesday":
                dayId = 3;
                break;
            case "Thursday":
                dayId = 4;
                break;
            case "Friday":
                dayId = 5;
                break;
            case "Saturday":
                dayId = 6;
                break;
            case "Sunday":
                dayId = 7;
                break;
            default:
                break;
        }
        HashMap<String, String> userDetails = sqLiteHandler.getUserDetails();
        int id = Integer.parseInt(userDetails.get("id"));
        Log.d("ID", id + ", " + dayId);
        Subject subject = new Subject(id, dayId, "Mathematics", "10:00", "11:00", 105);
        sqLiteHandler.addSchedule(subject);
        Intent i = new Intent(this, SubjectActivity.class);
        i.putExtra("userId", id);
        i.putExtra("dayId", dayId);
        startActivity(i);
//        List<Subject> subjectsByDay = sqLiteHandler.getSubjectsByDay(id, dayId);
//        Log.d("OBJECTS",subjectsByDay.get(0) + "");
//        subjectsByDay.get(0).setNameSubject("Physic");
//        sqLiteHandler.updateSubject(subjectsByDay.get(0));
//        subjectsByDay = sqLiteHandler.getSubjectsByDay(id, dayId);
//        Log.d("OBJECTS 2", subjectsByDay.get(0) + "");
//        sqLiteHandler.deleteSubject(subjectsByDay.get(0));
//        subjectsByDay = sqLiteHandler.getSubjectsByDay(id, dayId);
//        Log.d("OBJECTS 3", subjectsByDay.get(0) + "");
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        //noinspection ConstantConditions
        getSupportActionBar().setTitle(FORMATTER.format(date.getDate()));
    }

    public void AddSubject() {
    }
}
