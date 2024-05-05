package best.azura.client.impl.module.impl.other.disabler;

import best.azura.client.impl.Client;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventUpdate;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.other.Disabler;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;

public class ResourceDisablerImpl implements ModeImpl<Disabler> {

    @Override
    public Disabler getParent() {
        return (Disabler) Client.INSTANCE.getModuleManager().getModule(Disabler.class);
    }

    @Override
    public String getName() {
        return "Resourcepack";
    }

    @EventHandler
    public final Listener<EventUpdate> eventUpdateListener = e -> {
        StringBuilder builder = new StringBuilder();
        for (int i = 32; i < 256; i++) builder.append((char)i);
        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C19PacketResourcePackStatus(builder.toString(), C19PacketResourcePackStatus.Action.ACCEPTED));
        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C19PacketResourcePackStatus(builder.toString(), C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
    };

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}