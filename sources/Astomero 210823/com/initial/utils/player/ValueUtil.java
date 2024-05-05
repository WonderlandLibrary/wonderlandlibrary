package com.initial.utils.player;

import net.minecraft.client.*;
import net.minecraft.potion.*;

public class ValueUtil
{
    private static final Minecraft getMc;
    
    public static double getMotion(final double initialSpeed, final double speedMultiplier) {
        double speed = initialSpeed;
        if (ValueUtil.getMc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            final int effect = ValueUtil.getMc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            speed *= 1.0 + speedMultiplier * (effect + 1.0);
        }
        return speed;
    }
    
    public static double getModifiedMotionY(double mY) {
        if (ValueUtil.getMc.thePlayer.isPotionActive(Potion.jump)) {
            mY += (ValueUtil.getMc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1;
        }
        return mY;
    }
    
    public static double getBaseMotionY() {
        double motion = 0.41999998688697815;
        if (ValueUtil.getMc.thePlayer.isPotionActive(Potion.jump)) {
            motion += (ValueUtil.getMc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1;
        }
        return motion;
    }
    
    static {
        getMc = Minecraft.getMinecraft();
    }
}
