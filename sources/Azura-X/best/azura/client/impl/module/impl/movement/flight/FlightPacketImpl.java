package best.azura.client.impl.module.impl.movement.flight;

import best.azura.client.impl.Client;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventMove;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Flight;
import best.azura.client.util.player.MovementUtil;

public class FlightPacketImpl implements ModeImpl<Flight> {

    @Override
    public Flight getParent() {
        return (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
    }

    @Override
    public String getName() {
        return "Packet";
    }

    @Override
    public void onEnable() {
        Client.INSTANCE.getEventBus().register(this);
    }

    @Override
    public void onDisable() {
        Client.INSTANCE.getEventBus().unregister(this);
    }


    @EventHandler
    public final Listener<EventMove> moveListener = e -> {
        if (mc.thePlayer.isMoving()) {
            double speed = 0;
            for (int i = 1; i <= 10; i++) {
                speed += 0.1;
                double yaw = mc.thePlayer.getDirection();
                double x = -Math.sin(yaw) * speed;
                double z = Math.cos(yaw) * speed;
                MovementUtil.spoof(x, 0, z, true);
            }
            MovementUtil.setSpeed(speed, e);
        }
    };
}