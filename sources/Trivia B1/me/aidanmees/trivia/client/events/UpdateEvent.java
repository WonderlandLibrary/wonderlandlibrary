package me.aidanmees.trivia.client.events;

import me.aidanmees.trivia.client.tools.Location;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class UpdateEvent extends Event {
	
	public float yaw;
	public float pitch;
	public double x;
	public double y;
	public double z;
	private static Location location;
	public boolean onGround;
	public boolean autopot;
	
	public UpdateEvent(float yaw, float pitch, double x, double y, double z, boolean onGround) {
		this.yaw = yaw;
		
		this.pitch = pitch;
		this.y = y;
		this.x = x;
		this.z = z;
		this.onGround = onGround;
	}

	

	public float getYaw() {
		
		return yaw;
	}
    public float getPitch() {
		
		return pitch;
	}


    public BlockPos getPosition()
    {
    	
      return location.toBlockPos();
    }



   
  }


