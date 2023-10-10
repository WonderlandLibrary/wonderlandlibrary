package tech.atani.client.listener.event.minecraft.render;

import net.minecraft.client.gui.ScaledResolution;
import tech.atani.client.listener.event.Event;

public class Render2DEvent extends Event {
    private final ScaledResolution scaledResolution;
    private final float partialTicks;
    
	public Render2DEvent(ScaledResolution scaledResolution, float partialTicks) {
		super();
		this.scaledResolution = scaledResolution;
		this.partialTicks = partialTicks;
	}

	public ScaledResolution getScaledResolution() {
		return scaledResolution;
	}

	public float getPartialTicks() {
		return partialTicks;
	}
    
}
