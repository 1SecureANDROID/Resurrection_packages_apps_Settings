/*
 * Copyright (C) 2012 ParanoidAndroid Project
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

package com.android.settings.paranoid;

import android.content.Context;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

/**
 * Statusbar Settings
 */
public class Statusbar extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    public static final String KEY_AM_PM_STYLE = "am_pm_style";
    public static final String KEY_SHOW_CLOCK = "show_clock";
    public static final String KEY_STATUS_BAR_NOTIF_COUNT = "status_bar_notif_count";

    private CheckBoxPreference mShowClock;
    private ListPreference mAmPmStyle;
    private CheckBoxPreference mStatusBarNotifCount;

    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.status_bar_settings);
        PreferenceScreen prefSet = getPreferenceScreen();
        mContext = getActivity();

        mShowClock = (CheckBoxPreference) prefSet.findPreference(KEY_SHOW_CLOCK);
        mShowClock.setChecked(Settings.System.getInt(mContext.getContentResolver(),
                Settings.System.STATUS_BAR_SHOW_CLOCK, 1) == 1);

        mAmPmStyle = (ListPreference) prefSet.findPreference(KEY_AM_PM_STYLE);
        int amPmStyle = Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.STATUS_BAR_AM_PM_STYLE, 2);
        mAmPmStyle.setValue(String.valueOf(amPmStyle));
        mAmPmStyle.setSummary(mAmPmStyle.getEntry());
        mAmPmStyle.setOnPreferenceChangeListener(this);

        try {
            if (Settings.System.getInt(getActivity().getContentResolver(),
                    Settings.System.TIME_12_24) == 24) {
                mAmPmStyle.setEnabled(false);
                mAmPmStyle.setSummary(R.string.status_bar_am_pm_info);
            }
        } catch (SettingNotFoundException e) {
            // This will hurt you, run away
        }

        mStatusBarNotifCount = (CheckBoxPreference) prefSet.findPreference(KEY_STATUS_BAR_NOTIF_COUNT);
        mStatusBarNotifCount.setChecked(Settings.System.getInt(getActivity().getContentResolver(), 
                Settings.System.STATUS_BAR_NOTIF_COUNT, 0) == 1);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mShowClock) {
            Settings.System.putInt(mContext.getContentResolver(),
                    Settings.System.STATUS_BAR_SHOW_CLOCK, mShowClock.isChecked()
                    ? 1 : 0);
        } else if (preference == mStatusBarNotifCount) {	
            Settings.System.putInt(mContext.getContentResolver(),
                    Settings.System.STATUS_BAR_NOTIF_COUNT,	mStatusBarNotifCount.isChecked()
                    ? 1 : 0);	
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mAmPmStyle) {
            int statusBarAmPmSize = Integer.valueOf((String) newValue);
            int index = mAmPmStyle.findIndexOfValue((String) newValue);
            Settings.System.putInt(mContext.getContentResolver(),
                    Settings.System.STATUS_BAR_AM_PM_STYLE, statusBarAmPmSize);
            mAmPmStyle.setSummary(mAmPmStyle.getEntries()[index]);
            return true;
        }
        return false;
    }
}
