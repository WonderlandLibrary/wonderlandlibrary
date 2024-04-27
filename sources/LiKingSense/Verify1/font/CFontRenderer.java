/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  qiriyou.verV3Z.Loader
 */
package Verify1.font;

import Verify1.font.CFont;
import java.awt.Font;
import java.util.List;
import net.minecraft.client.renderer.texture.DynamicTexture;
import qiriyou.verV3Z.Loader;

public class CFontRenderer
extends CFont {
    protected CFont.CharData[] boldChars = new CFont.CharData[256];
    protected CFont.CharData[] italicChars = new CFont.CharData[256];
    protected CFont.CharData[] boldItalicChars = new CFont.CharData[256];
    private final int[] colorCode = new int[32];
    private final String colorcodeIdentifiers = "0123456789abcdefklmnor";
    protected DynamicTexture texBold;
    protected DynamicTexture texItalic;
    protected DynamicTexture texItalicBold;

    public CFontRenderer(Font font, boolean bl, boolean bl2) {
        super(font, bl, bl2);
        this.setupMinecraftColorcodes();
        this.setupBoldItalicIDs();
    }

    public native String trimStringToWidth(String var1, int var2, boolean var3);

    public native String trimStringToWidth(String var1, int var2);

    public native float drawStringWithShadow(String var1, double var2, double var4, int var6);

    public native float drawString(String var1, float var2, float var3, int var4);

    public native float drawCenteredString(String var1, double var2, double var4, int var6);

    public native float drawCenteredStringWithShadow(String var1, float var2, float var3, int var4);

    public native float drawCenteredStringWithShadow(String var1, double var2, double var4, int var6);

    public native float drawString(String var1, double var2, double var4, int var6, boolean var7);

    @Override
    public native int getStringWidth(String var1);

    @Override
    public native void setFont(Font var1);

    @Override
    public native void setAntiAlias(boolean var1);

    @Override
    public native void setFractionalMetrics(boolean var1);

    private native void setupBoldItalicIDs();

    private native void drawLine(double var1, double var3, double var5, double var7, float var9);

    public native List<String> wrapWords(String var1, double var2);

    public native List<String> formatString(String var1, double var2);

    private native void setupMinecraftColorcodes();

    static {
        Loader.registerNativesForClass((int)12, CFontRenderer.class);
        CFontRenderer.$qiriyouLoader();
    }

    public static native /* synthetic */ void $qiriyouLoader();
}

