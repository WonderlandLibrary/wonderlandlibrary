package net.minecraft.util;

import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import net.minecraft.client.Minecraft;

public class Timer
{
    /**
     * How many full ticks have turned over since the last call to updateTimer(), capped at 10.
     */
    public int elapsedTicks;
    public float renderPartialTicks;
    public float elapsedPartialTicks;

    /**
     * The time reported by the system clock at the last sync, in milliseconds
     */
    private long lastSyncSysClock;
    public float tickLength;

    public Timer(float tps)
    {
        this.tickLength = 1000.0F / tps;
        this.lastSyncSysClock = Minecraft.getSystemTime();
    }

    /**
     * Updates all fields of the Timer using the current time
     */
    public void updateTimer()
    {
        long i = Minecraft.getSystemTime();
        this.elapsedPartialTicks = (float)(i - this.lastSyncSysClock) / this.tickLength;
        this.lastSyncSysClock = i;
        this.renderPartialTicks += this.elapsedPartialTicks;
        this.elapsedTicks = (int)this.renderPartialTicks;
        this.renderPartialTicks -= (float)this.elapsedTicks;
    }
    
    public void setTimerSpeed(float timerSpeed) {
    	this.tickLength = 1000f / (20 * timerSpeed);
    }
    
}
