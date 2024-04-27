/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  qiriyou.verV3Z.Loader
 */
package Verify1.font;

import java.awt.Font;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.texture.DynamicTexture;
import qiriyou.verV3Z.Loader;

public class CFont {
    private final float imgSize = 512.0f;
    protected CharData[] charData = new CharData[256];
    protected Font font;
    protected boolean antiAlias;
    protected boolean fractionalMetrics;
    protected int fontHeight = -1;
    protected int charOffset = 0;
    protected DynamicTexture tex;

    public CFont(Font font, boolean bl, boolean bl2) {
        this.font = font;
        this.antiAlias = bl;
        this.fractionalMetrics = bl2;
        this.tex = this.setupTexture(font, bl, bl2, this.charData);
    }

    protected native DynamicTexture setupTexture(Font var1, boolean var2, boolean var3, CharData[] var4);

    protected native BufferedImage generateFontImage(Font var1, boolean var2, boolean var3, CharData[] var4);

    public native void drawChar(CharData[] var1, char var2, float var3, float var4) throws ArrayIndexOutOfBoundsException;

    protected native void drawQuad(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8);

    public native int getStringHeight(String var1);

    public native int getHeight();

    public native int getStringWidth(String var1);

    public native boolean isAntiAlias();

    public native void setAntiAlias(boolean var1);

    public native boolean isFractionalMetrics();

    public native void setFractionalMetrics(boolean var1);

    public native Font getFont();

    public native void setFont(Font var1);

    static {
        Loader.registerNativesForClass((int)5, CFont.class);
        CFont.$qiriyouLoader();
    }

    public static native /* synthetic */ void $qiriyouLoader();

    protected class CharData {
        public int width;
        public int height;
        public int storedX;
        public int storedY;

        protected CharData() {
        }
    }
}

