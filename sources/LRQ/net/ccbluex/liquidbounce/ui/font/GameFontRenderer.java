/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.collections.CollectionsKt
 *  kotlin.jvm.JvmStatic
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL20
 */
package net.ccbluex.liquidbounce.ui.font;

import java.awt.Color;
import java.awt.Font;
import java.util.List;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.util.IWrappedFontRenderer;
import net.ccbluex.liquidbounce.event.TextEvent;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;
import net.ccbluex.liquidbounce.ui.font.AWTFontRenderer;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.shader.shaders.RainbowFontShader;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public final class GameFontRenderer
implements IWrappedFontRenderer {
    private final int fontHeight;
    private AWTFontRenderer defaultFont;
    private AWTFontRenderer boldFont;
    private AWTFontRenderer italicFont;
    private AWTFontRenderer boldItalicFont;
    public static final Companion Companion = new Companion(null);

    public final int getFontHeight() {
        return this.fontHeight;
    }

    public final AWTFontRenderer getDefaultFont() {
        return this.defaultFont;
    }

    public final void setDefaultFont(AWTFontRenderer aWTFontRenderer) {
        this.defaultFont = aWTFontRenderer;
    }

    public final int getHeight() {
        return this.defaultFont.getHeight() / 2;
    }

    public final int getSize() {
        return this.defaultFont.getFont().getSize();
    }

    @Override
    public int drawString(@Nullable String s, float x, float y, int color) {
        return this.drawString(s, x, y, color, false);
    }

    @Override
    public int drawStringWithShadow(@Nullable String text, float x, float y, int color) {
        return this.drawString(text, x, y, color, true);
    }

    @Override
    public int drawCenteredString(String s, float x, float y, int color, boolean shadow) {
        return this.drawString(s, x - (float)this.getStringWidth(s) / 2.0f, y, color, shadow);
    }

    @Override
    public int drawCenteredString(String s, float x, float y, int color) {
        return this.drawStringWithShadow(s, x - (float)this.getStringWidth(s) / 2.0f, y, color);
    }

    @Override
    public int drawString(@Nullable String text, float x, float y, int color, boolean shadow) {
        String currentText = text;
        TextEvent event = new TextEvent(currentText);
        LiquidBounce.INSTANCE.getEventManager().callEvent(event);
        String string = event.getText();
        if (string == null) {
            return 0;
        }
        currentText = string;
        float currY = y - 3.0f;
        boolean rainbow = RainbowFontShader.INSTANCE.isInUse();
        if (shadow) {
            GL20.glUseProgram((int)0);
            GameFontRenderer.drawText$default(this, currentText, x + 1.0f, currY + 1.0f, new Color(0, 0, 0, 150).getRGB(), true, false, 32, null);
        }
        return this.drawText(currentText, x, currY, color, false, rainbow);
    }

    /*
     * WARNING - void declaration
     */
    private final int drawText(String text, float x, float y, int color, boolean ignoreColor, boolean rainbow) {
        if (text == null) {
            return 0;
        }
        CharSequence charSequence = text;
        boolean bl = false;
        boolean bl2 = false;
        if (charSequence.length() == 0) {
            return (int)x;
        }
        int rainbowShaderId = RainbowFontShader.INSTANCE.getProgramId();
        if (rainbow) {
            GL20.glUseProgram((int)rainbowShaderId);
        }
        GL11.glTranslated((double)((double)x - 1.5), (double)((double)y + 0.5), (double)0.0);
        WrapperImpl.INSTANCE.getClassProvider().getGlStateManager().enableAlpha();
        WrapperImpl.INSTANCE.getClassProvider().getGlStateManager().enableBlend();
        WrapperImpl.INSTANCE.getClassProvider().getGlStateManager().tryBlendFuncSeparate(770, 771, 1, 0);
        WrapperImpl.INSTANCE.getClassProvider().getGlStateManager().enableTexture2D();
        int currentColor = color;
        if ((currentColor & 0xFC000000) == 0) {
            currentColor |= 0xFF000000;
        }
        int defaultColor = currentColor;
        int alpha = currentColor >> 24 & 0xFF;
        if (text.equals("\u00a7")) {
            List parts = StringsKt.split$default((CharSequence)text, (String[])new String[]{"\u00a7"}, (boolean)false, (int)0, (int)6, null);
            AWTFontRenderer currentFont = this.defaultFont;
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
                int n = index$iv++;
                boolean bl3 = false;
                if (n < 0) {
                    CollectionsKt.throwIndexOverflow();
                }
                int n2 = n;
                String string = (String)item$iv;
                int index = n2;
                boolean bl4 = false;
                CharSequence charSequence2 = (CharSequence)part;
                boolean bl5 = false;
                if (charSequence2.length() == 0) continue;
                if (index == 0) {
                    currentFont.drawString((String)part, width, 0.0, currentColor);
                    width += (double)currentFont.getStringWidth((String)part);
                    continue;
                }
                void var30_33 = part;
                int n3 = 1;
                int n4 = 0;
                void v0 = var30_33;
                if (v0 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                String words = v0.substring(n3);
                char type = part.charAt(0);
                int colorIndex = Companion.getColorIndex(type);
                n4 = colorIndex;
                if (0 <= n4 && 15 >= n4) {
                    if (!ignoreColor) {
                        currentColor = ColorUtils.hexColors[colorIndex] | alpha << 24;
                        if (rainbow) {
                            GL20.glUseProgram((int)0);
                        }
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
                    currentColor = color;
                    if ((currentColor & 0xFC000000) == 0) {
                        currentColor |= 0xFF000000;
                    }
                    if (rainbow) {
                        GL20.glUseProgram((int)rainbowShaderId);
                    }
                    bold = false;
                    italic = false;
                    randomCase = false;
                    underline = false;
                    strikeThrough = false;
                }
                currentFont = bold && italic ? this.boldItalicFont : (bold ? this.boldFont : (italic ? this.italicFont : this.defaultFont));
                currentFont.drawString(randomCase ? ColorUtils.INSTANCE.randomMagicText(words) : words, width, 0.0, currentColor);
                if (strikeThrough) {
                    RenderUtils.drawLine(width / 2.0 + 1.0, (double)currentFont.getHeight() / 3.0, (width + (double)currentFont.getStringWidth(words)) / 2.0 + 1.0, (double)currentFont.getHeight() / 3.0, (float)this.fontHeight / 16.0f);
                }
                if (underline) {
                    RenderUtils.drawLine(width / 2.0 + 1.0, (double)currentFont.getHeight() / 2.0, (width + (double)currentFont.getStringWidth(words)) / 2.0 + 1.0, (double)currentFont.getHeight() / 2.0, (float)this.fontHeight / 16.0f);
                }
                width += (double)currentFont.getStringWidth(words);
            }
        } else {
            this.defaultFont.drawString(text, 0.0, 0.0, currentColor);
        }
        WrapperImpl.INSTANCE.getClassProvider().getGlStateManager().disableBlend();
        GL11.glTranslated((double)(-((double)x - 1.5)), (double)(-((double)y + 0.5)), (double)0.0);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        return (int)(x + (float)this.getStringWidth(text));
    }

    static /* synthetic */ int drawText$default(GameFontRenderer gameFontRenderer, String string, float f, float f2, int n, boolean bl, boolean bl2, int n2, Object object) {
        if ((n2 & 0x20) != 0) {
            bl2 = false;
        }
        return gameFontRenderer.drawText(string, f, f2, n, bl, bl2);
    }

    @Override
    public int getColorCode(char charCode) {
        return ColorUtils.hexColors[Companion.getColorIndex(charCode)];
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public int getStringWidth(@Nullable String text) {
        int n;
        String currentText = text;
        TextEvent event = new TextEvent(currentText);
        LiquidBounce.INSTANCE.getEventManager().callEvent(event);
        String string = event.getText();
        if (string == null) {
            return 0;
        }
        currentText = string;
        if (currentText.equals("\u00a7")) {
            List parts = StringsKt.split$default((CharSequence)currentText, (String[])new String[]{"\u00a7"}, (boolean)false, (int)0, (int)6, null);
            AWTFontRenderer currentFont = this.defaultFont;
            int width = 0;
            boolean bold = false;
            boolean italic = false;
            Iterable $this$forEachIndexed$iv = parts;
            boolean $i$f$forEachIndexed = false;
            int index$iv = 0;
            for (Object item$iv : $this$forEachIndexed$iv) {
                void part;
                int n2 = index$iv++;
                boolean bl = false;
                if (n2 < 0) {
                    CollectionsKt.throwIndexOverflow();
                }
                int n3 = n2;
                String string2 = (String)item$iv;
                int index = n3;
                boolean bl2 = false;
                CharSequence charSequence = (CharSequence)part;
                boolean bl3 = false;
                if (charSequence.length() == 0) continue;
                if (index == 0) {
                    width += currentFont.getStringWidth((String)part);
                    continue;
                }
                void var21_22 = part;
                int n4 = 1;
                boolean bl4 = false;
                void v1 = var21_22;
                if (v1 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                String words = v1.substring(n4);
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
                currentFont = bold && italic ? this.boldItalicFont : (bold ? this.boldFont : (italic ? this.italicFont : this.defaultFont));
                width += currentFont.getStringWidth(words);
            }
            n = width / 2;
        } else {
            n = this.defaultFont.getStringWidth(currentText) / 2;
        }
        return n;
    }

    @Override
    public int getCharWidth(char character) {
        return this.getStringWidth(String.valueOf(character));
    }

    public GameFontRenderer(Font font) {
        this.defaultFont = new AWTFontRenderer(font, 0, 0, 6, null);
        this.boldFont = new AWTFontRenderer(font.deriveFont(1), 0, 0, 6, null);
        this.italicFont = new AWTFontRenderer(font.deriveFont(2), 0, 0, 6, null);
        this.boldItalicFont = new AWTFontRenderer(font.deriveFont(3), 0, 0, 6, null);
        this.fontHeight = this.getHeight();
    }

    @JvmStatic
    public static final int getColorIndex(char type) {
        return Companion.getColorIndex(type);
    }

    public static final class Companion {
        @JvmStatic
        public final int getColorIndex(char type) {
            char c = type;
            char c2 = c;
            return '0' <= c2 && '9' >= c2 ? type - 48 : ('a' <= (c2 = c) && 'f' >= c2 ? type - 97 + 10 : ('k' <= (c2 = c) && 'o' >= c2 ? type - 107 + 16 : (c == 'r' ? 21 : -1)));
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

