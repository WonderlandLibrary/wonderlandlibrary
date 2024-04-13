package net.smoothboot.client.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class BlockUtil {

    protected static MinecraftClient mc = MinecraftClient.getInstance();

    public static BlockState getState(BlockPos pos)
    {
        return mc.world.getBlockState(pos);
    }

    public static BlockState getBlockState(BlockPos pos) {
        return mc.world.getBlockState(pos);
    }
    public static boolean isBlock(Block block, BlockPos pos) {
        return getBlockState(pos).getBlock() == block;
    }

    public static boolean isAnchorCharged(BlockPos anchor)
    {
        if (!isBlock(Blocks.RESPAWN_ANCHOR, anchor))
            return false;
        try
        {
            return BlockUtil.getBlockState(anchor).get(RespawnAnchorBlock.CHARGES) != 0;
        }
        catch (IllegalArgumentException e)
        {
            return false;
        }
    }

    private static VoxelShape getOutlineShape(BlockPos pos)
    {
        return getState(pos).getOutlineShape(mc.world, pos);
    }
    public static Box getBoundingBox(BlockPos pos)
    {
        return getOutlineShape(pos).getBoundingBox().offset(pos);
    }

    public static boolean canBeClicked(BlockPos pos)
    {
        return getOutlineShape(pos) != VoxelShapes.empty();
    }


    public static ActionResult interact(BlockPos pos, Direction dir) {
        Vec3d vec = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
        return interact(vec,dir);
    }

    public static ActionResult interact(Vec3d vec3d, Direction dir) {
        Vec3i vec3i = new Vec3i((int) vec3d.x, (int) vec3d.y, (int) vec3d.z);
        BlockPos pos = new BlockPos(vec3i);
        BlockHitResult result = new BlockHitResult(vec3d, dir,pos,false);
        return mc.interactionManager.interactBlock(mc.player, mc.player.getActiveHand(),result);
    }

    public static BlockHitResult clientRaycastBlock(BlockPos pos)
    {
        return mc.world.raycastBlock(RotationUtil.getEyesPos(), RotationUtil.getClientLookVec().multiply(6.0).add(RotationUtil.getEyesPos()), pos, getBlockState(pos).getOutlineShape(mc.world, pos), getBlockState(pos));
    }
}
