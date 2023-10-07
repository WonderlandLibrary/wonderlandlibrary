package best.azura.client.impl.module.impl.movement.longjump;

import best.azura.client.impl.Client;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventMove;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.LongJump;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.player.MovementUtil;
import net.minecraft.network.Packet;
import net.minecraft.util.Vec3;

import java.util.ArrayList;

public class LongJumpWatchdogImpl implements ModeImpl<LongJump> {
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
        return "Watchdog";
    }

    private final float[] LONG_JUMP_Y_MOTIONS = {
            0.44f, 0.44f, 0.43f, 0.41f, 0.4f, 0.38f, 0.36f, 0.34f,
            0.32f, 0.28f, 0.18f, 0.13f, 0.09f, 0.07f, 0.05f, 0.03f,
            -0.03f, -0.07f, -0.11f, -0.15f, -0.23f
    };

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
        } catch (Exception ignored) {}
    }

    @EventHandler
    public final Listener<Event> event = this::onEvent;

    private void onEvent(final Event event) {
        if (event instanceof EventMove) {
            EventMove e = (EventMove) event;
            switch (stage) {
                case 0:
                    MovementUtil.damagePlayerHypixel(1.5);
                    stage++;
                    break;
                case 1:
                    speed = MovementUtil.getBaseSpeed() * 1.77;
                    e.setY(mc.thePlayer.motionY = 0.42f);
                    stage++;
                    break;
                case 2:
                    if (tick < LONG_JUMP_Y_MOTIONS.length)
                        e.setY(mc.thePlayer.motionY = LONG_JUMP_Y_MOTIONS[tick++]);
                    e.setY(mc.thePlayer.motionY += MathUtil.getRandom_double(2.0E-8D, 2.0E-4D));
                    if (mc.thePlayer.onGround) getParent().setEnabled(false);
                    if (mc.thePlayer.isMoving()) MovementUtil.setSpeed(Math.max(MovementUtil.getBaseSpeed(), speed -= speed / 60), e);
                    else MovementUtil.setSpeed(speed = 0, e);
                    break;
            }
        }
    }
}