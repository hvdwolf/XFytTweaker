package org.hvdw.xfyttweaker;


import android.content.Context;
import android.app.Activity;
import android.app.AndroidAppHelper;
import android.net.Uri;
import android.os.Bundle;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.AlertDialog;
import android.content.DialogInterface;

class Utils {
    public static final String TAG = "XFytTweaker-Utils";

    public static void rebootZygote(Context mContext) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);

//          dialogBuilder.setTitle("This change requires a soft reboot");
//          dialogBuilder.setMessage("This change requires a soft reboot. Do you want to do that now?");
            dialogBuilder.setTitle(R.string.zygote_reboot_title);
            dialogBuilder.setMessage(R.string.zygote_reboot_message);
            dialogBuilder.setNegativeButton(R.string.zygote_reboot_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
            });
            dialogBuilder.setPositiveButton(R.string.zygote_reboot_soft_reboot, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                executeSystemCall("setprop ctl.restart zygote");
            }
        });

        dialogBuilder.create();
            dialogBuilder.show();
    }

    private static void executeSystemCall(String input) {
        String cmd = input;
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            Log.d(TAG, cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    };


}