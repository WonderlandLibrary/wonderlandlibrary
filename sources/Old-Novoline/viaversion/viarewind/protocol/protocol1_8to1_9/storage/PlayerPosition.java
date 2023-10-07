package viaversion.viarewind.protocol.protocol1_8to1_9.storage;

import java.util.Objects;
import viaversion.viaversion.api.data.StoredObject;
import viaversion.viaversion.api.data.UserConnection;

public class PlayerPosition extends StoredObject {

	private double posX, posY, posZ;
	private float yaw, pitch;
	private boolean onGround;
	private int confirmId = -1;

	public PlayerPosition(UserConnection user, double posX, double posY, double posZ, float yaw, float pitch, boolean onGround,
						  int confirmId) {
		super(user);
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.yaw = yaw;
		this.pitch = pitch;
		this.onGround = onGround;
		this.confirmId = confirmId;
	}

	public PlayerPosition(UserConnection user) {
		super(user);
	}

	public void setPos(double x, double y, double z) {
		this.posX = x;
		this.posY = y;
		this.posZ = z;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw % 360f;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch % 360f;
	}

	public double getPosX() { return posX; }
	public void setPosX(double posX) {
		this.posX = posX;
	}
	public double getPosY() { return posY; }
	public void setPosY(double posY) {
		this.posY = posY;
	}
	public double getPosZ() { return posZ; }
	public void setPosZ(double posZ) {
		this.posZ = posZ;
	}
	public float getYaw() { return yaw; }
	public float getPitch() { return pitch; }
	public boolean isOnGround() { return onGround; }
	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
	}
	public int getConfirmId() { return confirmId; }
	public void setConfirmId(int confirmId) {
		this.confirmId = confirmId;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(!(o instanceof PlayerPosition)) return false;

		PlayerPosition other = (PlayerPosition) o;
		return Double.compare(other.posX, posX) == 0 && Double.compare(other.posY, posY) == 0 && Double
				.compare(other.posZ, posZ) == 0 && Float.compare(other.yaw, yaw) == 0 && Float
				.compare(other.pitch, pitch) == 0 && onGround == other.onGround && confirmId == other.confirmId;
	}
	@Override
	public int hashCode() {
		return Objects.hash(posX, posY, posZ, yaw, pitch, onGround, confirmId);
	}
	@Override
	public String toString() {
		return "PlayerPosition{" + "posX=" + posX + ", posY=" + posY + ", posZ=" + posZ + ", yaw=" + yaw + ", pitch=" + pitch + ", onGround=" + onGround + ", confirmId=" + confirmId + '}';
	}
}
