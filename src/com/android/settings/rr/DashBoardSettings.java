/*Copyright (C) 2015 The ResurrectionRemix Project
     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at

          http://www.apache.org/licenses/LICENSE-2.0

     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License.
*/
package com.android.settings.rr;

import android.app.AlertDialog;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Process;
import android.content.Context;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.PreferenceCategory;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.SwitchPreference;
import android.provider.Settings;
import android.text.Spannable;
import android.text.TextUtils;
import android.widget.EditText;

import android.provider.SearchIndexableResource;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.settings.rr.Preferences.SystemSettingSwitchPreference;
import com.android.settings.rr.utils.RRUtils;
import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.search.SearchIndexable;


import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
@SearchIndexable
public class DashBoardSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener, Indexable {
    private static final String TAG = "UI";
    private static final String RR_CONFIG = "rr_config_style";
    private static final String ONE_UI = "settings_spacer";
    private static final String ANIMATION = "rr_config_anim";
    private static final String STYLE = "settings_spacer_style";
    private static final String FONT = "settings_spacer_font_style";
    private static final String SIZE = "settings_display_anim";

    private ListPreference mConfig;
    private SystemSettingSwitchPreference mUI;
    private ListPreference mAnim;
    private ListPreference mHomeStyle;
    private ListPreference mHomeFont;
    private ListPreference mSize;


    @Override
    public int getMetricsCategory() {
        return MetricsEvent.RESURRECTED;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.rr_dashboard_settings);
        final ContentResolver resolver = getActivity().getContentResolver();
        mConfig = (ListPreference) findPreference(RR_CONFIG);
        mConfig.setValue(String.valueOf(Settings.System.getInt(
                getContentResolver(), Settings.System.RR_CONFIG_STYLE, 0)));
        mConfig.setSummary(mConfig.getEntry());
        mConfig.setOnPreferenceChangeListener(this);
        
        mUI = (SystemSettingSwitchPreference) findPreference(ONE_UI);
        mUI.setOnPreferenceChangeListener(this);

        mAnim = (ListPreference) findPreference(ANIMATION);
        mAnim.setOnPreferenceChangeListener(this);
        int style = Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.SETTINGS_SPACER_STYLE, 0);
        mHomeStyle = (ListPreference) findPreference(STYLE);
        mHomeStyle.setOnPreferenceChangeListener(this);
        mHomeFont = (ListPreference) findPreference(FONT);
        mHomeFont.setOnPreferenceChangeListener(this);
        mSize = (ListPreference) findPreference(SIZE);
        mSize.setOnPreferenceChangeListener(this);
        updatePrefs(style);
        int anim = Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.RR_CONFIG_ANIM, 0);
        try {
            if (anim == 0) {
                removePreference("animation");
            } else if (anim == 1) {
                removePreference("preview");
            } else if (anim == 2) {
                removePreference("animation");
                removePreference("preview");
            }
        } catch (Exception e) {}
        mFooterPreferenceMixin.createFooterPreference().setTitle(R.string.switch_ui_warning);
       

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
      if (preference == mConfig) {
            int style = Integer.parseInt((String) objValue);
            Settings.System.putInt(getContentResolver(), Settings.System.RR_CONFIG_STYLE,
            Integer.valueOf((String) objValue));
            mConfig.setValue(String.valueOf(objValue));
            mConfig.setSummary(mConfig.getEntry());
             AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
             alertDialog.setTitle(getString(R.string.rr_dashboard_ui));
             alertDialog.setMessage(getString(R.string.rr_tools_message));
             alertDialog.setButton(getString(R.string.rr_reset_yes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                               Intent fabIntent = new Intent();
                               fabIntent.setClassName("com.android.settings", 
                                     "com.android.settings.Settings$MainSettingsLayoutActivity");
                                startActivity(fabIntent);
                       }
                    });
              alertDialog.setButton(Dialog.BUTTON_NEGATIVE ,getString(R.string.rr_reset_cancel), new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int which) {
                            return;
                         }
                  });
             alertDialog.show();
            return true;
       } else if (preference == mUI) {
             AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
             alertDialog.setTitle(getString(R.string.rr_dashboard_ui));
             alertDialog.setMessage(getString(R.string.rr_dashboard_message));
             alertDialog.setButton(getString(R.string.rr_reset_yes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                         Process.killProcess(Process.myPid());
                       }
                    });
              alertDialog.setButton(Dialog.BUTTON_NEGATIVE ,getString(R.string.rr_reset_cancel), new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int which) {
                            return;
                         }
                  });
             alertDialog.show();
            return true;
         } else if (preference == mAnim) {
             AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
             alertDialog.setTitle(getString(R.string.rr_dashboard_ui));
             alertDialog.setMessage(getString(R.string.rr_tools_ui));
             alertDialog.setButton(getString(R.string.rr_reset_yes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                             Process.killProcess(Process.myPid());
                     }
               });
              alertDialog.setButton(Dialog.BUTTON_NEGATIVE ,getString(R.string.rr_reset_cancel), new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int which) {
                            return;
                         }
                  });
             alertDialog.show();
            return true;
         } else if (preference == mHomeStyle) {
             int val = Integer.parseInt((String) objValue);
             AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
             alertDialog.setTitle(getString(R.string.rr_dashboard_ui));
             alertDialog.setMessage(getString(R.string.rr_tools_ui));
             alertDialog.setButton(getString(R.string.rr_reset_yes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                             Process.killProcess(Process.myPid());
                     }
               });
              alertDialog.setButton(Dialog.BUTTON_NEGATIVE ,getString(R.string.rr_reset_cancel), new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int which) {
                            return;
                         }
                  });
             alertDialog.show();
             updatePrefs(val);
            return true;
         } else if (preference == mHomeFont) {
             int val = Integer.parseInt((String) objValue);
             AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
             alertDialog.setTitle(getString(R.string.rr_dashboard_ui));
             alertDialog.setMessage(getString(R.string.rr_tools_ui));
             alertDialog.setButton(getString(R.string.rr_reset_yes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                             Process.killProcess(Process.myPid());
                     }
               });
              alertDialog.setButton(Dialog.BUTTON_NEGATIVE ,getString(R.string.rr_reset_cancel), new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int which) {
                            return;
                         }
                  });
             alertDialog.show();
            return true;
         } else if (preference == mSize) {
             int val = Integer.parseInt((String) objValue);
             AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
             alertDialog.setTitle(getString(R.string.rr_dashboard_ui));
             alertDialog.setMessage(getString(R.string.rr_tools_ui));
             alertDialog.setButton(getString(R.string.rr_reset_yes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                             Process.killProcess(Process.myPid());
                     }
               });
              alertDialog.setButton(Dialog.BUTTON_NEGATIVE ,getString(R.string.rr_reset_cancel), new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int which) {
                            return;
                         }
                  });
             alertDialog.show();
            return true;
         }
        return false;
    }

     @Override
     public boolean onPreferenceTreeClick(Preference preference) {
        return false;
    }

    private void updatePrefs(int which) {
        if (which == 2) {
            mHomeFont.setEnabled(true);
            mSize.setEnabled(true);
        } else {
            mHomeFont.setEnabled(false);
            mSize.setEnabled(false);
        }
    }
    /**
     * For Search.
     */
    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
        new BaseSearchIndexProvider() {
            @Override
            public List<SearchIndexableResource> getXmlResourcesToIndex(Context context, boolean enabled) {
                ArrayList<SearchIndexableResource> result =
                    new ArrayList<SearchIndexableResource>();
                    SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.rr_dashboard_settings;
                    result.add(sir);
                    return result;
            }

            @Override
            public List<String> getNonIndexableKeys(Context context) {
                List<String> keys = super.getNonIndexableKeys(context);
                return keys;
            }
        };
}
