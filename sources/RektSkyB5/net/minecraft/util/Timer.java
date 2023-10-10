/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

public class Timer {
    public float ticksPerSecond;
    private double lastHRTime;
    public int elapsedTicks;
    public float renderPartialTicks;
    public double timerSpeed = 1.0;
    public float elapsedPartialTicks;
    private long lastSyncSysClock;
    private long lastSyncHRClock;
    private long field_74285_i;
    private double timeSyncAdjustment = 1.0;

    public Timer(float p_i1018_1_) {
        this.ticksPerSecond = p_i1018_1_;
        this.lastSyncSysClock = Minecraft.getSystemTime();
        this.lastSyncHRClock = System.nanoTime() / 1000000L;
    }

    public void updateTimer() {
        long i2 = Minecraft.getSystemTime();
        long j2 = i2 - this.lastSyncSysClock;
        long k2 = System.nanoTime() / 1000000L;
        double d0 = (double)k2 / 1000.0;
        if (j2 <= 1000L && j2 >= 0L) {
            this.field_74285_i += j2;
            if (this.field_74285_i > 1000L) {
                long l2 = k2 - this.lastSyncHRClock;
                double d1 = (double)this.field_74285_i / (double)l2;
                this.timeSyncAdjustment += (d1 - this.timeSyncAdjustment) * (double)0.2f;
                this.lastSyncHRClock = k2;
                this.field_74285_i = 0L;
            }
            if (this.field_74285_i < 0L) {
                this.lastSyncHRClock = k2;
            }
        } else {
            this.lastHRTime = d0;
        }
        this.lastSyncSysClock = i2;
        double d2 = (d0 - this.lastHRTime) * this.timeSyncAdjustment;
        this.lastHRTime = d0;
        d2 = MathHelper.clamp_double(d2, 0.0, 1.0);
        this.elapsedPartialTicks = (float)((double)this.elapsedPartialTicks + d2 * this.timerSpeed * (double)this.ticksPerSecond);
        this.elapsedTicks = (int)this.elapsedPartialTicks;
        this.elapsedPartialTicks -= (float)this.elapsedTicks;
        if (this.elapsedTicks > 10) {
            this.elapsedTicks = 10;
        }
        this.renderPartialTicks = this.elapsedPartialTicks;
    }
}

