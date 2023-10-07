package best.azura.client.impl.module.impl.other;

import best.azura.client.impl.events.EventSentPacket;
import best.azura.client.impl.events.EventUpdate;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.impl.value.NumberValue;
import best.azura.client.impl.value.dependency.BooleanDependency;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;

import java.util.ArrayList;

@SuppressWarnings("unused")
@ModuleInfo(name = "Blink", description = "Choke packets to make it seem like you are lagging", category = Category.OTHER)
public class Blink extends Module {

	private final ArrayList<Packet<?>> movePackets = new ArrayList<>();

	private final BooleanValue antiPingKick = new BooleanValue("AntiPingKick", "Disables C00 & C0F cancelling", false);
	private final BooleanValue pulse = new BooleanValue("Pulse", "Pulse blink", false);
	private final NumberValue<Double> maxPulsePackets = new NumberValue<>("Max Pulse", "Max Packet amount for pulse blinking", new BooleanDependency(pulse, true), 25.0, 1.0, 1.0, 100.0);

	@EventHandler
	public Listener<EventSentPacket> eventSentPacketListener = event -> {
		if (mc.thePlayer == null || mc.theWorld == null) {
			movePackets.clear();
			setEnabled(false);
		}
		if (antiPingKick.getObject() && (event.getPacket() instanceof C0FPacketConfirmTransaction || event.getPacket() instanceof C00PacketKeepAlive))
			return;
		if (event.getPacket() instanceof C03PacketPlayer) {
			movePackets.add(event.getPacket());
		}
		event.setCancelled(true);
	};

	@EventHandler
	public final Listener<EventUpdate> eventUpdateListener = event -> {
		if (movePackets.size() > maxPulsePackets.getObject() && pulse.getObject()) {
			movePackets.forEach(p -> mc.thePlayer.sendQueue.addToSendQueueNoEvent(p));
			movePackets.clear();
		}
	};

	@Override
	public void onDisable() {
		for (Packet<?> packet : movePackets) mc.thePlayer.sendQueue.addToSendQueueNoEvent(packet);
		movePackets.clear();
		super.onDisable();
	}

}