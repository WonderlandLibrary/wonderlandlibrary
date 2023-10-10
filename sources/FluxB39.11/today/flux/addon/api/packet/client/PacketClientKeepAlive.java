package today.flux.addon.api.packet.client;

import com.soterdev.SoterObfuscator;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import today.flux.addon.api.packet.AddonPacket;

public class PacketClientKeepAlive extends AddonPacket {

	public PacketClientKeepAlive(C00PacketKeepAlive packet) {
		super(packet);
	}

	public PacketClientKeepAlive(int key) {
		super(null);
		this.nativePacket = new C00PacketKeepAlive(key);
	}

	
	public int getKey() {
		return ((C00PacketKeepAlive) nativePacket).getKey();
	}

	
	public void setKey(int key) {
		((C00PacketKeepAlive) nativePacket).key = key;
	}

}
