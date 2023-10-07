package best.azura.client.impl.module.impl.movement.flight;

import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.module.impl.movement.Flight;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;

public class FlightVulcanImpl implements ModeImpl<Flight> {

    @Override
    public Flight getParent() {
        return (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
    }

    @Override
    public String getName() {
        return "Vulcan Glide";
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;
    }

    @EventHandler
    public void onEvent(final Event event) {
        if (event instanceof EventMotion) {
            if (mc.thePlayer.fallDistance > 0.15) {
                ((EventMotion) event).onGround = true;
                mc.thePlayer.motionY = 0;
                mc.thePlayer.fallDistance = 0;
            }
        }
    }
}