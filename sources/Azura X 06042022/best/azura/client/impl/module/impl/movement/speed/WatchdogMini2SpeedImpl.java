package best.azura.client.impl.module.impl.movement.speed;

import best.azura.client.impl.Client;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventMove;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.combat.TargetStrafe;
import best.azura.client.impl.module.impl.movement.Speed;
import best.azura.client.util.player.MovementUtil;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;

@SuppressWarnings("unused")
public class WatchdogMini2SpeedImpl implements ModeImpl<Speed> {
    private int ticks;
    private boolean lowHop;

    @Override
    public Speed getParent() {
        return (Speed) Client.INSTANCE.getModuleManager().getModule(Speed.class);
    }

    @Override
    public String getName() {
        return "Watchdog Other Mini";
    }

    @Override
    public void onEnable() {
        Client.INSTANCE.getEventBus().register(this);
        ticks = 0;
    }

    @Override
    public void onDisable() {
        Client.INSTANCE.getEventBus().unregister(this);
        ticks = 0;
    }

    @EventHandler
    public final Listener<EventMove> moveListener = e -> MovementUtil.setSpeed(mc.thePlayer.isMoving() ? Math.max(MovementUtil.getBaseSpeed(), mc.thePlayer.getSpeed()) : 0, e);

    @EventHandler
    public final Listener<EventMotion> motionListener = e -> {
        final TargetStrafe targetStrafe = (TargetStrafe) Client.INSTANCE.getModuleManager().getModule(TargetStrafe.class);
        if (mc.thePlayer.onGround) {
            if (mc.thePlayer.isMoving()) {
                mc.thePlayer.jump();
                boolean goingToCollide = false;
                for (double d = 0; d < 2; d += 0.1) {
                    double x = -Math.sin(mc.thePlayer.getDirection()) * d;
                    double z = Math.cos(mc.thePlayer.getDirection()) * d;
                    for (int i = 0; i >= -100; i--) {
                        goingToCollide = true;
                        if (!mc.theWorld.isAirBlock(new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + i, mc.thePlayer.posZ + z))) {
                            goingToCollide = false;
                            break;
                        }
                    }
                    if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
                            mc.thePlayer.getEntityBoundingBox().offset(x, 0, z)).isEmpty()) goingToCollide = true;
                }
                switch (ticks) {
                    case 0:
                        mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 1.63);
                        ticks++;
                        break;
                    case 1:
                        mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 1.77);
                        ticks++;
                        break;
                    case 2:
                        mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 1.6);
                        ticks++;
                        break;
                    case 3:
                        mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 1.8);
                        ticks++;
                        break;
                    case 4:
                    case 5:
                        mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 1.7);
                        ticks++;
                        break;
                    case 6:
                    case 7:
                        mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 1.62);
                        ticks++;
                        break;
                    case 8:
                        mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 1.61);
                        ticks = 0;
                        break;
                }
                if (targetStrafe.isEnabled() && targetStrafe.isStrafingDo())
                    goingToCollide = true;
                lowHop = !this.mc.thePlayer.isPotionActive(Potion.jump) && !goingToCollide && !mc.gameSettings.keyBindJump.pressed;
                if (mc.gameSettings.keyBindJump.pressed) mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 0.9);
                if (lowHop) {
                    mc.thePlayer.setSpeed(mc.thePlayer.getSpeed() * 0.67);
                    mc.thePlayer.motionY = 0.2f;
                }
            }
        } else if (mc.thePlayer.isMoving()) {
            if (lowHop) {
                if (mc.thePlayer.motionY > 0.05 && mc.thePlayer.motionY < 0.15) mc.thePlayer.motionY = -0.06f;
                if (mc.thePlayer.motionY > 0.15 && mc.thePlayer.motionY < 0.2) mc.thePlayer.motionY = 0.13f;
            }
        }
    };
}