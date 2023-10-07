package viaversion.viarewind.protocol.protocol1_7_6_10to1_8.storage;

import viaversion.viaversion.api.data.StoredObject;
import viaversion.viaversion.api.data.UserConnection;

import java.util.Objects;

public class PlayerAbilities extends StoredObject {

	private boolean sprinting, allowFly, flying, invincible, creative;
	private float flySpeed, walkSpeed;

	public PlayerAbilities(UserConnection user) {
		super(user);
	}
	public PlayerAbilities(UserConnection user, boolean sprinting, boolean allowFly, boolean flying, boolean invincible, boolean creative,
						   float flySpeed, float walkSpeed) {
		super(user);
		this.sprinting = sprinting;
		this.allowFly = allowFly;
		this.flying = flying;
		this.invincible = invincible;
		this.creative = creative;
		this.flySpeed = flySpeed;
		this.walkSpeed = walkSpeed;
	}
	public byte getFlags() {
		byte flags = 0;
		if(invincible) flags |= 8;
		if(allowFly) flags |= 4;
		if(flying) flags |= 2;
		if(creative) flags |= 1;
		return flags;
	}

	public boolean isSprinting() { return sprinting; }
	public void setSprinting(boolean sprinting) {
		this.sprinting = sprinting;
	}
	public boolean isAllowFly() { return allowFly; }
	public void setAllowFly(boolean allowFly) {
		this.allowFly = allowFly;
	}
	public boolean isFlying() { return flying; }
	public void setFlying(boolean flying) {
		this.flying = flying;
	}
	public boolean isInvincible() { return invincible; }
	public void setInvincible(boolean invincible) {
		this.invincible = invincible;
	}
	public boolean isCreative() { return creative; }
	public void setCreative(boolean creative) {
		this.creative = creative;
	}
	public float getFlySpeed() { return flySpeed; }
	public void setFlySpeed(float flySpeed) {
		this.flySpeed = flySpeed;
	}
	public float getWalkSpeed() { return walkSpeed; }
	public void setWalkSpeed(float walkSpeed) {
		this.walkSpeed = walkSpeed;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(!(o instanceof PlayerAbilities)) return false;

		PlayerAbilities other = (PlayerAbilities) o;
		return sprinting == other.sprinting && allowFly == other.allowFly && flying == other.flying && invincible == other.invincible && creative == other.creative && Float
				.compare(other.flySpeed, flySpeed) == 0 && Float.compare(other.walkSpeed, walkSpeed) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(sprinting, allowFly, flying, invincible, creative, flySpeed, walkSpeed);
	}

	@Override
	public String toString() {
		return "PlayerAbilities{" + "sprinting=" + sprinting + ", allowFly=" + allowFly + ", flying=" + flying + ", invincible=" + invincible + ", creative=" + creative + ", flySpeed=" + flySpeed + ", walkSpeed=" + walkSpeed + '}';
	}
}
