/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.text.Font
 *  javafx.scene.text.FontPosture
 *  javafx.scene.text.FontWeight
 */
package com.sun.javafx.font;

import com.sun.javafx.font.FontFactory;
import com.sun.javafx.font.Metrics;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.PrismFontUtils;
import com.sun.javafx.scene.text.FontHelper;
import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.FontMetrics;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class PrismFontLoader
extends FontLoader {
    private static PrismFontLoader theInstance = new PrismFontLoader();
    private boolean embeddedFontsLoaded = false;
    FontFactory installedFontFactory = null;

    public static PrismFontLoader getInstance() {
        return theInstance;
    }

    Properties loadEmbeddedFontDefinitions() {
        Properties properties = new Properties();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            return properties;
        }
        URL uRL = classLoader.getResource("META-INF/fonts.mf");
        if (uRL == null) {
            return properties;
        }
        try (InputStream inputStream = uRL.openStream();){
            properties.load(inputStream);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return properties;
    }

    private void loadEmbeddedFonts() {
        if (!this.embeddedFontsLoaded) {
            FontFactory fontFactory = this.getFontFactoryFromPipeline();
            if (!fontFactory.hasPermission()) {
                this.embeddedFontsLoaded = true;
                return;
            }
            Properties properties = this.loadEmbeddedFontDefinitions();
            Enumeration<Object> enumeration = properties.keys();
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            while (enumeration.hasMoreElements()) {
                String string = (String)enumeration.nextElement();
                String string2 = properties.getProperty(string);
                if (!string2.startsWith("/")) continue;
                string2 = string2.substring(1);
                try {
                    InputStream inputStream = classLoader.getResourceAsStream(string2);
                    try {
                        fontFactory.loadEmbeddedFont(string, inputStream, 0.0f, true, false);
                    }
                    finally {
                        if (inputStream == null) continue;
                        inputStream.close();
                    }
                }
                catch (Exception exception) {}
            }
            this.embeddedFontsLoaded = true;
        }
    }

    private Font[] createFonts(PGFont[] arrpGFont) {
        if (arrpGFont == null || arrpGFont.length == 0) {
            return null;
        }
        Font[] arrfont = new Font[arrpGFont.length];
        for (int i = 0; i < arrpGFont.length; ++i) {
            arrfont[i] = this.createFont(arrpGFont[i]);
        }
        return arrfont;
    }

    @Override
    public Font[] loadFont(InputStream inputStream, double d, boolean bl) {
        FontFactory fontFactory = this.getFontFactoryFromPipeline();
        PGFont[] arrpGFont = fontFactory.loadEmbeddedFont(null, inputStream, (float)d, true, bl);
        return this.createFonts(arrpGFont);
    }

    @Override
    public Font[] loadFont(String string, double d, boolean bl) {
        FontFactory fontFactory = this.getFontFactoryFromPipeline();
        PGFont[] arrpGFont = fontFactory.loadEmbeddedFont(null, string, (float)d, true, bl);
        return this.createFonts(arrpGFont);
    }

    private Font createFont(PGFont pGFont) {
        return FontHelper.nativeFont(pGFont, pGFont.getName(), pGFont.getFamilyName(), pGFont.getStyleName(), pGFont.getSize());
    }

    @Override
    public List<String> getFamilies() {
        this.loadEmbeddedFonts();
        return Arrays.asList(this.getFontFactoryFromPipeline().getFontFamilyNames());
    }

    @Override
    public List<String> getFontNames() {
        this.loadEmbeddedFonts();
        return Arrays.asList(this.getFontFactoryFromPipeline().getFontFullNames());
    }

    @Override
    public List<String> getFontNames(String string) {
        this.loadEmbeddedFonts();
        return Arrays.asList(this.getFontFactoryFromPipeline().getFontFullNames(string));
    }

    @Override
    public Font font(String string, FontWeight fontWeight, FontPosture fontPosture, float f) {
        FontFactory fontFactory = this.getFontFactoryFromPipeline();
        if (!this.embeddedFontsLoaded && !fontFactory.isPlatformFont(string)) {
            this.loadEmbeddedFonts();
        }
        boolean bl = fontWeight != null && fontWeight.ordinal() >= FontWeight.BOLD.ordinal();
        boolean bl2 = fontPosture == FontPosture.ITALIC;
        PGFont pGFont = fontFactory.createFont(string, bl, bl2, f);
        Font font = FontHelper.nativeFont(pGFont, pGFont.getName(), pGFont.getFamilyName(), pGFont.getStyleName(), f);
        return font;
    }

    @Override
    public void loadFont(Font font) {
        FontFactory fontFactory = this.getFontFactoryFromPipeline();
        String string = font.getName();
        if (!this.embeddedFontsLoaded && !fontFactory.isPlatformFont(string)) {
            this.loadEmbeddedFonts();
        }
        PGFont pGFont = fontFactory.createFont(string, (float)font.getSize());
        String string2 = pGFont.getName();
        String string3 = pGFont.getFamilyName();
        String string4 = pGFont.getStyleName();
        FontHelper.setNativeFont(font, pGFont, string2, string3, string4);
    }

    @Override
    public FontMetrics getFontMetrics(Font font) {
        if (font != null) {
            PGFont pGFont = (PGFont)FontHelper.getNativeFont(font);
            Metrics metrics = PrismFontUtils.getFontMetrics(pGFont);
            float f = -metrics.getAscent();
            float f2 = -metrics.getAscent();
            float f3 = metrics.getXHeight();
            float f4 = metrics.getDescent();
            float f5 = metrics.getDescent();
            float f6 = metrics.getLineGap();
            return FontMetrics.createFontMetrics(f, f2, f3, f4, f5, f6, font);
        }
        return null;
    }

    @Override
    public float getCharWidth(char c, Font font) {
        PGFont pGFont = (PGFont)FontHelper.getNativeFont(font);
        return (float)PrismFontUtils.getCharWidth(pGFont, c);
    }

    @Override
    public float getSystemFontSize() {
        return PrismFontFactory.getSystemFontSize();
    }

    private FontFactory getFontFactoryFromPipeline() {
        if (this.installedFontFactory != null) {
            return this.installedFontFactory;
        }
        try {
            Class<?> class_ = Class.forName("com.sun.prism.GraphicsPipeline");
            Method method = class_.getMethod("getPipeline", null);
            Object object = method.invoke(null, new Object[0]);
            Method method2 = class_.getMethod("getFontFactory", null);
            Object object2 = method2.invoke(object, new Object[0]);
            this.installedFontFactory = (FontFactory)object2;
        }
        catch (Exception exception) {
            // empty catch block
        }
        return this.installedFontFactory;
    }
}

