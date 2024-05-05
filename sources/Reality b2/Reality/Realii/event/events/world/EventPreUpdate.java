
package Reality.Realii.event.events.world;

import Reality.Realii.event.Event;
import Reality.Realii.utils.cheats.player.Helper;
import Reality.Realii.utils.math.RotationUtil;

public class EventPreUpdate
extends Event {
    public static double x;
    public static double z;
	private float yaw;
    private float pitch;
    public static double y;
    private boolean ground;

    public EventPreUpdate(float yaw, float pitch, double y, boolean ground) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.y = y;
        this.ground = ground;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
		Helper.mc.thePlayer.renderYawOffset = yaw;
		Helper.mc.thePlayer.rotationYawHead = yaw;
        this.yaw = yaw;
    }
    
    public void setYawhead(float yaw) {
		Helper.mc.thePlayer.rotationYawHead = yaw;
        this.yaw = yaw;
    }
    
    
    
    
    public void setYawhead2(float yaw) {
    	if(Helper.mc.thePlayer.rotationPitchHead > 90) {
    		Helper.mc.thePlayer.renderYawOffset = yaw;
       	   
       	   
          }
		Helper.mc.thePlayer.rotationYawHead = yaw;
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
    	Helper.mc.thePlayer.rotationPitchHead = pitch;
        this.pitch = pitch;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public boolean isOnground() {
        return this.ground;
    }

    public void setOnground(boolean ground) {
        this.ground = ground;
    }
    
    public void setXspeed(double x) {
    	EventPreUpdate.x = x;
    }



    public void setYsped(double y) {
    	EventPreUpdate.y = y;
    }


    public void setZ(double z) {
    	EventPreUpdate.z = z;
    }

    public void setX(double x) {
    	EventPreUpdate.x = x;
    }
    
    



}

