package net.minecraft.util;

import io.github.raze.events.collection.game.EventTime;
import net.minecraft.client.Minecraft;

public class Timer
{

    public int elapsedTicks;
    public float partialTicks;
    public float field_194148_c;
    public float renderPartialTicks;
    private long lastSyncSysClock;
    private float field_194149_e;
    public float timerSpeed;

    public Timer(float tps)
    {
        this.field_194149_e = 1000.0F / tps;
        this.lastSyncSysClock = Minecraft.getSystemTime();
        this.timerSpeed = 1.0F;
    }

    public void updateTimer()
    {
        EventTime eventTime = new EventTime(Minecraft.getSystemTime());
        eventTime.call();
        long i = eventTime.getBalance();
        this.field_194148_c = (float)(i - this.lastSyncSysClock) / this.field_194149_e * this.timerSpeed;
        this.lastSyncSysClock = i;
        this.partialTicks += this.field_194148_c;
        this.elapsedTicks = (int)this.partialTicks;
        this.partialTicks -= (float)this.elapsedTicks;
        this.renderPartialTicks = this.partialTicks;
    }
}
