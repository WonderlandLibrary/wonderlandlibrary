package today.flux.addon.api.packet.client;

import com.soterdev.SoterObfuscator;
import net.minecraft.network.play.client.C03PacketPlayer;
import today.flux.addon.api.packet.AddonPacket;

public class PacketPlayer extends AddonPacket {
    public PacketPlayer(C03PacketPlayer packet) {
    	super(packet);
	}
    
    public PacketPlayer(boolean onGround) {
    	super(null);
    	this.nativePacket = new C03PacketPlayer(onGround);
    }

	
    public double getX() {
		return ((C03PacketPlayer) nativePacket).x;
	}

	
	public void setX(double x) {
		((C03PacketPlayer) nativePacket).x = x;
	}

	
	public double getY() {
		return ((C03PacketPlayer) nativePacket).y;
	}

	
	public void setY(double y) {
		((C03PacketPlayer) nativePacket).y = y;
	}

	
	public double getZ() {
		return ((C03PacketPlayer) nativePacket).z;
	}

	
	public void setZ(double z) {
		((C03PacketPlayer) nativePacket).z = z;
	}

	
	public float getYaw() {
		return ((C03PacketPlayer) nativePacket).getYaw();
	}

	
	public void setYaw(float yaw) {
		((C03PacketPlayer) nativePacket).yaw = yaw;
	}

	
	public float getPitch() {
		return ((C03PacketPlayer) nativePacket).getPitch();
	}

	
	public void setPitch(float pitch) {
		((C03PacketPlayer) nativePacket).pitch = pitch;
	}

	
	public boolean isOnGround() {
		return ((C03PacketPlayer) nativePacket).onGround;
	}

	
	public void setOnGround(boolean onGround) {
		((C03PacketPlayer) nativePacket).onGround = onGround;
	}

	
	public boolean isMoving() {
		return ((C03PacketPlayer) nativePacket).isMoving();
	}

	
	public void setMoving(boolean moving) {
		((C03PacketPlayer) nativePacket).moving = moving;
	}

	
	public boolean isRotating() {
		return ((C03PacketPlayer) nativePacket).getRotating();
	}

	
	public void setRotating(boolean rotating) {
		((C03PacketPlayer) nativePacket).rotating = rotating;
	}
    
	public static class Position extends PacketPlayer {
		
		public Position(C03PacketPlayer.C04PacketPlayerPosition packet) {
			super(packet);
		}
		
		public Position(double x, double y, double z, boolean onGround) {
			super(null);
			this.nativePacket = new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, onGround);
	    }
		
	}
	
	public static class Rotation extends PacketPlayer {
		
		public Rotation(C03PacketPlayer.C05PacketPlayerLook packet) {
			super(packet);
		}
		
		public Rotation(float yaw, float pitch, boolean onGround) {
			super(null);
			this.nativePacket = new C03PacketPlayer.C05PacketPlayerLook(yaw, pitch, onGround);
	    }
		
	}
	
	public static class PositionRotation extends PacketPlayer {
		
		public PositionRotation(C03PacketPlayer.C06PacketPlayerPosLook packet) {
			super(packet);
		}
		
		public PositionRotation(double x, double y, double z, float yaw, float pitch, boolean onGround) {
			super(null);
			this.nativePacket = new C03PacketPlayer.C06PacketPlayerPosLook(x, y, z, yaw, pitch, onGround);
	    }
		
	}
	
}
