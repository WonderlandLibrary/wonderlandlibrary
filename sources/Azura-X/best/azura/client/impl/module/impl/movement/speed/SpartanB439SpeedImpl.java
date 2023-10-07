package best.azura.client.impl.module.impl.movement.speed;

import best.azura.client.impl.Client;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventMove;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Speed;
import best.azura.client.util.player.MovementUtil;

public class SpartanB439SpeedImpl implements ModeImpl<Speed> {
    private double speed;

    @Override
    public Speed getParent() {
        return (Speed) Client.INSTANCE.getModuleManager().getModule(Speed.class);
    }

    @Override
    public String getName() {
        return "Spartan B439";
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
        if (!e.isPre()) return;
        if (mc.thePlayer.onGround && mc.thePlayer.isMoving()) {
            speed = 0.499;
            mc.thePlayer.jump();
        }
        if (!mc.thePlayer.onGround && mc.thePlayer.isMoving() && mc.thePlayer.motionY < 0.8 && mc.thePlayer.fallDistance < 1) {
            if (mc.thePlayer.motionY == 0.2403599859094576) speed = 0.5;
            mc.thePlayer.motionY -= 0.5;
        }
        if (!mc.thePlayer.isMoving()) speed = 0;
        mc.thePlayer.setSpeed(0);
        speed *= 0.999999999999;
    };
}