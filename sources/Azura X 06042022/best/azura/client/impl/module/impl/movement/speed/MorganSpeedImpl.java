package best.azura.client.impl.module.impl.movement.speed;

import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMove;
import best.azura.client.impl.module.impl.movement.Speed;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;

public class MorganSpeedImpl implements ModeImpl<Speed> {

    @EventHandler
    public final Listener<EventMove> onMove = e -> {
		if (!mc.gameSettings.keyBindJump.pressed && mc.thePlayer.isMoving()) {
			if (mc.thePlayer.onGround) e.setY(mc.thePlayer.motionY = 0.05f);
			if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0, -0.1, 0)).isEmpty())
                mc.thePlayer.setSpeed(Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ) + 0.3);
		}
    };

    @Override
    public Speed getParent() {
        return (Speed) Client.INSTANCE.getModuleManager().getModule(Speed.class);
    }

    @Override
    public String getName() {
        return "Morgan";
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;
    }
}