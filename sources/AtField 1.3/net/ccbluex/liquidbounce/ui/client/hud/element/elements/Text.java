/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.Unit
 *  kotlin.io.CloseableKt
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.ranges.RangesKt
 *  kotlin.text.StringsKt
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.util.ChatAllowedCharacters
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import dev.sakura_starring.util.safe.HWIDUtil;
import dev.sakura_starring.util.safe.QQUtils;
import java.awt.Color;
import java.io.Closeable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import liying.utils.blur.BlurBuffer;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.modules.render.CustomColor;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.Side;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.CPSCounter;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.ServerUtils;
import net.ccbluex.liquidbounce.utils.render.Palette;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.RoundedUtil;
import net.ccbluex.liquidbounce.utils.render.shader.shaders.RainbowFontShader;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.FontValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.TextValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.opengl.GL11;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@ElementInfo(name="Text")
public final class Text
extends Element {
    private final IntegerValue blueValue;
    private final IntegerValue amountValue;
    private final IntegerValue distanceValue;
    private final BoolValue bord;
    private boolean editMode;
    private long prevClick;
    private int slidetext;
    private final FloatValue rainbowY;
    private final IntegerValue redValue;
    private final IntegerValue astolfoRainbowOffset;
    private final ListValue Mode;
    private static final DecimalFormat DECIMAL_FORMAT;
    public static final Companion Companion;
    private final BoolValue shadow;
    private final ListValue shadowValue;
    private final FloatValue radiusValue;
    private String speedStr;
    private final IntegerValue gidentspeed;
    private final ListValue colorModeValue;
    private final FloatValue rainbowX;
    private final IntegerValue colorBlueValue2;
    private final IntegerValue balpha;
    private final IntegerValue colorGreenValue2;
    private final TextValue displayString;
    private static final SimpleDateFormat HOUR_FORMAT;
    private final FloatValue brightnessValue;
    private final IntegerValue colorRedValue2;
    private final FloatValue saturationValue;
    private final IntegerValue astolfoclient;
    private static final DecimalFormat DECIMAL_FORMAT_INT;
    private int editTicks;
    private final BoolValue slide;
    private String displayText;
    private final IntegerValue astolfoRainbowIndex;
    private final BoolValue char;
    private final IntegerValue newRainbowIndex;
    private final BoolValue blurValue;
    private final IntegerValue slidedelay;
    private final IntegerValue greenValue;
    private MSTimer slidetimer;
    private FontValue fontValue;
    private static final SimpleDateFormat DATE_FORMAT;
    private boolean doslide;

    public static final BoolValue access$getShadow$p(Text text) {
        return text.shadow;
    }

    public final void glColor(Color color) {
        float f = (float)color.getRed() / 255.0f;
        float f2 = (float)color.getGreen() / 255.0f;
        float f3 = (float)color.getBlue() / 255.0f;
        float f4 = (float)color.getAlpha() / 255.0f;
        GlStateManager.func_179131_c((float)f, (float)f2, (float)f3, (float)f4);
    }

    public static final FontValue access$getFontValue$p(Text text) {
        return text.fontValue;
    }

    public static final DecimalFormat access$getDECIMAL_FORMAT$cp() {
        return DECIMAL_FORMAT;
    }

    public final int getSlidetext() {
        return this.slidetext;
    }

    public final void setDoslide(boolean bl) {
        this.doslide = bl;
    }

    public Text(double d, double d2, float f, Side side, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 1) != 0) {
            d = 10.0;
        }
        if ((n & 2) != 0) {
            d2 = 10.0;
        }
        if ((n & 4) != 0) {
            f = 1.0f;
        }
        if ((n & 8) != 0) {
            side = Side.Companion.default();
        }
        this(d, d2, f, side);
    }

    public static final TextValue access$getDisplayString$p(Text text) {
        return text.displayString;
    }

    public final void drawRect(float f, float f2, float f3, float f4, int n) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        this.glColor(n);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)f3, (double)f2);
        GL11.glVertex2d((double)f, (double)f2);
        GL11.glVertex2d((double)f, (double)f4);
        GL11.glVertex2d((double)f3, (double)f4);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static final void access$setFontValue$p(Text text, FontValue fontValue) {
        text.fontValue = fontValue;
    }

    @Override
    public Border drawElement() {
        float f;
        IFontRenderer iFontRenderer;
        block44: {
            int[] nArray;
            boolean bl;
            int n;
            String string;
            char[] cArray;
            block43: {
                iFontRenderer = (IFontRenderer)this.fontValue.get();
                f = 4.5f;
                Object object = this.displayText;
                int n2 = 0;
                String string2 = object;
                if (string2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                cArray = string2.toCharArray();
                if (((Boolean)this.char.get()).booleanValue()) {
                    f = iFontRenderer.getStringWidth(this.displayText);
                } else {
                    for (char c : cArray) {
                        f += (float)iFontRenderer.getStringWidth(String.valueOf(c));
                    }
                }
                if (((Boolean)this.slide.get()).booleanValue() && !MinecraftInstance.classProvider.isGuiHudDesigner(MinecraftInstance.mc.getCurrentScreen())) {
                    String string3;
                    if (this.slidetimer.hasTimePassed(((Number)this.slidedelay.get()).intValue())) {
                        if (this.slidetext <= this.getDisplay().length() && this.doslide) {
                            ++this.slidetext;
                            this.slidetimer.reset();
                        } else if (!this.doslide && this.slidetext >= 0) {
                            --this.slidetext;
                            this.slidetimer.reset();
                        }
                    }
                    if (this.slidetext == this.getDisplay().length() && this.doslide) {
                        this.doslide = false;
                    } else if (this.slidetext == 0 && !this.doslide) {
                        this.doslide = true;
                    }
                    object = this.getDisplay();
                    n2 = 0;
                    int n3 = this.slidetext;
                    Text text = this;
                    int n4 = 0;
                    Object object2 = object;
                    if (object2 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    text.displayText = string3 = ((String)object2).substring(n2, n3);
                } else {
                    this.displayText = this.getDisplay();
                }
                Module module = LiquidBounce.INSTANCE.getModuleManager().get(HUD.class);
                if (module == null) {
                    throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.render.HUD");
                }
                object = (HUD)module;
                n2 = new Color(((Number)CustomColor.r.get()).intValue(), ((Number)CustomColor.g.get()).intValue(), ((Number)CustomColor.b.get()).intValue()).getRGB();
                int n5 = new Color(((Number)CustomColor.r2.get()).intValue(), ((Number)CustomColor.g2.get()).intValue(), ((Number)CustomColor.b2.get()).intValue()).getRGB();
                if (((String)this.Mode.get()).equals("Top")) {
                    switch ((String)this.shadowValue.get()) {
                        case "Basic": {
                            RenderUtils.drawShadow(-2.5f, -2.5f, iFontRenderer.getStringWidth(this.displayText) + 2, iFontRenderer.getFontHeight());
                            break;
                        }
                        case "Thick": {
                            RenderUtils.drawShadow(-2.0f, -2.0f, iFontRenderer.getStringWidth(this.displayText) + 2, iFontRenderer.getFontHeight());
                            RenderUtils.drawShadow(-2.0f, -2.0f, iFontRenderer.getStringWidth(this.displayText) + 2, iFontRenderer.getFontHeight());
                            break;
                        }
                    }
                    if (((Boolean)this.blurValue.get()).booleanValue()) {
                        GL11.glTranslated((double)(-this.getRenderX()), (double)(-this.getRenderY()), (double)0.0);
                        GL11.glPushMatrix();
                        BlurBuffer.blurRoundArea((float)this.getRenderX(), (float)this.getRenderY(), iFontRenderer.getStringWidth(this.displayText) + 2, iFontRenderer.getFontHeight(), (int)((Number)this.radiusValue.get()).floatValue());
                        GL11.glPopMatrix();
                        GL11.glTranslated((double)this.getRenderX(), (double)this.getRenderY(), (double)0.0);
                    }
                    RoundedUtil.drawRound(-2.0f, -2.0f, iFontRenderer.getStringWidth(this.displayText) + 2, iFontRenderer.getFontHeight(), 0.0f, new Color(0, 0, 0, 100));
                    RenderUtils.drawGradientSideways(-3.0, -3.0, iFontRenderer.getStringWidth(this.displayText) + 2, -2.0, n2, n5);
                }
                if (((String)this.Mode.get()).equals("Onetap")) {
                    Gui.func_73734_a((int)-2, (int)-5, (int)(iFontRenderer.getStringWidth(this.displayText) + 2), (int)iFontRenderer.getFontHeight(), (int)new Color(0, 0, 0, 90).getRGB());
                    RenderUtils.drawGradientSideways(-1.0, -2.0, iFontRenderer.getStringWidth(this.displayText) + 1, -4.0, n2, n5);
                }
                string = (String)this.colorModeValue.get();
                n = new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()).getRGB();
                bl = StringsKt.equals((String)string, (String)"Rainbow", (boolean)true);
                if (((Boolean)this.bord.get()).booleanValue()) {
                    double d;
                    double d2;
                    int n6;
                    int n7;
                    double d3;
                    if (((String)this.Mode.get()).equals("Skeet")) {
                        RenderUtils.autoExhibition(-4.0, -5.2, f, (double)iFontRenderer.getFontHeight() + 1.5, 1.0);
                        d3 = f;
                        n7 = 0;
                        n6 = ((Number)this.amountValue.get()).intValue() - 1;
                        if (n7 <= n6) {
                            while (true) {
                                d2 = (double)n7 / (double)((Number)this.amountValue.get()).intValue() * d3;
                                d = (double)(n7 + 1) / (double)((Number)this.amountValue.get()).intValue() * d3;
                                RenderUtils.drawGradientSideways(-1.4 + d2, -2.7, -1.4 + d, -2.0, bl ? 0 : (StringsKt.equals((String)string, (String)"Fade", (boolean)true) ? Palette.fade2(new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()), n7 * ((Number)this.distanceValue.get()).intValue(), this.displayText.length() * 200).getRGB() : (StringsKt.equals((String)string, (String)"Astolfo", (boolean)true) ? RenderUtils.Astolfo(n7 * ((Number)this.distanceValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), ((Number)this.astolfoRainbowOffset.get()).intValue(), ((Number)this.astolfoRainbowIndex.get()).intValue(), ((Number)this.astolfoclient.get()).intValue()) : (StringsKt.equals((String)string, (String)"Gident", (boolean)true) ? RenderUtils.getGradientOffset(new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()), new Color(((Number)this.colorRedValue2.get()).intValue(), ((Number)this.colorGreenValue2.get()).intValue(), ((Number)this.colorBlueValue2.get()).intValue(), 1), Math.abs((double)System.currentTimeMillis() / (double)((Number)this.gidentspeed.get()).intValue() + (double)(n7 * ((Number)this.distanceValue.get()).intValue())) / (double)10).getRGB() : (StringsKt.equals((String)string, (String)"NewRainbow", (boolean)true) ? RenderUtils.getRainbow(n7 * ((Number)this.distanceValue.get()).intValue(), ((Number)this.newRainbowIndex.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue()) : n)))), bl ? 0 : (StringsKt.equals((String)string, (String)"Fade", (boolean)true) ? Palette.fade2(new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()), n7 * ((Number)this.distanceValue.get()).intValue(), this.displayText.length() * 200).getRGB() : (StringsKt.equals((String)string, (String)"Astolfo", (boolean)true) ? RenderUtils.Astolfo(n7 * ((Number)this.distanceValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), ((Number)this.astolfoRainbowOffset.get()).intValue(), ((Number)this.astolfoRainbowIndex.get()).intValue(), ((Number)this.astolfoclient.get()).intValue()) : (StringsKt.equals((String)string, (String)"Gident", (boolean)true) ? RenderUtils.getGradientOffset(new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()), new Color(((Number)this.colorRedValue2.get()).intValue(), ((Number)this.colorGreenValue2.get()).intValue(), ((Number)this.colorBlueValue2.get()).intValue(), 1), Math.abs((double)System.currentTimeMillis() / (double)((Number)this.gidentspeed.get()).intValue() + (double)(n7 * ((Number)this.distanceValue.get()).intValue())) / (double)10).getRGB() : (StringsKt.equals((String)string, (String)"NewRainbow", (boolean)true) ? RenderUtils.getRainbow(n7 * ((Number)this.distanceValue.get()).intValue(), ((Number)this.newRainbowIndex.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue()) : n)))));
                                if (n7 == n6) break;
                                ++n7;
                            }
                        }
                    }
                    if (((String)this.Mode.get()).equals("Slide")) {
                        RenderUtils.drawRect(-4.0f, -4.5f, f, (float)iFontRenderer.getFontHeight(), new Color(0, 0, 0, ((Number)this.balpha.get()).intValue()).getRGB());
                        d3 = f + 1.0f;
                        n7 = 0;
                        n6 = ((Number)this.amountValue.get()).intValue() - 1;
                        if (n7 <= n6) {
                            while (true) {
                                d2 = (double)n7 / (double)((Number)this.amountValue.get()).intValue() * d3;
                                d = (double)(n7 + 1) / (double)((Number)this.amountValue.get()).intValue() * d3;
                                RenderUtils.drawGradientSideways(-4.0 + d2, -4.2, -1.0 + d, -3.0, bl ? 0 : (StringsKt.equals((String)string, (String)"Fade", (boolean)true) ? Palette.fade2(new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()), n7 * ((Number)this.distanceValue.get()).intValue(), this.displayText.length() * 200).getRGB() : (StringsKt.equals((String)string, (String)"Astolfo", (boolean)true) ? RenderUtils.Astolfo(n7 * ((Number)this.distanceValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), ((Number)this.astolfoRainbowOffset.get()).intValue(), ((Number)this.astolfoRainbowIndex.get()).intValue(), ((Number)this.astolfoclient.get()).intValue()) : (StringsKt.equals((String)string, (String)"Gident", (boolean)true) ? RenderUtils.getGradientOffset(new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()), new Color(((Number)this.colorRedValue2.get()).intValue(), ((Number)this.colorGreenValue2.get()).intValue(), ((Number)this.colorBlueValue2.get()).intValue(), 1), Math.abs((double)System.currentTimeMillis() / (double)((Number)this.gidentspeed.get()).intValue() + (double)(n7 * ((Number)this.distanceValue.get()).intValue())) / (double)10).getRGB() : (StringsKt.equals((String)string, (String)"NewRainbow", (boolean)true) ? RenderUtils.getRainbow(n7 * ((Number)this.distanceValue.get()).intValue(), ((Number)this.newRainbowIndex.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue()) : n)))), bl ? 0 : (StringsKt.equals((String)string, (String)"Fade", (boolean)true) ? Palette.fade2(new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()), n7 * ((Number)this.distanceValue.get()).intValue(), this.displayText.length() * 200).getRGB() : (StringsKt.equals((String)string, (String)"Astolfo", (boolean)true) ? RenderUtils.Astolfo(n7 * ((Number)this.distanceValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), ((Number)this.astolfoRainbowOffset.get()).intValue(), ((Number)this.astolfoRainbowIndex.get()).intValue(), ((Number)this.astolfoclient.get()).intValue()) : (StringsKt.equals((String)string, (String)"Gident", (boolean)true) ? RenderUtils.getGradientOffset(new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()), new Color(((Number)this.colorRedValue2.get()).intValue(), ((Number)this.colorGreenValue2.get()).intValue(), ((Number)this.colorBlueValue2.get()).intValue(), 1), Math.abs((double)System.currentTimeMillis() / (double)((Number)this.gidentspeed.get()).intValue() + (double)(n7 * ((Number)this.distanceValue.get()).intValue())) / (double)10).getRGB() : (StringsKt.equals((String)string, (String)"NewRainbow", (boolean)true) ? RenderUtils.getRainbow(n7 * ((Number)this.distanceValue.get()).intValue(), ((Number)this.newRainbowIndex.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue()) : n)))));
                                if (n7 == n6) break;
                                ++n7;
                            }
                        }
                    }
                }
                nArray = new int[]{0};
                if (!((Boolean)this.char.get()).booleanValue()) break block43;
                boolean bl2 = StringsKt.equals((String)string, (String)"Rainbow", (boolean)true);
                float f2 = ((Number)this.rainbowX.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowX.get()).floatValue();
                float f3 = ((Number)this.rainbowY.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowY.get()).floatValue();
                float f4 = (float)(System.currentTimeMillis() % (long)10000) / 10000.0f;
                boolean bl3 = false;
                if (bl2) {
                    RainbowFontShader.INSTANCE.setStrengthX(f2);
                    RainbowFontShader.INSTANCE.setStrengthY(f3);
                    RainbowFontShader.INSTANCE.setOffset(f4);
                    RainbowFontShader.INSTANCE.startShader();
                }
                Closeable closeable = RainbowFontShader.INSTANCE;
                boolean bl4 = false;
                Throwable throwable = null;
                try {
                    RainbowFontShader rainbowFontShader = (RainbowFontShader)closeable;
                    boolean bl5 = false;
                    iFontRenderer.drawString(this.displayText, 0.0f, 0.0f, bl2 ? 0 : (StringsKt.equals((String)string, (String)"Fade", (boolean)true) ? Palette.fade2(new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()), nArray[0] * 100, this.displayText.length() * 200).getRGB() : (StringsKt.equals((String)string, (String)"Astolfo", (boolean)true) ? RenderUtils.Astolfo(nArray[0] * 100, ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), ((Number)this.astolfoRainbowOffset.get()).intValue(), ((Number)this.astolfoRainbowIndex.get()).intValue(), ((Number)this.astolfoclient.get()).intValue()) : (StringsKt.equals((String)string, (String)"NewRainbow", (boolean)true) ? RenderUtils.getRainbow(nArray[0] * 100, ((Number)this.newRainbowIndex.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue()) : (StringsKt.equals((String)string, (String)"Gident", (boolean)true) ? RenderUtils.getGradientOffset(new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()), new Color(((Number)this.colorRedValue2.get()).intValue(), ((Number)this.colorGreenValue2.get()).intValue(), ((Number)this.colorBlueValue2.get()).intValue(), 1), Math.abs((double)System.currentTimeMillis() / (double)((Number)this.gidentspeed.get()).intValue() + (double)nArray[0]) / (double)10).getRGB() : n)))), (Boolean)this.shadow.get());
                    if (this.editMode && MinecraftInstance.classProvider.isGuiHudDesigner(MinecraftInstance.mc.getCurrentScreen()) && this.editTicks <= 40) {
                        iFontRenderer.drawString("_", iFontRenderer.getStringWidth(this.displayText), 0.0f, bl2 ? 0 : (StringsKt.equals((String)string, (String)"Fade", (boolean)true) ? Palette.fade2(new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()), nArray[0] * 100, this.displayText.length() * 200).getRGB() : (StringsKt.equals((String)string, (String)"Astolfo", (boolean)true) ? RenderUtils.Astolfo(nArray[0] * 100, ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), ((Number)this.astolfoRainbowOffset.get()).intValue(), ((Number)this.astolfoRainbowIndex.get()).intValue(), ((Number)this.astolfoclient.get()).intValue()) : (StringsKt.equals((String)string, (String)"Gident", (boolean)true) ? RenderUtils.getGradientOffset(new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()), new Color(((Number)this.colorRedValue2.get()).intValue(), ((Number)this.colorGreenValue2.get()).intValue(), ((Number)this.colorBlueValue2.get()).intValue(), 1), Math.abs((double)System.currentTimeMillis() / (double)((Number)this.gidentspeed.get()).intValue() + (double)nArray[0]) / (double)10).getRGB() : (StringsKt.equals((String)string, (String)"NewRainbow", (boolean)true) ? RenderUtils.getRainbow(nArray[0] * 100, ((Number)this.newRainbowIndex.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue()) : n)))), (Boolean)this.shadow.get());
                    }
                    nArray[0] = nArray[0] + 1;
                    rainbowFontShader = Unit.INSTANCE;
                }
                catch (Throwable throwable2) {
                    throwable = throwable2;
                    throw throwable2;
                }
                CloseableKt.closeFinally((Closeable)closeable, (Throwable)throwable);
                break block44;
            }
            int n8 = 0;
            float f5 = ((Number)this.rainbowX.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowX.get()).floatValue();
            float f6 = ((Number)this.rainbowY.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowY.get()).floatValue();
            float f7 = (float)(System.currentTimeMillis() % (long)10000) / 10000.0f;
            boolean bl6 = false;
            if (bl) {
                RainbowFontShader.INSTANCE.setStrengthX(f5);
                RainbowFontShader.INSTANCE.setStrengthY(f6);
                RainbowFontShader.INSTANCE.setOffset(f7);
                RainbowFontShader.INSTANCE.startShader();
            }
            Closeable closeable = RainbowFontShader.INSTANCE;
            boolean bl7 = false;
            Throwable throwable = null;
            try {
                RainbowFontShader rainbowFontShader = (RainbowFontShader)closeable;
                boolean bl8 = false;
                for (char c : cArray) {
                    boolean bl9 = StringsKt.equals((String)string, (String)"Rainbow", (boolean)true);
                    iFontRenderer.drawString(String.valueOf(c), n8, 0.0f, bl9 ? 0 : (StringsKt.equals((String)string, (String)"Fade", (boolean)true) ? Palette.fade2(new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()), nArray[0] * 100, this.displayText.length() * 200).getRGB() : (StringsKt.equals((String)string, (String)"Astolfo", (boolean)true) ? RenderUtils.Astolfo(nArray[0] * 100, ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), ((Number)this.astolfoRainbowOffset.get()).intValue(), ((Number)this.astolfoRainbowIndex.get()).intValue(), ((Number)this.astolfoclient.get()).intValue()) : (StringsKt.equals((String)string, (String)"NewRainbow", (boolean)true) ? RenderUtils.getRainbow(nArray[0] * 100, ((Number)this.newRainbowIndex.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue()) : (StringsKt.equals((String)string, (String)"Gident", (boolean)true) ? RenderUtils.getGradientOffset(new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()), new Color(((Number)this.colorRedValue2.get()).intValue(), ((Number)this.colorGreenValue2.get()).intValue(), ((Number)this.colorBlueValue2.get()).intValue(), 1), Math.abs((double)System.currentTimeMillis() / (double)((Number)this.gidentspeed.get()).intValue() + (double)nArray[0]) / (double)10).getRGB() : n)))), (Boolean)this.shadow.get());
                    nArray[0] = nArray[0] + 1;
                    nArray[0] = RangesKt.coerceIn((int)nArray[0], (int)0, (int)this.displayText.length());
                    n8 += iFontRenderer.getStringWidth(String.valueOf(c));
                }
                if (this.editMode && MinecraftInstance.classProvider.isGuiHudDesigner(MinecraftInstance.mc.getCurrentScreen()) && this.editTicks <= 40) {
                    iFontRenderer.drawString("_", f, 0.0f, bl ? 0 : (StringsKt.equals((String)string, (String)"Fade", (boolean)true) ? Palette.fade2(new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()), nArray[0] * 100, this.displayText.length() * 200).getRGB() : (StringsKt.equals((String)string, (String)"Astolfo", (boolean)true) ? RenderUtils.Astolfo(nArray[0] * 100, ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), ((Number)this.astolfoRainbowOffset.get()).intValue(), ((Number)this.astolfoRainbowIndex.get()).intValue(), ((Number)this.astolfoclient.get()).intValue()) : (StringsKt.equals((String)string, (String)"Gident", (boolean)true) ? RenderUtils.getGradientOffset(new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()), new Color(((Number)this.colorRedValue2.get()).intValue(), ((Number)this.colorGreenValue2.get()).intValue(), ((Number)this.colorBlueValue2.get()).intValue(), 1), Math.abs((double)System.currentTimeMillis() / (double)((Number)this.gidentspeed.get()).intValue() + (double)nArray[0]) / (double)10).getRGB() : (StringsKt.equals((String)string, (String)"NewRainbow", (boolean)true) ? RenderUtils.getRainbow(nArray[0] * 100, ((Number)this.newRainbowIndex.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue()) : n)))), (Boolean)this.shadow.get());
                }
                rainbowFontShader = Unit.INSTANCE;
            }
            catch (Throwable throwable3) {
                throwable = throwable3;
                throw throwable3;
            }
            CloseableKt.closeFinally((Closeable)closeable, (Throwable)throwable);
        }
        if (this.editMode && !MinecraftInstance.classProvider.isGuiHudDesigner(MinecraftInstance.mc.getCurrentScreen())) {
            this.editMode = false;
            this.updateElement();
        }
        return new Border(-2.0f, -2.0f, f, iFontRenderer.getFontHeight());
    }

    public static final SimpleDateFormat access$getHOUR_FORMAT$cp() {
        return HOUR_FORMAT;
    }

    public final MSTimer getSlidetimer() {
        return this.slidetimer;
    }

    private final String multiReplace(String string) {
        int n = -1;
        StringBuilder stringBuilder = new StringBuilder();
        int n2 = ((CharSequence)string).length();
        for (int i = 0; i < n2; ++i) {
            if (string.charAt(i) == '%') {
                if (n != -1) {
                    if (n + 1 != i) {
                        String string2 = string;
                        int n3 = n + 1;
                        Text text = this;
                        boolean bl = false;
                        String string3 = string2;
                        if (string3 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        String string4 = string3.substring(n3, i);
                        String string5 = text.getReplacement(string4);
                        if (string5 != null) {
                            stringBuilder.append(string5);
                            n = -1;
                            continue;
                        }
                    }
                    stringBuilder.append(string, n, i);
                }
                n = i;
                continue;
            }
            if (n != -1) continue;
            stringBuilder.append(string.charAt(i));
        }
        if (n != -1) {
            stringBuilder.append(string, n, string.length());
        }
        return stringBuilder.toString();
    }

    public final Text setColor(Color color) {
        this.redValue.set((Object)color.getRed());
        this.greenValue.set((Object)color.getGreen());
        this.blueValue.set((Object)color.getBlue());
        return this;
    }

    public static final SimpleDateFormat access$getDATE_FORMAT$cp() {
        return DATE_FORMAT;
    }

    public Text() {
        this(0.0, 0.0, 0.0f, null, 15, null);
    }

    private final String getReplacement(String string) {
        String string2;
        if (MinecraftInstance.mc.getThePlayer() != null) {
            switch (string) {
                case "x": {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    return DECIMAL_FORMAT.format(iEntityPlayerSP.getPosX());
                }
                case "y": {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    return DECIMAL_FORMAT.format(iEntityPlayerSP.getPosY());
                }
                case "z": {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    return DECIMAL_FORMAT.format(iEntityPlayerSP.getPosZ());
                }
                case "xInt": {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    return DECIMAL_FORMAT_INT.format(iEntityPlayerSP.getPosX());
                }
                case "yInt": {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    return DECIMAL_FORMAT_INT.format(iEntityPlayerSP.getPosY());
                }
                case "zInt": {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    return DECIMAL_FORMAT_INT.format(iEntityPlayerSP.getPosZ());
                }
                case "xdp": {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    return String.valueOf(iEntityPlayerSP.getPosX());
                }
                case "ydp": {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    return String.valueOf(iEntityPlayerSP.getPosY());
                }
                case "zdp": {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    return String.valueOf(iEntityPlayerSP.getPosZ());
                }
                case "velocity": {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    double d = iEntityPlayerSP.getMotionX();
                    IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP2 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d2 = d * iEntityPlayerSP2.getMotionX();
                    IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP3 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d3 = iEntityPlayerSP3.getMotionZ();
                    IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP4 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d4 = d2 + d3 * iEntityPlayerSP4.getMotionZ();
                    DecimalFormat decimalFormat = DECIMAL_FORMAT;
                    boolean bl = false;
                    double d5 = Math.sqrt(d4);
                    return decimalFormat.format(d5);
                }
                case "ping": {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    return String.valueOf(EntityUtils.getPing(iEntityPlayerSP));
                }
                case "health": {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    return DECIMAL_FORMAT.format(Float.valueOf(iEntityPlayerSP.getHealth()));
                }
                case "maxHealth": {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    return DECIMAL_FORMAT.format(Float.valueOf(iEntityPlayerSP.getMaxHealth()));
                }
                case "healthInt": {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    return DECIMAL_FORMAT_INT.format(Float.valueOf(iEntityPlayerSP.getHealth()));
                }
                case "maxHealthInt": {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    return DECIMAL_FORMAT_INT.format(Float.valueOf(iEntityPlayerSP.getMaxHealth()));
                }
                case "yaw": {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    return DECIMAL_FORMAT.format(Float.valueOf(iEntityPlayerSP.getRotationYaw()));
                }
                case "pitch": {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    return DECIMAL_FORMAT.format(Float.valueOf(iEntityPlayerSP.getRotationPitch()));
                }
                case "yawInt": {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    return DECIMAL_FORMAT_INT.format(Float.valueOf(iEntityPlayerSP.getRotationYaw()));
                }
                case "pitchInt": {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    return DECIMAL_FORMAT_INT.format(Float.valueOf(iEntityPlayerSP.getRotationPitch()));
                }
                case "bps": {
                    return this.speedStr;
                }
                case "hurtTime": {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    return String.valueOf(iEntityPlayerSP.getHurtTime());
                }
                case "onGround": {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    return String.valueOf(iEntityPlayerSP.getOnGround());
                }
            }
        }
        switch (string) {
            case "hwidname": {
                string2 = HWIDUtil.username;
                break;
            }
            case "qqnumber": {
                string2 = QQUtils.qqNumber;
                break;
            }
            case "username": {
                string2 = MinecraftInstance.mc2.func_110432_I().func_111285_a();
                break;
            }
            case "clientname": {
                string2 = "AtField";
                break;
            }
            case "clientversion": {
                string2 = "bv1.3";
                break;
            }
            case "clientcreator": {
                string2 = "Sakura\u00b7StarRing";
                break;
            }
            case "fps": {
                string2 = String.valueOf(Minecraft.func_175610_ah());
                break;
            }
            case "date": {
                string2 = DATE_FORMAT.format(System.currentTimeMillis());
                break;
            }
            case "time": {
                string2 = HOUR_FORMAT.format(System.currentTimeMillis());
                break;
            }
            case "serverip": {
                string2 = ServerUtils.getRemoteIp();
                break;
            }
            case "cps": 
            case "lcps": {
                return String.valueOf(CPSCounter.getCPS(CPSCounter.MouseButton.LEFT));
            }
            case "mcps": {
                return String.valueOf(CPSCounter.getCPS(CPSCounter.MouseButton.MIDDLE));
            }
            case "rcps": {
                return String.valueOf(CPSCounter.getCPS(CPSCounter.MouseButton.RIGHT));
            }
            case "userName": {
                string2 = MinecraftInstance.mc.getSession().getUsername();
                break;
            }
            case "clientName": {
                string2 = "AtField";
                break;
            }
            case "clientVersion": {
                string2 = "v1.3".toString();
                break;
            }
            case "clientCreator": {
                string2 = "Sakura\u00b7StarRing";
                break;
            }
            case "fps": {
                string2 = String.valueOf(Minecraft.func_175610_ah());
                break;
            }
            case "date": {
                string2 = DATE_FORMAT.format(System.currentTimeMillis());
                break;
            }
            case "time": {
                string2 = HOUR_FORMAT.format(System.currentTimeMillis());
                break;
            }
            case "serverIp": {
                string2 = ServerUtils.getRemoteIp();
                break;
            }
            case "cps": 
            case "lcps": {
                return String.valueOf(CPSCounter.getCPS(CPSCounter.MouseButton.LEFT));
            }
            case "mcps": {
                return String.valueOf(CPSCounter.getCPS(CPSCounter.MouseButton.MIDDLE));
            }
            case "rcps": {
                return String.valueOf(CPSCounter.getCPS(CPSCounter.MouseButton.RIGHT));
            }
            default: {
                string2 = null;
            }
        }
        return string2;
    }

    public final void glColor(int n, int n2, int n3, int n4) {
        GlStateManager.func_179131_c((float)((float)n / 255.0f), (float)((float)n2 / 255.0f), (float)((float)n3 / 255.0f), (float)((float)n4 / 255.0f));
    }

    public final void setSlidetimer(MSTimer mSTimer) {
        this.slidetimer = mSTimer;
    }

    static {
        Companion = new Companion(null);
        DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        HOUR_FORMAT = new SimpleDateFormat("HH:mm");
        DECIMAL_FORMAT = new DecimalFormat("0.00");
        DECIMAL_FORMAT_INT = new DecimalFormat("0");
    }

    @Override
    public void handleMouseClick(double d, double d2, int n) {
        if (this.isInBorder(d, d2) && n == 0) {
            if (System.currentTimeMillis() - this.prevClick <= 250L) {
                this.editMode = true;
            }
            this.prevClick = System.currentTimeMillis();
        } else {
            this.editMode = false;
        }
    }

    public Text(double d, double d2, float f, Side side) {
        super(d, d2, f, side);
        this.colorModeValue = new ListValue("Text-Color", new String[]{"Custom", "Rainbow", "Fade", "Astolfo", "NewRainbow", "Gident"}, "Custom");
        this.brightnessValue = new FloatValue("Brightness", 1.0f, 0.0f, 1.0f);
        this.displayString = new TextValue("DisplayText", "");
        this.redValue = new IntegerValue("Text-R", 255, 0, 255);
        this.greenValue = new IntegerValue("Text-G", 255, 0, 255);
        this.blueValue = new IntegerValue("Text-B", 255, 0, 255);
        this.colorRedValue2 = new IntegerValue("Text-R2", 0, 0, 255);
        this.colorGreenValue2 = new IntegerValue("Text-G2", 111, 0, 255);
        this.colorBlueValue2 = new IntegerValue("Text-B2", 255, 0, 255);
        this.blurValue = new BoolValue("Top-Blur", true);
        this.shadowValue = new ListValue("Top-Shadow", new String[]{"None", "Basic", "Thick"}, "Thick");
        this.radiusValue = new FloatValue("Top-Radius", 4.25f, 0.0f, 10.0f);
        this.gidentspeed = new IntegerValue("GidentSpeed", 100, 1, 1000);
        this.newRainbowIndex = new IntegerValue("NewRainbowOffset", 1, 1, 50);
        this.astolfoRainbowOffset = new IntegerValue("AstolfoOffset", 5, 1, 20);
        this.astolfoclient = new IntegerValue("AstolfoRange", 109, 1, 765);
        this.astolfoRainbowIndex = new IntegerValue("AstolfoIndex", 109, 1, 300);
        this.saturationValue = new FloatValue("Saturation", 0.9f, 0.0f, 1.0f);
        this.Mode = new ListValue("Border-Mode", new String[]{"Slide", "Skeet", "Top", "Onetap"}, "Top");
        this.rainbowX = new FloatValue("Rainbow-X", -1000.0f, -2000.0f, 2000.0f);
        this.rainbowY = new FloatValue("Rainbow-Y", -1000.0f, -2000.0f, 2000.0f);
        this.shadow = new BoolValue("Shadow", true);
        this.bord = new BoolValue("Border", true);
        this.slide = new BoolValue("Slide", false);
        this.char = new BoolValue("NotChar", false);
        this.slidedelay = new IntegerValue("SlideDelay", 100, 0, 1000);
        this.balpha = new IntegerValue("BordAlpha", 255, 0, 255);
        this.distanceValue = new IntegerValue("Distance", 0, 0, 400);
        this.amountValue = new IntegerValue("Amount", 25, 1, 50);
        this.fontValue = new FontValue("Font", Fonts.productSans40);
        this.speedStr = "";
        this.displayText = "";
        this.slidetimer = new MSTimer();
        this.doslide = true;
    }

    public static final DecimalFormat access$getDECIMAL_FORMAT_INT$cp() {
        return DECIMAL_FORMAT_INT;
    }

    @Override
    public void updateElement() {
        this.editTicks += 5;
        if (this.editTicks > 80) {
            this.editTicks = 0;
        }
        this.displayText = this.editMode ? (String)this.displayString.get() : this.getDisplay();
    }

    @Override
    public void handleKey(char c, int n) {
        if (this.editMode && MinecraftInstance.classProvider.isGuiHudDesigner(MinecraftInstance.mc.getCurrentScreen())) {
            if (n == 14) {
                CharSequence charSequence = (CharSequence)this.displayString.get();
                int n2 = 0;
                if (charSequence.length() > 0) {
                    charSequence = (String)this.displayString.get();
                    n2 = 0;
                    int n3 = ((String)this.displayString.get()).length() - 1;
                    TextValue textValue = this.displayString;
                    boolean bl = false;
                    CharSequence charSequence2 = charSequence;
                    if (charSequence2 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    String string = ((String)charSequence2).substring(n2, n3);
                    textValue.set(string);
                }
                this.updateElement();
                return;
            }
            if (ChatAllowedCharacters.func_71566_a((char)c) || c == '\u00a7') {
                this.displayString.set((String)this.displayString.get() + c);
            }
            this.updateElement();
        }
    }

    public final void setSlidetext(int n) {
        this.slidetext = n;
    }

    public final void glColor(int n) {
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        GlStateManager.func_179131_c((float)f2, (float)f3, (float)f4, (float)f);
    }

    public final void drawRect(double d, double d2, double d3, double d4, int n) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        this.glColor(n);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)d3, (double)d2);
        GL11.glVertex2d((double)d, (double)d2);
        GL11.glVertex2d((double)d, (double)d4);
        GL11.glVertex2d((double)d3, (double)d4);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public final boolean getDoslide() {
        return this.doslide;
    }

    private final String getDisplay() {
        CharSequence charSequence = (CharSequence)this.displayString.get();
        boolean bl = false;
        String string = charSequence.length() == 0 && !this.editMode ? "AtField | Fps:%fps% | %serverip%" : (String)this.displayString.get();
        return this.multiReplace(string);
    }

    public static final class Companion {
        public final Text defaultClient() {
            Text text = new Text(2.0, 2.0, 2.0f, null, 8, null);
            Text.access$getDisplayString$p(text).set("AtField | Fps:%fps% | %serverip%");
            Text.access$getShadow$p(text).set(true);
            Text.access$getFontValue$p(text).set(Fonts.roboto40);
            text.setColor(new Color(0, 111, 255));
            return text;
        }

        private Companion() {
        }

        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final SimpleDateFormat getHOUR_FORMAT() {
            return Text.access$getHOUR_FORMAT$cp();
        }

        public final DecimalFormat getDECIMAL_FORMAT() {
            return Text.access$getDECIMAL_FORMAT$cp();
        }

        public final DecimalFormat getDECIMAL_FORMAT_INT() {
            return Text.access$getDECIMAL_FORMAT_INT$cp();
        }

        public final SimpleDateFormat getDATE_FORMAT() {
            return Text.access$getDATE_FORMAT$cp();
        }
    }
}

