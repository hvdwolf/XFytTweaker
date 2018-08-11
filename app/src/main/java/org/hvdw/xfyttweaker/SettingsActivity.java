package org.hvdw.xfyttweaker;

import android.app.Activity;
import android.app.AndroidAppHelper;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;

/*import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;*/

import android.preference.ListPreference;
import android.util.AttributeSet;
import android.preference.PreferenceFragment;

import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceActivity;

import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
//import android.categories.Category;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import java.util.List;
import java.util.ArrayList;
import android.app.Application;



public class SettingsActivity extends PreferenceActivity {
    public static final String TAG = "XFytTweaker-SettingsActivity";

    Context mContext;
    AttributeSet attrs;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getFragmentManager().findFragmentByTag("settings") == null) {
            getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment(), "settings")
                .commit();
        }

        //buildAppsList(mContext, attrs);

        //check our assets file and copy to /sdcard/XFytTweaker if necessary
        //Context mContext = (Context) AndroidAppHelper.currentApplication();
/*      Log.d(TAG, "copying navi_app.txt");
        CheckCopyAssetFile( "navi_app.txt");
        Log.d(TAG, "copying player_app.txt");
        CheckCopyAssetFile( "player_app.txt"); */


    }

    /* Copy the navi_app.txt and player_app.txt to /sdcard/XFytTweaker
    *  but only if they do not exist yet, which means on first run or ifthe user
    *  deleted them, or they are missing for whatever reason
    */
    //    public static void CheckCopyAssetFile(Context mContext, String fileName) {
    public static void CheckCopyAssetFile(String fileName) {
        Context mContext = (Context) AndroidAppHelper.currentApplication();
        AssetManager assetManager = mContext.getAssets();
        String[] files = null;
        InputStream in = null;
        OutputStream out = null;
        // Check if folder exists
        File folder = new File("/sdcard/XFytTweaker");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        // Check if file exists
        File file = new File("/sdcard/XFytTweaker/" + fileName);
        if (!file.exists()) {
            try {
                in = assetManager.open(fileName);
                out = new FileOutputStream("/sdcard/XFytTweaker/" + fileName);
                copyFile(in, out);
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            } catch (IOException e) {
                Log.e(TAG, "Failed to copy asset file: " + fileName, e);
            }
        }
    }

    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
    /* End of the assets file copy */

    public static void buildAppsList(Context mContext, AttributeSet attrs) {
        //AppsList MyAppsList = new AppsList(mContext, attrs);
        //MyAppsList = AppsList.AppsList();
        /*SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        ListPreference listPreferenceCategory = (ListPreference) findPreference("app_selector");
        if (listPreferenceCategory != null) {
            ArrayList<Category> categoryList = getCategories();
            CharSequence entries[] = new String[categoryList.size()];
            CharSequence entryValues[] = new String[categoryList.size()];
            int i = 0;
            for (Category category : categoryList) {
                entries[i] = category.getCategoryName();
                entryValues[i] = Integer.toString(i);
                i++;
            }
            listPreferenceCategory.setEntries(entries);
            listPreferenceCategory.setEntryValues(entryValues);
        } */
    }


}
