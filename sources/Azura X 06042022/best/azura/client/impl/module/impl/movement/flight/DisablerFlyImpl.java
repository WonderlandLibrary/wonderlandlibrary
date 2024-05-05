package best.azura.client.impl.module.impl.movement.flight;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventSentPacket;
import best.azura.client.impl.events.EventUpdate;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Flight;
import net.minecraft.network.play.client.*;

import java.util.concurrent.ThreadLocalRandom;

public class DisablerFlyImpl implements ModeImpl<Flight> {
    @Override
    public Flight getParent() {
        return (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
    }

    @Override
    public String getName() {
        return "Verus Disabler";
    }

    @Override
    public void onDeselect() {

    }

    @EventHandler
    public final Listener<EventUpdate> eventUpdateListener = e -> {
    };

    @Override
    public void onEnable() {
        Client.INSTANCE.getEventBus().register(this);
    }

    @Override
    public void onDisable() {
        Client.INSTANCE.getEventBus().unregister(this);
        mc.timer.timerSpeed = 1F;
        mc.thePlayer.setSpeed(0);
    }

    @EventHandler
    public final Listener<EventSentPacket> eventSentPacketListener = e -> {
        if(e.getPacket() instanceof C03PacketPlayer) {
            if(!(mc.thePlayer.ticksExisted % 9 == 0)) {
                e.setCancelled(true);
            }
        }
    };


    @EventHandler
    public final Listener<EventMotion> motionListener = e -> {

        mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.RIDING_JUMP, ThreadLocalRandom.current().nextInt(0, 1000000)));
        mc.thePlayer.motionY = 6E-14;
        e.onGround = true;

        if (!e.isPre()) return;
        if (!mc.thePlayer.isMoving()) mc.thePlayer.setSpeed(0);
        if (mc.thePlayer.ticksExisted % 3 == 0 && mc.thePlayer.isMoving()) {
            mc.thePlayer.setSpeed(3);
        } else {
            mc.thePlayer.setSpeed(0);
        }
    };
}