/*
 * Decompiled with CFR 0.152.
 */
package cc.paimon.skid.lbp.newVer.element.components;

import cc.paimon.skid.lbp.newVer.ColorManager;
import cc.paimon.skid.lbp.newVer.extensions.AnimHelperKt;
import java.awt.Color;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;

public final class Slider {
    private float value;
    private float smooth;

    public final void setValue(float f, float f2, float f3) {
        this.value = (f - f2) / (f3 - f2) * 100.0f;
    }

    public final void onDraw(float f, float f2, float f3, Color color) {
        this.smooth = AnimHelperKt.animSmooth(this.smooth, this.value, 0.5f);
        RenderUtils.drawRoundedRect(f - 1.0f, f2 - 1.0f, f + f3 + 1.0f, f2 + 1.0f, 1.0f, ColorManager.INSTANCE.getUnusedSlider().getRGB());
        RenderUtils.drawRoundedRect(f - 1.0f, f2 - 1.0f, f + f3 * (this.smooth / 100.0f) + 1.0f, f2 + 1.0f, 1.0f, color.getRGB());
        RenderUtils.drawFilledCircle((int)(f + f3 * (this.smooth / 100.0f)), (int)f2, 5.0f, new Color(0, 140, 255));
        RenderUtils.drawFilledCircle((int)(f + f3 * (this.smooth / 100.0f)), (int)f2, 3.0f, ColorManager.INSTANCE.getBackground());
    }
}

