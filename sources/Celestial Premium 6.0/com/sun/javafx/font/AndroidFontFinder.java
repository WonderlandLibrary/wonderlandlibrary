/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.glass.utils.NativeLibLoader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.CallSite;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class AndroidFontFinder {
    private static final String SYSTEM_FONT_NAME = "sans serif";
    private static final float SYSTEM_FONT_SIZE = 16.0f;
    static final String fontDescriptor_2_X_Path = "/com/sun/javafx/font/android_system_fonts.xml";
    static final String fontDescriptor_4_X_Path = "/system/etc/system_fonts.xml";
    static final String systemFontsDir = "/system/fonts";

    AndroidFontFinder() {
    }

    public static String getSystemFont() {
        return SYSTEM_FONT_NAME;
    }

    public static float getSystemFontSize() {
        return 16.0f;
    }

    public static String getSystemFontsDir() {
        return systemFontsDir;
    }

    private static boolean parse_2_X_SystemDefaultFonts(HashMap<String, String> hashMap, HashMap<String, String> hashMap2, HashMap<String, ArrayList<String>> hashMap3) {
        InputStream inputStream = AndroidFontFinder.class.getResourceAsStream(fontDescriptor_2_X_Path);
        if (inputStream == null) {
            System.err.println("Resource not found: /com/sun/javafx/font/android_system_fonts.xml");
            return false;
        }
        return AndroidFontFinder.parseSystemDefaultFonts(inputStream, hashMap, hashMap2, hashMap3);
    }

    private static boolean parse_4_X_SystemDefaultFonts(HashMap<String, String> hashMap, HashMap<String, String> hashMap2, HashMap<String, ArrayList<String>> hashMap3) {
        File file = new File(fontDescriptor_4_X_Path);
        try {
            return AndroidFontFinder.parseSystemDefaultFonts(new FileInputStream(file), hashMap, hashMap2, hashMap3);
        }
        catch (FileNotFoundException fileNotFoundException) {
            System.err.println("File not found: /system/etc/system_fonts.xml");
            return false;
        }
    }

    private static boolean parseSystemDefaultFonts(InputStream inputStream, final HashMap<String, String> hashMap, final HashMap<String, String> hashMap2, final HashMap<String, ArrayList<String>> hashMap3) {
        try {
            SAXParserFactory sAXParserFactory = SAXParserFactory.newInstance();
            SAXParser sAXParser = sAXParserFactory.newSAXParser();
            DefaultHandler defaultHandler = new DefaultHandler(){
                private static final char DASH = '-';
                private static final String FAMILY = "family";
                private static final String FILE = "file";
                private static final String FILESET = "fileset";
                private static final String NAME = "name";
                private static final String NAMESET = "nameset";
                private static final char SPACE = ' ';
                final List<String> filesets = new ArrayList<String>();
                boolean inFamily = false;
                boolean inFile = false;
                boolean inFileset = false;
                boolean inName = false;
                boolean inNameset = false;
                private final List<String> namesets = new ArrayList<String>();
                private final String[] styles = new String[]{"regular", "bold", "italic", "bold italic"};

                @Override
                public void characters(char[] arrc, int n, int n2) throws SAXException {
                    if (this.inName) {
                        String string = new String(arrc, n, n2).toLowerCase();
                        this.namesets.add(string);
                    } else if (this.inFile) {
                        String string = new String(arrc, n, n2);
                        this.filesets.add(string);
                    }
                }

                @Override
                public void endElement(String string, String string2, String string3) throws SAXException {
                    if (string3.equalsIgnoreCase(FAMILY)) {
                        for (String string4 : this.namesets) {
                            int n = 0;
                            String string5 = string4.replace('-', ' ');
                            for (String string6 : this.filesets) {
                                String string7 = string5 + " " + this.styles[n];
                                String string8 = AndroidFontFinder.systemFontsDir + File.separator + string6;
                                File file = new File(string8);
                                if (!file.exists() || !file.canRead()) continue;
                                hashMap.put(string7, string8);
                                hashMap2.put(string7, string5);
                                ArrayList<CallSite> arrayList = (ArrayList<CallSite>)hashMap3.get(string5);
                                if (arrayList == null) {
                                    arrayList = new ArrayList<CallSite>();
                                    hashMap3.put(string5, arrayList);
                                }
                                arrayList.add((CallSite)((Object)string7));
                                ++n;
                            }
                        }
                        this.inFamily = false;
                    } else if (string3.equalsIgnoreCase(NAMESET)) {
                        this.inNameset = false;
                    } else if (string3.equalsIgnoreCase(FILESET)) {
                        this.inFileset = false;
                    } else if (string3.equalsIgnoreCase(NAME)) {
                        this.inName = false;
                    } else if (string3.equalsIgnoreCase(FILE)) {
                        this.inFile = false;
                    }
                }

                @Override
                public void startElement(String string, String string2, String string3, Attributes attributes) throws SAXException {
                    if (string3.equalsIgnoreCase(FAMILY)) {
                        this.inFamily = true;
                        this.namesets.clear();
                        this.filesets.clear();
                    } else if (string3.equalsIgnoreCase(NAMESET)) {
                        this.inNameset = true;
                    } else if (string3.equalsIgnoreCase(FILESET)) {
                        this.inFileset = true;
                    } else if (string3.equalsIgnoreCase(NAME)) {
                        this.inName = true;
                    } else if (string3.equalsIgnoreCase(FILE)) {
                        this.inFile = true;
                    }
                }
            };
            sAXParser.parse(inputStream, defaultHandler);
            return true;
        }
        catch (IOException iOException) {
            System.err.println("Failed to load default fonts descriptor: /system/etc/system_fonts.xml");
        }
        catch (Exception exception) {
            System.err.println("Failed parsing default fonts descriptor;");
            exception.printStackTrace();
        }
        return false;
    }

    public static void populateFontFileNameMap(HashMap<String, String> hashMap, HashMap<String, String> hashMap2, HashMap<String, ArrayList<String>> hashMap3, Locale locale) {
        boolean bl;
        if (hashMap == null || hashMap2 == null || hashMap3 == null) {
            return;
        }
        if (locale == null) {
            locale = Locale.ENGLISH;
        }
        if (!(bl = AndroidFontFinder.parse_4_X_SystemDefaultFonts(hashMap, hashMap2, hashMap3))) {
            AndroidFontFinder.parse_2_X_SystemDefaultFonts(hashMap, hashMap2, hashMap3);
        }
    }

    static {
        Void void_ = AccessController.doPrivileged(() -> {
            NativeLibLoader.loadLibrary("javafx_font");
            return null;
        });
    }
}

