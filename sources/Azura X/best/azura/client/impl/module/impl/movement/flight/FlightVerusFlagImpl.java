package best.azura.client.impl.module.impl.movement.flight;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventMove;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Flight;
import best.azura.client.util.player.MovementUtil;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.api.value.Value;

import java.util.Arrays;
import java.util.List;

public class FlightVerusFlagImpl implements ModeImpl<Flight> {

    private final BooleanValue fast = new BooleanValue("Zoom", "Go faster", false);
    private final BooleanValue vertical = new BooleanValue("Vertical", "Move up or down", false);

    @Override
    public List<Value<?>> getValues() {
        return Arrays.asList(fast, vertical);
    }

    @Override
    public Flight getParent() {
        return (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
    }

    @Override
    public String getName() {
        return "Verus Flag";
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public final Listener<Event> eventListener = this::handle;

    private void handle(final Event event) {
        if (event instanceof EventMotion) {
            EventMotion e = (EventMotion) event;
            if (mc.thePlayer.ticksExisted % 4 == 0) {
                mc.thePlayer.motionY = 0;
                e.onGround = true;

                    e.z += 100;
                    e.x += 100;

            }
            e.y += 0.1;
            if (vertical.getObject()) {
                if (mc.gameSettings.keyBindJump.pressed) e.y += 1;
                if (mc.gameSettings.keyBindSneak.pressed) e.y -= 1;
            }
            if (mc.thePlayer.isMoving() && fast.getObject()) {
                e.x -= Math.sin(mc.thePlayer.getDirection());
                e.z += Math.cos(mc.thePlayer.getDirection());
            }
        }
    }


    @EventHandler
    public final Listener<EventMove> moveListener = e -> {
        if(mc.thePlayer.isMoving()) MovementUtil.setSpeed(fast.getObject() ? 0.1 : 0.3, e);
    };

}