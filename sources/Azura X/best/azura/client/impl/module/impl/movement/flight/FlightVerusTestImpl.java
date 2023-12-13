package best.azura.client.impl.module.impl.movement.flight;

import best.azura.client.impl.Client;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Flight;
import best.azura.client.util.player.MovementUtil;
import best.azura.client.impl.events.EventMove;
import best.azura.client.impl.events.EventReceivedPacket;
import best.azura.client.impl.events.EventSentPacket;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

import java.util.ArrayList;

public class FlightVerusTestImpl implements ModeImpl<Flight> {

    private final ArrayList<Packet<?>> packets = new ArrayList<>();
    private int ticks, lagBacks;

    @Override
    public Flight getParent() {
        return (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
    }

    @Override
    public String getName() {
        return "Verus Test";
    }

    @Override
    public void onEnable() {
        ticks = lagBacks = 0;
    }

    @Override
    public void onDisable() {
        ticks = lagBacks = 0;
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
                lagBacks++;
            }
        }
        if (event instanceof EventSentPacket) {
            final EventSentPacket e = (EventSentPacket) event;
            if (e.getPacket() instanceof C03PacketPlayer) {
                final C03PacketPlayer c03PacketPlayer = e.getPacket();
                if (ticks == 0) {
                    c03PacketPlayer.y = 1.0D;
                    c03PacketPlayer.onGround = true;
                } else if (ticks < 5 && lagBacks > 0) {
                    c03PacketPlayer.y = -999.0D;
                    c03PacketPlayer.onGround = true;
                }
                ticks++;
                if (ticks > 130) ticks = 0;
            }
        }
    }


    @EventHandler
    public final Listener<EventMove> moveListener = e -> {
        if (ticks < 10) {
            MovementUtil.setSpeed(0.1, e);
            return;
        }
        e.setY(mc.thePlayer.motionY = 0);
        if (mc.gameSettings.keyBindSneak.pressed) e.setY(mc.thePlayer.motionY -= Flight.speedValue.getObject() * 0.75);
        if (mc.gameSettings.keyBindJump.pressed) e.setY(mc.thePlayer.motionY += Flight.speedValue.getObject() * 0.75);
        if (mc.thePlayer.isMoving()) MovementUtil.setSpeed(Flight.speedValue.getObject(), e);
        else MovementUtil.setSpeed(0, e);
    };

}