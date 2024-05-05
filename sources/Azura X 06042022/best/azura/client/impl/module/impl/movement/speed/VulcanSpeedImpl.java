package best.azura.client.impl.module.impl.movement.speed;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMotion;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Speed;

public class VulcanSpeedImpl implements ModeImpl<Speed> {


    @Override
    public Speed getParent() {
        return (Speed) Client.INSTANCE.getModuleManager().getModule(Speed.class);
    }

    @Override
    public String getName() {
        return "Vulcan";
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        mc.thePlayer.setSpeed(0);
    }

    @EventHandler
    private void onEvent(final Event event) {
        if (event instanceof EventMotion) {
            final EventMotion e = (EventMotion) event;
            if (!e.isPre()) {
                mc.thePlayer.posY = mc.thePlayer.lastTickPosY;
                return;
            }
            if (!mc.thePlayer.isMoving()) {
                mc.thePlayer.setSpeed(0);
                return;
            }
            if (mc.thePlayer.onGround) {
                mc.thePlayer.jump();
                mc.thePlayer.setSpeed(0);
            } else {
                if (mc.thePlayer.motionY > 0 && mc.thePlayer.motionY < 0.3 && mc.thePlayer.fallDistance <= 0) e.y -= 1.1F;
                else if (mc.thePlayer.motionY == -0.0784000015258789 && mc.thePlayer.fallDistance <= 0.1 && !mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
                        mc.thePlayer.getEntityBoundingBox().offset(0, -1.1, 0)).isEmpty()) {
                    mc.thePlayer.setSpeed(Speed.speedValue.getObject());
                }
            }
        }
    }

}