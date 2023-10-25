/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.event.events;

public final class RotationMoveEvent {
    private final Type type;
    private float yaw;

    public RotationMoveEvent(Type type, float yaw) {
        this.type = type;
        this.yaw = yaw;
    }

    public final void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public final float getYaw() {
        return this.yaw;
    }

    public final Type getType() {
        return this.type;
    }

    public static enum Type {
        MOTION_UPDATE,
        JUMP;

    }
}

