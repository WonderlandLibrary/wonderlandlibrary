package best.azura.client.impl.module.impl.other.disabler;

import best.azura.client.impl.Client;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventSentPacket;
import best.azura.client.impl.events.EventUpdate;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.other.Disabler;
import best.azura.client.util.other.DelayUtil;
import best.azura.client.util.other.ServerUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;

import java.net.Socket;
import java.util.ArrayList;

public class IntroubleDisablerImpl implements ModeImpl<Disabler> {

    private long ping = -1;
    private final DelayUtil delay = new DelayUtil();
    private final ArrayList<Packet<?>> packets = new ArrayList<>();

    @Override
    public Disabler getParent() {
        return (Disabler) Client.INSTANCE.getModuleManager().getModule(Disabler.class);
    }

    @Override
    public String getName() {
        return "Introuble";
    }

    @EventHandler
    public final Listener<EventUpdate> eventUpdateListener = e -> {
        if (delay.hasReached(5000) || ping == -1) {
            new Thread(() -> {
                long startTime = System.currentTimeMillis();
                try {
                    Socket socket = new Socket(ServerUtil.lastIP, ServerUtil.lastPort);
                    if (socket.isConnected()) {
                        ping = (System.currentTimeMillis() - startTime);
                        socket.close();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }).start();
            delay.reset();
        }
        if (ping != -1) {
            if (packets.size() > ping) {
                packets.forEach(mc.thePlayer.sendQueue::addToSendQueueNoEvent);
                packets.clear();
            }
        }
    };

    @EventHandler
    public final Listener<EventSentPacket> eventSendPacketListener = e -> {
        if (e.getPacket() instanceof C0FPacketConfirmTransaction) {
            C0FPacketConfirmTransaction c0f = e.getPacket();
            if (c0f.getUid() < 0 && ping != -1) {
                packets.add(e.getPacket());
                e.setCancelled(true);
            }
        } else if (e.getPacket() instanceof C00PacketKeepAlive || e.getPacket() instanceof C0BPacketEntityAction || e.getPacket() instanceof C17PacketCustomPayload) {
            e.setCancelled(true);
        }
    };

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        ping = -1;
    }
}