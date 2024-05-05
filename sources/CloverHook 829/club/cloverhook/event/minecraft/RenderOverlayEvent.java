package club.cloverhook.event.minecraft;

import club.cloverhook.event.MultiStageEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

/**
 * @author antja03
 */
public class RenderOverlayEvent extends MultiStageEvent {
    private ScaledResolution resolution;

    public RenderOverlayEvent() {
        resolution = new ScaledResolution(Minecraft.getMinecraft());
    }

    public ScaledResolution getResolution() {
        return resolution;
    }
}
