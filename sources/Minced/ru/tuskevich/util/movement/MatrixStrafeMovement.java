// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.movement;

import net.minecraft.entity.player.EntityPlayer;

public class MatrixStrafeMovement
{
    public static double oldSpeed;
    public static double contextFriction;
    public static boolean needSwap;
    public static boolean prevSprint;
    public static int counter;
    public static int noSlowTicks;
    
    public static float getAIMoveSpeed(final EntityPlayer contextPlayer) {
        final boolean prevSprinting = contextPlayer.isSprinting();
        contextPlayer.setSprinting(false);
        final float speed = contextPlayer.getAIMoveSpeed() * 1.3f;
        contextPlayer.setSprinting(prevSprinting);
        return speed;
    }
}
