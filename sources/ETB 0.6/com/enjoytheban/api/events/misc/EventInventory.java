package com.enjoytheban.api.events.misc;

import com.enjoytheban.api.Event;

import net.minecraft.entity.player.EntityPlayer;

public class EventInventory extends Event {

	private final EntityPlayer player;

	public EventInventory(EntityPlayer player) {
		this.player = player;
	}

	public EntityPlayer getPlayer() {
		return this.player;
	}
}
