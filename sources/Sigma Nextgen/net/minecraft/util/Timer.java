package net.minecraft.util;

import info.sigmaclient.sigma.utils.render.anims.PartialTicksAnim;

public class Timer
{
    public float renderPartialTicks;
    public float elapsedPartialTicks;
    private long lastSyncSysClock;
    public float tickLength;

    public Timer(float ticks, long lastSyncSysClock)
    {
        this.tickLength = 1000.0F / ticks;
        this.lastSyncSysClock = lastSyncSysClock;
    }
    public float getTimerSpeed(){
        return tickLength / 50;
    }
    public void setTimerSpeed(float speed){
        tickLength = 50F / speed;
    }

    public int getPartialTicks(long gameTime)
    {
        this.elapsedPartialTicks = (float)(gameTime - this.lastSyncSysClock) / this.tickLength;
        this.lastSyncSysClock = gameTime;
        this.renderPartialTicks += this.elapsedPartialTicks;
        int i = (int)this.renderPartialTicks;
        this.renderPartialTicks -= (float)i;
        return i;
    }
}
