package best.azura.client.impl.module.impl.other.disabler;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventSentPacket;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.other.Disabler;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C17PacketCustomPayload;

public class GommeDisablerImpl implements ModeImpl<Disabler> {

    private int c0fAmount;

    @Override
    public Disabler getParent() {
        return (Disabler) Client.INSTANCE.getModuleManager().getModule(Disabler.class);
    }

    @Override
    public String getName() {
        return "Gomme";
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public final Listener<EventSentPacket> eventSentPacketListener = e -> {
        if (mc.thePlayer.ticksExisted < 2) c0fAmount = 0;
        if (e.getPacket() instanceof C0FPacketConfirmTransaction) {
            final C0FPacketConfirmTransaction c0f = e.getPacket();
            c0fAmount++;
            if (c0fAmount < 3) return;
            e.setPacket(new C0FPacketConfirmTransaction(c0f.getWindowId(), c0f.getUid(), false));
            if (c0fAmount % 13 == 0) {
                e.setPacket(new C0FPacketConfirmTransaction(c0f.getWindowId(), (short) -c0f.getUid(), false));
            }
        }
        if (e.getPacket() instanceof C17PacketCustomPayload) {
            final C17PacketCustomPayload c17 = e.getPacket();
            if (c17.getChannelName().equals("MC|Brand"))
                e.setPacket(new C17PacketCustomPayload(c17.getChannelName(), new PacketBuffer(Unpooled.buffer()).writeString("blc")));
        }
    };

}