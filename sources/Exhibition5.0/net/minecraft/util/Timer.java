// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import net.minecraft.client.Minecraft;

public class Timer
{
    float ticksPerSecond;
    private double lastHRTime;
    public int elapsedTicks;
    public float renderPartialTicks;
    public float timerSpeed;
    public float elapsedPartialTicks;
    private long lastSyncSysClock;
    private long lastSyncHRClock;
    private long field_74285_i;
    private double timeSyncAdjustment;
    private static final String __OBFID = "CL_00000658";
    
    public Timer(final float p_i1018_1_) {
        this.timerSpeed = 1.0f;
        this.timeSyncAdjustment = 1.0;
        this.ticksPerSecond = p_i1018_1_;
        this.lastSyncSysClock = Minecraft.getSystemTime();
        this.lastSyncHRClock = System.nanoTime() / 1000000L;
    }
    
    public void updateTimer() {
        final long var1 = Minecraft.getSystemTime();
        final long var2 = var1 - this.lastSyncSysClock;
        final long var3 = System.nanoTime() / 1000000L;
        final double var4 = var3 / 1000.0;
        if (var2 <= 1000L && var2 >= 0L) {
            this.field_74285_i += var2;
            if (this.field_74285_i > 1000L) {
                final long var5 = var3 - this.lastSyncHRClock;
                final double var6 = this.field_74285_i / var5;
                this.timeSyncAdjustment += (var6 - this.timeSyncAdjustment) * 0.20000000298023224;
                this.lastSyncHRClock = var3;
                this.field_74285_i = 0L;
            }
            if (this.field_74285_i < 0L) {
                this.lastSyncHRClock = var3;
            }
        }
        else {
            this.lastHRTime = var4;
        }
        this.lastSyncSysClock = var1;
        double var7 = (var4 - this.lastHRTime) * this.timeSyncAdjustment;
        this.lastHRTime = var4;
        var7 = MathHelper.clamp_double(var7, 0.0, 1.0);
        this.elapsedPartialTicks += (float)(var7 * this.timerSpeed * this.ticksPerSecond);
        this.elapsedTicks = (int)this.elapsedPartialTicks;
        this.elapsedPartialTicks -= this.elapsedTicks;
        if (this.elapsedTicks > 10) {
            this.elapsedTicks = 10;
        }
        this.renderPartialTicks = this.elapsedPartialTicks;
    }
}
