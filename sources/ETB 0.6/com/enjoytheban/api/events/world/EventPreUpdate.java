package com.enjoytheban.api.events.world;

import com.enjoytheban.api.Event;
import com.enjoytheban.api.Type;

import net.minecraft.entity.Entity;

/**
 * Standard Minecraft EventPreUpdate
 * @author Purity
 * @called EntityPlayerSP func_175161_p, hooks in the packets
 */

public class EventPreUpdate extends Event {

	private float yaw,pitch;
	public double y;
	private boolean ground;
	
	public EventPreUpdate(float yaw, float pitch, double y, boolean ground){
		this.yaw = yaw;
		this.pitch = pitch;
		this.y = y;
		this.ground = ground;
	}
	
	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public boolean isOnground() {
		return ground;
	}

	public void setOnground(boolean ground) {
		this.ground = ground;
	}
}