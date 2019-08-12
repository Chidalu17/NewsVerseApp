package com.example.newsverse;


import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        // Navigate with the app icon in the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    /**
     * The NewsPreferenceFragment implements the Preference.OnPreferenceChangeListener interface
     * to set up to listen for any Preference changes made by the user.
     * And the NewsPreferenceFragment also implements the DatePickerDialog.OnDateSetListener to
     * receive a callback when the user has finished selecting a date.
     */
    public static class NewsPreferenceFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            // Find the preference for number of items
            Preference country = findPreference(getString(R.string.settings_select_your_country_key));
            // bind the current preference value to be displayed
            bindPreferenceSummaryToValue(country);


        }



        /**
         * This method is called when the user has changed a Preference.
         * Update the displayed preference summary (the UI) after it has been changed.
         * @param preference the changed Preference
         * @param value the new value of the Preference
         * @return True to update the state of the Preference with the new value
         */
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            // Update the summary of a ListPreference using the label
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }

        /**
         * Set this fragment as the OnPreferenceChangeListener and
         * bind the value that is in SharedPreferences to what will show up in the preference summary
         */
        private void bindPreferenceSummaryToValue(Preference preference) {
            // Set the current NewsPreferenceFragment instance to listen for changes to the preference
            // we pass in using
            preference.setOnPreferenceChangeListener(this);

            // Read the current value of the preference stored in the SharedPreferences on the device,
            // and display that in the preference summary
            SharedPreferences preferences =
                    PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }

    }

    // Go back to the MainActivity when up button in action bar is clicked on.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

