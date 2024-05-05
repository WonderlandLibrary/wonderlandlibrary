package club.cloverhook.cheat.impl.visual;

import club.cloverhook.cheat.Cheat;
import club.cloverhook.cheat.CheatCategory;
import club.cloverhook.event.minecraft.RenderOverlayEvent;
import club.cloverhook.utils.Draw;
import club.cloverhook.utils.property.impl.BooleanProperty;
import club.cloverhook.utils.property.impl.ColorProperty;
import club.cloverhook.utils.property.impl.DoubleProperty;
import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

public class Crosshair extends Cheat {

    public ColorProperty color = new ColorProperty("Color", "Changes the color.", null, 1f, 0f, 1f, 255);
    private BooleanProperty fixed = new BooleanProperty("Fixed", "Bsaically no spread xD.", null, false);
    private DoubleProperty width = new DoubleProperty("Width", "The width.", null, 0.4, 0.25, 5, 0.05, null);
    private DoubleProperty gap = new DoubleProperty("Gap", "The gap.", null, 1.2, 0.25, 5, 0.05, null);
    private DoubleProperty length = new DoubleProperty("Length", "The length.", null, 5, 0.25, 15, 0.05, null);

    public Crosshair() {
        super("Crosshair", "CSGO lookin ass nibba", CheatCategory.VISUAL);
        registerProperties(color, fixed, width, gap, length);
    }

    @Collect
    public void onRender(RenderOverlayEvent e) {
        int alph = 255;

        ScaledResolution scaledRes = new ScaledResolution(mc);
        Draw.drawBorderedRectangle(
                scaledRes.getScaledWidth() / 2 - width.getValue(),
                scaledRes.getScaledHeight() / 2 - gap.getValue() - length.getValue() - getGap(),
                scaledRes.getScaledWidth() / 2 + 1.0f + width.getValue(),
                scaledRes.getScaledHeight() / 2 - gap.getValue() - getGap(),
                0.5f, color.getValue().getRGB(),
                new Color(0, 0, 0, alph).getRGB(), true);
        Draw.drawBorderedRectangle(
                scaledRes.getScaledWidth() / 2 - width.getValue(),
                scaledRes.getScaledHeight() / 2 + gap.getValue() + 1 + getGap() - 0.15,
                scaledRes.getScaledWidth() / 2 + 1.0f + width.getValue(),
                scaledRes.getScaledHeight() / 2 + 1 + gap.getValue() + length.getValue() + getGap()  - 0.15, 0.5f, color.getValue().getRGB(),
                new Color(0, 0, 0, alph).getRGB(), true);
        Draw.drawBorderedRectangle(
                scaledRes.getScaledWidth() / 2 - gap.getValue() - getGap() - length.getValue()  + 0.15,
                scaledRes.getScaledHeight() / 2 - width.getValue(),
                scaledRes.getScaledWidth() / 2 - gap.getValue() - getGap() + 0.15,
                scaledRes.getScaledHeight() / 2 + 1.0f + width.getValue(), 0.5f, color.getValue().getRGB(),
                new Color(0, 0, 0, alph).getRGB(), true);
        Draw.drawBorderedRectangle(
                scaledRes.getScaledWidth() / 2 + 1 + gap.getValue() + getGap(),
                scaledRes.getScaledHeight() / 2 - width.getValue(),
                scaledRes.getScaledWidth() / 2 + length.getValue() + gap.getValue() + getGap() + 1.0f,
                scaledRes.getScaledHeight() / 2 + 1.0f + width.getValue(), 0.5f, color.getValue().getRGB(),
                new Color(0, 0, 0, alph).getRGB(), true);
    }

    public double getGap() {
        if(fixed.getValue()) {
            return 0;
        }
        return (mc.thePlayer.isMoving() ? (mc.thePlayer.isSprinting() ? 2 : 1) : 0) - (mc.thePlayer.isSneaking() ? 1 : 0) + (mc.thePlayer.swingProgress > 0 ? 2 : 0);
    }

}
