/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.gui.client.hud.element.elements;

import java.awt.Color;
import java.text.DecimalFormat;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.gui.client.hud.element.Border;
import net.dev.important.gui.client.hud.element.Element;
import net.dev.important.gui.client.hud.element.ElementInfo;
import net.dev.important.gui.client.hud.element.Side;
import net.dev.important.gui.font.Fonts;
import net.dev.important.gui.font.GameFontRenderer;
import net.dev.important.modules.module.modules.combat.KillAura;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.misc.MovementUtil;
import net.dev.important.utils.render.ColorUtils;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import org.jetbrains.annotations.NotNull;

@ElementInfo(name="SessionInfo")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u000e\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\"\u001a\u00020#H\u0016R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0006\"\u0004\b\u000b\u0010\bR\u001a\u0010\f\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u0006\"\u0004\b\u000e\u0010\bR\u001a\u0010\u000f\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0006\"\u0004\b\u0011\u0010\bR\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u001dX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006$"}, d2={"Lnet/dev/important/gui/client/hud/element/elements/SessionInfo;", "Lnet/dev/important/gui/client/hud/element/Element;", "()V", "H", "", "getH", "()I", "setH", "(I)V", "HM", "getHM", "setHM", "M", "getM", "setM", "S", "getS", "setS", "addX", "", "addY", "alphaValue", "Lnet/dev/important/value/IntegerValue;", "blueValue", "gblueValue", "ggreenValue", "gredValue", "greenValue", "iconValue", "Lnet/dev/important/value/BoolValue;", "modeValue", "Lnet/dev/important/value/ListValue;", "redValue", "shadow", "drawElement", "Lnet/dev/important/gui/client/hud/element/Border;", "LiquidBounce"})
public final class SessionInfo
extends Element {
    @NotNull
    private final ListValue modeValue;
    @NotNull
    private final IntegerValue alphaValue;
    @NotNull
    private final IntegerValue redValue;
    @NotNull
    private final IntegerValue greenValue;
    @NotNull
    private final IntegerValue blueValue;
    @NotNull
    private final IntegerValue gredValue;
    @NotNull
    private final IntegerValue ggreenValue;
    @NotNull
    private final IntegerValue gblueValue;
    @NotNull
    private final BoolValue shadow;
    @NotNull
    private final BoolValue iconValue;
    private float addX;
    private float addY;
    private int HM;
    private int S;
    private int M;
    private int H;

    public SessionInfo() {
        super(200.0, 100.0, 1.0f, new Side(Side.Horizontal.RIGHT, Side.Vertical.UP));
        String[] stringArray = new String[]{"Default", "Shadow"};
        this.modeValue = new ListValue("BackGround", stringArray, "Default");
        this.alphaValue = new IntegerValue("Background-Alpha", 180, 0, 255);
        this.redValue = new IntegerValue("Red", 255, 0, 255);
        this.greenValue = new IntegerValue("Green", 255, 0, 255);
        this.blueValue = new IntegerValue("Blue", 255, 0, 255);
        this.gredValue = new IntegerValue("GradientRed", 255, 0, 255);
        this.ggreenValue = new IntegerValue("GradientGreen", 255, 0, 255);
        this.gblueValue = new IntegerValue("GradientBlue", 255, 0, 255);
        this.shadow = new BoolValue("Shadow", true);
        this.iconValue = new BoolValue("Icon", true);
    }

    public final int getHM() {
        return this.HM;
    }

    public final void setHM(int n) {
        this.HM = n;
    }

    public final int getS() {
        return this.S;
    }

    public final void setS(int n) {
        this.S = n;
    }

    public final int getM() {
        return this.M;
    }

    public final void setM(int n) {
        this.M = n;
    }

    public final int getH() {
        return this.H;
    }

    public final void setH(int n) {
        this.H = n;
    }

    @Override
    @NotNull
    public Border drawElement() {
        Color color1 = new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue());
        Color color2 = new Color(((Number)this.gredValue.get()).intValue(), ((Number)this.ggreenValue.get()).intValue(), ((Number)this.gblueValue.get()).intValue());
        ++this.HM;
        if (this.HM == 120) {
            ++this.S;
            this.HM = 0;
        }
        if (this.S == 60) {
            ++this.M;
            this.S = 0;
        }
        if (this.M == 60) {
            ++this.H;
            this.M = 0;
        }
        GameFontRenderer font = Fonts.font35;
        GameFontRenderer icon = Fonts.flux35;
        int color = Color.WHITE.getRGB();
        int fontHeight = Fonts.Poppins.field_78288_b;
        DecimalFormat format = new DecimalFormat("#.##");
        if (((Boolean)this.shadow.get()).booleanValue() && StringsKt.equals((String)this.modeValue.get(), "Default", true)) {
            RenderUtils.drawShadow(0, 0, 150, 3 + fontHeight + font.field_78288_b * 3 + 30);
        }
        if (((Boolean)this.shadow.get()).booleanValue() && StringsKt.equals((String)this.modeValue.get(), "Shadow", true)) {
            RenderUtils.drawShadow(0, 0, 150, 3 + fontHeight + font.field_78288_b * 3 + 30);
        }
        String string = ((String)this.modeValue.get()).toLowerCase();
        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
        String string2 = string;
        if (Intrinsics.areEqual(string2, "default")) {
            float f = 3.0f + (float)fontHeight + (float)(font.field_78288_b * 3) + 30.0f;
            Color color3 = Color.BLACK;
            Intrinsics.checkNotNullExpressionValue(color3, "BLACK");
            RenderUtils.drawRect(0.0f, 0.0f, 150.0f, f, ColorUtils.reAlpha(color3, ((Number)this.alphaValue.get()).intValue()));
        } else if (Intrinsics.areEqual(string2, "shadow")) {
            RenderUtils.drawShadow(0, 0, 150, 3 + fontHeight + font.field_78288_b * 3 + 30);
        }
        if (StringsKt.equals((String)this.modeValue.get(), "Default", true)) {
            RenderUtils.drawGradientSideways(0.0, 0.0, 150.0, 1.0, color1.getRGB(), color2.getRGB());
        }
        Fonts.Poppins.drawString("Session Info", 5.0f, 3.0f, color);
        if (((Boolean)this.iconValue.get()).booleanValue()) {
            icon.drawString("F", 5.0f, 3.0f + (float)fontHeight + 6.0f, color);
            icon.drawString("I", 5.0f, 3.0f + (float)fontHeight + (float)font.field_78288_b + 11.0f, color);
            icon.drawString("K", 5.0f, 3.0f + (float)fontHeight + (float)(font.field_78288_b * 2) + 16.0f, color);
            icon.drawString("G", 5.0f, 3.0f + (float)fontHeight + (float)(font.field_78288_b * 3) + 21.0f, color);
        }
        if (((Boolean)this.iconValue.get()).booleanValue()) {
            font.drawString("Played Time", 7.0f + (float)icon.func_78256_a("F"), 3.0f + (float)fontHeight + 5.0f, color);
            font.drawString("Speed", 7.0f + (float)icon.func_78256_a("I"), 3.0f + (float)fontHeight + (float)font.field_78288_b + 10.0f, color);
            font.drawString("Ping", 7.0f + (float)icon.func_78256_a("K"), 3.0f + (float)fontHeight + (float)(font.field_78288_b * 2) + 15.0f, color);
            font.drawString("Kills", 7.0f + (float)icon.func_78256_a("G"), 3.0f + (float)fontHeight + (float)(font.field_78288_b * 3) + 20.0f, color);
        } else {
            font.drawString("Played Time", 7.0f, 3.0f + (float)fontHeight + 5.0f, color);
            font.drawString("Speed", 7.0f, 3.0f + (float)fontHeight + (float)font.field_78288_b + 10.0f, color);
            font.drawString("Ping", 7.0f, 3.0f + (float)fontHeight + (float)(font.field_78288_b * 2) + 15.0f, color);
            font.drawString("Kills", 7.0f, 3.0f + (float)fontHeight + (float)(font.field_78288_b * 3) + 20.0f, color);
        }
        font.drawString(this.H + "h " + this.M + "m " + this.S + 's', (float)(150 - font.func_78256_a(this.H + "h " + this.M + "m " + this.S + 's')) - 5.0f, 3.0f + (float)fontHeight + 5.0f, color);
        string2 = format.format(MovementUtil.INSTANCE.getBps());
        Intrinsics.checkNotNullExpressionValue(string2, "format.format(MovementUtil.bps)");
        String string3 = string2;
        string2 = format.format(MovementUtil.INSTANCE.getBps());
        Intrinsics.checkNotNullExpressionValue(string2, "format.format(MovementUtil.bps)");
        font.drawString(string3, (float)(150 - font.func_78256_a(string2)) - 5.0f, 3.0f + (float)fontHeight + (float)font.field_78288_b + 10.0f, color);
        if (MinecraftInstance.mc.func_71356_B()) {
            font.drawString("0ms (Singleplayer)", (float)(150 - font.func_78256_a("0ms (Singleplayer)")) - 5.0f, 3.0f + (float)fontHeight + (float)(font.field_78288_b * 2) + 15.0f, color);
        } else {
            font.drawString(String.valueOf(MinecraftInstance.mc.func_147114_u().func_175102_a(MinecraftInstance.mc.field_71439_g.func_110124_au()).func_178853_c()), (float)(150 - font.func_78256_a(String.valueOf(MinecraftInstance.mc.func_147114_u().func_175102_a(MinecraftInstance.mc.field_71439_g.func_110124_au()).func_178853_c()))) - 5.0f, 3.0f + (float)fontHeight + (float)(font.field_78288_b * 2) + 15.0f, color);
        }
        font.drawString(String.valueOf(KillAura.Companion.getKillCounts()), (float)(150 - font.func_78256_a(String.valueOf(KillAura.Companion.getKillCounts()))) - 5.0f, 3.0f + (float)fontHeight + (float)(font.field_78288_b * 3) + 20.0f, color);
        return new Border(0.0f, 0.0f, 150.0f, 3.0f + (float)fontHeight + (float)(font.field_78288_b * 3) + 30.0f);
    }
}

