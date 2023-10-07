package best.azura.client.impl.module.impl.movement.speed;

import best.azura.client.impl.Client;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Speed;
import best.azura.client.util.player.MovementUtil;

public class NCPSpeedImpl implements ModeImpl<Speed> {
    private double speed;
    private int ticks = 0;

    @Override
    public Speed getParent() {
        return (Speed) Client.INSTANCE.getModuleManager().getModule(Speed.class);
    }

    @Override
    public String getName() {
        return "NCP";
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
        switch (ticks) {
            case 0:
            case 1:
                if (mc.thePlayer.isMoving()) {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                        ticks++;
                    }
                }

            case 2:
                if (mc.thePlayer.isMoving()) {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                        mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() + 0.2);
                        mc.timer.timerSpeed = 1.07f;
                    } else {
                        mc.thePlayer.setSpeed(mc.thePlayer.getSpeed());
                    }
                }
                break;


        }

    };
}