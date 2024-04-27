/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.ui.client.newui.element.components;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.ui.client.newui.ColorManager;
import net.ccbluex.liquidbounce.ui.client.newui.extensions.AnimHelperKt;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J&\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\fJ\u001e\u0010\r\u001a\u00020\u00072\u0006\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/newui/element/components/Slider;", "", "()V", "smooth", "", "value", "onDraw", "", "x", "y", "width", "accentColor", "Ljava/awt/Color;", "setValue", "desired", "min", "max", "LiKingSense"})
public final class Slider {
    private float smooth;
    private float value;

    public final void onDraw(float x, float y, float width, @NotNull Color accentColor) {
        Intrinsics.checkParameterIsNotNull((Object)accentColor, (String)"accentColor");
        this.smooth = AnimHelperKt.animSmooth(this.smooth, this.value, 0.5f);
        RenderUtils.drawRoundedRect(x - 1.0f, y - 1.0f, x + width + 1.0f, y + 1.0f, 1.0f, ColorManager.INSTANCE.getUnusedSlider().getRGB());
        RenderUtils.drawRoundedRect(x - 1.0f, y - 1.0f, x + width * (this.smooth / 100.0f) + 1.0f, y + 1.0f, 1.0f, accentColor.getRGB());
        RenderUtils.drawFilledCircle((int)(x + width * (this.smooth / 100.0f)), (int)y, 5.0f, Color.white);
        RenderUtils.drawFilledCircle((int)(x + width * (this.smooth / 100.0f)), (int)y, 3.0f, ColorManager.INSTANCE.getBackground());
    }

    public final void setValue(float desired, float min, float max) {
        this.value = (desired - min) / (max - min) * 100.0f;
    }
}

