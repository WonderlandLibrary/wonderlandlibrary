package today.flux.addon.api.packet.client;

import com.soterdev.SoterObfuscator;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import today.flux.addon.api.packet.AddonPacket;
import today.flux.addon.api.utils.BlockPosition;
import today.flux.addon.api.utils.EntityPosition;

public class PacketPlace extends AddonPacket {
	public PacketPlace(C08PacketPlayerBlockPlacement packet) {
		super(packet);
	}

	
	public BlockPosition getPosition() {
		return BlockPosition.getBlockPosition(((C08PacketPlayerBlockPlacement) nativePacket).getPosition());
	}

	
	public void setPosition(BlockPosition position) {
		((C08PacketPlayerBlockPlacement) nativePacket).position = position.getNativeBlockPos();
	}

	
	public EntityPosition getFacing() {
		C08PacketPlayerBlockPlacement packet = ((C08PacketPlayerBlockPlacement) nativePacket);
		return new EntityPosition(packet.facingX, packet.facingY, packet.facingZ);
	}

	
	public int getPlaceDirection() {
		return ((C08PacketPlayerBlockPlacement) nativePacket).placedBlockDirection;
	}

	
	public void setPlaceDirection(int placeDirection) {
		((C08PacketPlayerBlockPlacement) nativePacket).placedBlockDirection = placeDirection;
	}
}
