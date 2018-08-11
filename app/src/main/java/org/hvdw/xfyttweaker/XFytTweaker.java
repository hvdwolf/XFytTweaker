package org.hvdw.xfyttweaker;

import android.util.Log;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
/*import android.content.SharedPreferences;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo; */
import android.content.ComponentName;
import android.content.SharedPreferences;
/*import android.content.SharedPreferences;
import android.preference.Preferences;
import android.preference.PreferenceManager; */
import android.app.AndroidAppHelper;
import android.widget.Toast;
/* shellExec and rootExec methods */
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import android.view.KeyEvent;
/* assets filecopy */
/*import android.content.res.AssetManager;
import java.io.OutputStream;
import java.io.File;
import java.io.FileOutputStream; */

//CPUTemp
import java.io.InputStreamReader;
import java.io.BufferedReader;
import android.widget.TextView;
import android.graphics.Color;

import android.media.AudioManager;  //USB-DAC

// Timer functions for double/triple tap
import java.util.Timer;
import java.util.TimerTask;

import de.robv.android.xposed.XposedBridge;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import de.robv.android.xposed.XposedHelpers;
//import de.robv.android.xposed.XC_MethodReplacement;
//import de.robv.android.xposed.callbacks.XC_LoadPackage;

import com.crossbowffs.remotepreferences.RemotePreferences;
//import org.hvdw.xfyttweaker.MySettings;
import java.io.File;
import java.util.Map;
//import android.os.SystemProperties;
import java.lang.System;
import android.os.Build;

public class XFytTweaker implements IXposedHookZygoteInit, IXposedHookLoadPackage {

    public static final String TAG = "XFytTweaker ";
    public static Context mContext;
    private static PackageManager pm;
    private static XSharedPreferences xsharedprefs;
    private static SharedPreferences sharedprefs;
    //private static SharedPreferences prefs;

    private AudioManager audioManager; //USB-DAC
    private int CurrentVolume; //USB-DAC

    private boolean noKillEnabled;
    private boolean UsbDac;  //USB-DAC
    private boolean noMcuErrors;
    private boolean skip_ch_four;
    private boolean disable_airhelper;
    private boolean disable_doorhelper;
    private boolean disable_btphonetop;
    private boolean use_root_access;
    private boolean show_cpu_temp;
    private boolean display_org_clock = false; // just a helper boolean for show_cpu_temp, not a setting.
    public boolean firstCall = false; // For the "eliminate feedback during the call if you have OK Google anywhere enabled"

    private Integer tap_delay;

    private String band_call_option;
    private String band_call_entry;
    private String band_call_option_second;
    private String band_call_entry_second;
    private String band_call_option_third;
    private String band_call_entry_third;
    private String bt_phone_call_option;
    private String bt_phone_call_entry;
    private String bt_phone_call_option_second;
    private String bt_phone_call_entry_second;
    private String bt_phone_call_option_third;
    private String bt_phone_call_entry_third;
    private String bt_hang_call_option;
    private String bt_hang_call_entry;
    private String dvd_call_option;
    private String dvd_call_entry;
    private String dvd_call_option_second;
    private String dvd_call_entry_second;
    private String dvd_call_option_third;
    private String dvd_call_entry_third;
    private String eject_call_option;
    private String eject_call_entry;
    private String eject_call_option_second;
    private String eject_call_entry_second;
    private String eject_call_option_third;
    private String eject_call_entry_third;
    private String eq_call_option;
    private String eq_call_entry;
    private String eq_call_option_second;
    private String eq_call_entry_second;
    private String eq_call_option_third;
    private String eq_call_entry_third;
    private String media_call_option;
    private String media_call_entry;
    private String media_call_option_second;
    private String media_call_entry_second;
    private String media_call_option_third;
    private String media_call_entry_third;
    private String mode_src_call_option;
    private String mode_src_call_entry;
    private String mode_src_call_option_second;
    private String mode_src_call_entry_second;
    private String mode_src_call_option_third;
    private String mode_src_call_entry_third;
    private String navi_call_option;
    private String navi_call_entry;
    private String navi_call_option_second;
    private String navi_call_entry_second;
    private String navi_call_option_third;
    private String navi_call_entry_third;

    private String acc_on_call_option;
    private String acc_on_call_entry;
    private String acc_off_call_option;
    private String acc_off_call_entry;
    private String resume_call_option;
    private String resume_call_entry;

    private boolean home_key_capture_enabled;
    private String home_call_option;
    private String home_call_entry;
    private boolean mute_key_capture_enabled;
    private String mute_call_option;
    private String mute_call_entry;

    private String test_call_option;
    private String test_entry;

    private static int count3 = 0;
    //private static int delay3 = 300;

    /*public static boolean isDebugMode() {
        // Hard-coded flag check
        //if (Common.getInstance().DEBUG) {
        //    return true;
        //}

        // Load XSharedPreferences before we start checking if debug mode is enabled from user.
        if (xsharedprefs == null) {
            refreshSharedPreferences(false);
        }

        // Attempt to check if debug mode is toggled by the user
        // Use RemotePreference first as this will be have the most recent (and stable) data.
        // Use XSharedPreference instance if RemotePreference instance doesn't exist yet.
        if(sharedprefs != null) {
            return sharedprefs.getBoolean(PreferencesSettings.KEYS.MAIN.DEBUG, PreferencesSettings.DEFAULT_VALUES.MAIN.DEBUG);
        } else {
            return xsharedprefs.getBoolean(PreferencesSettings.KEYS.MAIN.DEBUG, PreferencesSettings.DEFAULT_VALUES.MAIN.DEBUG);
        }
    } */

    public static void log(String message) {
        XposedBridge.log("[" + TAG + "] " + message);
    }


    public static void makePrefsReadable() {
        // Set preferences file permissions to be world readable
        File prefsFile = new File("/data/data/org.hvdw.xfyttweaker/shared_prefs/org.hvdw.xfyttweaker_preferences.xml");
        if (prefsFile.exists()) {
            log("MakePrefsReadable: found org.hvdw.xfyttweaker prefsFile and set to readable for all");
            prefsFile.setReadable(true, false);
        } else {
            log("MakePrefsReadable: Can find or can't access the org.hvdw.xfyttweaker prefsFile");
        }
    }

    public static void refreshSharedPreferences(boolean displayLogs) {
        log("inside public static void refreshSharedPreferences");
        //makePrefsReadable();

        xsharedprefs = new XSharedPreferences(MySettings.SHARED_PREFS_FILENAME);
        //xsharedprefs.makeWorldReadable();
        //xsharedprefs.reload();

        // Only continue if we want to produce logging
        //if(!displayLogs) {
        //    return;
        //}

        // Logging the properties to see if the file is actually readable
        //XposedBridge.log(TAG + "Shared Preferences Properties:");
        //XposedBridge.log(TAG + "World Readable: " + xsharedprefs.makeWorldReadable());
        //XposedBridge.log(TAG + "Path: " + xsharedprefs.getFile().getAbsolutePath());
        //XposedBridge.log(TAG + "File Readable: " + xsharedprefs.getFile().canRead());
        //XposedBridge.log(TAG + "Exists: " + xsharedprefs.getFile().exists());

        if (xsharedprefs.getAll().size() == 0) {
            log("Shared Preferences seems not to be initialized or does not have read permissions. Common on ROMs with SELinux enforcing.");
            //log("Trying to load Shared Preferences Defaults Instead.");
        } else {
            log("");
            log("Loading Shared Preferences:");
            Map<String, ?> prefsMap = sharedprefs.getAll();
            for(String key: prefsMap.keySet()) {
                String val = prefsMap.get(key).toString();
                log("\t " + key + ": " + val);
            }
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        log("inside public void initZygote");
        makePrefsReadable();        
        //refreshSharedPreferences(true);
        //Do not try to read the RemotePreferences here. Not available yet in Initzygote
        //GlobalVars GlobalVars = GlobalVars.getInstance();


        // check our assets file and copy to /sdcard/XFytTweaker if necessary
        /*Log.d(TAG, "copying navi_app.txt");
        UtilsActivity.CheckCopyAssetFile(mContext, "navi_app.txt");
        Log.d(TAG, "copying player_app.txt");
        UtilsActivity.CheckCopyAssetFile(mContext, "player_app.txt"); */

    }


/*    public void ReadSettings(Context mcontext) {
        //makePrefsReadable();  
        //Context mcontext = MyApplication.getAppContext();
        sharedprefs = new RemotePreferences(mcontext, "org.hvdw.xfyttweaker.preferences.provider", MySettings.SHARED_PREFS_FILENAME);
        //log("Inside ReadSettings trying to read the preferences via the RemotePreferences");
        //sharedprefs = new RemotePreferences(ctx, "org.hvdw.xfyttweaker.preferences.provider", "preferences");
        //sharedprefs = new RemotePreferences((Context) param.thisObject, "org.hvdw.xfyttweaker.preferences.provider", "preferences");

        noKillEnabled = sharedprefs.getBoolean(MySettings.PREF_NO_KILL, true);
        //GlobalVars.setInstance().noKillEnabled(sharedprefs.getBoolean(MySettings.PREF_NO_KILL, true));
        log("ReadSettings noKillEnabled: " + Boolean.toString(noKillEnabled));
        UsbDac = sharedprefs.getBoolean(MySettings.PREF_UsbDac, false); //USB-DAC
        noMcuErrors = sharedprefs.getBoolean(MySettings.PREF_NO_MCU_ERRORS, false);
        skip_ch_four = sharedprefs.getBoolean(MySettings.PREF_SKIP_CH_FOUR, false);
        disable_airhelper = sharedprefs.getBoolean(MySettings.PREF_DISABLE_AIRHELPER, false);
        disable_doorhelper = sharedprefs.getBoolean(MySettings.PREF_DISABLE_DOORHELPER, false);
        disable_btphonetop = sharedprefs.getBoolean(MySettings.PREF_DISABLE_BTPHONETOP, false);
        use_root_access = sharedprefs.getBoolean(MySettings.USE_ROOT_ACCESS, true);
        show_cpu_temp = sharedprefs.getBoolean(MySettings.SHOW_CPU_TEMP, false);
        log("ReadSettings show_cpu_temp: " + Boolean.toString(show_cpu_temp));
        tap_delay = Integer.parseInt(sharedprefs.getString(MySettings.PREF_TAP_DELAY, "300"));

        band_call_option = sharedprefs.getString(MySettings.BAND_CALL_OPTION, "");
        log("ReadSettings band_call_option: " + band_call_option);
        band_call_entry = sharedprefs.getString(MySettings.BAND_CALL_ENTRY, "");
        log("ReadSettings band_call_entry: " + band_call_entry);
        band_call_option_second = sharedprefs.getString(MySettings.BAND_CALL_OPTION_SECOND, "");
        band_call_entry_second = sharedprefs.getString(MySettings.BAND_CALL_ENTRY_SECOND, "");
        band_call_option_third = sharedprefs.getString(MySettings.BAND_CALL_OPTION_THIRD, "");
        band_call_entry_third = sharedprefs.getString(MySettings.BAND_CALL_ENTRY_THIRD, "");
        bt_phone_call_option = sharedprefs.getString(MySettings.BT_PHONE_CALL_OPTION, "");
        bt_phone_call_entry = sharedprefs.getString(MySettings.BT_PHONE_CALL_ENTRY, "");
        bt_phone_call_option_second = sharedprefs.getString(MySettings.BT_PHONE_CALL_OPTION_SECOND, "");
        bt_phone_call_entry_second = sharedprefs.getString(MySettings.BT_PHONE_CALL_ENTRY_SECOND, "");
        bt_phone_call_option_third = sharedprefs.getString(MySettings.BT_PHONE_CALL_OPTION_THIRD, "");
        bt_phone_call_entry_third = sharedprefs.getString(MySettings.BT_PHONE_CALL_ENTRY_THIRD, "");
        bt_hang_call_option = sharedprefs.getString(MySettings.BT_HANG_CALL_OPTION, "");
        bt_hang_call_entry = sharedprefs.getString(MySettings.BT_HANG_CALL_ENTRY, "");
        dvd_call_option = sharedprefs.getString(MySettings.DVD_CALL_OPTION, "");
        dvd_call_entry = sharedprefs.getString(MySettings.DVD_CALL_ENTRY, "");
        dvd_call_option_second = sharedprefs.getString(MySettings.DVD_CALL_OPTION_SECOND, "");
        dvd_call_entry_second = sharedprefs.getString(MySettings.DVD_CALL_ENTRY_SECOND, "");
        dvd_call_option_third = sharedprefs.getString(MySettings.DVD_CALL_OPTION_THIRD, "");
        dvd_call_entry_third = sharedprefs.getString(MySettings.DVD_CALL_ENTRY_THIRD, "");
        eject_call_option = sharedprefs.getString(MySettings.EJECT_CALL_OPTION, "");
        eject_call_entry = sharedprefs.getString(MySettings.EJECT_CALL_ENTRY, "");
        eject_call_option_second = sharedprefs.getString(MySettings.EJECT_CALL_OPTION_SECOND, "");
        eject_call_entry_second = sharedprefs.getString(MySettings.EJECT_CALL_ENTRY_SECOND, "");
        eject_call_option_third = sharedprefs.getString(MySettings.EJECT_CALL_OPTION_THIRD, "");
        eject_call_entry_third = sharedprefs.getString(MySettings.EJECT_CALL_ENTRY_THIRD, "");
        eq_call_option = sharedprefs.getString(MySettings.EQ_CALL_OPTION, "");
        eq_call_entry = sharedprefs.getString(MySettings.EQ_CALL_ENTRY, "");
        eq_call_option_second = sharedprefs.getString(MySettings.EQ_CALL_OPTION_SECOND, "");
        eq_call_entry_second = sharedprefs.getString(MySettings.EQ_CALL_ENTRY_SECOND, "");
        eq_call_option_third = sharedprefs.getString(MySettings.EQ_CALL_OPTION_THIRD, "");
        eq_call_entry_third = sharedprefs.getString(MySettings.EQ_CALL_ENTRY_THIRD, "");
        media_call_option = sharedprefs.getString(MySettings.MEDIA_CALL_OPTION, "");
        media_call_entry = sharedprefs.getString(MySettings.MEDIA_CALL_ENTRY, "");
        media_call_option_second = sharedprefs.getString(MySettings.MEDIA_CALL_OPTION_SECOND, "");
        media_call_entry_second = sharedprefs.getString(MySettings.MEDIA_CALL_ENTRY_SECOND, "");
        media_call_option_third = sharedprefs.getString(MySettings.MEDIA_CALL_OPTION_THIRD, "");
        media_call_entry_third = sharedprefs.getString(MySettings.MEDIA_CALL_ENTRY_THIRD, "");
        mode_src_call_option = sharedprefs.getString(MySettings.MODE_SRC_CALL_OPTION, "");
        mode_src_call_entry = sharedprefs.getString(MySettings.MODE_SRC_CALL_ENTRY, "");
        mode_src_call_option_second = sharedprefs.getString(MySettings.MODE_SRC_CALL_OPTION_SECOND, "");
        mode_src_call_entry_second = sharedprefs.getString(MySettings.MODE_SRC_CALL_ENTRY_SECOND, "");
        mode_src_call_option_second = sharedprefs.getString(MySettings.MODE_SRC_CALL_OPTION_THIRD, "");
        mode_src_call_entry_second = sharedprefs.getString(MySettings.MODE_SRC_CALL_ENTRY_THIRD, "");
        navi_call_option = sharedprefs.getString(MySettings.NAVI_CALL_OPTION, "");
        navi_call_entry = sharedprefs.getString(MySettings.NAVI_CALL_ENTRY, "");
        navi_call_option_second = sharedprefs.getString(MySettings.NAVI_CALL_OPTION_SECOND, "");
        navi_call_entry_second = sharedprefs.getString(MySettings.NAVI_CALL_ENTRY_SECOND, "");
        navi_call_option_third = sharedprefs.getString(MySettings.NAVI_CALL_OPTION_THIRD, "");
        navi_call_entry_third = sharedprefs.getString(MySettings.NAVI_CALL_ENTRY_THIRD, "");

        acc_on_call_option = sharedprefs.getString(MySettings.ACC_ON_CALL_OPTION, "");
        acc_on_call_entry = sharedprefs.getString(MySettings.ACC_ON_CALL_ENTRY, "");
        acc_off_call_option = sharedprefs.getString(MySettings.ACC_OFF_CALL_OPTION, "");
        acc_off_call_entry = sharedprefs.getString(MySettings.ACC_OFF_CALL_ENTRY, "");
        resume_call_option = sharedprefs.getString(MySettings.RESUME_CALL_OPTION, "");
        resume_call_entry = sharedprefs.getString(MySettings.RESUME_CALL_ENTRY, "");

        home_key_capture_enabled = sharedprefs.getBoolean(MySettings.HOME_KEY_CAPTURE, true);
        home_call_option = sharedprefs.getString(MySettings.HOME_CALL_OPTION, "");
        home_call_entry = sharedprefs.getString(MySettings.HOME_CALL_ENTRY, "");
        mute_key_capture_enabled = sharedprefs.getBoolean(MySettings.MUTE_KEY_CAPTURE, true);
        mute_call_option = sharedprefs.getString(MySettings.MUTE_CALL_OPTION, "");
        mute_call_entry = sharedprefs.getString(MySettings.MUTE_CALL_ENTRY, "");
    }
*/

    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
       log("handleLoadPackage: Loaded app: " + lpparam.packageName);

        // Hook the Application class of this application. This allows us
        // to have some application Context and use it to update the RemotePreferences Content Provider
        // and the other information. This allows us to read SharedPreference from the app process
        // without tripping SELinux and Android 8 restrictions
        findAndHookMethod("android.app.Application", lpparam.classLoader, "onCreate", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            }
        });

      if (lpparam.packageName.equals("com.syu.ms")) {

/**********************************************************************************************************************************************/

        // Hook for the "eliminate feedback during the call if you have OK Google anywhere enabled"
        Class<?> ProfileInfoClass = XposedHelpers.findClass("module.bt.HandlerBt", lpparam.classLoader);
            XposedBridge.hookAllMethods(ProfileInfoClass, "btPhoneState", new XC_MethodHook() {
            public void beforeHookedMethod(MethodHookParam param) throws Throwable {
                int phonestate = (int) param.args[0];
                //5 is in call
                //4 is ringing?
                //3 is dialing
                //2 is idle?
                //1 is ?
                //0 is n/c?

                //XposedBridge.log("BTTEST " + "phonestate:" + phonestate);
                if (phonestate == 3 || phonestate == 4) {
                    firstCall = true;
                    sudoVoiceKill();
                }
                if ((phonestate == 2) && (firstCall == true)) {
                    //XposedBridge.log("BTTEST CALL WAS HUNG UP");
                    firstCall = false;
                    sudoVoiceRestart();
                }
                }
            });
        // End of the hook for the "eliminate feedback during the call if you have OK Google anywhere enabled"

        /* Correct the mediaKey function in app/ToolkitApp.java
        *  Gustdens modified code only sends media keys to the active media player
        *  The original code uses an intent and "every" media player listening will react. */
        findAndHookMethod("app.ToolkitApp", lpparam.classLoader, "mediaKey", int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                int onKey1 = (int) param.args[0];
                Context context = (Context) AndroidAppHelper.currentApplication();
                AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

                if (onKey1 == KeyEvent.KEYCODE_MEDIA_PREVIOUS) {
                    // same as onKey1 == 88
                    Log.d(TAG, "PREV");
                    KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PREVIOUS);
                    mAudioManager.dispatchMediaKeyEvent(event);
                    KeyEvent event2 = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PREVIOUS);
                    mAudioManager.dispatchMediaKeyEvent(event2);
                }

                if (onKey1 == KeyEvent.KEYCODE_MEDIA_NEXT) {
                    //same as onKey1 == 87
                    Log.d(TAG, "Next");
                    KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_NEXT);
                    mAudioManager.dispatchMediaKeyEvent(event);
                    KeyEvent event2 = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_NEXT);
                    mAudioManager.dispatchMediaKeyEvent(event2);
                }

                if (onKey1 == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
                    // same as onKey1 == 85
                    Log.d(TAG, "play/pause");
                    KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
                    mAudioManager.dispatchMediaKeyEvent(event);
                    KeyEvent event2 = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
                    mAudioManager.dispatchMediaKeyEvent(event2);
                }

                param.setResult(null);
            }
        });
        // End of the hook to correct the media keys.

        /* This is the No Kill function */
//        if (noKillEnabled == true) {
        findAndHookMethod("app.ToolkitApp", lpparam.classLoader, "killAppWhenSleep", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                Context mcontext = (Context) AndroidAppHelper.currentApplication();
                sharedprefs = new RemotePreferences(mcontext, "org.hvdw.xfyttweaker.preferences.provider", MySettings.SHARED_PREFS_FILENAME);
                noKillEnabled = sharedprefs.getBoolean(MySettings.PREF_NO_KILL, true);
                if (noKillEnabled == true) {
                    String Strnke = String.valueOf(noKillEnabled);
                    log("value of noKillEnabled is: " + Strnke);
                    param.setResult(null);
                }
            }
        });
//        } else {
//            XposedBridge.log(TAG + " nokill disabled");
//        }

        //USB-DAC-START
//        if (UsbDac == true) {
        findAndHookMethod("module.main.HandlerMain", lpparam.classLoader, "mcuKeyVolUp", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                Context mcontext = (Context) AndroidAppHelper.currentApplication();
                sharedprefs = new RemotePreferences(mcontext, "org.hvdw.xfyttweaker.preferences.provider", MySettings.SHARED_PREFS_FILENAME);
                UsbDac = sharedprefs.getBoolean(MySettings.PREF_UsbDac, true);
                if (UsbDac == true) {
                    AudioManager audioManager = (AudioManager)mcontext.getSystemService(Context.AUDIO_SERVICE);
                    CurrentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    audioManager.setStreamVolume(3, CurrentVolume + 1, 1);
                    int MaxVolume3 = audioManager.getStreamMaxVolume(3);
                    int MaxVolume5 = audioManager.getStreamMaxVolume(5);
                    audioManager.setStreamVolume(5, (int) Math.ceil((((double) MaxVolume5) / ((double) MaxVolume3)) * ((double) CurrentVolume)) , 0);
                    //log("USBDac VolUp;
                   param.setResult(null);
                }
            }
        });

        findAndHookMethod("module.main.HandlerMain", lpparam.classLoader, "mcuKeyVolDown", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                Context mcontext = (Context) AndroidAppHelper.currentApplication();
                sharedprefs = new RemotePreferences(mcontext, "org.hvdw.xfyttweaker.preferences.provider", MySettings.SHARED_PREFS_FILENAME);
                UsbDac = sharedprefs.getBoolean(MySettings.PREF_UsbDac, true);
                if (UsbDac == true) {
                    AudioManager audioManager = (AudioManager)mcontext.getSystemService(Context.AUDIO_SERVICE);
                    CurrentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    audioManager.setStreamVolume(3, CurrentVolume + -1, 1);
                    int MaxVolume3 = audioManager.getStreamMaxVolume(3);
                    int MaxVolume5 = audioManager.getStreamMaxVolume(5);
                    audioManager.setStreamVolume(5, (int) Math.ceil((((double) MaxVolume5) / ((double) MaxVolume3)) * ((double) CurrentVolume)) , 0);
                    //log("USBDac VolDown");
                    param.setResult(null);
                }
            }
        });

        findAndHookMethod("module.main.HandlerMain", lpparam.classLoader, "mcuKeyVolMute", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                Context mcontext = (Context) AndroidAppHelper.currentApplication();
                sharedprefs = new RemotePreferences(mcontext, "org.hvdw.xfyttweaker.preferences.provider", MySettings.SHARED_PREFS_FILENAME);
                UsbDac = sharedprefs.getBoolean(MySettings.PREF_UsbDac, true);
                if (UsbDac == true) {
                    AudioManager audioManager = (AudioManager)mcontext.getSystemService(Context.AUDIO_SERVICE);
                    audioManager.adjustStreamVolume(3, 101, 1);
                    audioManager.adjustStreamVolume(0, 101, 0);
                    audioManager.adjustStreamVolume(1, 101, 0);
                    audioManager.adjustStreamVolume(2, 101, 0);
                    audioManager.adjustStreamVolume(4, 101, 0);
                    audioManager.adjustStreamVolume(5, 101, 0);
                    audioManager.adjustStreamVolume(8, 101, 0);
                    //log("USBDac Mute");
                    param.setResult(null);
                }
            }
        });

        findAndHookMethod("app.ToolkitApp", lpparam.classLoader, "setStreamVol", int.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                Context mcontext = (Context) AndroidAppHelper.currentApplication();
                sharedprefs = new RemotePreferences(mcontext, "org.hvdw.xfyttweaker.preferences.provider", MySettings.SHARED_PREFS_FILENAME);
                UsbDac = sharedprefs.getBoolean(MySettings.PREF_UsbDac, true);
                if (UsbDac == true) {
                    //log("USBDac Stop setStreamVol");
                    param.setResult(null);
                }
            }
        });
//        }
        //USB-DAC-END

        findAndHookMethod("ui.InfoView", lpparam.classLoader, "addSelfToWindow", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                Context mcontext = (Context) AndroidAppHelper.currentApplication();
                sharedprefs = new RemotePreferences(mcontext, "org.hvdw.xfyttweaker.preferences.provider", MySettings.SHARED_PREFS_FILENAME);
                noMcuErrors = sharedprefs.getBoolean(MySettings.PREF_NO_MCU_ERRORS, true);
                if (noMcuErrors == true) {
                    log("McuErrors enabled; do not display McuErrors");
                    param.setResult(null);
                } else {
                    log("McuErrors disabled; mcuerrors displayed if relevant");
                }
            }
        });


        /* This should prevent the mute of audio channel 4 (alarm) which is used by Google voice for voice feedback 
        *  This seems like a must-do switch on setting, but when no other channel is used it will give noise, although 
        *  you won't hear that with the engine on */
//        if (skip_ch_four == true && UsbDac == false) { //USB-DAC find and replace if (skip_ch_four == true)
        findAndHookMethod("app.ToolkitApp", lpparam.classLoader, "setStreamVol", int.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                int stream = (int) param.args[0];
                if (stream == 4) {
                    Context mcontext = (Context) AndroidAppHelper.currentApplication();
                    sharedprefs = new RemotePreferences(mcontext, "org.hvdw.xfyttweaker.preferences.provider", MySettings.SHARED_PREFS_FILENAME);
                    skip_ch_four = sharedprefs.getBoolean(MySettings.PREF_SKIP_CH_FOUR, false);
                    UsbDac = sharedprefs.getBoolean(MySettings.PREF_UsbDac, true);
                    if (skip_ch_four == true && UsbDac == false) { //USB-DAC find and replace if (skip_ch_four == true)
                        log(" skipping alarm channel 4 mute");
                        param.setResult(null);
                    }
                }
            }
        });
//        }

//        if (disable_btphonetop == true) {
            //Class<?> ProfileInfoClass = XposedHelpers.findClass("module.bt.HandlerBt$3", lpparam.classLoader);
            //No lo nger necessary to declare as it is declared on top in the OK Google function
        ProfileInfoClass = XposedHelpers.findClass("module.bt.HandlerBt$3", lpparam.classLoader);
           XposedBridge.hookAllMethods(ProfileInfoClass, "topChanged", new XC_MethodHook() {
               @Override
               protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Context mcontext = (Context) AndroidAppHelper.currentApplication();
                    sharedprefs = new RemotePreferences(mcontext, "org.hvdw.xfyttweaker.preferences.provider", MySettings.SHARED_PREFS_FILENAME);
                    disable_btphonetop = sharedprefs.getBoolean(MySettings.PREF_DISABLE_BTPHONETOP, false);
                    if (disable_btphonetop == true) {
                       log("Prevent bt phone app from forcing always on top during call");
                       param.setResult(null);
                   }
               }
        });
//        }

/**********************************************************************************************************************************************/
        /* Below are the captured key functions */
        findAndHookMethod("app.HandlerApp", lpparam.classLoader, "wakeup", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                Context mcontext = (Context) AndroidAppHelper.currentApplication();
                sharedprefs = new RemotePreferences(mcontext, "org.hvdw.xfyttweaker.preferences.provider", MySettings.SHARED_PREFS_FILENAME);
                resume_call_option = sharedprefs.getString(MySettings.RESUME_CALL_OPTION, "");
                resume_call_entry = sharedprefs.getString(MySettings.RESUME_CALL_ENTRY, "");
                log(" Execute the RESUME action using specific call method");
            }
        });


//        if ((bt_phone_call_option != "") && (bt_phone_call_entry != "")) {
        findAndHookMethod("util.JumpPage", lpparam.classLoader, "btPageDialByKey", new XC_MethodHook() { 
            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                Context mcontext = (Context) AndroidAppHelper.currentApplication();
                sharedprefs = new RemotePreferences(mcontext, "org.hvdw.xfyttweaker.preferences.provider", MySettings.SHARED_PREFS_FILENAME);
                bt_phone_call_option = sharedprefs.getString(MySettings.BT_PHONE_CALL_OPTION, "");
                bt_phone_call_entry = sharedprefs.getString(MySettings.BT_PHONE_CALL_ENTRY, "");
                bt_phone_call_option_second = sharedprefs.getString(MySettings.BT_PHONE_CALL_OPTION_SECOND, "");
                bt_phone_call_entry_second = sharedprefs.getString(MySettings.BT_PHONE_CALL_ENTRY_SECOND, "");
                bt_phone_call_option_third = sharedprefs.getString(MySettings.BT_PHONE_CALL_OPTION_THIRD, "");
                bt_phone_call_entry_third = sharedprefs.getString(MySettings.BT_PHONE_CALL_ENTRY_THIRD, "");
                tap_delay = Integer.parseInt(sharedprefs.getString(MySettings.PREF_TAP_DELAY, "300"));
                if ((bt_phone_call_option != "") && (bt_phone_call_entry != "")) {
                log("mcuKeyBtPhone pressed; bt_phone_call_option: " + bt_phone_call_option + " bt_phone_call_entry : " + bt_phone_call_entry);
                    /* whichActionToPerform(context, bt_phone_call_option, bt_phone_call_entry); */
                    multitap(bt_phone_call_option, bt_phone_call_entry, bt_phone_call_option_second, bt_phone_call_entry_second, bt_phone_call_option_third, bt_phone_call_entry_third, tap_delay);

                    param.setResult(null);
                }
            }
        });
//        }

//        if ((navi_call_option != "") && (navi_call_entry != "")) {
        findAndHookMethod("module.main.HandlerMain", lpparam.classLoader, "mcuKeyNavi", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                Context mcontext = (Context) AndroidAppHelper.currentApplication();
                sharedprefs = new RemotePreferences(mcontext, "org.hvdw.xfyttweaker.preferences.provider", MySettings.SHARED_PREFS_FILENAME);
                navi_call_option = sharedprefs.getString(MySettings.NAVI_CALL_OPTION, "");
                navi_call_entry = sharedprefs.getString(MySettings.NAVI_CALL_ENTRY, "");
                navi_call_option_second = sharedprefs.getString(MySettings.NAVI_CALL_OPTION_SECOND, "");
                navi_call_entry_second = sharedprefs.getString(MySettings.NAVI_CALL_ENTRY_SECOND, "");
                navi_call_option_third = sharedprefs.getString(MySettings.NAVI_CALL_OPTION_THIRD, "");
                navi_call_entry_third = sharedprefs.getString(MySettings.NAVI_CALL_ENTRY_THIRD, "");
                tap_delay = Integer.parseInt(sharedprefs.getString(MySettings.PREF_TAP_DELAY, "300"));
                if ((navi_call_option != "") && (navi_call_entry != "")) {
                    log("mcuKeyNavi  pressed; navi_call_option: " + navi_call_option + " navi_call_entry : " + navi_call_entry);
                    /* whichActionToPerform(context, navi_call_option, navi_call_entry); */
                    multitap(navi_call_option, navi_call_entry, navi_call_option_second, navi_call_entry_second, navi_call_option_third, navi_call_entry_third, tap_delay);

                    param.setResult(null);
                }
            }
        });
//        }

//        if ((band_call_option != "") && (band_call_entry != "")) {
        // band button = radio
        findAndHookMethod("module.main.HandlerMain", lpparam.classLoader, "mcuKeyBand", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                Context mcontext = (Context) AndroidAppHelper.currentApplication();
                sharedprefs = new RemotePreferences(mcontext, "org.hvdw.xfyttweaker.preferences.provider", MySettings.SHARED_PREFS_FILENAME);
                band_call_option = sharedprefs.getString(MySettings.BAND_CALL_OPTION, "");
                //log("mcuKeyBand band_call_option: " + band_call_option);
                band_call_entry = sharedprefs.getString(MySettings.BAND_CALL_ENTRY, "");
                //log("mcuKeyBand band_call_entry: " + band_call_entry);
                band_call_option_second = sharedprefs.getString(MySettings.BAND_CALL_OPTION_SECOND, "");
                //log("mcuKeyBand band_call_option_second: " + band_call_option_second);
                band_call_entry_second = sharedprefs.getString(MySettings.BAND_CALL_ENTRY_SECOND, "");
                //log("mcuKeyBand band_call_entry_second: " + band_call_entry_second);
                band_call_option_third = sharedprefs.getString(MySettings.BAND_CALL_OPTION_THIRD, "");
                //log("mcuKeyBand band_call_option_third: " + band_call_option_third);
                band_call_entry_third = sharedprefs.getString(MySettings.BAND_CALL_ENTRY_THIRD, "");
                //log("mcuKeyBand band_call_entry_third: " + band_call_entry_third);
                tap_delay = Integer.parseInt(sharedprefs.getString(MySettings.PREF_TAP_DELAY, "300"));
                //log("mcuKeyBand tap_delay: " + tap_delay);
                log("mcuKeyBand (Radio) pressed; forward action to specific call method");
                log("mcuKeyBand band_call_option: " + band_call_option + " band_call_entry : " + band_call_entry);
                /* whichActionToPerform(context, band_call_option, band_call_entry); */
                multitap(band_call_option, band_call_entry, band_call_option_second, band_call_entry_second, band_call_option_third, band_call_entry_third, tap_delay);

                param.setResult(null);
            }
        });
//        }

//        if ((mode_src_call_option != "") && (mode_src_call_entry != "")) {
        findAndHookMethod("module.main.HandlerMain", lpparam.classLoader, "mcuKeyMode", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                Context mcontext = (Context) AndroidAppHelper.currentApplication();
                sharedprefs = new RemotePreferences(mcontext, "org.hvdw.xfyttweaker.preferences.provider", MySettings.SHARED_PREFS_FILENAME);
                mode_src_call_option = sharedprefs.getString(MySettings.MODE_SRC_CALL_OPTION, "");
                mode_src_call_entry = sharedprefs.getString(MySettings.MODE_SRC_CALL_ENTRY, "");
                mode_src_call_option_second = sharedprefs.getString(MySettings.MODE_SRC_CALL_OPTION_SECOND, "");
                mode_src_call_entry_second = sharedprefs.getString(MySettings.MODE_SRC_CALL_ENTRY_SECOND, "");
                mode_src_call_option_third = sharedprefs.getString(MySettings.MODE_SRC_CALL_OPTION_THIRD, "");
                mode_src_call_entry_third = sharedprefs.getString(MySettings.MODE_SRC_CALL_ENTRY_THIRD, "");
                tap_delay = Integer.parseInt(sharedprefs.getString(MySettings.PREF_TAP_DELAY, "300"));
                if ((mode_src_call_option != "") && (mode_src_call_entry != "")) {
                    log(" Source/Mode pressed; forward action  to specific call method");
                    /*whichActionToPerform(context, mode_src_call_option, mode_src_call_entry); */
                    multitap(mode_src_call_option, mode_src_call_entry, mode_src_call_option_second, mode_src_call_entry_second, mode_src_call_option_third, mode_src_call_entry_third, tap_delay);

                    param.setResult(null);
                }
            }
        });
//        }

//        if ((media_call_option != "") && (media_call_entry != "")) {
        findAndHookMethod("module.main.HandlerMain", lpparam.classLoader, "mcuKeyPlayer", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                Context mcontext = (Context) AndroidAppHelper.currentApplication();
                sharedprefs = new RemotePreferences(mcontext, "org.hvdw.xfyttweaker.preferences.provider", MySettings.SHARED_PREFS_FILENAME);
                media_call_option = sharedprefs.getString(MySettings.MEDIA_CALL_OPTION, "");
                media_call_entry = sharedprefs.getString(MySettings.MEDIA_CALL_ENTRY, "");
                media_call_option_second = sharedprefs.getString(MySettings.MEDIA_CALL_OPTION_SECOND, "");
                media_call_entry_second = sharedprefs.getString(MySettings.MEDIA_CALL_ENTRY_SECOND, "");
                media_call_option_third = sharedprefs.getString(MySettings.MEDIA_CALL_OPTION_THIRD, "");
                media_call_entry_third = sharedprefs.getString(MySettings.MEDIA_CALL_ENTRY_THIRD, "");
                tap_delay = Integer.parseInt(sharedprefs.getString(MySettings.PREF_TAP_DELAY, "300"));
                if ((media_call_option != "") && (media_call_entry != "")) {
                    log("MEDIA button pressed; forward action to specific call method");
                    /*whichActionToPerform(context, media_call_option, media_call_entry); */
                    multitap(media_call_option, media_call_entry, media_call_option_second, media_call_entry_second, media_call_option_third, media_call_entry_third, tap_delay);

                    param.setResult(null);
                }
            }
        });
//        }

//        if ((eq_call_option != "") && (eq_call_entry != "")) {
//            findAndHookMethod("util.JumpPage", lpparam.classLoader, "eq", new XC_MethodHook() {
        findAndHookMethod("module.main.HandlerMain", lpparam.classLoader, "mcuKeyAudio", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                Context mcontext = (Context) AndroidAppHelper.currentApplication();
                sharedprefs = new RemotePreferences(mcontext, "org.hvdw.xfyttweaker.preferences.provider", MySettings.SHARED_PREFS_FILENAME);
                eq_call_option = sharedprefs.getString(MySettings.EQ_CALL_OPTION, "");
                eq_call_entry = sharedprefs.getString(MySettings.EQ_CALL_ENTRY, "");
                eq_call_option_second = sharedprefs.getString(MySettings.EQ_CALL_OPTION_SECOND, "");
                eq_call_entry_second = sharedprefs.getString(MySettings.EQ_CALL_ENTRY_SECOND, "");
                eq_call_option_third = sharedprefs.getString(MySettings.EQ_CALL_OPTION_THIRD, "");
                eq_call_entry_third = sharedprefs.getString(MySettings.EQ_CALL_ENTRY_THIRD, "");
                tap_delay = Integer.parseInt(sharedprefs.getString(MySettings.PREF_TAP_DELAY, "300"));
                if ((eq_call_option != "") && (eq_call_entry != "")) {
                    log("EQ button pressed; forward action  to specific call method");
                    //whichActionToPerform(context, eq_call_option, eq_call_entry);
                    multitap(eq_call_option, eq_call_entry, eq_call_option_second, eq_call_entry_second, eq_call_option_third, eq_call_entry_third, tap_delay);

                    param.setResult(null);
                }
            }
        });
//        }

        findAndHookMethod("dev.ReceiverMcu", lpparam.classLoader, "onHandle", byte[].class, int.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                Context mcontext = (Context) AndroidAppHelper.currentApplication();
                //byte[] data  = getByteField(param.thisObject, "byte[].class");
                byte[] data =  (byte[]) param.args[0];
                /* int start = getIntField(param.thisObject, "start");
                int length = getIntField(param.thisObject, "length"); */
                int start = (int) param.args[1];
                int length = (int) param.args[2];
                byte b = data[start];

                sharedprefs = new RemotePreferences(mcontext, "org.hvdw.xfyttweaker.preferences.provider", MySettings.SHARED_PREFS_FILENAME);

                //Log.d(TAG, "DVD or eject button; Executed the Media action to the launcher.sh");
                if ((b & 255) == 1 && (data[start + 1] & 255) == 0 && (data[start + 2] & 255) == 16 && (data[start + 3] & 255) == 80) {
                    dvd_call_option = sharedprefs.getString(MySettings.DVD_CALL_OPTION, "");
                    dvd_call_entry = sharedprefs.getString(MySettings.DVD_CALL_ENTRY, "");
                    dvd_call_option_second = sharedprefs.getString(MySettings.DVD_CALL_OPTION_SECOND, "");
                    dvd_call_entry_second = sharedprefs.getString(MySettings.DVD_CALL_ENTRY_SECOND, "");
                    dvd_call_option_third = sharedprefs.getString(MySettings.DVD_CALL_OPTION_THIRD, "");
                    dvd_call_entry_third = sharedprefs.getString(MySettings.DVD_CALL_ENTRY_THIRD, "");
                    tap_delay = Integer.parseInt(sharedprefs.getString(MySettings.PREF_TAP_DELAY, "300"));
                    log(" DVD button pressed; forward action to specific call method");
                    /*whichActionToPerform(context, dvd_call_option, dvd_call_entry); */
                    multitap(dvd_call_option, dvd_call_entry, dvd_call_option_second, dvd_call_entry_second, dvd_call_option_third, dvd_call_entry_third, tap_delay);
                }
                if ((b & 255) == 1 && (data[start + 1] & 255) == 161 && (data[start + 2] & 255) == 2 && (data[start + 3] & 255) == 91) {
                    eject_call_option = sharedprefs.getString(MySettings.EJECT_CALL_OPTION, "");
                    eject_call_entry = sharedprefs.getString(MySettings.EJECT_CALL_ENTRY, "");
                    eject_call_option_second = sharedprefs.getString(MySettings.EJECT_CALL_OPTION_SECOND, "");
                    eject_call_entry_second = sharedprefs.getString(MySettings.EJECT_CALL_ENTRY_SECOND, "");
                    eject_call_option_third = sharedprefs.getString(MySettings.EJECT_CALL_OPTION_THIRD, "");
                    eject_call_entry_third = sharedprefs.getString(MySettings.EJECT_CALL_ENTRY_THIRD, "");
                    tap_delay = Integer.parseInt(sharedprefs.getString(MySettings.PREF_TAP_DELAY, "300"));
                    log(" EJECT button pressed; forward action to specific call method");
                    /* whichActionToPerform(context, eject_call_option, eject_call_entry); */
                    multitap(eject_call_option, eject_call_entry, eject_call_option_second, eject_call_entry_second, eject_call_option_third, eject_call_entry_third, tap_delay);
                }
                // Yes or No paramresult? old bug?
                //param.setResult(null);
            }
        });

//        if ((bt_hang_call_option != "") && (bt_hang_call_entry != "")) {
        findAndHookMethod("dev.ReceiverMcu", lpparam.classLoader, "onHandleBt", byte[].class, int.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                byte[] data =  (byte[]) param.args[0];
                int start = (int) param.args[1];
                byte b = data[start];
                byte c = data[start + 1];

                if ((b == 16) && (c == 0)) {
                    Context mcontext = (Context) AndroidAppHelper.currentApplication();
                    sharedprefs = new RemotePreferences(mcontext, "org.hvdw.xfyttweaker.preferences.provider", MySettings.SHARED_PREFS_FILENAME);
                    bt_hang_call_option = sharedprefs.getString(MySettings.BT_HANG_CALL_OPTION, "");
                    bt_hang_call_entry = sharedprefs.getString(MySettings.BT_HANG_CALL_ENTRY, "");
                    if ((bt_hang_call_option != "") && (bt_hang_call_entry != "")) {
                        log(" BT_Hang pressed; bt_hang_call_option: " + bt_hang_call_option + " bt_hang_call_entry : " + bt_hang_call_entry);
                        whichActionToPerform(mcontext, bt_hang_call_option, bt_hang_call_entry);

                        param.setResult(null);
                    }
                }
            }
        });
//        }


        findAndHookMethod("util.JumpPage", lpparam.classLoader, "broadcastByIntentName", String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                String actionName = (String) param.args[0];
                Context mcontext = (Context) AndroidAppHelper.currentApplication();
                sharedprefs = new RemotePreferences(mcontext, "org.hvdw.xfyttweaker.preferences.provider", MySettings.SHARED_PREFS_FILENAME);
                log(" broadcastByIntentName in util.JumpPage beforeHooked " + actionName);
                if (actionName == "com.glsx.boot.ACCON") {
                    acc_on_call_option = sharedprefs.getString(MySettings.ACC_ON_CALL_OPTION, "");
                    acc_on_call_entry = sharedprefs.getString(MySettings.ACC_ON_CALL_ENTRY, "");
                    log(" ACC_ON command received");
                    whichActionToPerform(mcontext, acc_on_call_option, acc_on_call_entry);
                }
                if (actionName == "com.glsx.boot.ACCOFF") {
                    acc_off_call_option = sharedprefs.getString(MySettings.ACC_OFF_CALL_OPTION, "");
                    acc_off_call_entry = sharedprefs.getString(MySettings.ACC_OFF_CALL_ENTRY, "");
                    log(TAG + " ACC_OFF command received");
                    whichActionToPerform(mcontext, acc_off_call_option, acc_off_call_entry);
                }
            }
        });

//        if (home_key_capture_enabled == true) {
        findAndHookMethod("app.ToolkitApp", lpparam.classLoader, "keyHome", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                Context mcontext = (Context) AndroidAppHelper.currentApplication();
                sharedprefs = new RemotePreferences(mcontext, "org.hvdw.xfyttweaker.preferences.provider", MySettings.SHARED_PREFS_FILENAME);
                home_key_capture_enabled = sharedprefs.getBoolean(MySettings.HOME_KEY_CAPTURE, false);
                home_call_option = sharedprefs.getString(MySettings.HOME_CALL_OPTION, "");
                home_call_entry = sharedprefs.getString(MySettings.HOME_CALL_ENTRY, "");
                if (home_key_capture_enabled == true) {
                    log(" HOME button pressed; forward action to specific call method");
                    //executeSystemCall("am start -a android.intent.action.MAIN -c android.intent.category.HOME");
                    whichActionToPerform(mcontext, home_call_option, home_call_entry);
                    param.setResult(null);
                }
            }
        });
//        }

/*      findAndHookMethod("app.ToolkitApp", lpparam.classLoader, "keyBack", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                XposedBridge.log(TAG + " BACK button pressed; forward action  to the launcher.sh");
                Log.d(TAG, "BACK button pressed; forward action  to the launcher.sh");
                onItemSelectedp(4);
                param.setResult(null);
            }
        }); */

//        if (mute_key_capture_enabled == true) {
        findAndHookMethod("module.main.HandlerMain", lpparam.classLoader, "mcuKeyVolMute", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                Context mcontext = (Context) AndroidAppHelper.currentApplication();
                sharedprefs = new RemotePreferences(mcontext, "org.hvdw.xfyttweaker.preferences.provider", MySettings.SHARED_PREFS_FILENAME);
                mute_key_capture_enabled = sharedprefs.getBoolean(MySettings.MUTE_KEY_CAPTURE, true);
                mute_call_option = sharedprefs.getString(MySettings.MUTE_CALL_OPTION, "");
                mute_call_entry = sharedprefs.getString(MySettings.MUTE_CALL_ENTRY, "");
                if (mute_key_capture_enabled == true) {
                    log(" MUTE button pressed; forward action to specific call method");
                    whichActionToPerform(mcontext, mute_call_option, mute_call_entry);
                    param.setResult(null);
                }
            }
        });
//        }

       /* End of the part where the SofiaServer hooks are taking place
       *  Now starts the part where the keys of the Canbus apk are captured
       */ 
       } else if (lpparam.packageName.equals("com.syu.canbus")) {
          if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N){

//        if (disable_airhelper == true) {
          findAndHookMethod("com.syu.ui.air.AirHelper", lpparam.classLoader, "showAndRefresh", new XC_MethodHook() {
              @Override
              protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                Context mcontext = (Context) AndroidAppHelper.currentApplication();
                sharedprefs = new RemotePreferences(mcontext, "org.hvdw.xfyttweaker.preferences.provider", MySettings.SHARED_PREFS_FILENAME);
                disable_airhelper = sharedprefs.getBoolean(MySettings.PREF_DISABLE_AIRHELPER, true);
                if (disable_airhelper == true) {
                    log(" prevent canbus airconditiong change popup");
                    param.setResult(null);
                }
              }
          });
//        }


//        if (disable_doorhelper == true) {
          findAndHookMethod("com.syu.ui.door.DoorHelper", lpparam.classLoader, "showAndRefresh", new XC_MethodHook() {
              @Override
              protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                Context mcontext = (Context) AndroidAppHelper.currentApplication();
                sharedprefs = new RemotePreferences(mcontext, "org.hvdw.xfyttweaker.preferences.provider", MySettings.SHARED_PREFS_FILENAME);
                  disable_doorhelper = sharedprefs.getBoolean(MySettings.PREF_DISABLE_DOORHELPER, true);
                  if (disable_doorhelper == true) {
                    XposedBridge.log(TAG + " prevent canbus door open popup");
                    param.setResult(null);
                  }
              }
          });
//        }
          } // end of Android version check

       /* End of the part where the CANbus apk hooks are taking place
       *  Nowstarts the part where the SystemUI clock display is captured to display the CPU temp.
       */
//       } else if ((lpparam.packageName.equals("com.android.systemui")) && (show_cpu_temp == true)) {
       } else if ((lpparam.packageName.equals("com.android.systemui"))) {
        findAndHookMethod("com.android.systemui.statusbar.policy.Clock", lpparam.classLoader, "updateClock", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Context mcontext = (Context) AndroidAppHelper.currentApplication();
                sharedprefs = new RemotePreferences(mcontext, "org.hvdw.xfyttweaker.preferences.provider", MySettings.SHARED_PREFS_FILENAME);
                show_cpu_temp = sharedprefs.getBoolean(MySettings.SHOW_CPU_TEMP, false);
                if (show_cpu_temp == true) {
                    TextView tv = (TextView) param.thisObject;
                    String text = tv.getText().toString();
                    String temp = String.valueOf(getCpuTemp());
                    //remove + in front of string
                    temp = temp.replace("+", "");
                    tv.setText("CPU: " + temp + " °C  " + text);
                    //tv.setTextColor(Color.YELLOW);
                    //tv.setTextColor(Color.parseColor("#F06D2F")); // orange
                   //tv.setTextColor(Color.RED);
                }
            }

        });
       /*
       *  simply return out of the module if no sofiaser or no CANbus or no SystemUI (would be very strange) is detected
       */
       } else return;
    }
    /* End of the handleLoadPackage function doing the capture key functions */
/**********************************************************************************************************************************************/

    public void whichActionToPerform (Context context, String callMethod, String actionString) {
        XposedBridge.log(TAG + " WhichActionToPerform: Call method: " + callMethod + " actionString: " + actionString);
        if (callMethod.equals("pkgname")) {
            //Log.d(TAG, " the callmethond is indeed pkgname");
            startActivityByPackageName(context, actionString);
        }
        if (callMethod.equals("pkg_intent")) {
            startActivityByIntentName(context, actionString);
        }
        if (callMethod.equals("sys_call")) {
            XSharedPreferences sharedPreferences = new XSharedPreferences("org.hvdw.xfyttweaker");
            sharedPreferences.makeWorldReadable();
            use_root_access = sharedprefs.getBoolean(MySettings.USE_ROOT_ACCESS, true);
            //executeSystemCall(actionString);
            String[] cmd = actionString.split(";");
            if (use_root_access == true) {
                rootExec(cmd);
            } else {
                shellExec(cmd);
            }
        }
    };


/*  private static void executeSystemCall(String input) {
        String cmd = input;
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            Log.d(TAG, cmd);
            XposedBridge.log(TAG + ": " + cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    };
*/

// Simple versions: a normal shell version and an su version
/*  public static void shellExec(String...strings) {
        try{
            Process sh = Runtime.getRuntime().exec("sh");
            DataOutputStream outputStream = new DataOutputStream(sh.getOutputStream());

            for (String s : strings) {
                s = s.trim();
                outputStream.writeBytes(s+"\n");
                outputStream.flush();
            }

            outputStream.writeBytes("exit\n");
            outputStream.flush();
            try {
                sh.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            outputStream.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void rootExec(String...strings) {
        try{
            Process su = Runtime.getRuntime().exec("su");
            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

            for (String s : strings) {
                s = s.trim();
                outputStream.writeBytes(s+"\n");
                outputStream.flush();
            }

            outputStream.writeBytes("exit\n");
            outputStream.flush();
            try {
                su.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            outputStream.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
*/

/*  More complicated versions of above shell and su call. As I want to run multiple commands I also need to look at that. 
    copied from https://stackoverflow.com/questions/20932102/execute-shell-command-from-android/26654728
    from the code of CarloCannas
*/
    public static String shellExec(String... strings) {
        String res = "";
        DataOutputStream outputStream = null;
        InputStream response = null;
        try {
            Process sh = Runtime.getRuntime().exec("sh");
            outputStream = new DataOutputStream(sh.getOutputStream());
            response = sh.getInputStream();

            for (String s : strings) {
                s = s.trim();
                outputStream.writeBytes(s + "\n");
                outputStream.flush();
            }

            outputStream.writeBytes("exit\n");
            outputStream.flush();
            try {
                sh.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            res = readFully(response);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Closer.closeSilently(outputStream, response);
        }
        return res;
    }


    public static String rootExec(String... strings) {
        String res = "";
        DataOutputStream outputStream = null;
        InputStream response = null;
        try {
            Process su = Runtime.getRuntime().exec("su");
            outputStream = new DataOutputStream(su.getOutputStream());
            response = su.getInputStream();

            for (String s : strings) {
                s = s.trim();
                outputStream.writeBytes(s + "\n");
                outputStream.flush();
            }

            outputStream.writeBytes("exit\n");
            outputStream.flush();
            try {
                su.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            res = readFully(response);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Closer.closeSilently(outputStream, response);
        }
        return res;
    }

    public static String readFully(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = is.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos.toString("UTF-8");
    }


    private static void executeScript(String input) {
        String cmd = input;
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            Log.d(TAG, cmd);
            XposedBridge.log(TAG + ": " + cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

    private static void executeBroadcast(String input) {
        StringBuffer output = new StringBuffer();
        String cmd = "am broadcast -a " + input;
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            Log.d(TAG, cmd);
            XposedBridge.log(TAG + ": " + cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

    public void startActivityByIntentName(Context context, String component) {
        Intent sIntent = new Intent(Intent.ACTION_MAIN);
        sIntent.setComponent(ComponentName.unflattenFromString(component));
        sIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        sIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sIntent);
    }


    public void startActivityByPackageName(Context context, String packageName) {
        PackageManager pManager = context.getPackageManager();
        Intent intent = pManager.getLaunchIntentForPackage(packageName);
        XposedBridge.log(TAG + " startActivityByPackageName: " + packageName);
        if (intent != null) {
            context.startActivity(intent);
        }
    }

    public float getCpuTemp() {
        Process p;
        float temp = 0;
        String tempstr;
        String build_version;

        build_version = Build.VERSION.RELEASE;
        log("build_version: " + build_version);
        if (build_version.toLowerCase().contains("6".toLowerCase())) {
            try {
                for (int i=1; i<5; i++) {
                    p = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone" + Integer.toString(i) + "/temp");
                    p.waitFor();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

                    String line = reader.readLine();
                    float tmptemp = Float.parseFloat(line) / 1000.0f;
                    if (tmptemp > temp) temp = tmptemp;
                }
                // round to 1 decimal like 45.3
                int scale = (int) Math.pow(10, 1);
                return (float) Math.round(temp *scale)/scale;

            } catch (Exception e) {
                e.printStackTrace();
                return 0.0f;
            }
        } else {
            try {
                p = Runtime.getRuntime().exec("cat /sys/rockchip_thermal/temp");
                p.waitFor();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String splitline[] = reader.readLine().split(":");
                tempstr = splitline[1].trim();
                return (float) Float.parseFloat(tempstr);
            } catch (Exception e) {
                e.printStackTrace();
                return 0.0f;
            }
        }
    }

    // Methods for the "eliminate feedback during the call if you have OK Google anywhere enabled"
    public void sudoVoiceKill(){
        try {
            Process suProcess = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());
            //XposedBridge.log("BTTEST new su kill");
            os.writeBytes("am force-stop com.google.android.googlequicksearchbox" + "\n");
            os.flush();
            os.writeBytes("pm revoke com.google.android.googlequicksearchbox android.permission.RECORD_AUDIO" + "\n");
            os.flush();
        }
        catch (Exception e) {
            RuntimeException ex = new RuntimeException("Unable to kill Google Voice Search: " + e.getMessage());
            throw ex;
        }
    }


    public void sudoVoiceRestart(){
        try {
            Process suProcess = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());
            //XposedBridge.log("BTTEST new su start");
            os.writeBytes("pm grant com.google.android.googlequicksearchbox android.permission.RECORD_AUDIO" + "\n");
            os.flush();
            os.writeBytes("am start com.google.android.googlequicksearchbox" + "\n");
            os.flush();
        }
        catch (Exception e) {
            RuntimeException ex = new RuntimeException("Unable to start Google Voice Search: " + e.getMessage());
            throw ex;
        }
    }
    // End of methods for the "eliminate feedback during the call if you have OK Google anywhere enabled"


    //Function used for the multiptap options
    public void multitap(String first_call_option, String first_call_entry, String second_call_option, String second_call_entry, String third_call_option, String third_call_entry, Integer tap_delay) {
        final String first_call = first_call_option;
        final String first_entry = first_call_entry;
        final String second_call = second_call_option;
        final String second_entry = second_call_entry;
        final String third_call = third_call_option;
        final String third_entry = third_call_entry;
        final Context context = (Context) AndroidAppHelper.currentApplication();
        XposedBridge.log(TAG + String.valueOf(tap_delay));
        count3++;

                    if (count3 == 1) {
                        new Timer().schedule(
                            new TimerTask() {
                               @Override
                               public void run() {
                                  if (count3 == 1) {
                                     whichActionToPerform(context, first_call, first_entry);
                                     count3 = 0;
                                  }
                               }
                            },
                            tap_delay
                        );
                    } else if (count3 == 2) {
                        new Timer().schedule(
                            new TimerTask() {
                               @Override
                               public void run() {
                                  if (count3 == 2) {
                                      whichActionToPerform(context, second_call, second_entry);
                                      count3 = 0;
                                  }
                               }
                            },
                            tap_delay
                        );
                    } else if (count3 == 3) {
                        new Timer().schedule(
                            new TimerTask() {
                               @Override
                               public void run() {
                                  if (count3 == 3) {
                                      whichActionToPerform(context, third_call, third_entry);
                                      count3 = 0;
                                  }
                               }
                            },
                            tap_delay
                        );
                    } else if (count3 >= 4) {
                        new Timer().schedule(
                            new TimerTask() {
                               @Override
                               public void run() {
                                  if (count3 >= 4) {
                                      //Use the first option again
                                      whichActionToPerform(context, first_call, first_entry);
                                      count3 = 0;
                                  }
                               }
                            },
                            tap_delay
                        );
                    }
    }

}
