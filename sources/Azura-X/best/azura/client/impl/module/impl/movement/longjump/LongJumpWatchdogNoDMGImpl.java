package best.azura.client.impl.module.impl.movement.longjump;

import best.azura.client.impl.Client;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventMove;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.LongJump;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.player.MovementUtil;
import net.minecraft.network.Packet;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Vec3;

import java.util.ArrayList;

public class LongJumpWatchdogNoDMGImpl implements ModeImpl<LongJump> {
    private double speed, boostSpeed;
    private int tick, stage, state, ticks, slot;
    private boolean boosted, flyBoosted, flying;
    private Vec3 startPos;
    private final ArrayList<Packet<?>> packets = new ArrayList<>();

    @Override
    public LongJump getParent() {
        return (LongJump) Client.INSTANCE.getModuleManager().getModule(LongJump.class);
    }

    @Override
    public String getName() {
        return "Watchdog Damageless";
    }
    @Override
    public void onEnable() {
        speed = 0;
        tick = ticks = stage = state = 0;
        boosted = false;
        boostSpeed = 0;
        flyBoosted = false;
        flying = false;
        startPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;
        try {
            final double dffX = MathUtil.getDifference(startPos.xCoord, mc.thePlayer.posX),
                    dffZ = MathUtil.getDifference(startPos.zCoord, mc.thePlayer.posZ),
                    distXZ = Math.sqrt(dffX * dffX + dffZ * dffZ);
            mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("Distance: " + distXZ));
        } catch (Exception ignored) {}
    }

    @EventHandler
    public final Listener<Event> event = this::onEvent;

    private void onEvent(final Event event) {
        if (event instanceof EventMotion) {
            if (mc.thePlayer.onGround) {
                if (stage != 0) {
                    getParent().setEnabled(false);
                    return;
                }
                if (mc.thePlayer.isMoving()) {
                    stage++;
                    mc.thePlayer.jump();
                    mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 1.8);
                    mc.thePlayer.setSpeed(mc.thePlayer.getSpeed() * 1.06);
                    tick = 1;
                }
            } else if (mc.thePlayer.isMoving()) {
                if (tick == 1) {
                    mc.thePlayer.motionY = 0.35f;
                    mc.thePlayer.setSpeed(mc.thePlayer.getSpeed() * 1.1);
                    tick = 0;
                }
                if (mc.thePlayer.motionY < 0) {
                    if (state < 7) {
                        mc.thePlayer.motionY = 0;
                        state++;
                    } else if (state < 9) {
                        mc.thePlayer.motionY *= 0.9;
                        state++;
                    }
                }
            }
        }
        if (event instanceof EventMove) {
            EventMove e = (EventMove) event;
            MovementUtil.setSpeed(mc.thePlayer.isMoving() ? Math.max(MovementUtil.getBaseSpeed(), mc.thePlayer.getSpeed()) : 0, e);
        }
    }
}