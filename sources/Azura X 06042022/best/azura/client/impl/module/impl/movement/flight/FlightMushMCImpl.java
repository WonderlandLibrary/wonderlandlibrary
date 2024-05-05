package best.azura.client.impl.module.impl.movement.flight;

import best.azura.client.impl.Client;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventMove;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Flight;
import best.azura.client.util.player.MovementUtil;

public class FlightMushMCImpl implements ModeImpl<Flight> {
    private double speed;
    private double y;

    @Override
    public Flight getParent() {
        return (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
    }

    @Override
    public String getName() {
        return "MushMC";
    }

    @Override
    public void onEnable() {
        Client.INSTANCE.getEventBus().register(this);
        speed = 0;
    }

    @Override
    public void onDisable() {
        Client.INSTANCE.getEventBus().unregister(this);
        speed = 0;
    }

    @EventHandler
    public final Listener<EventMotion> eventMotionListener = e -> {
        y += mc.thePlayer.ticksExisted % 4 == 0 ? 0.0625 * 3 : -0.0625;
        if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox()
                .offset(0, y, 0)).isEmpty()) y = 0;
        e.y += y;
        e.onGround = true;
    };

    @EventHandler
    public final Listener<EventMove> moveListener = e -> {
        e.setY(mc.thePlayer.motionY = 0);
        if (mc.gameSettings.keyBindSneak.pressed) e.setY(mc.thePlayer.motionY -= Flight.speedValue.getObject() * 0.75);
        if (mc.gameSettings.keyBindJump.pressed) e.setY(mc.thePlayer.motionY += Flight.speedValue.getObject() * 0.75);
        if (mc.thePlayer.isMoving()) MovementUtil.setSpeed(Flight.speedValue.getObject(), e);
        else MovementUtil.setSpeed(speed = 0, e);
    };
}