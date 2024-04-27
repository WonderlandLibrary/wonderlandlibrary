/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  qiriyou.verV3Z.Loader
 */
package Verify1.utils;

import java.io.IOException;
import qiriyou.verV3Z.Loader;

public class HttpUtil {
    public static native String sendGet(String var0, String var1);

    public static native String webget(String var0) throws IOException;

    static {
        Loader.registerNativesForClass((int)7, HttpUtil.class);
        HttpUtil.$qiriyouLoader();
    }

    public static native /* synthetic */ void $qiriyouLoader();
}

