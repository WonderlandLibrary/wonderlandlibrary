package epsilon.events.listeners;

import epsilon.events.Event;
import net.minecraft.entity.Entity;

public class EventRiding extends Event<EventRiding>{

	public float moveStrafing, moveForward, rotationYaw, rotationPitch;
	public boolean jump, sneak, onGround;
	public Entity entity;
	
	public EventRiding(float moveStrafing, float moveForward, boolean jump, boolean sneak, float rotationYaw,
			float rotationPitch, boolean onGround, Entity entity) {
		
		this.moveStrafing = moveStrafing;
		this.moveForward = moveForward;
		
		this.entity = entity;
		
		this.jump = jump;
		this.sneak = sneak;
		
		this.rotationYaw = rotationYaw;
		this.rotationPitch = rotationPitch;
		
		this.onGround = onGround;
		
		
	}

	public Entity getEntity() {
		return entity;
	}
	
	public float getMoveStrafing() {
		return moveStrafing;
	}

	public void setMoveStrafing(float moveStrafing) {
		this.moveStrafing = moveStrafing;
	}

	public float getMoveForward() {
		return moveForward;
	}

	public void setMoveForward(float moveForward) {
		this.moveForward = moveForward;
	}

	public float getRotationYaw() {
		return rotationYaw;
	}

	public void setRotationYaw(float rotationYaw) {
		this.rotationYaw = rotationYaw;
	}

	public float getRotationPitch() {
		return rotationPitch;
	}

	public void setRotationPitch(float rotationPitch) {
		this.rotationPitch = rotationPitch;
	}

	public boolean isJump() {
		return jump;
	}

	public void setJump(boolean jump) {
		this.jump = jump;
	}

	public boolean isSneak() {
		return sneak;
	}

	public void setSneak(boolean sneak) {
		this.sneak = sneak;
	}

	public boolean isOnGround() {
		return onGround;
	}

	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
	}

}
