package rip.helium.cheat.impl.visual;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.gui.ScaledResolution;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.event.minecraft.RenderOverlayEvent;
import rip.helium.utils.Draw;
import rip.helium.utils.property.impl.BooleanProperty;
import rip.helium.utils.property.impl.ColorProperty;
import rip.helium.utils.property.impl.DoubleProperty;

import java.awt.*;

public class Crosshair extends Cheat {

    public ColorProperty color = new ColorProperty("Color", "Changes the color.", null, 1f, 0f, 1f, 255);
    private final BooleanProperty fixed = new BooleanProperty("Fixed", "Bsaically no spread xD.", null, false);
    private final DoubleProperty width = new DoubleProperty("Width", "The width.", null, 0.4, 0.25, 5, 0.05, null);
    private final DoubleProperty gap = new DoubleProperty("Gap", "The gap.", null, 1.2, 0.25, 5, 0.05, null);
    private final DoubleProperty length = new DoubleProperty("Length", "The length.", null, 5, 0.25, 15, 0.05, null);

    public Crosshair() {
        super("Crosshair", "CSGO lookin ass nibba", CheatCategory.VISUAL);
        registerProperties(color, fixed, width, gap, length);
    }

    @Collect
    public void onRender(RenderOverlayEvent e) {
        int alph = 255;

        ScaledResolution scaledRes = new ScaledResolution(mc);
        Draw.drawBorderedRectangle(
                ScaledResolution.getScaledWidth() / 2 - width.getValue(),
                ScaledResolution.getScaledHeight() / 2 - gap.getValue() - length.getValue() - getGap(),
                ScaledResolution.getScaledWidth() / 2 + 1.0f + width.getValue(),
                ScaledResolution.getScaledHeight() / 2 - gap.getValue() - getGap(),
                0.5f, color.getValue().getRGB(),
                new Color(0, 0, 0, alph).getRGB(), true);
        Draw.drawBorderedRectangle(
                ScaledResolution.getScaledWidth() / 2 - width.getValue(),
                ScaledResolution.getScaledHeight() / 2 + gap.getValue() + 1 + getGap() - 0.15,
                ScaledResolution.getScaledWidth() / 2 + 1.0f + width.getValue(),
                ScaledResolution.getScaledHeight() / 2 + 1 + gap.getValue() + length.getValue() + getGap() - 0.15, 0.5f, color.getValue().getRGB(),
                new Color(0, 0, 0, alph).getRGB(), true);
        Draw.drawBorderedRectangle(
                ScaledResolution.getScaledWidth() / 2 - gap.getValue() - getGap() - length.getValue() + 0.15,
                ScaledResolution.getScaledHeight() / 2 - width.getValue(),
                ScaledResolution.getScaledWidth() / 2 - gap.getValue() - getGap() + 0.15,
                ScaledResolution.getScaledHeight() / 2 + 1.0f + width.getValue(), 0.5f, color.getValue().getRGB(),
                new Color(0, 0, 0, alph).getRGB(), true);
        Draw.drawBorderedRectangle(
                ScaledResolution.getScaledWidth() / 2 + 1 + gap.getValue() + getGap(),
                ScaledResolution.getScaledHeight() / 2 - width.getValue(),
                ScaledResolution.getScaledWidth() / 2 + length.getValue() + gap.getValue() + getGap() + 1.0f,
                ScaledResolution.getScaledHeight() / 2 + 1.0f + width.getValue(), 0.5f, color.getValue().getRGB(),
                new Color(0, 0, 0, alph).getRGB(), true);
    }

    public double getGap() {
        if (fixed.getValue()) {
            return 0;
        }
        return (mc.thePlayer.isMoving() ? (mc.thePlayer.isSprinting() ? 2 : 1) : 0) - (mc.thePlayer.isSneaking() ? 1 : 0) + (mc.thePlayer.swingProgress > 0 ? 2 : 0);
    }

}
