package best.azura.client.impl.module.impl.movement.speed;

import best.azura.client.impl.Client;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventMove;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Speed;
import best.azura.client.util.player.MovementUtil;

public class WatchdogFastSpeedImpl implements ModeImpl<Speed> {
    private double speed;
    private int ticks;

    @Override
    public Speed getParent() {
        return (Speed) Client.INSTANCE.getModuleManager().getModule(Speed.class);
    }

    @Override
    public String getName() {
        return "Watchdog Fast";
    }

    @Override
    public void onEnable() {
        Client.INSTANCE.getEventBus().register(this);
        speed = 0;
        ticks = 0;
    }

    @Override
    public void onDisable() {
        Client.INSTANCE.getEventBus().unregister(this);
        speed = 0;
        ticks = 0;
    }

    @EventHandler
    public final Listener<EventMove> moveListener = e -> {
        if (!mc.thePlayer.isMoving() || mc.thePlayer.isCollidedHorizontally) speed = 0;
        MovementUtil.setSpeed(mc.thePlayer.isMoving() ? Math.max(MovementUtil.getBaseSpeed(), mc.thePlayer.getSpeed()) : 0, e);
    };

    @EventHandler
    public final Listener<EventMotion> motionListener = e -> {
        if (mc.thePlayer.onGround) {
            if (mc.thePlayer.isMoving()) {
                if (mc.gameSettings.keyBindJump.isKeyDown()) return;
                mc.thePlayer.jump();
                mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 1.5);
                if (ticks > 0) mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 1.77);
                ticks++;
            }
        } else if (mc.thePlayer.isMoving()) {
            if (mc.thePlayer.motionY > 0.05 && mc.thePlayer.motionY < 0.15) mc.thePlayer.motionY = (float) -0.01;
            if (mc.thePlayer.motionY > -0.07 && mc.thePlayer.motionY < 0.) mc.thePlayer.motionY = (float) -0.09;
        }
        if (!mc.thePlayer.isMoving()) speed = 0;
    };
}