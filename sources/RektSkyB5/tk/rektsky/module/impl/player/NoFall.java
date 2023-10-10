/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.player;

import java.util.ArrayDeque;
import java.util.Queue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.greenrobot.eventbus.Subscribe;
import tk.rektsky.event.impl.PacketSentEvent;
import tk.rektsky.event.impl.WorldTickEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.settings.ListSetting;
import tk.rektsky.utils.Timer;

public class NoFall
extends Module {
    public ListSetting mode = new ListSetting("Mode", new String[]{"Vanilla", "Verus"}, "Verus");
    private final Queue<Packet<?>> packets = new ArrayDeque();
    private Timer timer = new Timer();

    public NoFall() {
        super("NoFall", "Makes you take no or less fall damage", Category.PLAYER, false);
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
        this.clearQueue();
    }

    @Subscribe
    public void onEvent(PacketSentEvent event) {
        C03PacketPlayer c03PacketPlayer;
        if (this.mode.getValue().equalsIgnoreCase("Vanilla") && event.getPacket() instanceof C03PacketPlayer) {
            c03PacketPlayer = (C03PacketPlayer)event.getPacket();
            c03PacketPlayer.onGround = true;
        }
        if (this.mode.getValue().equalsIgnoreCase("Verus")) {
            if (event.getPacket() instanceof C03PacketPlayer && this.mc.thePlayer.fallDistance > 3.0f) {
                this.mc.thePlayer.fallDistance = 0.0f;
                c03PacketPlayer = (C03PacketPlayer)event.getPacket();
                c03PacketPlayer.onGround = true;
                if (c03PacketPlayer.isMoving()) {
                    c03PacketPlayer.y -= c03PacketPlayer.y % 0.015625;
                }
            }
            if ((event.getPacket() instanceof C03PacketPlayer || event.getPacket() instanceof C02PacketUseEntity) && !event.isCanceled()) {
                event.setCanceled(true);
                this.packets.add(event.getPacket());
            }
        }
    }

    public void clearQueue() {
        while (!this.packets.isEmpty()) {
            if (!this.mc.getNetHandler().getNetworkManager().isChannelOpen()) continue;
            try {
                this.mc.getNetHandler().addToSendQueueSilent(this.packets.poll());
            }
            catch (Exception exception) {}
        }
    }

    @Subscribe
    public void onTick(WorldTickEvent event) {
        if (this.mode.getValue().equalsIgnoreCase("Verus") && this.timer.hasTimeElapsed(250L, true)) {
            this.clearQueue();
        }
    }

    @Override
    public String getSuffix() {
        return this.mode.getValue();
    }
}

