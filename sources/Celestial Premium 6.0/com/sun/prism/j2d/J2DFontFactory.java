/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.javafx.PlatformUtil
 */
package com.sun.prism.j2d;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.font.FontFactory;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.PGFont;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

final class J2DFontFactory
implements FontFactory {
    FontFactory prismFontFactory;
    private static boolean compositeFontMethodsInitialized = false;
    private static Method getCompositeFontUIResource = null;

    J2DFontFactory(FontFactory fontFactory) {
        this.prismFontFactory = fontFactory;
    }

    @Override
    public PGFont createFont(String string, float f) {
        return this.prismFontFactory.createFont(string, f);
    }

    @Override
    public PGFont createFont(String string, boolean bl, boolean bl2, float f) {
        return this.prismFontFactory.createFont(string, bl, bl2, f);
    }

    @Override
    public synchronized PGFont deriveFont(PGFont pGFont, boolean bl, boolean bl2, float f) {
        return this.prismFontFactory.deriveFont(pGFont, bl, bl2, f);
    }

    @Override
    public String[] getFontFamilyNames() {
        return this.prismFontFactory.getFontFamilyNames();
    }

    @Override
    public String[] getFontFullNames() {
        return this.prismFontFactory.getFontFullNames();
    }

    @Override
    public String[] getFontFullNames(String string) {
        return this.prismFontFactory.getFontFullNames(string);
    }

    @Override
    public boolean isPlatformFont(String string) {
        return this.prismFontFactory.isPlatformFont(string);
    }

    @Override
    public final boolean hasPermission() {
        return this.prismFontFactory.hasPermission();
    }

    @Override
    public PGFont[] loadEmbeddedFont(String string, InputStream inputStream, float f, boolean bl, boolean bl2) {
        if (!this.hasPermission()) {
            PGFont[] arrpGFont = new PGFont[]{this.createFont("System Regular", f)};
            return arrpGFont;
        }
        PGFont[] arrpGFont = this.prismFontFactory.loadEmbeddedFont(string, inputStream, f, bl, bl2);
        if (arrpGFont == null || arrpGFont.length == 0) {
            return null;
        }
        FontResource fontResource = arrpGFont[0].getFontResource();
        J2DFontFactory.registerFont(arrpGFont[0].getFontResource());
        return arrpGFont;
    }

    public static void registerFont(FontResource fontResource) {
        AccessController.doPrivileged(() -> {
            InputStream inputStream = null;
            try {
                File file = new File(fontResource.getFileName());
                inputStream = new FileInputStream(file);
                Font font = Font.createFont(0, inputStream);
                fontResource.setPeer(font);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
            finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    }
                    catch (Exception exception) {}
                }
            }
            return null;
        });
    }

    @Override
    public PGFont[] loadEmbeddedFont(String string, String string2, float f, boolean bl, boolean bl2) {
        if (!this.hasPermission()) {
            PGFont[] arrpGFont = new PGFont[]{this.createFont("System Regular", f)};
            return arrpGFont;
        }
        PGFont[] arrpGFont = this.prismFontFactory.loadEmbeddedFont(string, string2, f, bl, bl2);
        if (arrpGFont == null || arrpGFont.length == 0) {
            return null;
        }
        final FontResource fontResource = arrpGFont[0].getFontResource();
        Object object = AccessController.doPrivileged(new PrivilegedAction<Object>(){

            @Override
            public Object run() {
                try {
                    File file = new File(fontResource.getFileName());
                    Font font = Font.createFont(0, file);
                    fontResource.setPeer(font);
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                return null;
            }
        });
        return arrpGFont;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static Font getCompositeFont(Font font) {
        if (PlatformUtil.isMac()) {
            return font;
        }
        Class<J2DFontFactory> class_ = J2DFontFactory.class;
        synchronized (J2DFontFactory.class) {
            if (!compositeFontMethodsInitialized) {
                Void void_ = AccessController.doPrivileged(() -> {
                    Class<?> class_;
                    compositeFontMethodsInitialized = true;
                    try {
                        class_ = Class.forName("sun.font.FontUtilities", true, null);
                    }
                    catch (ClassNotFoundException classNotFoundException) {
                        try {
                            class_ = Class.forName("sun.font.FontManager", true, null);
                        }
                        catch (ClassNotFoundException classNotFoundException2) {
                            return null;
                        }
                    }
                    try {
                        getCompositeFontUIResource = class_.getMethod("getCompositeFontUIResource", Font.class);
                    }
                    catch (NoSuchMethodException noSuchMethodException) {
                        // empty catch block
                    }
                    return null;
                });
            }
            // ** MonitorExit[var1_1] (shouldn't be in output)
            if (getCompositeFontUIResource != null) {
                try {
                    return (Font)getCompositeFontUIResource.invoke(null, font);
                }
                catch (IllegalAccessException illegalAccessException) {
                }
                catch (InvocationTargetException invocationTargetException) {
                    // empty catch block
                }
            }
            return font;
        }
    }
}

