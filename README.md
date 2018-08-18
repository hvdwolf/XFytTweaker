# XFytTweaker

For releases see the "[Xposed Module Repository](http://repo.xposed.info/module/org.hvdw.xfyttweaker)" or use the Xposed Installer on your Joying unit. The module can also be installed from my [JET apk](https://hvdwolf.github.io/Joying-RootAssistant/).

![logo](https://github.com/hvdwolf/XFytTweaker/blob/master/images/logo.png)

This Xposed module is for Android car units based on FYT SOMs. It is primarily focussing on Joying units for both the Intel Sofia 3GR units as well as the Rockchip PX5 units. However, it should function as well on (rooted and Xposed enhanced) FYT based Witson units and a couple of other FYT based units. It should work with Android 6.0.1 and Android 8.0.0 units.

This Xposed module would not have been possible without the excellent Xposed framework and tools by [rovo89](https://github.com/rovo89). Thanks a lot.<br>
This Xposed module would not have been possible on Android 8 without the great [RemotePreferences](https://github.com/apsun/RemotePreferences) library from apsun.<br>

This module is the successor of the [XSofiaTweaker](https://github.com/hvdwolf/XSofiaTweaker) module which completely focused on the Joying Intel Sofia 3GR unit on Android 6.0.1.

This xposed module:
* captures the hardware keys from the Joying unit and allows you to reprogram them. This must/can be configured from the Settings (sub)screen.
* allows you to switch on/off the nokill option (prevents killing of all apps when going into sleep).
* allows you to unmute channel 4, which is used for Google voice feedback.
* can act on the ACC_ON, ACC_OFF and Resume events.
* has an option to prevent the Bluetooth app from staying full screen on top (thanks to @gtxaspec)
* has an option to prevent the CANbus overlays popping up on heat/airco change or door(s) opening (Android 6 only)
* has an option to run system calls as root (Know what you do!)
* has an option to display the CPU temperature in the status bar
* has an option to disable the yellow MCU errors overlay (in case this happens to you) (thanks to @RoNeReR) (Android 6 only)
* has an option to hide the volume bar which overlays on volume changes (volume is always numerically displayed in status bar) (thanks to @IG_Vasilich for pointing out the hook)
* supports a usb dac where volume control will be rerouted to stock android volume control (thanks to @RoNeReR)
* eliminates feedback during the call if you have OK Google anywhere enabled (thanks to @gtxaspec)
This must/can be configured from the Settings (sub)screens.

This module can and should replace the custom key mod mod from Gustden.

Please help to translate in your own language: see bottom of this page.


**Working:** 
* ACC_ON/ACC_OF (key 97 and 98)
* Wake_up/resume (key 99)
* NAVI (key 9)
* Phone/BT(key 27) (confirmed by gtx(aspec))
* BAND (radio) (key 34)
* DVD (key 31) (confirmed by gtx(aspec))
* Eject (key 32) (confirmed by gtx(aspec))
* MEDIA (key 33)
* SRC/Mode (key 37) (confirmed by gtx(aspec))
* EQ button
* HOME (key 3)
* double tap/triple tap of keys, meaning that you can "rotate" keys. For example: Your steering wheel BT or Mode/SRC button, can start the radio app on 1 tap, start a media player on 2 taps, or start the phone app on 3 taps.


**ToDo list:**

* BACK (key 4)
* ....

**Note w.r.t. the NoKill function:**
Some apps prevent the unit from going into deep-sleep. The old SofiaServer simply killed these apps. This module doesn't do that if you enable the skipping/bypassing of that "kill" function. Some apps keep a "wakelock" preventing Android from going to sleep. The MCU detects the apps keeping the CPU awake and will completely switch off the unit, resulting in a cold-boot upon switching on the contact. That is not the fault of this module. It is the fault of these "bad behaving" apps. A way to overcome this is to kill this specific bad behaving app(s) on ACC_OFF event (key 98), and to start them again on the ACC_ON event (key 97).

## Some preliminary images
**Main settings screen**
![Image of main settings screen](https://github.com/hvdwolf/XFytTweaker/blob/master/images/01-Settings-Main.png)

**Miscellaneous Settings**
![Image of Miscellaneous Settings](https://github.com/hvdwolf/XFytTweaker/blob/master/images/02-Settings-Miscellaneous.png)

**Application Key Mod Settings**
![Image of app key mods settings](https://github.com/hvdwolf/XFytTweaker/blob/master/images/03-00-Settings-AppKeyMods.png)

**Call Method**
![Image of app key call method](https://github.com/hvdwolf/XFytTweaker/blob/master/images/03-01-Settings-AppKeyMods.png)

**String to be used**
![Image of app key mods text string](https://github.com/hvdwolf/XFytTweaker/blob/master/images/03-02-Settings-AppKeyMods.png)

**What to do when the contact/unit is switched ON or OFF**
![Image of acc_on_off_resume text string](https://github.com/hvdwolf/XFytTweaker/blob/master/images/04-Settings-AccOnOff.png)

**System Key Mod Settings**
![Image of System Key Mod Settings](https://github.com/hvdwolf/XFytTweaker/blob/master/images/05-00-systemkeys.png)

**System Key Mod Settings modified**
![Image of System Key Mod Settings modified](https://github.com/hvdwolf/XFytTweaker/blob/master/images/05-01-systemkeys.png)

**Info screen**<br>
![Image of System Key Mod Settings](https://github.com/hvdwolf/XFytTweaker/blob/master/images/06-00-info.png)

## Translations
You can help to translate this app into your own language.

See https://github.com/hvdwolf/XFytTweaker/tree/master/app/src/main/res You will see a folder values. In this folder you will find a strings.xml file. In this file you will find lines like: 
`<string name="app_settings">Application preferences</string>`
and 
`<string name="misc_settings">Miscellaneous Settings</string>`
The second part "Application preferences" and Miscellaneous Settings are the strings to be translated.

Note: Do use an editor that can handle unix linefeeds. Do NOT use windows notepad. Instead use a tool like Notepad++ (if you are on Windows)

If you are ready, create an issue (top menu of this page) and call it something like "Finnish translation" (or whatever your language is). Attach your file and again: specify which language it is!
