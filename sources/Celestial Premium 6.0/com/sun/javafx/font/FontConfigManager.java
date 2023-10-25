/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;

class FontConfigManager {
    static boolean debugFonts = false;
    static boolean useFontConfig = true;
    static boolean fontConfigFailed = false;
    static boolean useEmbeddedFontSupport = false;
    private static final String[] fontConfigNames;
    private static FcCompFont[] fontConfigFonts;
    private static String defaultFontFile;

    private FontConfigManager() {
    }

    private static String[] getFontConfigNames() {
        return fontConfigNames;
    }

    private static String getFCLocaleStr() {
        Locale locale = Locale.getDefault();
        Object object = locale.getLanguage();
        String string = locale.getCountry();
        if (!string.equals("")) {
            object = (String)object + "-" + string;
        }
        return object;
    }

    private static native boolean getFontConfig(String var0, FcCompFont[] var1, boolean var2);

    private static synchronized void initFontConfigLogFonts() {
        int n;
        int n2;
        if (fontConfigFonts != null || fontConfigFailed) {
            return;
        }
        long l = 0L;
        if (debugFonts) {
            l = System.nanoTime();
        }
        String[] arrstring = FontConfigManager.getFontConfigNames();
        FcCompFont[] arrfcCompFont = new FcCompFont[arrstring.length];
        for (n2 = 0; n2 < arrfcCompFont.length; ++n2) {
            arrfcCompFont[n2] = new FcCompFont();
            arrfcCompFont[n2].fcName = arrstring[n2];
            int n3 = arrfcCompFont[n2].fcName.indexOf(58);
            arrfcCompFont[n2].fcFamily = arrfcCompFont[n2].fcName.substring(0, n3);
            arrfcCompFont[n2].style = n2 % 4;
        }
        n2 = 0;
        if (useFontConfig) {
            n2 = FontConfigManager.getFontConfig(FontConfigManager.getFCLocaleStr(), arrfcCompFont, true) ? 1 : 0;
        } else if (debugFonts) {
            System.err.println("Not using FontConfig");
        }
        if (useEmbeddedFontSupport || n2 == 0) {
            EmbeddedFontSupport.initLogicalFonts(arrfcCompFont);
        }
        FontConfigFont fontConfigFont = null;
        for (n = 0; n < arrfcCompFont.length; ++n) {
            FcCompFont fcCompFont = arrfcCompFont[n];
            if (fcCompFont.firstFont == null) {
                if (debugFonts) {
                    System.err.println("Fontconfig returned no font for " + arrfcCompFont[n].fcName);
                }
                fontConfigFailed = true;
                continue;
            }
            if (fontConfigFont != null) continue;
            fontConfigFont = fcCompFont.firstFont;
            defaultFontFile = fontConfigFont.fontFile;
        }
        if (fontConfigFont == null) {
            fontConfigFailed = true;
            System.err.println("Error: JavaFX detected no fonts! Please refer to release notes for proper font configuration");
            return;
        }
        if (fontConfigFailed) {
            for (n = 0; n < arrfcCompFont.length; ++n) {
                if (arrfcCompFont[n].firstFont != null) continue;
                arrfcCompFont[n].firstFont = fontConfigFont;
            }
        }
        fontConfigFonts = arrfcCompFont;
        if (debugFonts) {
            long l2 = System.nanoTime();
            System.err.println("Time spent accessing fontconfig=" + (l2 - l) / 1000000L + "ms.");
            for (int i = 0; i < fontConfigFonts.length; ++i) {
                FcCompFont fcCompFont = fontConfigFonts[i];
                System.err.println("FC font " + fcCompFont.fcName + " maps to " + fcCompFont.firstFont.fullName + " in file " + fcCompFont.firstFont.fontFile);
                if (fcCompFont.allFonts == null) continue;
                for (int j = 0; j < fcCompFont.allFonts.length; ++j) {
                    FontConfigFont fontConfigFont2 = fcCompFont.allFonts[j];
                    System.err.println(" " + j + ") Family=" + fontConfigFont2.familyName + ", Style=" + fontConfigFont2.styleStr + ", Fullname=" + fontConfigFont2.fullName + ", File=" + fontConfigFont2.fontFile);
                }
            }
        }
    }

    private static native boolean populateMapsNative(HashMap<String, String> var0, HashMap<String, String> var1, HashMap<String, ArrayList<String>> var2, Locale var3);

    public static void populateMaps(HashMap<String, String> hashMap, HashMap<String, String> hashMap2, HashMap<String, ArrayList<String>> hashMap3, Locale locale) {
        boolean bl = false;
        if (useFontConfig && !fontConfigFailed) {
            bl = FontConfigManager.populateMapsNative(hashMap, hashMap2, hashMap3, locale);
        }
        if (fontConfigFailed || useEmbeddedFontSupport || !bl) {
            EmbeddedFontSupport.populateMaps(hashMap, hashMap2, hashMap3, locale);
        }
    }

    private static String mapFxToFcLogicalFamilyName(String string) {
        if (string.equals("serif")) {
            return "serif";
        }
        if (string.equals("monospaced")) {
            return "monospace";
        }
        return "sans";
    }

    public static FcCompFont getFontConfigFont(String string, boolean bl, boolean bl2) {
        int n;
        FontConfigManager.initFontConfigLogFonts();
        if (fontConfigFonts == null) {
            return null;
        }
        String string2 = FontConfigManager.mapFxToFcLogicalFamilyName(string.toLowerCase());
        int n2 = n = bl ? 1 : 0;
        if (bl2) {
            n += 2;
        }
        FcCompFont fcCompFont = null;
        for (int i = 0; i < fontConfigFonts.length; ++i) {
            if (!string2.equals(FontConfigManager.fontConfigFonts[i].fcFamily) || n != FontConfigManager.fontConfigFonts[i].style) continue;
            fcCompFont = fontConfigFonts[i];
            break;
        }
        if (fcCompFont == null) {
            fcCompFont = fontConfigFonts[0];
        }
        if (debugFonts) {
            System.err.println("FC name=" + string2 + " style=" + n + " uses " + fcCompFont.firstFont.fullName + " in file: " + fcCompFont.firstFont.fontFile);
        }
        return fcCompFont;
    }

    public static String getDefaultFontPath() {
        if (fontConfigFonts == null && !fontConfigFailed) {
            FontConfigManager.getFontConfigFont("System", false, false);
        }
        return defaultFontFile;
    }

    public static ArrayList<String> getFileNames(FcCompFont fcCompFont, boolean bl) {
        ArrayList<String> arrayList = new ArrayList<String>();
        if (fcCompFont.allFonts != null) {
            int n;
            for (int i = n = bl ? 1 : 0; i < fcCompFont.allFonts.length; ++i) {
                arrayList.add(fcCompFont.allFonts[i].fontFile);
            }
        }
        return arrayList;
    }

    public static ArrayList<String> getFontNames(FcCompFont fcCompFont, boolean bl) {
        ArrayList<String> arrayList = new ArrayList<String>();
        if (fcCompFont.allFonts != null) {
            int n;
            for (int i = n = bl ? 1 : 0; i < fcCompFont.allFonts.length; ++i) {
                arrayList.add(fcCompFont.allFonts[i].fullName);
            }
        }
        return arrayList;
    }

    static {
        Void void_ = AccessController.doPrivileged(() -> {
            String string = System.getProperty("prism.debugfonts", "");
            debugFonts = "true".equals(string);
            String string2 = System.getProperty("prism.useFontConfig", "true");
            useFontConfig = "true".equals(string2);
            String string3 = System.getProperty("prism.embeddedfonts", "");
            useEmbeddedFontSupport = "true".equals(string3);
            return null;
        });
        fontConfigNames = new String[]{"sans:regular:roman", "sans:bold:roman", "sans:regular:italic", "sans:bold:italic", "serif:regular:roman", "serif:bold:roman", "serif:regular:italic", "serif:bold:italic", "monospace:regular:roman", "monospace:bold:roman", "monospace:regular:italic", "monospace:bold:italic"};
    }

    public static class FcCompFont {
        public String fcName;
        public String fcFamily;
        public int style;
        public FontConfigFont firstFont;
        public FontConfigFont[] allFonts;
    }

    private static class EmbeddedFontSupport {
        private static String fontDirProp = null;
        private static String fontDir;
        private static boolean fontDirFromJRE;
        static String[] jreFontsProperties;

        private EmbeddedFontSupport() {
        }

        private static void initEmbeddedFonts() {
            fontDirProp = System.getProperty("prism.fontdir");
            if (fontDirProp != null) {
                fontDir = fontDirProp;
            } else {
                try {
                    String string = System.getProperty("java.home");
                    if (string == null) {
                        return;
                    }
                    File file = new File(string, "lib/fonts");
                    if (file.exists()) {
                        fontDirFromJRE = true;
                        fontDir = file.getPath();
                    }
                    if (debugFonts) {
                        System.err.println("Fallback fontDir is " + file + " exists = " + file.exists());
                    }
                }
                catch (Exception exception) {
                    if (debugFonts) {
                        exception.printStackTrace();
                    }
                    fontDir = "/";
                }
            }
        }

        private static String getStyleStr(int n) {
            switch (n) {
                case 0: {
                    return "regular";
                }
                case 1: {
                    return "bold";
                }
                case 2: {
                    return "italic";
                }
                case 3: {
                    return "bolditalic";
                }
            }
            return "regular";
        }

        private static boolean exists(File file) {
            return AccessController.doPrivileged(() -> file.exists());
        }

        static void initLogicalFonts(FcCompFont[] arrfcCompFont) {
            Object object;
            Properties properties;
            block11: {
                properties = new Properties();
                try {
                    File file = new File(fontDir, "logicalfonts.properties");
                    if (file.exists()) {
                        object = new FileInputStream(file);
                        properties.load((InputStream)object);
                        ((FileInputStream)object).close();
                    } else if (fontDirFromJRE) {
                        for (int i = 0; i < jreFontsProperties.length; i += 2) {
                            properties.setProperty(jreFontsProperties[i], jreFontsProperties[i + 1]);
                        }
                        if (debugFonts) {
                            System.err.println("Using fallback implied logicalfonts.properties");
                        }
                    }
                }
                catch (IOException iOException) {
                    if (!debugFonts) break block11;
                    System.err.println(iOException);
                    return;
                }
            }
            for (int i = 0; i < arrfcCompFont.length; ++i) {
                object = arrfcCompFont[i].fcFamily;
                String string = EmbeddedFontSupport.getStyleStr(arrfcCompFont[i].style);
                String string2 = (String)object + "." + string + ".";
                ArrayList<FontConfigFont> arrayList = new ArrayList<FontConfigFont>();
                int n = 0;
                while (true) {
                    String string3 = properties.getProperty(string2 + n + ".file");
                    String string4 = properties.getProperty(string2 + n + ".font");
                    ++n;
                    if (string3 == null) break;
                    File file = new File(fontDir, string3);
                    if (!EmbeddedFontSupport.exists(file)) {
                        if (!debugFonts) continue;
                        System.out.println("Failed to find logical font file " + file);
                        continue;
                    }
                    FontConfigFont fontConfigFont = new FontConfigFont();
                    fontConfigFont.fontFile = file.getPath();
                    fontConfigFont.fullName = string4;
                    fontConfigFont.familyName = null;
                    fontConfigFont.styleStr = null;
                    if (arrfcCompFont[i].firstFont == null) {
                        arrfcCompFont[i].firstFont = fontConfigFont;
                    }
                    arrayList.add(fontConfigFont);
                }
                if (arrayList.size() <= 0) continue;
                arrfcCompFont[i].allFonts = new FontConfigFont[arrayList.size()];
                arrayList.toArray(arrfcCompFont[i].allFonts);
            }
        }

        static void populateMaps(HashMap<String, String> hashMap, HashMap<String, String> hashMap2, HashMap<String, ArrayList<String>> hashMap3, Locale locale) {
            Properties properties = new Properties();
            Void void_ = AccessController.doPrivileged(() -> {
                block2: {
                    try {
                        String string = fontDir + "/allfonts.properties";
                        FileInputStream fileInputStream = new FileInputStream(string);
                        properties.load(fileInputStream);
                        fileInputStream.close();
                    }
                    catch (IOException iOException) {
                        properties.clear();
                        if (!debugFonts) break block2;
                        System.err.println(iOException);
                        System.err.println("Fall back to opening the files");
                    }
                }
                return null;
            });
            if (!properties.isEmpty()) {
                int n = Integer.MAX_VALUE;
                try {
                    n = Integer.parseInt(properties.getProperty("maxFont", ""));
                }
                catch (NumberFormatException numberFormatException) {
                    // empty catch block
                }
                if (n <= 0) {
                    n = Integer.MAX_VALUE;
                }
                for (int i = 0; i < n; ++i) {
                    String string = properties.getProperty("family." + i);
                    String string2 = properties.getProperty("font." + i);
                    String string3 = properties.getProperty("file." + i);
                    if (string3 == null) break;
                    File file = new File(fontDir, string3);
                    if (!EmbeddedFontSupport.exists(file) || string == null || string2 == null) continue;
                    String string4 = string2.toLowerCase(Locale.ENGLISH);
                    String string5 = string.toLowerCase(Locale.ENGLISH);
                    hashMap.put(string4, file.getPath());
                    hashMap2.put(string4, string);
                    ArrayList<String> arrayList = hashMap3.get(string5);
                    if (arrayList == null) {
                        arrayList = new ArrayList(4);
                        hashMap3.put(string5, arrayList);
                    }
                    arrayList.add(string2);
                }
            }
        }

        static {
            fontDirFromJRE = false;
            Void void_ = AccessController.doPrivileged(() -> {
                EmbeddedFontSupport.initEmbeddedFonts();
                return null;
            });
            jreFontsProperties = new String[]{"sans.regular.0.font", "Lucida Sans Regular", "sans.regular.0.file", "LucidaSansRegular.ttf", "sans.bold.0.font", "Lucida Sans Bold", "sans.bold.0.file", "LucidaSansDemiBold.ttf", "monospace.regular.0.font", "Lucida Typewriter Regular", "monospace.regular.0.file", "LucidaTypewriterRegular.ttf", "monospace.bold.0.font", "Lucida Typewriter Bold", "monospace.bold.0.file", "LucidaTypewriterBold.ttf", "serif.regular.0.font", "Lucida Bright", "serif.regular.0.file", "LucidaBrightRegular.ttf", "serif.bold.0.font", "Lucida Bright Demibold", "serif.bold.0.file", "LucidaBrightDemiBold.ttf", "serif.italic.0.font", "Lucida Bright Italic", "serif.italic.0.file", "LucidaBrightItalic.ttf", "serif.bolditalic.0.font", "Lucida Bright Demibold Italic", "serif.bolditalic.0.file", "LucidaBrightDemiItalic.ttf"};
        }
    }

    public static class FontConfigFont {
        public String familyName;
        public String styleStr;
        public String fullName;
        public String fontFile;
    }
}

