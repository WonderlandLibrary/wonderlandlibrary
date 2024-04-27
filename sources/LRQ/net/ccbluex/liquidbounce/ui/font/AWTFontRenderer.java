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
    private int fontHeight;
    private final CharLocation[] charLocations;
    private final HashMap<String, CachedFont> cachedStrings;
    private int textureID;
    private int textureWidth;
    private int textureHeight;
    private final Font font;
    private static boolean assumeNonVolatile;
    private static final ArrayList<AWTFontRenderer> activeFontRenderers;
    private static int gcTicks;
    private static final int GC_TICKS = 600;
    private static final int CACHED_FONT_REMOVAL_TIME = 30000;
    public static final Companion Companion;

    /*
     * WARNING - void declaration
     */
    private final void collectGarbage() {
        void $this$filterTo$iv$iv;
        long currentTime = System.currentTimeMillis();
        Map $this$filter$iv = this.cachedStrings;
        boolean $i$f$filter = false;
        Map map = $this$filter$iv;
        Map destination$iv$iv = new LinkedHashMap();
        boolean $i$f$filterTo = false;
        void var8_9 = $this$filterTo$iv$iv;
        boolean bl = false;
        Iterator iterator = var8_9.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry element$iv$iv;
            Map.Entry it = element$iv$iv = iterator.next();
            boolean bl2 = false;
            if (!(currentTime - ((CachedFont)it.getValue()).getLastUsage() > (long)30000)) continue;
            destination$iv$iv.put(element$iv$iv.getKey(), element$iv$iv.getValue());
        }
        Map $this$forEach$iv = destination$iv$iv;
        boolean $i$f$forEach = false;
        map = $this$forEach$iv;
        boolean bl3 = false;
        Iterator iterator2 = map.entrySet().iterator();
        while (iterator2.hasNext()) {
            Map.Entry element$iv;
            Map.Entry it = element$iv = iterator2.next();
            boolean bl4 = false;
            GL11.glDeleteLists((int)((CachedFont)it.getValue()).getDisplayList(), (int)1);
            ((CachedFont)it.getValue()).setDeleted(true);
            this.cachedStrings.remove(it.getKey());
        }
    }

    public final int getHeight() {
        return (this.fontHeight - 8) / 2;
    }

    public final void drawString(String text, double x, double y, int color) {
        double scale = 0.25;
        double reverse = 1.0 / scale;
        GL11.glPushMatrix();
        GL11.glScaled((double)scale, (double)scale, (double)scale);
        GL11.glTranslated((double)(x * (double)2.0f), (double)(y * 2.0 - 2.0), (double)0.0);
        MinecraftInstance.classProvider.getGlStateManager().bindTexture(this.textureID);
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        double currX = 0.0;
        CachedFont cached = this.cachedStrings.get(text);
        if (cached != null) {
            GL11.glCallList((int)cached.getDisplayList());
            cached.setLastUsage(System.currentTimeMillis());
            GL11.glPopMatrix();
            return;
        }
        int list = -1;
        if (assumeNonVolatile) {
            list = GL11.glGenLists((int)1);
            GL11.glNewList((int)list, (int)4865);
        }
        GL11.glBegin((int)7);
        String string = text;
        boolean bl = false;
        for (char c : string.toCharArray()) {
            CharLocation fontChar;
            if (c >= this.charLocations.length) {
                GL11.glEnd();
                GL11.glScaled((double)reverse, (double)reverse, (double)reverse);
                MinecraftInstance.mc.getFontRendererObj().drawString(String.valueOf(c), (float)currX * (float)scale + 1.0f, 2.0f, color, false);
                currX += (double)MinecraftInstance.mc.getFontRendererObj().getStringWidth(String.valueOf(c)) * reverse;
                GL11.glScaled((double)scale, (double)scale, (double)scale);
                MinecraftInstance.classProvider.getGlStateManager().bindTexture(this.textureID);
                GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
                GL11.glBegin((int)7);
                continue;
            }
            if (this.charLocations[c] == null) {
                continue;
            }
            this.drawChar(fontChar, (float)currX, 0.0f);
            currX += (double)fontChar.getWidth() - 8.0;
        }
        GL11.glEnd();
        if (assumeNonVolatile) {
            ((Map)this.cachedStrings).put(text, new CachedFont(list, System.currentTimeMillis(), false, 4, null));
            GL11.glEndList();
        }
        GL11.glPopMatrix();
    }

    private final void drawChar(CharLocation charLocation, float x, float y) {
        float width = charLocation.getWidth();
        float height = charLocation.getHeight();
        float srcX = charLocation.getX();
        float srcY = charLocation.getY();
        float renderX = srcX / (float)this.textureWidth;
        float renderY = srcY / (float)this.textureHeight;
        float renderWidth = width / (float)this.textureWidth;
        float renderHeight = height / (float)this.textureHeight;
        GL11.glTexCoord2f((float)renderX, (float)renderY);
        GL11.glVertex2f((float)x, (float)y);
        GL11.glTexCoord2f((float)renderX, (float)(renderY + renderHeight));
        GL11.glVertex2f((float)x, (float)(y + height));
        GL11.glTexCoord2f((float)(renderX + renderWidth), (float)(renderY + renderHeight));
        GL11.glVertex2f((float)(x + width), (float)(y + height));
        GL11.glTexCoord2f((float)(renderX + renderWidth), (float)renderY);
        GL11.glVertex2f((float)(x + width), (float)y);
    }

    /*
     * WARNING - void declaration
     */
    private final void renderBitmap(int startChar, int stopChar) {
        BufferedImage[] fontImages = new BufferedImage[stopChar];
        int rowHeight = 0;
        int charX = 0;
        int charY = 0;
        int n = startChar;
        int n2 = stopChar;
        while (n < n2) {
            void targetChar;
            BufferedImage fontImage = this.drawCharToImage((char)targetChar);
            CharLocation fontChar = new CharLocation(charX, charY, fontImage.getWidth(), fontImage.getHeight());
            if (fontChar.getHeight() > this.fontHeight) {
                this.fontHeight = fontChar.getHeight();
            }
            if (fontChar.getHeight() > rowHeight) {
                rowHeight = fontChar.getHeight();
            }
            this.charLocations[targetChar] = fontChar;
            fontImages[targetChar] = fontImage;
            if ((charX += fontChar.getWidth()) > 2048) {
                if (charX > this.textureWidth) {
                    this.textureWidth = charX;
                }
                charX = 0;
                charY += rowHeight;
                rowHeight = 0;
            }
            ++targetChar;
        }
        this.textureHeight = charY + rowHeight;
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
        int fontImage = startChar;
        int n3 = stopChar;
        while (fontImage < n3) {
            void targetChar;
            if (fontImages[targetChar] != null && this.charLocations[targetChar] != null) {
                Image image2 = fontImages[targetChar];
                CharLocation charLocation = this.charLocations[targetChar];
                if (charLocation == null) {
                    Intrinsics.throwNpe();
                }
                int n4 = charLocation.getX();
                CharLocation charLocation2 = this.charLocations[targetChar];
                if (charLocation2 == null) {
                    Intrinsics.throwNpe();
                }
                graphics2D.drawImage(image2, n4, charLocation2.getY(), null);
            }
            ++targetChar;
        }
        this.textureID = TextureUtil.func_110989_a((int)TextureUtil.func_110996_a(), (BufferedImage)bufferedImage, (boolean)true, (boolean)true);
    }

    private final BufferedImage drawCharToImage(char ch) {
        int charHeight;
        Graphics graphics = new BufferedImage(1, 1, 2).getGraphics();
        if (graphics == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.awt.Graphics2D");
        }
        Graphics2D graphics2D = (Graphics2D)graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setFont(this.font);
        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        int charWidth = fontMetrics.charWidth(ch) + 8;
        if (charWidth <= 0) {
            charWidth = 7;
        }
        if ((charHeight = fontMetrics.getHeight() + 3) <= 0) {
            charHeight = this.font.getSize();
        }
        BufferedImage fontImage = new BufferedImage(charWidth, charHeight, 2);
        Graphics graphics2 = fontImage.getGraphics();
        if (graphics2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.awt.Graphics2D");
        }
        Graphics2D graphics3 = (Graphics2D)graphics2;
        graphics3.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics3.setFont(this.font);
        graphics3.setColor(Color.WHITE);
        graphics3.drawString(String.valueOf(ch), 3, 1 + fontMetrics.getAscent());
        return fontImage;
    }

    public final int getStringWidth(String text) {
        int width = 0;
        String string = text;
        boolean bl = false;
        for (int n : string.toCharArray()) {
            CharLocation fontChar;
            if (this.charLocations[n < this.charLocations.length ? n : 3] == null) {
                continue;
            }
            width += fontChar.getWidth() - 8;
        }
        return width / 2;
    }

    public final Font getFont() {
        return this.font;
    }

    public AWTFontRenderer(Font font, int startChar, int stopChar) {
        this.font = font;
        this.fontHeight = -1;
        this.charLocations = new CharLocation[stopChar];
        this.cachedStrings = new HashMap();
        this.renderBitmap(startChar, stopChar);
        activeFontRenderers.add(this);
    }

    public /* synthetic */ AWTFontRenderer(Font font, int n, int n2, int n3, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n3 & 2) != 0) {
            n = 0;
        }
        if ((n3 & 4) != 0) {
            n2 = 255;
        }
        this(font, n, n2);
    }

    static {
        Companion = new Companion(null);
        activeFontRenderers = new ArrayList();
    }

    private static final class CharLocation {
        private int x;
        private int y;
        private int width;
        private int height;

        public final int getX() {
            return this.x;
        }

        public final void setX(int n) {
            this.x = n;
        }

        public final int getY() {
            return this.y;
        }

        public final void setY(int n) {
            this.y = n;
        }

        public final int getWidth() {
            return this.width;
        }

        public final void setWidth(int n) {
            this.width = n;
        }

        public final int getHeight() {
            return this.height;
        }

        public final void setHeight(int n) {
            this.height = n;
        }

        public CharLocation(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public final int component1() {
            return this.x;
        }

        public final int component2() {
            return this.y;
        }

        public final int component3() {
            return this.width;
        }

        public final int component4() {
            return this.height;
        }

        public final CharLocation copy(int x, int y, int width, int height) {
            return new CharLocation(x, y, width, height);
        }

        public static /* synthetic */ CharLocation copy$default(CharLocation charLocation, int n, int n2, int n3, int n4, int n5, Object object) {
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

        public String toString() {
            return "CharLocation(x=" + this.x + ", y=" + this.y + ", width=" + this.width + ", height=" + this.height + ")";
        }

        public int hashCode() {
            return ((Integer.hashCode(this.x) * 31 + Integer.hashCode(this.y)) * 31 + Integer.hashCode(this.width)) * 31 + Integer.hashCode(this.height);
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
    }

    public static final class Companion {
        public final boolean getAssumeNonVolatile() {
            return assumeNonVolatile;
        }

        public final void setAssumeNonVolatile(boolean bl) {
            assumeNonVolatile = bl;
        }

        public final ArrayList<AWTFontRenderer> getActiveFontRenderers() {
            return activeFontRenderers;
        }

        public final void garbageCollectionTick() {
            int n = gcTicks;
            gcTicks = n + 1;
            if (n > 600) {
                Iterable $this$forEach$iv = this.getActiveFontRenderers();
                boolean $i$f$forEach = false;
                for (Object element$iv : $this$forEach$iv) {
                    AWTFontRenderer it = (AWTFontRenderer)element$iv;
                    boolean bl = false;
                    it.collectGarbage();
                }
                gcTicks = 0;
            }
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

