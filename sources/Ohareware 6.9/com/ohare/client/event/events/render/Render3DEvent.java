package com.ohare.client.event.events.render;

import com.ohare.client.event.Event;

public class Render3DEvent extends Event {

	private float partialTicks;

	public Render3DEvent(final float partialTicks) {
		this.partialTicks = partialTicks;
	}

	public float getPartialTicks() {
		return partialTicks;
	}
}
