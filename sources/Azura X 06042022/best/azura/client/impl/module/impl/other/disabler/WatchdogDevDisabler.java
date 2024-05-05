package best.azura.client.impl.module.impl.other.disabler;

import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventSentPacket;
import best.azura.client.impl.events.EventWorldChange;
import best.azura.client.impl.module.impl.other.Disabler;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.other.ServerUtil;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;

import java.util.ArrayList;
import java.util.UUID;

public class WatchdogDevDisabler implements ModeImpl<Disabler> {

    @Override
    public Disabler getParent() {
        return (Disabler) Client.INSTANCE.getModuleManager().getModule(Disabler.class);
    }

    @Override
    public String getName() {
        return "Watchdog Dev";
    }

    private final ArrayList<Packet<?>> packets = new ArrayList<>();
    private int ticks;

    @EventHandler
    public final Listener<EventSentPacket> eventSendPacketListener = e -> {
        if (!ServerUtil.isHypixel()) return;
        if (packets.contains(e.getPacket()))
            return;
        if (e.getPacket() instanceof C00PacketKeepAlive || e.getPacket() instanceof C0FPacketConfirmTransaction) {
            e.setCancelled(true);
            packets.add(e.getPacket());
        }
        if (e.getPacket() instanceof C00PacketKeepAlive || e.getPacket() instanceof C0FPacketConfirmTransaction) {
            ticks++;
            if (ticks % 16 == 0) {
                for (Packet<?> packet : packets)
                    mc.thePlayer.sendQueue.addToSendQueue(packet);
                packets.clear();
                mc.thePlayer.sendQueue.addToSendQueue(new C11PacketEnchantItem(MathUtil.getRandom_int(Integer.MIN_VALUE, Integer.MAX_VALUE),
                        MathUtil.getRandom_int(Integer.MIN_VALUE, Integer.MAX_VALUE)));
            }
        }
    };

    @EventHandler
    public final Listener<EventWorldChange> eventWorldChangeListener = e -> ticks = 0;

    /*private boolean skipPacket;

    @EventHandler
    public final Listener<EventSentPacket> eventSendPacketListener = e -> {
        if (!ServerUtil.isHypixel()) return;
        if (e.getPacket() instanceof C03PacketPlayer) {
            if (!skipPacket)
                mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C00PacketKeepAlive(MathUtil.getRandom_int(-10000, 0)));
            skipPacket = false;
        }
    };

    @EventHandler
    public final Listener<EventReceivedPacket> eventReceivedPacketListener = e -> {
        if (e.getPacket() instanceof S08PacketPlayerPosLook) {
            skipPacket = true;
        }
    };*/

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}