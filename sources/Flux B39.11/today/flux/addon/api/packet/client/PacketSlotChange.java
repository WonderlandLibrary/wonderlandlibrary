package today.flux.addon.api.packet.client;

import com.soterdev.SoterObfuscator;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import today.flux.addon.api.packet.AddonPacket;

public class PacketSlotChange extends AddonPacket {
	public PacketSlotChange(C09PacketHeldItemChange packet) {
		super(packet);
	}

	public PacketSlotChange(int slot) {
		super(null);
		this.nativePacket = new C09PacketHeldItemChange(slot);
	}

	
	public int getSlot() {
		return ((C09PacketHeldItemChange) nativePacket).getSlotId();
	}

	
	public void setSlot(int slot) {
		((C09PacketHeldItemChange) nativePacket).slotId = slot;
	}
}
