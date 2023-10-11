package com.initial.events.impl;

import com.initial.events.*;

public class MoveEvent extends Event
{
    private double motionX;
    private double motionY;
    private double motionZ;
    
    public MoveEvent(final double motionX, final double motionY, final double motionZ) {
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
    }
    
    public void actualSetSpeedX(final double motionX) {
        this.motionX = motionX;
    }
    
    public void actualSetSpeedY(final double motionY) {
        this.motionY = motionY;
    }
    
    public void actualSetSpeedZ(final double motionZ) {
        this.motionZ = motionZ;
    }
    
    public double getMotionX() {
        return this.motionX;
    }
    
    public double getMotionY() {
        return this.motionY;
    }
    
    public double getMotionZ() {
        return this.motionZ;
    }
    
    public void zero() {
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
    }
}
