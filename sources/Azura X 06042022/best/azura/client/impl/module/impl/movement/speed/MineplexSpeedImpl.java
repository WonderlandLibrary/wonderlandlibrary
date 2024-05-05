package best.azura.client.impl.module.impl.movement.speed;

import best.azura.client.impl.Client;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventMove;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Speed;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.player.MovementUtil;

public class MineplexSpeedImpl implements ModeImpl<Speed> {
    private float speed;

    @Override
    public Speed getParent() {
        return (Speed) Client.INSTANCE.getModuleManager().getModule(Speed.class);
    }

    @Override
    public String getName() {
        return "Mineplex";
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
        e.yaw = MathUtil.getRandom_float(-180, 180);
        e.pitch = MathUtil.getRandom_float(-90, 90);
        mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
        mc.thePlayer.setPosition(mc.thePlayer.posX - mc.thePlayer.motionX, mc.thePlayer.posY, mc.thePlayer.posZ - mc.thePlayer.motionZ);
        if (mc.thePlayer.onGround) {
            if (mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0) {
                speed = (float) Math.min(Speed.speedValue.getObject(), speed + 0.1);
                if (speed < 0.65) speed = (float) 0.65;
                MovementUtil.setSpeed(true, -speed);
                mc.thePlayer.motionY = 0.42f;
            }
        } else {
            speed *= 0.99;
        }
        if (!mc.thePlayer.isMoving()) speed = 0;
        speed = Math.min(Speed.speedValue.getObject(), speed);
        if (mc.thePlayer.moveForward > 0) {
            mc.thePlayer.setSprinting(true);
        }
    };
}