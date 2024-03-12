package xyz.cucumber.base.events.ext;

import net.minecraft.client.gui.ScaledResolution;
import xyz.cucumber.base.events.Event;

public class EventRenderGui extends Event{
	
	private ScaledResolution scaledResolution;

	public ScaledResolution getScaledResolution() {
		return scaledResolution;
	}

	public void setScaledResolution(ScaledResolution scaledResolution) {
		this.scaledResolution = scaledResolution;
	}

	public EventRenderGui(ScaledResolution scaledResolution) {
		this.scaledResolution = scaledResolution;
	} 
	
}
