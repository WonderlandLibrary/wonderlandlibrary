/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.javafx.font.CharToGlyphMapper;
import com.sun.javafx.font.CompositeFontResource;
import com.sun.javafx.font.CompositeGlyphMapper;
import com.sun.javafx.font.CompositeStrike;
import com.sun.javafx.font.Disposer;
import com.sun.javafx.font.FallbackResource;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.FontStrikeDesc;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.PrismFontFile;
import com.sun.javafx.geom.transform.BaseTransform;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class PrismCompositeFontResource
implements CompositeFontResource {
    private FontResource primaryResource;
    private FallbackResource fallbackResource;
    CompositeGlyphMapper mapper;
    Map<FontStrikeDesc, WeakReference<FontStrike>> strikeMap = new ConcurrentHashMap<FontStrikeDesc, WeakReference<FontStrike>>();

    PrismCompositeFontResource(FontResource fontResource, String string) {
        if (!(fontResource instanceof PrismFontFile)) {
            Thread.dumpStack();
            throw new IllegalStateException("wrong resource type");
        }
        if (string != null) {
            PrismFontFactory prismFontFactory = PrismFontFactory.getFontFactory();
            prismFontFactory.compResourceMap.put(string, this);
        }
        this.primaryResource = fontResource;
        int n = fontResource.getDefaultAAMode();
        boolean bl = fontResource.isBold();
        boolean bl2 = fontResource.isItalic();
        this.fallbackResource = FallbackResource.getFallbackResource(bl, bl2, n);
    }

    @Override
    public int getNumSlots() {
        return this.fallbackResource.getNumSlots() + 1;
    }

    @Override
    public int getSlotForFont(String string) {
        if (this.primaryResource.getFullName().equalsIgnoreCase(string)) {
            return 0;
        }
        return this.fallbackResource.getSlotForFont(string) + 1;
    }

    @Override
    public FontResource getSlotResource(int n) {
        if (n == 0) {
            return this.primaryResource;
        }
        FontResource fontResource = this.fallbackResource.getSlotResource(n - 1);
        if (fontResource != null) {
            return fontResource;
        }
        return this.primaryResource;
    }

    @Override
    public String getFullName() {
        return this.primaryResource.getFullName();
    }

    @Override
    public String getPSName() {
        return this.primaryResource.getPSName();
    }

    @Override
    public String getFamilyName() {
        return this.primaryResource.getFamilyName();
    }

    @Override
    public String getStyleName() {
        return this.primaryResource.getStyleName();
    }

    @Override
    public String getLocaleFullName() {
        return this.primaryResource.getLocaleFullName();
    }

    @Override
    public String getLocaleFamilyName() {
        return this.primaryResource.getLocaleFamilyName();
    }

    @Override
    public String getLocaleStyleName() {
        return this.primaryResource.getLocaleStyleName();
    }

    @Override
    public String getFileName() {
        return this.primaryResource.getFileName();
    }

    @Override
    public int getFeatures() {
        return this.primaryResource.getFeatures();
    }

    @Override
    public Object getPeer() {
        return this.primaryResource.getPeer();
    }

    @Override
    public void setPeer(Object object) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public boolean isEmbeddedFont() {
        return this.primaryResource.isEmbeddedFont();
    }

    @Override
    public boolean isBold() {
        return this.primaryResource.isBold();
    }

    @Override
    public boolean isItalic() {
        return this.primaryResource.isItalic();
    }

    @Override
    public CharToGlyphMapper getGlyphMapper() {
        if (this.mapper == null) {
            this.mapper = new CompositeGlyphMapper(this);
        }
        return this.mapper;
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
    public Map<FontStrikeDesc, WeakReference<FontStrike>> getStrikeMap() {
        return this.strikeMap;
    }

    @Override
    public int getDefaultAAMode() {
        return this.getSlotResource(0).getDefaultAAMode();
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

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof PrismCompositeFontResource)) {
            return false;
        }
        PrismCompositeFontResource prismCompositeFontResource = (PrismCompositeFontResource)object;
        return this.primaryResource.equals(prismCompositeFontResource.primaryResource);
    }

    public int hashCode() {
        return this.primaryResource.hashCode();
    }
}

