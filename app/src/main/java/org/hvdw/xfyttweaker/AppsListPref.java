package org.hvdw.xfyttweaker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.util.Log;

public class AppsListPref extends ListPreference {

    private static String TAG = "XFytTweaker-AppsListPref";
    private Intent localintent;

    public AppsListPref(Context context, AttributeSet attrs) {
        super(context, attrs);
        final PackageManager pm = context.getPackageManager();
        final String[] pk1 = new String[1];
        final String[] pk2 = new String[1];
        Log.d(TAG, "Inside AppsList.java");
        List<PackageInfo> appListInfo = pm.getInstalledPackages(PackageManager.GET_ACTIVITIES);
        Collections.sort(appListInfo, new Comparator<PackageInfo>() {
            @Override
            public int compare(PackageInfo o1, PackageInfo o2) {
                try {
                    pk1[0] = o1.applicationInfo.loadLabel(pm).toString();
                    pk2[0] = o2.applicationInfo.loadLabel(pm).toString();
                } catch (Resources.NotFoundException e) {
                    Log.w(TAG, "No resource found");
                }
                return pk1[0].compareToIgnoreCase(pk2[0]);

//                return o1.applicationInfo.loadLabel(pm).toString().compareToIgnoreCase(o2.applicationInfo.loadLabel(pm).toString());
            }
        });
        List<CharSequence> entries = new ArrayList<CharSequence>();
        List<CharSequence> entriesValues = new ArrayList<CharSequence>();

        try {
            for (PackageInfo p : appListInfo) {
                localintent = new Intent();
                localintent = pm.getLaunchIntentForPackage(p.applicationInfo.packageName);
                if (localintent != null) {
                    entries.add(p.applicationInfo.loadLabel(pm).toString());
                    entriesValues.add(p.applicationInfo.packageName);
                }
            }
        } catch (Exception e) {
            Log.w(TAG, "Error in appslistpref");
            e.printStackTrace();
        }

        setEntries(entries.toArray(new CharSequence[]{}));
        setEntryValues(entriesValues.toArray(new CharSequence[]{}));
    }

}