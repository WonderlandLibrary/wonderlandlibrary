/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.Minecraft
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import kotlin.TypeCastException;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.Side;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.CPSCounter;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.ServerUtils;
import net.ccbluex.liquidbounce.utils.extensions.PlayerExtensionKt;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.FontValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.TextValue;
import net.minecraft.client.Minecraft;

@ElementInfo(name="Text2")
public final class Text2
extends Element {
    private final TextValue displayString;
    private final IntegerValue redValue;
    private final IntegerValue greenValue;
    private final IntegerValue blueValue;
    private final IntegerValue alphaValue;
    private final ListValue colorModeValue;
    private final BoolValue shadow;
    private final IntegerValue rectRedValue;
    private final IntegerValue rectGreenValue;
    private final IntegerValue rectBlueValue;
    private final IntegerValue rectAlphaValue;
    private final ListValue rectColorModeValue;
    private final ListValue rectValue;
    private final FloatValue rectExpandValue;
    private final IntegerValue rainbowSpeed;
    private final IntegerValue rainbowIndex;
    private final FontValue fontValue;
    private boolean editMode;
    private int editTicks;
    private long prevClick;
    private String displayText;
    private static final SimpleDateFormat DATE_FORMAT;
    private static final SimpleDateFormat HOUR_FORMAT;
    private static final DecimalFormat DECIMAL_FORMAT;
    public static final Companion Companion;

    public final TextValue getDisplayString() {
        return this.displayString;
    }

    public final ListValue getColorModeValue() {
        return this.colorModeValue;
    }

    public final ListValue getRectColorModeValue() {
        return this.rectColorModeValue;
    }

    public final ListValue getRectValue() {
        return this.rectValue;
    }

    private final String getDisplay() {
        CharSequence charSequence = (CharSequence)this.displayString.get();
        boolean bl = false;
        String textContent = charSequence.length() == 0 && !this.editMode ? "Text Element" : (String)this.displayString.get();
        return this.multiReplace(textContent);
    }

    private final String getReplacement(String str) {
        String string;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP != null) {
            switch (str) {
                case "x": {
                    IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP2 == null) {
                        Intrinsics.throwNpe();
                    }
                    return DECIMAL_FORMAT.format(iEntityPlayerSP2.getPosX());
                }
                case "y": {
                    IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP3 == null) {
                        Intrinsics.throwNpe();
                    }
                    return DECIMAL_FORMAT.format(iEntityPlayerSP3.getPosY());
                }
                case "z": {
                    IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP4 == null) {
                        Intrinsics.throwNpe();
                    }
                    return DECIMAL_FORMAT.format(iEntityPlayerSP4.getPosZ());
                }
                case "xdp": {
                    IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP5 == null) {
                        Intrinsics.throwNpe();
                    }
                    return String.valueOf(iEntityPlayerSP5.getPosX());
                }
                case "ydp": {
                    IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP6 == null) {
                        Intrinsics.throwNpe();
                    }
                    return String.valueOf(iEntityPlayerSP6.getPosY());
                }
                case "zdp": {
                    IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP7 == null) {
                        Intrinsics.throwNpe();
                    }
                    return String.valueOf(iEntityPlayerSP7.getPosZ());
                }
                case "velocity": {
                    IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP8 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d = iEntityPlayerSP8.getMotionX();
                    IEntityPlayerSP iEntityPlayerSP9 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP9 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d2 = d * iEntityPlayerSP9.getMotionX();
                    IEntityPlayerSP iEntityPlayerSP10 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP10 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d3 = iEntityPlayerSP10.getMotionZ();
                    IEntityPlayerSP iEntityPlayerSP11 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP11 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d4 = d2 + d3 * iEntityPlayerSP11.getMotionZ();
                    DecimalFormat decimalFormat = DECIMAL_FORMAT;
                    boolean bl = false;
                    double d5 = Math.sqrt(d4);
                    return decimalFormat.format(d5);
                }
                case "ping": {
                    IEntityPlayerSP iEntityPlayerSP12 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP12 == null) {
                        Intrinsics.throwNpe();
                    }
                    return String.valueOf(PlayerExtensionKt.getPing(iEntityPlayerSP12));
                }
            }
        }
        switch (str) {
            case "clientName": {
                string = "LRQ";
                break;
            }
            case "clientVersion": {
                string = String.valueOf(1.0);
                break;
            }
            case "clientCreator": {
                string = "CCBlueX";
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

    public final String getClientName(int i, int i2) {
        String string = "SadNess";
        boolean bl = false;
        return string.substring(i, i2);
    }

    /*
     * Exception decompiling
     */
    @Override
    public Border drawElement() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Can't sort instructions [@NONE, blocks:[17] lbl114 : CaseStatement: default:\u000a, @NONE, blocks:[17] lbl114 : CaseStatement: default:\u000a]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.CompareByIndex.compare(CompareByIndex.java:25)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.CompareByIndex.compare(CompareByIndex.java:8)
         *     at java.util.TimSort.countRunAndMakeAscending(Unknown Source)
         *     at java.util.TimSort.sort(Unknown Source)
         *     at java.util.Arrays.sort(Unknown Source)
         *     at java.util.ArrayList.sort(Unknown Source)
         *     at java.util.Collections.sort(Unknown Source)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.buildSwitchCases(SwitchReplacer.java:271)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.replaceRawSwitch(SwitchReplacer.java:258)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.replaceRawSwitches(SwitchReplacer.java:66)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:517)
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
                    textValue.set(string);
                }
                this.updateElement();
                return;
            }
            if (ColorUtils.INSTANCE.isAllowedCharacter(c) || c == '\u00a7') {
                this.displayString.set((String)this.displayString.get() + c);
            }
            this.updateElement();
        }
    }

    public final Text2 setColor(Color c) {
        this.redValue.set(c.getRed());
        this.greenValue.set(c.getGreen());
        this.blueValue.set(c.getBlue());
        return this;
    }

    public Text2(double x, double y, float scale, Side side) {
        super(x, y, scale, side);
        this.displayString = new TextValue("DisplayText", "");
        this.redValue = new IntegerValue("Red", 255, 0, 255);
        this.greenValue = new IntegerValue("Green", 255, 0, 255);
        this.blueValue = new IntegerValue("Blue", 255, 0, 255);
        this.alphaValue = new IntegerValue("Alpha", 255, 0, 255);
        this.colorModeValue = new ListValue("Color", new String[]{"Custom", "Rainbow", "AnotherRainbow", "SkyRainbow"}, "Custom");
        this.shadow = new BoolValue("Shadow", false);
        this.rectRedValue = new IntegerValue("RectRed", 0, 0, 255);
        this.rectGreenValue = new IntegerValue("RectGreen", 0, 0, 255);
        this.rectBlueValue = new IntegerValue("RectBlue", 0, 0, 255);
        this.rectAlphaValue = new IntegerValue("RectAlpha", 255, 0, 255);
        this.rectColorModeValue = new ListValue("RectColor", new String[]{"Custom", "Rainbow", "AnotherRainbow", "SkyRainbow"}, "Custom");
        this.rectValue = new ListValue("Rect", new String[]{"Normal", "RNormal", "OneTap", "Skeet", "None"}, "None");
        this.rectExpandValue = new FloatValue("RectExpand", 0.3f, 0.0f, 1.0f);
        this.rainbowSpeed = new IntegerValue("RainbowSpeed", 10, 1, 10);
        this.rainbowIndex = new IntegerValue("RainbowIndex", 1, 1, 20);
        this.fontValue = new FontValue("Font", Fonts.font40);
        this.displayText = this.getDisplay();
    }

    public /* synthetic */ Text2(double d, double d2, float f, Side side, int n, DefaultConstructorMarker defaultConstructorMarker) {
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

    public Text2() {
        this(0.0, 0.0, 0.0f, null, 15, null);
    }

    static {
        Companion = new Companion(null);
        DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        HOUR_FORMAT = new SimpleDateFormat("HH:mm");
        DECIMAL_FORMAT = new DecimalFormat("#.##");
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

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

