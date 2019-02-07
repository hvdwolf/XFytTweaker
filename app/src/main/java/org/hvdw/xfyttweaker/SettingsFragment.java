package org.hvdw.xfyttweaker;

import android.util.Log;
//import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
//import android.net.Uri;
import android.preference.Preference;
import android.preference.PreferenceFragment;
//import android.preference.PreferenceManager;
//import android.preference.PreferenceActivity;
//import android.preference.ListPreference;
import android.widget.Toast;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;

import android.os.AsyncTask;
import android.os.Handler;
import android.widget.ProgressBar;
import android.util.AttributeSet;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import java.util.List;
import java.util.ArrayList;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.File;



public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
    Context mContext;
    AttributeSet attrs;

    private ProgressBar pb;
    static Runnable myRunnable;
    private static Handler myHandler;
    private boolean zygote_reboot;

    private BroadcastReceiver broadcastReceiver;
    IntentFilter intentFilter = new IntentFilter();



    @Override
    public void onAttach(Activity activity) {
//    public void onAttach(Context mContext) {
//    super.onAttach(mContext);
        super.onAttach(activity);
        mContext = getActivity();
        //attrs = getAttributeSet();

        //Toast mToast = Toast.makeText(mContext, "Retrieving installed apps", Toast.LENGTH_LONG);
        //mToast.show();

        //getPackages(mContext);
        AsyncTask<Void, Void, Void> doAppsList = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                //AppsListPref.AppsListPref(mContext, attrs);
                //new AppsList.AppsListPref(mContext, attrs);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                //pd.dismiss();
            }

    };


    doAppsList.execute((Void[])null);
    }

    public static final String TAG = "XFytTweaker-SettingsFragment";



    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N){
            Log.d(TAG, "onCreate: in Sofia 6.0.1 sdk23");
            //Old version used on Sofia 6.0.1 sdk23
            getPreferenceManager().setSharedPreferencesMode(Context.MODE_WORLD_READABLE);
            addPreferencesFromResource(R.xml.preferences);
        } else {
            Log.d(TAG, "onCreate: Running on Android 8.0.0 sdk26");
            getPreferenceManager().setSharedPreferencesMode(Context.MODE_PRIVATE);
            addPreferencesFromResource(R.xml.preferences);
        }

        getActivity().registerReceiver(broadcastReceiver, intentFilter);
        //getPackages(mContext);
    }

    @Override
    public void onResume() {
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
        super.onResume();
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Intent intent = new Intent();
        String toastText = "";
        String additionalText = "";

        switch (key) {
            /* All the settings belonging to the SofiaServer */
            case MySettings.PREF_NO_KILL:
                intent.setAction(MySettings.ACTION_PREF_NO_KILL_CHANGED);
                intent.putExtra(MySettings.EXTRA_PREF_NO_KILL_ENABLED, sharedPreferences.getBoolean(key, false));
                toastText = "BOOLEAN_KEY";
                break;
            case MySettings.PREF_UsbDac:
                intent.setAction(MySettings.ACTION_PREF_UsbDac_CHANGED);
                intent.putExtra(MySettings.EXTRA_PREF_UsbDac_ENABLED, sharedPreferences.getBoolean(key, false));
                toastText = "BOOLEAN_KEY";
                break;
            case MySettings.PREF_NO_MCU_ERRORS:
                intent.setAction(MySettings.ACTION_PREF_NO_MCU_ERRORS_CHANGED);
                intent.putExtra(MySettings.EXTRA_PREF_NO_MCU_ERRORS_ENABLED, sharedPreferences.getBoolean(key, false));
                toastText = "BOOLEAN_KEY";
                break;
            case MySettings.PREF_SKIP_CH_FOUR:
                intent.setAction(MySettings.ACTION_PREF_SKIP_CH_FOUR_CHANGED);
                intent.putExtra(MySettings.EXTRA_PREF_SKIP_CH_FOUR_ENABLED, sharedPreferences.getBoolean(key, false));
                toastText = "BOOLEAN_KEY";
                break;
            case MySettings.PREF_TAP_DELAY:
                intent.setAction(MySettings.ACTION_PREF_TAP_DELAY_CHANGED);
                intent.putExtra(MySettings.EXTRA_PREF_TAP_DELAY_ENTRY, sharedPreferences.getString(key, "300"));
                break;
            case MySettings.BAND_CALL_OPTION:
                intent.setAction(MySettings.ACTION_BAND_CALL_OPTION_CHANGED);
                intent.putExtra(MySettings.EXTRA_BAND_CALL_OPTION_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.BAND_CALL_ENTRY:
                intent.setAction(MySettings.ACTION_BAND_CALL_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_BAND_CALL_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.BAND_CALL_OPTION_SECOND:
                intent.setAction(MySettings.ACTION_BAND_CALL_OPTION_SECOND_CHANGED);
                intent.putExtra(MySettings.EXTRA_BAND_CALL_OPTION_SECOND_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.BAND_CALL_ENTRY_SECOND:
                intent.setAction(MySettings.ACTION_BAND_CALL_ENTRY_SECOND_CHANGED);
                intent.putExtra(MySettings.EXTRA_BAND_CALL_ENTRY_SECOND_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.BAND_CALL_OPTION_THIRD:
                intent.setAction(MySettings.ACTION_BAND_CALL_OPTION_THIRD_CHANGED);
                intent.putExtra(MySettings.EXTRA_BAND_CALL_OPTION_THIRD_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.BAND_CALL_ENTRY_THIRD:
                intent.setAction(MySettings.ACTION_BAND_CALL_ENTRY_THIRD_CHANGED);
                intent.putExtra(MySettings.EXTRA_BAND_CALL_ENTRY_THIRD_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.BT_PHONE_CALL_OPTION:
                intent.setAction(MySettings.ACTION_BT_PHONE_CALL_OPTION_CHANGED);
                intent.putExtra(MySettings.EXTRA_BT_PHONE_CALL_OPTION_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.BT_PHONE_CALL_ENTRY:
                intent.setAction(MySettings.ACTION_BT_PHONE_CALL_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_BT_PHONE_CALL_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.BT_PHONE_CALL_OPTION_SECOND:
                intent.setAction(MySettings.ACTION_BT_PHONE_CALL_OPTION_SECOND_CHANGED);
                intent.putExtra(MySettings.EXTRA_BT_PHONE_CALL_OPTION_SECOND_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.BT_PHONE_CALL_ENTRY_SECOND:
                intent.setAction(MySettings.ACTION_BT_PHONE_CALL_ENTRY_SECOND_CHANGED);
                intent.putExtra(MySettings.EXTRA_BT_PHONE_CALL_ENTRY_SECOND_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.BT_PHONE_CALL_OPTION_THIRD:
                intent.setAction(MySettings.ACTION_BT_PHONE_CALL_OPTION_THIRD_CHANGED);
                intent.putExtra(MySettings.EXTRA_BT_PHONE_CALL_OPTION_THIRD_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.BT_PHONE_CALL_ENTRY_THIRD:
                intent.setAction(MySettings.ACTION_BT_PHONE_CALL_ENTRY_THIRD_CHANGED);
                intent.putExtra(MySettings.EXTRA_BT_PHONE_CALL_ENTRY_THIRD_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.BT_HANG_CALL_OPTION:
                intent.setAction(MySettings.ACTION_BT_HANG_CALL_OPTION_CHANGED);
                intent.putExtra(MySettings.EXTRA_BT_HANG_CALL_OPTION_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.BT_HANG_CALL_ENTRY:
                intent.setAction(MySettings.ACTION_BT_HANG_CALL_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_BT_HANG_CALL_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.DVD_CALL_OPTION:
                intent.setAction(MySettings.ACTION_DVD_CALL_OPTION_CHANGED);
                intent.putExtra(MySettings.EXTRA_DVD_CALL_OPTION_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.DVD_CALL_ENTRY:
                intent.setAction(MySettings.ACTION_DVD_CALL_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_DVD_CALL_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.DVD_CALL_OPTION_SECOND:
                intent.setAction(MySettings.ACTION_DVD_CALL_OPTION_SECOND_CHANGED);
                intent.putExtra(MySettings.EXTRA_DVD_CALL_OPTION_SECOND_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.DVD_CALL_ENTRY_SECOND:
                intent.setAction(MySettings.ACTION_DVD_CALL_ENTRY_SECOND_CHANGED);
                intent.putExtra(MySettings.EXTRA_DVD_CALL_ENTRY_SECOND_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.DVD_CALL_OPTION_THIRD:
                intent.setAction(MySettings.ACTION_DVD_CALL_OPTION_THIRD_CHANGED);
                intent.putExtra(MySettings.EXTRA_DVD_CALL_OPTION_THIRD_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.DVD_CALL_ENTRY_THIRD:
                intent.setAction(MySettings.ACTION_DVD_CALL_ENTRY_THIRD_CHANGED);
                intent.putExtra(MySettings.EXTRA_DVD_CALL_ENTRY_THIRD_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.EJECT_CALL_OPTION:
                intent.setAction(MySettings.ACTION_EJECT_CALL_OPTION_CHANGED);
                intent.putExtra(MySettings.EXTRA_EJECT_CALL_OPTION_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.EJECT_CALL_ENTRY:
                intent.setAction(MySettings.ACTION_EJECT_CALL_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_EJECT_CALL_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.EJECT_CALL_OPTION_SECOND:
                intent.setAction(MySettings.ACTION_EJECT_CALL_OPTION_SECOND_CHANGED);
                intent.putExtra(MySettings.EXTRA_EJECT_CALL_OPTION_SECOND_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.EJECT_CALL_ENTRY_SECOND:
                intent.setAction(MySettings.ACTION_EJECT_CALL_ENTRY_SECOND_CHANGED);
                intent.putExtra(MySettings.EXTRA_EJECT_CALL_ENTRY_SECOND_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.EJECT_CALL_OPTION_THIRD:
                intent.setAction(MySettings.ACTION_EJECT_CALL_OPTION_THIRD_CHANGED);
                intent.putExtra(MySettings.EXTRA_EJECT_CALL_OPTION_THIRD_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.EJECT_CALL_ENTRY_THIRD:
                intent.setAction(MySettings.ACTION_EJECT_CALL_ENTRY_THIRD_CHANGED);
                intent.putExtra(MySettings.EXTRA_EJECT_CALL_ENTRY_THIRD_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.EQ_CALL_OPTION:
                intent.setAction(MySettings.ACTION_EQ_CALL_OPTION_CHANGED);
                intent.putExtra(MySettings.EXTRA_EQ_CALL_OPTION_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.EQ_CALL_ENTRY:
                intent.setAction(MySettings.ACTION_EQ_CALL_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_EQ_CALL_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.EQ_CALL_OPTION_SECOND:
                intent.setAction(MySettings.ACTION_EQ_CALL_OPTION_SECOND_CHANGED);
                intent.putExtra(MySettings.EXTRA_EQ_CALL_OPTION_SECOND_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.EQ_CALL_ENTRY_SECOND:
                intent.setAction(MySettings.ACTION_EQ_CALL_ENTRY_SECOND_CHANGED);
                intent.putExtra(MySettings.EXTRA_EQ_CALL_ENTRY_SECOND_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.EQ_CALL_OPTION_THIRD:
                intent.setAction(MySettings.ACTION_EQ_CALL_OPTION_THIRD_CHANGED);
                intent.putExtra(MySettings.EXTRA_EQ_CALL_OPTION_THIRD_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.EQ_CALL_ENTRY_THIRD:
                intent.setAction(MySettings.ACTION_EQ_CALL_ENTRY_THIRD_CHANGED);
                intent.putExtra(MySettings.EXTRA_EQ_CALL_ENTRY_THIRD_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.MEDIA_CALL_OPTION:
                intent.setAction(MySettings.ACTION_MEDIA_CALL_OPTION_CHANGED);
                intent.putExtra(MySettings.EXTRA_MEDIA_CALL_OPTION_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.MEDIA_CALL_ENTRY:
                intent.setAction(MySettings.ACTION_MEDIA_CALL_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_MEDIA_CALL_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.MEDIA_CALL_OPTION_SECOND:
                intent.setAction(MySettings.ACTION_MEDIA_CALL_OPTION_SECOND_CHANGED);
                intent.putExtra(MySettings.EXTRA_MEDIA_CALL_OPTION_SECOND_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.MEDIA_CALL_ENTRY_SECOND:
                intent.setAction(MySettings.ACTION_MEDIA_CALL_ENTRY_SECOND_CHANGED);
                intent.putExtra(MySettings.EXTRA_MEDIA_CALL_ENTRY_SECOND_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.MEDIA_CALL_OPTION_THIRD:
                intent.setAction(MySettings.ACTION_MEDIA_CALL_OPTION_THIRD_CHANGED);
                intent.putExtra(MySettings.EXTRA_MEDIA_CALL_OPTION_THIRD_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.MEDIA_CALL_ENTRY_THIRD:
                intent.setAction(MySettings.ACTION_MEDIA_CALL_ENTRY_THIRD_CHANGED);
                intent.putExtra(MySettings.EXTRA_MEDIA_CALL_ENTRY_THIRD_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.MODE_SRC_CALL_OPTION:
                intent.setAction(MySettings.ACTION_MODE_SRC_CALL_OPTION_CHANGED);
                intent.putExtra(MySettings.EXTRA_MODE_SRC_CALL_OPTION_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.MODE_SRC_CALL_ENTRY:
                intent.setAction(MySettings.ACTION_MODE_SRC_CALL_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_MODE_SRC_CALL_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.MODE_SRC_CALL_OPTION_SECOND:
                intent.setAction(MySettings.ACTION_MODE_SRC_CALL_OPTION_SECOND_CHANGED);
                intent.putExtra(MySettings.EXTRA_MODE_SRC_CALL_OPTION_SECOND_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.MODE_SRC_CALL_ENTRY_SECOND:
                intent.setAction(MySettings.ACTION_MODE_SRC_CALL_ENTRY_SECOND_CHANGED);
                intent.putExtra(MySettings.EXTRA_MODE_SRC_CALL_ENTRY_SECOND_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.MODE_SRC_CALL_OPTION_THIRD:
                intent.setAction(MySettings.ACTION_MODE_SRC_CALL_OPTION_THIRD_CHANGED);
                intent.putExtra(MySettings.EXTRA_MODE_SRC_CALL_OPTION_THIRD_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.MODE_SRC_CALL_ENTRY_THIRD:
                intent.setAction(MySettings.ACTION_MODE_SRC_CALL_ENTRY_THIRD_CHANGED);
                intent.putExtra(MySettings.EXTRA_MODE_SRC_CALL_ENTRY_THIRD_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.NAVI_CALL_OPTION:
                intent.setAction(MySettings.ACTION_NAVI_CALL_OPTION_CHANGED);
                intent.putExtra(MySettings.EXTRA_NAVI_CALL_OPTION_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.NAVI_CALL_ENTRY:
                intent.setAction(MySettings.ACTION_NAVI_CALL_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_NAVI_CALL_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.NAVI_CALL_OPTION_SECOND:
                intent.setAction(MySettings.ACTION_NAVI_CALL_OPTION_SECOND_CHANGED);
                intent.putExtra(MySettings.EXTRA_NAVI_CALL_OPTION_SECOND_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.NAVI_CALL_ENTRY_SECOND:
                intent.setAction(MySettings.ACTION_NAVI_CALL_ENTRY_SECOND_CHANGED);
                intent.putExtra(MySettings.EXTRA_NAVI_CALL_ENTRY_SECOND_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.NAVI_CALL_OPTION_THIRD:
                intent.setAction(MySettings.ACTION_NAVI_CALL_OPTION_THIRD_CHANGED);
                intent.putExtra(MySettings.EXTRA_NAVI_CALL_OPTION_THIRD_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.NAVI_CALL_ENTRY_THIRD:
                intent.setAction(MySettings.ACTION_NAVI_CALL_ENTRY_THIRD_CHANGED);
                intent.putExtra(MySettings.EXTRA_NAVI_CALL_ENTRY_THIRD_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.ACC_ON_CALL_OPTION:
                intent.setAction(MySettings.ACTION_ACC_ON_CALL_OPTION_CHANGED);
                intent.putExtra(MySettings.EXTRA_ACC_ON_CALL_OPTION_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.ACC_ON_CALL_ENTRY:
                intent.setAction(MySettings.ACTION_ACC_ON_CALL_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_ACC_ON_CALL_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.ACC_OFF_CALL_OPTION:
                intent.setAction(MySettings.ACTION_ACC_OFF_CALL_OPTION_CHANGED);
                intent.putExtra(MySettings.EXTRA_ACC_OFF_CALL_OPTION_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.ACC_OFF_CALL_ENTRY:
                intent.setAction(MySettings.ACTION_ACC_OFF_CALL_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_ACC_OFF_CALL_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.RESUME_CALL_OPTION:
                intent.setAction(MySettings.ACTION_RESUME_CALL_OPTION_CHANGED);
                intent.putExtra(MySettings.EXTRA_RESUME_CALL_OPTION_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.RESUME_CALL_ENTRY:
                intent.setAction(MySettings.ACTION_RESUME_CALL_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_RESUME_CALL_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.BACK_KEY_CAPTURE:
                intent.setAction(MySettings.ACTION_BACK_KEY_CAPTURE_CHANGED);
                intent.putExtra(MySettings.EXTRA_BACK_KEY_CAPTURE_STRING, sharedPreferences.getBoolean(key, true));
                toastText = "BOOLEAN_KEY";
                break;
            case MySettings.BACK_CALL_OPTION:
                intent.setAction(MySettings.ACTION_BACK_CALL_OPTION_CHANGED);
                intent.putExtra(MySettings.EXTRA_BACK_CALL_OPTION_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.BACK_CALL_ENTRY:
                intent.setAction(MySettings.ACTION_BACK_CALL_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_BACK_CALL_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.HOME_KEY_CAPTURE:
                intent.setAction(MySettings.ACTION_HOME_KEY_CAPTURE_CHANGED);
                intent.putExtra(MySettings.EXTRA_HOME_KEY_CAPTURE_STRING, sharedPreferences.getBoolean(key, true));
                toastText = "BOOLEAN_KEY";
                break;
            case MySettings.HOME_CALL_OPTION:
                intent.setAction(MySettings.ACTION_HOME_CALL_OPTION_CHANGED);
                intent.putExtra(MySettings.EXTRA_HOME_CALL_OPTION_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.HOME_CALL_ENTRY:
                intent.setAction(MySettings.ACTION_HOME_CALL_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_HOME_CALL_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.MUTE_KEY_CAPTURE:
                intent.setAction(MySettings.ACTION_MUTE_KEY_CAPTURE_CHANGED);
                intent.putExtra(MySettings.EXTRA_MUTE_KEY_CAPTURE_STRING, sharedPreferences.getBoolean(key, true));
                toastText = "BOOLEAN_KEY";
                break;
            case MySettings.MUTE_CALL_OPTION:
                intent.setAction(MySettings.ACTION_MUTE_CALL_OPTION_CHANGED);
                intent.putExtra(MySettings.EXTRA_MUTE_CALL_OPTION_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.MUTE_CALL_ENTRY:
                intent.setAction(MySettings.ACTION_MUTE_CALL_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_MUTE_CALL_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
            /* The keys from the CANbus apk */
            case MySettings.PREF_DISABLE_AIRHELPER:
                intent.setAction(MySettings.ACTION_PREF_DISABLE_AIRHELPER_CHANGED);
                intent.putExtra(MySettings.EXTRA_PREF_DISABLE_AIRHELPER_ENABLED, sharedPreferences.getBoolean(key, false));
                toastText = "BOOLEAN_KEY";
                zygote_reboot = true;
                break;
            case MySettings.PREF_DISABLE_DOORHELPER:
                intent.setAction(MySettings.ACTION_PREF_DISABLE_DOORHELPER_CHANGED);
                intent.putExtra(MySettings.EXTRA_PREF_DISABLE_DOORHELPER_ENABLED, sharedPreferences.getBoolean(key, false));
                toastText = "BOOLEAN_KEY";
                zygote_reboot = true;
                break;
            case MySettings.PREF_DISABLE_BTPHONETOP:
                intent.setAction(MySettings.ACTION_PREF_DISABLE_BTPHONETOP_CHANGED);
                intent.putExtra(MySettings.EXTRA_PREF_DISABLE_BTPHONETOP_ENABLED, sharedPreferences.getBoolean(key, false));
                toastText = "BOOLEAN_KEY";
                break;
            case MySettings.USE_ROOT_ACCESS:
                intent.setAction(MySettings.ACTION_USE_ROOT_ACCESS_CHANGED);
                intent.putExtra(MySettings.EXTRA_USE_ROOT_ACCESS_ENABLED, sharedPreferences.getBoolean(key, true));
                toastText = "BOOLEAN_KEY";
                break;
            case MySettings.SHOW_CPU_TEMP:
                intent.setAction(MySettings.ACTION_SHOW_CPU_TEMP_CHANGED);
                intent.putExtra(MySettings.EXTRA_SHOW_CPU_TEMP_ENABLED, sharedPreferences.getBoolean(key, false));
                toastText = "BOOLEAN_KEY";
                additionalText = "\nWait up to 1 minute for the update of the time in the status bar";
                //zygote_reboot = true;
                break;
            case MySettings.HIDE_VOLUMEBAR:
                intent.setAction(MySettings.ACTION_HIDE_VOLUMEBAR_CHANGED);
                intent.putExtra(MySettings.EXTRA_HIDE_VOLUMEBAR_ENABLED, sharedPreferences.getBoolean(key, false));
                toastText = "BOOLEAN_KEY";
                break;
            default:
                Log.d(TAG, "Invalid setting encountered");
                break;
       }

        Log.d(TAG, "updated key is " + key);
        if (toastText.equals("BOOLEAN_KEY")) {
            toastText = "You updated boolean key \"" + (String)key + "\" to \"" + String.valueOf(sharedPreferences.getBoolean(key, false)) + "\"";
        } else {
            Log.d(TAG, "updated string is " + sharedPreferences.getString(key, ""));
            toastText = "You updated key \"" + key + "\" to \"" + sharedPreferences.getString(key, "") + "\"";
        }
        if (additionalText != "") {
            toastText = toastText + additionalText;
        }
        Toast mToast = Toast.makeText(mContext, toastText, Toast.LENGTH_LONG);
        mToast.show();

        if (intent.getAction() != null) {
            getActivity().sendBroadcast(intent);
        }

        if (zygote_reboot == true) {
            zygote_reboot = false;
            Utils.rebootZygote(getContext());
        }


    }


}