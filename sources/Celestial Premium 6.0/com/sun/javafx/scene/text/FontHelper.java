/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.text.Font
 */
package com.sun.javafx.scene.text;

import com.sun.javafx.util.Utils;
import javafx.scene.text.Font;

public class FontHelper {
    private static FontAccessor fontAccessor;

    private FontHelper() {
    }

    public static Object getNativeFont(Font font) {
        return fontAccessor.getNativeFont(font);
    }

    public static void setNativeFont(Font font, Object object, String string, String string2, String string3) {
        fontAccessor.setNativeFont(font, object, string, string2, string3);
    }

    public static Font nativeFont(Object object, String string, String string2, String string3, double d) {
        return fontAccessor.nativeFont(object, string, string2, string3, d);
    }

    public static void setFontAccessor(FontAccessor fontAccessor) {
        if (FontHelper.fontAccessor != null) {
            throw new IllegalStateException();
        }
        FontHelper.fontAccessor = fontAccessor;
    }

    static {
        Utils.forceInit(Font.class);
    }

    public static interface FontAccessor {
        public Object getNativeFont(Font var1);

        public void setNativeFont(Font var1, Object var2, String var3, String var4, String var5);

        public Font nativeFont(Object var1, String var2, String var3, String var4, double var5);
    }
}

