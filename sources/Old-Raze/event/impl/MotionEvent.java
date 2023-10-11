package markgg.event.impl;

import markgg.event.Event;
import net.minecraft.client.Minecraft;

public class MotionEvent extends Event {

    private double x, y, z;
    private float yaw, prevYaw, pitch;

    private boolean onGround, sneaking;

    private Type type;

    public MotionEvent(Type type, double x, double y, double z, float yaw, float prevYaw, float pitch, boolean onGround, boolean sneaking) {
        this.setType(type);
        this.x = x;
        this.y = y;
        this.z = z;
        this.prevYaw = prevYaw;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.sneaking = sneaking;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public void setSneaking(boolean sneaking) {
        this.sneaking = sneaking;
    }

    public boolean isSneaking() {
        return sneaking;
    }

    public double getX() {
        return x;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public double getY() {
        return y;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public double getZ() {
        return z;
    }
    
    public void setZ(double z) {
        this.z = z;
    }
    
    public float getYaw() {
        return yaw;
    }

    public float getPrevYaw() {
        return prevYaw;
    }

    public void setPrevYaw(float prevYaw) {
        this.prevYaw = prevYaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
    
    public float getPitch() {
        return pitch;
    }
    
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public enum Type{
        UPDATE,
        PRE,
        POST
    }
}
