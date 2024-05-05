package best.azura.client.impl.module.impl.movement.speed;

import best.azura.client.impl.Client;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventMove;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Speed;
import best.azura.client.util.player.MovementUtil;

public class PacketGroundSpeedImpl implements ModeImpl<Speed> {

    @Override
    public Speed getParent() {
        return (Speed) Client.INSTANCE.getModuleManager().getModule(Speed.class);
    }

    @Override
    public String getName() {
        return "Packet Ground";
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;
    }

    @EventHandler
    public final Listener<EventMove> moveListener = e -> {
        if (mc.thePlayer.isMoving() && mc.thePlayer.onGround) {
            double speed = 0;
            for (double d = 0; d <= Speed.speedValue.getObject(); d += 0.15) {
                speed += 0.15;
                double yaw = mc.thePlayer.getDirection();
                double x = -Math.sin(yaw) * speed;
                double z = Math.cos(yaw) * speed;
                if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(x, 0, z)).isEmpty()) {
                    break;
                }
                MovementUtil.spoof(x, 0, z, mc.thePlayer.onGround);
            }
            /*for (int i = 1; i <= Math.floor(Speed.speedValue.getObject()); i++) {
                speed += 0.15;
                double yaw = mc.thePlayer.getDirection();
                double x = -Math.sin(yaw) * speed;
                double z = Math.cos(yaw) * speed;
                MovementUtil.spoof(x, 0, z, mc.thePlayer.onGround);
            }*/
            MovementUtil.setSpeed(speed, e);
        }
    };
}