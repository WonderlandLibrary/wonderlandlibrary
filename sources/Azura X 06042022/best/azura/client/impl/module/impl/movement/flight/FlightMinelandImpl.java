package best.azura.client.impl.module.impl.movement.flight;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMove;
import best.azura.client.impl.events.EventUpdate;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Flight;
import best.azura.client.util.player.MovementUtil;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.api.value.Value;

import java.util.Collections;
import java.util.List;

public class FlightMinelandImpl implements ModeImpl<Flight> {

    private int ticks, speedingTicks;
    private final BooleanValue fastValue = new BooleanValue("Fast", "Faster flight by adding a slight speed boost", false);

    @Override
    public List<Value<?>> getValues() {
        return Collections.singletonList(fastValue);
    }

    @Override
    public Flight getParent() {
        return (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
    }

    @Override
    public String getName() {
        return "Mineland";
    }

    @Override
    public void onEnable() {
        speedingTicks = ticks = 0;
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;
        mc.thePlayer.setSpeed(Math.min(0.255, mc.thePlayer.getSpeed()));
        speedingTicks = ticks = 0;
    }

    @EventHandler
    public void onEvent(final Event event) {
        if (event instanceof EventUpdate) {
            switch (ticks) {
                case 0:
                    if (mc.thePlayer.isMoving()) mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed());
                    if (mc.thePlayer.onGround) mc.thePlayer.jump();
                    ticks++;
                    break;
                case 1:
                    if (mc.thePlayer.isMoving() && fastValue.getObject()) mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 1.8);
                    mc.thePlayer.motionY = 0;
                    if (fastValue.getObject()) mc.timer.timerSpeed = 2.0f;
                    ticks++;
                    break;
                case 2:
                    mc.thePlayer.motionY = 0;
                    MovementUtil.vClip(1.0E-10 * (mc.thePlayer.ticksExisted % 2 == 0 ? -1 : 1));
                    if (mc.thePlayer.isMoving()) mc.thePlayer.setSpeed(mc.thePlayer.getSpeed() * (mc.thePlayer.getSpeed() > 0.272 && fastValue.getObject() ? 1.005 : 1.0));
                    else mc.thePlayer.setSpeed(0);
                    mc.timer.timerSpeed = Math.max(1, mc.timer.timerSpeed * 0.99f);
                    if (!fastValue.getObject()) mc.timer.timerSpeed = 1.0f;
                    break;
            }
        }
        if (event instanceof EventMove && mc.thePlayer.isMoving()) {
            final EventMove e = (EventMove) event;
            MovementUtil.setSpeed(Math.max(MovementUtil.getBaseSpeed() - 0.0045, mc.thePlayer.getSpeed()), e);
        }
    }

}