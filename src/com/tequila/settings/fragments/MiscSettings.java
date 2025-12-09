package com.tequila.settings.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.os.Handler;
import android.util.Log;
import com.tequila.settings.fragments.misc.KeyboxDataPreference;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;

import com.android.internal.logging.nano.MetricsProto;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import com.android.internal.util.tequila.SystemRestartUtils;

public class MiscSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    private static final String KEYBOX_DATA_KEY = "keybox_data_setting";
    private static final String KEY_PIF_JSON_FILE_PREFERENCE = "pif_json_file_preference";
    private ActivityResultLauncher<Intent> mKeyboxFilePickerLauncher;
    private KeyboxDataPreference mKeyboxDataPreference;
    private Preference mPifJsonFilePreference;
    private Handler mHandler;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        mHandler = new Handler();
        addPreferencesFromResource(R.xml.tequila_settings_misc);

        mPifJsonFilePreference = findPreference(KEY_PIF_JSON_FILE_PREFERENCE);
        mKeyboxFilePickerLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
            Uri uri = result.getData().getData();
            Preference pref = findPreference(KEYBOX_DATA_KEY);
            if (pref instanceof KeyboxDataPreference) {
                ((KeyboxDataPreference) pref).handleFileSelected(uri);
            }
        }
    }
    );
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TEQUILA;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mKeyboxDataPreference = findPreference(KEYBOX_DATA_KEY);
        if (mKeyboxDataPreference != null) {
            mKeyboxDataPreference.setFilePickerLauncher(mKeyboxFilePickerLauncher);
        }
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference == mPifJsonFilePreference) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/json");
            startActivityForResult(intent, 10001);
            return true;
        }
        return super.onPreferenceTreeClick(preference);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10001 && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            Log.d(TAG, "URI received: " + uri.toString());
            try (InputStream inputStream = getActivity().getContentResolver().openInputStream(uri)) {
                if (inputStream != null) {
                    String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    Log.d(TAG, "JSON data: " + json);
                    JSONObject jsonObject = new JSONObject(json);
                    for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
                        String key = it.next();
                        String value = jsonObject.getString(key);
                        Log.d(TAG, "Setting property: persist.sys.pihooks_" + key + " = " + value);
                        SystemProperties.set("persist.sys.pihooks_" + key, value);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error reading JSON or setting properties", e);
            }
            mHandler.postDelayed(() -> {
                SystemRestartUtils.showSystemRestartDialog(getContext());
            }, 1250);
        }
    }
}