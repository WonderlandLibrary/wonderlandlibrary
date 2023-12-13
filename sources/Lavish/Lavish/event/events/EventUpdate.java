// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.event.events;

import net.minecraft.client.Minecraft;
import Lavish.event.Event;

public class EventUpdate extends Event<EventUpdate>
{
    public float yaw;
    public float pitch;
    public boolean ground;
    public double x;
    public double z;
    public double y;
    private boolean pre;
    
    public EventUpdate(final float yaw, final float pitch, final boolean ground, final double y, final double x, final double z, final boolean pre) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.ground = ground;
        this.y = y;
        this.x = x;
        this.z = z;
        this.pre = pre;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public void setYaw(final float yaw) {
        this.yaw = yaw;
        Minecraft.getMinecraft().thePlayer.renderYawOffset = yaw;
        Minecraft.getMinecraft().thePlayer.rotationYawHead = yaw;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }
    
    public boolean isGround() {
        return this.ground;
    }
    
    public void setGround(final boolean ground) {
        this.ground = ground;
    }
    
    public double getY() {
        return this.y;
    }
    
    public void setY(final double y) {
        this.y = y;
    }
    
    public void setX(final double x) {
        this.x = x;
    }
    
    public void setZ(final double z) {
        this.z = z;
    }
    
    @Override
    public boolean isPre() {
        return this.pre;
    }
    
    @Override
    public boolean isPost() {
        return !this.pre;
    }
}
