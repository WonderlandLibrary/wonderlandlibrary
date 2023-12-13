package me.aidanmees.trivia.client.events;

import me.aidanmees.trivia.client.tools.Angles;
import me.aidanmees.trivia.client.tools.Location;

public class TickEvent extends Event {
	
	public float yaw;
	public float pitch;
	public double x;
	public double y;
	public double z;
	private static Location location;
	public boolean onGround;
	public boolean autopot;
	
	public TickEvent(Location location, float yaw, float pitch, double x, double y, double z, boolean onGround) {
		this.yaw = yaw;
		this.location = location;
		this.pitch = pitch;
		this.y = y;
		this.x = x;
		this.z = z;
		this.onGround = onGround;
	}

	public static Location getLocation() {
		return location;
	}

	public float getYaw() {
		
		return yaw;
	}
    public float getPitch() {
		
		return pitch;
	}

}
