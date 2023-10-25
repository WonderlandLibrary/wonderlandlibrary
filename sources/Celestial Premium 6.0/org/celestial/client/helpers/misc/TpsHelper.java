/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.helpers.misc;

import java.util.Arrays;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.util.math.MathHelper;
import org.celestial.client.event.EventManager;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.packet.EventReceivePacket;
import org.celestial.client.helpers.Helper;

public class TpsHelper
implements Helper {
    private static float[] tickRates = new float[20];
    private int nextIndex = 0;
    private long timeLastTimeUpdate = -1L;

    public TpsHelper() {
        Arrays.fill(tickRates, 0.0f);
        EventManager.register(this);
    }

    public static float getTickRate() {
        float numTicks = 0.0f;
        float sumTickRates = 0.0f;
        for (float tickRate : tickRates) {
            if (!(tickRate > 0.0f)) continue;
            sumTickRates += tickRate;
            numTicks += 1.0f;
        }
        return MathHelper.clamp(sumTickRates / numTicks, 0.0f, 20.0f);
    }

    private void onTimeUpdate() {
        if (this.timeLastTimeUpdate != -1L) {
            float timeElapsed = (float)(System.currentTimeMillis() - this.timeLastTimeUpdate) / 1000.0f;
            TpsHelper.tickRates[this.nextIndex % TpsHelper.tickRates.length] = MathHelper.clamp(20.0f / timeElapsed, 0.0f, 20.0f);
            ++this.nextIndex;
        }
        this.timeLastTimeUpdate = System.currentTimeMillis();
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof SPacketTimeUpdate) {
            this.onTimeUpdate();
        }
    }
}

