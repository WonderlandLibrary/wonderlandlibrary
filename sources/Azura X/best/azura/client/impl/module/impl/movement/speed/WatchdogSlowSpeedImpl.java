package best.azura.client.impl.module.impl.movement.speed;

import best.azura.client.impl.Client;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventMove;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Speed;
import best.azura.client.util.player.MovementUtil;

public class WatchdogSlowSpeedImpl implements ModeImpl<Speed> {
    private double speed;
    private int ticks;

    @Override
    public Speed getParent() {
        return (Speed) Client.INSTANCE.getModuleManager().getModule(Speed.class);
    }

    @Override
    public String getName() {
        return "Watchdog Slow";
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
                switch (ticks) {
                    case 0:
                        mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 1.63);
                        ticks++;
                        break;
                    case 1:
                        mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 1.75);
                        ticks++;
                        break;
                    case 2:
                        mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 1.67);
                        ticks++;
                        break;
                    case 3:
                        mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 1.74);
                        ticks++;
                        break;
                    case 4:
                    case 5:
                        mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 1.7);
                        ticks++;
                        break;
                    case 6:
                    case 7:
                        mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 1.64);
                        ticks++;
                        break;
                    case 8:
                        mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 1.68);
                        ticks = 0;
                        break;
                }
            }
        }
        if (!mc.thePlayer.isMoving()) speed = 0;
    };
}