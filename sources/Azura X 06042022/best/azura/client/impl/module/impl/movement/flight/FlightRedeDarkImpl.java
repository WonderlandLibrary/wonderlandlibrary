package best.azura.client.impl.module.impl.movement.flight;

import best.azura.client.impl.Client;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventMove;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Flight;
import best.azura.client.util.player.MovementUtil;
import net.minecraft.block.BlockAir;

public class FlightRedeDarkImpl implements ModeImpl<Flight> {

    @Override
    public Flight getParent() {
        return (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
    }

    @Override
    public String getName() {
        return "Rede Dark";
    }
    @Override
    public void onEnable() {
        BlockAir.collision = true;
        BlockAir.collisionMaxY = (int) mc.thePlayer.posY;
    }

    @Override
    public void onDeselect() {
        BlockAir.collision = false;
        BlockAir.collisionMaxY = -1;
    }

    @Override
    public void onDisable() {
        BlockAir.collision = false;
        BlockAir.collisionMaxY = -1;
    }

    @EventHandler
    public final Listener<EventMove> moveListener = e -> {
        if (mc.gameSettings.keyBindJump.pressed && mc.thePlayer.onGround) {
            MovementUtil.spoof(1, true);
            MovementUtil.spoof(1, true);
            MovementUtil.vClip(1);
            if (!mc.gameSettings.keyBindSneak.pressed) e.setY(mc.thePlayer.motionY = 0);
        }
        if (mc.gameSettings.keyBindSneak.pressed && mc.thePlayer.onGround) {
            MovementUtil.spoof(-1, true);
            MovementUtil.spoof(-1, true);
            MovementUtil.vClip(-1);
            if (!mc.gameSettings.keyBindJump.pressed) e.setY(mc.thePlayer.motionY = 0);
        }
        if (mc.thePlayer.onGround)
            BlockAir.collisionMaxY = (int) mc.thePlayer.posY;
        if (!mc.thePlayer.isMoving() || !mc.thePlayer.onGround) return;
        double speed = 0;
        for (double d = 0; d <= Flight.speedValue.getObject(); d += 0.15) {
            speed += 0.15;
            double yaw = mc.thePlayer.getDirection();
            double x = -Math.sin(yaw) * speed;
            double z = Math.cos(yaw) * speed;
            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(x, 0, z)).isEmpty())
                break;
            MovementUtil.spoof(x, 0, z, mc.thePlayer.onGround);
        }
        MovementUtil.setSpeed(speed, e);
    };
}