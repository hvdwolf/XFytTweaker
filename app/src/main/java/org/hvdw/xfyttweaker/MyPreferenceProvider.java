package org.hvdw.xfyttweaker;

import com.crossbowffs.remotepreferences.RemotePreferenceProvider;
import org.hvdw.xfyttweaker.MySettings;

public class MyPreferenceProvider extends RemotePreferenceProvider {
    public MyPreferenceProvider() {
        super("org.hvdw.xfyttweaker.preferences.provider", new String[] {"org.hvdw.xfyttweaker_preferences"});
    }
}