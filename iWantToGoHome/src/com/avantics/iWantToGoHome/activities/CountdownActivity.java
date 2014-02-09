package com.avantics.iWantToGoHome.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.avantics.iWantToGoHome.R;
import com.avantics.iWantToGoHome.fragments.SettingsFragment;

public class CountdownActivity extends Activity {
    private TextView timerLabel;
    private TextView goHomeDecisionLabel;
    private TextView subHomeDecisionLabel;
    private CountDownTimer currentTimer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        PreferenceManager.setDefaultValues(this, R.layout.settings, false);

        timerLabel = (TextView) findViewById(R.id.countdownLabel);
        goHomeDecisionLabel = (TextView) findViewById(R.id.goHomeDecision);
        subHomeDecisionLabel = (TextView) findViewById(R.id.subHomeDecision);

//        setTimer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    Intent i = new Intent(this, SupportSettingsActivity.class);
                    startActivityForResult(i, 1);
                } else {
                    Intent i = new Intent(this, SettingsActivity.class);
                    startActivityForResult(i, 1);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        setTimer();
    }

    @Override
    protected void onStart() {
        super.onStart();

//        setTimer();
    }

    public String formatTime(long millis) {
        String output = "00:00";
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        seconds = seconds % 60;
        minutes = minutes % 60;
        hours = hours % 60;

        String sec = String.valueOf(seconds);
        String min = String.valueOf(minutes);
        String hrs = String.valueOf(hours);

        if (seconds < 10)
            sec = "0" + seconds;
        if (minutes < 10)
            min = "0" + minutes;
        if (hours < 10)
            hrs = "0" + hours;

        output = hrs + ":" + min + ":" + sec;

        return output;
    }

    private void setTimer() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Time TimeNow = new Time();
        TimeNow.setToNow(); // set the date to Current Time
        TimeNow.normalize(true);
        long millis2 = TimeNow.toMillis(true);

        String leaveTime = preferences.getString(SettingsFragment.PREF_HOME_TIME, String.valueOf(MODE_PRIVATE));

        if (leaveTime.equals("0")) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(SettingsFragment.PREF_HOME_TIME, "17:30");
            editor.commit();

            leaveTime = "17:30";
        }

        int leaveTimeHours = Integer.parseInt(leaveTime.split(":")[0]);
        int leaveTimeMinutes = Integer.parseInt(leaveTime.split(":")[1]);

        int offset = 0;
        if (TimeNow.weekDay > 5 || TimeNow.weekDay == 4 && (TimeNow.hour > leaveTimeHours || TimeNow.hour == leaveTimeHours && TimeNow.minute > leaveTimeMinutes)) {
            offset = 7 - TimeNow.weekDay;
        } else if (TimeNow.hour > leaveTimeHours || TimeNow.hour == leaveTimeHours && TimeNow.minute > leaveTimeMinutes) {
            offset = 1;
        }

        Time TimerSet = new Time();
        TimerSet.set(0, leaveTimeMinutes, leaveTimeHours, TimeNow.monthDay + offset, TimeNow.month, TimeNow.year); //day month year
        TimerSet.normalize(true);
        long millis = TimerSet.toMillis(true);

        long millisset = millis - millis2; //subtract current from future to set the time remaining

        if (currentTimer != null) {
            currentTimer.cancel();
        }

        currentTimer = new CountDownTimer(millisset, 1000) {
            public void onTick(long millisUntilFinished) {
                timerLabel.setText(formatTime(millisUntilFinished));
            }

            public void onFinish() {
                goHomeDecisionLabel.setText("Go! Go! Go!");
                subHomeDecisionLabel.setText("Go for a run? Time for the Pub? Dinner Out? Catch up with Friends? Mozie on down to the Pictures? Take in a show?");
            }
        }.start();
    }
}
