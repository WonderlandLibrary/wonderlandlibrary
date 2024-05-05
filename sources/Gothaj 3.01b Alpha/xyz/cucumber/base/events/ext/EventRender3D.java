package xyz.cucumber.base.events.ext;

import xyz.cucumber.base.events.Event;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;


public class EventRender3D extends Event{
	
	private float partialTicks;

	public float getPartialTicks() {
		return partialTicks;
	}

	public void setPartialTicks(float partialTicks) {
		this.partialTicks = partialTicks;
	}

	public EventRender3D(float partialTicks) {
		this.partialTicks = partialTicks;
	}
	
	
	
}
