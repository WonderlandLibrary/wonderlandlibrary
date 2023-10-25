/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.javafx.font.PrismFontFactory;
import java.util.HashMap;

class WindowsFontMap {
    static HashMap<String, FamilyDescription> platformFontMap;

    WindowsFontMap() {
    }

    static HashMap<String, FamilyDescription> populateHardcodedFileNameMap() {
        HashMap<String, FamilyDescription> hashMap = new HashMap<String, FamilyDescription>();
        FamilyDescription familyDescription = new FamilyDescription();
        familyDescription.familyName = "Segoe UI";
        familyDescription.plainFullName = "Segoe UI";
        familyDescription.plainFileName = "segoeui.ttf";
        familyDescription.boldFullName = "Segoe UI Bold";
        familyDescription.boldFileName = "segoeuib.ttf";
        familyDescription.italicFullName = "Segoe UI Italic";
        familyDescription.italicFileName = "segoeuii.ttf";
        familyDescription.boldItalicFullName = "Segoe UI Bold Italic";
        familyDescription.boldItalicFileName = "segoeuiz.ttf";
        hashMap.put("segoe", familyDescription);
        familyDescription = new FamilyDescription();
        familyDescription.familyName = "Tahoma";
        familyDescription.plainFullName = "Tahoma";
        familyDescription.plainFileName = "tahoma.ttf";
        familyDescription.boldFullName = "Tahoma Bold";
        familyDescription.boldFileName = "tahomabd.ttf";
        hashMap.put("tahoma", familyDescription);
        familyDescription = new FamilyDescription();
        familyDescription.familyName = "Verdana";
        familyDescription.plainFullName = "Verdana";
        familyDescription.plainFileName = "verdana.TTF";
        familyDescription.boldFullName = "Verdana Bold";
        familyDescription.boldFileName = "verdanab.TTF";
        familyDescription.italicFullName = "Verdana Italic";
        familyDescription.italicFileName = "verdanai.TTF";
        familyDescription.boldItalicFullName = "Verdana Bold Italic";
        familyDescription.boldItalicFileName = "verdanaz.TTF";
        hashMap.put("verdana", familyDescription);
        familyDescription = new FamilyDescription();
        familyDescription.familyName = "Arial";
        familyDescription.plainFullName = "Arial";
        familyDescription.plainFileName = "ARIAL.TTF";
        familyDescription.boldFullName = "Arial Bold";
        familyDescription.boldFileName = "ARIALBD.TTF";
        familyDescription.italicFullName = "Arial Italic";
        familyDescription.italicFileName = "ARIALI.TTF";
        familyDescription.boldItalicFullName = "Arial Bold Italic";
        familyDescription.boldItalicFileName = "ARIALBI.TTF";
        hashMap.put("arial", familyDescription);
        familyDescription = new FamilyDescription();
        familyDescription.familyName = "Times New Roman";
        familyDescription.plainFullName = "Times New Roman";
        familyDescription.plainFileName = "times.ttf";
        familyDescription.boldFullName = "Times New Roman Bold";
        familyDescription.boldFileName = "timesbd.ttf";
        familyDescription.italicFullName = "Times New Roman Italic";
        familyDescription.italicFileName = "timesi.ttf";
        familyDescription.boldItalicFullName = "Times New Roman Bold Italic";
        familyDescription.boldItalicFileName = "timesbi.ttf";
        hashMap.put("times", familyDescription);
        familyDescription = new FamilyDescription();
        familyDescription.familyName = "Courier New";
        familyDescription.plainFullName = "Courier New";
        familyDescription.plainFileName = "cour.ttf";
        familyDescription.boldFullName = "Courier New Bold";
        familyDescription.boldFileName = "courbd.ttf";
        familyDescription.italicFullName = "Courier New Italic";
        familyDescription.italicFileName = "couri.ttf";
        familyDescription.boldItalicFullName = "Courier New Bold Italic";
        familyDescription.boldItalicFileName = "courbi.ttf";
        hashMap.put("courier", familyDescription);
        return hashMap;
    }

    static String getPathName(String string) {
        return PrismFontFactory.getPathNameWindows(string);
    }

    static String findFontFile(String string, int n) {
        FamilyDescription familyDescription;
        if (platformFontMap == null) {
            platformFontMap = WindowsFontMap.populateHardcodedFileNameMap();
        }
        if (platformFontMap == null || platformFontMap.size() == 0) {
            return null;
        }
        int n2 = string.indexOf(32);
        String string2 = string;
        if (n2 > 0) {
            string2 = string.substring(0, n2);
        }
        if ((familyDescription = platformFontMap.get(string2)) == null) {
            return null;
        }
        String string3 = null;
        if (n < 0) {
            if (string.equalsIgnoreCase(familyDescription.plainFullName)) {
                string3 = familyDescription.plainFileName;
            } else if (string.equalsIgnoreCase(familyDescription.boldFullName)) {
                string3 = familyDescription.boldFileName;
            } else if (string.equalsIgnoreCase(familyDescription.italicFullName)) {
                string3 = familyDescription.italicFileName;
            } else if (string.equalsIgnoreCase(familyDescription.boldItalicFullName)) {
                string3 = familyDescription.boldItalicFileName;
            }
            if (string3 != null) {
                return WindowsFontMap.getPathName(string3);
            }
            return null;
        }
        if (!string.equalsIgnoreCase(familyDescription.familyName)) {
            return null;
        }
        switch (n) {
            case 0: {
                string3 = familyDescription.plainFileName;
                break;
            }
            case 1: {
                string3 = familyDescription.boldFileName;
                if (string3 != null) break;
                string3 = familyDescription.plainFileName;
                break;
            }
            case 2: {
                string3 = familyDescription.italicFileName;
                if (string3 != null) break;
                string3 = familyDescription.plainFileName;
                break;
            }
            case 3: {
                string3 = familyDescription.boldItalicFileName;
                if (string3 == null) {
                    string3 = familyDescription.italicFileName;
                }
                if (string3 == null) {
                    string3 = familyDescription.boldFileName;
                }
                if (string3 != null) break;
                string3 = familyDescription.plainFileName;
            }
        }
        if (string3 != null) {
            return WindowsFontMap.getPathName(string3);
        }
        return null;
    }

    static class FamilyDescription {
        public String familyName;
        public String plainFullName;
        public String boldFullName;
        public String italicFullName;
        public String boldItalicFullName;
        public String plainFileName;
        public String boldFileName;
        public String italicFileName;
        public String boldItalicFileName;

        FamilyDescription() {
        }
    }
}

