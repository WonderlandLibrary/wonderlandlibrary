/*
 * Decompiled with CFR 0.152.
 */
package cc.paimon.skid.lbp.newVer.element.components;

import cc.paimon.skid.lbp.newVer.extensions.AnimHelperKt;
import java.awt.Color;
import net.ccbluex.liquidbounce.utils.BlendUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;

public final class ToggleSwitch {
    private boolean state;
    private float smooth;

    public final boolean getState() {
        return this.state;
    }

    public final void onDraw(float f, float f2, float f3, float f4, Color color, Color color2) {
        this.smooth = AnimHelperKt.animLinear(this.smooth, (this.state ? 0.2f : -0.2f) * (float)RenderUtils.deltaTime * 0.045f, 0.0f, 1.0f);
        Color color3 = BlendUtils.blendColors(new float[]{0.0f, 1.0f}, new Color[]{new Color(160, 160, 160), color2}, this.smooth);
        Color color4 = BlendUtils.blendColors(new float[]{0.0f, 1.0f}, new Color[]{color, color2}, this.smooth);
        Color color5 = BlendUtils.blendColors(new float[]{0.0f, 1.0f}, new Color[]{new Color(160, 160, 160), color}, this.smooth);
        RenderUtils.drawRoundedRect(f - 0.5f, f2 - 0.5f, f + f3 + 0.5f, f2 + f4 + 0.5f, (f4 + 1.0f) / 2.0f, color3.getRGB());
        RenderUtils.drawRoundedRect(f, f2, f + f3, f2 + f4, f4 / 2.0f, color4.getRGB());
        RenderUtils.drawFilledCircle((int)(f + (1.0f - this.smooth) * (2.0f + (f4 - 4.0f) / 2.0f) + this.smooth * (f3 - 2.0f - (f4 - 4.0f) / 2.0f)), (int)(f2 + 2.0f + (f4 - 4.0f) / 2.0f), (f4 - 4.0f) / 2.0f, color5);
    }

    public final void setState(boolean bl) {
        this.state = bl;
    }
}

