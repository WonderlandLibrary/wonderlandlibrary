package best.azura.client.impl.module.impl.other.disabler;

import best.azura.client.impl.Client;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.other.Disabler;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventReceivedPacket;
import best.azura.client.impl.events.EventSentPacket;
import best.azura.client.impl.events.EventWorldChange;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

import java.util.ArrayList;

public class VerusTestDisablerImpl implements ModeImpl<Disabler> {

    private final ArrayList<Packet<?>> packets = new ArrayList<>();
    private int ticks;
    private boolean silenceNext;

    @Override
    public Disabler getParent() {
        return (Disabler) Client.INSTANCE.getModuleManager().getModule(Disabler.class);
    }

    @Override
    public String getName() {
        return "Blocks MC";
    }

    @Override
    public void onEnable() {
        packets.clear();
        ticks = 0;
    }

    @Override
    public void onDisable() {
        for (final Packet<?> packet : packets) {
            mc.thePlayer.sendQueue.addToSendQueueNoEvent(packet);
        }
        packets.clear();
        ticks = 0;
    }

    @EventHandler
    public final Listener<Event> eventListener = this::handle;

    private void handle(final Event event) {
        if (event instanceof EventWorldChange) {
            packets.clear();
            ticks = 0;
        }
        if (event instanceof EventReceivedPacket) {
            final EventReceivedPacket e = (EventReceivedPacket) event;
            if (e.getPacket() instanceof S08PacketPlayerPosLook && mc.thePlayer.ticksExisted > 10) {
                final S08PacketPlayerPosLook s08 = e.getPacket();
                s08.yaw = mc.thePlayer.rotationYaw;
                s08.pitch = mc.thePlayer.rotationPitch;
                if (silenceNext) {
                    e.setCancelled(true);
                    mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(s08.x, s08.y, s08.z, s08.yaw, s08.pitch, false));
                    silenceNext = false;
                }
            }
        }
        if (event instanceof EventMotion && ticks % 45 == 0) {
            ((EventMotion) event).y -= Math.random();
        }
        if (event instanceof EventSentPacket) {
            final EventSentPacket e = (EventSentPacket) event;
            if (e.getPacket() instanceof C03PacketPlayer) {
                final C03PacketPlayer c03PacketPlayer = e.getPacket();
                if (mc.thePlayer.ticksExisted < 5) {
                    ticks = 0;
                    return;
                }
                if (ticks % 45 == 0) {
                    e.setCancelled(true);
                    c03PacketPlayer.y = -0.753D;
                    c03PacketPlayer.onGround = false;
                    c03PacketPlayer.moving = false;
                    c03PacketPlayer.rotating = false;
                    mc.thePlayer.sendQueue.addToSendQueueNoEvent(c03PacketPlayer);
                    silenceNext = true;
                }
                ticks++;
            }
            if (e.getPacket() instanceof C0BPacketEntityAction) e.setCancelled(true);
            if (e.getPacket() instanceof C00PacketKeepAlive || e.getPacket() instanceof C0FPacketConfirmTransaction) {
                if (e.getPacket() instanceof C00PacketKeepAlive) {
                    final C00PacketKeepAlive c00 = e.getPacket();
                    e.setPacket(new C00PacketKeepAlive(-c00.getKey()));
                }
                while (packets.size() > 350)
                    mc.thePlayer.sendQueue.addToSendQueueNoEvent(packets.remove(0));
                e.setCancelled(true);
                packets.add(e.getPacket());
            }
            if (e.getPacket() instanceof C08PacketPlayerBlockPlacement) {
                final C08PacketPlayerBlockPlacement c08 = e.getPacket();
                c08.stack = null;
            }
        }
    }
}