package rip.helium.event.minecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import rip.helium.event.MultiStageEvent;

/**
 * @author antja03
 */
public class RenderOverlayEvent extends MultiStageEvent {
    private final ScaledResolution resolution;

    public RenderOverlayEvent() {
        resolution = new ScaledResolution(Minecraft.getMinecraft());
    }

    public ScaledResolution getResolution() {
        return resolution;
    }
}
