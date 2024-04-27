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
import net.ccbluex.liquidbounce.features.module.modules.render.CustomFont;
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
    private AWTFontRenderer defaultFont;
    private AWTFontRenderer boldItalicFont;
    private final int fontHeight;
    private AWTFontRenderer boldFont;
    private AWTFontRenderer italicFont;
    public static final Companion Companion = new Companion(null);

    public final int getFontHeight() {
        return this.fontHeight;
    }

    @Override
    public int drawCenteredString(String string, float f, float f2, int n, boolean bl) {
        return this.drawString(string, f - (float)this.getStringWidth(string) / 2.0f, f2, n, bl);
    }

    @Override
    public int drawString(@Nullable String string, float f, float f2, int n, boolean bl) {
        String string2 = string;
        TextEvent textEvent = new TextEvent(string2);
        LiquidBounce.INSTANCE.getEventManager().callEvent(textEvent);
        String string3 = textEvent.getText();
        if (string3 == null) {
            return 0;
        }
        string2 = string3;
        float f3 = f2 - 3.0f;
        boolean bl2 = RainbowFontShader.INSTANCE.isInUse();
        if (bl) {
            GL20.glUseProgram((int)0);
            if (((String)CustomFont.shadowValue.get()).equals("LiquidBounce")) {
                GameFontRenderer.drawText$default(this, string2, f + 1.0f, f3 + 1.0f, new Color(0, 0, 0, 150).getRGB(), true, false, 32, null);
            } else if (((String)CustomFont.shadowValue.get()).equals("Default")) {
                GameFontRenderer.drawText$default(this, string2, f + 0.5f, f3 + 0.5f, new Color(0, 0, 0, 130).getRGB(), true, false, 32, null);
            } else if (((String)CustomFont.shadowValue.get()).equals("Custom")) {
                GameFontRenderer.drawText$default(this, string2, f + ((Number)CustomFont.shadowstrenge.get()).floatValue(), f3 + ((Number)CustomFont.shadowstrenge.get()).floatValue(), new Color(20, 20, 20, 200).getRGB(), true, false, 32, null);
            } else if (((String)CustomFont.shadowValue.get()).equals("Outline")) {
                GameFontRenderer.drawText$default(this, string2, f + 0.5f, f3 + 0.5f, new Color(0, 0, 0, 130).getRGB(), true, false, 32, null);
                GameFontRenderer.drawText$default(this, string2, f - 0.5f, f3 - 0.5f, new Color(0, 0, 0, 130).getRGB(), true, false, 32, null);
                GameFontRenderer.drawText$default(this, string2, f + 0.5f, f3 - 0.5f, new Color(0, 0, 0, 130).getRGB(), true, false, 32, null);
                GameFontRenderer.drawText$default(this, string2, f - 0.5f, f3 + 0.5f, new Color(0, 0, 0, 130).getRGB(), true, false, 32, null);
            }
        }
        return this.drawText(string2, f, f3, n, false, bl2);
    }

    @Override
    public int getColorCode(char c) {
        return ColorUtils.hexColors[Companion.getColorIndex(c)];
    }

    public final int getHeight() {
        return this.defaultFont.getHeight();
    }

    @Override
    public int getCharWidth(char c) {
        return this.getStringWidth(String.valueOf(c));
    }

    @Override
    public int drawCenteredStringWithShadow(String string, float f, float f2, int n) {
        return this.drawCenteredString(string, f, f2, n, true);
    }

    public GameFontRenderer(Font font) {
        this.defaultFont = new AWTFontRenderer(font, 0, 0, 6, null);
        this.boldFont = new AWTFontRenderer(font.deriveFont(1), 0, 0, 6, null);
        this.italicFont = new AWTFontRenderer(font.deriveFont(2), 0, 0, 6, null);
        this.boldItalicFont = new AWTFontRenderer(font.deriveFont(3), 0, 0, 6, null);
        this.fontHeight = this.getHeight();
    }

    @Override
    public int drawCenteredString(String string, float f, float f2, int n) {
        return this.drawStringWithShadow(string, f - (float)this.getStringWidth(string) / 2.0f, f2, n);
    }

    @Override
    public int drawStringWithShadow(@Nullable String string, float f, float f2, int n) {
        return this.drawString(string, f, f2, n, true);
    }

    public final void setDefaultFont(AWTFontRenderer aWTFontRenderer) {
        this.defaultFont = aWTFontRenderer;
    }

    @JvmStatic
    public static final int getColorIndex(char c) {
        return Companion.getColorIndex(c);
    }

    private final int drawText(String string, float f, float f2, int n, boolean bl, boolean bl2) {
        if (string == null) {
            return 0;
        }
        CharSequence charSequence = string;
        int n2 = 0;
        if (charSequence.length() == 0) {
            return (int)f;
        }
        int n3 = RainbowFontShader.INSTANCE.getProgramId();
        if (bl2) {
            GL20.glUseProgram((int)n3);
        }
        GL11.glTranslated((double)((double)f - 1.5), (double)((double)f2 + 0.5), (double)0.0);
        WrapperImpl.INSTANCE.getClassProvider().getGlStateManager().enableAlpha();
        WrapperImpl.INSTANCE.getClassProvider().getGlStateManager().enableBlend();
        WrapperImpl.INSTANCE.getClassProvider().getGlStateManager().tryBlendFuncSeparate(770, 771, 1, 0);
        WrapperImpl.INSTANCE.getClassProvider().getGlStateManager().enableTexture2D();
        n2 = n;
        if ((n2 & 0xFC000000) == 0) {
            n2 |= 0xFF000000;
        }
        int n4 = n2;
        int n5 = n2 >> 24 & 0xFF;
        if (string.equals("\u00a7")) {
            List list = StringsKt.split$default((CharSequence)string, (String[])new String[]{"\u00a7"}, (boolean)false, (int)0, (int)6, null);
            AWTFontRenderer aWTFontRenderer = this.defaultFont;
            double d = 0.0;
            boolean bl3 = false;
            boolean bl4 = false;
            boolean bl5 = false;
            boolean bl6 = false;
            boolean bl7 = false;
            Iterable iterable = list;
            boolean bl8 = false;
            int n6 = 0;
            for (Object t : iterable) {
                int n7 = n6++;
                boolean bl9 = false;
                if (n7 < 0) {
                    CollectionsKt.throwIndexOverflow();
                }
                int n8 = n7;
                String string2 = (String)t;
                int n9 = n8;
                boolean bl10 = false;
                CharSequence charSequence2 = string2;
                char c = '\u0000';
                if (charSequence2.length() == 0) continue;
                if (n9 == 0) {
                    aWTFontRenderer.drawString(string2, d, 0.0, n2);
                    d += (double)aWTFontRenderer.getStringWidth(string2);
                    continue;
                }
                String string3 = string2;
                int n10 = 1;
                int n11 = 0;
                String string4 = string3;
                if (string4 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                charSequence2 = string4.substring(n10);
                c = string2.charAt(0);
                n10 = Companion.getColorIndex(c);
                n11 = n10;
                if (0 <= n11 && 15 >= n11) {
                    if (!bl) {
                        n2 = ColorUtils.hexColors[n10] | n5 << 24;
                        if (bl2) {
                            GL20.glUseProgram((int)0);
                        }
                    }
                    bl4 = false;
                    bl5 = false;
                    bl3 = false;
                    bl7 = false;
                    bl6 = false;
                } else if (n10 == 16) {
                    bl3 = true;
                } else if (n10 == 17) {
                    bl4 = true;
                } else if (n10 == 18) {
                    bl6 = true;
                } else if (n10 == 19) {
                    bl7 = true;
                } else if (n10 == 20) {
                    bl5 = true;
                } else if (n10 == 21) {
                    n2 = n;
                    if ((n2 & 0xFC000000) == 0) {
                        n2 |= 0xFF000000;
                    }
                    if (bl2) {
                        GL20.glUseProgram((int)n3);
                    }
                    bl4 = false;
                    bl5 = false;
                    bl3 = false;
                    bl7 = false;
                    bl6 = false;
                }
                aWTFontRenderer = bl4 && bl5 ? this.boldItalicFont : (bl4 ? this.boldFont : (bl5 ? this.italicFont : this.defaultFont));
                aWTFontRenderer.drawString((String)(bl3 ? ColorUtils.INSTANCE.randomMagicText((String)charSequence2) : charSequence2), d, 0.0, n2);
                if (bl6) {
                    RenderUtils.drawLine(d / 2.0 + 1.0, (double)aWTFontRenderer.getHeight() / 3.0, (d + (double)aWTFontRenderer.getStringWidth((String)charSequence2)) / 2.0 + 1.0, (double)aWTFontRenderer.getHeight() / 3.0, (float)this.fontHeight / 16.0f);
                }
                if (bl7) {
                    RenderUtils.drawLine(d / 2.0 + 1.0, (double)aWTFontRenderer.getHeight() / 2.0, (d + (double)aWTFontRenderer.getStringWidth((String)charSequence2)) / 2.0 + 1.0, (double)aWTFontRenderer.getHeight() / 2.0, (float)this.fontHeight / 16.0f);
                }
                d += (double)aWTFontRenderer.getStringWidth((String)charSequence2);
            }
        } else {
            this.defaultFont.drawString(string, 0.0, 0.0, n2);
        }
        WrapperImpl.INSTANCE.getClassProvider().getGlStateManager().disableBlend();
        GL11.glTranslated((double)(-((double)f - 1.5)), (double)(-((double)f2 + 0.5)), (double)0.0);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        return (int)(f + (float)this.getStringWidth(string));
    }

    public final int getSize() {
        return this.defaultFont.getFont().getSize();
    }

    static int drawText$default(GameFontRenderer gameFontRenderer, String string, float f, float f2, int n, boolean bl, boolean bl2, int n2, Object object) {
        if ((n2 & 0x20) != 0) {
            bl2 = false;
        }
        return gameFontRenderer.drawText(string, f, f2, n, bl, bl2);
    }

    @Override
    public int getStringWidth(@Nullable String string) {
        int n;
        String string2 = string;
        TextEvent textEvent = new TextEvent(string2);
        LiquidBounce.INSTANCE.getEventManager().callEvent(textEvent);
        String string3 = textEvent.getText();
        if (string3 == null) {
            return 0;
        }
        string2 = string3;
        if (string2.equals("\u00a7")) {
            List list = StringsKt.split$default((CharSequence)string2, (String[])new String[]{"\u00a7"}, (boolean)false, (int)0, (int)6, null);
            AWTFontRenderer aWTFontRenderer = this.defaultFont;
            int n2 = 0;
            boolean bl = false;
            boolean bl2 = false;
            Iterable iterable = list;
            boolean bl3 = false;
            int n3 = 0;
            for (Object t : iterable) {
                int n4 = n3++;
                boolean bl4 = false;
                if (n4 < 0) {
                    CollectionsKt.throwIndexOverflow();
                }
                int n5 = n4;
                String string4 = (String)t;
                int n6 = n5;
                boolean bl5 = false;
                CharSequence charSequence = string4;
                char c = '\u0000';
                if (charSequence.length() == 0) continue;
                if (n6 == 0) {
                    n2 += aWTFontRenderer.getStringWidth(string4);
                    continue;
                }
                String string5 = string4;
                int n7 = 1;
                boolean bl6 = false;
                String string6 = string5;
                if (string6 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                charSequence = string6.substring(n7);
                c = string4.charAt(0);
                n7 = Companion.getColorIndex(c);
                if (n7 < 16) {
                    bl = false;
                    bl2 = false;
                } else if (n7 == 17) {
                    bl = true;
                } else if (n7 == 20) {
                    bl2 = true;
                } else if (n7 == 21) {
                    bl = false;
                    bl2 = false;
                }
                aWTFontRenderer = bl && bl2 ? this.boldItalicFont : (bl ? this.boldFont : (bl2 ? this.italicFont : this.defaultFont));
                n2 += aWTFontRenderer.getStringWidth((String)charSequence);
            }
            n = n2 / 1;
        } else {
            n = this.defaultFont.getStringWidth(string2) / 1;
        }
        return n;
    }

    @Override
    public int drawString(@Nullable String string, float f, float f2, int n) {
        return this.drawString(string, f, f2, n, false);
    }

    public final AWTFontRenderer getDefaultFont() {
        return this.defaultFont;
    }

    public static final class Companion {
        private Companion() {
        }

        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        @JvmStatic
        public final int getColorIndex(char c) {
            char c2 = c;
            char c3 = c2;
            return '0' <= c3 && '9' >= c3 ? c - 48 : ('a' <= (c3 = c2) && 'f' >= c3 ? c - 97 + 10 : ('k' <= (c3 = c2) && 'o' >= c3 ? c - 107 + 16 : (c2 == 'r' ? 21 : -1)));
        }
    }
}

