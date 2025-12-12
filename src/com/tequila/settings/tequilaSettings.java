package com.tequila.settings;

import android.os.Bundle;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

public class tequilaSettings extends SettingsPreferenceFragment {

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.tequila_settings);

        Preference aboutFragment = findPreference("about_fragment");
        if (aboutFragment != null) {
            aboutFragment.setLayoutResource(R.layout.superioros_dashboard_preference_tequila);
            aboutFragment.setTitle(R.string.tequila_settings_about_entry);
            aboutFragment.setSummary(R.string.tequila_settings_aboutus_entry);
            aboutFragment.setIcon(R.drawable.ic_tequila_settings);
        }

        findPreference("statusbar_fragment").setLayoutResource(R.layout.superioros_dashboard_preference_top);
        findPreference("quick_settings_fragment").setLayoutResource(R.layout.superioros_dashboard_preference_middle);
        findPreference("lockscreen_fragment").setLayoutResource(R.layout.superioros_dashboard_preference_middle);
        findPreference("notifications_fragment").setLayoutResource(R.layout.superioros_dashboard_preference_middle);
        findPreference("btn_fragment").setLayoutResource(R.layout.superioros_dashboard_preference_middle);
        findPreference("misc_fragment").setLayoutResource(R.layout.superioros_dashboard_preference_bottom);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TEQUILA;
    }

}
