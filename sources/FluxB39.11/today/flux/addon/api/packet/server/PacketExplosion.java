package today.flux.addon.api.packet.server;

import com.soterdev.SoterObfuscator;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.util.BlockPos;
import today.flux.addon.api.packet.AddonPacket;
import today.flux.addon.api.utils.BlockPosition;
import today.flux.addon.api.utils.Motion;
import today.flux.addon.api.utils.EntityPosition;

import java.util.ArrayList;
import java.util.List;

public class PacketExplosion extends AddonPacket {
	public PacketExplosion(S27PacketExplosion packet) {
		super(packet);
	}

	
	public EntityPosition getPosition() {
		S27PacketExplosion packet = ((S27PacketExplosion) nativePacket);
		return new EntityPosition(packet.getX(), packet.getY(), packet.getZ());
	}

	
	public void setPosition(EntityPosition position) {
		S27PacketExplosion packet = ((S27PacketExplosion) nativePacket);
		packet.posX = position.x;
		packet.posY = position.y;
		packet.posZ = position.z;
	}

	
	public float getStrength() {
		return ((S27PacketExplosion) nativePacket).strength;
	}

	
	public void setStrength(float strength) {
		((S27PacketExplosion) nativePacket).strength = strength;
	}

	
	public List<BlockPosition> getAffectedBlockPositions() {
		ArrayList<BlockPosition> list = new ArrayList<>();
		for (BlockPos pos : ((S27PacketExplosion) nativePacket).affectedBlockPositions) {
			list.add(BlockPosition.getBlockPosition(pos));
		}
		return list;
	}

	
	public void setAffectedBlockPositions(List<BlockPosition> affectedBlockPositions) {
		ArrayList<BlockPos> list = new ArrayList<>();
		for (BlockPosition pos : affectedBlockPositions) {
			list.add(pos.getNativeBlockPos());
		}
		((S27PacketExplosion) nativePacket).affectedBlockPositions = list;
	}

	
	public Motion getMotion() {
		S27PacketExplosion packet = ((S27PacketExplosion) nativePacket);
		return new Motion(packet.motionX, packet.motionY, packet.motionZ);
	}

	
	public void setMotion(Motion motion) {
		S27PacketExplosion packet = ((S27PacketExplosion) nativePacket);
		packet.motionX = (float) motion.x;
		packet.motionY = (float) motion.y;
		packet.motionZ = (float) motion.z;
	}
}
