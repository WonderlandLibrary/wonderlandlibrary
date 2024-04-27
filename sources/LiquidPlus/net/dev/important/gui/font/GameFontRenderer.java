/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.resources.IResourceManager
 *  net.minecraft.util.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.gui.font;

import java.awt.Color;
import java.awt.Font;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.event.TextEvent;
import net.dev.important.gui.font.AWTFontRenderer;
import net.dev.important.utils.ClassUtils;
import net.dev.important.utils.render.ColorUtils;
import net.dev.important.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0010\f\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 02\u00020\u0001:\u00010B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0012\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017H\u0014J&\u0010\u0018\u001a\u00020\u000e2\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001c2\u0006\u0010\u001e\u001a\u00020\u000eJ.\u0010\u0018\u001a\u00020\u000e2\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001c2\u0006\u0010\u001e\u001a\u00020\u000e2\u0006\u0010\u001f\u001a\u00020 J&\u0010!\u001a\u00020\u000e2\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001c2\u0006\u0010\u001e\u001a\u00020\u000eJ0\u0010!\u001a\u00020\u000e2\u0006\u0010\"\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001c2\u0006\u0010\u001e\u001a\u00020\u000e2\u0006\u0010\u001f\u001a\u00020 H\u0016J(\u0010#\u001a\u00020\u000e2\u0006\u0010\"\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001c2\u0006\u0010\u001e\u001a\u00020\u000eH\u0016J2\u0010$\u001a\u00020\u000e2\b\u0010\"\u001a\u0004\u0018\u00010\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001c2\u0006\u0010%\u001a\u00020\u000e2\u0006\u0010&\u001a\u00020 H\u0002J\u0010\u0010'\u001a\u00020\u000e2\u0006\u0010(\u001a\u00020)H\u0016J\u0010\u0010*\u001a\u00020\u000e2\u0006\u0010+\u001a\u00020)H\u0016J\u0010\u0010,\u001a\u00020\u000e2\u0006\u0010\"\u001a\u00020\u001aH\u0016J\u0010\u0010-\u001a\u00020\u00152\u0006\u0010.\u001a\u00020/H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\u000e8F\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0011\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0012\u001a\u00020\u000e8F\u00a2\u0006\u0006\u001a\u0004\b\u0013\u0010\u0010\u00a8\u00061"}, d2={"Lnet/dev/important/gui/font/GameFontRenderer;", "Lnet/minecraft/client/gui/FontRenderer;", "font", "Ljava/awt/Font;", "(Ljava/awt/Font;)V", "boldFont", "Lnet/dev/important/gui/font/AWTFontRenderer;", "boldItalicFont", "defaultFont", "getDefaultFont", "()Lnet/dev/important/gui/font/AWTFontRenderer;", "setDefaultFont", "(Lnet/dev/important/gui/font/AWTFontRenderer;)V", "height", "", "getHeight", "()I", "italicFont", "size", "getSize", "bindTexture", "", "location", "Lnet/minecraft/util/ResourceLocation;", "drawCenteredString", "s", "", "x", "", "y", "color", "shadow", "", "drawString", "text", "drawStringWithShadow", "drawText", "colorHex", "ignoreColor", "getCharWidth", "character", "", "getColorCode", "charCode", "getStringWidth", "onResourceManagerReload", "resourceManager", "Lnet/minecraft/client/resources/IResourceManager;", "Companion", "LiquidBounce"})
public final class GameFontRenderer
extends FontRenderer {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private AWTFontRenderer defaultFont;
    @NotNull
    private AWTFontRenderer boldFont;
    @NotNull
    private AWTFontRenderer italicFont;
    @NotNull
    private AWTFontRenderer boldItalicFont;

    public GameFontRenderer(@NotNull Font font) {
        Intrinsics.checkNotNullParameter(font, "font");
        super(Minecraft.func_71410_x().field_71474_y, new ResourceLocation("textures/font/ascii.png"), ClassUtils.INSTANCE.hasForge() ? null : Minecraft.func_71410_x().func_110434_K(), false);
        this.defaultFont = new AWTFontRenderer(font, 0, 0, 6, null);
        Font font2 = font.deriveFont(1);
        Intrinsics.checkNotNullExpressionValue(font2, "font.deriveFont(Font.BOLD)");
        this.boldFont = new AWTFontRenderer(font2, 0, 0, 6, null);
        font2 = font.deriveFont(2);
        Intrinsics.checkNotNullExpressionValue(font2, "font.deriveFont(Font.ITALIC)");
        this.italicFont = new AWTFontRenderer(font2, 0, 0, 6, null);
        font2 = font.deriveFont(3);
        Intrinsics.checkNotNullExpressionValue(font2, "font.deriveFont(Font.BOLD or Font.ITALIC)");
        this.boldItalicFont = new AWTFontRenderer(font2, 0, 0, 6, null);
        this.field_78288_b = this.getHeight();
    }

    @NotNull
    public final AWTFontRenderer getDefaultFont() {
        return this.defaultFont;
    }

    public final void setDefaultFont(@NotNull AWTFontRenderer aWTFontRenderer) {
        Intrinsics.checkNotNullParameter(aWTFontRenderer, "<set-?>");
        this.defaultFont = aWTFontRenderer;
    }

    public final int getHeight() {
        return this.defaultFont.getHeight() / 2;
    }

    public final int getSize() {
        return this.defaultFont.getFont().getSize();
    }

    public final int drawString(@NotNull String s, float x, float y, int color) {
        Intrinsics.checkNotNullParameter(s, "s");
        return this.func_175065_a(s, x, y, color, false);
    }

    public int func_175063_a(@NotNull String text, float x, float y, int color) {
        Intrinsics.checkNotNullParameter(text, "text");
        return this.func_175065_a(text, x, y, color, true);
    }

    public final int drawCenteredString(@NotNull String s, float x, float y, int color, boolean shadow) {
        Intrinsics.checkNotNullParameter(s, "s");
        return this.func_175065_a(s, x - (float)this.func_78256_a(s) / 2.0f, y, color, shadow);
    }

    public final int drawCenteredString(@NotNull String s, float x, float y, int color) {
        Intrinsics.checkNotNullParameter(s, "s");
        return this.func_175063_a(s, x - (float)this.func_78256_a(s) / 2.0f, y, color);
    }

    public int func_175065_a(@NotNull String text, float x, float y, int color, boolean shadow) {
        Intrinsics.checkNotNullParameter(text, "text");
        String currentText = text;
        TextEvent event = new TextEvent(currentText);
        Client.INSTANCE.getEventManager().callEvent(event);
        String string = event.getText();
        if (string == null) {
            return 0;
        }
        currentText = string;
        float currY = y - 3.0f;
        if (shadow) {
            this.drawText(currentText, x + 1.0f, currY + 1.0f, new Color(0, 0, 0, 150).getRGB(), true);
        }
        return this.drawText(currentText, x, currY, color, false);
    }

    /*
     * WARNING - void declaration
     */
    private final int drawText(String text, float x, float y, int colorHex, boolean ignoreColor) {
        if (text == null) {
            return 0;
        }
        if (((CharSequence)text).length() == 0) {
            return (int)x;
        }
        GlStateManager.func_179137_b((double)((double)x - 1.5), (double)((double)y + 0.5), (double)0.0);
        GlStateManager.func_179141_d();
        GlStateManager.func_179147_l();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179098_w();
        int hexColor = 0;
        hexColor = colorHex;
        if ((hexColor & 0xFC000000) == 0) {
            hexColor |= 0xFF000000;
        }
        int alpha2 = hexColor >> 24 & 0xFF;
        if (StringsKt.contains$default((CharSequence)text, "\u00a7", false, 2, null)) {
            String[] stringArray = new String[]{"\u00a7"};
            List parts = StringsKt.split$default((CharSequence)text, stringArray, false, 0, 6, null);
            AWTFontRenderer currentFont = null;
            currentFont = this.defaultFont;
            double width = 0.0;
            boolean randomCase = false;
            boolean bold = false;
            boolean italic = false;
            boolean strikeThrough = false;
            boolean underline = false;
            Iterable $this$forEachIndexed$iv = parts;
            boolean $i$f$forEachIndexed = false;
            int index$iv = 0;
            for (Object item$iv : $this$forEachIndexed$iv) {
                void part;
                int n = index$iv;
                index$iv = n + 1;
                if (n < 0) {
                    CollectionsKt.throwIndexOverflow();
                }
                String string = (String)item$iv;
                int index = n;
                boolean bl = false;
                if (((CharSequence)part).length() == 0) continue;
                if (index == 0) {
                    currentFont.drawString((String)part, width, 0.0, hexColor);
                    width += (double)currentFont.getStringWidth((String)part);
                    continue;
                }
                String string2 = part.substring(1);
                Intrinsics.checkNotNullExpressionValue(string2, "this as java.lang.String).substring(startIndex)");
                String words = string2;
                char type = part.charAt(0);
                int colorIndex = Companion.getColorIndex(type);
                boolean bl2 = 0 <= colorIndex ? colorIndex < 16 : false;
                if (bl2) {
                    if (!ignoreColor) {
                        hexColor = ColorUtils.hexColors[colorIndex] | alpha2 << 24;
                    }
                    bold = false;
                    italic = false;
                    randomCase = false;
                    underline = false;
                    strikeThrough = false;
                } else if (colorIndex == 16) {
                    randomCase = true;
                } else if (colorIndex == 17) {
                    bold = true;
                } else if (colorIndex == 18) {
                    strikeThrough = true;
                } else if (colorIndex == 19) {
                    underline = true;
                } else if (colorIndex == 20) {
                    italic = true;
                } else if (colorIndex == 21) {
                    hexColor = colorHex;
                    if ((hexColor & 0xFC000000) == 0) {
                        hexColor |= 0xFF000000;
                    }
                    bold = false;
                    italic = false;
                    randomCase = false;
                    underline = false;
                    strikeThrough = false;
                }
                currentFont = bold && italic ? this.boldItalicFont : (bold ? this.boldFont : (italic ? this.italicFont : this.getDefaultFont()));
                currentFont.drawString(randomCase ? ColorUtils.INSTANCE.randomMagicText(words) : words, width, 0.0, hexColor);
                if (strikeThrough) {
                    RenderUtils.drawLine(width / 2.0 + 1.0, (double)currentFont.getHeight() / 3.0, (width + (double)currentFont.getStringWidth(words)) / 2.0 + 1.0, (double)currentFont.getHeight() / 3.0, (float)this.field_78288_b / 16.0f);
                }
                if (underline) {
                    RenderUtils.drawLine(width / 2.0 + 1.0, (double)currentFont.getHeight() / 2.0, (width + (double)currentFont.getStringWidth(words)) / 2.0 + 1.0, (double)currentFont.getHeight() / 2.0, (float)this.field_78288_b / 16.0f);
                }
                width += (double)currentFont.getStringWidth(words);
            }
        } else {
            this.defaultFont.drawString(text, 0.0, 0.0, hexColor);
        }
        GlStateManager.func_179084_k();
        GlStateManager.func_179137_b((double)(-((double)x - 1.5)), (double)(-((double)y + 0.5)), (double)0.0);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        return (int)(x + (float)this.func_78256_a(text));
    }

    public int func_175064_b(char charCode) {
        return ColorUtils.hexColors[Companion.getColorIndex(charCode)];
    }

    /*
     * WARNING - void declaration
     */
    public int func_78256_a(@NotNull String text) {
        int n;
        Intrinsics.checkNotNullParameter(text, "text");
        String currentText = text;
        TextEvent event = new TextEvent(currentText);
        Client.INSTANCE.getEventManager().callEvent(event);
        String string = event.getText();
        if (string == null) {
            return 0;
        }
        currentText = string;
        if (StringsKt.contains$default((CharSequence)currentText, "\u00a7", false, 2, null)) {
            String[] stringArray = new String[]{"\u00a7"};
            List parts = StringsKt.split$default((CharSequence)currentText, stringArray, false, 0, 6, null);
            AWTFontRenderer currentFont = null;
            currentFont = this.defaultFont;
            int width = 0;
            boolean bold = false;
            boolean italic = false;
            Iterable $this$forEachIndexed$iv = parts;
            boolean $i$f$forEachIndexed = false;
            int index$iv = 0;
            for (Object item$iv : $this$forEachIndexed$iv) {
                void part;
                int n2 = index$iv;
                index$iv = n2 + 1;
                if (n2 < 0) {
                    CollectionsKt.throwIndexOverflow();
                }
                String string2 = (String)item$iv;
                int index = n2;
                boolean bl = false;
                if (((CharSequence)part).length() == 0) continue;
                if (index == 0) {
                    width += currentFont.getStringWidth((String)part);
                    continue;
                }
                String string3 = part.substring(1);
                Intrinsics.checkNotNullExpressionValue(string3, "this as java.lang.String).substring(startIndex)");
                String words = string3;
                char type = part.charAt(0);
                int colorIndex = Companion.getColorIndex(type);
                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                } else if (colorIndex == 17) {
                    bold = true;
                } else if (colorIndex == 20) {
                    italic = true;
                } else if (colorIndex == 21) {
                    bold = false;
                    italic = false;
                }
                currentFont = bold && italic ? this.boldItalicFont : (bold ? this.boldFont : (italic ? this.italicFont : this.getDefaultFont()));
                width += currentFont.getStringWidth(words);
            }
            n = width / 2;
        } else {
            n = this.defaultFont.getStringWidth(currentText) / 2;
        }
        return n;
    }

    public int func_78263_a(char character) {
        return this.func_78256_a(String.valueOf(character));
    }

    public void func_110549_a(@NotNull IResourceManager resourceManager) {
        Intrinsics.checkNotNullParameter(resourceManager, "resourceManager");
    }

    protected void bindTexture(@Nullable ResourceLocation location) {
    }

    @JvmStatic
    public static final int getColorIndex(char type) {
        return Companion.getColorIndex(type);
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\f\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0007"}, d2={"Lnet/dev/important/gui/font/GameFontRenderer$Companion;", "", "()V", "getColorIndex", "", "type", "", "LiquidBounce"})
    public static final class Companion {
        private Companion() {
        }

        @JvmStatic
        public final int getColorIndex(char type) {
            int n;
            block1: {
                char c;
                block0: {
                    c = type;
                    boolean bl = '0' <= c ? c < ':' : false;
                    if (!bl) break block0;
                    n = type - 48;
                    break block1;
                }
                n = ('a' <= c ? c < 'g' : false) ? type - 97 + 10 : (('k' <= c ? c < 'p' : false) ? type - 107 + 16 : (c == 'r' ? 21 : -1));
            }
            return n;
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

