package rosegoldclient.utils;

public class Rotation {
    private float yaw;
    private float pitch;

    public Rotation(final float yaw, final float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }

    public void addYaw(final float yaw) {
        this.yaw += yaw;
    }

    public void addPitch(final float pitch) {
        this.pitch += pitch;
    }

    public float getValue() {
        return Math.abs(this.yaw) + Math.abs(this.pitch);
    }

    @Override
    public String toString() {
        return "Rotation{yaw=" + this.yaw + ", pitch=" + this.pitch + '}';
    }
}
