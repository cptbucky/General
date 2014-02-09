package com.avantics.iWantToGoHome.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import com.avantics.iWantToGoHome.R;
import com.avantics.iWantToGoHome.TimePreference;

/**
 * Created with IntelliJ IDEA.
 * User: tom
 * Date: 20/08/13
 * Time: 22:46
 * To change this template use File | Settings | File Templates.
 */
public class SettingsFragment extends PreferenceFragment implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String PREF_HOME_TIME = "pref_home_time";

    private SharedPreferences sPreferences;

    private TimePreference prefHomeTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.layout.settings);

        // get shared resources for resume
        sPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        prefHomeTime = (TimePreference) getPreferenceScreen()
                .findPreference(PREF_HOME_TIME);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        if (key.equals(PREF_HOME_TIME)) {
            prefHomeTime.setSummary(sharedPreferences.getString(key, ""));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        prefHomeTime.setSummary(sPreferences.getString(PREF_HOME_TIME,
                "").equals("") ? getResources().getText(
                R.string.pref_home_time_summary) : sPreferences.getString(
                PREF_HOME_TIME, ""));

        // register for changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
