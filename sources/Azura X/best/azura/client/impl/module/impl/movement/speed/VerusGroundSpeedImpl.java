package best.azura.client.impl.module.impl.movement.speed;

import best.azura.client.impl.Client;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventJump;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Speed;
import best.azura.client.util.player.MovementUtil;

public class VerusGroundSpeedImpl implements ModeImpl<Speed> {
    private double speed;
    private int ticks = 0;

    @Override
    public Speed getParent() {
        return (Speed) Client.INSTANCE.getModuleManager().getModule(Speed.class);
    }

    @Override
    public String getName() {
        return "Verus Ground";
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
    public final Listener<EventJump> eventJumpListener = e -> {
        e.setSpeed(0);
    };


    @EventHandler
    public final Listener<EventMotion> motionListener = e -> {
        if (!e.isPre()) return;
        if(!mc.thePlayer.isMoving()) mc.thePlayer.setSpeed(0);
        if (mc.thePlayer.onGround && mc.thePlayer.isMoving()) {
            switch (ticks) {
                case 0:
                    mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() + 0.08);
                    e.onGround = true;
                    ticks++;
                    break;
                case 1:
                    mc.thePlayer.onGround = false;
                    mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 0.991);
                    e.y += 0.5;
                    e.onGround = false;
                    ticks++;
                    break;
                case 2:
                    mc.thePlayer.onGround = false;
                    mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 0.991);
                    e.y += 0.42159999847412166;
                    e.onGround = false;
                    ticks++;
                    break;
                case 3:
                    mc.thePlayer.onGround = false;
                    mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 0.991);
                    e.y += 0.2663679939575161;
                    e.onGround = false;
                    ticks++;
                    break;
                case 4:
                    mc.thePlayer.onGround = false;
                    mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 0.991);
                    e.y += 0.03584062504455687;
                    e.onGround = false;
                    ticks = 0;
                    break;
            }
            mc.thePlayer.setSpeed(mc.thePlayer.getSpeed());
        }
    };
}