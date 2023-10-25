package tech.dort.dortware.impl.events;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import tech.dort.dortware.api.event.Event;

public class PlayerMovementUpdateEvent extends Event {

    private final State state;
    private float strafe;
    private float forward;
    private float friction;
    private float yaw;
    private float jumpHeight;
    private boolean silent;
    private float pitch;
    private float multi;

    public PlayerMovementUpdateEvent(float strafe, float forward, float friction, float yaw, float pitch, State state) {
        this.strafe = strafe;
        this.forward = forward;
        this.friction = friction;
        this.yaw = yaw;
        this.pitch = pitch;
        this.state = state;
    }

    public PlayerMovementUpdateEvent(float rotationYaw, float forward, float height) {
        this(0, forward, 0, rotationYaw, 0, State.JUMP);
        this.jumpHeight = height;
    }

    public float getStrafe() {
        return strafe;
    }

    public void setStrafe(float strafe) {
        this.strafe = strafe;
    }

    public float getForward() {
        return forward;
    }

    public void setForward(float forward) {
        this.forward = forward;
    }

    public float getFriction() {
        return friction;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public State getState() {
        return state;
    }

    public float getJumpHeight() {
        return jumpHeight;
    }

    public void setJumpHeight(float jumpHeight) {
        this.jumpHeight = jumpHeight;
    }

    public boolean isSilent() {
        return silent;
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setHorizontalBoost(float multi) {
        this.multi = multi;
    }

    public float getHorizontalBoost() {
        return multi;
    }

    public void doJump(Entity entity) {
        entity.motionY = jumpHeight;

        if (entity.isSprinting()) {
            float var1 = entity.rotationYaw * 0.017453292F;
            entity.motionX -= MathHelper.sin(var1) * multi;
            entity.motionZ += MathHelper.cos(var1) * multi;
        }
    }

    public enum State {
        JUMP, STRAFE
    }

}
