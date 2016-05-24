package kz.alisher.scheduleapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Alisher Kozhabay on 22.05.2016.
 */
public class BootReceiver extends BroadcastReceiver {
    private String mTime;
    private String mDate;
    private String[] mDateSplit;
    private String[] mTimeSplit;
    private int mYear, mMonth, mHour, mMinute, mDay, mReceivedID;

    private Calendar mCalendar;
    private AlarmReceiver mAlarmReceiver;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            SQLiteHandler rb = new SQLiteHandler(context);
            mCalendar = Calendar.getInstance();
            mAlarmReceiver = new AlarmReceiver();

            List<Subject> reminders = rb.getAllSubjects();

            for (Subject rm : reminders) {
                mReceivedID = rm.getId();
                mDate = rm.getDateS();
                mTime = rm.getStartTime();

                mDateSplit = mDate.split("/");
                mTimeSplit = mTime.split(":");

                mDay = Integer.parseInt(mDateSplit[0]);
                mMonth = Integer.parseInt(mDateSplit[1]);
                mYear = Integer.parseInt(mDateSplit[2]);
                mHour = Integer.parseInt(mTimeSplit[0]);
                mMinute = Integer.parseInt(mTimeSplit[1]);

                mCalendar.set(Calendar.MONTH, --mMonth);
                mCalendar.set(Calendar.YEAR, mYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
                mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
                mCalendar.set(Calendar.MINUTE, mMinute);
                mCalendar.set(Calendar.SECOND, 0);


                mAlarmReceiver.setAlarm(context, mCalendar, mReceivedID);
            }
        }
    }
}
