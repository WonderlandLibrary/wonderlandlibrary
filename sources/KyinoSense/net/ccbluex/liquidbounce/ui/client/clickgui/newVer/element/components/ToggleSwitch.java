/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.newVer.element.components;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.ui.client.clickgui.newVer.extensions.AnimHelperKt;
import net.ccbluex.liquidbounce.utils.render.BlendUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J6\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0012R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\n\u00a8\u0006\u0014"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/clickgui/newVer/element/components/ToggleSwitch;", "", "()V", "smooth", "", "state", "", "getState", "()Z", "setState", "(Z)V", "onDraw", "", "x", "y", "width", "height", "bgColor", "Ljava/awt/Color;", "accentColor", "KyinoClient"})
public final class ToggleSwitch {
    private float smooth;
    private boolean state;

    public final boolean getState() {
        return this.state;
    }

    public final void setState(boolean bl) {
        this.state = bl;
    }

    public final void onDraw(float x, float y, float width, float height, @NotNull Color bgColor, @NotNull Color accentColor) {
        Intrinsics.checkParameterIsNotNull(bgColor, "bgColor");
        Intrinsics.checkParameterIsNotNull(accentColor, "accentColor");
        this.smooth = AnimHelperKt.animLinear(this.smooth, (this.state ? 0.2f : -0.2f) * (float)RenderUtils.deltaTime * 0.045f, 0.0f, 1.0f);
        Color borderColor = BlendUtils.blendColors(new float[]{0.0f, 1.0f}, new Color[]{new Color(160, 160, 160), accentColor}, this.smooth);
        Color mainColor = BlendUtils.blendColors(new float[]{0.0f, 1.0f}, new Color[]{bgColor, accentColor}, this.smooth);
        Color switchColor = BlendUtils.blendColors(new float[]{0.0f, 1.0f}, new Color[]{new Color(160, 160, 160), bgColor}, this.smooth);
        float f = (height + 1.0f) / 2.0f;
        Color color = borderColor;
        Intrinsics.checkExpressionValueIsNotNull(color, "borderColor");
        RenderUtils.originalRoundedRect(x - 0.5f, y - 0.5f, x + width + 0.5f, y + height + 0.5f, f, color.getRGB());
        float f2 = height / 2.0f;
        Color color2 = mainColor;
        Intrinsics.checkExpressionValueIsNotNull(color2, "mainColor");
        RenderUtils.originalRoundedRect(x, y, x + width, y + height, f2, color2.getRGB());
        RenderUtils.drawFilledCircleGui(x + (1.0f - this.smooth) * (2.0f + (height - 4.0f) / 2.0f) + this.smooth * (width - 2.0f - (height - 4.0f) / 2.0f), y + 2.0f + (height - 4.0f) / 2.0f, (height - 4.0f) / 2.0f, switchColor);
    }
}

