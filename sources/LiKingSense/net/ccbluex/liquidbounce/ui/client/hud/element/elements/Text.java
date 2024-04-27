/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.util.ChatAllowedCharacters
 *  net.minecraft.util.Session
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.Side;
import net.ccbluex.liquidbounce.utils.CPSCounter;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.ServerUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.FontValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.TextValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@ElementInfo(name="Text")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u0092\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\f\n\u0002\b\b\b\u0007\u0018\u0000 e2\u00020\u0001:\u0001eB-\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\n\u0010L\u001a\u0004\u0018\u00010MH\u0016J.\u0010N\u001a\u00020O2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010P\u001a\u00020\u00032\u0006\u0010Q\u001a\u00020\u00032\u0006\u0010R\u001a\u00020.J.\u0010N\u001a\u00020O2\u0006\u0010\u0002\u001a\u00020\u00062\u0006\u0010\u0004\u001a\u00020\u00062\u0006\u0010P\u001a\u00020\u00062\u0006\u0010Q\u001a\u00020\u00062\u0006\u0010R\u001a\u00020.J\u0012\u0010S\u001a\u0004\u0018\u00010\u001f2\u0006\u0010T\u001a\u00020\u001fH\u0002J\u000e\u0010U\u001a\u00020O2\u0006\u0010R\u001a\u00020VJ\u000e\u0010U\u001a\u00020O2\u0006\u0010W\u001a\u00020.J&\u0010U\u001a\u00020O2\u0006\u0010X\u001a\u00020.2\u0006\u0010Y\u001a\u00020.2\u0006\u0010Z\u001a\u00020.2\u0006\u0010[\u001a\u00020.J\u0018\u0010\\\u001a\u00020O2\u0006\u0010]\u001a\u00020^2\u0006\u0010_\u001a\u00020.H\u0016J \u0010`\u001a\u00020O2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010a\u001a\u00020.H\u0016J\u0010\u0010b\u001a\u00020\u001f2\u0006\u0010T\u001a\u00020\u001fH\u0002J\u000e\u0010c\u001a\u00020\u00002\u0006\u0010]\u001a\u00020VJ\b\u0010d\u001a\u00020OH\u0016R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001e\u001a\u00020\u001f8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b \u0010!R\u000e\u0010\"\u001a\u00020#X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u001fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010&\u001a\u00020'X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b(\u0010)\"\u0004\b*\u0010+R\u000e\u0010,\u001a\u00020'X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020.X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u000200X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00101\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00102\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00103\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00104\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00105\u001a\u000206X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00107\u001a\u00020\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00108\u001a\u00020\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00109\u001a\u00020\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010:\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010;\u001a\u00020\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010<\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010=\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010>\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010?\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010@\u001a\u00020.X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bA\u0010B\"\u0004\bC\u0010DR\u001a\u0010E\u001a\u00020FX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bG\u0010H\"\u0004\bI\u0010JR\u000e\u0010K\u001a\u00020\u001fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006f"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Text;", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Element;", "x", "", "y", "scale", "", "side", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Side;", "(DDFLnet/ccbluex/liquidbounce/ui/client/hud/element/Side;)V", "Mode", "Lnet/ccbluex/liquidbounce/value/ListValue;", "TopAplha", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "amountValue", "astolfoRainbowIndex", "astolfoRainbowOffset", "astolfoclient", "balpha", "blueValue", "blurValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "bord", "brightnessValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "char", "colorBlueValue2", "colorGreenValue2", "colorModeValue", "colorRedValue2", "display", "", "getDisplay", "()Ljava/lang/String;", "displayString", "Lnet/ccbluex/liquidbounce/value/TextValue;", "displayText", "distanceValue", "doslide", "", "getDoslide", "()Z", "setDoslide", "(Z)V", "editMode", "editTicks", "", "fontValue", "Lnet/ccbluex/liquidbounce/value/FontValue;", "gidentspeed", "greenValue", "newRainbowIndex", "onealpha", "prevClick", "", "radiusValue", "rainbowX", "rainbowY", "redValue", "saturationValue", "shadow", "shadowValue", "slide", "slidedelay", "slidetext", "getSlidetext", "()I", "setSlidetext", "(I)V", "slidetimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "getSlidetimer", "()Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "setSlidetimer", "(Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;)V", "speedStr", "drawElement", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Border;", "drawRect", "", "x2", "y2", "color", "getReplacement", "str", "glColor", "Ljava/awt/Color;", "hex", "red", "green", "blue", "alpha", "handleKey", "c", "", "keyCode", "handleMouseClick", "mouseButton", "multiReplace", "setColor", "updateElement", "Companion", "LiKingSense"})
public final class Text
extends Element {
    private final ListValue colorModeValue;
    private final FloatValue brightnessValue;
    private final TextValue displayString;
    private final IntegerValue redValue;
    private final IntegerValue greenValue;
    private final IntegerValue blueValue;
    private final IntegerValue colorRedValue2;
    private final IntegerValue colorGreenValue2;
    private final IntegerValue colorBlueValue2;
    private final BoolValue blurValue;
    private final ListValue shadowValue;
    private final IntegerValue TopAplha;
    private final FloatValue radiusValue;
    private final IntegerValue gidentspeed;
    private final IntegerValue newRainbowIndex;
    private final IntegerValue astolfoRainbowOffset;
    private final IntegerValue astolfoclient;
    private final IntegerValue astolfoRainbowIndex;
    private final FloatValue saturationValue;
    private final ListValue Mode;
    private final IntegerValue onealpha;
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
    public static final Companion Companion;

    private final String getDisplay() {
        CharSequence charSequence = (CharSequence)this.displayString.get();
        boolean bl = false;
        String textContent = charSequence.length() == 0 && !this.editMode ? "LiKingSense | Fps:%fps% | %serverip%" : (String)this.displayString.get();
        return this.multiReplace(textContent);
    }

    /*
     * WARNING - bad return control flow
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private final String getReplacement(String str) {
        String string;
        block74: {
            block76: {
                block69: {
                    block70: {
                        block72: {
                            block71: {
                                block73: {
                                    block75: {
                                        block68: {
                                            if (MinecraftInstance.mc.getThePlayer() != null) {
                                                switch (str) {
                                                    case "x": {
                                                        return DECIMAL_FORMAT.format(MinecraftInstance.mc.getThePlayer().getPosX());
                                                    }
                                                    case "y": {
                                                        return DECIMAL_FORMAT.format(MinecraftInstance.mc.getThePlayer().getPosY());
                                                    }
                                                    case "z": {
                                                        return DECIMAL_FORMAT.format(MinecraftInstance.mc.getThePlayer().getPosZ());
                                                    }
                                                    case "xInt": {
                                                        return DECIMAL_FORMAT_INT.format(MinecraftInstance.mc.getThePlayer().getPosX());
                                                    }
                                                    case "yInt": {
                                                        return DECIMAL_FORMAT_INT.format(MinecraftInstance.mc.getThePlayer().getPosY());
                                                    }
                                                    case "zInt": {
                                                        return DECIMAL_FORMAT_INT.format(MinecraftInstance.mc.getThePlayer().getPosZ());
                                                    }
                                                    case "xdp": {
                                                        return String.valueOf(MinecraftInstance.mc.getThePlayer().getPosX());
                                                    }
                                                    case "ydp": {
                                                        return String.valueOf(MinecraftInstance.mc.getThePlayer().getPosY());
                                                    }
                                                    case "zdp": {
                                                        return String.valueOf(MinecraftInstance.mc.getThePlayer().getPosZ());
                                                    }
                                                    case "velocity": {
                                                        double d = MinecraftInstance.mc.getThePlayer().getMotionX() * MinecraftInstance.mc.getThePlayer().getMotionX() + MinecraftInstance.mc.getThePlayer().getMotionZ() * MinecraftInstance.mc.getThePlayer().getMotionZ();
                                                        DecimalFormat decimalFormat = DECIMAL_FORMAT;
                                                        boolean bl = false;
                                                        double d2 = Math.sqrt(d);
                                                        return decimalFormat.format(d2);
                                                    }
                                                    case "ping": {
                                                        return String.valueOf(EntityUtils.getPing(MinecraftInstance.mc.getThePlayer()));
                                                    }
                                                    case "health": {
                                                        return DECIMAL_FORMAT.format(Float.valueOf(MinecraftInstance.mc.getThePlayer().getHealth()));
                                                    }
                                                    case "maxHealth": {
                                                        return DECIMAL_FORMAT.format(Float.valueOf(MinecraftInstance.mc.getThePlayer().getMaxHealth()));
                                                    }
                                                    case "healthInt": {
                                                        return DECIMAL_FORMAT_INT.format(Float.valueOf(MinecraftInstance.mc.getThePlayer().getHealth()));
                                                    }
                                                    case "maxHealthInt": {
                                                        return DECIMAL_FORMAT_INT.format(Float.valueOf(MinecraftInstance.mc.getThePlayer().getMaxHealth()));
                                                    }
                                                    case "yaw": {
                                                        return DECIMAL_FORMAT.format(Float.valueOf(MinecraftInstance.mc.getThePlayer().getRotationYaw()));
                                                    }
                                                    case "pitch": {
                                                        return DECIMAL_FORMAT.format(Float.valueOf(MinecraftInstance.mc.getThePlayer().getRotationPitch()));
                                                    }
                                                    case "yawInt": {
                                                        return DECIMAL_FORMAT_INT.format(Float.valueOf(MinecraftInstance.mc.getThePlayer().getRotationYaw()));
                                                    }
                                                    case "pitchInt": {
                                                        return DECIMAL_FORMAT_INT.format(Float.valueOf(MinecraftInstance.mc.getThePlayer().getRotationPitch()));
                                                    }
                                                    case "bps": {
                                                        return this.speedStr;
                                                    }
                                                    case "hurtTime": {
                                                        return String.valueOf(MinecraftInstance.mc.getThePlayer().getHurtTime());
                                                    }
                                                    case "onGround": {
                                                        return String.valueOf(MinecraftInstance.mc.getThePlayer().getOnGround());
                                                    }
                                                }
                                            }
                                            String string2 = str;
                                            switch (string2.hashCode()) {
                                                case 3076014: {
                                                    if (string2.equals("date")) break block68;
                                                    if (!string2.equals("date")) return null;
                                                    break block69;
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
                                                    return "b";
                                                }
                                                case 1102251254: {
                                                    if (!string2.equals("clientName")) return null;
                                                    return "LiKingSense";
                                                }
                                                case 98726: {
                                                    if (string2.equals("cps")) return String.valueOf(CPSCounter.getCPS(CPSCounter.MouseButton.LEFT));
                                                    if (!string2.equals("cps")) return null;
                                                    return String.valueOf(CPSCounter.getCPS(CPSCounter.MouseButton.LEFT));
                                                }
                                                case 101609: {
                                                    if (string2.equals("fps")) break;
                                                    if (!string2.equals("fps")) return null;
                                                    break block70;
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
                                                    break block71;
                                                }
                                                case 771880589: {
                                                    if (!string2.equals("clientVersion")) return null;
                                                    break block72;
                                                }
                                                case 1103204566: {
                                                    if (!string2.equals("clientname")) return null;
                                                    return "LiKingSense";
                                                }
                                                case 1379104682: {
                                                    if (!string2.equals("serverip")) return null;
                                                    break block73;
                                                }
                                                case 1379103690: {
                                                    if (!string2.equals("serverIp")) return null;
                                                    break block74;
                                                }
                                                case 1448827361: {
                                                    if (!string2.equals("clientCreator")) return null;
                                                    return "CCBlueX";
                                                }
                                                case 3560141: {
                                                    if (string2.equals("time")) break block75;
                                                    if (!string2.equals("time")) return null;
                                                    break block76;
                                                }
                                                case -265713450: {
                                                    if (!string2.equals("username")) return null;
                                                    Session session = MinecraftInstance.mc2.func_110432_I();
                                                    Intrinsics.checkExpressionValueIsNotNull((Object)session, (String)"mc2.getSession()");
                                                    string = session.func_111285_a();
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
                        string = "".toString();
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
                    Intrinsics.checkExpressionValueIsNotNull((Object)v0.substring(var8_8, (int)i), (String)"(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
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
        Intrinsics.checkExpressionValueIsNotNull((Object)v1, (String)"result.toString()");
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
        Intrinsics.checkParameterIsNotNull((Object)mSTimer, (String)"<set-?>");
        this.slidetimer = mSTimer;
    }

    public final boolean getDoslide() {
        return this.doslide;
    }

    public final void setDoslide(boolean bl) {
        this.doslide = bl;
    }

    /*
     * Exception decompiling
     */
    @Override
    @Nullable
    public Border drawElement() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl374 : INVOKESTATIC - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
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
                    Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                    String string2 = string;
                    textValue.set(string2);
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

    @NotNull
    public final Text setColor(@NotNull Color c) {
        Intrinsics.checkParameterIsNotNull((Object)c, (String)"c");
        this.redValue.set(c.getRed());
        this.greenValue.set(c.getGreen());
        this.blueValue.set(c.getBlue());
        return this;
    }

    public final void drawRect(float x, float y, float x2, float y2, int color) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        int n = 0;
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
        int n = 0;
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

    public final void glColor(@NotNull Color color) {
        Intrinsics.checkParameterIsNotNull((Object)color, (String)"color");
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

    /*
     * Exception decompiling
     */
    public Text(double x, double y, float scale, @NotNull Side side) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl65 : PUTFIELD - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
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

    public static final /* synthetic */ TextValue access$getDisplayString$p(Text $this) {
        return $this.displayString;
    }

    public static final /* synthetic */ BoolValue access$getShadow$p(Text $this) {
        return $this.shadow;
    }

    public static final /* synthetic */ FontValue access$getFontValue$p(Text $this) {
        return $this.fontValue;
    }

    public static final /* synthetic */ void access$setFontValue$p(Text $this, FontValue fontValue) {
        $this.fontValue = fontValue;
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u000f\u001a\u00020\u0010R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u000b\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\nR\u0011\u0010\r\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u0006\u00a8\u0006\u0011"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Text$Companion;", "", "()V", "DATE_FORMAT", "Ljava/text/SimpleDateFormat;", "getDATE_FORMAT", "()Ljava/text/SimpleDateFormat;", "DECIMAL_FORMAT", "Ljava/text/DecimalFormat;", "getDECIMAL_FORMAT", "()Ljava/text/DecimalFormat;", "DECIMAL_FORMAT_INT", "getDECIMAL_FORMAT_INT", "HOUR_FORMAT", "getHOUR_FORMAT", "defaultClient", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Text;", "LiKingSense"})
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

        /*
         * Exception decompiling
         */
        @NotNull
        public final Text defaultClient() {
            /*
             * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
             * 
             * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl32 : INVOKEVIRTUAL - null : Stack underflow
             *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
             *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
             *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
             *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
             *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
             *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
             *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
             *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseInnerClassesPass1(ClassFile.java:923)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1035)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
             *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
             *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
             *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
             *     at org.benf.cfr.reader.Main.main(Main.java:54)
             */
            throw new IllegalStateException("Decompilation failed");
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

