package best.azura.client.impl.module.impl.movement.flight;

import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.Client;
import best.azura.client.impl.events.*;
import best.azura.client.impl.module.impl.movement.Flight;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.player.MovementUtil;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;

public class FlightSpartanB456Impl implements ModeImpl<Flight> {

    private int flyTicks;

    @Override
    public Flight getParent() {
        return (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
    }

    @Override
    public String getName() {
        return "Spartan Test";
    }

    @EventHandler
    public void onEvent(final Event event) {
        if (event instanceof EventMotion) {
            final EventMotion e = (EventMotion) event;
            MovementUtil.spoof(0, true);
            MovementUtil.spoof(-Math.sin(mc.thePlayer.getDirection()) * 0.005, 0.01, Math.cos(mc.thePlayer.getDirection()) * 0.005, true);
            if (e.isPost()) MovementUtil.spoof(MathUtil.getRandom_double(500, 5000), true);
            MovementUtil.spoof(0, true);
        }
        /*if (event instanceof EventMotion) {
            final EventMotion e = (EventMotion) event;
            if (e.isPre() && mc.thePlayer.ticksExisted % 3 == 0) {
                MovementUtil.spoof(0, mc.thePlayer.onGround);
                MovementUtil.spoof(500, mc.thePlayer.onGround);
                MovementUtil.spoof(0, mc.thePlayer.onGround);
            }
        }
        if (event instanceof EventMove) {
            final EventMove e = (EventMove) event;
            mc.timer.timerSpeed = 0.4f;
            e.setY(mc.thePlayer.motionY = 0);
            if (mc.gameSettings.keyBindSneak.pressed)
                e.setY(mc.thePlayer.motionY -= Flight.speedValue.getObject() * 0.75);
            if (mc.gameSettings.keyBindJump.pressed)
                e.setY(mc.thePlayer.motionY += Flight.speedValue.getObject() * 0.75);
            mc.thePlayer.motionY = 0;
            MovementUtil.setSpeed(mc.thePlayer.isMoving() ? Flight.speedValue.getObject() : 0, e);
        }*/
    }

    @Override
    public void onEnable() {
        flyTicks = 0;
        MovementUtil.spoof(0, true);
        MovementUtil.spoof(-Math.sin(mc.thePlayer.getDirection()) * 0.005, 0.005, Math.cos(mc.thePlayer.getDirection()) * 0.005, true);
        MovementUtil.spoof(MathUtil.getRandom_double(500, 5000), true);
        MovementUtil.spoof(0, true);
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;
    }
}