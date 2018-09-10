package io.github.nikkoes.katalogfilm.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.provider.Settings;
import android.widget.Toast;

import butterknife.BindString;
import butterknife.ButterKnife;
import io.github.nikkoes.katalogfilm.R;
import io.github.nikkoes.katalogfilm.activity.SettingsActivity;
import io.github.nikkoes.katalogfilm.scheduler.MovieReceiver;


public class MyPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener{

    MovieReceiver movieReceiver = new MovieReceiver();

    public MyPreferenceFragment(){

    }

    @BindString(R.string.key_daily_reminder)
    String reminder_daily;

    @BindString(R.string.key_released_today)
    String released_today;

    @BindString(R.string.key_setting_locale)
    String setting_locale;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        ButterKnife.bind(this, getActivity());

        findPreference(reminder_daily).setOnPreferenceChangeListener(this);
        findPreference(released_today).setOnPreferenceChangeListener(this);
        findPreference(setting_locale).setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        String key = preference.getKey();
        boolean isOn = (boolean) o;

        if (key.equals(reminder_daily)) {
            if (isOn) {
                movieReceiver.setRepeatingAlarm(getActivity(), MovieReceiver.TYPE_REPEATING, "07:00", getString(R.string.label_alarm_daily_reminder));
            } else {
                movieReceiver.cancelAlarm(getActivity(), MovieReceiver.TYPE_REPEATING);
            }
            return true;
        }

        else if (key.equals(released_today)) {
            if (isOn) {
                movieReceiver.setRepeatingAlarm(getActivity(), MovieReceiver.TYPE_RELEASED, "08:00", getString(R.string.label_alarm_released_today));
            }
            else{
                movieReceiver.cancelAlarm(getActivity(), MovieReceiver.TYPE_RELEASED);
            }
            return true;
        }


        return false;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();

        if (key.equals(setting_locale)) {
            Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intent);
            getActivity().finish();

            return true;
        }

        return false;
    }
}