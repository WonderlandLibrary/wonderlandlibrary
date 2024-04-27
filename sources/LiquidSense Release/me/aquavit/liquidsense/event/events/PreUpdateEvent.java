package me.aquavit.liquidsense.event.events;

import me.aquavit.liquidsense.event.Event;

public class PreUpdateEvent extends Event {

    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private boolean isSneaking;
    private boolean isOnGround;
    private static float YAW;
    private static float PITCH;
    private static float prevYAW;
    private static float prevPITCH;
    private static boolean SNEAKING;
    private static boolean OnGround;

    public float getYaw(float yaw) {
        return yaw;
    }

    public float getPitch(float pitch) {
        return pitch;
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

    public float getYaw() {
        return this.yaw;
    }

    public boolean isSneaking() {
        return this.isSneaking;
    }

    public boolean isOnGround() {
        return this.isOnGround;
    }

    public void setOnGround(boolean isOnGround) {
        this.isOnGround = isOnGround;
    }

    public PreUpdateEvent(double x, double y, double z, float yaw, float pitch, boolean isSneaking, boolean isOnGround) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.isSneaking = isSneaking;
        this.isOnGround = isOnGround;
    }

    public void fire() {
        prevYAW = YAW;
        prevPITCH = PITCH;
        YAW = this.yaw;
        PITCH = this.pitch;
        SNEAKING = this.isSneaking;
        OnGround = this.isOnGround;
    }

    public static final class Companion {
        public static float getYAW() {
            return PreUpdateEvent.YAW;
        }

        public static void setYAW(float YAW) {
            PreUpdateEvent.YAW = YAW;
        }

        public static float getPITCH() {
            return PreUpdateEvent.PITCH;
        }

        public static void setPITCH(float PITCH) {
            PreUpdateEvent.PITCH = PITCH;
        }

        public static float getPrevYAW() {
            return PreUpdateEvent.prevYAW;
        }

        public static void setPrevYAW(float prevYAW) {
            PreUpdateEvent.prevYAW = prevYAW;
        }

        public static float getPrevPITCH() {
            return PreUpdateEvent.prevPITCH;
        }

        public static void setPrevPITCH(float prevPITCH) {
            PreUpdateEvent.prevPITCH = prevPITCH;
        }

        public static boolean getSNEAKING() {
            return PreUpdateEvent.SNEAKING;
        }

        public static void setSNEAKING(boolean SNEAKING) {
            PreUpdateEvent.SNEAKING = SNEAKING;
        }

        public static boolean getOnGround() {
            return PreUpdateEvent.OnGround;
        }

        public static void setOnGround(boolean OnGround) {
            PreUpdateEvent.OnGround = OnGround;
        }
    }

}
