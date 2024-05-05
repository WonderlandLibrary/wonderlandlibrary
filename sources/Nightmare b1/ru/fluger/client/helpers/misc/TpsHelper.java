// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.helpers.misc;

import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.packet.EventReceivePacket;
import ru.fluger.client.event.EventManager;
import java.util.Arrays;
import ru.fluger.client.helpers.Helper;

public class TpsHelper implements Helper
{
    private static float[] tickRates;
    private int nextIndex;
    private long timeLastTimeUpdate;
    
    public TpsHelper() {
        this.nextIndex = 0;
        this.nextIndex = 0;
        this.timeLastTimeUpdate = -1L;
        Arrays.fill(TpsHelper.tickRates, 0.0f);
        EventManager.register(this);
    }
    
    public static float getTickRate() {
        float numTicks = 0.0f;
        float sumTickRates = 0.0f;
        for (final float tickRate : TpsHelper.tickRates) {
            if (tickRate > 0.0f) {
                sumTickRates += tickRate;
                ++numTicks;
            }
        }
        return rk.a(sumTickRates / numTicks, 0.0f, 20.0f);
    }
    
    private void onTimeUpdate() {
        if (this.timeLastTimeUpdate != -1L) {
            final float timeElapsed = (System.currentTimeMillis() - this.timeLastTimeUpdate) / 1000.0f;
            TpsHelper.tickRates[this.nextIndex % TpsHelper.tickRates.length] = rk.a(20.0f / timeElapsed, 0.0f, 20.0f);
            ++this.nextIndex;
        }
        this.timeLastTimeUpdate = System.currentTimeMillis();
    }
    
    @EventTarget
    public void onReceivePacket(final EventReceivePacket event) {
        if (event.getPacket() instanceof ko) {
            this.onTimeUpdate();
        }
    }
    
    static {
        TpsHelper.tickRates = new float[20];
    }
}
