/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.texture.TextureUtil
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 */
package net.dev.important.gui.font;

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
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.gui.font.CachedFont;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\f\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0006\b\u0007\u0018\u0000 ,2\u00020\u0001:\u0002+,B!\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0007J\b\u0010\u001a\u001a\u00020\u001bH\u0002J \u0010\u001c\u001a\u00020\u001b2\u0006\u0010\u001d\u001a\u00020\u000f2\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u001fH\u0002J\u0010\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020$H\u0002J&\u0010%\u001a\u00020\u001b2\u0006\u0010&\u001a\u00020\n2\u0006\u0010\u001e\u001a\u00020'2\u0006\u0010 \u001a\u00020'2\u0006\u0010(\u001a\u00020\u0005J\u000e\u0010)\u001a\u00020\u00052\u0006\u0010&\u001a\u00020\nJ\u0018\u0010*\u001a\u00020\u001b2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u0002R*\u0010\b\u001a\u001e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000b0\tj\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000b`\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0018\u0010\r\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000f0\u000eX\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0010R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u000e\u0010\u0013\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0014\u001a\u00020\u00058F\u00a2\u0006\u0006\u001a\u0004\b\u0015\u0010\u0016R\u000e\u0010\u0017\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006-"}, d2={"Lnet/dev/important/gui/font/AWTFontRenderer;", "", "font", "Ljava/awt/Font;", "startChar", "", "stopChar", "(Ljava/awt/Font;II)V", "cachedStrings", "Ljava/util/HashMap;", "", "Lnet/dev/important/gui/font/CachedFont;", "Lkotlin/collections/HashMap;", "charLocations", "", "Lnet/dev/important/gui/font/AWTFontRenderer$CharLocation;", "[Lnet/dev/important/gui/font/AWTFontRenderer$CharLocation;", "getFont", "()Ljava/awt/Font;", "fontHeight", "height", "getHeight", "()I", "textureHeight", "textureID", "textureWidth", "collectGarbage", "", "drawChar", "char", "x", "", "y", "drawCharToImage", "Ljava/awt/image/BufferedImage;", "ch", "", "drawString", "text", "", "color", "getStringWidth", "renderBitmap", "CharLocation", "Companion", "LiquidBounce"})
public final class AWTFontRenderer {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final Font font;
    private int fontHeight;
    @NotNull
    private final CharLocation[] charLocations;
    @NotNull
    private final HashMap<String, CachedFont> cachedStrings;
    private int textureID;
    private int textureWidth;
    private int textureHeight;
    private static boolean assumeNonVolatile;
    @NotNull
    private static final ArrayList<AWTFontRenderer> activeFontRenderers;
    private static int gcTicks;
    private static final int GC_TICKS = 600;
    private static final int CACHED_FONT_REMOVAL_TIME = 30000;

    public AWTFontRenderer(@NotNull Font font, int startChar, int stopChar) {
        Intrinsics.checkNotNullParameter(font, "font");
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

    @NotNull
    public final Font getFont() {
        return this.font;
    }

    /*
     * WARNING - void declaration
     */
    private final void collectGarbage() {
        void $this$filterTo$iv$iv;
        long currentTime = System.currentTimeMillis();
        Map $this$filter$iv = this.cachedStrings;
        boolean $i$f$filter = false;
        Object object = $this$filter$iv;
        Map destination$iv$iv = new LinkedHashMap();
        boolean $i$f$filterTo = false;
        Iterator iterator2 = $this$filterTo$iv$iv.entrySet().iterator();
        while (iterator2.hasNext()) {
            Map.Entry element$iv$iv;
            Map.Entry it = element$iv$iv = iterator2.next();
            boolean bl = false;
            if (!(currentTime - ((CachedFont)it.getValue()).getLastUsage() > 30000L)) continue;
            destination$iv$iv.put(element$iv$iv.getKey(), element$iv$iv.getValue());
        }
        Map $this$forEach$iv = destination$iv$iv;
        boolean $i$f$forEach = false;
        object = $this$forEach$iv.entrySet().iterator();
        while (object.hasNext()) {
            Map.Entry element$iv;
            Map.Entry it = element$iv = (Map.Entry)object.next();
            boolean bl = false;
            GL11.glDeleteLists((int)((CachedFont)it.getValue()).getDisplayList(), (int)1);
            ((CachedFont)it.getValue()).setDeleted(true);
            this.cachedStrings.remove(it.getKey());
        }
    }

    public final int getHeight() {
        return (this.fontHeight - 8) / 2;
    }

    public final void drawString(@NotNull String text, double x, double y, int color) {
        Intrinsics.checkNotNullParameter(text, "text");
        double scale = 0.25;
        double reverse = 1.0 / scale;
        GlStateManager.func_179094_E();
        GlStateManager.func_179139_a((double)scale, (double)scale, (double)scale);
        GL11.glTranslated((double)(x * (double)2.0f), (double)(y * 2.0 - 2.0), (double)0.0);
        GlStateManager.func_179144_i((int)this.textureID);
        float red2 = (float)(color >> 16 & 0xFF) / 255.0f;
        float green2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue2 = (float)(color & 0xFF) / 255.0f;
        float alpha2 = (float)(color >> 24 & 0xFF) / 255.0f;
        GlStateManager.func_179131_c((float)red2, (float)green2, (float)blue2, (float)alpha2);
        double currX = 0.0;
        CachedFont cached = this.cachedStrings.get(text);
        if (cached != null) {
            GL11.glCallList((int)cached.getDisplayList());
            cached.setLastUsage(System.currentTimeMillis());
            GlStateManager.func_179121_F();
            return;
        }
        int list = -1;
        if (assumeNonVolatile) {
            list = GL11.glGenLists((int)1);
            GL11.glNewList((int)list, (int)4865);
        }
        GL11.glBegin((int)7);
        char[] cArray = text.toCharArray();
        Intrinsics.checkNotNullExpressionValue(cArray, "this as java.lang.String).toCharArray()");
        for (char c : cArray) {
            if (c < this.charLocations.length) continue;
            GL11.glEnd();
            GlStateManager.func_179139_a((double)reverse, (double)reverse, (double)reverse);
            Minecraft.func_71410_x().field_71466_p.func_175065_a(String.valueOf(c), (float)currX * (float)scale + 1.0f, 2.0f, color, false);
            currX += (double)Minecraft.func_71410_x().field_71466_p.func_78256_a(String.valueOf(c)) * reverse;
            GlStateManager.func_179139_a((double)scale, (double)scale, (double)scale);
            GlStateManager.func_179144_i((int)this.textureID);
            GlStateManager.func_179131_c((float)red2, (float)green2, (float)blue2, (float)alpha2);
            GL11.glBegin((int)7);
        }
        GL11.glEnd();
        if (assumeNonVolatile) {
            Object object = this.cachedStrings;
            CachedFont cachedFont = new CachedFont(list, System.currentTimeMillis(), false, 4, null);
            object.put(text, cachedFont);
            GL11.glEndList();
        }
        GlStateManager.func_179121_F();
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

    private final void renderBitmap(int startChar, int stopChar) {
        BufferedImage[] fontImages = new BufferedImage[stopChar];
        int rowHeight = 0;
        int charX = 0;
        int charY = 0;
        int n = startChar;
        while (n < stopChar) {
            int targetChar;
            BufferedImage fontImage;
            CharLocation fontChar;
            if ((fontChar = new CharLocation(charX, charY, (fontImage = this.drawCharToImage((char)(targetChar = n++))).getWidth(), fontImage.getHeight())).getHeight() > this.fontHeight) {
                this.fontHeight = fontChar.getHeight();
            }
            if (fontChar.getHeight() > rowHeight) {
                rowHeight = fontChar.getHeight();
            }
            this.charLocations[targetChar] = fontChar;
            fontImages[targetChar] = fontImage;
            if ((charX += fontChar.getWidth()) <= 2048) continue;
            if (charX > this.textureWidth) {
                this.textureWidth = charX;
            }
            charX = 0;
            charY += rowHeight;
            rowHeight = 0;
        }
        this.textureHeight = charY + rowHeight;
        BufferedImage bufferedImage = new BufferedImage(this.textureWidth, this.textureHeight, 2);
        Graphics graphics = bufferedImage.getGraphics();
        if (graphics == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.awt.Graphics2D");
        }
        Graphics2D graphics2D = (Graphics2D)graphics;
        graphics2D.setFont(this.font);
        graphics2D.setColor(new Color(255, 255, 255, 0));
        graphics2D.fillRect(0, 0, this.textureWidth, this.textureHeight);
        graphics2D.setColor(Color.white);
        int n2 = startChar;
        while (n2 < stopChar) {
            int targetChar;
            if (fontImages[targetChar = n2++] == null || this.charLocations[targetChar] == null) continue;
            Image image = fontImages[targetChar];
            CharLocation charLocation = this.charLocations[targetChar];
            Intrinsics.checkNotNull(charLocation);
            int n3 = charLocation.getX();
            CharLocation charLocation2 = this.charLocations[targetChar];
            Intrinsics.checkNotNull(charLocation2);
            graphics2D.drawImage(image, n3, charLocation2.getY(), null);
        }
        this.textureID = TextureUtil.func_110989_a((int)TextureUtil.func_110996_a(), (BufferedImage)bufferedImage, (boolean)true, (boolean)true);
    }

    private final BufferedImage drawCharToImage(char ch) {
        int charHeight;
        Graphics graphics = new BufferedImage(1, 1, 2).getGraphics();
        if (graphics == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.awt.Graphics2D");
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
            throw new NullPointerException("null cannot be cast to non-null type java.awt.Graphics2D");
        }
        Graphics2D graphics3 = (Graphics2D)graphics2;
        graphics3.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics3.setFont(this.font);
        graphics3.setColor(Color.WHITE);
        graphics3.drawString(String.valueOf(ch), 3, 1 + fontMetrics.getAscent());
        return fontImage;
    }

    public final int getStringWidth(@NotNull String text) {
        Intrinsics.checkNotNullParameter(text, "text");
        int width = 0;
        char[] cArray = text.toCharArray();
        Intrinsics.checkNotNullExpressionValue(cArray, "this as java.lang.String).toCharArray()");
        for (int n : cArray) {
            CharLocation charLocation = this.charLocations[n < this.charLocations.length ? n : 3];
            if (charLocation == null) continue;
            CharLocation fontChar = charLocation;
            width += fontChar.getWidth() - 8;
        }
        return width / 2;
    }

    static {
        activeFontRenderers = new ArrayList();
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0013\u001a\u00020\u0014R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R!\u0010\u0006\u001a\u0012\u0012\u0004\u0012\u00020\b0\u0007j\b\u0012\u0004\u0012\u00020\b`\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u001a\u0010\f\u001a\u00020\rX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0012\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2={"Lnet/dev/important/gui/font/AWTFontRenderer$Companion;", "", "()V", "CACHED_FONT_REMOVAL_TIME", "", "GC_TICKS", "activeFontRenderers", "Ljava/util/ArrayList;", "Lnet/dev/important/gui/font/AWTFontRenderer;", "Lkotlin/collections/ArrayList;", "getActiveFontRenderers", "()Ljava/util/ArrayList;", "assumeNonVolatile", "", "getAssumeNonVolatile", "()Z", "setAssumeNonVolatile", "(Z)V", "gcTicks", "garbageCollectionTick", "", "LiquidBounce"})
    public static final class Companion {
        private Companion() {
        }

        public final boolean getAssumeNonVolatile() {
            return assumeNonVolatile;
        }

        public final void setAssumeNonVolatile(boolean bl) {
            assumeNonVolatile = bl;
        }

        @NotNull
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

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0014\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0007J\t\u0010\u0012\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0013\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0014\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0015\u001a\u00020\u0003H\u00c6\u0003J1\u0010\u0016\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u0017\u001a\u00020\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001a\u001a\u00020\u0003H\u00d6\u0001J\t\u0010\u001b\u001a\u00020\u001cH\u00d6\u0001R\u001a\u0010\u0006\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u001a\u0010\u0005\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\t\"\u0004\b\r\u0010\u000bR\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\t\"\u0004\b\u000f\u0010\u000bR\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\t\"\u0004\b\u0011\u0010\u000b\u00a8\u0006\u001d"}, d2={"Lnet/dev/important/gui/font/AWTFontRenderer$CharLocation;", "", "x", "", "y", "width", "height", "(IIII)V", "getHeight", "()I", "setHeight", "(I)V", "getWidth", "setWidth", "getX", "setX", "getY", "setY", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "toString", "", "LiquidBounce"})
    private static final class CharLocation {
        private int x;
        private int y;
        private int width;
        private int height;

        public CharLocation(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

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

        @NotNull
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

        @NotNull
        public String toString() {
            return "CharLocation(x=" + this.x + ", y=" + this.y + ", width=" + this.width + ", height=" + this.height + ')';
        }

        public int hashCode() {
            int result = Integer.hashCode(this.x);
            result = result * 31 + Integer.hashCode(this.y);
            result = result * 31 + Integer.hashCode(this.width);
            result = result * 31 + Integer.hashCode(this.height);
            return result;
        }

        public boolean equals(@Nullable Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof CharLocation)) {
                return false;
            }
            CharLocation charLocation = (CharLocation)other;
            if (this.x != charLocation.x) {
                return false;
            }
            if (this.y != charLocation.y) {
                return false;
            }
            if (this.width != charLocation.width) {
                return false;
            }
            return this.height == charLocation.height;
        }
    }
}

