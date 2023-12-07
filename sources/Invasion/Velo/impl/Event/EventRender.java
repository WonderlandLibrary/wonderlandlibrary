package Velo.impl.Event;

import Velo.api.Event.Event;
import net.minecraft.client.gui.ScaledResolution;

public class EventRender extends Event<EventRender> {
	private float width, height;
	 public ScaledResolution sr;
	 private ScaledResolution scaledResolution;
	 
	 
	public EventRender(float width, float height, ScaledResolution sr) {
		this.height = height;
		this.width = width;
		 this.sr = sr;
	        this.scaledResolution = sr;
	}
	
	
	
	   public ScaledResolution getScaledResolution() {
	        return scaledResolution;
	    }
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
}
