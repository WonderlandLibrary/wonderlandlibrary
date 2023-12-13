package today.flux.addon.api.packet.server;

import com.soterdev.SoterObfuscator;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import today.flux.addon.api.packet.AddonPacket;
import today.flux.addon.api.utils.EntityPosition;
import today.flux.addon.api.utils.Rotation;

public class PacketSetPosition extends AddonPacket {
	public PacketSetPosition(S08PacketPlayerPosLook packet) {
		super(packet);
	}

	
	public EntityPosition getPosition() {
		S08PacketPlayerPosLook packet = ((S08PacketPlayerPosLook) nativePacket);
		return new EntityPosition(packet.x, packet.y, packet.z);
	}

	
	public void setPosition(EntityPosition position) {
		S08PacketPlayerPosLook packet = ((S08PacketPlayerPosLook) nativePacket);
		packet.x = position.x;
		packet.y = position.y;
		packet.z = position.z;
	}

	
	public Rotation getRotation() {
		S08PacketPlayerPosLook packet = ((S08PacketPlayerPosLook) nativePacket);
		return new Rotation(packet.yaw, packet.pitch);
	}

	
	public void setRotation(Rotation rotation) {
		S08PacketPlayerPosLook packet = ((S08PacketPlayerPosLook) nativePacket);
		packet.yaw = rotation.yaw;
		packet.pitch = rotation.pitch;
	}
}
