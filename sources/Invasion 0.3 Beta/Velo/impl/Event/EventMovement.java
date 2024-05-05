package Velo.impl.Event;

import Velo.api.Event.Event;

public class EventMovement extends Event<EventMovement>{
	
	public double moveStrafe, moveForward;
	
	
	   public double x;
	   public double y;
	   public double z;
	   
	public EventMovement( double x, double y, double z) {
	
		   this.x = x;
		      this.y = y;
		      this.z = z;
	}

	public double getMoveStrafe() {
		return moveStrafe;
	}

	public void setMoveStrafe(double moveStrafe) {
		this.moveStrafe = moveStrafe;
	}

	public double getMoveForward() {
		return moveForward;
	}

	public void setMoveForward(double moveForward) {
		this.moveForward = moveForward;
	}
	

    public double getX() {
        return this.x;
    }

    public void setX(final double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(final double y2) {
        this.y = y2;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(final double z) {
        this.z = z;
    }

    public void setXYZ(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
	
}
