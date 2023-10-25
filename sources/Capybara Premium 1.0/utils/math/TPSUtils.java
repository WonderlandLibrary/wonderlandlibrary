package fun.expensive.client.utils.math;

import fun.expensive.client.event.EventManager;
import fun.expensive.client.event.EventTarget;
import fun.expensive.client.event.events.impl.packet.EventReceivePacket;
import fun.expensive.client.utils.Helper;
import net.minecraft.util.math.MathHelper;

import java.util.Arrays;

public class TPSUtils implements Helper {
    private static float[] tickRates = new float[20];

    private int nextIndex = 0;

    private long timeLastTimeUpdate;

    public TPSUtils() {
        this.nextIndex = 0;
        this.timeLastTimeUpdate = -1L;
        Arrays.fill(tickRates, 0.0F);
        EventManager.register(this);
    }

    public static float getTickRate() {
        float numTicks = 0.0F;
        float sumTickRates = 0.0F;
        for (float tickRate : tickRates) {
            if (tickRate > 0.0F) {
                sumTickRates += tickRate;
                numTicks++;
            }
        }
        return MathHelper.clamp(sumTickRates / numTicks, 0.0F, 20.0F);
    }

    private void onTimeUpdate() {
        if (this.timeLastTimeUpdate != -1L) {
            float timeElapsed = (float)(System.currentTimeMillis() - this.timeLastTimeUpdate) / 1000.0F;
            tickRates[this.nextIndex % tickRates.length] = MathHelper.clamp(20.0F / timeElapsed, 0.0F, 20.0F);
            this.nextIndex++;
        }
        this.timeLastTimeUpdate = System.currentTimeMillis();
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketTimeUpdate)
            onTimeUpdate();
    }
}
