package best.azura.client.impl.module.impl.other.disabler;

import best.azura.client.api.module.ModeImpl;
import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventReceivedPacket;
import best.azura.client.impl.events.EventSentPacket;
import best.azura.client.impl.events.EventWorldChange;
import best.azura.client.impl.module.impl.other.Disabler;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.player.MovementUtil;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

import java.util.ArrayList;

public class HycraftDisablerImpl implements ModeImpl<Disabler> {

    private final ArrayList<Packet<?>> packets = new ArrayList<>();
    private int ticks, lags;
    private boolean silenceNext;

    @Override
    public Disabler getParent() {
        return (Disabler) Client.INSTANCE.getModuleManager().getModule(Disabler.class);
    }

    @Override
    public String getName() {
        return "Hycraft";
    }

    @Override
    public void onEnable() {
        ticks = lags = 0;
        silenceNext = false;
    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    private void onEvent(final Event event) {
        if (event instanceof EventWorldChange) {
            ticks = lags = 0;
            silenceNext = false;
        }
        if (mc.thePlayer == null || mc.thePlayer.ticksExisted < 40) return;
        if (event instanceof EventReceivedPacket) {
            final EventReceivedPacket e = (EventReceivedPacket) event;
            if (e.getPacket() instanceof S08PacketPlayerPosLook && silenceNext) {
                final S08PacketPlayerPosLook s08 = e.getPacket();
                if (mc.thePlayer.getDistance(s08.x, s08.y, s08.z) < 8) {
                    e.setCancelled(true);
                    mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(s08.x, s08.y, s08.z, s08.yaw, s08.pitch, false));
                }
                silenceNext = false;
            }
        }
        if (event instanceof EventSentPacket) {
            final EventSentPacket e = (EventSentPacket) event;
            if (e.getPacket() instanceof C00PacketKeepAlive || e.getPacket() instanceof C0FPacketConfirmTransaction) {
                mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C00PacketKeepAlive());
                mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0FPacketConfirmTransaction());
            }
        }
        if (event instanceof EventMotion) {
            final EventMotion e = (EventMotion) event;
            if (e.isPre() && ++ticks % 15 == 0) {
                e.y += 1000 + MathUtil.getRandom_double(0.5, 1.0);
                e.onGround = true;
                silenceNext = true;
                lags++;
            }
        }
    }
}