package best.azura.client.impl.module.impl.movement.speed;

import best.azura.client.impl.Client;
import best.azura.eventbus.handler.Listener;
import best.azura.eventbus.handler.EventHandler;
import best.azura.client.impl.events.EventJump;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Speed;
import best.azura.client.impl.module.impl.player.Scaffold;
import best.azura.client.util.player.MovementUtil;
import net.minecraft.util.BlockPos;

public class VerusLowSpeedImpl implements ModeImpl<Speed> {
    private double speed;
    private int ticks = 0;

    @Override
    public Speed getParent() {
        return (Speed) Client.INSTANCE.getModuleManager().getModule(Speed.class);
    }

    @Override
    public String getName() {
        return "Verus Low";
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
        switch (ticks) {
            case 0:
            case 1:
                if (mc.thePlayer.isMoving()) {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.motionY = 0.4F;
                        mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() + 0.18);
                        ticks++;
                    }
                }
            case 2:
                if (mc.thePlayer.isMoving()) {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.motionY = 0.42F;
                        mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() + 0.1);
                    } else {
                        mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() + 0.02 * 0.991);

                    }
                }
                break;
        }
        boolean goingToCollide = false;
        for (double d = -0.5; d < 2; d += 0.1) {
            double x = -Math.sin(mc.thePlayer.getDirection()) * d;
            double z = Math.cos(mc.thePlayer.getDirection()) * d;
            for (int i = 0; i >= -100 && !Client.INSTANCE.getModuleManager().getModule(Scaffold.class).isEnabled(); i--) {
                goingToCollide = true;
                if (!mc.theWorld.isAirBlock(new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + i, mc.thePlayer.posZ + z))) {
                    goingToCollide = false;
                    break;
                }
            }
            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
                    mc.thePlayer.getEntityBoundingBox().offset(x, 0, z)).isEmpty()) goingToCollide = true;
        }
        if (mc.thePlayer.motionY > 0 && mc.thePlayer.motionY < 0.4 && !mc.gameSettings.keyBindJump.pressed && !goingToCollide) {
            e.onGround = true;
            mc.thePlayer.motionY = 0;
        }
    };
}