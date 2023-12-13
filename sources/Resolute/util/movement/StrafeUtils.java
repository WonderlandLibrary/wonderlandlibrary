// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.movement;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MathHelper;
import vip.Resolute.events.impl.StrafeEvent;
import net.minecraft.client.Minecraft;

public class StrafeUtils
{
    private static Minecraft mc;
    
    public static void customSilentMoveFlying(final StrafeEvent event, final float yaw) {
        final int dif = (int)((MathHelper.wrapAngleTo180_float(StrafeUtils.mc.thePlayer.rotationYaw - yaw - 23.5f - 135.0f) + 180.0f) / 45.0f);
        final float strafe = event.getStrafe();
        final float forward = event.getForward();
        final float friction = event.getFriction();
        float calcForward = 0.0f;
        float calcStrafe = 0.0f;
        switch (dif) {
            case 0: {
                calcForward = forward;
                calcStrafe = strafe;
                break;
            }
            case 1: {
                calcForward += forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe += strafe;
                break;
            }
            case 2: {
                calcForward = strafe;
                calcStrafe = -forward;
                break;
            }
            case 3: {
                calcForward -= forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe -= strafe;
                break;
            }
            case 4: {
                calcForward = -forward;
                calcStrafe = -strafe;
                break;
            }
            case 5: {
                calcForward -= forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe -= strafe;
                break;
            }
            case 6: {
                calcForward = -strafe;
                calcStrafe = forward;
                break;
            }
            case 7: {
                calcForward += forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe += strafe;
                break;
            }
        }
        if (calcForward > 1.0f || (calcForward < 0.9f && calcForward > 0.3f) || calcForward < -1.0f || (calcForward > -0.9f && calcForward < -0.3f)) {
            calcForward *= 0.5f;
        }
        if (calcStrafe > 1.0f || (calcStrafe < 0.9f && calcStrafe > 0.3f) || calcStrafe < -1.0f || (calcStrafe > -0.9f && calcStrafe < -0.3f)) {
            calcStrafe *= 0.5f;
        }
        float d;
        if ((d = calcStrafe * calcStrafe + calcForward * calcForward) >= 1.0E-4f) {
            if ((d = MathHelper.sqrt_float(d)) < 1.0f) {
                d = 1.0f;
            }
            d = friction / d;
            final float yawSin = MathHelper.sin((float)(yaw * 3.141592653589793 / 180.0));
            final float yawCos = MathHelper.cos((float)(yaw * 3.141592653589793 / 180.0));
            final EntityPlayerSP thePlayer = StrafeUtils.mc.thePlayer;
            thePlayer.motionX += (calcStrafe *= d) * yawCos - (calcForward *= d) * yawSin;
            final EntityPlayerSP thePlayer2 = StrafeUtils.mc.thePlayer;
            thePlayer2.motionZ += calcForward * yawCos + calcStrafe * yawSin;
        }
    }
    
    static {
        StrafeUtils.mc = Minecraft.getMinecraft();
    }
}
