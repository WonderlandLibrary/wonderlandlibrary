package Method.Client.utils;


import Method.Client.utils.system.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;
import java.util.List;

public final class BlockUtils {
    public static List<Block> emptyBlocks;

    static {
        BlockUtils.emptyBlocks = Arrays.asList(Blocks.AIR, Blocks.FLOWING_LAVA, Blocks.LAVA, Blocks.FLOWING_WATER, Blocks.WATER, Blocks.VINE, Blocks.SNOW_LAYER, Blocks.TALLGRASS, Blocks.FIRE);
    }

    public static Material getMaterial(BlockPos pos) {
        return getState(pos).getMaterial();
    }

    public static IBlockState getState(BlockPos pos) {
        return Wrapper.INSTANCE.mc().world.getBlockState(pos);
    }

    public static Block getBlock(BlockPos pos) {
        return getState(pos).getBlock();
    }

    public static boolean canBeClicked(BlockPos pos) {
        return getBlock(pos).canCollideCheck(getState(pos), false);
    }

    public static float getHardness(BlockPos pos) {
        return getState(pos).getPlayerRelativeBlockHardness(Wrapper.INSTANCE.player(), Wrapper.INSTANCE.world(), pos);
    }

    public static boolean CanPlaceBlock(BlockPos target_pos) {
        // we do this instead of
        //  return Wrapper.mc.world.checkNoEntityCollision(new AxisAlignedBB(target_pos));
        // so that we can ignore entities like xporbs and item entities
        try {
            return Wrapper.mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(target_pos))
                    .stream().noneMatch(entity -> !(entity instanceof EntityItem) && !(entity instanceof EntityXPOrb))
                    && Wrapper.mc.world.getBlockState(target_pos).getMaterial().isReplaceable();
        } catch (Exception ignored) {
        }
        return false;
    }

    public static boolean breakBlockSimple(BlockPos pos) {
        EnumFacing side = null;
        EnumFacing[] facings = EnumFacing.values();
        for (EnumFacing f : facings) {
            Vec3d visibleSide;
            visibleSide = canSeeBlockSide(pos.offset(f), f.getOpposite());
            if (visibleSide != null) {
                side = f;
                break;
            }
        }
        if (side != null) {
            if (!Wrapper.INSTANCE.controller().onPlayerDamageBlock(pos, side))
                return false;
            Wrapper.INSTANCE.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
        }
        return true;
    }


    public static void breakBlockPacketSpam(BlockPos blocks) {
        Vec3d eyesPos = Utils.getEyesPos();
        NetHandlerPlayClient connection = Wrapper.INSTANCE.player().connection;

        Vec3d posVec = new Vec3d(blocks).add(0.5, 0.5, 0.5);
        double distanceSqPosVec = eyesPos.squareDistanceTo(posVec);

        for (EnumFacing side : EnumFacing.values()) {
            Vec3d hitVec = posVec.add(new Vec3d(side.getDirectionVec()).scale(0.5));

            if (eyesPos.squareDistanceTo(hitVec) >= distanceSqPosVec)
                continue;

            connection.sendPacket(new CPacketPlayerDigging(
                    CPacketPlayerDigging.Action.START_DESTROY_BLOCK, blocks, side));
            connection.sendPacket(new CPacketPlayerDigging(
                    CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blocks, side));

            break;
        }

    }

    public static void placeBlock(BlockPos pos, boolean rotate, boolean canSeeBlockSide, EnumHand hand) {
        if (!isBlockEmpty(pos) && canSeeBlockSide) {
            return;
        }

        EnumFacing[] facings = EnumFacing.values();
        for (EnumFacing f : facings) {
            Vec3d visibleSide;
            if (canSeeBlockSide) {
                //BlockPos pos, EnumFacing facing, Vec3d point
                if (canSeeBlockSidePoint(pos.offset(f), f.getOpposite(), getBlockSideCenter(pos.offset(f), f.getOpposite())))
                    visibleSide = getBlockSideCenter(pos.offset(f), f.getOpposite());
                else
                    visibleSide = canSeeBlockSide(pos.offset(f), f.getOpposite());
            } else visibleSide = getBlockSideCenter(pos.offset(f), f.getOpposite());
            if (visibleSide == null) {
                continue;
            }
            Block neighborBlock = Wrapper.mc.world.getBlockState(pos.offset(f)).getBlock();
            if (canSeeBlockSide)
                if (emptyBlocks.contains(neighborBlock) || Wrapper.mc.player.getPositionEyes(Wrapper.mc.getRenderPartialTicks()).distanceTo(visibleSide) > 4.3D) {
                    continue;
                }

            float[] rot = new float[]{Wrapper.mc.player.rotationYaw, Wrapper.mc.player.rotationPitch};
            if (rotate) {
                final float[] array = Utils.getNeededRotations(visibleSide, 0, 0);
                Wrapper.mc.player.connection.sendPacket(new CPacketPlayer.Rotation(array[0], array[1], Wrapper.mc.player.onGround));
            }

            boolean activated = neighborBlock.onBlockActivated(Wrapper.mc.world, pos, Wrapper.mc.world.getBlockState(pos), Wrapper.mc.player, EnumHand.MAIN_HAND, f, 0, 0, 0);

            if (activated)
                Wrapper.mc.player.connection.sendPacket(new CPacketEntityAction(Wrapper.mc.player, CPacketEntityAction.Action.START_SNEAKING));


            Wrapper.mc.playerController.processRightClickBlock(Wrapper.mc.player, Wrapper.mc.world, pos.offset(f), f.getOpposite(), new Vec3d(pos), EnumHand.MAIN_HAND);

            if (activated)
                Wrapper.mc.player.connection.sendPacket(new CPacketEntityAction(Wrapper.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));


            if (rotate) {
                Wrapper.mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rot[0], rot[1], Wrapper.mc.player.onGround));
            }

            Wrapper.mc.player.swingArm(hand);
            return;
        }
    }


    public static void placeEntity(BlockPos pos, boolean rotate, boolean canSeeBlockSide, EnumHand hand) {
        if (isBlockEmpty(pos) && canSeeBlockSide) {
            return;
        }

        EnumFacing[] facings = EnumFacing.values();
        for (EnumFacing f : facings) {
            Vec3d visibleSide;
            if (canSeeBlockSide) {
                if (canSeeBlockSidePoint(pos, f, getBlockSideCenter(pos, f)))
                    visibleSide = getBlockSideCenter(pos, f);
                else
                    visibleSide = canSeeBlockSide(pos, f);
            } else visibleSide = getBlockSideCenter(pos, f);
            if (visibleSide == null) {
                continue;
            }

            if (canSeeBlockSide)
                if (Wrapper.mc.player.getPositionEyes(Wrapper.mc.getRenderPartialTicks()).distanceTo(visibleSide) > 4.3D) {
                    continue;
                }

            float[] rot = new float[]{Wrapper.mc.player.rotationYaw, Wrapper.mc.player.rotationPitch};
            if (rotate) {
                final float[] array = Utils.getNeededRotations(visibleSide, 0, 0);
                Wrapper.mc.player.connection.sendPacket(new CPacketPlayer.Rotation(array[0], array[1], Wrapper.mc.player.onGround));
            }
            Vec3d vec = new Vec3d(pos).add(0.5, 0.5, 0.5).add(new Vec3d(f.getDirectionVec()).scale(0.5));

            float f2 = (float) (vec.x - (double) pos.getX());
            float f3 = (float) (vec.y - (double) pos.getY());
            float f4 = (float) (vec.z - (double) pos.getZ());

            Wrapper.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, f, hand, f2, f3, f4));
            if (rotate) {
                Wrapper.mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rot[0], rot[1], Wrapper.mc.player.onGround));
            }
            return;
        }
    }

    public static boolean isBlockEmpty(BlockPos pos) {
        if (emptyBlocks.contains(Wrapper.mc.world.getBlockState(pos).getBlock())) {
            return CanPlaceBlock(pos);
        }
        return false;
    }

    private static final double OFFSET = 0.01;

    public static Vec3d canSeeBlockSide(BlockPos pos, EnumFacing facing) {
        Vec3d blockCenter = getBlockSideCenter(pos, facing);
        double xOffset = facing.getXOffset() == 0 ? 0.5D - OFFSET : 0;
        double yOffset = facing.getYOffset() == 0 ? 0.5D - OFFSET : 0;
        double zOffset = facing.getZOffset() == 0 ? 0.5D - OFFSET : 0;
        int gridSize = 10;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                double xGridOffset = (i - gridSize / 2) * xOffset / (gridSize / 2);
                double yGridOffset = (i - gridSize / 2) * yOffset / (gridSize / 2);
                double zGridOffset = (i - gridSize / 2) * zOffset / (gridSize / 2);

                double xGridOffset2 = (j - gridSize / 2) * xOffset / (gridSize / 2);
                double yGridOffset2 = (j - gridSize / 2) * yOffset / (gridSize / 2);
                double zGridOffset2 = (j - gridSize / 2) * zOffset / (gridSize / 2);
                if (canSeeBlockSidePoint(pos, facing, blockCenter.add(xGridOffset + xGridOffset2, yGridOffset + yGridOffset2, zGridOffset + zGridOffset2))) {
                    return blockCenter.add(xGridOffset + xGridOffset2, yGridOffset + yGridOffset2, zGridOffset + zGridOffset2);
                }
            }
        }
        return null;
    }

    private static boolean canSeeBlockSidePoint(BlockPos pos, EnumFacing facing, Vec3d point) {
        Vec3d playerPos = Wrapper.mc.player.getPositionEyes(Wrapper.mc.getRenderPartialTicks());
        RayTraceResult result = Wrapper.mc.world.rayTraceBlocks(playerPos, point, false, false, false);
        if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
            return result.getBlockPos().equals(pos) && result.sideHit == facing;
        }
        return false;
    }

    public static Vec3d getBlockSideCenter(BlockPos pos, EnumFacing facing) {
        Vec3d blockCenter = new Vec3d(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
        Vec3d facingOffset = new Vec3d(
                facing.getXOffset() * 0.5D - OFFSET * facing.getXOffset(),
                facing.getYOffset() * 0.5D - OFFSET * facing.getYOffset(),
                facing.getZOffset() * 0.5D - OFFSET * facing.getZOffset()
        );
        return blockCenter.add(facingOffset);
    }


}
