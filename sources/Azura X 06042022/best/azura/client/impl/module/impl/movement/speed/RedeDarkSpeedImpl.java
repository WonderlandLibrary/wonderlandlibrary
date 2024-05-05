package best.azura.client.impl.module.impl.movement.speed;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventUpdate;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventMove;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Speed;
import best.azura.client.util.player.MovementUtil;

public class RedeDarkSpeedImpl implements ModeImpl<Speed> {
    private double speed;

    @Override
    public Speed getParent() {
        return (Speed) Client.INSTANCE.getModuleManager().getModule(Speed.class);
    }

    @Override
    public String getName() {
        return "RedeDark";
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
    public final Listener<EventUpdate> eventUpdateListener = e -> {
        if(mc.thePlayer.onGround && !mc.thePlayer.isCollidedHorizontally) {
            mc.thePlayer.motionY = 0.42F;
        }
    };

    @EventHandler
    public final Listener<EventMove> moveListener = e -> {
        if (mc.thePlayer.isMoving() && !mc.thePlayer.isCollidedHorizontally) {
            double speed = 0;
            for (int i = 1; i <= Speed.speedValue.getObject() * 10; i++) {
                speed += 0.1;
                double yaw = mc.thePlayer.getDirection();
                double x = -Math.sin(yaw) * speed;
                double z = Math.cos(yaw) * speed;
                if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(x, 0, z)).isEmpty())
                    break;
                if(!(mc.thePlayer.fallDistance > 1.1) && !mc.thePlayer.isCollidedHorizontally) MovementUtil.spoof(x, 0, z, true);
            }
            if(!(mc.thePlayer.fallDistance > 1.1) && !mc.thePlayer.isCollidedHorizontally) MovementUtil.setSpeed(speed, e);
            if(mc.thePlayer.fallDistance > 1.1 || mc.thePlayer.isCollidedHorizontally) MovementUtil.setSpeed(0, e);
        }
    };
}