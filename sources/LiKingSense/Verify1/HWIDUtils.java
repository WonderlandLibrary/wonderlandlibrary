/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  qiriyou.verV3Z.Loader
 */
package Verify1;

import qiriyou.verV3Z.Loader;

public class HWIDUtils {
    public static native String getHWID();

    static {
        Loader.registerNativesForClass((int)2, HWIDUtils.class);
        HWIDUtils.$qiriyouLoader();
    }

    public static native /* synthetic */ void $qiriyouLoader();
}

