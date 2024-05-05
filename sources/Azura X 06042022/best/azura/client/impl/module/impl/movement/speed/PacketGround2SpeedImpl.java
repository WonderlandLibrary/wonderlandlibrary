package best.azura.client.impl.module.impl.movement.speed;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMove;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Speed;
import best.azura.client.util.player.MovementUtil;

public class PacketGround2SpeedImpl implements ModeImpl<Speed> {

    @Override
    public Speed getParent() {
        return (Speed) Client.INSTANCE.getModuleManager().getModule(Speed.class);
    }

    @Override
    public String getName() {
        return "Packet Ground 2";
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
            double endX = 0, endZ = 0;
            for (double d = 0; d <= Speed.speedValue.getObject(); d += 0.15) {
                double x = mc.thePlayer.motionX;
                double z = mc.thePlayer.motionZ;
                if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(x, 0, z)).isEmpty()) {
                    break;
                }
                MovementUtil.spoof(x, 0, z, mc.thePlayer.onGround);
                endX += x;
                endZ += z;
            }
            if (endX != 0) e.setX(endX);
            if (endZ != 0) e.setZ(endZ);
        }
    };
}