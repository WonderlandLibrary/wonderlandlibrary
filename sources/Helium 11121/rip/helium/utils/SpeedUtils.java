package rip.helium.utils;


import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import rip.helium.event.minecraft.PlayerMoveEvent;

public class SpeedUtils {
    private static final Minecraft mc;

    static {
        mc = Minecraft.getMinecraft();
    }

    public static boolean isMoving() {
        return SpeedUtils.mc.thePlayer.moveForward != 0.0f || SpeedUtils.mc.thePlayer.moveStrafing != 0.0f;
    }

    public static void setMoveSpeed(final PlayerMoveEvent event, final double speed) {
        double forward = MovementInput.moveForward;
        double strafe = MovementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw; //oh nigger
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            event.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f))
                    + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
            event.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f))
                    - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
        }
    }

    public static double getBaseMoveSpeed() {
        double speed = 0.2873;
        if (SpeedUtils.mc.thePlayer.isPotionActive(1)) {
            final double amp = SpeedUtils.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
            speed *= 1.0 + 0.125 * (amp + 1.0);
        }
        return speed;
    }

    public static double getSpeedAmp() {
        double speed = 0.0;
        if (SpeedUtils.mc.thePlayer.isPotionActive(1)) {
            final double amp = SpeedUtils.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
            speed = amp + 1.0;
        }
        return speed;
    }

    public static void setPlayerSpeed(final PlayerMoveEvent e, final double speed) {
        if (isMoving()) {
            final double d = 0.0;
            e.setX(-(MathHelper.sin(getDir(SpeedUtils.mc.thePlayer)) * (speed + d)));
            e.setZ(MathHelper.cos(getDir(SpeedUtils.mc.thePlayer)) * (speed + d));
        } else {
            e.setX(0.0);
            e.setZ(0.0);
        }
    }

    public static Float getDir(final EntityPlayer player) {
        double f = player.moveForward;
        final double s = player.moveStrafing;
        double y = player.rotationYaw;
        final double st = 45.0;
        if (s != 0.0 && f == 0.0) {
            y += 360.0;
            if (s > 0.0) {
                y -= 89.0;
            } else if (s < 0.0) {
                y += 89.0;
            }
            f = 0.0;
        } else if (f > 0.0) {
            if (s > 0.0) {
                y -= st;
            }
            if (s < 0.0) {
                y += st;
            }
        } else {
            if (s > 0.0) {
                y += st;
            }
            if (s < 0.0) {
                y -= st;
            }
        }
        if (f < 0.0) {
            y -= 180.0;
        }
        y *= 0.01746532879769802;
        return (float) y;
    }

    public static double getPlayerSpeed() {
        return Math.hypot(SpeedUtils.mc.thePlayer.motionX, SpeedUtils.mc.thePlayer.motionZ);
    }

    public static void setPlayerSpeed(final double speed) {
        if (isMoving()) {
            SpeedUtils.mc.thePlayer.motionX = -(MathHelper.sin(getDir(SpeedUtils.mc.thePlayer)) * speed);
            SpeedUtils.mc.thePlayer.motionZ = MathHelper.cos(getDir(SpeedUtils.mc.thePlayer)) * speed;
        } else {
            SpeedUtils.mc.thePlayer.motionX = 0.0;
            SpeedUtils.mc.thePlayer.motionZ = 0.0;
        }
    }

    public static void setPlayerSpeedMax(final double a, final double b) {
        setPlayerSpeed(Math.max(a, b));
    }

    public static void setPlayerSpeedMax(final PlayerMoveEvent event, final double a, final double b) {
        setPlayerSpeed(event, Math.max(a, b));
    }

    public double getNCPBoost(final PlayerMoveEvent event, final int stage) {
        double value = getBaseMoveSpeed() + 0.028 * getSpeedAmp() + getSpeedAmp() / 15.0;
        final double firstvalue = 0.4145 + getSpeedAmp() / 12.5;
        final double decr = stage / 500.0 * 2.0;
        if (stage == 0) {
            value = 0.64 + (getSpeedAmp() + 0.028 * getSpeedAmp()) * 0.134;
        } else if (stage == 1) {
            value = firstvalue;
        } else if (stage >= 2) {
            value = firstvalue - decr;
        }
        value = Math.max(value, getBaseMoveSpeed());
        return value;
    }
}