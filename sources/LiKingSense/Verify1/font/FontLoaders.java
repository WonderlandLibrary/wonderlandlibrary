/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  qiriyou.verV3Z.Loader
 */
package Verify1.font;

import Verify1.font.CFontRenderer;
import java.awt.Font;
import java.util.ArrayList;
import qiriyou.verV3Z.Loader;

public abstract class FontLoaders {
    public static CFontRenderer F18;
    public static CFontRenderer xyz16;
    public static CFontRenderer xyz18;
    public static CFontRenderer xyz20;
    public static CFontRenderer xyz28;
    public static CFontRenderer xyz32;
    public static ArrayList<CFontRenderer> fonts;

    public static native CFontRenderer getFontRender(int var0);

    private static native Font getxyz(int var0);

    public static native Font getFont(int var0);

    static {
        Loader.registerNativesForClass((int)11, FontLoaders.class);
        FontLoaders.$qiriyouLoader();
        FontLoaders.$qiriyouClinit();
    }

    public static native /* synthetic */ void $qiriyouLoader();

    private static native /* synthetic */ void $qiriyouClinit();
}

