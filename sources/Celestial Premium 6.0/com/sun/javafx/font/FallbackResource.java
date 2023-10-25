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
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.geom.transform.BaseTransform;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class FallbackResource
implements CompositeFontResource {
    private ArrayList<String> linkedFontFiles;
    private ArrayList<String> linkedFontNames;
    private FontResource[] fallbacks;
    private FontResource[] nativeFallbacks;
    private boolean isBold;
    private boolean isItalic;
    private int aaMode;
    private CompositeGlyphMapper mapper;
    Map<FontStrikeDesc, WeakReference<FontStrike>> strikeMap = new ConcurrentHashMap<FontStrikeDesc, WeakReference<FontStrike>>();
    static FallbackResource[] greyFallBackResource = new FallbackResource[4];
    static FallbackResource[] lcdFallBackResource = new FallbackResource[4];

    @Override
    public Map<FontStrikeDesc, WeakReference<FontStrike>> getStrikeMap() {
        return this.strikeMap;
    }

    FallbackResource(boolean bl, boolean bl2, int n) {
        this.isBold = bl;
        this.isItalic = bl2;
        this.aaMode = n;
    }

    static FallbackResource getFallbackResource(boolean bl, boolean bl2, int n) {
        FallbackResource fallbackResource;
        int n2;
        FallbackResource[] arrfallbackResource = n == 0 ? greyFallBackResource : lcdFallBackResource;
        int n3 = n2 = bl ? 1 : 0;
        if (bl2) {
            n2 += 2;
        }
        if ((fallbackResource = arrfallbackResource[n2]) == null) {
            arrfallbackResource[n2] = fallbackResource = new FallbackResource(bl, bl2, n);
        }
        return fallbackResource;
    }

    @Override
    public int getDefaultAAMode() {
        return this.aaMode;
    }

    private String throwException() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public String getFullName() {
        return this.throwException();
    }

    @Override
    public String getPSName() {
        return this.throwException();
    }

    @Override
    public String getFamilyName() {
        return this.throwException();
    }

    @Override
    public String getStyleName() {
        return this.throwException();
    }

    @Override
    public String getLocaleFullName() {
        return this.throwException();
    }

    @Override
    public String getLocaleFamilyName() {
        return this.throwException();
    }

    @Override
    public String getLocaleStyleName() {
        return this.throwException();
    }

    @Override
    public boolean isBold() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public boolean isItalic() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public int getFeatures() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public String getFileName() {
        return this.throwException();
    }

    @Override
    public Object getPeer() {
        return null;
    }

    @Override
    public void setPeer(Object object) {
        this.throwException();
    }

    @Override
    public boolean isEmbeddedFont() {
        return false;
    }

    @Override
    public CharToGlyphMapper getGlyphMapper() {
        if (this.mapper == null) {
            this.mapper = new CompositeGlyphMapper(this);
        }
        return this.mapper;
    }

    @Override
    public int getSlotForFont(String string) {
        FontResource[] arrfontResource;
        this.getLinkedFonts();
        int n = 0;
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

    private void getLinkedFonts() {
        if (this.fallbacks == null) {
            if (PrismFontFactory.isLinux) {
                FontConfigManager.FcCompFont fcCompFont = FontConfigManager.getFontConfigFont("sans", this.isBold, this.isItalic);
                this.linkedFontFiles = FontConfigManager.getFileNames(fcCompFont, false);
                this.linkedFontNames = FontConfigManager.getFontNames(fcCompFont, false);
                this.fallbacks = new FontResource[this.linkedFontFiles.size()];
            } else {
                ArrayList<String>[] arrarrayList = PrismFontFactory.isMacOSX ? PrismFontFactory.getLinkedFonts("Arial Unicode MS", true) : PrismFontFactory.getLinkedFonts("Tahoma", true);
                this.linkedFontFiles = arrarrayList[0];
                this.linkedFontNames = arrarrayList[1];
                this.fallbacks = new FontResource[this.linkedFontFiles.size()];
            }
        }
    }

    @Override
    public int getNumSlots() {
        this.getLinkedFonts();
        int n = this.linkedFontFiles.size();
        if (this.nativeFallbacks != null) {
            n += this.nativeFallbacks.length;
        }
        return n;
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
    public synchronized FontResource getSlotResource(int n) {
        this.getLinkedFonts();
        if (n >= this.fallbacks.length) {
            if (this.nativeFallbacks == null || (n -= this.fallbacks.length) >= this.nativeFallbacks.length) {
                return null;
            }
            return this.nativeFallbacks[n];
        }
        if (this.fallbacks[n] == null) {
            String string = this.linkedFontFiles.get(n);
            String string2 = this.linkedFontNames.get(n);
            this.fallbacks[n] = PrismFontFactory.getFontFactory().getFontResource(string2, string, false);
        }
        return this.fallbacks[n];
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
}

