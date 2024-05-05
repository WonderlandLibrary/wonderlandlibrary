/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.ChatAllowedCharacters
 *  net.minecraft.util.EnumChatFormatting
 *  net.minecraft.util.Session
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.UnaryOperator;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.ranges.IntProgression;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import me.report.liquidware.utils.ui.FuckerNMSL;
import me.report.liquidware.utils.ui.utils.EmptyInputBox;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.Side;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Textss$WhenMappings;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.utils.CPSCounter;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.PacketUtils;
import net.ccbluex.liquidbounce.utils.ServerUtils;
import net.ccbluex.liquidbounce.utils.SessionUtils;
import net.ccbluex.liquidbounce.utils.render.BlurUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.ShadowUtils;
import net.ccbluex.liquidbounce.utils.render.UiUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.FontValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.TextValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="Text")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u009a\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0014\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\f\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u0000 m2\u00020\u0001:\u0001mB-\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\n\u0010[\u001a\u0004\u0018\u00010\\H\u0016J(\u0010]\u001a\u00020^2\u0006\u0010\u0002\u001a\u00020\u00062\u0006\u0010\u0004\u001a\u00020\u00062\u0006\u0010_\u001a\u00020\u00062\u0006\u0010`\u001a\u00020\u0006H\u0002J\u0012\u0010a\u001a\u0004\u0018\u00010\u00132\u0006\u0010b\u001a\u00020\u0013H\u0002J\u0018\u0010c\u001a\u00020^2\u0006\u0010d\u001a\u00020e2\u0006\u0010f\u001a\u00020.H\u0016J \u0010g\u001a\u00020^2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010h\u001a\u00020.H\u0016J\u0010\u0010i\u001a\u00020\u00132\u0006\u0010b\u001a\u00020\u0013H\u0002J\u000e\u0010j\u001a\u00020\u00002\u0006\u0010d\u001a\u00020kJ\b\u0010l\u001a\u00020^H\u0016R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001f\u001a\u00020\u00138BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b \u0010!R\u000e\u0010\"\u001a\u00020#X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010&\u001a\u00020'X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b(\u0010)\"\u0004\b*\u0010+R\u000e\u0010,\u001a\u00020'X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020.X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u000200X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00101\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00102\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00103\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00104\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00105\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00106\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00107\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00108\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00109\u001a\u00020.X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010:\u001a\u00020;X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010<\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010=\u001a\u00020>\u00a2\u0006\b\n\u0000\u001a\u0004\b?\u0010@R\u000e\u0010A\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010B\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010C\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010D\u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010E\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010F\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010G\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010H\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010I\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010J\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010K\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010L\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010M\u001a\u00020.X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bN\u0010O\"\u0004\bP\u0010QR\u001a\u0010R\u001a\u00020SX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bT\u0010U\"\u0004\bV\u0010WR\u000e\u0010X\u001a\u00020\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010Y\u001a\b\u0012\u0004\u0012\u00020\u00130ZX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006n"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Textss;", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Element;", "x", "", "y", "scale", "", "side", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Side;", "(DDFLnet/ccbluex/liquidbounce/ui/client/hud/element/Side;)V", "BlurbackgroundValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "BlurbackgroundValue2", "Rianbowb", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "Rianbowg", "Rianbowr", "alphaValue", "autoComplete", "", "backgroundValue", "bgalphaValue", "bgblueValue", "bggreenValue", "bgredValue", "blueValue", "blurStrength", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "brightnessValue", "cRainbowDistValue", "cRainbowSecValue", "display", "getDisplay", "()Ljava/lang/String;", "displayString", "Lnet/ccbluex/liquidbounce/value/TextValue;", "displayText", "distanceValue", "doslide", "", "getDoslide", "()Z", "setDoslide", "(Z)V", "editMode", "editTicks", "", "fontValue", "Lnet/ccbluex/liquidbounce/value/FontValue;", "gradientAmountValue", "greenValue", "lastX", "lastZ", "lineValue", "op", "ops", "opss", "pointer", "prevClick", "", "rainbow", "rainbowList", "Lnet/ccbluex/liquidbounce/value/ListValue;", "getRainbowList", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "rainbowss", "rainbowsss", "redValue", "saturationValue", "shadow", "shadowStrength", "sk", "skeetRectValue", "skeetRectValue2", "skeetRectValue22", "slide", "slidedelay", "slidetext", "getSlidetext", "()I", "setSlidetext", "(I)V", "slidetimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "getSlidetimer", "()Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "setSlidetimer", "(Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;)V", "speedStr", "suggestion", "", "drawElement", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Border;", "drawExhiRect", "", "x2", "y2", "getReplacement", "str", "handleKey", "c", "", "keyCode", "handleMouseClick", "mouseButton", "multiReplace", "setColor", "Ljava/awt/Color;", "updateElement", "Companion", "KyinoClient"})
public final class Textss
extends Element {
    private final TextValue displayString;
    private final BoolValue slide;
    private final IntegerValue slidedelay;
    private final BoolValue backgroundValue;
    private final BoolValue BlurbackgroundValue;
    private final FloatValue blurStrength;
    private final BoolValue skeetRectValue;
    private final BoolValue skeetRectValue22;
    private final BoolValue skeetRectValue2;
    private final BoolValue op;
    private final BoolValue ops;
    private final BoolValue opss;
    private final BoolValue rainbow;
    private final BoolValue rainbowss;
    private final BoolValue rainbowsss;
    private final BoolValue sk;
    private final IntegerValue cRainbowDistValue;
    private final BoolValue BlurbackgroundValue2;
    private final IntegerValue shadowStrength;
    private final BoolValue lineValue;
    private final IntegerValue Rianbowr;
    private final IntegerValue Rianbowb;
    private final IntegerValue Rianbowg;
    private final IntegerValue redValue;
    private final IntegerValue greenValue;
    private final IntegerValue blueValue;
    private final IntegerValue alphaValue;
    private final IntegerValue bgredValue;
    private final IntegerValue bggreenValue;
    private final IntegerValue bgblueValue;
    private final IntegerValue bgalphaValue;
    @NotNull
    private final ListValue rainbowList;
    private final FloatValue saturationValue;
    private final FloatValue brightnessValue;
    private final IntegerValue cRainbowSecValue;
    private final IntegerValue distanceValue;
    private final IntegerValue gradientAmountValue;
    private final BoolValue shadow;
    private FontValue fontValue;
    private boolean editMode;
    private int editTicks;
    private long prevClick;
    private double lastX;
    private double lastZ;
    private String speedStr;
    private List<String> suggestion;
    private String displayText;
    private int pointer;
    private String autoComplete;
    private int slidetext;
    @NotNull
    private MSTimer slidetimer;
    private boolean doslide;
    @NotNull
    private static final SimpleDateFormat DATE_FORMAT;
    @NotNull
    private static final SimpleDateFormat HOUR_FORMAT;
    @NotNull
    private static final DecimalFormat DECIMAL_FORMAT;
    @NotNull
    private static final DecimalFormat DECIMAL_FORMAT_INT;
    @NotNull
    private static final DecimalFormat Y_FORMATs;
    @NotNull
    private static SimpleDateFormat formatter;
    public static final Companion Companion;

    @NotNull
    public final ListValue getRainbowList() {
        return this.rainbowList;
    }

    private final String getDisplay() {
        CharSequence charSequence = (CharSequence)this.displayString.get();
        boolean bl = false;
        String textContent = charSequence.length() == 0 && !this.editMode ? "%gamesense%" : (String)this.displayString.get();
        return ColorUtils.translateAlternateColorCodes(this.multiReplace(textContent));
    }

    private final String getReplacement(String str) {
        String string;
        if (Textss.access$getMc$p$s1046033730().field_71439_g != null) {
            switch (str) {
                case "x": {
                    return DECIMAL_FORMAT.format(Textss.access$getMc$p$s1046033730().field_71439_g.field_70165_t);
                }
                case "y": {
                    return DECIMAL_FORMAT.format(Textss.access$getMc$p$s1046033730().field_71439_g.field_70163_u);
                }
                case "z": {
                    return DECIMAL_FORMAT.format(Textss.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
                }
                case "xInt": {
                    return DECIMAL_FORMAT_INT.format(Textss.access$getMc$p$s1046033730().field_71439_g.field_70165_t);
                }
                case "yInt": {
                    return DECIMAL_FORMAT_INT.format(Textss.access$getMc$p$s1046033730().field_71439_g.field_70163_u);
                }
                case "zInt": {
                    return DECIMAL_FORMAT_INT.format(Textss.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
                }
                case "xdp": {
                    return String.valueOf(Textss.access$getMc$p$s1046033730().field_71439_g.field_70165_t);
                }
                case "ydp": {
                    return String.valueOf(Textss.access$getMc$p$s1046033730().field_71439_g.field_70163_u);
                }
                case "zdp": {
                    return String.valueOf(Textss.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
                }
                case "healths": {
                    EntityPlayerSP entityPlayerSP = Textss.access$getMc$p$s1046033730().field_71439_g;
                    Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
                    return String.valueOf(entityPlayerSP.func_110143_aJ());
                }
                case "velocity": {
                    double d = Textss.access$getMc$p$s1046033730().field_71439_g.field_70159_w * Textss.access$getMc$p$s1046033730().field_71439_g.field_70159_w + Textss.access$getMc$p$s1046033730().field_71439_g.field_70179_y * Textss.access$getMc$p$s1046033730().field_71439_g.field_70179_y;
                    DecimalFormat decimalFormat = DECIMAL_FORMAT;
                    boolean bl = false;
                    double d2 = Math.sqrt(d);
                    return decimalFormat.format(d2);
                }
                case "ping": {
                    return String.valueOf(EntityUtils.getPing((EntityPlayer)Textss.access$getMc$p$s1046033730().field_71439_g));
                }
                case "health": {
                    EntityPlayerSP entityPlayerSP = Textss.access$getMc$p$s1046033730().field_71439_g;
                    Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
                    return DECIMAL_FORMAT.format(Float.valueOf(entityPlayerSP.func_110143_aJ()));
                }
                case "maxHealth": {
                    EntityPlayerSP entityPlayerSP = Textss.access$getMc$p$s1046033730().field_71439_g;
                    Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
                    return DECIMAL_FORMAT.format(Float.valueOf(entityPlayerSP.func_110138_aP()));
                }
                case "healthInt": {
                    EntityPlayerSP entityPlayerSP = Textss.access$getMc$p$s1046033730().field_71439_g;
                    Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
                    return DECIMAL_FORMAT_INT.format(Float.valueOf(entityPlayerSP.func_110143_aJ()));
                }
                case "maxHealthInt": {
                    EntityPlayerSP entityPlayerSP = Textss.access$getMc$p$s1046033730().field_71439_g;
                    Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
                    return DECIMAL_FORMAT_INT.format(Float.valueOf(entityPlayerSP.func_110138_aP()));
                }
                case "yaw": {
                    return DECIMAL_FORMAT.format(Float.valueOf(Textss.access$getMc$p$s1046033730().field_71439_g.field_70177_z));
                }
                case "pitch": {
                    return DECIMAL_FORMAT.format(Float.valueOf(Textss.access$getMc$p$s1046033730().field_71439_g.field_70125_A));
                }
                case "yawInt": {
                    return DECIMAL_FORMAT_INT.format(Float.valueOf(Textss.access$getMc$p$s1046033730().field_71439_g.field_70177_z));
                }
                case "pitchInt": {
                    return DECIMAL_FORMAT_INT.format(Float.valueOf(Textss.access$getMc$p$s1046033730().field_71439_g.field_70125_A));
                }
                case "bps": {
                    return this.speedStr;
                }
                case "maxHealthInt2": {
                    EntityPlayerSP entityPlayerSP = Textss.access$getMc$p$s1046033730().field_71439_g;
                    Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
                    return Y_FORMATs.format(Float.valueOf(entityPlayerSP.func_110138_aP()));
                }
                case "inBound": {
                    return String.valueOf(PacketUtils.INSTANCE.getAvgInBound());
                }
                case "outBound": {
                    return String.valueOf(PacketUtils.INSTANCE.getAvgOutBound());
                }
                case "hurtTime": {
                    return String.valueOf(Textss.access$getMc$p$s1046033730().field_71439_g.field_70737_aN);
                }
                case "onGround": {
                    return String.valueOf(Textss.access$getMc$p$s1046033730().field_71439_g.field_70122_E);
                }
                case "0": {
                    return "\u00a70";
                }
                case "1": {
                    return "\u00a71";
                }
                case "2": {
                    return "\u00a72";
                }
                case "3": {
                    return "\u00a73";
                }
                case "4": {
                    return "\u00a74";
                }
                case "5": {
                    return "\u00a75";
                }
                case "6": {
                    return "\u00a76";
                }
                case "7": {
                    return "\u00a77";
                }
                case "8": {
                    return "\u00a78";
                }
                case "9": {
                    return "\u00a79";
                }
                case "a": {
                    return "\u00a7a";
                }
                case "b": {
                    return "\u00a7b";
                }
                case "c": {
                    return "\u00a7c";
                }
                case "d": {
                    return "\u00a7d";
                }
                case "e": {
                    return "\u00a7e";
                }
                case "f": {
                    return "\u00a7f";
                }
                case "n": {
                    return "\u00a7n";
                }
                case "m": {
                    return "\u00a7m";
                }
                case "l": {
                    return "\u00a7l";
                }
                case "k": {
                    return "\u00a7k";
                }
                case "o": {
                    return "\u00a7o";
                }
                case "r": {
                    return "\u00a7r";
                }
            }
        }
        switch (str) {
            case "userName": {
                Session session = Textss.access$getMc$p$s1046033730().field_71449_j;
                Intrinsics.checkExpressionValueIsNotNull(session, "mc.session");
                string = session.func_111285_a();
                break;
            }
            case "clientName": {
                string = "KyinoClient";
                break;
            }
            case "clientVersion": {
                string = "17.1".toString();
                break;
            }
            case "clientCreator": {
                string = "Report.";
                break;
            }
            case "gamesense": {
                StringBuilder stringBuilder = new StringBuilder().append("\u00a77kyino\u00a72sense\u00a7r\u00a7f | ");
                EmptyInputBox emptyInputBox = FuckerNMSL.username;
                Intrinsics.checkExpressionValueIsNotNull((Object)emptyInputBox, "FuckerNMSL.username");
                string = stringBuilder.append(emptyInputBox.getText().toString()).append(" | ").append(String.valueOf(Minecraft.func_175610_ah())).append("fps | ").append(String.valueOf(EntityUtils.getPing((EntityPlayer)Textss.access$getMc$p$s1046033730().field_71439_g))).append("ms | ").append(Math.round(LiquidBounce.INSTANCE.getLastTPS() * (float)10) / 10).append("tick | ").append(formatter.format(System.currentTimeMillis())).toString();
                break;
            }
            case "fps": {
                string = String.valueOf(Minecraft.func_175610_ah());
                break;
            }
            case "Rank": {
                string = EnumChatFormatting.GRAY.toString() + "Rank:" + EnumChatFormatting.BLUE + "User";
                break;
            }
            case "date": {
                string = DATE_FORMAT.format(System.currentTimeMillis());
                break;
            }
            case "time": {
                string = HOUR_FORMAT.format(System.currentTimeMillis());
                break;
            }
            case "serverIp": {
                string = ServerUtils.getRemoteIp();
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
            case "wdStatus": {
                return PacketUtils.isWatchdogActive() ? "Active" : "Inactive";
            }
            case "sessionTime": {
                return SessionUtils.getFormatSessionTime();
            }
            case "worldTime": {
                return SessionUtils.getFormatWorldTime();
            }
            case "Timer": {
                string = String.valueOf(Minecraft.func_71386_F());
                break;
            }
            case "LiquidSense": {
                StringBuilder stringBuilder = new StringBuilder().append("K\u00a7fyinosense \u00a77|| [\u00a7fFPS ").append(Minecraft.func_175610_ah()).append("\u00a77] || ").append(ServerUtils.getRemoteIp()).append(" || [\u00a7fTime ").append(HOUR_FORMAT.format(System.currentTimeMillis())).append("\u00a77] || \u00a7f");
                Session session = Textss.access$getMc$p$s1046033730().func_110432_I();
                Intrinsics.checkExpressionValueIsNotNull(session, "mc.getSession()");
                return stringBuilder.append(session.func_111285_a()).toString();
            }
            case "Kill": {
                string = String.valueOf(KillAura.CombatListener.INSTANCE.getKillCounts());
                break;
            }
            case "Kill2": {
                string = "\u00a77You killed: " + KillAura.CombatListener.INSTANCE.getKillCounts();
                break;
            }
            default: {
                string = null;
            }
        }
        return string;
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
                    Intrinsics.checkExpressionValueIsNotNull(v0.substring(var8_8, (int)i), "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
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
        v1 = result.toString();
        Intrinsics.checkExpressionValueIsNotNull(v1, "result.toString()");
        return v1;
    }

    public final int getSlidetext() {
        return this.slidetext;
    }

    public final void setSlidetext(int n) {
        this.slidetext = n;
    }

    @NotNull
    public final MSTimer getSlidetimer() {
        return this.slidetimer;
    }

    public final void setSlidetimer(@NotNull MSTimer mSTimer) {
        Intrinsics.checkParameterIsNotNull(mSTimer, "<set-?>");
        this.slidetimer = mSTimer;
    }

    public final boolean getDoslide() {
        return this.doslide;
    }

    public final void setDoslide(boolean bl) {
        this.doslide = bl;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @Nullable
    public Border drawElement() {
        Border border;
        int n;
        int n2;
        int barLength2;
        int LiquidSlowly;
        int LiquidSlowlys;
        if (((Boolean)this.slide.get()).booleanValue() && Intrinsics.areEqual(Textss.access$getMc$p$s1046033730().field_71462_r, (Object)new GuiHudDesigner()) ^ true) {
            String string;
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
            String string2 = this.getDisplay();
            int n3 = 0;
            int n4 = this.slidetext;
            Textss textss = this;
            boolean bl = false;
            String string3 = string2;
            if (string3 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string4 = string3.substring(n3, n4);
            Intrinsics.checkExpressionValueIsNotNull(string4, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
            textss.displayText = string = string4;
        } else {
            this.displayText = this.getDisplay();
        }
        int color = new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue(), ((Number)this.alphaValue.get()).intValue()).getRGB();
        FontRenderer fontRenderer = (FontRenderer)this.fontValue.get();
        String rainbowType = (String)this.rainbowList.get();
        switch (Textss$WhenMappings.$EnumSwitchMapping$0[this.getSide().getHorizontal().ordinal()]) {
            case 1: {
                GL11.glTranslatef((float)0.0f, (float)0.0f, (float)0.0f);
                break;
            }
            case 2: {
                GL11.glTranslatef((float)(-((float)fontRenderer.func_78256_a(this.displayText)) / 2.0f), (float)0.0f, (float)(-((float)fontRenderer.func_78256_a(this.displayText)) / 2.0f));
                break;
            }
            case 3: {
                GL11.glTranslatef((float)(-((float)fontRenderer.func_78256_a(this.displayText))), (float)0.0f, (float)(-((float)fontRenderer.func_78256_a(this.displayText))));
            }
        }
        float floatX = (float)this.getRenderX();
        float floatY = (float)this.getRenderY();
        float Rsaturation = 0.4f;
        float Rbrightness = 1.0f;
        int[] counter = new int[]{0};
        Color color2 = ColorUtils.LiquidSlowly(System.nanoTime(), 90, Rsaturation, Rbrightness);
        int c = LiquidSlowlys = (color2 != null ? Integer.valueOf(color2.getRGB()) : null).intValue();
        Color col = new Color(c);
        int braibow = new Color(((Number)this.Rianbowr.get()).intValue(), col.getGreen() / 2 + ((Number)this.Rianbowb.get()).intValue(), col.getGreen() / 2 + ((Number)this.Rianbowb.get()).intValue() + ((Number)this.Rianbowg.get()).intValue()).getRGB();
        int colord = new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()).getRGB() + new Color(0, 0, 0, 50).getRGB();
        if (((Boolean)this.backgroundValue.get()).booleanValue()) {
            RenderUtils.drawRect(-2.0f, -2.0f, (float)fontRenderer.func_78256_a(this.displayText) + 2.0f, (float)fontRenderer.field_78288_b + 0.0f, new Color(((Number)this.bgredValue.get()).intValue(), ((Number)this.bggreenValue.get()).intValue(), ((Number)this.bgblueValue.get()).intValue(), ((Number)this.bgalphaValue.get()).intValue()));
        }
        if (((Boolean)this.BlurbackgroundValue.get()).booleanValue()) {
            GL11.glTranslated((double)(-this.getRenderX()), (double)(-this.getRenderY()), (double)0.0);
            GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glPushMatrix();
            BlurUtils.blurArea(floatX * this.getScale() - 2.0f * this.getScale(), floatY * this.getScale() - 2.0f * this.getScale(), (floatX + (float)fontRenderer.func_78256_a(this.displayText) + 2.0f) * this.getScale(), (floatY + (float)fontRenderer.field_78288_b) * this.getScale(), ((Number)this.blurStrength.get()).floatValue());
            GL11.glPopMatrix();
            GL11.glScalef((float)this.getScale(), (float)this.getScale(), (float)this.getScale());
            GL11.glTranslated((double)this.getRenderX(), (double)this.getRenderY(), (double)0.0);
        }
        if (((Boolean)this.BlurbackgroundValue2.get()).booleanValue()) {
            GL11.glTranslated((double)(-this.getRenderX()), (double)(-this.getRenderY()), (double)0.0);
            GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glPushMatrix();
            ShadowUtils.INSTANCE.shadow(((Number)this.shadowStrength.get()).intValue(), new Function0<Unit>(this){
                final /* synthetic */ Textss this$0;

                public final void invoke() {
                    GL11.glPushMatrix();
                    GL11.glTranslated((double)this.this$0.getRenderX(), (double)this.this$0.getRenderY(), (double)0.0);
                    GL11.glPopMatrix();
                }
                {
                    this.this$0 = textss;
                    super(0);
                }
            }, new Function0<Unit>(this){
                final /* synthetic */ Textss this$0;

                public final void invoke() {
                    GL11.glPushMatrix();
                    GL11.glTranslated((double)this.this$0.getRenderX(), (double)this.this$0.getRenderY(), (double)0.0);
                    GL11.glPopMatrix();
                }
                {
                    this.this$0 = textss;
                    super(0);
                }
            });
            GL11.glPopMatrix();
            GL11.glScalef((float)this.getScale(), (float)this.getScale(), (float)this.getScale());
            GL11.glTranslated((double)this.getRenderX(), (double)this.getRenderY(), (double)0.0);
        }
        if (((Boolean)this.skeetRectValue.get()).booleanValue()) {
            this.drawExhiRect(-4.0f, (Boolean)this.lineValue.get() != false ? -5.0f : -4.0f, (float)fontRenderer.func_78256_a(this.displayText) + 4.0f, (float)fontRenderer.field_78288_b + 2.0f);
        }
        if (((Boolean)this.skeetRectValue22.get()).booleanValue()) {
            RenderUtils.drawOutlinedRect(-4.0f, (Boolean)this.lineValue.get() != false ? -5.0f : -4.0f, (float)fontRenderer.func_78256_a(this.displayText) + 4.0f, (float)fontRenderer.field_78288_b + 2.0f, 1.0f, color);
        }
        if (((Boolean)this.op.get()).booleanValue()) {
            RenderUtils.drawRect(-4.0f, -8.0f, (float)(fontRenderer.func_78256_a(this.displayText) + 3), (float)fontRenderer.field_78288_b, new Color(43, 43, 43).getRGB());
            RenderUtils.drawGradientSideways(-3.0, -7.0, (double)fontRenderer.func_78256_a(this.displayText) + 2.0, -3.0, (Boolean)this.rainbow.get() != false ? ColorUtils.rainbow(400000000L).getRGB() + new Color(0, 0, 0, 40).getRGB() : colord, (Boolean)this.rainbow.get() != false ? ColorUtils.rainbow(400000000L).getRGB() : color);
        }
        if (((Boolean)this.ops.get()).booleanValue()) {
            RenderUtils.drawRect(-4.0f, -8.0f, (float)(fontRenderer.func_78256_a(this.displayText) + 3), (float)fontRenderer.field_78288_b, new Color(43, 43, 43).getRGB());
            RenderUtils.drawGradientSideways(-3.0, -7.0, (double)fontRenderer.func_78256_a(this.displayText) + 2.0, -3.0, (Boolean)this.rainbowss.get() != false ? RenderUtils.getRainbowOpaque(((Number)this.cRainbowSecValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), counter[0] * (50 * ((Number)this.cRainbowDistValue.get()).intValue())) + new Color(0, 0, 0, 40).getRGB() : colord, (Boolean)this.rainbowss.get() != false ? RenderUtils.getRainbowOpaque(((Number)this.cRainbowSecValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), counter[0] * (50 * ((Number)this.cRainbowDistValue.get()).intValue())) : color);
        }
        if (((Boolean)this.opss.get()).booleanValue()) {
            RenderUtils.drawRect(-4.0f, -8.0f, (float)(fontRenderer.func_78256_a(this.displayText) + 3), (float)fontRenderer.field_78288_b, new Color(43, 43, 43).getRGB());
            RenderUtils.drawGradientSideways(-3.0, -7.0, (double)fontRenderer.func_78256_a(this.displayText) + 2.0, -3.0, (Boolean)this.rainbowsss.get() != false ? new Color(((Number)this.Rianbowr.get()).intValue(), col.getGreen() / 2 + ((Number)this.Rianbowb.get()).intValue(), col.getGreen() / 2 + ((Number)this.Rianbowb.get()).intValue() + ((Number)this.Rianbowg.get()).intValue()).getRGB() + new Color(0, 0, 0, 40).getRGB() : colord, (Boolean)this.rainbowsss.get() != false ? new Color(((Number)this.Rianbowr.get()).intValue(), col.getGreen() / 2 + ((Number)this.Rianbowb.get()).intValue(), col.getGreen() / 2 + ((Number)this.Rianbowb.get()).intValue() + ((Number)this.Rianbowg.get()).intValue()).getRGB() : color);
        }
        if (((Boolean)this.sk.get()).booleanValue()) {
            UiUtils.drawRect(-11.0, -9.5, fontRenderer.func_78256_a(this.displayText) + 9, (double)fontRenderer.field_78288_b + (double)6, new Color(0, 0, 0).getRGB());
            UiUtils.outlineRect(-10.0, -8.5, fontRenderer.func_78256_a(this.displayText) + 8, (double)fontRenderer.field_78288_b + (double)5, 8.0, new Color(59, 59, 59).getRGB(), new Color(59, 59, 59).getRGB());
            UiUtils.outlineRect(-9.0, -7.5, fontRenderer.func_78256_a(this.displayText) + 7, (double)fontRenderer.field_78288_b + (double)4, 4.0, new Color(59, 59, 59).getRGB(), new Color(40, 40, 40).getRGB());
            UiUtils.outlineRect(-4.0, -3.0, fontRenderer.func_78256_a(this.displayText) + 2, (double)fontRenderer.field_78288_b + 0.0, 1.0, new Color(18, 18, 18).getRGB(), new Color(0, 0, 0).getRGB());
        }
        int FadeColor = ColorUtils.fade(new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue(), ((Number)this.alphaValue.get()).intValue()), 0, 100).getRGB();
        Color color3 = ColorUtils.LiquidSlowly(System.nanoTime(), 0, ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
        int liquidSlowli = LiquidSlowly = (color3 != null ? Integer.valueOf(color3.getRGB()) : null).intValue();
        if (((Boolean)this.lineValue.get()).booleanValue()) {
            double barLength2 = (float)fontRenderer.func_78256_a(this.displayText) + 4.0f;
            int n5 = 0;
            int n6 = ((Number)this.gradientAmountValue.get()).intValue() - 1;
            if (n5 <= n6) {
                while (true) {
                    int n7;
                    int n8;
                    void i;
                    double barStart = (double)i / (double)((Number)this.gradientAmountValue.get()).intValue() * barLength2;
                    double barEnd = (double)(i + true) / (double)((Number)this.gradientAmountValue.get()).intValue() * barLength2;
                    switch (rainbowType) {
                        case "CRainbow": {
                            n8 = RenderUtils.getRainbowOpaque(((Number)this.cRainbowSecValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), (int)(i * ((Number)this.distanceValue.get()).intValue()));
                            break;
                        }
                        case "LiquidSlowly": {
                            Color color4 = ColorUtils.LiquidSlowly(System.nanoTime(), (int)(i * ((Number)this.distanceValue.get()).intValue()), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                            if (color4 == null) {
                                Intrinsics.throwNpe();
                            }
                            n8 = color4.getRGB();
                            break;
                        }
                        case "Fade": {
                            n8 = ColorUtils.fade(new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()), (int)(i * ((Number)this.distanceValue.get()).intValue()), 100).getRGB();
                            break;
                        }
                        case "Rainbow": {
                            n8 = RenderUtils.getRainbowOpaque(((Number)this.cRainbowSecValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), (int)(i * ((Number)this.distanceValue.get()).intValue()));
                            break;
                        }
                        case "SkyRainbow": {
                            n8 = RenderUtils.SkyRainbow(0, ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                            break;
                        }
                        case "Bainbow": {
                            n8 = new Color(((Number)this.Rianbowr.get()).intValue(), col.getGreen() / 2 + ((Number)this.Rianbowb.get()).intValue(), col.getGreen() / 2 + ((Number)this.Rianbowb.get()).intValue() + ((Number)this.Rianbowg.get()).intValue()).getRGB();
                            break;
                        }
                        default: {
                            n8 = color;
                        }
                    }
                    switch (rainbowType) {
                        case "CRainbow": {
                            n7 = RenderUtils.getRainbowOpaque(((Number)this.cRainbowSecValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), (int)((i + true) * ((Number)this.distanceValue.get()).intValue()));
                            break;
                        }
                        case "LiquidSlowly": {
                            Color color5 = ColorUtils.LiquidSlowly(System.nanoTime(), (int)((i + true) * ((Number)this.distanceValue.get()).intValue()), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                            if (color5 == null) {
                                Intrinsics.throwNpe();
                            }
                            n7 = color5.getRGB();
                            break;
                        }
                        case "Fade": {
                            n7 = ColorUtils.fade(new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()), (int)((i + true) * ((Number)this.distanceValue.get()).intValue()), 100).getRGB();
                            break;
                        }
                        case "Rainbow": {
                            n7 = RenderUtils.getRainbowOpaque(((Number)this.cRainbowSecValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), (int)(i * ((Number)this.distanceValue.get()).intValue()));
                            break;
                        }
                        case "SkyRainbow": {
                            n7 = RenderUtils.SkyRainbow(0, ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                            break;
                        }
                        case "Bainbow": {
                            n7 = new Color(((Number)this.Rianbowr.get()).intValue(), col.getGreen() / 2 + ((Number)this.Rianbowb.get()).intValue(), col.getGreen() / 2 + ((Number)this.Rianbowb.get()).intValue() + ((Number)this.Rianbowg.get()).intValue()).getRGB();
                            break;
                        }
                        default: {
                            n7 = color;
                        }
                    }
                    RenderUtils.drawGradientSideways(-2.0 + barStart, -3.0, -2.0 + barEnd, -2.0, n8, n7);
                    if (i == n6) break;
                    ++i;
                }
            }
        }
        if (((Boolean)this.skeetRectValue2.get()).booleanValue() && (barLength2 = 0) <= (n2 = ((Number)this.gradientAmountValue.get()).intValue() - 1)) {
            while (true) {
                int n9;
                void i;
                double barStart = (double)i / (double)((Number)this.gradientAmountValue.get()).intValue();
                double barEnd = (double)(i + true) / (double)((Number)this.gradientAmountValue.get()).intValue();
                double d = (double)-4.0f + barStart;
                float f = (Boolean)this.lineValue.get() != false ? -5.0f : -4.0f;
                double d2 = (double)((float)fontRenderer.func_78256_a(this.displayText) + 4.0f) + barEnd;
                float f2 = (float)fontRenderer.field_78288_b + 2.0f;
                switch (rainbowType) {
                    case "CRainbow": {
                        n9 = RenderUtils.getRainbowOpaque(((Number)this.cRainbowSecValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), (int)(i * ((Number)this.distanceValue.get()).intValue()));
                        break;
                    }
                    case "LiquidSlowly": {
                        Color color6 = ColorUtils.LiquidSlowly(System.nanoTime(), (int)(i * ((Number)this.distanceValue.get()).intValue()), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                        if (color6 == null) {
                            Intrinsics.throwNpe();
                        }
                        n9 = color6.getRGB();
                        break;
                    }
                    case "Fade": {
                        n9 = ColorUtils.fade(new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()), (int)(i * ((Number)this.distanceValue.get()).intValue()), 100).getRGB();
                        break;
                    }
                    case "Rainbow": {
                        n9 = RenderUtils.getRainbowOpaque(((Number)this.cRainbowSecValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), (int)(i * ((Number)this.distanceValue.get()).intValue()));
                        break;
                    }
                    case "SkyRainbow": {
                        n9 = RenderUtils.SkyRainbow(0, ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                        break;
                    }
                    case "Bainbow": {
                        n9 = new Color(((Number)this.Rianbowr.get()).intValue(), col.getGreen() / 2 + ((Number)this.Rianbowb.get()).intValue(), col.getGreen() / 2 + ((Number)this.Rianbowb.get()).intValue() + ((Number)this.Rianbowg.get()).intValue()).getRGB();
                        break;
                    }
                    default: {
                        n9 = color;
                    }
                }
                RenderUtils.drawOutlinedRectTextElement(d, f, d2, f2, 1.0f, n9);
                if (i == n2) break;
                ++i;
            }
        }
        switch (rainbowType) {
            case "CRainbow": {
                n = RenderUtils.getRainbowOpaque(((Number)this.cRainbowSecValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), 0);
                break;
            }
            case "LiquidSlowly": {
                n = liquidSlowli;
                break;
            }
            case "Fade": {
                n = FadeColor;
                break;
            }
            case "Rainbow": {
                n = RenderUtils.getRainbowOpaque(((Number)this.cRainbowSecValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), 0);
                break;
            }
            case "SkyRainbow": {
                n = RenderUtils.SkyRainbow(0, ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                break;
            }
            case "Bainbow": {
                n = new Color(((Number)this.Rianbowr.get()).intValue(), col.getGreen() / 2 + ((Number)this.Rianbowb.get()).intValue(), col.getGreen() / 2 + ((Number)this.Rianbowb.get()).intValue() + ((Number)this.Rianbowg.get()).intValue()).getRGB();
                break;
            }
            default: {
                n = color;
            }
        }
        fontRenderer.func_175065_a(this.displayText, 0.0f, 0.0f, n, ((Boolean)this.shadow.get()).booleanValue());
        if (this.editMode && Textss.access$getMc$p$s1046033730().field_71462_r instanceof GuiHudDesigner) {
            if (this.editTicks <= 40) {
                int n10;
                float f = (float)fontRenderer.func_78256_a(this.displayText) + 2.0f;
                switch (rainbowType) {
                    case "CRainbow": {
                        n10 = RenderUtils.getRainbowOpaque(((Number)this.cRainbowSecValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), 0);
                        break;
                    }
                    case "LiquidSlowly": {
                        n10 = liquidSlowli;
                        break;
                    }
                    case "Fade": {
                        n10 = FadeColor;
                        break;
                    }
                    case "SkyRainbow": {
                        n10 = RenderUtils.SkyRainbow(0, ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                        break;
                    }
                    case "Bainbow": {
                        n10 = new Color(((Number)this.Rianbowr.get()).intValue(), col.getGreen() / 2 + ((Number)this.Rianbowb.get()).intValue(), col.getGreen() / 2 + ((Number)this.Rianbowb.get()).intValue() + ((Number)this.Rianbowg.get()).intValue()).getRGB();
                        break;
                    }
                    default: {
                        n10 = color;
                    }
                }
                fontRenderer.func_175065_a("_", f, 0.0f, n10, ((Boolean)this.shadow.get()).booleanValue());
            }
            if (this.suggestion.size() > 0) {
                GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                int totalLength = fontRenderer.func_78256_a(this.suggestion.get(0));
                Iterable $this$forEachIndexed$iv = this.suggestion;
                boolean $i$f$forEachIndexed = false;
                int index$iv = 0;
                for (Object item$iv : $this$forEachIndexed$iv) {
                    void suggest;
                    int n11 = index$iv++;
                    boolean bl = false;
                    if (n11 < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    int n12 = n11;
                    String string = (String)item$iv;
                    int index = n12;
                    boolean bl2 = false;
                    RenderUtils.drawRect((float)fontRenderer.func_78256_a(this.displayText) + 2.0f, (float)fontRenderer.field_78288_b * (float)index + 5.0f, (float)fontRenderer.func_78256_a(this.displayText) + 6.0f + (float)totalLength, (float)fontRenderer.field_78288_b * (float)index + 5.0f + (float)fontRenderer.field_78288_b, index == this.pointer ? new Color(90, 90, 90, 120).getRGB() : new Color(0, 0, 0, 120).getRGB());
                    fontRenderer.func_175063_a((String)suggest, (float)fontRenderer.func_78256_a(this.displayText) + 4.0f, (float)fontRenderer.field_78288_b * (float)index + 5.0f, -1);
                }
            }
        }
        if (this.editMode && !(Textss.access$getMc$p$s1046033730().field_71462_r instanceof GuiHudDesigner)) {
            this.editMode = false;
            this.updateElement();
        }
        switch (Textss$WhenMappings.$EnumSwitchMapping$1[this.getSide().getHorizontal().ordinal()]) {
            case 1: {
                GL11.glTranslatef((float)0.0f, (float)0.0f, (float)0.0f);
                break;
            }
            case 2: {
                GL11.glTranslatef((float)((float)fontRenderer.func_78256_a(this.displayText) / 2.0f), (float)0.0f, (float)((float)fontRenderer.func_78256_a(this.displayText) / 2.0f));
                break;
            }
            case 3: {
                GL11.glTranslatef((float)fontRenderer.func_78256_a(this.displayText), (float)0.0f, (float)fontRenderer.func_78256_a(this.displayText));
            }
        }
        switch (Textss$WhenMappings.$EnumSwitchMapping$2[this.getSide().getHorizontal().ordinal()]) {
            case 1: {
                border = new Border(-2.0f, -2.0f, (float)fontRenderer.func_78256_a(this.displayText) + 2.0f, fontRenderer.field_78288_b);
                break;
            }
            case 2: {
                border = new Border(-((float)fontRenderer.func_78256_a(this.displayText)) / 2.0f, -2.0f, (float)fontRenderer.func_78256_a(this.displayText) / 2.0f + 2.0f, fontRenderer.field_78288_b);
                break;
            }
            case 3: {
                border = new Border(2.0f, -2.0f, (float)(-fontRenderer.func_78256_a(this.displayText)) - 2.0f, fontRenderer.field_78288_b);
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return border;
    }

    private final void drawExhiRect(float x, float y, float x2, float y2) {
        RenderUtils.drawRect(x - 1.5f, y - 1.5f, x2 + 1.5f, y2 + 1.5f, new Color(8, 8, 8).getRGB());
        RenderUtils.drawRect(x - 1.0f, y - 1.0f, x2 + 1.0f, y2 + 1.0f, new Color(49, 49, 49).getRGB());
        RenderUtils.drawBorderedRect(x + 2.0f, y + 2.0f, x2 - 2.0f, y2 - 2.0f, 0.5f, new Color(18, 18, 18).getRGB(), new Color(28, 28, 28).getRGB());
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void updateElement() {
        boolean bl;
        List list;
        Textss textss;
        void suggestStr;
        this.editTicks += 5;
        if (this.editTicks > 80) {
            this.editTicks = 0;
        }
        this.displayText = this.editMode ? (String)this.displayString.get() : this.getDisplay();
        Ref.ObjectRef objectRef = new Ref.ObjectRef();
        objectRef.element = "";
        boolean foundPlaceHolder = false;
        IntProgression intProgression = RangesKt.step(RangesKt.downTo(this.displayText.length() - 1, 0), 1);
        int n = intProgression.getFirst();
        int n2 = intProgression.getLast();
        int n3 = intProgression.getStep();
        int n4 = n;
        int n5 = n2;
        if (n3 >= 0 ? n4 <= n5 : n4 >= n5) {
            while (true) {
                void i;
                if (Intrinsics.areEqual(String.valueOf(this.displayText.charAt((int)i)), "%")) {
                    int placeHolderCounter = 1;
                    void z = i;
                    IntProgression intProgression2 = RangesKt.step(RangesKt.downTo((int)z, 0), 1);
                    int n6 = intProgression2.getFirst();
                    int n7 = intProgression2.getLast();
                    int n8 = intProgression2.getStep();
                    int n9 = n6;
                    int n10 = n7;
                    if (n8 >= 0 ? n9 <= n10 : n9 >= n10) {
                        while (true) {
                            void j;
                            if (Intrinsics.areEqual(String.valueOf(this.displayText.charAt((int)j)), "%")) {
                                ++placeHolderCounter;
                            }
                            if (j == n7) break;
                            j += n8;
                        }
                    }
                    if (placeHolderCounter % 2 != 0) break;
                    try {
                        String j = this.displayText;
                        n7 = this.displayText.length();
                        textss = suggestStr;
                        n8 = 0;
                        String string = j;
                        if (string == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        String string2 = string.substring((int)i, n7);
                        Intrinsics.checkExpressionValueIsNotNull(string2, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                        list = string2;
                        ((Ref.ObjectRef)((Object)textss)).element = StringsKt.replace$default((String)((Object)list), "%", "", false, 4, null);
                        foundPlaceHolder = true;
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                if (i == n2) break;
                i += n3;
            }
        }
        this.autoComplete = "";
        if (!foundPlaceHolder) {
            this.suggestion.clear();
        } else {
            void $this$sortedBy$iv;
            void $this$filterTo$iv$iv;
            Iterable $this$filter$iv;
            Iterable i = CollectionsKt.listOf("x", "y", "z", "xInt", "yInt", "zInt", "xdp", "ydp", "zdp", "velocity", "ping", "health", "maxHealth", "healthInt", "maxHealthInt", "maxHealthInt2", "Timer", "yaw", "pitch", "yawInt", "pitchInt", "bps", "inBound", "outBound", "hurtTime", "onGround", "userName", "clientName", "clientVersion", "clientCreator", "fps", "date", "time", "serverIp", "cps", "lcps", "mcps", "rcps", "portalVersion", "staffLastMin", "wdStatus", "sessionTime", "gamesense", "worldTime", "LiquidSense", "Rank", "Kill", "Kill2", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "n", "m", "l", "k", "o", "r");
            textss = this;
            boolean $i$f$filter = false;
            void var5_9 = $this$filter$iv;
            Collection destination$iv$iv = new ArrayList();
            boolean $i$f$filterTo = false;
            for (Object element$iv$iv : $this$filterTo$iv$iv) {
                String it = (String)element$iv$iv;
                boolean bl2 = false;
                if (!(StringsKt.startsWith(it, (String)suggestStr.element, true) && it.length() > ((String)suggestStr.element).length())) continue;
                destination$iv$iv.add(element$iv$iv);
            }
            list = (List)destination$iv$iv;
            $this$filter$iv = list;
            boolean $i$f$sortedBy = false;
            var5_9 = $this$sortedBy$iv;
            bl = false;
            Comparator comparator = new Comparator<T>(){

                public final int compare(T a, T b) {
                    boolean bl = false;
                    String it = (String)a;
                    boolean bl2 = false;
                    Comparable comparable = Integer.valueOf(it.length());
                    it = (String)b;
                    Comparable comparable2 = comparable;
                    bl2 = false;
                    Integer n = it.length();
                    return ComparisonsKt.compareValues(comparable2, (Comparable)n);
                }
            };
            list = CollectionsKt.sortedWith(var5_9, comparator);
            textss.suggestion = CollectionsKt.toMutableList(CollectionsKt.reversed(list));
        }
        this.pointer = RangesKt.coerceIn(this.pointer, 0, RangesKt.coerceAtLeast(this.suggestion.size() - 1, 0));
        if (this.suggestion.size() > 0) {
            String string = this.suggestion.get(this.pointer);
            n2 = RangesKt.coerceIn(((String)suggestStr.element).length(), 0, this.suggestion.get(this.pointer).length());
            n3 = this.suggestion.get(this.pointer).length();
            textss = this;
            bl = false;
            String string3 = string;
            if (string3 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string4 = string3.substring(n2, n3);
            Intrinsics.checkExpressionValueIsNotNull(string4, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
            list = string4;
            textss.autoComplete = list;
            this.suggestion.replaceAll(new UnaryOperator<String>((Ref.ObjectRef)suggestStr){
                final /* synthetic */ Ref.ObjectRef $suggestStr;

                @NotNull
                public final String apply(@NotNull String s) {
                    Intrinsics.checkParameterIsNotNull(s, "s");
                    String string = s;
                    int n = RangesKt.coerceIn(((String)this.$suggestStr.element).length(), 0, s.length());
                    int n2 = s.length();
                    StringBuilder stringBuilder = new StringBuilder().append("\u00a77").append((String)this.$suggestStr.element).append("\u00a7r");
                    boolean bl = false;
                    String string2 = string.substring(n, n2);
                    Intrinsics.checkExpressionValueIsNotNull(string2, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                    String string3 = string2;
                    return stringBuilder.append(string3).toString();
                }
                {
                    this.$suggestStr = objectRef;
                }
            });
        }
        if (Textss.access$getMc$p$s1046033730().field_71439_g == null) {
            return;
        }
        double d = Math.pow(this.lastX - Textss.access$getMc$p$s1046033730().field_71439_g.field_70165_t, 2.0) + Math.pow(this.lastZ - Textss.access$getMc$p$s1046033730().field_71439_g.field_70161_v, 2.0);
        list = DECIMAL_FORMAT;
        textss = this;
        n3 = 0;
        double d2 = Math.sqrt(d);
        String string = ((NumberFormat)((Object)list)).format(d2 * (double)20 * (double)Textss.access$getMc$p$s1046033730().field_71428_T.field_74278_d);
        Intrinsics.checkExpressionValueIsNotNull(string, "DECIMAL_FORMAT.format(sq\u202620 * mc.timer.timerSpeed)");
        textss.speedStr = string;
        this.lastX = Textss.access$getMc$p$s1046033730().field_71439_g.field_70165_t;
        this.lastZ = Textss.access$getMc$p$s1046033730().field_71439_g.field_70161_v;
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
        if (this.editMode && Textss.access$getMc$p$s1046033730().field_71462_r instanceof GuiHudDesigner) {
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
                    Intrinsics.checkExpressionValueIsNotNull(string, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                    String string2 = string;
                    textValue.set(string2);
                }
                this.updateElement();
                return;
            }
            if (keyCode == 200) {
                if (this.suggestion.size() > 1) {
                    if (this.pointer <= 0) {
                        this.pointer = this.suggestion.size() - 1;
                    } else {
                        int n = this.pointer;
                        this.pointer = n + -1;
                    }
                }
                this.updateElement();
                return;
            }
            if (keyCode == 208) {
                if (this.suggestion.size() > 1) {
                    if (this.pointer >= this.suggestion.size() - 1) {
                        this.pointer = 0;
                    } else {
                        int n = this.pointer;
                        this.pointer = n + 1;
                    }
                }
                this.updateElement();
                return;
            }
            if (keyCode == 15 || keyCode == 28) {
                this.displayString.set((String)this.displayString.get() + this.autoComplete);
                this.updateElement();
                return;
            }
            if (ChatAllowedCharacters.func_71566_a((char)c) || c == '\u00a7') {
                this.displayString.set((String)this.displayString.get() + c);
            }
            this.updateElement();
        }
    }

    @NotNull
    public final Textss setColor(@NotNull Color c) {
        Intrinsics.checkParameterIsNotNull(c, "c");
        this.redValue.set(c.getRed());
        this.greenValue.set(c.getGreen());
        this.blueValue.set(c.getBlue());
        return this;
    }

    public Textss(double x, double y, float scale, @NotNull Side side) {
        List list;
        Intrinsics.checkParameterIsNotNull(side, "side");
        super(x, y, scale, side);
        this.displayString = new TextValue("DisplayText", "Kyino");
        this.slide = new BoolValue("Slide", false);
        this.slidedelay = new IntegerValue("Slide Delay", 500, 0, 1000);
        this.backgroundValue = new BoolValue("Background", false);
        this.BlurbackgroundValue = new BoolValue("BlurBackground", false);
        this.blurStrength = new FloatValue("Blur-Strength", 0.0f, 0.0f, 30.0f);
        this.skeetRectValue = new BoolValue("Skeet Rect", false);
        this.skeetRectValue22 = new BoolValue("Outlined Rect Test", false);
        this.skeetRectValue2 = new BoolValue("Outlined Rect Test2", false);
        this.op = new BoolValue("OneTap Rect", false);
        this.ops = new BoolValue("OneTap KyinoSense", false);
        this.opss = new BoolValue("OneTap Bainbow", false);
        this.rainbow = new BoolValue("Rainbow", false);
        this.rainbowss = new BoolValue("Rainbow KyinoSense", false);
        this.rainbowsss = new BoolValue("OneTap Bainbow", false);
        this.sk = new BoolValue("Skeet.", false);
        this.cRainbowDistValue = new IntegerValue("Rainbow Distance", 2, 1, 6);
        this.BlurbackgroundValue2 = new BoolValue("Shadow", false);
        this.shadowStrength = new IntegerValue("Shadow Strength", 1, 1, 30);
        this.lineValue = new BoolValue("Line", false);
        this.Rianbowr = new IntegerValue("Bainbow Red", 0, 0, 255);
        this.Rianbowb = new IntegerValue("Bainbow Blue", 50, 0, 64);
        this.Rianbowg = new IntegerValue("Bainbow Green", 50, 0, 64);
        this.redValue = new IntegerValue("Red", 255, 0, 255);
        this.greenValue = new IntegerValue("Green", 255, 0, 255);
        this.blueValue = new IntegerValue("Blue", 255, 0, 255);
        this.alphaValue = new IntegerValue("Alpha", 255, 0, 255);
        this.bgredValue = new IntegerValue("Background Red", 0, 0, 255);
        this.bggreenValue = new IntegerValue("Background Green", 0, 0, 255);
        this.bgblueValue = new IntegerValue("Background Blue", 0, 0, 255);
        this.bgalphaValue = new IntegerValue("Background Alpha", 120, 0, 255);
        this.rainbowList = new ListValue("Rainbow", new String[]{"Off", "CRainbow", "LiquidSlowly", "Fade", "Rainbow", "SkyRainbow", "Bainbow"}, "Off");
        this.saturationValue = new FloatValue("Saturation", 0.9f, 0.0f, 1.0f);
        this.brightnessValue = new FloatValue("Brightness", 1.0f, 0.0f, 1.0f);
        this.cRainbowSecValue = new IntegerValue("Seconds", 2, 1, 10);
        this.distanceValue = new IntegerValue("Line Distance", 0, 0, 400);
        this.gradientAmountValue = new IntegerValue("Gradient Amount", 25, 1, 50);
        this.shadow = new BoolValue("Shadow", true);
        GameFontRenderer gameFontRenderer = Fonts.font40;
        Intrinsics.checkExpressionValueIsNotNull((Object)gameFontRenderer, "Fonts.font40");
        this.fontValue = new FontValue("Font", gameFontRenderer);
        this.speedStr = "";
        Textss textss = this;
        boolean bl = false;
        textss.suggestion = list = (List)new ArrayList();
        this.displayText = this.getDisplay();
        this.autoComplete = "";
        this.slidetimer = new MSTimer();
        this.doslide = true;
    }

    public /* synthetic */ Textss(double d, double d2, float f, Side side, int n, DefaultConstructorMarker defaultConstructorMarker) {
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

    public Textss() {
        this(0.0, 0.0, 0.0f, null, 15, null);
    }

    static {
        Companion = new Companion(null);
        DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
        HOUR_FORMAT = new SimpleDateFormat("HH:mm");
        DECIMAL_FORMAT = new DecimalFormat("0.00");
        DECIMAL_FORMAT_INT = new DecimalFormat("0");
        Y_FORMATs = new DecimalFormat("0.000000");
        formatter = new SimpleDateFormat("HH:mm:ss");
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }

    public static final /* synthetic */ void access$setFontValue$p(Textss $this, FontValue fontValue) {
        $this.fontValue = fontValue;
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0015\u001a\u00020\u0016R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u000b\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\nR\u0011\u0010\r\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u0006R\u0011\u0010\u000f\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\nR\u001a\u0010\u0011\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0006\"\u0004\b\u0013\u0010\u0014\u00a8\u0006\u0017"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Textss$Companion;", "", "()V", "DATE_FORMAT", "Ljava/text/SimpleDateFormat;", "getDATE_FORMAT", "()Ljava/text/SimpleDateFormat;", "DECIMAL_FORMAT", "Ljava/text/DecimalFormat;", "getDECIMAL_FORMAT", "()Ljava/text/DecimalFormat;", "DECIMAL_FORMAT_INT", "getDECIMAL_FORMAT_INT", "HOUR_FORMAT", "getHOUR_FORMAT", "Y_FORMATs", "getY_FORMATs", "formatter", "getFormatter", "setFormatter", "(Ljava/text/SimpleDateFormat;)V", "defaultClient", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Textss;", "KyinoClient"})
    public static final class Companion {
        @NotNull
        public final SimpleDateFormat getDATE_FORMAT() {
            return DATE_FORMAT;
        }

        @NotNull
        public final SimpleDateFormat getHOUR_FORMAT() {
            return HOUR_FORMAT;
        }

        @NotNull
        public final DecimalFormat getDECIMAL_FORMAT() {
            return DECIMAL_FORMAT;
        }

        @NotNull
        public final DecimalFormat getDECIMAL_FORMAT_INT() {
            return DECIMAL_FORMAT_INT;
        }

        @NotNull
        public final DecimalFormat getY_FORMATs() {
            return Y_FORMATs;
        }

        @NotNull
        public final SimpleDateFormat getFormatter() {
            return formatter;
        }

        public final void setFormatter(@NotNull SimpleDateFormat simpleDateFormat) {
            Intrinsics.checkParameterIsNotNull(simpleDateFormat, "<set-?>");
            formatter = simpleDateFormat;
        }

        @NotNull
        public final Textss defaultClient() {
            Textss text = new Textss(5.0, 5.0, 1.0f, null, 8, null);
            text.displayString.set("%clientName%");
            text.shadow.set(false);
            FontValue fontValue = text.fontValue;
            FontRenderer fontRenderer = Fonts.minecraftFont;
            Intrinsics.checkExpressionValueIsNotNull(fontRenderer, "Fonts.minecraftFont");
            fontValue.set(fontRenderer);
            text.setColor(new Color(255, 255, 255));
            return text;
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

