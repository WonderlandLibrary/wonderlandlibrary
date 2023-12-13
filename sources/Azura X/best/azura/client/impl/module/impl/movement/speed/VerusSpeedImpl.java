package best.azura.client.impl.module.impl.movement.speed;

import best.azura.client.impl.Client;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Speed;
import best.azura.client.util.player.MovementUtil;

public class VerusSpeedImpl implements ModeImpl<Speed> {
    private double speed;
    private int ticks = 0;

    @Override
    public Speed getParent() {
        return (Speed) Client.INSTANCE.getModuleManager().getModule(Speed.class);
    }

    @Override
    public String getName() {
        return "Verus";
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
        mc.timer.timerSpeed = 1F;
        speed = 0;
        ticks = 0;
    }


    @EventHandler
    public final Listener<EventMotion> motionListener = e -> {
        if(!mc.thePlayer.isMoving()) mc.thePlayer.setSpeed(0);
        switch (ticks) {
            case 0:
            case 1:
                if (mc.thePlayer.isMoving()) {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.motionY = 0.42F;
                        mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() + 0.17);
                        ticks++;
                    }
                }
            case 2:
                if (mc.thePlayer.isMoving()) {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.motionY = 0.42F;
                        mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() + 0.17);
                    } else {
                        mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() + 0.02 * 0.991);

                    }
                }
                break;
        }
    };
}