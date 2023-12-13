package me.dev.legacy.util;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TestUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static List<Block> emptyBlocks = Arrays.asList(Blocks.AIR, Blocks.FLOWING_LAVA, Blocks.LAVA, Blocks.FLOWING_WATER, Blocks.WATER, Blocks.VINE, Blocks.SNOW_LAYER, Blocks.TALLGRASS, Blocks.FIRE);
    public static List<Block> rightclickableBlocks = Arrays.asList(Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.ENDER_CHEST, Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.ANVIL, Blocks.WOODEN_BUTTON, Blocks.STONE_BUTTON, Blocks.UNPOWERED_COMPARATOR, Blocks.UNPOWERED_REPEATER, Blocks.POWERED_REPEATER, Blocks.POWERED_COMPARATOR, Blocks.OAK_FENCE_GATE, Blocks.SPRUCE_FENCE_GATE, Blocks.BIRCH_FENCE_GATE, Blocks.JUNGLE_FENCE_GATE, Blocks.DARK_OAK_FENCE_GATE, Blocks.ACACIA_FENCE_GATE, Blocks.BREWING_STAND, Blocks.DISPENSER, Blocks.DROPPER, Blocks.LEVER, Blocks.NOTEBLOCK, Blocks.JUKEBOX, Blocks.BEACON, Blocks.BED, Blocks.FURNACE, Blocks.OAK_DOOR, Blocks.SPRUCE_DOOR, Blocks.BIRCH_DOOR, Blocks.JUNGLE_DOOR, Blocks.ACACIA_DOOR, Blocks.DARK_OAK_DOOR, Blocks.CAKE, Blocks.ENCHANTING_TABLE, Blocks.DRAGON_EGG, Blocks.HOPPER, Blocks.REPEATING_COMMAND_BLOCK, Blocks.COMMAND_BLOCK, Blocks.CHAIN_COMMAND_BLOCK, Blocks.CRAFTING_TABLE);

    public static boolean canSeeBlock(BlockPos p_Pos) {
        return TestUtil.mc.player != null && TestUtil.mc.world.rayTraceBlocks(new Vec3d(TestUtil.mc.player.posX, TestUtil.mc.player.posY + (double) TestUtil.mc.player.getEyeHeight(), TestUtil.mc.player.posZ), new Vec3d(p_Pos.getX(), p_Pos.getY(), p_Pos.getZ()), false, true, false) == null;
    }

    public static void placeCrystalOnBlock(BlockPos pos, EnumHand hand) {
        RayTraceResult result = TestUtil.mc.world.rayTraceBlocks(new Vec3d(TestUtil.mc.player.posX, TestUtil.mc.player.posY + (double) TestUtil.mc.player.getEyeHeight(), TestUtil.mc.player.posZ), new Vec3d((double) pos.getX() + 0.5, (double) pos.getY() - 0.5, (double) pos.getZ() + 0.5));
        EnumFacing facing = result == null || result.sideHit == null ? EnumFacing.UP : result.sideHit;
        TestUtil.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, facing, hand, 0.0f, 0.0f, 0.0f));
    }

    public static boolean rayTracePlaceCheck(BlockPos pos, boolean shouldCheck, float height) {
        return !shouldCheck || TestUtil.mc.world.rayTraceBlocks(new Vec3d(TestUtil.mc.player.posX, TestUtil.mc.player.posY + (double) TestUtil.mc.player.getEyeHeight(), TestUtil.mc.player.posZ), new Vec3d(pos.getX(), (float) pos.getY() + height, pos.getZ()), false, true, false) == null;
    }

    public static boolean rayTracePlaceCheck(BlockPos pos, boolean shouldCheck) {
        return TestUtil.rayTracePlaceCheck(pos, shouldCheck, 1.0f);
    }

    public static void openBlock(BlockPos pos) {
        EnumFacing[] facings;
        for (EnumFacing f : facings = EnumFacing.values()) {
            Block neighborBlock = TestUtil.mc.world.getBlockState(pos.offset(f)).getBlock();
            if (!emptyBlocks.contains(neighborBlock)) continue;
            TestUtil.mc.playerController.processRightClickBlock(TestUtil.mc.player, TestUtil.mc.world, pos, f.getOpposite(), new Vec3d(pos), EnumHand.MAIN_HAND);
            return;
        }
    }

    public static boolean placeBlock(BlockPos pos) {
        if (TestUtil.isBlockEmpty(pos)) {
            EnumFacing[] facings;
            for (EnumFacing f : facings = EnumFacing.values()) {
                Block neighborBlock = TestUtil.mc.world.getBlockState(pos.offset(f)).getBlock();
                Vec3d vec = new Vec3d((double) pos.getX() + 0.5 + (double) f.getXOffset() * 0.5, (double) pos.getY() + 0.5 + (double) f.getYOffset() * 0.5, (double) pos.getZ() + 0.5 + (double) f.getZOffset() * 0.5);
                if (emptyBlocks.contains(neighborBlock) || !(TestUtil.mc.player.getPositionEyes(mc.getRenderPartialTicks()).distanceTo(vec) <= 4.25))
                    continue;
                float[] rot = new float[]{TestUtil.mc.player.rotationYaw, TestUtil.mc.player.rotationPitch};
                if (rightclickableBlocks.contains(neighborBlock)) {
                    TestUtil.mc.player.connection.sendPacket(new CPacketEntityAction(TestUtil.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                }
                TestUtil.mc.playerController.processRightClickBlock(TestUtil.mc.player, TestUtil.mc.world, pos.offset(f), f.getOpposite(), new Vec3d(pos), EnumHand.MAIN_HAND);
                if (rightclickableBlocks.contains(neighborBlock)) {
                    TestUtil.mc.player.connection.sendPacket(new CPacketEntityAction(TestUtil.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                }
                TestUtil.mc.player.swingArm(EnumHand.MAIN_HAND);
                return true;
            }
        }
        return false;
    }

    public static boolean isBlockEmpty(BlockPos pos) {
        try {
            if (emptyBlocks.contains(TestUtil.mc.world.getBlockState(pos).getBlock())) {
                Entity e;
                AxisAlignedBB box = new AxisAlignedBB(pos);
                Iterator entityIter = TestUtil.mc.world.loadedEntityList.iterator();
                do {
                    if (entityIter.hasNext()) continue;
                    return true;
                } while (!((e = (Entity) entityIter.next()) instanceof EntityLivingBase) || !box.intersects(e.getEntityBoundingBox()));
            }
        } catch (Exception exception) {
            // empty catch block
        }
        return false;
    }

    public static boolean canPlaceBlock(BlockPos pos) {
        if (TestUtil.isBlockEmpty(pos)) {
            EnumFacing[] facings;
            for (EnumFacing f : facings = EnumFacing.values()) {
                if (emptyBlocks.contains(TestUtil.mc.world.getBlockState(pos.offset(f)).getBlock())) continue;
                Vec3d vec3d = new Vec3d((double) pos.getX() + 0.5 + (double) f.getXOffset() * 0.5, (double) pos.getY() + 0.5 + (double) f.getYOffset() * 0.5, (double) pos.getZ() + 0.5 + (double) f.getZOffset() * 0.5);
                if (!(TestUtil.mc.player.getPositionEyes(mc.getRenderPartialTicks()).distanceTo(vec3d) <= 4.25))
                    continue;
                return true;
            }
        }
        return false;
    }
}

