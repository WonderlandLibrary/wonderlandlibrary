package best.azura.client.impl.module.impl.movement.speed;

import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventJump;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventMove;
import best.azura.client.impl.module.impl.movement.Speed;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.player.MovementUtil;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;

public class MercurySpeedImpl implements ModeImpl<Speed> {

    private double speed;

    @Override
    public Speed getParent() {
        return (Speed) Client.INSTANCE.getModuleManager().getModule(Speed.class);
    }

    @Override
    public String getName() {
        return "Mercury";
    }

    @Override
    public void onEnable() {
        speed = 0;
    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public void onEvent(final Event event) {
        if (event instanceof EventJump) {
            final EventJump e = (EventJump) event;
            e.setSpeed(0);
        }
        if (event instanceof EventMotion) {
            final EventMotion e = (EventMotion) event;
            if (mc.thePlayer.isCollidedHorizontally)
                speed = 0;
            if (mc.thePlayer.isMoving()) {
                if (mc.thePlayer.onGround) {
                    speed += 0.1;
                    mc.thePlayer.motionY = 0.42F;
                } else {
                    //if (mc.thePlayer.motionY > 0 && !mc.gameSettings.keyBindJump.pressed) mc.thePlayer.motionY -= 0.25;
                    speed *= 0.992;
                }
            } else {
                mc.thePlayer.setSpeed(speed = 0.);
            }
        }
        if (event instanceof EventMove && mc.thePlayer.isMoving()) {
            final EventMove e = (EventMove) event;
            MovementUtil.setSpeed(Math.max(MovementUtil.getBaseSpeed(), speed), e);
        }
    }
}