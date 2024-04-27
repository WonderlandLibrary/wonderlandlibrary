package net.ccbluex.liquidbounce.event;

public class SommtheEvent extends Event  {

    private float yaw;
    private float pitch;
    private double x, y, z;
    private boolean onground;
    private boolean sneaking;
    public static float YAW, PITCH, PREVYAW, PREVPITCH;
    public static boolean SNEAKING;

    public  SommtheEvent(double x, double y, double z, float yaw, float pitch, boolean sneaking, boolean ground) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.y = y;
        this.x = x;
        this.z = z;
        this.onground = ground;
        this.sneaking = sneaking;
    }

    public void fire() {
        PREVYAW = YAW;
        PREVPITCH = PITCH;
        YAW = this.yaw;
        PITCH = this.pitch;
        SNEAKING = this.sneaking;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public boolean isSneaking(){
        return this.sneaking;
    }

    public boolean isOnground() {
        return this.onground;
    }

}
