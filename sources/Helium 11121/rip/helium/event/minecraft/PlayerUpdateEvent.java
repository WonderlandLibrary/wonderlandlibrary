package rip.helium.event.minecraft;


import me.hippo.systems.lwjeb.event.Cancelable;

/**
 * Class made by Anthony A.
 * -------------------------------
 * 3/13/2019 / 8:42 PM
 * ethereal.rip
 * <p>
 * Things added by niada:
 * Ground state
 * yaw
 * pitch
 * player x,y,z position
 * last yaw and pitch
 **/
public class PlayerUpdateEvent extends Cancelable {
    public static boolean onGround;
    public static float lastpitch;
    public static float lastyaw;
    public static double ypos;
    public static double xpos;
    public static double zpos;
    public static float pitch;
    public static float yaw;
    private static boolean isPre;

    public PlayerUpdateEvent(boolean ground, double xpos, double ypos, double zpos, float yaw, float pitch, float lastyaw, float lastpitch) {
        PlayerUpdateEvent.ypos = ypos;
        PlayerUpdateEvent.xpos = xpos;
        PlayerUpdateEvent.zpos = zpos;
        PlayerUpdateEvent.yaw = yaw;
        PlayerUpdateEvent.pitch = pitch;
        onGround = ground;
        isPre = true;
    }

    public PlayerUpdateEvent() {
        isPre = false;
    }

    public void setOnGround(boolean ground) {
        onGround = ground;
    }

    public boolean isGrounded() {
        return onGround;
    }

    public double getPosX() {
        return xpos;
    }

    public void setPosX(double x) {
        xpos = x;
    }

    public double getPosY() {
        return ypos;
    }

    public void setPosY(double y) {
        ypos = y;
    }

    public double getPosZ() {
        return zpos;
    }

    public void setPosZ(double z) {
        zpos = z;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float theyaw) {
        yaw = theyaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float thepitch) {

        pitch = thepitch;
        if (thepitch >= 90) {
            pitch = 90;
        } else if (thepitch <= -90) {
            pitch = -90;
        }
    }

    public float getLastYaw() {
        return lastyaw;
    }

    public void setLastYaw(float attackedyaw) {
        lastyaw = attackedyaw;
    }

    public float getLastPitch() {
        return lastpitch;
    }

    public void setLastPitch(float attackedpitch) {
        lastpitch = attackedpitch;
    }

    public boolean isPre() {
        return isPre;
    }
}
