/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package cc.paimon.skid.lbp.newVer.element.components;

import cc.paimon.skid.lbp.newVer.extensions.AnimHelperKt;
import java.awt.Color;
import net.ccbluex.liquidbounce.utils.BlendUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import org.lwjgl.opengl.GL11;

public final class Checkbox {
    private boolean state;
    private float smooth;

    public final void onDraw(float f, float f2, float f3, float f4, Color color, Color color2) {
        this.smooth = AnimHelperKt.animLinear(this.smooth, (this.state ? 0.2f : -0.2f) * (float)RenderUtils.deltaTime * 0.045f, 0.0f, 1.0f);
        Color color3 = BlendUtils.blendColors(new float[]{0.0f, 1.0f}, new Color[]{new Color(160, 160, 160), color2}, this.smooth);
        Color color4 = BlendUtils.blendColors(new float[]{0.0f, 1.0f}, new Color[]{color, color2}, this.smooth);
        RenderUtils.drawRoundedRect(f - 0.5f, f2 - 0.5f, f + f3 + 0.5f, f2 + f3 + 0.5f, 3.0f, color3.getRGB());
        RenderUtils.drawRoundedRect(f, f2, f + f3, f2 + f3, 3.0f, color4.getRGB());
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)1.0f);
        RenderUtils.drawLine((double)f + (double)f3 / 4.0, (double)f2 + (double)f3 / 2.0, (double)f + (double)f3 / 2.15, (double)f2 + (double)f3 / 4.0 * 3.0, 2.0f);
        RenderUtils.drawLine((double)f + (double)f3 / 2.15, (double)f2 + (double)f3 / 4.0 * 3.0, (double)f + (double)f3 / 3.95 * 3.0, (double)f2 + (double)f3 / 3.0, 2.0f);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public final void setState(boolean bl) {
        this.state = bl;
    }

    public final boolean getState() {
        return this.state;
    }
}

