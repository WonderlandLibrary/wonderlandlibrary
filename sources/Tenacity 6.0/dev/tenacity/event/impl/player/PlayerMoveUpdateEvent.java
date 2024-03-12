// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.event.impl.player;

import net.minecraft.client.entity.EntityPlayerSP;
import dev.tenacity.utils.player.MovementUtils;
import net.minecraft.client.Minecraft;
import dev.tenacity.event.Event;

public class PlayerMoveUpdateEvent extends Event
{
    private float strafe;
    private float forward;
    private float friction;
    private float yaw;
    private float pitch;
    
    public void applyMotion(final double speed, float strafeMotion) {
        final float remainder = 1.0f - strafeMotion;
        final EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        strafeMotion *= (float)0.91;
        if (player.onGround) {
            MovementUtils.setSpeed(speed);
        }
        else {
            player.motionX = player.getMotionX() * strafeMotion;
            player.motionZ = player.getMotionZ() * strafeMotion;
            this.friction = (float)speed * remainder;
        }
    }
    
    public float getStrafe() {
        return this.strafe;
    }
    
    public float getForward() {
        return this.forward;
    }
    
    public float getFriction() {
        return this.friction;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public void setStrafe(final float strafe) {
        this.strafe = strafe;
    }
    
    public void setForward(final float forward) {
        this.forward = forward;
    }
    
    public void setFriction(final float friction) {
        this.friction = friction;
    }
    
    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }
    
    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }
    
    public PlayerMoveUpdateEvent(final float strafe, final float forward, final float friction, final float yaw, final float pitch) {
        this.strafe = strafe;
        this.forward = forward;
        this.friction = friction;
        this.yaw = yaw;
        this.pitch = pitch;
    }
}
