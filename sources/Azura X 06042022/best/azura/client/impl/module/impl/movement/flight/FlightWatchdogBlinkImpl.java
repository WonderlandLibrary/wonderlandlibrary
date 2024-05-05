package best.azura.client.impl.module.impl.movement.flight;

import best.azura.client.impl.Client;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventMove;
import best.azura.client.impl.events.EventSentPacket;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Flight;
import best.azura.client.util.player.MovementUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

import java.util.ArrayList;

public class FlightWatchdogBlinkImpl implements ModeImpl<Flight> {
    private double speed, boostSpeed;
    private int tick, stage, state, ticks, slot;
    private boolean boosted, flyBoosted, flying;
    private final ArrayList<Packet<?>> packets = new ArrayList<>();

    @Override
    public Flight getParent() {
        return (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
    }

    @Override
    public String getName() {
        return "Watchdog Blink";
    }

    @Override
    public void onEnable() {
        speed = 0;
        tick = ticks = stage = state = 0;
        boosted = false;
        boostSpeed = 0;
        flyBoosted = false;
        flying = false;
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;
        for (Packet<?> packet : packets) {
            mc.thePlayer.sendQueue.addToSendQueueNoEvent(packet);
        }
        packets.clear();
    }

    @EventHandler
    public final Listener<Event> event = this::onEvent;

    private void onEvent(final Event event) {
        if (event instanceof EventSentPacket) {
            EventSentPacket e = (EventSentPacket) event;
            if (e.getPacket() instanceof C03PacketPlayer && stage > 1) {
                e.setCancelled(true);
                packets.add(e.getPacket());
            }
        }
        if (event instanceof EventMove) {
            EventMove e = (EventMove) event;
            switch (stage) {
                case 0:
                    MovementUtil.damagePlayerHypixel();
                    stage++;
                    break;
                case 1:
                    speed = MovementUtil.getBaseSpeed() * 2;
                    e.setY(mc.thePlayer.motionY = (float)0.2);
                    stage++;
                    break;
                case 2:
                    if (packets.size() > 12) {
                        for (Packet<?> packet : packets) {
                            mc.thePlayer.sendQueue.addToSendQueueNoEvent(packet);
                        }
                        packets.clear();
                    }
                    mc.timer.timerSpeed = (float) Math.min(2.1F, 2.1F * 0.992);
                    if (mc.thePlayer.fallDistance > 0) e.setY(mc.thePlayer.motionY = 1.0E-12 + MovementUtil.getWatchdogUnpatchValues());
                    if (mc.thePlayer.isMoving()) MovementUtil.setSpeed(Math.max(MovementUtil.getBaseSpeed(), speed *= 0.98), e);
                    else MovementUtil.setSpeed(speed = 0, e);
                    break;
            }
        }
    }
}