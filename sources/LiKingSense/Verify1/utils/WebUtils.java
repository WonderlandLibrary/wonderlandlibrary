/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  qiriyou.verV3Z.Loader
 */
package Verify1.utils;

import java.io.IOException;
import qiriyou.verV3Z.Loader;

public class WebUtils {
    public static native String get(String var0) throws IOException;

    public static native String readContent(String var0) throws IOException;

    static {
        Loader.registerNativesForClass((int)1, WebUtils.class);
        WebUtils.$qiriyouLoader();
    }

    public static native /* synthetic */ void $qiriyouLoader();
}

