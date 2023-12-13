package cn.hanabi.utils.rotation.blocks;

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
      this(blockPos, enumFacing, new Vec3((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5));
   }

   public static PlaceInfo get(BlockPos blockPos) {
      BlockPos blockPos6;
      if (BlockUtils.canBeClicked(blockPos.add(0, -1, 0))) {
         blockPos6 = blockPos.add(0, -1, 0);
         return new PlaceInfo(blockPos6, EnumFacing.UP);
      } else if (BlockUtils.canBeClicked(blockPos.add(0, 0, 1))) {
         blockPos6 = blockPos.add(0, 0, 1);
         return new PlaceInfo(blockPos6, EnumFacing.NORTH);
      } else if (BlockUtils.canBeClicked(blockPos.add(-1, 0, 0))) {
         blockPos6 = blockPos.add(-1, 0, 0);
         return new PlaceInfo(blockPos6, EnumFacing.EAST);
      } else if (BlockUtils.canBeClicked(blockPos.add(0, 0, -1))) {
         blockPos6 = blockPos.add(0, 0, -1);
         return new PlaceInfo(blockPos6, EnumFacing.SOUTH);
      } else {
         PlaceInfo placeInfo;
         if (BlockUtils.canBeClicked(blockPos.add(1, 0, 0))) {
            blockPos6 = blockPos.add(1, 0, 0);
            placeInfo = new PlaceInfo(blockPos6, EnumFacing.WEST);
         } else {
            placeInfo = null;
         }

         return placeInfo;
      }
   }
}
