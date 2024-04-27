package me.utils;

import net.ccbluex.liquidbounce.api.minecraft.potion.PotionType;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

public class ValueUtil {
    public static double getMotion(double initialSpeed, double speedMultiplier) {
        double speed = initialSpeed;
        if (MinecraftInstance.mc.getThePlayer().isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED))) {
            int effect = MinecraftInstance.mc.getThePlayer().getActivePotionEffect(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED)).getAmplifier();
            speed *= 1.0 + speedMultiplier * ((double)effect + 1.0);
        }
        return speed;
    }

    public static double getModifiedMotionY(double mY) {
        if (MinecraftInstance.mc.getThePlayer().isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.JUMP))) {
            mY += (double)(MinecraftInstance.mc.getThePlayer().getActivePotionEffect(MinecraftInstance.classProvider.getPotionEnum(PotionType.JUMP)).getAmplifier() + 1) * 0.1;
        }
        return mY;
    }

    public static double getBaseMotionY() {
        double motion = 0.42f;
        if (MinecraftInstance.mc.getThePlayer().isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.JUMP))) {
            motion += (double)(MinecraftInstance.mc.getThePlayer().getActivePotionEffect(MinecraftInstance.classProvider.getPotionEnum(PotionType.JUMP)).getAmplifier() + 1) * 0.1;
        }
        return motion;
    }
}
