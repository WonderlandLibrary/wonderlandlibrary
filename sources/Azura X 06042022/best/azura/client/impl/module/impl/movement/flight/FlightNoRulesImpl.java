package best.azura.client.impl.module.impl.movement.flight;

import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMove;
import best.azura.client.impl.module.impl.movement.Flight;
import best.azura.client.util.player.MovementUtil;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;

public class FlightNoRulesImpl implements ModeImpl<Flight> {

    private int ticks, stage, flyTicks, tick;
    private double speed, moveSpeed;

    @Override
    public Flight getParent() {
        return (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
    }

    @Override
    public String getName() {
        return "NoRules";
    }

    @Override
    public void onEnable() {
        ticks = flyTicks = stage = tick = 0;
        speed = moveSpeed = 0;
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;
    }

    @EventHandler
    public void onEvent(final Event event) {
        if (event instanceof EventMove) {
            final EventMove e = (EventMove) event;
            switch (ticks) {
                case 0:
                    for (int i = 0; i < 60; i++) {
                        MovementUtil.spoof(0.06, false);
                        MovementUtil.spoof(0, false);
                    }
                    MovementUtil.spoof(0, true);
                    if (mc.thePlayer.isMoving()) MovementUtil.setSpeed(MovementUtil.getBaseSpeed(), e);
                    speed = MovementUtil.getBaseSpeed() * 8.0;
                    ticks++;
                    break;
                case 1:
                    MovementUtil.spoof(-Math.sin(mc.thePlayer.getDirection()) * speed, 0, Math.cos(mc.thePlayer.getDirection()) * speed, true);
                    e.setY(mc.thePlayer.motionY = 0.42);
                    ticks++;
                    break;
                case 2:
                    e.setY(mc.thePlayer.motionY = 0);
                    MovementUtil.vClip(4.3E-8 * (flyTicks++ % 2 == 0 ? 1 : -1));
                    if (!mc.thePlayer.isMoving() || mc.thePlayer.isCollidedHorizontally) speed = 0;
                    if (mc.thePlayer.isMoving()) MovementUtil.setSpeed(Math.max(MovementUtil.getBaseSpeed(), speed *= 0.99), e);
                    else MovementUtil.setSpeed(speed, e);
                    break;
            }
        }
    }
}
