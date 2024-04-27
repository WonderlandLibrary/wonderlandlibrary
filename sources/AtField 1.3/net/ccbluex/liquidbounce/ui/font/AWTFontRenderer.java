/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.renderer.texture.TextureUtil
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.TypeCastException;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.features.module.modules.render.CustomFont;
import net.ccbluex.liquidbounce.ui.font.CachedFont;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public final class AWTFontRenderer
extends MinecraftInstance {
    private int textureHeight;
    private int fontHeight;
    private int textureWidth;
    private final HashMap cachedStrings;
    private final Font font;
    public static final Companion Companion;
    private final CharLocation[] charLocations;
    private int textureID;
    private static int gcTicks;
    private static final ArrayList activeFontRenderers;
    private static final int CACHED_FONT_REMOVAL_TIME;
    private static final int GC_TICKS;
    private static boolean assumeNonVolatile;

    public final int getStringWidth(String string) {
        int n = 0;
        String string2 = string;
        boolean bl = false;
        for (int n2 : string2.toCharArray()) {
            CharLocation charLocation;
            if (this.charLocations[n2 < this.charLocations.length ? n2 : 3] == null) {
                continue;
            }
            n += charLocation.getWidth() - ((Number)CustomFont.fontWidthValue.get()).intValue();
        }
        return n / 2;
    }

    static {
        CACHED_FONT_REMOVAL_TIME = 30000;
        GC_TICKS = 600;
        Companion = new Companion(null);
        activeFontRenderers = new ArrayList();
    }

    public final Font getFont() {
        return this.font;
    }

    private final void renderBitmap(int n, int n2) {
        BufferedImage[] bufferedImageArray = new BufferedImage[n2];
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = n2;
        for (int i = n; i < n6; ++i) {
            BufferedImage bufferedImage = this.drawCharToImage((char)i);
            CharLocation charLocation = new CharLocation(n4, n5, bufferedImage.getWidth(), bufferedImage.getHeight());
            if (charLocation.getHeight() > this.fontHeight) {
                this.fontHeight = charLocation.getHeight();
            }
            if (charLocation.getHeight() > n3) {
                n3 = charLocation.getHeight();
            }
            this.charLocations[i] = charLocation;
            bufferedImageArray[i] = bufferedImage;
            if ((n4 += charLocation.getWidth()) <= 2048) continue;
            if (n4 > this.textureWidth) {
                this.textureWidth = n4;
            }
            n4 = 0;
            n5 += n3;
            n3 = 0;
        }
        this.textureHeight = n5 + n3;
        BufferedImage bufferedImage = new BufferedImage(this.textureWidth, this.textureHeight, 2);
        Graphics graphics = bufferedImage.getGraphics();
        if (graphics == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.awt.Graphics2D");
        }
        Graphics2D graphics2D = (Graphics2D)graphics;
        graphics2D.setFont(this.font);
        graphics2D.setColor(new Color(255, 255, 255, 0));
        graphics2D.fillRect(0, 0, this.textureWidth, this.textureHeight);
        graphics2D.setColor(Color.white);
        int n7 = n2;
        for (int i = n; i < n7; ++i) {
            if (bufferedImageArray[i] == null || this.charLocations[i] == null) continue;
            Image image2 = bufferedImageArray[i];
            CharLocation charLocation = this.charLocations[i];
            if (charLocation == null) {
                Intrinsics.throwNpe();
            }
            int n8 = charLocation.getX();
            CharLocation charLocation2 = this.charLocations[i];
            if (charLocation2 == null) {
                Intrinsics.throwNpe();
            }
            graphics2D.drawImage(image2, n8, charLocation2.getY(), null);
        }
        this.textureID = TextureUtil.func_110989_a((int)TextureUtil.func_110996_a(), (BufferedImage)bufferedImage, (boolean)true, (boolean)true);
    }

    public static final boolean access$getAssumeNonVolatile$cp() {
        return assumeNonVolatile;
    }

    private final void collectGarbage() {
        long l = System.currentTimeMillis();
        Map map = this.cachedStrings;
        boolean bl = false;
        Map map2 = map;
        Map map3 = new LinkedHashMap();
        boolean bl2 = false;
        Object object = map2;
        boolean bl3 = false;
        Iterator iterator2 = object.entrySet().iterator();
        while (iterator2.hasNext()) {
            Map.Entry entry;
            Map.Entry entry2 = entry = iterator2.next();
            boolean bl4 = false;
            if (!(l - ((CachedFont)entry2.getValue()).getLastUsage() > (long)30000)) continue;
            map3.put(entry.getKey(), entry.getValue());
        }
        map = map3;
        bl = false;
        map2 = map;
        boolean bl5 = false;
        Iterator iterator3 = map2.entrySet().iterator();
        while (iterator3.hasNext()) {
            Object object2 = object = iterator3.next();
            boolean bl6 = false;
            GL11.glDeleteLists((int)((CachedFont)object2.getValue()).getDisplayList(), (int)1);
            ((CachedFont)object2.getValue()).setDeleted(true);
            this.cachedStrings.remove(object2.getKey());
        }
    }

    private final void drawChar(CharLocation charLocation, float f, float f2) {
        float f3 = charLocation.getWidth();
        float f4 = charLocation.getHeight();
        float f5 = charLocation.getX();
        float f6 = charLocation.getY();
        float f7 = f5 / (float)this.textureWidth;
        float f8 = f6 / (float)this.textureHeight;
        float f9 = f3 / (float)this.textureWidth;
        float f10 = f4 / (float)this.textureHeight;
        GL11.glTexCoord2f((float)f7, (float)f8);
        GL11.glVertex2f((float)f, (float)f2);
        GL11.glTexCoord2f((float)f7, (float)(f8 + f10));
        GL11.glVertex2f((float)f, (float)(f2 + f4));
        GL11.glTexCoord2f((float)(f7 + f9), (float)(f8 + f10));
        GL11.glVertex2f((float)(f + f3), (float)(f2 + f4));
        GL11.glTexCoord2f((float)(f7 + f9), (float)f8);
        GL11.glVertex2f((float)(f + f3), (float)f2);
    }

    public AWTFontRenderer(Font font, int n, int n2) {
        this.font = font;
        this.fontHeight = -1;
        this.charLocations = new CharLocation[n2];
        this.cachedStrings = new HashMap();
        this.renderBitmap(n, n2);
        activeFontRenderers.add(this);
    }

    private final BufferedImage drawCharToImage(char c) {
        int n;
        Graphics graphics = new BufferedImage(1, 1, 2).getGraphics();
        if (graphics == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.awt.Graphics2D");
        }
        Graphics2D graphics2D = (Graphics2D)graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setFont(this.font);
        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        int n2 = fontMetrics.charWidth(c) + 8;
        if (n2 <= 0) {
            n2 = 7;
        }
        if ((n = fontMetrics.getHeight() + 3) <= 0) {
            n = this.font.getSize();
        }
        BufferedImage bufferedImage = new BufferedImage(n2, n, 2);
        Graphics graphics2 = bufferedImage.getGraphics();
        if (graphics2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.awt.Graphics2D");
        }
        Graphics2D graphics2D2 = (Graphics2D)graphics2;
        graphics2D2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D2.setFont(this.font);
        graphics2D2.setColor(Color.WHITE);
        graphics2D2.drawString(String.valueOf(c), 3, 1 + fontMetrics.getAscent());
        return bufferedImage;
    }

    public final void drawString(String string, double d, double d2, int n) {
        double d3 = 0.5;
        double d4 = 1.0 / d3;
        GL11.glPushMatrix();
        GL11.glScaled((double)d3, (double)d3, (double)d3);
        GL11.glTranslated((double)(d * (double)2.0f), (double)(d2 * 2.0 - 2.0), (double)0.0);
        MinecraftInstance.classProvider.getGlStateManager().bindTexture(this.textureID);
        float f = (float)(n >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(n & 0xFF) / 255.0f;
        float f4 = (float)(n >> 24 & 0xFF) / 255.0f;
        GL11.glColor4f((float)f, (float)f2, (float)f3, (float)f4);
        double d5 = 0.0;
        CachedFont cachedFont = (CachedFont)this.cachedStrings.get(string);
        if (cachedFont != null) {
            GL11.glCallList((int)cachedFont.getDisplayList());
            cachedFont.setLastUsage(System.currentTimeMillis());
            GL11.glPopMatrix();
            return;
        }
        int n2 = -1;
        if (assumeNonVolatile) {
            n2 = GL11.glGenLists((int)1);
            GL11.glNewList((int)n2, (int)4865);
        }
        GL11.glBegin((int)7);
        String string2 = string;
        boolean bl = false;
        for (char c : string2.toCharArray()) {
            CharLocation charLocation;
            if (c >= this.charLocations.length) {
                GL11.glEnd();
                GL11.glScaled((double)d4, (double)d4, (double)d4);
                MinecraftInstance.mc.getFontRendererObj().drawString(String.valueOf(c), (float)d5 * (float)d3 + 1.0f, 2.0f, n, false);
                d5 += (double)MinecraftInstance.mc.getFontRendererObj().getStringWidth(String.valueOf(c)) * d4;
                GL11.glScaled((double)d3, (double)d3, (double)d3);
                MinecraftInstance.classProvider.getGlStateManager().bindTexture(this.textureID);
                GL11.glColor4f((float)f, (float)f2, (float)f3, (float)f4);
                GL11.glBegin((int)7);
                continue;
            }
            if (this.charLocations[c] == null) {
                continue;
            }
            this.drawChar(charLocation, (float)d5, 0.0f);
            d5 += (double)(charLocation.getWidth() - ((Number)CustomFont.fontWidthValue.get()).intValue());
        }
        GL11.glEnd();
        if (assumeNonVolatile) {
            ((Map)this.cachedStrings).put(string, new CachedFont(n2, System.currentTimeMillis(), false, 4, null));
            GL11.glEndList();
        }
        GL11.glPopMatrix();
    }

    public static final int access$getGcTicks$cp() {
        return gcTicks;
    }

    public static final void access$collectGarbage(AWTFontRenderer aWTFontRenderer) {
        aWTFontRenderer.collectGarbage();
    }

    public static final void access$setGcTicks$cp(int n) {
        gcTicks = n;
    }

    public static final void access$setAssumeNonVolatile$cp(boolean bl) {
        assumeNonVolatile = bl;
    }

    public final int getHeight() {
        return (this.fontHeight - 8) / 2;
    }

    public AWTFontRenderer(Font font, int n, int n2, int n3, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n3 & 2) != 0) {
            n = 0;
        }
        if ((n3 & 4) != 0) {
            n2 = 255;
        }
        this(font, n, n2);
    }

    public static final ArrayList access$getActiveFontRenderers$cp() {
        return activeFontRenderers;
    }

    private static final class CharLocation {
        private int height;
        private int width;
        private int x;
        private int y;

        public final int component3() {
            return this.width;
        }

        public final int component4() {
            return this.height;
        }

        public final void setWidth(int n) {
            this.width = n;
        }

        public static CharLocation copy$default(CharLocation charLocation, int n, int n2, int n3, int n4, int n5, Object object) {
            if ((n5 & 1) != 0) {
                n = charLocation.x;
            }
            if ((n5 & 2) != 0) {
                n2 = charLocation.y;
            }
            if ((n5 & 4) != 0) {
                n3 = charLocation.width;
            }
            if ((n5 & 8) != 0) {
                n4 = charLocation.height;
            }
            return charLocation.copy(n, n2, n3, n4);
        }

        public final void setX(int n) {
            this.x = n;
        }

        public final CharLocation copy(int n, int n2, int n3, int n4) {
            return new CharLocation(n, n2, n3, n4);
        }

        public final void setY(int n) {
            this.y = n;
        }

        public final int getY() {
            return this.y;
        }

        public int hashCode() {
            return ((Integer.hashCode(this.x) * 31 + Integer.hashCode(this.y)) * 31 + Integer.hashCode(this.width)) * 31 + Integer.hashCode(this.height);
        }

        public final int getWidth() {
            return this.width;
        }

        public CharLocation(int n, int n2, int n3, int n4) {
            this.x = n;
            this.y = n2;
            this.width = n3;
            this.height = n4;
        }

        public final int getX() {
            return this.x;
        }

        public final int getHeight() {
            return this.height;
        }

        public final void setHeight(int n) {
            this.height = n;
        }

        public String toString() {
            return "CharLocation(x=" + this.x + ", y=" + this.y + ", width=" + this.width + ", height=" + this.height + ")";
        }

        public boolean equals(@Nullable Object object) {
            block3: {
                block2: {
                    if (this == object) break block2;
                    if (!(object instanceof CharLocation)) break block3;
                    CharLocation charLocation = (CharLocation)object;
                    if (this.x != charLocation.x || this.y != charLocation.y || this.width != charLocation.width || this.height != charLocation.height) break block3;
                }
                return true;
            }
            return false;
        }

        public final int component1() {
            return this.x;
        }

        public final int component2() {
            return this.y;
        }
    }

    public static final class Companion {
        public final void garbageCollectionTick() {
            int n = AWTFontRenderer.access$getGcTicks$cp();
            AWTFontRenderer.access$setGcTicks$cp(n + 1);
            if (n > 600) {
                Iterable iterable = this.getActiveFontRenderers();
                boolean bl = false;
                for (Object t : iterable) {
                    AWTFontRenderer aWTFontRenderer = (AWTFontRenderer)t;
                    boolean bl2 = false;
                    AWTFontRenderer.access$collectGarbage(aWTFontRenderer);
                }
                AWTFontRenderer.access$setGcTicks$cp(0);
            }
        }

        public final ArrayList getActiveFontRenderers() {
            return AWTFontRenderer.access$getActiveFontRenderers$cp();
        }

        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final boolean getAssumeNonVolatile() {
            return AWTFontRenderer.access$getAssumeNonVolatile$cp();
        }

        private Companion() {
        }

        public final void setAssumeNonVolatile(boolean bl) {
            AWTFontRenderer.access$setAssumeNonVolatile$cp(bl);
        }
    }
}

