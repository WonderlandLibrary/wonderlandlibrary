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

import java.awt.Color;
import java.io.Closeable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import jx.utils.render.BlurBuffer;
import jx.utils.render.RoundedUtil;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.modules.hyt.VisualColor;
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
    private final ListValue colorModeValue;
    private final IntegerValue colorRedValue;
    private final IntegerValue colorGreenValue;
    private final IntegerValue colorBlueValue;
    private final FloatValue brightnessValue;
    private final TextValue displayString;
    private final BoolValue blurValue;
    private final ListValue shadowValue;
    private final FloatValue radiusValue;
    private final IntegerValue gidentspeed;
    private final IntegerValue newRainbowIndex;
    private final IntegerValue astolfoRainbowOffset;
    private final IntegerValue astolfoclient;
    private final IntegerValue astolfoRainbowIndex;
    private final FloatValue saturationValue;
    private final ListValue Mode;
    private final FloatValue rainbowX;
    private final FloatValue rainbowY;
    private final BoolValue shadow;
    private final BoolValue bord;
    private final BoolValue slide;
    private final BoolValue char;
    private final IntegerValue slidedelay;
    private final IntegerValue balpha;
    private final IntegerValue distanceValue;
    private final IntegerValue amountValue;
    private FontValue fontValue;
    private boolean editMode;
    private int editTicks;
    private long prevClick;
    private String speedStr;
    private String displayText;
    private int slidetext;
    private MSTimer slidetimer;
    private boolean doslide;
    private static final SimpleDateFormat DATE_FORMAT;
    private static final SimpleDateFormat HOUR_FORMAT;
    private static final DecimalFormat DECIMAL_FORMAT;
    private static final DecimalFormat DECIMAL_FORMAT_INT;
    public static final Companion Companion;

    public final IntegerValue getColorRedValue() {
        return this.colorRedValue;
    }

    public final IntegerValue getColorGreenValue() {
        return this.colorGreenValue;
    }

    public final IntegerValue getColorBlueValue() {
        return this.colorBlueValue;
    }

    private final String getDisplay() {
        CharSequence charSequence = (CharSequence)this.displayString.get();
        boolean bl = false;
        String textContent = charSequence.length() == 0 && !this.editMode ? "LiqKiller | Fps:%fps% | %serverip%" : (String)this.displayString.get();
        return this.multiReplace(textContent);
    }

    /*
     * WARNING - bad return control flow
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private final String getReplacement(String str) {
        String string;
        block79: {
            block81: {
                block74: {
                    block75: {
                        block77: {
                            block76: {
                                block78: {
                                    block80: {
                                        block73: {
                                            if (MinecraftInstance.mc.getThePlayer() != null) {
                                                switch (str) {
                                                    case "x": {
                                                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                                                        if (iEntityPlayerSP != null) return DECIMAL_FORMAT.format(iEntityPlayerSP.getPosX());
                                                        Intrinsics.throwNpe();
                                                        return DECIMAL_FORMAT.format(iEntityPlayerSP.getPosX());
                                                    }
                                                    case "y": {
                                                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                                                        if (iEntityPlayerSP != null) return DECIMAL_FORMAT.format(iEntityPlayerSP.getPosY());
                                                        Intrinsics.throwNpe();
                                                        return DECIMAL_FORMAT.format(iEntityPlayerSP.getPosY());
                                                    }
                                                    case "z": {
                                                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                                                        if (iEntityPlayerSP != null) return DECIMAL_FORMAT.format(iEntityPlayerSP.getPosZ());
                                                        Intrinsics.throwNpe();
                                                        return DECIMAL_FORMAT.format(iEntityPlayerSP.getPosZ());
                                                    }
                                                    case "xInt": {
                                                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                                                        if (iEntityPlayerSP != null) return DECIMAL_FORMAT_INT.format(iEntityPlayerSP.getPosX());
                                                        Intrinsics.throwNpe();
                                                        return DECIMAL_FORMAT_INT.format(iEntityPlayerSP.getPosX());
                                                    }
                                                    case "yInt": {
                                                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                                                        if (iEntityPlayerSP != null) return DECIMAL_FORMAT_INT.format(iEntityPlayerSP.getPosY());
                                                        Intrinsics.throwNpe();
                                                        return DECIMAL_FORMAT_INT.format(iEntityPlayerSP.getPosY());
                                                    }
                                                    case "zInt": {
                                                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                                                        if (iEntityPlayerSP == null) {
                                                            Intrinsics.throwNpe();
                                                        }
                                                        if (iEntityPlayerSP != null) return DECIMAL_FORMAT_INT.format(iEntityPlayerSP.getPosZ());
                                                        Intrinsics.throwNpe();
                                                        return DECIMAL_FORMAT_INT.format(iEntityPlayerSP.getPosZ());
                                                    }
                                                    case "xdp": {
                                                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                                                        if (iEntityPlayerSP != null) return String.valueOf(iEntityPlayerSP.getPosX());
                                                        Intrinsics.throwNpe();
                                                        return String.valueOf(iEntityPlayerSP.getPosX());
                                                    }
                                                    case "ydp": {
                                                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                                                        if (iEntityPlayerSP != null) return String.valueOf(iEntityPlayerSP.getPosY());
                                                        Intrinsics.throwNpe();
                                                        return String.valueOf(iEntityPlayerSP.getPosY());
                                                    }
                                                    case "zdp": {
                                                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                                                        if (iEntityPlayerSP != null) return String.valueOf(iEntityPlayerSP.getPosZ());
                                                        Intrinsics.throwNpe();
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
                                                        if (iEntityPlayerSP != null) return String.valueOf(EntityUtils.getPing(iEntityPlayerSP));
                                                        Intrinsics.throwNpe();
                                                        return String.valueOf(EntityUtils.getPing(iEntityPlayerSP));
                                                    }
                                                    case "health": {
                                                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                                                        if (iEntityPlayerSP != null) return DECIMAL_FORMAT.format(Float.valueOf(iEntityPlayerSP.getHealth()));
                                                        Intrinsics.throwNpe();
                                                        return DECIMAL_FORMAT.format(Float.valueOf(iEntityPlayerSP.getHealth()));
                                                    }
                                                    case "maxHealth": {
                                                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                                                        if (iEntityPlayerSP != null) return DECIMAL_FORMAT.format(Float.valueOf(iEntityPlayerSP.getMaxHealth()));
                                                        Intrinsics.throwNpe();
                                                        return DECIMAL_FORMAT.format(Float.valueOf(iEntityPlayerSP.getMaxHealth()));
                                                    }
                                                    case "healthInt": {
                                                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                                                        if (iEntityPlayerSP != null) return DECIMAL_FORMAT_INT.format(Float.valueOf(iEntityPlayerSP.getHealth()));
                                                        Intrinsics.throwNpe();
                                                        return DECIMAL_FORMAT_INT.format(Float.valueOf(iEntityPlayerSP.getHealth()));
                                                    }
                                                    case "maxHealthInt": {
                                                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                                                        if (iEntityPlayerSP != null) return DECIMAL_FORMAT_INT.format(Float.valueOf(iEntityPlayerSP.getMaxHealth()));
                                                        Intrinsics.throwNpe();
                                                        return DECIMAL_FORMAT_INT.format(Float.valueOf(iEntityPlayerSP.getMaxHealth()));
                                                    }
                                                    case "yaw": {
                                                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                                                        if (iEntityPlayerSP != null) return DECIMAL_FORMAT.format(Float.valueOf(iEntityPlayerSP.getRotationYaw()));
                                                        Intrinsics.throwNpe();
                                                        return DECIMAL_FORMAT.format(Float.valueOf(iEntityPlayerSP.getRotationYaw()));
                                                    }
                                                    case "pitch": {
                                                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                                                        if (iEntityPlayerSP != null) return DECIMAL_FORMAT.format(Float.valueOf(iEntityPlayerSP.getRotationPitch()));
                                                        Intrinsics.throwNpe();
                                                        return DECIMAL_FORMAT.format(Float.valueOf(iEntityPlayerSP.getRotationPitch()));
                                                    }
                                                    case "yawInt": {
                                                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                                                        if (iEntityPlayerSP != null) return DECIMAL_FORMAT_INT.format(Float.valueOf(iEntityPlayerSP.getRotationYaw()));
                                                        Intrinsics.throwNpe();
                                                        return DECIMAL_FORMAT_INT.format(Float.valueOf(iEntityPlayerSP.getRotationYaw()));
                                                    }
                                                    case "pitchInt": {
                                                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                                                        if (iEntityPlayerSP != null) return DECIMAL_FORMAT_INT.format(Float.valueOf(iEntityPlayerSP.getRotationPitch()));
                                                        Intrinsics.throwNpe();
                                                        return DECIMAL_FORMAT_INT.format(Float.valueOf(iEntityPlayerSP.getRotationPitch()));
                                                    }
                                                    case "bps": {
                                                        return this.speedStr;
                                                    }
                                                    case "hurtTime": {
                                                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                                                        if (iEntityPlayerSP != null) return String.valueOf(iEntityPlayerSP.getHurtTime());
                                                        Intrinsics.throwNpe();
                                                        return String.valueOf(iEntityPlayerSP.getHurtTime());
                                                    }
                                                    case "onGround": {
                                                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                                                        if (iEntityPlayerSP != null) return String.valueOf(iEntityPlayerSP.getOnGround());
                                                        Intrinsics.throwNpe();
                                                        return String.valueOf(iEntityPlayerSP.getOnGround());
                                                    }
                                                }
                                            }
                                            String string2 = str;
                                            switch (string2.hashCode()) {
                                                case 3076014: {
                                                    if (string2.equals("date")) break block73;
                                                    if (!string2.equals("date")) return null;
                                                    break block74;
                                                }
                                                case 3494900: {
                                                    if (string2.equals("rcps")) return String.valueOf(CPSCounter.getCPS(CPSCounter.MouseButton.RIGHT));
                                                    if (!string2.equals("rcps")) return null;
                                                    return String.valueOf(CPSCounter.getCPS(CPSCounter.MouseButton.RIGHT));
                                                }
                                                case -215825919: {
                                                    if (!string2.equals("clientcreator")) return null;
                                                    return "CCBlueX";
                                                }
                                                case -892772691: {
                                                    if (!string2.equals("clientversion")) return null;
                                                    return "b1.0";
                                                }
                                                case 1102251254: {
                                                    if (!string2.equals("clientName")) return null;
                                                    return "LRQ";
                                                }
                                                case 98726: {
                                                    if (string2.equals("cps")) return String.valueOf(CPSCounter.getCPS(CPSCounter.MouseButton.LEFT));
                                                    if (!string2.equals("cps")) return null;
                                                    return String.valueOf(CPSCounter.getCPS(CPSCounter.MouseButton.LEFT));
                                                }
                                                case 101609: {
                                                    if (string2.equals("fps")) break;
                                                    if (!string2.equals("fps")) return null;
                                                    break block75;
                                                }
                                                case 3316154: {
                                                    if (string2.equals("lcps")) return String.valueOf(CPSCounter.getCPS(CPSCounter.MouseButton.LEFT));
                                                    if (!string2.equals("lcps")) return null;
                                                    return String.valueOf(CPSCounter.getCPS(CPSCounter.MouseButton.LEFT));
                                                }
                                                case 3345945: {
                                                    if (string2.equals("mcps")) return String.valueOf(CPSCounter.getCPS(CPSCounter.MouseButton.MIDDLE));
                                                    if (!string2.equals("mcps")) return null;
                                                    return String.valueOf(CPSCounter.getCPS(CPSCounter.MouseButton.MIDDLE));
                                                }
                                                case -266666762: {
                                                    if (!string2.equals("userName")) return null;
                                                    break block76;
                                                }
                                                case 771880589: {
                                                    if (!string2.equals("clientVersion")) return null;
                                                    break block77;
                                                }
                                                case 1103204566: {
                                                    if (!string2.equals("clientname")) return null;
                                                    return "LRQ";
                                                }
                                                case 1379104682: {
                                                    if (!string2.equals("serverip")) return null;
                                                    break block78;
                                                }
                                                case 1379103690: {
                                                    if (!string2.equals("serverIp")) return null;
                                                    break block79;
                                                }
                                                case 1448827361: {
                                                    if (!string2.equals("clientCreator")) return null;
                                                    return "CCBlueX";
                                                }
                                                case 3560141: {
                                                    if (string2.equals("time")) break block80;
                                                    if (!string2.equals("time")) return null;
                                                    break block81;
                                                }
                                                case -265713450: {
                                                    if (!string2.equals("username")) return null;
                                                    string = MinecraftInstance.mc2.func_110432_I().func_111285_a();
                                                    return string;
                                                }
                                            }
                                            string = String.valueOf(Minecraft.func_175610_ah());
                                            return string;
                                        }
                                        string = DATE_FORMAT.format(System.currentTimeMillis());
                                        return string;
                                    }
                                    string = HOUR_FORMAT.format(System.currentTimeMillis());
                                    return string;
                                }
                                string = ServerUtils.getRemoteIp();
                                return string;
                            }
                            string = MinecraftInstance.mc.getSession().getUsername();
                            return string;
                        }
                        string = String.valueOf(1.0);
                        return string;
                    }
                    string = String.valueOf(Minecraft.func_175610_ah());
                    return string;
                }
                string = DATE_FORMAT.format(System.currentTimeMillis());
                return string;
            }
            string = HOUR_FORMAT.format(System.currentTimeMillis());
            return string;
        }
        string = ServerUtils.getRemoteIp();
        return string;
        return null;
    }

    /*
     * Unable to fully structure code
     */
    private final String multiReplace(String str) {
        lastPercent = -1;
        result = new StringBuilder();
        var4_4 = 0;
        var5_5 = ((CharSequence)str).length();
        while (var4_4 < var5_5) {
            block7: {
                block6: {
                    if (str.charAt((int)i) != '%') break block6;
                    if (lastPercent == -1) ** GOTO lbl25
                    if (lastPercent + 1 == i) ** GOTO lbl-1000
                    var7_7 = str;
                    var8_8 = lastPercent + 1;
                    var10_10 = this;
                    var9_9 = false;
                    v0 = var7_7;
                    if (v0 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    var11_11 = v0.substring(var8_8, (int)i);
                    replacement = var10_10.getReplacement(var11_11);
                    if (replacement != null) {
                        result.append(replacement);
                        lastPercent = -1;
                    } else lbl-1000:
                    // 2 sources

                    {
                        result.append(str, lastPercent, (int)i);
lbl25:
                        // 2 sources

                        lastPercent = i;
                    }
                    break block7;
                }
                if (lastPercent == -1) {
                    result.append(str.charAt((int)i));
                }
            }
            ++i;
        }
        if (lastPercent != -1) {
            result.append(str, lastPercent, str.length());
        }
        return result.toString();
    }

    public final int getSlidetext() {
        return this.slidetext;
    }

    public final void setSlidetext(int n) {
        this.slidetext = n;
    }

    public final MSTimer getSlidetimer() {
        return this.slidetimer;
    }

    public final void setSlidetimer(MSTimer mSTimer) {
        this.slidetimer = mSTimer;
    }

    public final boolean getDoslide() {
        return this.doslide;
    }

    public final void setDoslide(boolean bl) {
        this.doslide = bl;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * WARNING - void declaration
     */
    @Override
    public Border drawElement() {
        IFontRenderer fontRenderer = (IFontRenderer)this.fontValue.get();
        float length2 = 4.5f;
        String string = this.displayText;
        int n = 0;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        char[] charArray = string2.toCharArray();
        if (((Boolean)this.char.get()).booleanValue()) {
            length2 = fontRenderer.getStringWidth(this.displayText);
        } else {
            for (char charIndex : charArray) {
                length2 += (float)fontRenderer.getStringWidth(String.valueOf(charIndex));
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
            String charIndex = this.getDisplay();
            n = 0;
            int n2 = this.slidetext;
            Text text = this;
            int n3 = 0;
            String string4 = charIndex;
            if (string4 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            text.displayText = string3 = string4.substring(n, n2);
        } else {
            this.displayText = this.getDisplay();
        }
        double heightVal = 11.0;
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(HUD.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.render.HUD");
        }
        HUD hud = (HUD)module;
        int mwc1 = new Color(((Number)VisualColor.Companion.getR().get()).intValue(), ((Number)VisualColor.Companion.getB().get()).intValue(), ((Number)VisualColor.Companion.getG().get()).intValue()).getRGB();
        int mwc2 = new Color(((Number)VisualColor.Companion.getR2().get()).intValue(), ((Number)VisualColor.Companion.getB2().get()).intValue(), ((Number)VisualColor.Companion.getG2().get()).intValue()).getRGB();
        if (((String)this.Mode.get()).equals("Top")) {
            switch ((String)this.shadowValue.get()) {
                case "Basic": {
                    RenderUtils.drawShadow(-2.5f, -2.5f, (float)(fontRenderer.getStringWidth(this.displayText) + 2), (float)fontRenderer.getFontHeight());
                    break;
                }
                case "Thick": {
                    RenderUtils.drawShadow(-2.0f, -2.0f, (float)(fontRenderer.getStringWidth(this.displayText) + 2), (float)fontRenderer.getFontHeight());
                    RenderUtils.drawShadow(-2.0f, -2.0f, (float)(fontRenderer.getStringWidth(this.displayText) + 2), (float)fontRenderer.getFontHeight());
                }
            }
            if (((Boolean)this.blurValue.get()).booleanValue()) {
                GL11.glTranslated((double)(-this.getRenderX()), (double)(-this.getRenderY()), (double)0.0);
                GL11.glPushMatrix();
                BlurBuffer.blurRoundArea((float)this.getRenderX(), (float)this.getRenderY(), fontRenderer.getStringWidth(this.displayText) + 2, fontRenderer.getFontHeight(), (int)((Number)this.radiusValue.get()).floatValue());
                GL11.glPopMatrix();
                GL11.glTranslated((double)this.getRenderX(), (double)this.getRenderY(), (double)0.0);
            }
            RoundedUtil.drawRound(-2.0f, -2.0f, fontRenderer.getStringWidth(this.displayText) + 2, fontRenderer.getFontHeight(), 0.0f, new Color(0, 0, 0, 100));
            RenderUtils.drawGradientSideways(-2.0, -3.0, fontRenderer.getStringWidth(this.displayText) + 2, -2.0, mwc1, mwc2);
        }
        if (((String)this.Mode.get()).equals("Onetap")) {
            Gui.func_73734_a((int)-2, (int)-5, (int)(fontRenderer.getStringWidth(this.displayText) + 2), (int)fontRenderer.getFontHeight(), (int)new Color(0, 0, 0, 90).getRGB());
            RenderUtils.drawGradientSideways(-1.0, -2.0, fontRenderer.getStringWidth(this.displayText) + 1, -4.0, mwc1, mwc2);
        }
        String colorMode = (String)this.colorModeValue.get();
        int color = new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue()).getRGB();
        boolean rainbow = StringsKt.equals((String)colorMode, (String)"Rainbow", (boolean)true);
        if (((Boolean)this.bord.get()).booleanValue()) {
            double barEnd;
            double barStart;
            int i;
            int n4;
            double barLength;
            if (((String)this.Mode.get()).equals("Skeet")) {
                RenderUtils.autoExhibition(-4.0, -5.2, length2, (double)fontRenderer.getFontHeight() + 1.5, 1.0);
                barLength = length2;
                int n5 = 0;
                n4 = ((Number)this.amountValue.get()).intValue() - 1;
                if (n5 <= n4) {
                    while (true) {
                        barStart = (double)i / (double)((Number)this.amountValue.get()).intValue() * barLength;
                        barEnd = (double)(i + 1) / (double)((Number)this.amountValue.get()).intValue() * barLength;
                        RenderUtils.drawGradientSideways(-1.4 + barStart, -2.7, -1.4 + barEnd, -2.0, rainbow ? 0 : (StringsKt.equals((String)colorMode, (String)"Fade", (boolean)true) ? Palette.fade2(new Color(((Number)VisualColor.Companion.getR().get()).intValue(), ((Number)VisualColor.Companion.getB().get()).intValue(), ((Number)VisualColor.Companion.getG().get()).intValue()), i * ((Number)this.distanceValue.get()).intValue(), this.displayText.length() * 200).getRGB() : (StringsKt.equals((String)colorMode, (String)"Astolfo", (boolean)true) ? RenderUtils.Astolfo(i * ((Number)this.distanceValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), ((Number)this.astolfoRainbowOffset.get()).intValue(), ((Number)this.astolfoRainbowIndex.get()).intValue(), ((Number)this.astolfoclient.get()).intValue()) : (StringsKt.equals((String)colorMode, (String)"Gident", (boolean)true) ? RenderUtils.getGradientOffset(new Color(((Number)VisualColor.Companion.getR().get()).intValue(), ((Number)VisualColor.Companion.getB().get()).intValue(), ((Number)VisualColor.Companion.getG().get()).intValue()), new Color(((Number)VisualColor.Companion.getR2().get()).intValue(), ((Number)VisualColor.Companion.getB2().get()).intValue(), ((Number)VisualColor.Companion.getG2().get()).intValue(), 1), Math.abs((double)System.currentTimeMillis() / (double)((Number)this.gidentspeed.get()).intValue() + (double)(i * ((Number)this.distanceValue.get()).intValue())) / (double)10).getRGB() : (StringsKt.equals((String)colorMode, (String)"NewRainbow", (boolean)true) ? RenderUtils.getRainbow(i * ((Number)this.distanceValue.get()).intValue(), ((Number)this.newRainbowIndex.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue()) : color)))), rainbow ? 0 : (StringsKt.equals((String)colorMode, (String)"Fade", (boolean)true) ? Palette.fade2(new Color(((Number)VisualColor.Companion.getR().get()).intValue(), ((Number)VisualColor.Companion.getB().get()).intValue(), ((Number)VisualColor.Companion.getG().get()).intValue()), i * ((Number)this.distanceValue.get()).intValue(), this.displayText.length() * 200).getRGB() : (StringsKt.equals((String)colorMode, (String)"Astolfo", (boolean)true) ? RenderUtils.Astolfo(i * ((Number)this.distanceValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), ((Number)this.astolfoRainbowOffset.get()).intValue(), ((Number)this.astolfoRainbowIndex.get()).intValue(), ((Number)this.astolfoclient.get()).intValue()) : (StringsKt.equals((String)colorMode, (String)"Gident", (boolean)true) ? RenderUtils.getGradientOffset(new Color(((Number)VisualColor.Companion.getR().get()).intValue(), ((Number)VisualColor.Companion.getB().get()).intValue(), ((Number)VisualColor.Companion.getG().get()).intValue()), new Color(((Number)VisualColor.Companion.getR2().get()).intValue(), ((Number)VisualColor.Companion.getB2().get()).intValue(), ((Number)VisualColor.Companion.getG2().get()).intValue(), 1), Math.abs((double)System.currentTimeMillis() / (double)((Number)this.gidentspeed.get()).intValue() + (double)(i * ((Number)this.distanceValue.get()).intValue())) / (double)10).getRGB() : (StringsKt.equals((String)colorMode, (String)"NewRainbow", (boolean)true) ? RenderUtils.getRainbow(i * ((Number)this.distanceValue.get()).intValue(), ((Number)this.newRainbowIndex.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue()) : color)))));
                        if (i == n4) break;
                        ++i;
                    }
                }
            }
            if (((String)this.Mode.get()).equals("Slide")) {
                RenderUtils.drawRect(-4.0f, -4.5f, length2, (float)fontRenderer.getFontHeight(), new Color(0, 0, 0, ((Number)this.balpha.get()).intValue()).getRGB());
                barLength = length2 + 1.0f;
                i = 0;
                n4 = ((Number)this.amountValue.get()).intValue() - 1;
                if (i <= n4) {
                    while (true) {
                        barStart = (double)i / (double)((Number)this.amountValue.get()).intValue() * barLength;
                        barEnd = (double)(i + 1) / (double)((Number)this.amountValue.get()).intValue() * barLength;
                        RenderUtils.drawGradientSideways(-4.0 + barStart, -4.2, -1.0 + barEnd, -3.0, rainbow ? 0 : (StringsKt.equals((String)colorMode, (String)"Fade", (boolean)true) ? Palette.fade2(new Color(((Number)VisualColor.Companion.getR().get()).intValue(), ((Number)VisualColor.Companion.getB().get()).intValue(), ((Number)VisualColor.Companion.getG().get()).intValue()), i * ((Number)this.distanceValue.get()).intValue(), this.displayText.length() * 200).getRGB() : (StringsKt.equals((String)colorMode, (String)"Astolfo", (boolean)true) ? RenderUtils.Astolfo(i * ((Number)this.distanceValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), ((Number)this.astolfoRainbowOffset.get()).intValue(), ((Number)this.astolfoRainbowIndex.get()).intValue(), ((Number)this.astolfoclient.get()).intValue()) : (StringsKt.equals((String)colorMode, (String)"Gident", (boolean)true) ? RenderUtils.getGradientOffset(new Color(((Number)VisualColor.Companion.getR().get()).intValue(), ((Number)VisualColor.Companion.getB().get()).intValue(), ((Number)VisualColor.Companion.getG().get()).intValue()), new Color(((Number)VisualColor.Companion.getR2().get()).intValue(), ((Number)VisualColor.Companion.getB2().get()).intValue(), ((Number)VisualColor.Companion.getG2().get()).intValue(), 1), Math.abs((double)System.currentTimeMillis() / (double)((Number)this.gidentspeed.get()).intValue() + (double)(i * ((Number)this.distanceValue.get()).intValue())) / (double)10).getRGB() : (StringsKt.equals((String)colorMode, (String)"NewRainbow", (boolean)true) ? RenderUtils.getRainbow(i * ((Number)this.distanceValue.get()).intValue(), ((Number)this.newRainbowIndex.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue()) : color)))), rainbow ? 0 : (StringsKt.equals((String)colorMode, (String)"Fade", (boolean)true) ? Palette.fade2(new Color(((Number)VisualColor.Companion.getR().get()).intValue(), ((Number)VisualColor.Companion.getB().get()).intValue(), ((Number)VisualColor.Companion.getG().get()).intValue()), i * ((Number)this.distanceValue.get()).intValue(), this.displayText.length() * 200).getRGB() : (StringsKt.equals((String)colorMode, (String)"Astolfo", (boolean)true) ? RenderUtils.Astolfo(i * ((Number)this.distanceValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), ((Number)this.astolfoRainbowOffset.get()).intValue(), ((Number)this.astolfoRainbowIndex.get()).intValue(), ((Number)this.astolfoclient.get()).intValue()) : (StringsKt.equals((String)colorMode, (String)"Gident", (boolean)true) ? RenderUtils.getGradientOffset(new Color(((Number)VisualColor.Companion.getR().get()).intValue(), ((Number)VisualColor.Companion.getB().get()).intValue(), ((Number)VisualColor.Companion.getG().get()).intValue()), new Color(((Number)VisualColor.Companion.getR2().get()).intValue(), ((Number)VisualColor.Companion.getB2().get()).intValue(), ((Number)VisualColor.Companion.getG2().get()).intValue(), 1), Math.abs((double)System.currentTimeMillis() / (double)((Number)this.gidentspeed.get()).intValue() + (double)(i * ((Number)this.distanceValue.get()).intValue())) / (double)10).getRGB() : (StringsKt.equals((String)colorMode, (String)"NewRainbow", (boolean)true) ? RenderUtils.getRainbow(i * ((Number)this.distanceValue.get()).intValue(), ((Number)this.newRainbowIndex.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue()) : color)))));
                        if (i == n4) break;
                        ++i;
                    }
                }
            }
        }
        int[] counter = new int[]{0};
        if (((Boolean)this.char.get()).booleanValue()) {
            boolean rainbow2 = StringsKt.equals((String)colorMode, (String)"Rainbow", (boolean)true);
            float i = ((Number)this.rainbowX.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowX.get()).floatValue();
            float f = ((Number)this.rainbowY.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowY.get()).floatValue();
            float offset$iv22 = (float)(System.currentTimeMillis() % (long)10000) / 10000.0f;
            boolean $i$f$begin = false;
            if (rainbow2) {
                void y$iv;
                void x$iv;
                RainbowFontShader.INSTANCE.setStrengthX((float)x$iv);
                RainbowFontShader.INSTANCE.setStrengthY((float)y$iv);
                RainbowFontShader.INSTANCE.setOffset(offset$iv22);
                RainbowFontShader.INSTANCE.startShader();
            }
            Closeable x$iv = RainbowFontShader.INSTANCE;
            boolean y$iv = false;
            Throwable offset$iv22 = null;
            try {
                RainbowFontShader it = (RainbowFontShader)x$iv;
                boolean bl = false;
                fontRenderer.drawString(this.displayText, 0.0f, 0.0f, rainbow2 ? 0 : (StringsKt.equals((String)colorMode, (String)"Fade", (boolean)true) ? Palette.fade2(new Color(((Number)VisualColor.Companion.getR().get()).intValue(), ((Number)VisualColor.Companion.getB().get()).intValue(), ((Number)VisualColor.Companion.getG().get()).intValue()), counter[0] * 100, this.displayText.length() * 200).getRGB() : (StringsKt.equals((String)colorMode, (String)"Astolfo", (boolean)true) ? RenderUtils.Astolfo(counter[0] * 100, ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), ((Number)this.astolfoRainbowOffset.get()).intValue(), ((Number)this.astolfoRainbowIndex.get()).intValue(), ((Number)this.astolfoclient.get()).intValue()) : (StringsKt.equals((String)colorMode, (String)"NewRainbow", (boolean)true) ? RenderUtils.getRainbow(counter[0] * 100, ((Number)this.newRainbowIndex.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue()) : (StringsKt.equals((String)colorMode, (String)"Gident", (boolean)true) ? RenderUtils.getGradientOffset(new Color(((Number)VisualColor.Companion.getR().get()).intValue(), ((Number)VisualColor.Companion.getB().get()).intValue(), ((Number)VisualColor.Companion.getG().get()).intValue()), new Color(((Number)VisualColor.Companion.getR2().get()).intValue(), ((Number)VisualColor.Companion.getB2().get()).intValue(), ((Number)VisualColor.Companion.getG2().get()).intValue(), 1), Math.abs((double)System.currentTimeMillis() / (double)((Number)this.gidentspeed.get()).intValue() + (double)counter[0]) / (double)10).getRGB() : color)))), (Boolean)this.shadow.get());
                if (this.editMode && MinecraftInstance.classProvider.isGuiHudDesigner(MinecraftInstance.mc.getCurrentScreen()) && this.editTicks <= 40) {
                    fontRenderer.drawString("_", fontRenderer.getStringWidth(this.displayText), 0.0f, rainbow2 ? 0 : (StringsKt.equals((String)colorMode, (String)"Fade", (boolean)true) ? Palette.fade2(new Color(((Number)VisualColor.Companion.getR().get()).intValue(), ((Number)VisualColor.Companion.getB().get()).intValue(), ((Number)VisualColor.Companion.getG().get()).intValue()), counter[0] * 100, this.displayText.length() * 200).getRGB() : (StringsKt.equals((String)colorMode, (String)"Astolfo", (boolean)true) ? RenderUtils.Astolfo(counter[0] * 100, ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), ((Number)this.astolfoRainbowOffset.get()).intValue(), ((Number)this.astolfoRainbowIndex.get()).intValue(), ((Number)this.astolfoclient.get()).intValue()) : (StringsKt.equals((String)colorMode, (String)"Gident", (boolean)true) ? RenderUtils.getGradientOffset(new Color(((Number)VisualColor.Companion.getR().get()).intValue(), ((Number)VisualColor.Companion.getB().get()).intValue(), ((Number)VisualColor.Companion.getG().get()).intValue()), new Color(((Number)VisualColor.Companion.getR2().get()).intValue(), ((Number)VisualColor.Companion.getB2().get()).intValue(), ((Number)VisualColor.Companion.getG2().get()).intValue(), 1), Math.abs((double)System.currentTimeMillis() / (double)((Number)this.gidentspeed.get()).intValue() + (double)counter[0]) / (double)10).getRGB() : (StringsKt.equals((String)colorMode, (String)"NewRainbow", (boolean)true) ? RenderUtils.getRainbow(counter[0] * 100, ((Number)this.newRainbowIndex.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue()) : color)))), (Boolean)this.shadow.get());
                }
                counter[0] = counter[0] + 1;
                it = Unit.INSTANCE;
            }
            catch (Throwable it) {
                offset$iv22 = it;
                throw it;
            }
            finally {
                CloseableKt.closeFinally((Closeable)x$iv, (Throwable)offset$iv22);
            }
        }
        int length = 0;
        float x$iv = ((Number)this.rainbowX.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowX.get()).floatValue();
        float y$iv = ((Number)this.rainbowY.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowY.get()).floatValue();
        float offset$iv = (float)(System.currentTimeMillis() % (long)10000) / 10000.0f;
        boolean $i$f$begin = false;
        if (rainbow) {
            RainbowFontShader.INSTANCE.setStrengthX(x$iv);
            RainbowFontShader.INSTANCE.setStrengthY(y$iv);
            RainbowFontShader.INSTANCE.setOffset(offset$iv);
            RainbowFontShader.INSTANCE.startShader();
        }
        Closeable closeable = RainbowFontShader.INSTANCE;
        boolean bl = false;
        Throwable throwable = null;
        try {
            RainbowFontShader it = (RainbowFontShader)closeable;
            boolean bl2 = false;
            for (char charIndex : charArray) {
                boolean rainbow3 = StringsKt.equals((String)colorMode, (String)"Rainbow", (boolean)true);
                fontRenderer.drawString(String.valueOf(charIndex), length, 0.0f, rainbow3 ? 0 : (StringsKt.equals((String)colorMode, (String)"Fade", (boolean)true) ? Palette.fade2(new Color(((Number)VisualColor.Companion.getR().get()).intValue(), ((Number)VisualColor.Companion.getB().get()).intValue(), ((Number)VisualColor.Companion.getG().get()).intValue()), counter[0] * 100, this.displayText.length() * 200).getRGB() : (StringsKt.equals((String)colorMode, (String)"Astolfo", (boolean)true) ? RenderUtils.Astolfo(counter[0] * 100, ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), ((Number)this.astolfoRainbowOffset.get()).intValue(), ((Number)this.astolfoRainbowIndex.get()).intValue(), ((Number)this.astolfoclient.get()).intValue()) : (StringsKt.equals((String)colorMode, (String)"NewRainbow", (boolean)true) ? RenderUtils.getRainbow(counter[0] * 100, ((Number)this.newRainbowIndex.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue()) : (StringsKt.equals((String)colorMode, (String)"Gident", (boolean)true) ? RenderUtils.getGradientOffset(new Color(((Number)VisualColor.Companion.getR().get()).intValue(), ((Number)VisualColor.Companion.getB().get()).intValue(), ((Number)VisualColor.Companion.getG().get()).intValue()), new Color(((Number)VisualColor.Companion.getR2().get()).intValue(), ((Number)VisualColor.Companion.getB2().get()).intValue(), ((Number)VisualColor.Companion.getG2().get()).intValue(), 1), Math.abs((double)System.currentTimeMillis() / (double)((Number)this.gidentspeed.get()).intValue() + (double)counter[0]) / (double)10).getRGB() : color)))), (Boolean)this.shadow.get());
                counter[0] = counter[0] + 1;
                counter[0] = RangesKt.coerceIn((int)counter[0], (int)0, (int)this.displayText.length());
                length += fontRenderer.getStringWidth(String.valueOf(charIndex));
            }
            if (this.editMode && MinecraftInstance.classProvider.isGuiHudDesigner(MinecraftInstance.mc.getCurrentScreen()) && this.editTicks <= 40) {
                fontRenderer.drawString("_", length2, 0.0f, rainbow ? 0 : (StringsKt.equals((String)colorMode, (String)"Fade", (boolean)true) ? Palette.fade2(new Color(((Number)VisualColor.Companion.getR().get()).intValue(), ((Number)VisualColor.Companion.getB().get()).intValue(), ((Number)VisualColor.Companion.getG().get()).intValue()), counter[0] * 100, this.displayText.length() * 200).getRGB() : (StringsKt.equals((String)colorMode, (String)"Astolfo", (boolean)true) ? RenderUtils.Astolfo(counter[0] * 100, ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), ((Number)this.astolfoRainbowOffset.get()).intValue(), ((Number)this.astolfoRainbowIndex.get()).intValue(), ((Number)this.astolfoclient.get()).intValue()) : (StringsKt.equals((String)colorMode, (String)"Gident", (boolean)true) ? RenderUtils.getGradientOffset(new Color(((Number)VisualColor.Companion.getR().get()).intValue(), ((Number)VisualColor.Companion.getB().get()).intValue(), ((Number)VisualColor.Companion.getG().get()).intValue()), new Color(((Number)VisualColor.Companion.getR2().get()).intValue(), ((Number)VisualColor.Companion.getB2().get()).intValue(), ((Number)VisualColor.Companion.getG2().get()).intValue(), 1), Math.abs((double)System.currentTimeMillis() / (double)((Number)this.gidentspeed.get()).intValue() + (double)counter[0]) / (double)10).getRGB() : (StringsKt.equals((String)colorMode, (String)"NewRainbow", (boolean)true) ? RenderUtils.getRainbow(counter[0] * 100, ((Number)this.newRainbowIndex.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue()) : color)))), (Boolean)this.shadow.get());
            }
            Unit unit = Unit.INSTANCE;
        }
        catch (Throwable throwable2) {
            throwable = throwable2;
            throw throwable2;
        }
        finally {
            CloseableKt.closeFinally((Closeable)closeable, (Throwable)throwable);
        }
        if (this.editMode && !MinecraftInstance.classProvider.isGuiHudDesigner(MinecraftInstance.mc.getCurrentScreen())) {
            this.editMode = false;
            this.updateElement();
        }
        return new Border(-2.0f, -2.0f, length2, fontRenderer.getFontHeight());
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
    public void handleMouseClick(double x, double y, int mouseButton) {
        if (this.isInBorder(x, y) && mouseButton == 0) {
            if (System.currentTimeMillis() - this.prevClick <= 250L) {
                this.editMode = true;
            }
            this.prevClick = System.currentTimeMillis();
        } else {
            this.editMode = false;
        }
    }

    @Override
    public void handleKey(char c, int keyCode) {
        if (this.editMode && MinecraftInstance.classProvider.isGuiHudDesigner(MinecraftInstance.mc.getCurrentScreen())) {
            if (keyCode == 14) {
                CharSequence charSequence = (CharSequence)this.displayString.get();
                int n = 0;
                if (charSequence.length() > 0) {
                    charSequence = (String)this.displayString.get();
                    n = 0;
                    int n2 = ((String)this.displayString.get()).length() - 1;
                    TextValue textValue = this.displayString;
                    boolean bl = false;
                    CharSequence charSequence2 = charSequence;
                    if (charSequence2 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    String string = ((String)charSequence2).substring(n, n2);
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

    public final Text setColor(Color c) {
        VisualColor.Companion.getR().set(c.getRed());
        VisualColor.Companion.getB().set(c.getGreen());
        VisualColor.Companion.getG().set(c.getBlue());
        return this;
    }

    public final void drawRect(float x, float y, float x2, float y2, int color) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        this.glColor(color);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public final void drawRect(double x, double y, double x2, double y2, int color) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        this.glColor(color);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public final void glColor(int red, int green, int blue, int alpha) {
        GlStateManager.func_179131_c((float)((float)red / 255.0f), (float)((float)green / 255.0f), (float)((float)blue / 255.0f), (float)((float)alpha / 255.0f));
    }

    public final void glColor(Color color) {
        float red = (float)color.getRed() / 255.0f;
        float green = (float)color.getGreen() / 255.0f;
        float blue = (float)color.getBlue() / 255.0f;
        float alpha = (float)color.getAlpha() / 255.0f;
        GlStateManager.func_179131_c((float)red, (float)green, (float)blue, (float)alpha);
    }

    public final void glColor(int hex) {
        float alpha = (float)(hex >> 24 & 0xFF) / 255.0f;
        float red = (float)(hex >> 16 & 0xFF) / 255.0f;
        float green = (float)(hex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hex & 0xFF) / 255.0f;
        GlStateManager.func_179131_c((float)red, (float)green, (float)blue, (float)alpha);
    }

    public Text(double x, double y, float scale, Side side) {
        super(x, y, scale, side);
        this.colorModeValue = new ListValue("Text-Color", new String[]{"Custom", "Fade", "Astolfo", "NewRainbow", "Gident"}, "Custom");
        this.colorRedValue = new IntegerValue("Red", 0, 0, 255);
        this.colorGreenValue = new IntegerValue("Green", 111, 0, 255);
        this.colorBlueValue = new IntegerValue("Blue", 255, 0, 255);
        this.brightnessValue = new FloatValue("Brightness", 1.0f, 0.0f, 1.0f);
        this.displayString = new TextValue("DisplayText", "");
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
        this.fontValue = new FontValue("Font", Fonts.sfbold40);
        this.speedStr = "";
        this.displayText = "";
        this.slidetimer = new MSTimer();
        this.doslide = true;
    }

    public /* synthetic */ Text(double d, double d2, float f, Side side, int n, DefaultConstructorMarker defaultConstructorMarker) {
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

    public Text() {
        this(0.0, 0.0, 0.0f, null, 15, null);
    }

    static {
        Companion = new Companion(null);
        DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        HOUR_FORMAT = new SimpleDateFormat("HH:mm");
        DECIMAL_FORMAT = new DecimalFormat("0.00");
        DECIMAL_FORMAT_INT = new DecimalFormat("0");
    }

    public static final /* synthetic */ void access$setFontValue$p(Text $this, FontValue fontValue) {
        $this.fontValue = fontValue;
    }

    public static final class Companion {
        public final SimpleDateFormat getDATE_FORMAT() {
            return DATE_FORMAT;
        }

        public final SimpleDateFormat getHOUR_FORMAT() {
            return HOUR_FORMAT;
        }

        public final DecimalFormat getDECIMAL_FORMAT() {
            return DECIMAL_FORMAT;
        }

        public final DecimalFormat getDECIMAL_FORMAT_INT() {
            return DECIMAL_FORMAT_INT;
        }

        public final Text defaultClient() {
            Text text = new Text(2.0, 2.0, 2.0f, null, 8, null);
            text.displayString.set("LiquidKiller | Fps:%fps% | %serverip%");
            text.shadow.set(true);
            text.fontValue.set(Fonts.sfbold40);
            text.setColor(new Color(0, 111, 255));
            return text;
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

