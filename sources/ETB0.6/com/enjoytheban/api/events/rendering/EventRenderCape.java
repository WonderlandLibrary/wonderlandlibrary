package com.enjoytheban.api.events.rendering;

import com.enjoytheban.api.Event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

/**
 * @author Rederpz
 */

public class EventRenderCape extends Event {

	private ResourceLocation capeLocation;
	private final EntityPlayer player;

	public EventRenderCape(ResourceLocation capeLocation, EntityPlayer player) {
		this.capeLocation = capeLocation;
		this.player = player;
	}

	public ResourceLocation getLocation() {
		return this.capeLocation;
	}

	public void setLocation(final ResourceLocation location) {
		this.capeLocation = location;
	}

	public EntityPlayer getPlayer() {
		return this.player;
	}
}
