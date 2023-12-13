package today.flux.addon.api.packet.server;

import com.soterdev.SoterObfuscator;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import today.flux.addon.api.packet.AddonPacket;

public class PacketTime extends AddonPacket {

	public PacketTime(S03PacketTimeUpdate packet) {
		super(packet);
	}

	
	public long getTotalWorldTime() {
		return ((S03PacketTimeUpdate) nativePacket).totalWorldTime;
	}

	
	public void setTotalWorldTime(long totalWorldTime) {
		((S03PacketTimeUpdate) nativePacket).totalWorldTime = totalWorldTime;
	}

	
	public long getWorldTime() {
		return ((S03PacketTimeUpdate) nativePacket).worldTime;
	}

	
	public void setWorldTime(long worldTime) {
		((S03PacketTimeUpdate) nativePacket).worldTime = worldTime;
	}


}
