package best.azura.client.impl.module.impl.movement.flight;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventUpdate;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Flight;
import best.azura.client.util.player.MovementUtil;

public class FlightOldNCPGlideImpl implements ModeImpl<Flight> {

    @Override
    public Flight getParent() {
        return (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
    }

    @Override
    public String getName() {
        return "Old NCP Glide";
    }

    @Override
    public void onEnable() {
        MovementUtil.vClip(1.0E-10);
    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public void onEvent(final Event event) {
        if (event instanceof EventUpdate) {
            mc.thePlayer.motionY = 0;
            MovementUtil.vClip(1.0E-10 * (mc.thePlayer.ticksExisted % 2 == 0 ? -1 : 1));
        }
    }

}