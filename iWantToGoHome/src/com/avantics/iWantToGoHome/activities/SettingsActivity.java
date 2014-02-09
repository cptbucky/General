package com.avantics.iWantToGoHome.activities;

/**
 * Created with IntelliJ IDEA.
 * User: tom
 * Date: 20/08/13
 * Time: 22:39
 * To change this template use File | Settings | File Templates.
 */

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.avantics.iWantToGoHome.fragments.SettingsFragment;

public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment()).commit();
    }
}

