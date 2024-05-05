package tech.drainwalk.utility.color;

import lombok.Setter;
import net.minecraft.client.Minecraft;

import java.awt.*;

public class FadeColor {

    private float prevSrcOffset, srcOffset;
    @Setter
    private int firstColor, secondColor;

    public int getAstolfo(float offset) {
        float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
        float srcOffset = this.prevSrcOffset + (this.srcOffset - this.prevSrcOffset) * partialTicks;
        float hue = (srcOffset + offset * 1) % 1f;
        float astolfo = 0.5f + hue;
        if (hue > 1f) {
            hue = 2f - hue;
        }
        return Color.HSBtoRGB(hue, 0.7f, 1f);
    }

    public int getFade(float offset) {
        float fadeOffset = ((srcOffset + offset * 1) % 1f) * 2f;
        if (fadeOffset > 1) {
            fadeOffset = 2 - fadeOffset;
        }
        return ColorUtility.interpolateColor(firstColor, secondColor, fadeOffset);
    }

    public void update(float value) {
        this.prevSrcOffset = this.srcOffset;
        this.srcOffset += value;
    }
}
