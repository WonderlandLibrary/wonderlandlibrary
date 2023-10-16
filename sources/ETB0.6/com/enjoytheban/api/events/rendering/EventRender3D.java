package com.enjoytheban.api.events.rendering;

import com.enjoytheban.api.Event;

import shadersmod.client.Shaders;

/**
 * An Event for handling 3D related things, might do more on this soon
 * 
 * @author Purity
 * @called EntityRenderer func_175068_a
 */

public class EventRender3D extends Event {

	private float ticks;
	private boolean isUsingShaders;

	public EventRender3D() {
		this.isUsingShaders = Shaders.getShaderPackName() != null;
	}

	public EventRender3D(float ticks) {
		this.ticks = ticks;
		isUsingShaders = Shaders.getShaderPackName() != null;
	}

	public float getPartialTicks() {
		return ticks;
	}

	public boolean isUsingShaders() {
		return isUsingShaders;
	}
}