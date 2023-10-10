package today.flux.utility;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.border.WorldBorder;

public class PlaceInfo {
    private BlockPos blockPos;
    private EnumFacing enumFacing;
    private Vec3 vec3;
    public static Companion Companion = new Companion();

    public PlaceInfo(BlockPos blockPos, EnumFacing enumFacing, Vec3 vec3) {
        this.blockPos = blockPos;
        this.enumFacing = enumFacing;
        this.vec3 = vec3;
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public EnumFacing getEnumFacing() {
        return this.enumFacing;
    }

    public Vec3 getVec3() {
        return this.vec3;
    }

    public void setVec3(Vec3 vec3) {
        this.vec3 = vec3;
    }

    public PlaceInfo(BlockPos blockPos, EnumFacing enumFacing, Vec3 vec3, int n) {
        this(blockPos, enumFacing, vec3);
        if ((n & 4) != 0) {
            vec3 = new Vec3((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5);
        }
    }

    public static PlaceInfo get(BlockPos blockPos) {
        return Companion.get(blockPos);
    }

    public static Block getBlock(BlockPos blockPos) {
        IBlockState var1;
        WorldClient var10000 = Minecraft.getMinecraft().theWorld;
        if (var10000 != null && (var1 = var10000.getBlockState(blockPos)) != null) {
            return var1.getBlock();
        }
        return null;
    }

    public static IBlockState getState(BlockPos blockPos) {
        return Minecraft.getMinecraft().theWorld.getBlockState(blockPos);
    }

    public static boolean canBeClicked(BlockPos blockPos) {
        Block var10000 = PlaceInfo.getBlock(blockPos);
        if (var10000 != null && var10000.canCollideCheck(PlaceInfo.getState(blockPos), false)) {
            WorldClient var1 = Minecraft.getMinecraft().theWorld;
            if (var1.getWorldBorder().contains(blockPos)) {
                return true;
            }
        }
        return false;
    }
}

class Companion {
    public  PlaceInfo get(BlockPos blockPos) {
        if (PlaceInfo.canBeClicked((BlockPos)blockPos.add(0, -1, 0))) {
            BlockPos blockPos2 = blockPos.add(0, -1, 0);
            return new PlaceInfo(blockPos2, EnumFacing.UP, null, 4);
        }
        if (PlaceInfo.canBeClicked((BlockPos)blockPos.add(0, 0, 1))) {
            BlockPos blockPos3 = blockPos.add(0, 0, 1);
            return new PlaceInfo(blockPos3, EnumFacing.NORTH, null, 4);
        }
        if (PlaceInfo.canBeClicked((BlockPos)blockPos.add(-1, 0, 0))) {
            BlockPos blockPos4 = blockPos.add(-1, 0, 0);
            return new PlaceInfo(blockPos4, EnumFacing.EAST, null, 4);
        }
        if (PlaceInfo.canBeClicked((BlockPos)blockPos.add(0, 0, -1))) {
            BlockPos blockPos5 = blockPos.add(0, 0, -1);
            return new PlaceInfo(blockPos5, EnumFacing.SOUTH, null, 4);
        }
        if (PlaceInfo.canBeClicked((BlockPos)blockPos.add(1, 0, 0))) {
            BlockPos blockPos6 = blockPos.add(1, 0, 0);
            return new PlaceInfo(blockPos6, EnumFacing.WEST, null, 4);
        }
        PlaceInfo placeInfo = null;
        return placeInfo;
    }

}

