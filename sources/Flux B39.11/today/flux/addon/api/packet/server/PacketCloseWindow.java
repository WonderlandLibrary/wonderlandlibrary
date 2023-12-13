package today.flux.addon.api.packet.server;

import com.soterdev.SoterObfuscator;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import today.flux.addon.api.packet.AddonPacket;

public class PacketCloseWindow extends AddonPacket {
	public PacketCloseWindow(S2EPacketCloseWindow packet) {
		super(packet);
	}

	
	public int getWindowId() {
		return ((S2EPacketCloseWindow) nativePacket).windowId;
	}

	
	public void setWindowId(int windowId) {
		((S2EPacketCloseWindow) nativePacket).windowId = windowId;
	}
	
}
