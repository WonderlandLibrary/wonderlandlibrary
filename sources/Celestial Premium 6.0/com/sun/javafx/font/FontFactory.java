/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.javafx.font.PGFont;
import java.io.InputStream;

public interface FontFactory {
    public static final String DEFAULT_FULLNAME = "System Regular";

    public PGFont createFont(String var1, float var2);

    public PGFont createFont(String var1, boolean var2, boolean var3, float var4);

    public PGFont deriveFont(PGFont var1, boolean var2, boolean var3, float var4);

    public String[] getFontFamilyNames();

    public String[] getFontFullNames();

    public String[] getFontFullNames(String var1);

    public boolean hasPermission();

    public PGFont[] loadEmbeddedFont(String var1, InputStream var2, float var3, boolean var4, boolean var5);

    public PGFont[] loadEmbeddedFont(String var1, String var2, float var3, boolean var4, boolean var5);

    public boolean isPlatformFont(String var1);
}

