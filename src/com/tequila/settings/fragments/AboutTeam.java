package com.tequila.settings.fragments;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.PreferenceScreen;

import com.android.internal.logging.nano.MetricsProto;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.tequila.settings.preferences.ExpandableAboutPreference;

public class AboutTeam extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.tequila_settings_about);
        
        // Replace the default preference with our custom expandable preference
        PreferenceScreen screen = getPreferenceScreen();
        Preference oldPref = findPreference("about_rom");
        
        if (oldPref != null) {
            // Create new expandable preference
            ExpandableAboutPreference aboutRom = new ExpandableAboutPreference(getContext());
            aboutRom.setKey("about_rom");
            aboutRom.setTitle(R.string.tequila_settings_aboutus);
            aboutRom.setSummary(R.string.tequila_settings_aboutus_summary);
            aboutRom.setOrder(oldPref.getOrder());
            
            // Remove old preference and add new one
            screen.removePreference(oldPref);
            screen.addPreference(aboutRom);
        }

        // Set layouts for other preferences
        Preference github = findPreference("github");
        if (github != null) {
            github.setLayoutResource(R.layout.superioros_dashboard_preference_top);
        }
        
        Preference gitlab = findPreference("gitlab");
        if (gitlab != null) {
            gitlab.setLayoutResource(R.layout.superioros_dashboard_preference_bottom);
        }
        
        Preference main = findPreference("Main");
        if (main != null) {
            main.setLayoutResource(R.layout.superioros_dashboard_preference_top);
        }
        
        Preference source = findPreference("Source");
        if (source != null) {
            source.setLayoutResource(R.layout.superioros_dashboard_preference_bottom);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.AOSPA;
    }
}
