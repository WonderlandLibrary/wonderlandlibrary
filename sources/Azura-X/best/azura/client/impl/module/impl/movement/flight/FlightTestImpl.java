package best.azura.client.impl.module.impl.movement.flight;

import best.azura.client.impl.Client;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Flight;

@SuppressWarnings("unused")
public class FlightTestImpl implements ModeImpl<Flight> {
    private double speed, boostSpeed;
    private int tick, stage, state, ticks;
    private boolean boosted, flyBoosted;

    @Override
    public Flight getParent() {
        return (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
    }

    @Override
    public String getName() {
        return "Test";
    }

    @Override
    public void onEnable() {
        Client.INSTANCE.getEventBus().register(this);
        speed = 0;
        tick = ticks = 0;
        boosted = false;
        boostSpeed = 0;
        flyBoosted = false;
    }

    @Override
    public void onDisable() {
        Client.INSTANCE.getEventBus().unregister(this);
        speed = 0;
        mc.timer.timerSpeed = 1.0f;
        mc.thePlayer.capabilities.isFlying = false;
    }

    @EventHandler
    public final Listener<EventMotion> eventMotionListener = e -> {
        if (e.isPre()) {
            mc.timer.timerSpeed = 0.5f;
            mc.thePlayer.capabilities.isFlying = true;
        }
    };
}