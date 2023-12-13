package best.azura.client.impl.module.impl.movement.flight;

import best.azura.client.api.module.ModeImpl;
import best.azura.client.api.value.Value;
import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMove;
import best.azura.client.impl.events.EventSentPacket;
import best.azura.client.impl.module.impl.movement.Flight;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.util.player.MovementUtil;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FlightMinemoraImpl implements ModeImpl<Flight> {

    private final ArrayList<Packet<?>> packets = new ArrayList<>();
    private final BooleanValue damage = new BooleanValue("Damage", "Damage yourself", false);

    @Override
    public List<Value<?>> getValues() {
        return Collections.singletonList(damage);
    }

    @Override
    public Flight getParent() {
        return (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
    }

    @Override
    public String getName() {
        return "Minemora";
    }

    @Override
    public void onEnable() {
        MovementUtil.vClip(0.42);
        if (damage.getObject()) MovementUtil.spoof(-4, false);
        else MovementUtil.spoof(-2.5, false);
        MovementUtil.spoof(0, true);
    }

    @Override
    public void onDisable() {
        for (final Packet<?> packet : packets) {
            mc.thePlayer.sendQueue.addToSendQueue(packet);
        }
        packets.clear();
    }

    @EventHandler
    public void onEvent(final Event event) {
        if (event instanceof EventSentPacket) {
            final EventSentPacket e = (EventSentPacket) event;
            if (e.getPacket() instanceof C03PacketPlayer || e.getPacket() instanceof C00PacketKeepAlive) {
                e.setCancelled(true);
                packets.add(e.getPacket());
            }
        }
        if (event instanceof EventMove) {
            final EventMove e = (EventMove) event;
            e.setY(mc.thePlayer.motionY = 0);
            if (mc.gameSettings.keyBindSneak.pressed)
                e.setY(mc.thePlayer.motionY -= Flight.speedValue.getObject() * 0.75);
            if (mc.gameSettings.keyBindJump.pressed)
                e.setY(mc.thePlayer.motionY += Flight.speedValue.getObject() * 0.75);
            if (mc.thePlayer.isMoving()) MovementUtil.setSpeed(Flight.speedValue.getObject(), e);
            else MovementUtil.setSpeed(0, e);
        }
    }

}