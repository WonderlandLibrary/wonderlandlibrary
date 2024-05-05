package best.azura.client.impl.module.impl.movement.speed;

import best.azura.client.impl.Client;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Speed;

public class WatchdogGroundSpeedImpl implements ModeImpl<Speed> {
    private double speed;
    private int ticks;

    @Override
    public Speed getParent() {
        return (Speed) Client.INSTANCE.getModuleManager().getModule(Speed.class);
    }

    @Override
    public String getName() {
        return "Watchdog Ground";
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
        mc.timer.timerSpeed = 1.0f;
    }

    @EventHandler
    public final Listener<EventMotion> motionListener = e -> {
        if (mc.thePlayer.onGround && mc.thePlayer.isMoving()) {
            if (ticks < 15) {
                mc.timer.timerSpeed = Speed.speedValue.getObject().floatValue();
                ticks++;
            } else if (ticks < 22) {
                mc.timer.timerSpeed = 1;
                ticks++;
            } else {
                ticks = 0;
            }
        } else {
            mc.timer.timerSpeed = 1.0f;
            ticks = 0;
        }
        if (!mc.thePlayer.isMoving()) speed = 0;
    };
}