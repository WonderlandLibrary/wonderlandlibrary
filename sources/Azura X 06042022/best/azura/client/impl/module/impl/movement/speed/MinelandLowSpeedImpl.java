package best.azura.client.impl.module.impl.movement.speed;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventJump;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventMove;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Speed;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.player.MovementUtil;

public class MinelandLowSpeedImpl implements ModeImpl<Speed> {

    @Override
    public Speed getParent() {
        return (Speed) Client.INSTANCE.getModuleManager().getModule(Speed.class);
    }

    @Override
    public String getName() {
        return "Mineland Low";
    }

    @Override
    public void onEnable() {

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
            if (mc.thePlayer.isMoving()) {
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * MathUtil.getRandom_double(1.4, 1.6));
                    mc.thePlayer.motionY = 0.42F;
                } else {
                    mc.thePlayer.setSpeed(mc.thePlayer.getSpeed());
                    if (mc.thePlayer.motionY > 0 && !mc.gameSettings.keyBindJump.pressed) mc.thePlayer.motionY -= 0.25;
                }
            } else {
                mc.thePlayer.setSpeed(0.);
            }
        }
        if (event instanceof EventMove && mc.thePlayer.isMoving()) {
            final EventMove e = (EventMove) event;
            MovementUtil.setSpeed(Math.max(MovementUtil.getBaseSpeed(), mc.thePlayer.getSpeed()), e);
        }
    }
}