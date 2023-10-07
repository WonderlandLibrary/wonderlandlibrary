package best.azura.client.impl.module.impl.movement.speed;

import best.azura.client.api.value.Value;
import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventJump;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventMove;
import best.azura.client.impl.events.EventReceivedPacket;
import best.azura.client.impl.value.BooleanValue;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.combat.TargetStrafe;
import best.azura.client.impl.module.impl.movement.Speed;
import best.azura.client.impl.module.impl.player.Scaffold;
import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import best.azura.client.util.player.MovementUtil;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;

import java.util.Collections;
import java.util.List;

public class FuncraftLowSpeedImpl implements ModeImpl<Speed> {
    private double speed;
    private int ticks, jumps;

    private final BooleanValue safeSpeed = new BooleanValue("Safe speed", "Calculate the speed based on the disatance to the next block", false);

    @Override
    public List<Value<?>> getValues() {
        return Collections.singletonList(safeSpeed);
    }

    @Override
    public Speed getParent() {
        return (Speed) Client.INSTANCE.getModuleManager().getModule(Speed.class);
    }

    @Override
    public String getName() {
        return "Funcraft Low";
    }

    @Override
    public void onEnable() {
        Client.INSTANCE.getEventBus().register(this);
        speed = 0;
        ticks = jumps = 0;
    }

    @Override
    public void onDisable() {
        Client.INSTANCE.getEventBus().unregister(this);
        speed = 0;
        mc.timer.timerSpeed = 1.0f;
        ticks = jumps = 0;
    }

    @EventHandler
    public final Listener<EventMove> moveListener = e -> {
        if (!mc.thePlayer.isMoving() || mc.thePlayer.isCollidedHorizontally) speed = 0;
        MovementUtil.setSpeed(mc.thePlayer.isMoving() ? Math.max(MovementUtil.getBaseSpeedFuncraft(), speed *= 0.992) : 0, e);
    };

    @EventHandler
    public final Listener<EventReceivedPacket> eventReceivedPacketListener = e -> {
        if (e.getPacket() instanceof S08PacketPlayerPosLook) {
            Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Disabled",
                    "Disabled due to lag back.", 1000, Type.INFO));
            getParent().setEnabled(false);
        }
    };

    @EventHandler
    public final Listener<EventJump> eventJumpListener = e -> {
        e.setSpeed(0);
    };

    @EventHandler
    public final Listener<EventMotion> motionListener = e -> {
        if (!e.isPre()) return;
        if (mc.thePlayer.onGround) {
            if (mc.thePlayer.isMoving()) {
                MovementUtil.spoof(0, mc.thePlayer.onGround);
                for (double d = 0.15; d <= MovementUtil.getBaseSpeedFuncraft() * 2.15; d += 0.15) {
                    final double x = -Math.sin(mc.thePlayer.getDirection()) * d, z = Math.cos(mc.thePlayer.getDirection()) * d;
                    if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(x, 0, z)).isEmpty())
                        break;
                    if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(x, -0.42, z)).isEmpty())
                        break;
                    MovementUtil.spoof(x, 0, z, mc.thePlayer.onGround);
                    if (safeSpeed.getObject()) speed = d;
                }
                mc.thePlayer.jump();
                if (!safeSpeed.getObject()) speed = MovementUtil.getBaseSpeedFuncraft() * 2.15;
                ticks = 0;
            }
        } else if (mc.thePlayer.isMoving()) {
            boolean goingToCollide = false;
            for (double d = -0.5; d < 1; d += 0.1) {
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
            final TargetStrafe targetStrafe = (TargetStrafe) Client.INSTANCE.getModuleManager().getModule(TargetStrafe.class);
            final boolean strafing = targetStrafe.isEnabled() && targetStrafe.isStrafingDo();
            if (!mc.gameSettings.keyBindJump.pressed && !strafing && !goingToCollide && mc.thePlayer.fallDistance < 0.5 && mc.thePlayer.motionY < 0.3) {
                mc.thePlayer.motionY = -0.15;
            }
        }
        if (!mc.thePlayer.isMoving()) speed = 0;
    };
}