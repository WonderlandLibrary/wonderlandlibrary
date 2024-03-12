// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.event.impl.player;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.Minecraft;
import dev.tenacity.event.Event;

public class StrafeEvent extends Event
{
    private float forward;
    private float strafe;
    private float friction;
    Minecraft mc;
    
    public void setSpeedPartialStrafe(float friction, final float strafe) {
        final float remainder = 1.0f - strafe;
        if (this.forward != 0.0f && this.strafe != 0.0f) {
            friction *= (float)0.91;
        }
        if (this.mc.thePlayer.onGround) {
            this.setSpeed(friction);
        }
        else {
            final EntityPlayerSP thePlayer = this.mc.thePlayer;
            thePlayer.motionX *= strafe;
            final EntityPlayerSP thePlayer2 = this.mc.thePlayer;
            thePlayer2.motionZ *= strafe;
            this.setFriction(friction * remainder);
        }
    }
    
    public void setSpeed(final float speed, final double motionMultiplier) {
        this.setFriction((this.getForward() != 0.0f && this.getStrafe() != 0.0f) ? (speed * 0.99f) : speed);
        final EntityPlayerSP thePlayer = this.mc.thePlayer;
        thePlayer.motionX *= motionMultiplier;
        final EntityPlayerSP thePlayer2 = this.mc.thePlayer;
        thePlayer2.motionZ *= motionMultiplier;
    }
    
    public void setSpeed(final float speed) {
        this.setFriction((this.getForward() != 0.0f && this.getStrafe() != 0.0f) ? (speed * 0.99f) : speed);
        final EntityPlayerSP thePlayer = this.mc.thePlayer;
        final EntityPlayerSP thePlayer2 = this.mc.thePlayer;
        final double n = 0.0;
        thePlayer2.motionZ = n;
        thePlayer.motionX = n;
    }
    
    public float getForward() {
        return this.forward;
    }
    
    public float getStrafe() {
        return this.strafe;
    }
    
    public float getFriction() {
        return this.friction;
    }
    
    public Minecraft getMc() {
        return this.mc;
    }
    
    public void setForward(final float forward) {
        this.forward = forward;
    }
    
    public void setStrafe(final float strafe) {
        this.strafe = strafe;
    }
    
    public void setFriction(final float friction) {
        this.friction = friction;
    }
    
    public void setMc(final Minecraft mc) {
        this.mc = mc;
    }
    
    public StrafeEvent(final float forward, final float strafe, final float friction, final Minecraft mc) {
        this.mc = Minecraft.getMinecraft();
        this.forward = forward;
        this.strafe = strafe;
        this.friction = friction;
        this.mc = mc;
    }
}
