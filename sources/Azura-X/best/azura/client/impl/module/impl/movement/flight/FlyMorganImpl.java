package best.azura.client.impl.module.impl.movement.flight;

import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMove;
import best.azura.client.impl.module.impl.movement.Flight;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;

public class FlyMorganImpl implements ModeImpl<Flight> {

    @Override
    public Flight getParent() {
        return (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
    }

    @Override
    public String getName() {
        return "Morgan";
    }

    @EventHandler
    public final Listener<EventMove> onMove = e -> {
        e.setY(mc.thePlayer.motionY = mc.thePlayer.motionY > 0 ? -1.0E-10 : 0.0);
        if (mc.thePlayer.onGround) {
            mc.thePlayer.jump();
            e.setY(mc.thePlayer.motionY = 0.1f);
        }
    };

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;
    }
}