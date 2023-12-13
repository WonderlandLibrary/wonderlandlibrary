package de.tired.util.combat.rotation;

public class Rotation {

    private float yaw, pitch;

    public Rotation(final float yaw, final float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }


    @Override
    public String toString() {
        return "Rotation [yaw=" + yaw + ", pitch=" + pitch + "]";
    }

}