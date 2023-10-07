package best.azura.client.impl.module.impl.movement.flight;

import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMove;
import best.azura.client.impl.module.impl.movement.Flight;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.player.MovementUtil;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import net.minecraft.potion.Potion;

public class FlightWatchduckImpl implements ModeImpl<Flight> {
    private int stage;

    @Override
    public Flight getParent() {
        return (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
    }

    @Override
    public String getName() {
        return "Watchduck";
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;
        stage = 0;
    }

    @EventHandler
    public final Listener<Event> event = this::onEvent;

    private void onEvent(final Event event) {
        if (event instanceof EventMove) {
            EventMove e = (EventMove) event;
            switch (stage) {
                case 0:
                    mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 2);
                    if (mc.thePlayer.isPotionActive(Potion.moveSpeed))
                        mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 3);
                    e.setY(mc.thePlayer.motionY = (float) 0.42D);
                    stage++;
                    break;
                case 1:
                    e.setY(mc.thePlayer.motionY = MathUtil.getRandom_double(2.0E-8D, 2.0E-4D));
                    if (mc.thePlayer.isMoving())
                        MovementUtil.setSpeed(Math.max(MovementUtil.getBaseSpeed() + 0.45 - 0.2873, mc.thePlayer.getSpeed() * 1.2), e);
                    else MovementUtil.setSpeed(0, e);
                    break;
            }
        }
    }
}