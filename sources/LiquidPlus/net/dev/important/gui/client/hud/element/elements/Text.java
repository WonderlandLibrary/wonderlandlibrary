/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.ChatAllowedCharacters
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 */
package net.dev.important.gui.client.hud.element.elements;

import de.enzaxd.viaforge.ViaForge;
import de.enzaxd.viaforge.protocol.ProtocolCollection;
import java.awt.Color;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import net.dev.important.gui.client.hud.designer.GuiHudDesigner;
import net.dev.important.gui.client.hud.element.Border;
import net.dev.important.gui.client.hud.element.Element;
import net.dev.important.gui.client.hud.element.ElementInfo;
import net.dev.important.gui.client.hud.element.Side;
import net.dev.important.gui.font.Fonts;
import net.dev.important.gui.font.GameFontRenderer;
import net.dev.important.modules.module.modules.color.ColorMixer;
import net.dev.important.modules.module.modules.misc.BanChecker;
import net.dev.important.utils.CPSCounter;
import net.dev.important.utils.EntityUtils;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.PacketUtils;
import net.dev.important.utils.ServerUtils;
import net.dev.important.utils.SessionUtils;
import net.dev.important.utils.render.BlurUtils;
import net.dev.important.utils.render.ColorManager;
import net.dev.important.utils.render.ColorUtils;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.FontValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.dev.important.value.TextValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatAllowedCharacters;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@ElementInfo(name="Text")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u008c\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\f\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u0000 L2\u00020\u0001:\u0001LB-\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\n\u0010:\u001a\u0004\u0018\u00010;H\u0016J(\u0010<\u001a\u00020=2\u0006\u0010\u0002\u001a\u00020\u00062\u0006\u0010\u0004\u001a\u00020\u00062\u0006\u0010>\u001a\u00020\u00062\u0006\u0010?\u001a\u00020\u0006H\u0002J\u0012\u0010@\u001a\u0004\u0018\u00010\u000f2\u0006\u0010A\u001a\u00020\u000fH\u0002J\u0018\u0010B\u001a\u00020=2\u0006\u0010C\u001a\u00020D2\u0006\u0010E\u001a\u00020&H\u0016J \u0010F\u001a\u00020=2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010G\u001a\u00020&H\u0016J\u0010\u0010H\u001a\u00020\u000f2\u0006\u0010A\u001a\u00020\u000fH\u0002J\u000e\u0010I\u001a\u00020\u00002\u0006\u0010C\u001a\u00020JJ\b\u0010K\u001a\u00020=H\u0016R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001c\u001a\u00020\u000f8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001d\u0010\u001eR\u000e\u0010\u001f\u001a\u00020 X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020$X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020&X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020(X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020&X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u000200X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00101\u001a\u000202X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00103\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00104\u001a\u00020\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00105\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00106\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00107\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u00108\u001a\b\u0012\u0004\u0012\u00020\u000f09X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006M"}, d2={"Lnet/dev/important/gui/client/hud/element/elements/Text;", "Lnet/dev/important/gui/client/hud/element/Element;", "x", "", "y", "scale", "", "side", "Lnet/dev/important/gui/client/hud/element/Side;", "(DDFLnet/dev/important/gui/client/hud/element/Side;)V", "alphaValue", "Lnet/dev/important/value/IntegerValue;", "astolfoRainbowIndex", "astolfoRainbowOffset", "autoComplete", "", "backgroundValue", "Lnet/dev/important/value/BoolValue;", "bgalphaValue", "bgblueValue", "bggreenValue", "bgredValue", "blueValue", "blurStrength", "Lnet/dev/important/value/FloatValue;", "blurValue", "brightnessValue", "cRainbowSecValue", "display", "getDisplay", "()Ljava/lang/String;", "displayString", "Lnet/dev/important/value/TextValue;", "displayText", "distanceValue", "editMode", "", "editTicks", "", "fontValue", "Lnet/dev/important/value/FontValue;", "gradientAmountValue", "greenValue", "lastX", "lastZ", "lineValue", "pointer", "prevClick", "", "rainbowList", "Lnet/dev/important/value/ListValue;", "redValue", "saturationValue", "shadow", "skeetRectValue", "speedStr", "suggestion", "", "drawElement", "Lnet/dev/important/gui/client/hud/element/Border;", "drawExhiRect", "", "x2", "y2", "getReplacement", "str", "handleKey", "c", "", "keyCode", "handleMouseClick", "mouseButton", "multiReplace", "setColor", "Ljava/awt/Color;", "updateElement", "Companion", "LiquidBounce"})
public final class Text
extends Element {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final TextValue displayString;
    @NotNull
    private final BoolValue backgroundValue;
    @NotNull
    private final BoolValue skeetRectValue;
    @NotNull
    private final BoolValue lineValue;
    @NotNull
    private final BoolValue blurValue;
    @NotNull
    private final FloatValue blurStrength;
    @NotNull
    private final IntegerValue redValue;
    @NotNull
    private final IntegerValue greenValue;
    @NotNull
    private final IntegerValue blueValue;
    @NotNull
    private final IntegerValue alphaValue;
    @NotNull
    private final IntegerValue bgredValue;
    @NotNull
    private final IntegerValue bggreenValue;
    @NotNull
    private final IntegerValue bgblueValue;
    @NotNull
    private final IntegerValue bgalphaValue;
    @NotNull
    private final ListValue rainbowList;
    @NotNull
    private final FloatValue saturationValue;
    @NotNull
    private final FloatValue brightnessValue;
    @NotNull
    private final IntegerValue cRainbowSecValue;
    @NotNull
    private final IntegerValue distanceValue;
    @NotNull
    private final IntegerValue gradientAmountValue;
    @NotNull
    private final BoolValue shadow;
    @NotNull
    private FontValue fontValue;
    @NotNull
    private final IntegerValue astolfoRainbowOffset;
    @NotNull
    private final IntegerValue astolfoRainbowIndex;
    private boolean editMode;
    private int editTicks;
    private long prevClick;
    private double lastX;
    private double lastZ;
    @NotNull
    private String speedStr;
    @NotNull
    private List<String> suggestion;
    @NotNull
    private String displayText;
    private int pointer;
    @NotNull
    private String autoComplete;
    @NotNull
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    @NotNull
    private static final SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat("HH:mm");
    @NotNull
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
    @NotNull
    private static final DecimalFormat DECIMAL_FORMAT_INT = new DecimalFormat("0");

    public Text(double x, double y, float scale, @NotNull Side side) {
        Intrinsics.checkNotNullParameter(side, "side");
        super(x, y, scale, side);
        this.displayString = new TextValue("DisplayText", "");
        this.backgroundValue = new BoolValue("Background", false);
        this.skeetRectValue = new BoolValue("SkeetRect", false);
        this.lineValue = new BoolValue("Line", false);
        this.blurValue = new BoolValue("Blur", false);
        this.blurStrength = new FloatValue("BlurStrength", 1.0f, 0.0f, 30.0f);
        this.redValue = new IntegerValue("Red", 255, 0, 255);
        this.greenValue = new IntegerValue("Green", 255, 0, 255);
        this.blueValue = new IntegerValue("Blue", 255, 0, 255);
        this.alphaValue = new IntegerValue("Alpha", 255, 0, 255);
        this.bgredValue = new IntegerValue("Background-Red", 0, 0, 255);
        this.bggreenValue = new IntegerValue("Background-Green", 0, 0, 255);
        this.bgblueValue = new IntegerValue("Background-Blue", 0, 0, 255);
        this.bgalphaValue = new IntegerValue("Background-Alpha", 120, 0, 255);
        Object object = new String[]{"Off", "CRainbow", "Sky", "LiquidSlowly", "Fade", "Mixer", "Astolfo"};
        this.rainbowList = new ListValue("Rainbow", (String[])object, "Off");
        this.saturationValue = new FloatValue("Saturation", 0.9f, 0.0f, 1.0f);
        this.brightnessValue = new FloatValue("Brightness", 1.0f, 0.0f, 1.0f);
        this.cRainbowSecValue = new IntegerValue("Seconds", 2, 1, 10);
        this.distanceValue = new IntegerValue("Line-Distance", 0, 0, 400);
        this.gradientAmountValue = new IntegerValue("Gradient-Amount", 25, 1, 50);
        this.shadow = new BoolValue("Shadow", true);
        object = Fonts.font40;
        Intrinsics.checkNotNullExpressionValue(object, "font40");
        this.fontValue = new FontValue("Font", (FontRenderer)object);
        this.astolfoRainbowOffset = new IntegerValue("AstolfoOffset", 5, 1, 20);
        this.astolfoRainbowIndex = new IntegerValue("AstolfoIndex", 109, 1, 300);
        this.speedStr = "";
        this.suggestion = new ArrayList();
        this.displayText = this.getDisplay();
        this.autoComplete = "";
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

    private final String getDisplay() {
        String textContent = ((CharSequence)this.displayString.get()).length() == 0 && !this.editMode ? "What Text" : (String)this.displayString.get();
        return ColorUtils.translateAlternateColorCodes(this.multiReplace(textContent));
    }

    private final String getReplacement(String str) {
        String string;
        block130: {
            if (MinecraftInstance.mc.field_71439_g == null) break block130;
            switch (str) {
                case "x": {
                    return DECIMAL_FORMAT.format(MinecraftInstance.mc.field_71439_g.field_70165_t);
                }
                case "y": {
                    return DECIMAL_FORMAT.format(MinecraftInstance.mc.field_71439_g.field_70163_u);
                }
                case "z": {
                    return DECIMAL_FORMAT.format(MinecraftInstance.mc.field_71439_g.field_70161_v);
                }
                case "xInt": {
                    return DECIMAL_FORMAT_INT.format(MinecraftInstance.mc.field_71439_g.field_70165_t);
                }
                case "yInt": {
                    return DECIMAL_FORMAT_INT.format(MinecraftInstance.mc.field_71439_g.field_70163_u);
                }
                case "zInt": {
                    return DECIMAL_FORMAT_INT.format(MinecraftInstance.mc.field_71439_g.field_70161_v);
                }
                case "xdp": {
                    return String.valueOf(MinecraftInstance.mc.field_71439_g.field_70165_t);
                }
                case "ydp": {
                    return String.valueOf(MinecraftInstance.mc.field_71439_g.field_70163_u);
                }
                case "zdp": {
                    return String.valueOf(MinecraftInstance.mc.field_71439_g.field_70161_v);
                }
                case "velocity": {
                    return DECIMAL_FORMAT.format(Math.sqrt(MinecraftInstance.mc.field_71439_g.field_70159_w * MinecraftInstance.mc.field_71439_g.field_70159_w + MinecraftInstance.mc.field_71439_g.field_70179_y * MinecraftInstance.mc.field_71439_g.field_70179_y));
                }
                case "ping": {
                    return String.valueOf(EntityUtils.getPing((EntityPlayer)MinecraftInstance.mc.field_71439_g));
                }
                case "health": {
                    return DECIMAL_FORMAT.format(Float.valueOf(MinecraftInstance.mc.field_71439_g.func_110143_aJ()));
                }
                case "maxHealth": {
                    return DECIMAL_FORMAT.format(Float.valueOf(MinecraftInstance.mc.field_71439_g.func_110138_aP()));
                }
                case "healthInt": {
                    return DECIMAL_FORMAT_INT.format(Float.valueOf(MinecraftInstance.mc.field_71439_g.func_110143_aJ()));
                }
                case "maxHealthInt": {
                    return DECIMAL_FORMAT_INT.format(Float.valueOf(MinecraftInstance.mc.field_71439_g.func_110138_aP()));
                }
                case "yaw": {
                    return DECIMAL_FORMAT.format(Float.valueOf(MinecraftInstance.mc.field_71439_g.field_70177_z));
                }
                case "pitch": {
                    return DECIMAL_FORMAT.format(Float.valueOf(MinecraftInstance.mc.field_71439_g.field_70125_A));
                }
                case "yawInt": {
                    return DECIMAL_FORMAT_INT.format(Float.valueOf(MinecraftInstance.mc.field_71439_g.field_70177_z));
                }
                case "pitchInt": {
                    return DECIMAL_FORMAT_INT.format(Float.valueOf(MinecraftInstance.mc.field_71439_g.field_70125_A));
                }
                case "bps": {
                    return this.speedStr;
                }
                case "inBound": {
                    return String.valueOf(PacketUtils.avgInBound);
                }
                case "outBound": {
                    return String.valueOf(PacketUtils.avgOutBound);
                }
                case "hurtTime": {
                    return String.valueOf(MinecraftInstance.mc.field_71439_g.field_70737_aN);
                }
                case "onGround": {
                    return String.valueOf(MinecraftInstance.mc.field_71439_g.field_70122_E);
                }
            }
        }
        switch (str) {
            case "userName": {
                string = MinecraftInstance.mc.field_71449_j.func_111285_a();
                break;
            }
            case "clientName": {
                string = "LiquidPlus";
                break;
            }
            case "clientVersion": {
                string = "idk";
                break;
            }
            case "fps": {
                string = String.valueOf(Minecraft.func_175610_ah());
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
            case "portalVersion": {
                string = ProtocolCollection.getProtocolById(ViaForge.getInstance().getVersion()).getName();
                break;
            }
            case "watchdogLastMin": {
                string = String.valueOf(BanChecker.WATCHDOG_BAN_LAST_MIN);
                break;
            }
            case "staffLastMin": {
                string = String.valueOf(BanChecker.STAFF_BAN_LAST_MIN);
                break;
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
            default: {
                string = null;
            }
        }
        return string;
    }

    private final String multiReplace(String str) {
        int lastPercent = -1;
        StringBuilder result = new StringBuilder();
        int n = 0;
        int n2 = str.length();
        while (n < n2) {
            int i;
            if (str.charAt(i = n++) == '%') {
                if (lastPercent != -1) {
                    if (lastPercent + 1 != i) {
                        String string = str;
                        int n3 = lastPercent + 1;
                        String string2 = string.substring(n3, i);
                        Intrinsics.checkNotNullExpressionValue(string2, "this as java.lang.String\u2026ing(startIndex, endIndex)");
                        String replacement = this.getReplacement(string2);
                        if (replacement != null) {
                            result.append(replacement);
                            lastPercent = -1;
                            continue;
                        }
                    }
                    result.append(str, lastPercent, i);
                }
                lastPercent = i;
                continue;
            }
            if (lastPercent != -1) continue;
            result.append(str.charAt(i));
        }
        if (lastPercent != -1) {
            result.append(str, lastPercent, str.length());
        }
        String string = result.toString();
        Intrinsics.checkNotNullExpressionValue(string, "result.toString()");
        return string;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @Nullable
    public Border drawElement() {
        Border border;
        FontRenderer fontRenderer;
        block112: {
            block113: {
                int n;
                int n2;
                int mixerColor;
                int Astolfo;
                int liquidSlowli;
                int FadeColor;
                String rainbowType;
                int color;
                block108: {
                    int i;
                    Integer LiquidSlowly;
                    color = new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue(), ((Number)this.alphaValue.get()).intValue()).getRGB();
                    fontRenderer = (FontRenderer)this.fontValue.get();
                    rainbowType = (String)this.rainbowList.get();
                    switch (WhenMappings.$EnumSwitchMapping$0[this.getSide().getHorizontal().ordinal()]) {
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
                    if (((Boolean)this.blurValue.get()).booleanValue()) {
                        GL11.glTranslated((double)(-this.getRenderX()), (double)(-this.getRenderY()), (double)0.0);
                        GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
                        GL11.glPushMatrix();
                        BlurUtils.blurArea(floatX * this.getScale() - 2.0f * this.getScale(), floatY * this.getScale() - 2.0f * this.getScale(), (floatX + (float)fontRenderer.func_78256_a(this.displayText) + 2.0f) * this.getScale(), (floatY + (float)fontRenderer.field_78288_b) * this.getScale(), ((Number)this.blurStrength.get()).floatValue());
                        GL11.glPopMatrix();
                        GL11.glScalef((float)this.getScale(), (float)this.getScale(), (float)this.getScale());
                        GL11.glTranslated((double)this.getRenderX(), (double)this.getRenderY(), (double)0.0);
                    }
                    if (((Boolean)this.backgroundValue.get()).booleanValue()) {
                        RenderUtils.drawRect(-2.0f, -2.0f, (float)fontRenderer.func_78256_a(this.displayText) + 2.0f, (float)fontRenderer.field_78288_b + 0.0f, new Color(((Number)this.bgredValue.get()).intValue(), ((Number)this.bggreenValue.get()).intValue(), ((Number)this.bgblueValue.get()).intValue(), ((Number)this.bgalphaValue.get()).intValue()));
                    }
                    if (((Boolean)this.skeetRectValue.get()).booleanValue()) {
                        this.drawExhiRect(-4.0f, (Boolean)this.lineValue.get() != false ? -5.0f : -4.0f, (float)fontRenderer.func_78256_a(this.displayText) + 4.0f, (float)fontRenderer.field_78288_b + 2.0f);
                    }
                    FadeColor = ColorUtils.fade(new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue(), ((Number)this.alphaValue.get()).intValue()), 0, 100).getRGB();
                    Color color2 = ColorUtils.LiquidSlowly(System.nanoTime(), 0, ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                    Integer n3 = LiquidSlowly = color2 == null ? null : Integer.valueOf(color2.getRGB());
                    Intrinsics.checkNotNull(n3);
                    liquidSlowli = n3;
                    int[] nArray = new int[]{0};
                    int[] counter = nArray;
                    Astolfo = ColorManager.astolfoRainbow(counter[0] * 100, ((Number)this.astolfoRainbowOffset.get()).intValue(), ((Number)this.astolfoRainbowIndex.get()).intValue());
                    mixerColor = ColorMixer.getMixedColor(0, ((Number)this.cRainbowSecValue.get()).intValue()).getRGB();
                    if (!((Boolean)this.lineValue.get()).booleanValue()) break block108;
                    double barLength = (float)fontRenderer.func_78256_a(this.displayText) + 4.0f;
                    int n4 = 0;
                    int n5 = ((Number)this.gradientAmountValue.get()).intValue() - 1;
                    if (n4 > n5) break block108;
                    do {
                        int n6;
                        int n7;
                        i = n4++;
                        double barStart = (double)i / (double)((Number)this.gradientAmountValue.get()).intValue() * barLength;
                        double barEnd = (double)(i + 1) / (double)((Number)this.gradientAmountValue.get()).intValue() * barLength;
                        switch (rainbowType) {
                            case "CRainbow": {
                                n7 = RenderUtils.getRainbowOpaque(((Number)this.cRainbowSecValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), i * ((Number)this.distanceValue.get()).intValue());
                                break;
                            }
                            case "Sky": {
                                n7 = RenderUtils.SkyRainbow(i * ((Number)this.distanceValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                                break;
                            }
                            case "LiquidSlowly": {
                                Color color3 = ColorUtils.LiquidSlowly(System.nanoTime(), i * ((Number)this.distanceValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                                Intrinsics.checkNotNull(color3);
                                n7 = color3.getRGB();
                                break;
                            }
                            case "Mixer": {
                                n7 = ColorMixer.getMixedColor(i * ((Number)this.distanceValue.get()).intValue(), ((Number)this.cRainbowSecValue.get()).intValue()).getRGB();
                                break;
                            }
                            case "Astolfo": {
                                n7 = Astolfo;
                                break;
                            }
                            case "Fade": {
                                n7 = ColorUtils.fade(new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()), i * ((Number)this.distanceValue.get()).intValue(), 100).getRGB();
                                break;
                            }
                            default: {
                                n7 = color;
                            }
                        }
                        switch (rainbowType) {
                            case "CRainbow": {
                                n6 = RenderUtils.getRainbowOpaque(((Number)this.cRainbowSecValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), (i + 1) * ((Number)this.distanceValue.get()).intValue());
                                break;
                            }
                            case "Sky": {
                                n6 = RenderUtils.SkyRainbow((i + 1) * ((Number)this.distanceValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                                break;
                            }
                            case "LiquidSlowly": {
                                Color color4 = ColorUtils.LiquidSlowly(System.nanoTime(), (i + 1) * ((Number)this.distanceValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                                Intrinsics.checkNotNull(color4);
                                n6 = color4.getRGB();
                                break;
                            }
                            case "Mixer": {
                                n6 = ColorMixer.getMixedColor((i + 1) * ((Number)this.distanceValue.get()).intValue(), ((Number)this.cRainbowSecValue.get()).intValue()).getRGB();
                                break;
                            }
                            case "Astolfo": {
                                n6 = Astolfo;
                                break;
                            }
                            case "Fade": {
                                n6 = ColorUtils.fade(new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()), (i + 1) * ((Number)this.distanceValue.get()).intValue(), 100).getRGB();
                                break;
                            }
                            default: {
                                n6 = color;
                            }
                        }
                        RenderUtils.drawGradientSideways(-2.0 + barStart, -3.0, -2.0 + barEnd, -2.0, n7, n6);
                    } while (i != n5);
                }
                switch (rainbowType) {
                    case "CRainbow": {
                        n2 = RenderUtils.getRainbowOpaque(((Number)this.cRainbowSecValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), 0);
                        break;
                    }
                    case "Sky": {
                        n2 = RenderUtils.SkyRainbow(0, ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                        break;
                    }
                    case "LiquidSlowly": {
                        n2 = liquidSlowli;
                        break;
                    }
                    case "Fade": {
                        n2 = FadeColor;
                        break;
                    }
                    case "Mixer": {
                        n2 = mixerColor;
                        break;
                    }
                    case "Astolfo": {
                        n2 = Astolfo;
                        break;
                    }
                    default: {
                        n2 = color;
                    }
                }
                fontRenderer.func_175065_a(this.displayText, 0.0f, 0.0f, n2, ((Boolean)this.shadow.get()).booleanValue());
                if (!this.editMode || !(MinecraftInstance.mc.field_71462_r instanceof GuiHudDesigner)) break block112;
                if (this.editTicks > 40) break block113;
                float f = (float)fontRenderer.func_78256_a(this.displayText) + 2.0f;
                switch (rainbowType) {
                    case "CRainbow": {
                        n = RenderUtils.getRainbowOpaque(((Number)this.cRainbowSecValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), 0);
                        break;
                    }
                    case "Sky": {
                        n = RenderUtils.SkyRainbow(0, ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
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
                    case "Mixer": {
                        n = mixerColor;
                        break;
                    }
                    default: {
                        n = color;
                    }
                }
                fontRenderer.func_175065_a("_", f, 0.0f, n, ((Boolean)this.shadow.get()).booleanValue());
            }
            if (this.suggestion.size() > 0) {
                GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                int totalLength = fontRenderer.func_78256_a(this.suggestion.get(0));
                Iterable $this$forEachIndexed$iv = this.suggestion;
                boolean $i$f$forEachIndexed = false;
                int index$iv = 0;
                for (Object item$iv : $this$forEachIndexed$iv) {
                    void suggest;
                    int n = index$iv;
                    index$iv = n + 1;
                    if (n < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    String barEnd = (String)item$iv;
                    int index = n;
                    boolean bl = false;
                    RenderUtils.drawRect((float)fontRenderer.func_78256_a(this.displayText) + 2.0f, (float)fontRenderer.field_78288_b * (float)index + 5.0f, (float)fontRenderer.func_78256_a(this.displayText) + 6.0f + (float)totalLength, (float)fontRenderer.field_78288_b * (float)index + 5.0f + (float)fontRenderer.field_78288_b, index == this.pointer ? new Color(90, 90, 90, 120).getRGB() : new Color(0, 0, 0, 120).getRGB());
                    fontRenderer.func_175063_a((String)suggest, (float)fontRenderer.func_78256_a(this.displayText) + 4.0f, (float)fontRenderer.field_78288_b * (float)index + 5.0f, -1);
                }
            }
        }
        if (this.editMode && !(MinecraftInstance.mc.field_71462_r instanceof GuiHudDesigner)) {
            this.editMode = false;
            this.updateElement();
        }
        switch (WhenMappings.$EnumSwitchMapping$0[this.getSide().getHorizontal().ordinal()]) {
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
        switch (WhenMappings.$EnumSwitchMapping$0[this.getSide().getHorizontal().ordinal()]) {
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
        this.editTicks += 5;
        if (this.editTicks > 80) {
            this.editTicks = 0;
        }
        this.displayText = this.editMode ? (String)this.displayString.get() : this.getDisplay();
        Ref.ObjectRef suggestStr = new Ref.ObjectRef();
        suggestStr.element = "";
        boolean foundPlaceHolder = false;
        int n = this.displayText.length() - 1;
        if (0 <= n) {
            do {
                int i;
                if (!Intrinsics.areEqual(String.valueOf(this.displayText.charAt(i = n--)), "%")) continue;
                int placeHolderCounter = 1;
                int z = i;
                int n2 = z;
                if (0 <= n2) {
                    do {
                        int j;
                        if (!Intrinsics.areEqual(String.valueOf(this.displayText.charAt(j = n2--)), "%")) continue;
                        int n3 = placeHolderCounter;
                        placeHolderCounter = n3 + 1;
                    } while (0 <= n2);
                }
                if (placeHolderCounter % 2 != 0) break;
                try {
                    String string = this.displayText;
                    int n4 = this.displayText.length();
                    String string2 = string.substring(i, n4);
                    Intrinsics.checkNotNullExpressionValue(string2, "this as java.lang.String\u2026ing(startIndex, endIndex)");
                    suggestStr.element = StringsKt.replace$default(string2, "%", "", false, 4, null);
                    foundPlaceHolder = true;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            } while (0 <= n);
        }
        this.autoComplete = "";
        if (!foundPlaceHolder) {
            this.suggestion.clear();
        } else {
            void $this$filterTo$iv$iv;
            void $this$filter$iv;
            Object object = new String[]{"x", "y", "z", "xInt", "yInt", "zInt", "xdp", "ydp", "zdp", "velocity", "ping", "health", "maxHealth", "healthInt", "maxHealthInt", "yaw", "pitch", "yawInt", "pitchInt", "bps", "inBound", "outBound", "hurtTime", "onGround", "userName", "clientName", "clientVersion", "clientCreator", "fps", "date", "time", "serverIp", "cps", "lcps", "mcps", "rcps", "portalVersion", "watchdogLastMin", "staffLastMin", "wdStatus", "sessionTime", "worldTime"};
            object = CollectionsKt.listOf(object);
            Text text = this;
            boolean $i$f$filter = false;
            void placeHolderCounter = $this$filter$iv;
            Collection destination$iv$iv = new ArrayList();
            boolean $i$f$filterTo = false;
            for (Object element$iv$iv : $this$filterTo$iv$iv) {
                String it = (String)element$iv$iv;
                boolean bl = false;
                if (!(StringsKt.startsWith(it, (String)suggestStr.element, true) && it.length() > ((String)suggestStr.element).length())) continue;
                destination$iv$iv.add(element$iv$iv);
            }
            Iterable $this$sortedBy$iv = (List)destination$iv$iv;
            boolean $i$f$sortedBy = false;
            text.suggestion = CollectionsKt.toMutableList(CollectionsKt.reversed(CollectionsKt.sortedWith($this$sortedBy$iv, new Comparator(){

                public final int compare(T a, T b) {
                    String it = (String)a;
                    boolean bl = false;
                    Comparable comparable = Integer.valueOf(it.length());
                    it = (String)b;
                    Comparable comparable2 = comparable;
                    bl = false;
                    return ComparisonsKt.compareValues(comparable2, it.length());
                }
            })));
        }
        this.pointer = RangesKt.coerceIn(this.pointer, 0, RangesKt.coerceAtLeast(this.suggestion.size() - 1, 0));
        if (this.suggestion.size() > 0) {
            String string = this.suggestion.get(this.pointer);
            int n5 = RangesKt.coerceIn(((String)suggestStr.element).length(), 0, this.suggestion.get(this.pointer).length());
            int n6 = this.suggestion.get(this.pointer).length();
            String string3 = string.substring(n5, n6);
            Intrinsics.checkNotNullExpressionValue(string3, "this as java.lang.String\u2026ing(startIndex, endIndex)");
            this.autoComplete = string3;
            this.suggestion.replaceAll(arg_0 -> Text.updateElement$lambda-3(suggestStr, arg_0));
        }
        if (MinecraftInstance.mc.field_71439_g == null) {
            return;
        }
        String string = DECIMAL_FORMAT.format(Math.sqrt(Math.pow(this.lastX - MinecraftInstance.mc.field_71439_g.field_70165_t, 2.0) + Math.pow(this.lastZ - MinecraftInstance.mc.field_71439_g.field_70161_v, 2.0)) * (double)20 * (double)MinecraftInstance.mc.field_71428_T.field_74278_d);
        Intrinsics.checkNotNullExpressionValue(string, "DECIMAL_FORMAT.format(sq\u202620 * mc.timer.timerSpeed)");
        this.speedStr = string;
        this.lastX = MinecraftInstance.mc.field_71439_g.field_70165_t;
        this.lastZ = MinecraftInstance.mc.field_71439_g.field_70161_v;
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
        if (this.editMode && MinecraftInstance.mc.field_71462_r instanceof GuiHudDesigner) {
            if (keyCode == 14) {
                if (((CharSequence)this.displayString.get()).length() > 0) {
                    String string = ((String)this.displayString.get()).substring(0, ((String)this.displayString.get()).length() - 1);
                    Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String\u2026ing(startIndex, endIndex)");
                    this.displayString.set(string);
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
            switch (keyCode) {
                case 15: 
                case 28: {
                    this.displayString.set(Intrinsics.stringPlus((String)this.displayString.get(), this.autoComplete));
                    this.updateElement();
                    return;
                }
            }
            if (ChatAllowedCharacters.func_71566_a((char)c) || c == '\u00a7') {
                this.displayString.set(Intrinsics.stringPlus((String)this.displayString.get(), Character.valueOf(c)));
            }
            this.updateElement();
        }
    }

    @NotNull
    public final Text setColor(@NotNull Color c) {
        Intrinsics.checkNotNullParameter(c, "c");
        this.redValue.set(c.getRed());
        this.greenValue.set(c.getGreen());
        this.blueValue.set(c.getBlue());
        return this;
    }

    private static final String updateElement$lambda-3(Ref.ObjectRef $suggestStr, String s) {
        Intrinsics.checkNotNullParameter($suggestStr, "$suggestStr");
        Intrinsics.checkNotNullParameter(s, "s");
        StringBuilder stringBuilder = new StringBuilder().append("\u00a77").append((String)$suggestStr.element).append("\u00a7r");
        String string = s.substring(RangesKt.coerceIn(((String)$suggestStr.element).length(), 0, s.length()), s.length());
        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String\u2026ing(startIndex, endIndex)");
        return stringBuilder.append(string).toString();
    }

    public Text() {
        this(0.0, 0.0, 0.0f, null, 15, null);
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u000f\u001a\u00020\u0010R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u000b\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\nR\u0011\u0010\r\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u0006\u00a8\u0006\u0011"}, d2={"Lnet/dev/important/gui/client/hud/element/elements/Text$Companion;", "", "()V", "DATE_FORMAT", "Ljava/text/SimpleDateFormat;", "getDATE_FORMAT", "()Ljava/text/SimpleDateFormat;", "DECIMAL_FORMAT", "Ljava/text/DecimalFormat;", "getDECIMAL_FORMAT", "()Ljava/text/DecimalFormat;", "DECIMAL_FORMAT_INT", "getDECIMAL_FORMAT_INT", "HOUR_FORMAT", "getHOUR_FORMAT", "defaultClient", "Lnet/dev/important/gui/client/hud/element/elements/Text;", "LiquidBounce"})
    public static final class Companion {
        private Companion() {
        }

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
        public final Text defaultClient() {
            Text text = new Text(5.0, 5.0, 1.0f, null, 8, null);
            text.displayString.set("%clientName%(%time%)");
            text.shadow.set(true);
            FontValue fontValue = text.fontValue;
            GameFontRenderer gameFontRenderer = Fonts.font40;
            Intrinsics.checkNotNullExpressionValue((Object)gameFontRenderer, "font40");
            fontValue.set(gameFontRenderer);
            text.rainbowList.set("LiquidSlowly");
            return text;
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }

    @Metadata(mv={1, 6, 0}, k=3, xi=48)
    public final class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] nArray = new int[Side.Horizontal.values().length];
            nArray[Side.Horizontal.LEFT.ordinal()] = 1;
            nArray[Side.Horizontal.MIDDLE.ordinal()] = 2;
            nArray[Side.Horizontal.RIGHT.ordinal()] = 3;
            $EnumSwitchMapping$0 = nArray;
        }
    }
}

