package best.azura.client.impl.module.impl.movement.flight;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMotion;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Flight;
import best.azura.client.impl.module.impl.movement.NoFall;
import best.azura.client.util.math.MathUtil;

public class FlightWatchdogGlideImpl implements ModeImpl<Flight> {

    @Override
    public Flight getParent() {
        return (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
    }

    @Override
    public String getName() {
        return "Watchdog Glide";
    }

    @Override
    public void onEnable() {
        ((NoFall)Client.INSTANCE.getModuleManager().getModule(NoFall.class)).disableAntiVoid = true;
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;
        ((NoFall)Client.INSTANCE.getModuleManager().getModule(NoFall.class)).disableAntiVoid = false;
    }

    @EventHandler
    public final Listener<Event> event = this::onEvent;

    private void onEvent(final Event event) {
        /*if (event instanceof EventUpdate) {
            if (mc.thePlayer.fallDistance > 2.3) {
                mc.thePlayer.setSpeed(2.0);
                mc.thePlayer.motionY = (float)0.42;
                mc.thePlayer.fallDistance = 0;
            }
        }*/
        if (event instanceof EventMotion) {
            EventMotion e = (EventMotion) event;
            if (e.isPre()) {
                mc.thePlayer.motionY = 0;
                e.y -= MathUtil.getRandom_double(1.75, 1.76);
                double yaw = mc.thePlayer.getDirection();
                double x = -Math.sin(yaw) * Flight.speedValue.getObject();
                double z = Math.cos(yaw) * Flight.speedValue.getObject();
                e.x += x;
                e.z += z;
                mc.thePlayer.posX = mc.thePlayer.lastTickPosX;
                mc.thePlayer.posY = mc.thePlayer.lastTickPosY;
                mc.thePlayer.posZ = mc.thePlayer.lastTickPosZ;
            }
        }
    }
}