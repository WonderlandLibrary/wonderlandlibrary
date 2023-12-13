package summer.cheat.eventsystem.events.player;

import summer.cheat.eventsystem.Event;
import summer.cheat.eventsystem.Stage;

public class EventUpdate extends Event {

    private static double x;
    private static double y;
    private static double z;
    public boolean onGround;
    private static float pitch;
    private static float yaw;
    private float lastPitch;
    private float lastYaw;
    private Stage stage;
    private static double lastX;
    private static double lastY;
    private static double lastZ;

    public EventUpdate(double x, double y, double z, boolean onGround, float yaw, float pitch, float lastYaw,
                       float lastPitch, Stage stage) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.onGround = onGround;
        this.pitch = pitch;
        this.yaw = yaw;
        this.lastPitch = lastPitch;
        this.lastYaw = lastYaw;
        this.stage = stage;
    }

    public boolean isPre() {
        if (stage.equals(Stage.PRE)) {
            return true;
        } else {
            return false;
        }
    }

    public static double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public static double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public static double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public static float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public static float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getLastPitch() {
        return this.lastPitch;
    }

    public void setLastPitch(float lastPitch) {
        this.lastPitch = lastPitch;
    }

    public float getLastYaw() {
        return this.lastYaw;
    }

    public void setLastYaw(float lastYaw) {
        this.lastYaw = lastYaw;
    }

    public static void setLastX(double lastX2) {
        lastX = lastX2;
    }

    public static double getLastX() {
        return lastX;
    }

    public static double getLastY() {
        return lastY;
    }

    public static void setLastY(double lastY2) {
        lastY = lastY2;
    }

    public static double getLastZ() {
        return lastZ;
    }

    public static void setLastZ(double lastZ2) {
        lastZ = lastZ2;
    }

    public boolean isPost() {
        return !this.isPre();
    }

}
