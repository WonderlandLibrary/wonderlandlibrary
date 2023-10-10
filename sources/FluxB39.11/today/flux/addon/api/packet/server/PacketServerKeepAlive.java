package today.flux.addon.api.packet.server;

import com.soterdev.SoterObfuscator;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import today.flux.addon.api.packet.AddonPacket;

public class PacketServerKeepAlive extends AddonPacket {

	public PacketServerKeepAlive(S00PacketKeepAlive packet) {
		super(packet);
	}

	
	public int getId() {
		return ((S00PacketKeepAlive) nativePacket).id;
	}

	
	public void setId(int id) {
		((S00PacketKeepAlive) nativePacket).id = id;
	}

}
