package best.azura.client.impl.module.impl.movement.speed;

import best.azura.client.impl.Client;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventMove;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Speed;
import best.azura.client.util.player.MovementUtil;

@SuppressWarnings("unused")
public class WatchdogMiniSpeedImpl implements ModeImpl<Speed> {
    private double speed;
    private int ticks;
    private boolean lowHop;

    @Override
    public Speed getParent() {
        return (Speed) Client.INSTANCE.getModuleManager().getModule(Speed.class);
    }

    @Override
    public String getName() {
        return "Watchdog Mini";
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
    };

    @EventHandler
    public final Listener<EventMotion> motionListener = e -> {
        if (!e.isPre()) return;
        if (mc.thePlayer.onGround) {
            if (mc.thePlayer.isMoving()) {
                if (lowHop) {
                    if (mc.thePlayer.ticksExisted % 4 == 0) {
                        mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed());
                        e.y += 0.1;
                    }
                }
            }
        }
        if (!mc.thePlayer.isMoving()) speed = 0;
    };
}