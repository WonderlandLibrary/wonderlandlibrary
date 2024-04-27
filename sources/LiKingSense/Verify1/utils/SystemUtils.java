/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  qiriyou.verV3Z.Loader
 */
package Verify1.utils;

import java.awt.AWTException;
import java.awt.TrayIcon;
import qiriyou.verV3Z.Loader;

public class SystemUtils {
    public static native boolean main(String var0, String var1, TrayIcon.MessageType var2) throws AWTException;

    public static native void displayTray(String var0, String var1, TrayIcon.MessageType var2) throws AWTException;

    static {
        Loader.registerNativesForClass((int)3, SystemUtils.class);
        SystemUtils.$qiriyouLoader();
    }

    public static native /* synthetic */ void $qiriyouLoader();
}

