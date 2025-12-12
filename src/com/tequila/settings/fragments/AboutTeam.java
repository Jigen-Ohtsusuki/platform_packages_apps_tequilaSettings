package com.tequila.settings.fragments;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;

import com.android.internal.logging.nano.MetricsProto;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;


public class AboutTeam extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.tequila_settings_about);
        Preference aboutRom = findPreference("about_rom");
        if (aboutRom != null) {
            aboutRom.setLayoutResource(R.layout.superioros_dashboard_preference_tequila);
            aboutRom.setTitle(R.string.tequila_settings_aboutus);
            aboutRom.setSummary(R.string.tequila_settings_aboutus_summary);
            aboutRom.setIcon(R.drawable.ic_tequila_settings);
        }

        findPreference("github").setLayoutResource(R.layout.superioros_dashboard_preference_top);
        findPreference("gitlab").setLayoutResource(R.layout.superioros_dashboard_preference_bottom);
        findPreference("Main").setLayoutResource(R.layout.superioros_dashboard_preference_top);
        findPreference("Source").setLayoutResource(R.layout.superioros_dashboard_preference_bottom);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TEQUILA;
    }
}
