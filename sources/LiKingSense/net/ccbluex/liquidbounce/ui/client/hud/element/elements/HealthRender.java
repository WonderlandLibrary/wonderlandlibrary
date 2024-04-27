/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
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
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RoundedUtil;
import org.jetbrains.annotations.NotNull;

@ElementInfo(name="HealthRender")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B-\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\b\u0010\n\u001a\u00020\u000bH\u0016\u00a8\u0006\f"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/HealthRender;", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Element;", "x", "", "y", "scale", "", "side", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Side;", "(DDFLnet/ccbluex/liquidbounce/ui/client/hud/element/Side;)V", "drawElement", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Border;", "LiKingSense"})
public final class HealthRender
extends Element {
    @Override
    @NotNull
    public Border drawElement() {
        Color color;
        Color color2;
        float health = MinecraftInstance.mc.getThePlayer().getHealth();
        float maxhealth = MinecraftInstance.mc.getThePlayer().getMaxHealth();
        float f = 0.0f;
        float f2 = 0.0f;
        RoundedUtil.drawRound(120.0f, 15.0f, 3.0f, (float)color2, (float)color2, (Color)0);
        float f3 = 0.0f;
        float f4 = 0.0f;
        RoundedUtil.drawRound(health / maxhealth * 120.0f, 15.0f, 3.0f, (float)color, (float)color, (Color)0);
        return new Border(0.0f, 0.0f, 120.0f, 15.0f, 0.0f);
    }

    public HealthRender(double x, double y, float scale, @NotNull Side side) {
        Intrinsics.checkParameterIsNotNull((Object)side, (String)"side");
        super(x, y, scale, side);
    }

    public /* synthetic */ HealthRender(double d, double d2, float f, Side side, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 1) != 0) {
            d = -8.0;
        }
        if ((n & 2) != 0) {
            d2 = 57.0;
        }
        if ((n & 4) != 0) {
            f = 1.0f;
        }
        if ((n & 8) != 0) {
            side = new Side(Side.Horizontal.MIDDLE, Side.Vertical.DOWN);
        }
        this(d, d2, f, side);
    }

    public HealthRender() {
        this(0.0, 0.0, 0.0f, null, 15, null);
    }
}

