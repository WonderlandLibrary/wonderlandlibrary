/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.player;

import java.util.ArrayDeque;
import java.util.Queue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S07PacketRespawn;
import org.greenrobot.eventbus.Subscribe;
import tk.rektsky.event.impl.PacketReceiveEvent;
import tk.rektsky.event.impl.PacketSentEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;

public class Blink
extends Module {
    Queue<Packet> packets = new ArrayDeque<Packet>();

    public Blink() {
        super("Blink", "Basically a lag switch", Category.PLAYER);
    }

    @Subscribe
    public void onPacketSend(PacketSentEvent event) {
        if (this.mc.thePlayer != null && this.mc.thePlayer.ticksExisted > 1) {
            this.packets.add(event.getPacket());
            event.setCanceled(true);
        }
    }

    @Subscribe
    public void onRejoin(PacketReceiveEvent event) {
        if (event.getPacket() instanceof S01PacketJoinGame || event.getPacket() instanceof S07PacketRespawn) {
            this.setToggled(false);
        }
    }

    @Override
    public void onDisable() {
        while (!this.packets.isEmpty()) {
            if (!this.mc.getNetHandler().getNetworkManager().isChannelOpen()) continue;
            try {
                this.mc.getNetHandler().addToSendQueueSilent(this.packets.poll());
            }
            catch (Exception exception) {}
        }
    }
}

