package com.initial.events.impl;

import com.initial.events.*;
import net.minecraft.client.*;

public class EventMove extends Event
{
    public double x;
    public double y;
    public double z;
    private final Minecraft getMc;
    
    public EventMove(final double x, final double y, final double z) {
        this.getMc = Minecraft.getMinecraft();
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public double getX() {
        return this.x;
    }
    
    public void setX(final double x) {
        this.x = x;
        this.getMc.thePlayer.motionX = x;
    }
    
    public void setY(final double y) {
        this.y = y;
        this.getMc.thePlayer.motionY = y;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public void setZ(final double z) {
        this.z = z;
        this.getMc.thePlayer.motionZ = z;
    }
}
