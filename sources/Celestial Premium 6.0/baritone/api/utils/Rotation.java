/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.utils;

public class Rotation {
    private float yaw;
    private float pitch;

    public Rotation(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public Rotation add(Rotation other) {
        return new Rotation(this.yaw + other.yaw, this.pitch + other.pitch);
    }

    public Rotation subtract(Rotation other) {
        return new Rotation(this.yaw - other.yaw, this.pitch - other.pitch);
    }

    public Rotation clamp() {
        return new Rotation(this.yaw, Rotation.clampPitch(this.pitch));
    }

    public Rotation normalize() {
        return new Rotation(Rotation.normalizeYaw(this.yaw), this.pitch);
    }

    public Rotation normalizeAndClamp() {
        return new Rotation(Rotation.normalizeYaw(this.yaw), Rotation.clampPitch(this.pitch));
    }

    public boolean isReallyCloseTo(Rotation other) {
        return this.yawIsReallyClose(other) && (double)Math.abs(this.pitch - other.pitch) < 0.01;
    }

    public boolean yawIsReallyClose(Rotation other) {
        float yawDiff = Math.abs(Rotation.normalizeYaw(this.yaw) - Rotation.normalizeYaw(other.yaw));
        return (double)yawDiff < 0.01 || (double)yawDiff > 359.99;
    }

    public static float clampPitch(float pitch) {
        return Math.max(-90.0f, Math.min(90.0f, pitch));
    }

    public static float normalizeYaw(float yaw) {
        float newYaw = yaw % 360.0f;
        if (newYaw < -180.0f) {
            newYaw += 360.0f;
        }
        if (newYaw > 180.0f) {
            newYaw -= 360.0f;
        }
        return newYaw;
    }

    public String toString() {
        return "Yaw: " + this.yaw + ", Pitch: " + this.pitch;
    }
}

