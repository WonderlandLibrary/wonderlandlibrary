package dev.tenacity.event.impl.player;

import dev.tenacity.event.Event;
import net.minecraft.entity.player.EntityPlayer;

public final class MotionEvent extends Event {
    private boolean canceled = false;

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public boolean isCanceled() {
        return canceled;
    }
    private double x, y, z;
    private float yaw, pitch;
    private boolean onGround;

    public MotionEvent(final double x, final double y, final double z, final float yaw, final float pitch, final boolean onGround) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setX(final double x) {
        this.x = x;
    }

    public void setY(final double y) {
        this.y = y;
    }

    public void setZ(final double z) {
        this.z = z;
    }

    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }

    public void setOnGround(final boolean onGround) {
        this.onGround = onGround;
    }
    private EntityPlayer player;

    public MotionEvent(EntityPlayer player, double x, double y, double z, float yaw, float pitch, boolean onGround) {
        this.player = player;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }
    public EntityPlayer getPlayer() {
        return this.player;
    }
}

