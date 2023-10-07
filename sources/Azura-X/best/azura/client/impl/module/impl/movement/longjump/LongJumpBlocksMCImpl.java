package best.azura.client.impl.module.impl.movement.longjump;

import best.azura.client.impl.Client;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventMove;
import best.azura.client.impl.events.EventReceivedPacket;
import best.azura.client.impl.events.EventSentPacket;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.LongJump;
import best.azura.client.util.player.MovementUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

import java.util.ArrayList;

public class LongJumpBlocksMCImpl implements ModeImpl<LongJump> {
    private double speed;
    private int ticks, stage, state;
    private boolean hasJumped = false;
    private final ArrayList<Packet<?>> packets = new ArrayList<>();

    @Override
    public LongJump getParent() {
        return (LongJump) Client.INSTANCE.getModuleManager().getModule(LongJump.class);
    }

    @Override
    public String getName() {
        return "Blocks MC";
    }

    @Override
    public void onEnable() {
        speed = ticks = state = stage = 0;
        hasJumped = false;
    }

    @Override
    public void onDisable() {
        speed = ticks = 0;
        hasJumped = false;
    }

    @EventHandler
    public final Listener<Event> eventListener = this::handle;

    private void handle(final Event event) {
        if (event instanceof EventReceivedPacket) {
            final EventReceivedPacket e = (EventReceivedPacket) event;
            if (e.getPacket() instanceof S08PacketPlayerPosLook && mc.thePlayer.ticksExisted > 10) {
                final S08PacketPlayerPosLook s08 = e.getPacket();
                s08.yaw = mc.thePlayer.rotationYaw;
                s08.pitch = mc.thePlayer.rotationPitch;
            }
        }
        if (event instanceof EventSentPacket) {
            final EventSentPacket e = (EventSentPacket) event;
            if ((e.getPacket() instanceof C0FPacketConfirmTransaction || e.getPacket() instanceof C00PacketKeepAlive) && !e.isCancelled()) e.setCancelled(true);
            if (e.getPacket() instanceof C03PacketPlayer) {
                final C03PacketPlayer c03PacketPlayer = e.getPacket();
                if (ticks == 0) {
                    c03PacketPlayer.y = 1.0D;
                    c03PacketPlayer.onGround = true;
                } else if (ticks < 5) {
                    c03PacketPlayer.y = -999.0D;
                    c03PacketPlayer.onGround = true;
                }
                ticks++;
            }
        }
    }


    @EventHandler
    public final Listener<EventMove> moveListener = e -> {
        if (ticks < 11) {
            MovementUtil.setSpeed(ticks % 2 == 0 ? 0.1 : -0.1, e);
            e.setY(mc.thePlayer.motionY = -0.07f);
            speed = getParent().speedValue.getObject();
            return;
        }
        if (!mc.thePlayer.isMoving() || mc.thePlayer.isCollidedHorizontally) speed = 0;
        if (ticks == 12) e.setY(mc.thePlayer.motionY = 0.9);
        if (ticks > 13 && mc.thePlayer.onGround) getParent().setEnabled(false);
        if (mc.thePlayer.isMoving()) MovementUtil.setSpeed(Math.max(0.2803, speed *= 0.992), e);
        else MovementUtil.setSpeed(0, e);
    };

}