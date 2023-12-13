package today.flux.addon.api.packet.server;


import com.soterdev.SoterObfuscator;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.ChatComponentText;
import today.flux.addon.api.packet.AddonPacket;

public class PacketReceiveChat extends AddonPacket {

	public PacketReceiveChat(S02PacketChat packet) {
		super(packet);
	}

	
	public String getMessage() {
		return ((S02PacketChat) nativePacket).chatComponent.getUnformattedText();
	}

	
	public void setMessage(String message) {
		((S02PacketChat) nativePacket).chatComponent = new ChatComponentText(message);
	}
}
