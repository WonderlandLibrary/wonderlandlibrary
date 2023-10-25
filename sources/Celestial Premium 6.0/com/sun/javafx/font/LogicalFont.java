/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.javafx.font.CharToGlyphMapper;
import com.sun.javafx.font.CompositeFontResource;
import com.sun.javafx.font.CompositeGlyphMapper;
import com.sun.javafx.font.CompositeStrike;
import com.sun.javafx.font.Disposer;
import com.sun.javafx.font.FontConfigManager;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.FontStrikeDesc;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.font.PrismFont;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.geom.transform.BaseTransform;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LogicalFont
implements CompositeFontResource {
    public static final String SYSTEM = "System";
    public static final String SERIF = "Serif";
    public static final String SANS_SERIF = "SansSerif";
    public static final String MONOSPACED = "Monospaced";
    public static final String STYLE_REGULAR = "Regular";
    public static final String STYLE_BOLD = "Bold";
    public static final String STYLE_ITALIC = "Italic";
    public static final String STYLE_BOLD_ITALIC = "Bold Italic";
    static final HashMap<String, String> canonicalFamilyMap = new HashMap();
    static LogicalFont[] logicalFonts;
    boolean isBold;
    boolean isItalic;
    private String fullName;
    private String familyName;
    private String styleName;
    private String physicalFamily;
    private String physicalFullName;
    private String physicalFileName;
    private FontResource slot0FontResource;
    private ArrayList<String> linkedFontFiles;
    private ArrayList<String> linkedFontNames;
    private FontResource[] fallbacks;
    private FontResource[] nativeFallbacks;
    CompositeGlyphMapper mapper;
    Map<FontStrikeDesc, WeakReference<FontStrike>> strikeMap = new ConcurrentHashMap<FontStrikeDesc, WeakReference<FontStrike>>();
    private static final int SANS_SERIF_INDEX = 0;
    private static final int SERIF_INDEX = 1;
    private static final int MONOSPACED_INDEX = 2;
    private static final int SYSTEM_INDEX = 3;
    static String[][] logFamilies;
    private int hash;

    static boolean isLogicalFont(String string) {
        int n = string.indexOf(32);
        if (n != -1) {
            string = string.substring(0, n);
        }
        return canonicalFamilyMap.get(string) != null;
    }

    private static String getCanonicalFamilyName(String string) {
        if (string == null) {
            return SANS_SERIF;
        }
        String string2 = string.toLowerCase();
        return canonicalFamilyMap.get(string2);
    }

    static PGFont getLogicalFont(String string, boolean bl, boolean bl2, float f) {
        LogicalFont logicalFont;
        String string2 = LogicalFont.getCanonicalFamilyName(string);
        if (string2 == null) {
            return null;
        }
        int n = 0;
        n = string2.equals(SANS_SERIF) ? 0 : (string2.equals(SERIF) ? 4 : (string2.equals(MONOSPACED) ? 8 : 12));
        if (bl) {
            ++n;
        }
        if (bl2) {
            n += 2;
        }
        if ((logicalFont = logicalFonts[n]) == null) {
            LogicalFont.logicalFonts[n] = logicalFont = new LogicalFont(string2, bl, bl2);
        }
        return new PrismFont(logicalFont, logicalFont.getFullName(), f);
    }

    static PGFont getLogicalFont(String string, float f) {
        int n = string.indexOf(32);
        if (n == -1 || n == string.length() - 1) {
            return null;
        }
        String string2 = string.substring(0, n);
        String string3 = LogicalFont.getCanonicalFamilyName(string2);
        if (string3 == null) {
            return null;
        }
        String string4 = string.substring(n + 1).toLowerCase();
        boolean bl = false;
        boolean bl2 = false;
        if (!string4.equals("regular")) {
            if (string4.equals("bold")) {
                bl = true;
            } else if (string4.equals("italic")) {
                bl2 = true;
            } else if (string4.equals("bold italic")) {
                bl = true;
                bl2 = true;
            } else {
                return null;
            }
        }
        return LogicalFont.getLogicalFont(string3, bl, bl2, f);
    }

    private LogicalFont(String string, boolean bl, boolean bl2) {
        this.familyName = string;
        this.isBold = bl;
        this.isItalic = bl2;
        this.styleName = !bl && !bl2 ? STYLE_REGULAR : (bl && !bl2 ? STYLE_BOLD : (!bl && bl2 ? STYLE_ITALIC : STYLE_BOLD_ITALIC));
        this.fullName = this.familyName + " " + this.styleName;
        if (PrismFontFactory.isLinux) {
            FontConfigManager.FcCompFont fcCompFont = FontConfigManager.getFontConfigFont(string, bl, bl2);
            this.physicalFullName = fcCompFont.firstFont.fullName;
            this.physicalFileName = fcCompFont.firstFont.fontFile;
        } else {
            this.physicalFamily = PrismFontFactory.getSystemFont(this.familyName);
        }
    }

    private FontResource getSlot0Resource() {
        if (this.slot0FontResource == null) {
            PrismFontFactory prismFontFactory = PrismFontFactory.getFontFactory();
            this.slot0FontResource = this.physicalFamily != null ? prismFontFactory.getFontResource(this.physicalFamily, this.isBold, this.isItalic, false) : prismFontFactory.getFontResource(this.physicalFullName, this.physicalFileName, false);
            if (this.slot0FontResource == null) {
                this.slot0FontResource = prismFontFactory.getDefaultFontResource(false);
            }
        }
        return this.slot0FontResource;
    }

    private void getLinkedFonts() {
        if (this.fallbacks == null) {
            if (PrismFontFactory.isLinux) {
                FontConfigManager.FcCompFont fcCompFont = FontConfigManager.getFontConfigFont(this.familyName, this.isBold, this.isItalic);
                this.linkedFontFiles = FontConfigManager.getFileNames(fcCompFont, true);
                this.linkedFontNames = FontConfigManager.getFontNames(fcCompFont, true);
            } else {
                ArrayList<String>[] arrarrayList = PrismFontFactory.getLinkedFonts("Tahoma", true);
                this.linkedFontFiles = arrarrayList[0];
                this.linkedFontNames = arrarrayList[1];
            }
            this.fallbacks = new FontResource[this.linkedFontFiles.size()];
        }
    }

    @Override
    public int getNumSlots() {
        this.getLinkedFonts();
        int n = this.linkedFontFiles.size();
        if (this.nativeFallbacks != null) {
            n += this.nativeFallbacks.length;
        }
        return n + 1;
    }

    @Override
    public int getSlotForFont(String string) {
        FontResource[] arrfontResource;
        this.getLinkedFonts();
        int n = 1;
        for (String string2 : this.linkedFontNames) {
            if (string.equalsIgnoreCase(string2)) {
                return n;
            }
            ++n;
        }
        if (this.nativeFallbacks != null) {
            for (FontResource fontResource : this.nativeFallbacks) {
                if (string.equalsIgnoreCase(fontResource.getFullName())) {
                    return n;
                }
                ++n;
            }
        }
        if (n >= 126) {
            if (PrismFontFactory.debugFonts) {
                System.err.println("\tToo many font fallbacks!");
            }
            return -1;
        }
        PrismFontFactory prismFontFactory = PrismFontFactory.getFontFactory();
        FontResource fontResource = prismFontFactory.getFontResource(string, null, false);
        if (fontResource == null) {
            if (PrismFontFactory.debugFonts) {
                System.err.println("\t Font name not supported \"" + string + "\".");
            }
            return -1;
        }
        if (this.nativeFallbacks == null) {
            arrfontResource = new FontResource[1];
        } else {
            arrfontResource = new FontResource[this.nativeFallbacks.length + 1];
            System.arraycopy(this.nativeFallbacks, 0, arrfontResource, 0, this.nativeFallbacks.length);
        }
        arrfontResource[arrfontResource.length - 1] = fontResource;
        this.nativeFallbacks = arrfontResource;
        return n;
    }

    @Override
    public FontResource getSlotResource(int n) {
        if (n == 0) {
            return this.getSlot0Resource();
        }
        this.getLinkedFonts();
        if (--n >= this.fallbacks.length) {
            if (this.nativeFallbacks == null || (n -= this.fallbacks.length) >= this.nativeFallbacks.length) {
                return null;
            }
            return this.nativeFallbacks[n];
        }
        if (this.fallbacks[n] == null) {
            String string = this.linkedFontFiles.get(n);
            String string2 = this.linkedFontNames.get(n);
            this.fallbacks[n] = PrismFontFactory.getFontFactory().getFontResource(string2, string, false);
            if (this.fallbacks[n] == null) {
                this.fallbacks[n] = this.getSlot0Resource();
            }
        }
        return this.fallbacks[n];
    }

    @Override
    public String getFullName() {
        return this.fullName;
    }

    @Override
    public String getPSName() {
        return this.fullName;
    }

    @Override
    public String getFamilyName() {
        return this.familyName;
    }

    @Override
    public String getStyleName() {
        return this.styleName;
    }

    @Override
    public String getLocaleFullName() {
        return this.fullName;
    }

    @Override
    public String getLocaleFamilyName() {
        return this.familyName;
    }

    @Override
    public String getLocaleStyleName() {
        return this.styleName;
    }

    @Override
    public boolean isBold() {
        return this.getSlotResource(0).isBold();
    }

    @Override
    public boolean isItalic() {
        return this.getSlotResource(0).isItalic();
    }

    @Override
    public String getFileName() {
        return this.getSlotResource(0).getFileName();
    }

    @Override
    public int getFeatures() {
        return this.getSlotResource(0).getFeatures();
    }

    @Override
    public Object getPeer() {
        return null;
    }

    @Override
    public boolean isEmbeddedFont() {
        return this.getSlotResource(0).isEmbeddedFont();
    }

    @Override
    public void setPeer(Object object) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public float[] getGlyphBoundingBox(int n, float f, float[] arrf) {
        int n2 = n >>> 24;
        int n3 = n & 0xFFFFFF;
        FontResource fontResource = this.getSlotResource(n2);
        return fontResource.getGlyphBoundingBox(n3, f, arrf);
    }

    @Override
    public float getAdvance(int n, float f) {
        int n2 = n >>> 24;
        int n3 = n & 0xFFFFFF;
        FontResource fontResource = this.getSlotResource(n2);
        return fontResource.getAdvance(n3, f);
    }

    @Override
    public CharToGlyphMapper getGlyphMapper() {
        if (this.mapper == null) {
            this.mapper = new CompositeGlyphMapper(this);
        }
        return this.mapper;
    }

    @Override
    public Map<FontStrikeDesc, WeakReference<FontStrike>> getStrikeMap() {
        return this.strikeMap;
    }

    @Override
    public int getDefaultAAMode() {
        return this.getSlot0Resource().getDefaultAAMode();
    }

    @Override
    public FontStrike getStrike(float f, BaseTransform baseTransform) {
        return this.getStrike(f, baseTransform, this.getDefaultAAMode());
    }

    @Override
    public FontStrike getStrike(float f, BaseTransform baseTransform, int n) {
        FontStrikeDesc fontStrikeDesc = new FontStrikeDesc(f, baseTransform, n);
        WeakReference<CompositeStrike> weakReference = this.strikeMap.get(fontStrikeDesc);
        CompositeStrike compositeStrike = null;
        if (weakReference != null) {
            compositeStrike = (CompositeStrike)weakReference.get();
        }
        if (compositeStrike == null) {
            compositeStrike = new CompositeStrike(this, f, baseTransform, n, fontStrikeDesc);
            weakReference = compositeStrike.disposer != null ? Disposer.addRecord(compositeStrike, compositeStrike.disposer) : new WeakReference<CompositeStrike>(compositeStrike);
            this.strikeMap.put(fontStrikeDesc, weakReference);
        }
        return compositeStrike;
    }

    private static void buildFamily(String[] arrstring, String string) {
        arrstring[0] = string + " Regular";
        arrstring[1] = string + " Bold";
        arrstring[2] = string + " Italic";
        arrstring[3] = string + " Bold Italic";
    }

    private static void buildFamilies() {
        if (logFamilies == null) {
            String[][] arrstring = new String[4][4];
            LogicalFont.buildFamily(arrstring[0], SANS_SERIF);
            LogicalFont.buildFamily(arrstring[1], SERIF);
            LogicalFont.buildFamily(arrstring[2], MONOSPACED);
            LogicalFont.buildFamily(arrstring[3], SYSTEM);
            logFamilies = arrstring;
        }
    }

    static void addFamilies(ArrayList<String> arrayList) {
        arrayList.add(SANS_SERIF);
        arrayList.add(SERIF);
        arrayList.add(MONOSPACED);
        arrayList.add(SYSTEM);
    }

    static void addFullNames(ArrayList<String> arrayList) {
        LogicalFont.buildFamilies();
        for (int i = 0; i < logFamilies.length; ++i) {
            for (int j = 0; j < logFamilies[i].length; ++j) {
                arrayList.add(logFamilies[i][j]);
            }
        }
    }

    static String[] getFontsInFamily(String string) {
        String string2 = LogicalFont.getCanonicalFamilyName(string);
        if (string2 == null) {
            return null;
        }
        LogicalFont.buildFamilies();
        if (string2.equals(SANS_SERIF)) {
            return logFamilies[0];
        }
        if (string2.equals(SERIF)) {
            return logFamilies[1];
        }
        if (string2.equals(MONOSPACED)) {
            return logFamilies[2];
        }
        return logFamilies[3];
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof LogicalFont)) {
            return false;
        }
        LogicalFont logicalFont = (LogicalFont)object;
        return this.fullName.equals(logicalFont.fullName);
    }

    public int hashCode() {
        if (this.hash != 0) {
            return this.hash;
        }
        this.hash = this.fullName.hashCode();
        return this.hash;
    }

    static {
        canonicalFamilyMap.put("system", SYSTEM);
        canonicalFamilyMap.put("serif", SERIF);
        canonicalFamilyMap.put("sansserif", SANS_SERIF);
        canonicalFamilyMap.put("sans-serif", SANS_SERIF);
        canonicalFamilyMap.put("dialog", SANS_SERIF);
        canonicalFamilyMap.put("default", SANS_SERIF);
        canonicalFamilyMap.put("monospaced", MONOSPACED);
        canonicalFamilyMap.put("monospace", MONOSPACED);
        canonicalFamilyMap.put("dialoginput", MONOSPACED);
        logicalFonts = new LogicalFont[16];
        logFamilies = null;
    }
}

