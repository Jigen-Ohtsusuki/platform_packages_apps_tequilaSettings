/*
 * Copyright (C) 2022 crDroid Android Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tequila.settings.preferences;

import android.content.Context;
import android.os.SystemProperties;
import android.util.AttributeSet;
import androidx.preference.PreferenceDataStore;
import androidx.preference.SwitchPreference;
import com.android.settingslib.development.SystemPropPoker;

public class SystemPropertySwitchPreference extends SwitchPreference {

    public SystemPropertySwitchPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initStore();
    }

    public SystemPropertySwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        initStore();
    }

    public SystemPropertySwitchPreference(Context context) {
        super(context);
        initStore();
    }

    private void initStore() {
        setPreferenceDataStore(new SystemPropertiesDataStore());
    }

    /**
     * Internal DataStore that reads/writes directly to SystemProperties.
     * This mimics the behavior of the original SelfRemovingSwitchPreference logic.
     */
    private static class SystemPropertiesDataStore extends PreferenceDataStore {
        
        @Override
        public void putBoolean(String key, boolean value) {
            SystemProperties.set(key, Boolean.toString(value));
            
            try {
                SystemPropPoker.getInstance().poke();
            } catch (Exception e) {
            }
        }

        @Override
        public boolean getBoolean(String key, boolean defaultValue) {
            String val = SystemProperties.get(key);
            return val.isEmpty() ? defaultValue : Boolean.parseBoolean(val);
        }
    }
}