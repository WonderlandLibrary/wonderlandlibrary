package slavikcodd3r.rainbow.utils;

import org.apache.commons.lang3.RandomUtils;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import slavikcodd3r.rainbow.event.events.MoveEvent;
import net.minecraft.client.Minecraft;

public class SpeedUtils2
{
    private static final Minecraft mc;
    public static double[] tsStuff;
    
    private SpeedUtils2() {
    }
    
    public static double getMinemanBaseSpeed() {
        return 0.2871;
    }
    
    public static void setMinemanFastestSpeed(final MoveEvent event, final int tickDelay) {
        double baseSpeed = getMinemanBaseSpeed();
        if (SpeedUtils2.mc.thePlayer.ticksExisted % ((tickDelay <= 2) ? 3 : tickDelay) == 0) {
            baseSpeed += ((event == null) ? 0.485 : 0.42);
        }
        if (event == null) {
            setMoveSpeed(baseSpeed);
        }
        else {
        }
    }
    
    public static void setMoveSpeed(final double moveSpeed) {
        double forward = SpeedUtils2.mc.thePlayer.movementInput.moveForward;
        double strafe = SpeedUtils2.mc.thePlayer.movementInput.moveStrafe;
        if (SpeedUtils2.tsStuff != null) {}
        if (forward > 0.0) {
            forward = 1.0;
        }
        else if (forward < 0.0) {
            forward = -1.0;
        }
        if (strafe > 0.0) {
            strafe = 1.0;
        }
        else if (strafe < 0.0) {
            strafe = -1.0;
        }
        final double yaw = SpeedUtils2.mc.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            SpeedUtils2.mc.thePlayer.motionX = 0.0;
            SpeedUtils2.mc.thePlayer.motionZ = 0.0;
        }
        if (forward != 0.0 && strafe != 0.0) {
            forward *= Math.sin(0.6398355709958845);
            strafe *= Math.cos(0.6398355709958845);
        }
        final double sin = -Math.sin(Math.toRadians(yaw));
        final double cos = Math.cos(Math.toRadians(yaw));
        SpeedUtils2.mc.thePlayer.motionX = forward * moveSpeed * sin + strafe * moveSpeed * cos;
        SpeedUtils2.mc.thePlayer.motionZ = forward * moveSpeed * cos - strafe * moveSpeed * sin;
    }
    
    public static double getPlayerSpeedCurr() {
        return Math.hypot(SpeedUtils2.mc.thePlayer.motionX, SpeedUtils2.mc.thePlayer.motionZ);
    }
    
    public static double getPlayerSpeedEvent(final MoveEvent e) {
        return Math.hypot(e.getX(), e.getZ());
    }
    
    public static double[] getDirXZ(final double speed, final double mult) {
        double forward = SpeedUtils2.mc.thePlayer.movementInput.moveForward;
        double strafe = SpeedUtils2.mc.thePlayer.movementInput.moveStrafe;
        final double yaw = SpeedUtils2.mc.thePlayer.rotationYaw;
        if (forward > 0.0) {
            forward = 1.0;
        }
        else if (forward < 0.0) {
            forward = -1.0;
        }
        if (strafe > 0.0) {
            strafe = 1.0;
        }
        else if (strafe < 0.0) {
            strafe = -1.0;
        }
        if (forward != 0.0 && strafe != 0.0) {
            forward *= Math.sin(0.6398355709958845);
            strafe *= Math.cos(0.6398355709958845);
        }
        return new double[] { forward * speed * -Math.sin(Math.toRadians(yaw)) + strafe * speed * Math.cos(Math.toRadians(yaw)) * mult, forward * speed * Math.cos(Math.toRadians(yaw)) - strafe * speed * -Math.sin(Math.toRadians(yaw)) * mult };
    }
    
    public static void setMoveSpeed(final MoveEvent event, final double moveSpeed) {
        final MovementInput progamermove = SpeedUtils2.mc.thePlayer.movementInput;
        double yeetthefuckingbaby = progamermove.moveForward;
        double yeetonniggers = progamermove.moveStrafe;
        double yaw = SpeedUtils2.mc.thePlayer.rotationYaw;
        if (yeetthefuckingbaby == 0.0 && yeetonniggers == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        }
        else {
            if (yeetonniggers > 0.0) {
                yeetonniggers = 1.0;
            }
            else if (yeetonniggers < 0.0) {
                yeetonniggers = -1.0;
            }
            if (yeetthefuckingbaby != 0.0) {
                if (yeetonniggers > 0.0) {
                    yaw += ((yeetthefuckingbaby > 0.0) ? -45 : 45);
                }
                else if (yeetonniggers < 0.0) {
                    yaw += ((yeetthefuckingbaby > 0.0) ? 45 : -45);
                }
                yeetonniggers = 0.0;
                if (yeetthefuckingbaby > 0.0) {
                    yeetthefuckingbaby = 1.0;
                }
                else if (yeetthefuckingbaby < 0.0) {
                    yeetthefuckingbaby = -1.0;
                }
            }
            event.setX(yeetthefuckingbaby * moveSpeed * Math.cos(Math.toRadians(yaw + 90.0)) + yeetonniggers * moveSpeed * Math.sin(Math.toRadians(yaw + 90.0)));
            event.setZ(yeetthefuckingbaby * moveSpeed * Math.sin(Math.toRadians(yaw + 90.0)) - yeetonniggers * moveSpeed * Math.cos(Math.toRadians(yaw + 90.0)));
        }
    }
    
    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (SpeedUtils2.mc.thePlayer != null && Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            final int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
    
    public static double getRandomness() {
        return RandomUtils.nextDouble(1.0E-12, 1.0E-4);
    }
    
    public static double getJumpHypixel() {
        double motionY = 0.415 + getRandomness();
        if (SpeedUtils2.mc.thePlayer.isPotionActive(Potion.jump)) {
            motionY += (SpeedUtils2.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1f;
        }
        return motionY;
    }
    
    public static double getBaseSpeedHypixel(final double multiplier) {
        return getBaseMoveSpeed() * multiplier + getRandomness();
    }
    
    public static double getHypixelSlow(final double base, final double speed) {
        return base * (speed - getBaseSpeedHypixel(1.0));
    }
    
    public static double getFrictionHypixel(final double speed) {
        return speed - speed / (129.0 + getRandomness());
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
}
