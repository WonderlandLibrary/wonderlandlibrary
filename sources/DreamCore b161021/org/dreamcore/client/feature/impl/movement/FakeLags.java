package org.dreamcore.client.feature.impl.movement;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.packet.EventSendPacket;
import org.dreamcore.client.event.events.impl.player.EventUpdate;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.misc.TimerHelper;
import org.dreamcore.client.settings.impl.NumberSetting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FakeLags extends Feature {

    public final LinkedList<double[]> positions = new LinkedList<>();
    public List<Packet<?>> packets = new ArrayList<>();
    public TimerHelper pulseTimer = new TimerHelper();
    public NumberSetting ticks = new NumberSetting("Ticks", 8, 1, 30, 1, () -> true);
    private boolean enableFakeLags;

    public FakeLags() {
        super("Fake Lags", "У других вы лагаете", Type.Movement);
        addSettings(ticks);
    }

    @Override
    public void onEnable() {
        synchronized (this.positions) {
            this.positions.add(new double[]{mc.player.posX, mc.player.getEntityBoundingBox().minY + mc.player.getEyeHeight() / 2.0f, mc.player.posZ});
            this.positions.add(new double[]{mc.player.posX, mc.player.getEntityBoundingBox().minY, mc.player.posZ});
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        packets.clear();
        positions.clear();
    }


    @EventTarget
    public void onUpdate(EventUpdate event) {
        synchronized (this.positions) {
            this.positions.add(new double[]{mc.player.posX, mc.player.getEntityBoundingBox().minY, mc.player.posZ});
        }
        if (this.pulseTimer.hasReached(ticks.getNumberValue() * 50)) {
            try {
                this.enableFakeLags = true;
                Iterator<Packet<?>> packetIterator = this.packets.iterator();
                while (packetIterator.hasNext()) {
                    mc.player.connection.sendPacket(packetIterator.next());
                    packetIterator.remove();
                }
                this.enableFakeLags = false;
            } catch (Exception e) {
                this.enableFakeLags = false;
            }
            synchronized (this.positions) {
                this.positions.clear();
            }
            this.pulseTimer.reset();
        }
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (mc.player == null || !(event.getPacket() instanceof CPacketPlayer) || enableFakeLags) {
            return;
        }
        event.setCancelled(true);
        if (!(event.getPacket() instanceof CPacketPlayer.Position) && !(event.getPacket() instanceof CPacketPlayer.PositionRotation)) {
            return;
        }
        this.packets.add(event.getPacket());
    }

}
