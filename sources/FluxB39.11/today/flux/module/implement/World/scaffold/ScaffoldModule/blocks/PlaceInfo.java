package today.flux.module.implement.World.scaffold.ScaffoldModule.blocks;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public final class PlaceInfo {

	private final BlockPos blockPos;

	private final EnumFacing enumFacing;

	private Vec3 vec3;

	public final BlockPos getBlockPos() {
		return this.blockPos;
	}

	public final EnumFacing getEnumFacing() {
		return this.enumFacing;
	}

	public final Vec3 getVec3() {
		return this.vec3;
	}

	public final void setVec3(Vec3 vec3) {
		this.vec3 = vec3;
	}

	public PlaceInfo(BlockPos blockPos, EnumFacing enumFacing, Vec3 vec3) {
		this.blockPos = blockPos;
		this.enumFacing = enumFacing;
		this.vec3 = vec3;
	}

	public PlaceInfo(BlockPos blockPos, EnumFacing enumFacing) {
		this(blockPos, enumFacing, new Vec3((double) blockPos.getX() + 0.5, (double) blockPos.getY() + 0.5,
				(double) blockPos.getZ() + 0.5));
	}

	public static PlaceInfo get(BlockPos blockPos) {
		PlaceInfo placeInfo;
		if (BlockUtils.canBeClicked(blockPos.add(0, -1, 0))) {
			BlockPos blockPos2 = blockPos.add(0, -1, 0);
			return new PlaceInfo(blockPos2, EnumFacing.UP);
		}
		if (BlockUtils.canBeClicked(blockPos.add(0, 0, 1))) {
			BlockPos blockPos3 = blockPos.add(0, 0, 1);
			return new PlaceInfo(blockPos3, EnumFacing.NORTH);
		}
		if (BlockUtils.canBeClicked(blockPos.add(-1, 0, 0))) {
			BlockPos blockPos4 = blockPos.add(-1, 0, 0);
			return new PlaceInfo(blockPos4, EnumFacing.EAST);
		}
		if (BlockUtils.canBeClicked(blockPos.add(0, 0, -1))) {
			BlockPos blockPos5 = blockPos.add(0, 0, -1);
			return new PlaceInfo(blockPos5, EnumFacing.SOUTH);
		}
		if (BlockUtils.canBeClicked(blockPos.add(1, 0, 0))) {
			BlockPos blockPos6;
			blockPos6 = blockPos.add(1, 0, 0);
			placeInfo = new PlaceInfo(blockPos6, EnumFacing.WEST);
		} else {
			placeInfo = null;
		}
		return placeInfo;
	}

}
