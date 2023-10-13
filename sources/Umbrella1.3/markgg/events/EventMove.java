/*
 * Decompiled with CFR 0.150.
 */
package markgg.events;

import markgg.events.Event;
import net.minecraft.client.Minecraft;

public class EventMove
extends Event {
    public double x;
    public double y;
    public double z;

    public EventMove(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setSpeed(double moveSpeed) {
        float forward = Minecraft.getMinecraft().thePlayer.movementInput.moveForward;
        float strafe = Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe;
        float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        if ((double)forward == 0.0 && (double)strafe == 0.0) {
            Minecraft.getMinecraft().thePlayer.motionX = 0.0;
            Minecraft.getMinecraft().thePlayer.motionZ = 0.0;
        } else {
            if ((double)forward != 0.0) {
                if (strafe > 0.0f) {
                    yaw += (float)((double)forward > 0.0 ? -45 : 45);
                } else if (strafe < 0.0f) {
                    yaw += (float)((double)forward > 0.0 ? 45 : -45);
                }
                strafe = 0.0f;
                if (forward > 0.0f) {
                    forward = 1.0f;
                } else if (forward < 0.0f) {
                    forward = -1.0f;
                }
            }
            double xDist = (double)forward * moveSpeed * Math.cos(Math.toRadians(yaw + 90.0f)) + (double)strafe * moveSpeed * Math.sin(Math.toRadians(yaw + 90.0f));
            double zDist = (double)forward * moveSpeed * Math.sin(Math.toRadians(yaw + 90.0f)) - (double)strafe * moveSpeed * Math.cos(Math.toRadians(yaw + 90.0f));
            Minecraft.getMinecraft().thePlayer.motionX = xDist;
            Minecraft.getMinecraft().thePlayer.motionZ = zDist;
            this.setX(xDist);
            this.setZ(zDist);
        }
    }

    public void setSpeed(double moveSpeed, float yaw) {
        float forward = Minecraft.getMinecraft().thePlayer.movementInput.moveForward;
        float strafe = Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe;
        if ((double)forward == 0.0 && (double)strafe == 0.0) {
            Minecraft.getMinecraft().thePlayer.motionX = 0.0;
            Minecraft.getMinecraft().thePlayer.motionZ = 0.0;
        } else {
            if ((double)forward != 0.0) {
                if (strafe > 0.0f) {
                    yaw += (float)((double)forward > 0.0 ? -45 : 45);
                } else if (strafe < 0.0f) {
                    yaw += (float)((double)forward > 0.0 ? 45 : -45);
                }
                strafe = 0.0f;
                if (forward > 0.0f) {
                    forward = 1.0f;
                } else if (forward < 0.0f) {
                    forward = -1.0f;
                }
            }
            double xDist = (double)forward * moveSpeed * Math.cos(Math.toRadians(yaw + 90.0f)) + (double)strafe * moveSpeed * Math.sin(Math.toRadians(yaw + 90.0f));
            double zDist = (double)forward * moveSpeed * Math.sin(Math.toRadians(yaw + 90.0f)) - (double)strafe * moveSpeed * Math.cos(Math.toRadians(yaw + 90.0f));
            Minecraft.getMinecraft().thePlayer.motionX = xDist;
            Minecraft.getMinecraft().thePlayer.motionZ = zDist;
            this.setX(xDist);
            this.setZ(zDist);
        }
    }
}

