package com.example.adilet.schedule;

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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnDateSelectedListener, OnMonthChangedListener {
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

    MaterialCalendarView widget;
    private SQLiteHandler sqLiteHandler;
    private TextView fullName;
    private TextView email;
    private NavigationView navigationView;

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

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        sqLiteHandler = new SQLiteHandler(getApplicationContext());
        initProfile();
    }

    private void initProfile() {
        View header = navigationView.getHeaderView(0);
        fullName = (TextView) header.findViewById(R.id.fullName);
        email = (TextView) header.findViewById(R.id.email);
        Log.d("FULLNAME", getIntent().getStringExtra("fullname"));
        fullName.setText(getIntent().getStringExtra("fullname"));
        email.setText(getIntent().getStringExtra("email"));
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

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
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
        Intent i = new Intent(this, SubjectActivity.class);
        i.putExtra("userId", id);
        i.putExtra("dayId", dayId);
        i.putExtra("date", FORMATTER.format(date.getDate()));
        startActivity(i);
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        //noinspection ConstantConditions
        getSupportActionBar().setTitle(FORMATTER.format(date.getDate()));
    }

}