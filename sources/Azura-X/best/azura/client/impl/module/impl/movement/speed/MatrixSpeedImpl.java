package best.azura.client.impl.module.impl.movement.speed;

import best.azura.client.impl.Client;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Speed;

public class MatrixSpeedImpl implements ModeImpl<Speed> {
    private double speed;

    @Override
    public Speed getParent() {
        return (Speed) Client.INSTANCE.getModuleManager().getModule(Speed.class);
    }

    @Override
    public String getName() {
        return "Matrix";
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
    public final Listener<EventMotion> motionListener = e -> {
        if (!e.isPre()) return;
        if (mc.thePlayer.onGround) {
            speed = 1.2;
            mc.thePlayer.jump();
            mc.thePlayer.setSpeed(mc.thePlayer.getSpeed());
        } else {
            mc.thePlayer.motionX *= speed;
            mc.thePlayer.motionZ *= speed;
            speed = 1;
        }
    };
}