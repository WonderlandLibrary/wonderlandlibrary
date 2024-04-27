/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.jvm.internal.StringCompanionObject
 *  kotlin.text.StringsKt
 *  net.minecraft.client.Minecraft
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 */
package ad.novoline.ui;

import ad.Palette;
import java.awt.Color;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.Side;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.InfosUtils.Recorder;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.blur.BlurUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="Session Info")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B-\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\n\u0010#\u001a\u0004\u0018\u00010$H\u0016J\b\u0010%\u001a\u00020&H\u0016R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0014\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u0017\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0016R\u000e\u0010\u0019\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001f\u001a\u00020\u0006X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0016R\u0011\u0010!\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u0016\u00a8\u0006'"}, d2={"Lad/novoline/ui/SessionInfo;", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Element;", "x", "", "y", "scale", "", "side", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Side;", "(DDFLnet/ccbluex/liquidbounce/ui/client/hud/element/Side;)V", "bgalphaValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "bgblueValue", "bggreenValue", "bgredValue", "blur", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "colorModeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "distanceValue", "floatX", "getFloatX", "()F", "floatY", "getFloatY", "gidentspeed", "gradientAmountValue", "outline", "radiusValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "shadowValue", "x2", "getX2", "y2", "getY2", "drawElement", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Border;", "shadow", "", "LiKingSense"})
public final class SessionInfo
extends Element {
    private final ListValue shadowValue;
    private final ListValue colorModeValue;
    private final IntegerValue gidentspeed;
    private final IntegerValue distanceValue;
    private final IntegerValue gradientAmountValue;
    private final FloatValue radiusValue;
    private final BoolValue blur;
    private final BoolValue outline;
    private final IntegerValue bgredValue;
    private final IntegerValue bggreenValue;
    private final IntegerValue bgblueValue;
    private final IntegerValue bgalphaValue;
    private final float y2;
    private final float x2 = 137.5f;
    private final float floatX;
    private final float floatY;

    public final float getY2() {
        return this.y2;
    }

    public final float getX2() {
        return this.x2;
    }

    public final float getFloatX() {
        return this.floatX;
    }

    public final float getFloatY() {
        return this.floatY;
    }

    @Override
    public void shadow() {
        RenderUtils.drawRoundedRect2(0.0f, -2.3f, this.x2, this.y2 - 0.3f, ((Number)this.radiusValue.get()).floatValue(), new Color(((Number)this.bgredValue.get()).intValue(), ((Number)this.bggreenValue.get()).intValue(), ((Number)this.bgblueValue.get()).intValue(), ((Number)this.bgalphaValue.get()).intValue() + 190).getRGB());
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @Nullable
    public Border drawElement() {
        String string;
        float y2 = ((float)(Fonts.posterama40.getFontHeight() * 5) + 11.0f + 3.0f) * 1.1f;
        float x2 = 150.0f;
        String time = null;
        Minecraft minecraft = Minecraft.func_71410_x();
        Intrinsics.checkExpressionValueIsNotNull((Object)minecraft, (String)"Minecraft.getMinecraft()");
        if (minecraft.func_71356_B()) {
            string = "SinglePlayer";
        } else {
            long durationInMillis = System.currentTimeMillis() - Recorder.INSTANCE.getStartTime();
            long second = durationInMillis / (long)1000 % (long)60;
            long minute = durationInMillis / (long)60000 % (long)60;
            long hour = durationInMillis / (long)3600000 % (long)24;
            StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
            String string2 = "%02d:%02d:%02d";
            Object[] objectArray = new Object[]{hour, minute, second};
            boolean bl = false;
            String string3 = String.format(string2, Arrays.copyOf(objectArray, objectArray.length));
            string = string3;
            Intrinsics.checkExpressionValueIsNotNull((Object)string3, (String)"java.lang.String.format(format, *args)");
        }
        time = string;
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(HUD.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.render.HUD");
        }
        HUD hud = (HUD)module;
        int n = 0;
        int n2 = ((Number)this.gradientAmountValue.get()).intValue() - 1;
        if (n <= n2) {
            while (true) {
                int n3;
                int n4;
                void i;
                String colorMode = (String)this.colorModeValue.get();
                double x3 = 144.67200574874877;
                double barStart = (double)i / (double)((Number)this.gradientAmountValue.get()).intValue() * x3;
                double barEnd = (double)(i + true) / (double)((Number)this.gradientAmountValue.get()).intValue() * x3;
                double d = (double)2 + barStart;
                double d2 = (double)Fonts.posterama40.getFontHeight() + 2.5 + 0.0;
                double d3 = barEnd + (double)2;
                double d4 = (double)Fonts.posterama45.getFontHeight() + 2.5 + (double)1.4f;
                if (StringsKt.equals((String)colorMode, (String)"Fade", (boolean)true)) {
                    Color color = Palette.fade2(new Color(((Number)hud.getR().get()).intValue(), ((Number)hud.getG().get()).intValue(), ((Number)hud.getB().get()).intValue()), (int)(i * ((Number)this.distanceValue.get()).intValue()), (int)(x3 * (double)200));
                    Intrinsics.checkExpressionValueIsNotNull((Object)color, (String)"Palette.fade2(Color(hud.\u2026et(), (x3 * 200).toInt())");
                    n4 = color.getRGB();
                } else if (StringsKt.equals((String)colorMode, (String)"Gident", (boolean)true)) {
                    Color color = RenderUtils.getGradientOffset3(new Color(((Number)hud.getR().get()).intValue(), ((Number)hud.getG().get()).intValue(), ((Number)hud.getB().get()).intValue()), new Color(((Number)hud.getR2().get()).intValue(), ((Number)hud.getG2().get()).intValue(), ((Number)hud.getB2().get()).intValue(), (int)i), Math.abs((double)System.currentTimeMillis() / (double)((Number)this.gidentspeed.get()).intValue() + (double)(i * ((Number)this.distanceValue.get()).intValue())) / (double)10);
                    Intrinsics.checkExpressionValueIsNotNull((Object)color, (String)"RenderUtils.getGradientO\u2026) / 10)\n                )");
                    n4 = color.getRGB();
                } else {
                    Color color = Color.WHITE;
                    Intrinsics.checkExpressionValueIsNotNull((Object)color, (String)"Color.WHITE");
                    n4 = color.getRGB();
                }
                if (StringsKt.equals((String)colorMode, (String)"Fade", (boolean)true)) {
                    Color color = Palette.fade2(new Color(((Number)hud.getR().get()).intValue(), ((Number)hud.getG().get()).intValue(), ((Number)hud.getB().get()).intValue()), (int)(i * ((Number)this.distanceValue.get()).intValue()), (int)(x3 * (double)200));
                    Intrinsics.checkExpressionValueIsNotNull((Object)color, (String)"Palette.fade2(Color(hud.\u2026t()\n                    )");
                    n3 = color.getRGB();
                } else if (StringsKt.equals((String)colorMode, (String)"Gident", (boolean)true)) {
                    Color color = RenderUtils.getGradientOffset(new Color(((Number)hud.getR().get()).intValue(), ((Number)hud.getG().get()).intValue(), ((Number)hud.getB().get()).intValue()), new Color(((Number)hud.getR2().get()).intValue(), ((Number)hud.getG2().get()).intValue(), ((Number)hud.getB2().get()).intValue(), 1), Math.abs((double)System.currentTimeMillis() / (double)((Number)this.gidentspeed.get()).intValue() + (double)(i * ((Number)this.distanceValue.get()).intValue())) / (double)10);
                    Intrinsics.checkExpressionValueIsNotNull((Object)color, (String)"RenderUtils.getGradientO\u202610)\n                    )");
                    n3 = color.getRGB();
                } else {
                    Color color = Color.WHITE;
                    Intrinsics.checkExpressionValueIsNotNull((Object)color, (String)"Color.WHITE");
                    n3 = color.getRGB();
                }
                RenderUtils.drawGradientSideways(d, d2, d3, d4, n4, n3);
                if (i == n2) break;
                ++i;
            }
        }
        switch ((String)this.shadowValue.get()) {
            case "Basic": {
                RenderUtils.drawShadow(0.0f, -3.0f, x2, y2 - 5.0f);
                break;
            }
            case "Thick": {
                RenderUtils.drawShadowWithCustomAlpha(0.0f, -3.0f, x2, y2 - 3.0f, 210.0f);
                RenderUtils.drawShadowWithCustomAlpha(0.0f, -3.0f, x2, y2 - 3.0f, 210.0f);
                break;
            }
        }
        if (((Boolean)this.blur.get()).booleanValue()) {
            GL11.glTranslated((double)(-this.getRenderX()), (double)(-this.getRenderY()), (double)0.0);
            GL11.glPushMatrix();
            BlurUtils.blurRoundArea((float)this.getRenderX(), (float)this.getRenderY() - 3.0f, x2, y2, (int)((Number)this.radiusValue.get()).floatValue());
            GL11.glPopMatrix();
            GL11.glTranslated((double)this.getRenderX(), (double)this.getRenderY(), (double)0.0);
        }
        RenderUtils.drawRoundedRect2(0.0f, -3.0f, x2, y2 - 3.0f, ((Number)this.radiusValue.get()).floatValue(), new Color(((Number)this.bgredValue.get()).intValue(), ((Number)this.bggreenValue.get()).intValue(), ((Number)this.bgblueValue.get()).intValue(), ((Number)this.bgalphaValue.get()).intValue()).getRGB());
        if (((Boolean)this.outline.get()).booleanValue()) {
            RenderUtils.drawGidentOutlinedRoundedRect(0.0, -3.0, x2, (double)y2 - (double)5.0f, ((Number)this.radiusValue.get()).floatValue(), 2.5f);
        }
        float f = x2 / 2.0f;
        Color color = Color.WHITE;
        Intrinsics.checkExpressionValueIsNotNull((Object)color, (String)"Color.WHITE");
        Fonts.posterama45.drawCenteredString("Session Infomation", f, 0.0f, color.getRGB(), true);
        float f2 = Fonts.posterama40.getFontHeight() + 7;
        Color color2 = Color.WHITE;
        Intrinsics.checkExpressionValueIsNotNull((Object)color2, (String)"Color.WHITE");
        Fonts.posterama40.drawString("Play Time:", 3.0f, f2, color2.getRGB(), true);
        String string4 = String.valueOf(time);
        float f3 = x2 - (float)Fonts.posterama40.getStringWidth(String.valueOf(time)) - 1.0f;
        float f4 = (float)Fonts.posterama40.getFontHeight() + 7.0f;
        Color color3 = Color.WHITE;
        Intrinsics.checkExpressionValueIsNotNull((Object)color3, (String)"Color.WHITE");
        Fonts.posterama40.drawString(string4, f3, f4, color3.getRGB(), true);
        float f5 = Fonts.posterama40.getFontHeight() * 2 + 9;
        Color color4 = Color.WHITE;
        Intrinsics.checkExpressionValueIsNotNull((Object)color4, (String)"Color.WHITE");
        Fonts.posterama40.drawString("Games Won", 3.0f, f5, color4.getRGB(), true);
        String string5 = String.valueOf(Recorder.INSTANCE.getWin());
        float f6 = x2 - (float)Fonts.posterama40.getStringWidth(String.valueOf(Recorder.INSTANCE.getWin())) - 1.0f;
        float f7 = (float)(Fonts.posterama40.getFontHeight() * 2) + 9.0f;
        Color color5 = Color.WHITE;
        Intrinsics.checkExpressionValueIsNotNull((Object)color5, (String)"Color.WHITE");
        Fonts.posterama40.drawString(string5, f6, f7, color5.getRGB(), true);
        float f8 = Fonts.posterama40.getFontHeight() * 3 + 11;
        Color color6 = Color.WHITE;
        Intrinsics.checkExpressionValueIsNotNull((Object)color6, (String)"Color.WHITE");
        Fonts.posterama40.drawString("Players Killed", 3.0f, f8, color6.getRGB(), true);
        String string6 = String.valueOf(LiquidBounce.INSTANCE.getCombatManager().getKills());
        float f9 = x2 - (float)Fonts.posterama40.getStringWidth(String.valueOf(LiquidBounce.INSTANCE.getCombatManager().getKills())) - 1.0f;
        float f10 = (float)(Fonts.posterama40.getFontHeight() * 3) + 11.0f;
        Color color7 = Color.WHITE;
        Intrinsics.checkExpressionValueIsNotNull((Object)color7, (String)"Color.WHITE");
        Fonts.posterama40.drawString(string6, f9, f10, color7.getRGB(), true);
        float f11 = Fonts.posterama40.getFontHeight() * 4 + 13;
        Color color8 = Color.WHITE;
        Intrinsics.checkExpressionValueIsNotNull((Object)color8, (String)"Color.WHITE");
        Fonts.posterama40.drawString("Staff/watchdogs Bans", 3.0f, f11, color8.getRGB(), true);
        String string7 = "0/" + Recorder.INSTANCE.getBan();
        float f12 = x2 - (float)Fonts.posterama40.getStringWidth("0/" + Recorder.INSTANCE.getBan()) - 1.0f;
        float f13 = (float)(Fonts.posterama40.getFontHeight() * 4) + 13.0f;
        Color color9 = Color.WHITE;
        Intrinsics.checkExpressionValueIsNotNull((Object)color9, (String)"Color.WHITE");
        Fonts.posterama40.drawString(string7, f12, f13, color9.getRGB(), true);
        return new Border(-2.0f, -2.0f, x2, y2, ((Number)this.radiusValue.get()).floatValue());
    }

    /*
     * Exception decompiling
     */
    public SessionInfo(double x, double y, float scale, @NotNull Side side) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl58 : PUTFIELD - null : Stack underflow
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

    public /* synthetic */ SessionInfo(double d, double d2, float f, Side side, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 1) != 0) {
            d = 10.0;
        }
        if ((n & 2) != 0) {
            d2 = 29.0;
        }
        if ((n & 4) != 0) {
            f = 1.0f;
        }
        if ((n & 8) != 0) {
            side = new Side(Side.Horizontal.LEFT, Side.Vertical.UP);
        }
        this(d, d2, f, side);
    }

    public SessionInfo() {
        this(0.0, 0.0, 0.0f, null, 15, null);
    }
}

