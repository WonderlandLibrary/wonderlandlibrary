/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.player.disabler;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

public class PacketSleepThread
extends Thread {
    private Packet packet;
    private long delay;

    public PacketSleepThread(Packet packet, long delay) {
        this.packet = packet;
        this.delay = delay;
    }

    @Override
    public void run() {
        try {
            PacketSleepThread.sleep(this.delay);
            Minecraft.getMinecraft().getNetHandler().sendPacketNoEvent(this.packet);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

