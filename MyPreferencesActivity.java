package com.hadar.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class MyPreferencesActivity extends AppCompatActivity {

    public static final String LANDED_ONLY = "landed_only";
    public static final String FLYING_ONLY = "flying_only";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment {

        private CheckBoxPreference landedOnly;
        private CheckBoxPreference flyingOnly;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            landedOnly = (CheckBoxPreference) findPreference("onlyLandedFlights");
            flyingOnly = (CheckBoxPreference) findPreference("onlyFlyingFlights");

            landedOnly.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    landedOnly.setChecked(true);
                    flyingOnly.setChecked(false);

                    return true;
                }
            });

            flyingOnly.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    landedOnly.setChecked(false);
                    flyingOnly.setChecked(true);

                    return true;
                }
            });
        }
    }

    @Override
    public void onBackPressed() {

        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        boolean landedOnly = getPrefs.getBoolean("onlyLandedFlights", false);
        boolean flyingOnly = getPrefs.getBoolean("onlyFlyingFlights", false);

        Intent i = new Intent();
        i.putExtra(LANDED_ONLY, landedOnly);
        i.putExtra(FLYING_ONLY, flyingOnly);
        setResult(RESULT_OK, i);
        finish();
    }
}
