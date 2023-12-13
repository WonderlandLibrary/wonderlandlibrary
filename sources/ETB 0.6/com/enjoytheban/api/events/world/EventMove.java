package com.enjoytheban.api.events.world;

import com.enjoytheban.api.Event;

/**
 * @desc A minecraft Event for movement related cheats
 * @author Purity
 * @called EntityPlayerSP MoveEntity
 */

public class EventMove extends Event {

	//Variables the hold X Y and Z
	public static double x,y,z;
	
	//Variables that hold the player Motion XYZ
	private double motionX, motionY, motionZ;

	//constructor
	public EventMove(double x, double y, double z) {
		//Set the variables
		this.x = x;
		this.y = y;
		this.z = z;
		motionX = x;
	    motionY = y;
	    motionZ = z;
	}
	
	//Getter and setter for X
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	//Getter and setter for Y
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	//Getter and setter for Z
	public double getZ() {
		return z;
	}
	
	public void setZ(double z) {
		this.z = z;
	}
}
