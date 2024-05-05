/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.Side;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.Colors;
import net.ccbluex.liquidbounce.utils.Translate2;
import net.ccbluex.liquidbounce.utils.render.BlurUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="Packet-Vanilla-CheckDisplay")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B-\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\n\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0016R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u000fX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u000fX\u0082D\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/KickWarn;", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Element;", "x", "", "y", "scale", "", "side", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Side;", "(DDFLnet/ccbluex/liquidbounce/ui/client/hud/element/Side;)V", "backgroundValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "blurStrength", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "counter", "", "lastCounter", "packetsCheck", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "tick0", "tick1", "tick2", "translat", "Lnet/ccbluex/liquidbounce/utils/Translate2;", "vanillaCheck", "xValue", "yValue", "drawElement", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Border;", "KyinoClient"})
public final class KickWarn
extends Element {
    private final int xValue;
    private final int yValue;
    private final BoolValue packetsCheck;
    private final BoolValue vanillaCheck;
    private final ListValue backgroundValue;
    private final FloatValue blurStrength;
    private Translate2 translat;
    private int counter;
    private int lastCounter;
    private int tick0;
    private int tick1;
    private int tick2;

    @Override
    @Nullable
    public Border drawElement() {
        float x = (float)RenderUtils.width() / 2.0f + (float)this.xValue;
        float y = (float)RenderUtils.height() / 2.0f + (float)this.yValue;
        float width = x + (float)100;
        GL11.glPushMatrix();
        float[] fractions = new float[]{0.0f, 0.5f, 1.0f};
        Color[] colors = new Color[]{new Color(100, 225, 100), new Color(225, 225, 100), new Color(225, 75, 75)};
        this.translat.translate2(this.tick1, this.tick2, 0.5);
        if (((Boolean)this.packetsCheck.get()).booleanValue()) {
            Fonts.font40.func_175065_a("TooMany Packets", x + (100.0f - (float)Fonts.font40.func_78256_a("TooMany Packets")) / 2.0f, y, new Color(255, 255, 255, 200).getRGB(), false);
            switch ((String)this.backgroundValue.get()) {
                case "Blur": {
                    BlurUtils.blurArea(x, y + 10.0f, width, y + 25.0f, ((Number)this.blurStrength.get()).floatValue());
                    break;
                }
                case "Default": {
                    RenderUtils.drawRect(x, y + 10.0f, width, y + 25.0f, new Color(0, 0, 0, 125));
                }
            }
            RenderUtils.drawRect(x, y + 10.0f, Math.min(x + this.translat.getX(), width), y + 25.0f, Colors.blendColors(fractions, colors, this.translat.getX() / 50.0f));
        }
        if (((Boolean)this.vanillaCheck.get()).booleanValue()) {
            Fonts.font40.func_175065_a("Vanilla Kick", x + (100.0f - (float)Fonts.font40.func_78256_a("Vanilla Kick")) / 2.0f, y + 35.0f, new Color(255, 255, 255, 200).getRGB(), false);
            switch ((String)this.backgroundValue.get()) {
                case "Blur": {
                    BlurUtils.blurArea(x, y + 45.0f, width, y + 60.0f, ((Number)this.blurStrength.get()).floatValue());
                    break;
                }
                case "Default": {
                    RenderUtils.drawRect(x, y + 45.0f, width, y + 60.0f, new Color(0, 0, 0, 125));
                }
            }
            RenderUtils.drawRect(x, y + 45.0f, Math.min(x + this.translat.getY(), width), y + 60.0f, Colors.blendColors(fractions, colors, this.translat.getY() / 50.0f));
        }
        GL11.glPopMatrix();
        return null;
    }

    public KickWarn(double x, double y, float scale, @NotNull Side side) {
        Intrinsics.checkParameterIsNotNull(side, "side");
        super(x, y, scale, side);
        this.xValue = -487;
        this.yValue = -304;
        this.packetsCheck = new BoolValue("Packets-Check", true);
        this.vanillaCheck = new BoolValue("Vanilla-Check", true);
        this.backgroundValue = new ListValue("Background-Color", new String[]{"Blur", "Default"}, "Default");
        this.blurStrength = new FloatValue("Blur-Strength", 0.0f, 0.0f, 30.0f);
        this.translat = new Translate2(0.0f, 0.0f);
    }

    public /* synthetic */ KickWarn(double d, double d2, float f, Side side, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 1) != 0) {
            d = 5.0;
        }
        if ((n & 2) != 0) {
            d2 = 25.0;
        }
        if ((n & 4) != 0) {
            f = 1.0f;
        }
        if ((n & 8) != 0) {
            side = new Side(Side.Horizontal.RIGHT, Side.Vertical.DOWN);
        }
        this(d, d2, f, side);
    }

    public KickWarn() {
        this(0.0, 0.0, 0.0f, null, 15, null);
    }
}

