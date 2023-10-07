package best.azura.client.impl.module.impl.movement.flight;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventSentPacket;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Flight;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.player.MovementUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;

public class FlightTeamMLGImpl implements ModeImpl<Flight> {

    @Override
    public Flight getParent() {
        return (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
    }

    @Override
    public String getName() {
        return "TeamMLG";
    }

    @Override
    public void onEnable() {
        MovementUtil.vClip(0.5);
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
    }

    @EventHandler
    public final Listener<EventSentPacket> eventSentPacketListener = event -> {
        if (event.getPacket() instanceof C03PacketPlayer) {
            if (!(mc.thePlayer.ticksExisted % 18 == 0)) {
                event.setCancelled(true);
            }
        }
    };

    @EventHandler
    public final Listener<EventMotion> eventMotionListener = e -> {
        mc.getNetHandler().addToSendQueueNoEvent(new C19PacketResourcePackStatus("FAILED", C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
        e.yaw = MathUtil.getRandom_float(-180, 180);
        if (mc.thePlayer.fallDistance > 1) {
            e.onGround = true;
            mc.thePlayer.motionY = 0.42;
            //mc.thePlayer.setSpeed(0.3);
            mc.thePlayer.fallDistance = 0;
        }
    };
}