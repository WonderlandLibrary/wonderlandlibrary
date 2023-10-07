package best.azura.client.impl.module.impl.other.disabler;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventSentPacket;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.other.Disabler;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;

public class VerusScaffoldDisabler implements ModeImpl<Disabler> {

    @Override
    public Disabler getParent() {
        return (Disabler) Client.INSTANCE.getModuleManager().getModule(Disabler.class);
    }

    @Override
    public String getName() {
        return "Verus Scaffold";
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public final Listener<Event> eventListener = this::handle;

    private void handle(final Event event) {
        if (event instanceof EventSentPacket) {
            final EventSentPacket e = (EventSentPacket) event;
            if (e.isCancelled()) return;
            if (e.getPacket() instanceof C08PacketPlayerBlockPlacement) {
                final C08PacketPlayerBlockPlacement c08 = e.getPacket();
                c08.stack = null;
            }
        }
    }
}