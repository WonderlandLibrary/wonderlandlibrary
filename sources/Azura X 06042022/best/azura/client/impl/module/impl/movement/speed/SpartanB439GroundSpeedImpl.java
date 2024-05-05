package best.azura.client.impl.module.impl.movement.speed;

import best.azura.client.impl.Client;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventMove;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Speed;
import best.azura.client.util.player.MovementUtil;

public class SpartanB439GroundSpeedImpl implements ModeImpl<Speed> {
    private double speed;

    @Override
    public Speed getParent() {
        return (Speed) Client.INSTANCE.getModuleManager().getModule(Speed.class);
    }

    @Override
    public String getName() {
        return "Spartan B439 Ground";
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
    public final Listener<EventMove> moveListener = e -> {
        if (!mc.thePlayer.isMoving() || mc.thePlayer.isCollidedHorizontally) speed = 0;
        MovementUtil.setSpeed(mc.thePlayer.isMoving() ? Math.max(MovementUtil.getBaseSpeed(), speed) : 0, e);
    };

    @EventHandler
    public final Listener<EventMotion> motionListener = e -> {
        mc.thePlayer.setSpeed(0);
        if (!e.isPre()) return;
        if (mc.thePlayer.onGround && mc.thePlayer.isMoving() && !mc.thePlayer.isRiding()) {
            if (mc.thePlayer.ticksExisted % 3 == 0) {
                e.y += 0.1;
                speed = 0.499;
            } else {
                speed *= 0.999;
            }
        } else speed = 0;
    };
}