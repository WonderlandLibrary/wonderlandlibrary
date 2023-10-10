package today.flux.addon.api.packet.server;

import com.soterdev.SoterObfuscator;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import today.flux.addon.api.packet.AddonPacket;
import today.flux.addon.api.utils.Motion;

public class PacketVelocity extends AddonPacket {

	public PacketVelocity(S12PacketEntityVelocity packet) {
		super(packet);
	}

	
	public int getEntityID() {
		return ((S12PacketEntityVelocity) nativePacket).entityID;
	}

	
	public void setEntityID(int entityID) {
		((S12PacketEntityVelocity) nativePacket).entityID = entityID;
	}

	
	public Motion getMotion() {
		S12PacketEntityVelocity packet = (S12PacketEntityVelocity) this.nativePacket;
		return new Motion(packet.getMotionX(), packet.getMotionY(), packet.getMotionZ());
	}

	
	public void setMotion(Motion motion) {
		S12PacketEntityVelocity packet = (S12PacketEntityVelocity) this.nativePacket;
		packet.setMotionX((int) motion.getX());
		packet.setMotionY((int) motion.getY());
		packet.setMotionZ((int) motion.getZ());
	}
}
