/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util;

import net.minecraft.client.Minecraft;

public class Timer {
    public int elapsedTicks;
    public float renderPartialTicks;
    public float field_194148_c;
    public float timerSpeed = 1.0f;
    private long lastSyncSysClock;
    private float field_194149_e;

    public Timer(float tps) {
        this.field_194149_e = 1000.0f / tps;
        this.lastSyncSysClock = Minecraft.getSystemTime();
    }

    public void updateTimer() {
        long i = Minecraft.getSystemTime();
        this.field_194148_c = (float)(i - this.lastSyncSysClock) * this.timerSpeed / this.field_194149_e;
        this.lastSyncSysClock = i;
        this.renderPartialTicks += this.field_194148_c;
        this.elapsedTicks = (int)this.renderPartialTicks;
        this.renderPartialTicks -= (float)this.elapsedTicks;
    }
}

