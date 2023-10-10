package today.flux.addon.api.packet.client;

import com.soterdev.SoterObfuscator;
import net.minecraft.network.play.client.C0CPacketInput;
import today.flux.addon.api.packet.AddonPacket;

public class PacketInput extends AddonPacket {
	public PacketInput(C0CPacketInput packet) {
		super(packet);
	}
	
	public PacketInput(float strafeSpeed, float forwardSpeed, boolean jumping, boolean sneaking) {
		super(null);
		this.nativePacket = new C0CPacketInput(strafeSpeed, forwardSpeed, jumping, sneaking);
    }

	
	public float getStrafeSpeed() {
		return ((C0CPacketInput) nativePacket).getStrafeSpeed();
	}

	
	public void setStrafeSpeed(float strafeSpeed) {
		((C0CPacketInput) nativePacket).strafeSpeed = strafeSpeed;
	}

	
	public float getForwardSpeed() {
		return ((C0CPacketInput) nativePacket).getForwardSpeed();
	}

	
	public void setForwardSpeed(float forwardSpeed) {
		((C0CPacketInput) nativePacket).forwardSpeed = forwardSpeed;
	}

	
	public boolean isJumping() {
		return ((C0CPacketInput) nativePacket).isJumping();
	}

	
	public void setJumping(boolean jumping) {
		((C0CPacketInput) nativePacket).jumping = jumping;
	}

	
	public boolean isSneaking() {
		return ((C0CPacketInput) nativePacket).isSneaking();
	}

	
	public void setSneaking(boolean sneaking) {
		((C0CPacketInput) nativePacket).sneaking = sneaking;
	}
	
}
