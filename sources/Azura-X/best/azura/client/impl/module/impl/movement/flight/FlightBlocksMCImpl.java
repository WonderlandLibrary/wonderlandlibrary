package best.azura.client.impl.module.impl.movement.flight;

import best.azura.client.impl.Client;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Flight;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.player.MovementUtil;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventMove;
import best.azura.client.impl.events.EventSentPacket;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

import java.util.ArrayList;

public class FlightBlocksMCImpl implements ModeImpl<Flight> {
    private int ticks;
    private final ArrayList<Packet<?>> movePackets = new ArrayList<>();


    @Override
    public Flight getParent() {
        return (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
    }

    @Override
    public String getName() {
        return "Verus";
    }

    @Override
    public void onEnable() {
        Client.INSTANCE.getEventBus().register(this);
        MovementUtil.vClip(0.5);
    }

    @Override
    public void onDisable() {
        Client.INSTANCE.getEventBus().unregister(this);
        if (movePackets.size() > 3) {
            for (Packet<?> movePacket : movePackets)
                mc.thePlayer.sendQueue.addToSendQueueNoEvent(movePacket);
            movePackets.clear();
        }
        mc.thePlayer.setSpeed(0);
    }

    @EventHandler
    public final Listener<EventMotion> eventMotionListener = e -> {
        if(mc.thePlayer.isSneaking()) return;
        if (mc.gameSettings.keyBindJump.pressed && ticks == 0) {
            e.onGround = true;
            MovementUtil.vClip(0.5);
            ticks = 4;
        } else {
            mc.thePlayer.motionY = 0;
        }
        if (ticks > 0) ticks--;
        e.yaw = MathUtil.getRandom_float(-180, 180);
        e.onGround = true;
        mc.thePlayer.cameraYaw = 1E-4F;
    };

    @EventHandler
    public final Listener<EventMove> moveListener = e -> {
        if(mc.thePlayer.isMoving() && !mc.thePlayer.isSneaking() && !mc.gameSettings.keyBindJump.pressed) mc.thePlayer.setSpeed(0.42 * 0.991);
        if(!mc.thePlayer.isMoving()) mc.thePlayer.setSpeed(0); mc.thePlayer.setSprinting(false);
    };

    @EventHandler
    public final Listener<EventSentPacket> EventSentPacketListener = e -> {
        if(e.getPacket() instanceof C03PacketPlayer) {
            e.setCancelled(true);
            movePackets.add(e.getPacket());
        }
    };

    @EventHandler
    public final Listener<best.azura.client.impl.events.EventUpdate> EventUpdate = e -> {
        if (movePackets.size() > 30) {
            for (Packet<?> movePacket : movePackets)
                mc.thePlayer.sendQueue.addToSendQueueNoEvent(movePacket);
            movePackets.clear();
        }
    };
}