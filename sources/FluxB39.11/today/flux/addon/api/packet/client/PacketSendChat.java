package today.flux.addon.api.packet.client;

import com.soterdev.SoterObfuscator;
import net.minecraft.network.play.client.C01PacketChatMessage;
import today.flux.addon.api.packet.AddonPacket;

public class PacketSendChat extends AddonPacket {
	public PacketSendChat(C01PacketChatMessage packet) {
		super(packet);
	}

	public PacketSendChat(String message) {
		super(null);
		nativePacket = new C01PacketChatMessage(message);
	}

	
	public String getMessage() {
		return ((C01PacketChatMessage) nativePacket).getMessage();
	}

	
	public void setMessage(String message) {
		((C01PacketChatMessage) nativePacket).message = message;
	}
}
