package best.azura.client.impl.module.impl.other.disabler;

import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventReceivedPacket;
import best.azura.client.impl.events.EventSentPacket;
import best.azura.client.impl.events.EventUpdate;
import best.azura.client.impl.events.EventWorldChange;
import best.azura.client.impl.module.impl.other.Disabler;
import best.azura.client.util.other.DelayUtil;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

import java.util.ArrayList;

public class MorganDisabler implements ModeImpl<Disabler> {

    private final ArrayList<Packet<?>> packets = new ArrayList<>();
    private final DelayUtil delayUtil = new DelayUtil();
    private boolean modifyC06;

    @Override
    public Disabler getParent() {
        return (Disabler) Client.INSTANCE.getModuleManager().getModule(Disabler.class);
    }

    @Override
    public String getName() {
        return "Morgan Semi";
    }

    @Override
    public void onEnable() {
        packets.clear();
    }

    @Override
    public void onDisable() {
        for (final Packet<?> packet : packets) {
            mc.thePlayer.sendQueue.addToSendQueueNoEvent(packet);
        }
        packets.clear();
    }

    @EventHandler
    public final Listener<Event> eventListener = this::handle;

    private void handle(final Event event) {
        if (event instanceof EventWorldChange) {
            packets.clear();
            delayUtil.reset();
        }
        if (event instanceof EventUpdate) {
            while (packets.size() > 220) mc.thePlayer.sendQueue.addToSendQueueNoEvent(packets.remove(0));
            StringBuilder builder = new StringBuilder();
            for (int i = 32; i < 256; i++) builder.append((char)i);
            mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C19PacketResourcePackStatus(builder.toString(), C19PacketResourcePackStatus.Action.ACCEPTED));
            mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C19PacketResourcePackStatus(builder.toString(), C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
        }
        if (event instanceof EventReceivedPacket) {
            final EventReceivedPacket e = (EventReceivedPacket) event;
            if (e.getPacket() instanceof S08PacketPlayerPosLook)
                modifyC06 = true;
        }
        if (event instanceof EventSentPacket) {
            final EventSentPacket e = (EventSentPacket) event;
            if (e.isCancelled()) return;
            if (e.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook && modifyC06) {
                final C03PacketPlayer c03 = e.getPacket();
                e.setPacket(new C03PacketPlayer.C04PacketPlayerPosition(c03.x, c03.y, c03.z, c03.onGround));
                modifyC06 = false;
            }
            if (e.getPacket() instanceof C00PacketKeepAlive || e.getPacket() instanceof C0FPacketConfirmTransaction) {
                e.setCancelled(true);
                packets.add(e.getPacket());
            }
        }
    }
}