/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.world;

import java.util.ArrayDeque;
import java.util.Queue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S07PacketRespawn;
import org.greenrobot.eventbus.Subscribe;
import tk.rektsky.event.Event;
import tk.rektsky.event.impl.HLPacketSentEvent;
import tk.rektsky.event.impl.PacketReceiveEvent;
import tk.rektsky.event.impl.PacketSentEvent;
import tk.rektsky.event.impl.WorldTickEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.settings.BooleanSetting;
import tk.rektsky.module.settings.DoubleSetting;

public class Timer
extends Module {
    public DoubleSetting timerSpeed = new DoubleSetting("Speed", 0.1, 20.0, 1.0);
    Queue<Packet> packets = new ArrayDeque<Packet>();
    public BooleanSetting verus = new BooleanSetting("Verus", false);
    private tk.rektsky.utils.Timer timer = new tk.rektsky.utils.Timer();

    public Timer() {
        super("Timer", "Changes the in game timer", Category.WORLD, false);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof WorldTickEvent && this.mc.thePlayer != null) {
            this.mc.timer.timerSpeed = (float)this.timerSpeed.getValue().doubleValue();
            if (this.timer.hasTimeElapsed(250L)) {
                this.timer.reset();
                while (!this.packets.isEmpty()) {
                    if (!this.mc.getNetHandler().getNetworkManager().isChannelOpen()) continue;
                    try {
                        this.mc.getNetHandler().addToSendQueueSilent(this.packets.poll());
                    }
                    catch (Exception exception) {}
                }
            }
        }
    }

    @Override
    public void onEnable() {
        this.packets.clear();
    }

    @Override
    public void onDisable() {
        this.mc.timer.timerSpeed = 1.0;
        while (!this.packets.isEmpty()) {
            if (!this.mc.getNetHandler().getNetworkManager().isChannelOpen()) continue;
            try {
                this.mc.getNetHandler().addToSendQueueSilent(this.packets.poll());
            }
            catch (Exception exception) {}
        }
    }

    @Subscribe
    public void test(HLPacketSentEvent event) {
        System.out.println(event.getPacket().getClass().getSimpleName());
    }

    @Subscribe
    public void onPacketSend(PacketSentEvent event) {
        if (this.mc.thePlayer != null && this.mc.thePlayer.ticksExisted > 1 && this.verus.getValue().booleanValue() && (event.getPacket() instanceof C03PacketPlayer || event.getPacket() instanceof C02PacketUseEntity) && !event.isCanceled()) {
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
}

