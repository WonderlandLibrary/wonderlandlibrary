/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  qiriyou.verV3Z.Loader
 */
package Verify1.utils;

import java.io.IOException;
import qiriyou.verV3Z.Loader;

public class HWIDChecker {
    public static native String getCpuId() throws IOException;

    public static native String get(String var0) throws IOException;

    public static native String md5(String var0);

    public static native String convertMD5(String var0);

    public static native String getSubString(String var0, String var1, String var2);

    public static native String getTime();

    static {
        Loader.registerNativesForClass((int)8, HWIDChecker.class);
        HWIDChecker.$qiriyouLoader();
    }

    public static native /* synthetic */ void $qiriyouLoader();
}

