package dev.rise.module.impl.other;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.other.WorldChangedEvent;
import dev.rise.event.impl.packet.PacketReceiveEvent;
import dev.rise.event.impl.packet.PacketSendEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.math.TimeUtil;
import dev.rise.util.player.PacketUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ModuleInfo(name = "PingSpoof", description = "Makes your ping higher for the server", category = Category.OTHER)
public final class PingSpoof extends Module {

    private final NumberSetting delay = new NumberSetting("Delay", this, 1000, 10, 30000, 1);

    private final ConcurrentHashMap<Packet<?>, Long> packets = new ConcurrentHashMap<>();

    private final TimeUtil timer = new TimeUtil();

    @Override
    protected void onDisable() {
        packets.clear();
    }

    @Override
    public void onWorldChanged(final WorldChangedEvent event) {
        if (mc.isSingleplayer()) return;

        packets.clear();
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (mc.isSingleplayer())
            return;

        for (final Iterator<Map.Entry<Packet<?>, Long>> iterator = packets.entrySet().iterator(); iterator.hasNext(); ) {
            final Map.Entry<Packet<?>, Long> entry = iterator.next();

            if (entry.getValue() < System.currentTimeMillis()) {
                PacketUtil.sendPacket(entry.getKey());
                iterator.remove();
            }
        }
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (mc.isSingleplayer())
            return;

        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            timer.reset();
        }
    }

    @Override
    public void onPacketSend(final PacketSendEvent event) {
        if (mc.isSingleplayer())
            return;

        final Packet<?> p = event.getPacket();

        if (p instanceof C0FPacketConfirmTransaction || p instanceof C00PacketKeepAlive) {
            packets.put(p, (long) (System.currentTimeMillis() + delay.getValue()));
            event.setCancelled(true);
        }
    }
}