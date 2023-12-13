package best.azura.client.impl.module.impl.other.disabler;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventSentPacket;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.other.Disabler;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.api.value.Value;
import net.minecraft.network.play.client.C00PacketKeepAlive;

import java.util.Collections;
import java.util.List;

public class KeepaliveDisablerImpl implements ModeImpl<Disabler> {

    private final BooleanValue negateId = new BooleanValue("Negate key", "Negate keepalive keys", false);

    @Override
    public Disabler getParent() {
        return (Disabler) Client.INSTANCE.getModuleManager().getModule(Disabler.class);
    }

    @Override
    public List<Value<?>> getValues() {
        return Collections.singletonList(negateId);
    }

    @Override
    public String getName() {
        return "Keep Alive";
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public final Listener<EventSentPacket> eventSentPacketListener = e -> {
        if (e.getPacket() instanceof C00PacketKeepAlive) {
            final C00PacketKeepAlive c00 = e.getPacket();
            e.setPacket(new C00PacketKeepAlive(c00.getKey() * (negateId.getObject() ? -1 : 1)));
        }
    };

}