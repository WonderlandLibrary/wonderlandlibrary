/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.events;

import cow.milkgod.cheese.events.PlayerUpdateEvent;

public class EventPreMotionUpdates
extends PlayerUpdateEvent {
    public double x;
    public double y;
    public double z;
    private double oldX;
    private double oldY;
    private double oldZ;
    private float oldYaw;
    private float oldPitch;
    private boolean sprinting;
    private boolean wasSprinting;
    private boolean wasSneaking;

    public EventPreMotionUpdates(double x2, double y2, double z2, double oldX, double oldY, double oldZ, float yaw, float pitch, float oldYaw, float oldPitch, boolean sprinting, boolean wasSprinting, boolean sneaking, boolean wasSneaking, boolean onGround) {
        this.x = x2;
        this.y = y2;
        this.z = z2;
        this.oldX = oldX;
        this.oldY = oldY;
        this.oldZ = oldZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.oldYaw = oldYaw;
        this.oldPitch = oldPitch;
        this.sprinting = sprinting;
        this.wasSprinting = wasSprinting;
        this.sneaking = sneaking;
        this.wasSneaking = wasSneaking;
        this.onGround = onGround;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x2) {
        this.x = x2;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y2) {
        this.y = y2;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z2) {
        this.z = z2;
    }

    public double getOldX() {
        return this.oldX;
    }

    public double getOldY() {
        return this.oldY;
    }

    public double getOldZ() {
        return this.oldZ;
    }

    public double getOldYaw() {
        return this.oldYaw;
    }

    public double getOldPitch() {
        return this.oldPitch;
    }

    public boolean isSprinting() {
        return this.sprinting;
    }

    public void setSprinting(boolean sprinting) {
        this.sprinting = sprinting;
    }

    public boolean wasSprinting() {
        return this.wasSprinting;
    }

    public boolean wasSneaking() {
        return this.wasSneaking;
    }

    public boolean isMoving() {
        double x2 = this.x - this.oldX;
        double y2 = this.y - this.oldY;
        double z2 = this.z - this.oldZ;
        if (x2 * x2 + y2 * y2 + z2 * z2 > 9.0E-4) {
            return true;
        }
        return false;
    }

    public boolean isRotating() {
        double yaw = this.yaw - this.oldYaw;
        double pitch = this.pitch - this.oldPitch;
        if (yaw == 0.0 && pitch == 0.0) {
            return false;
        }
        return true;
    }
}

