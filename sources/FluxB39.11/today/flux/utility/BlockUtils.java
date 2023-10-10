package today.flux.utility;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.util.*;

import java.util.Objects;

public class BlockUtils {
    public static Minecraft mc = Minecraft.getMinecraft();

    public static String getBlockName(Block block) {
        if (block == Blocks.air) {
            return null;
        } else {
            Item item = Item.getItemFromBlock(block);
            ItemStack itemStack = item != null ? new ItemStack(Item.getByNameOrId(block.getUnlocalizedName()), 1, 0) : null;
            String name = itemStack == null ? block.getLocalizedName() : item.getItemStackDisplayName(itemStack);
            return name.length() > 5 && name.startsWith("tile.") ? block.getUnlocalizedName() : name;
        }
    }

    public static boolean stairCollision() {
        return getBlockAtPosC(mc.thePlayer, 0.3100000023841858, 0.0, 0.3100000023841858) instanceof BlockStairs || getBlockAtPosC(mc.thePlayer, -0.3100000023841858, 0.0, -0.3100000023841858) instanceof BlockStairs || getBlockAtPosC(mc.thePlayer, 0.3100000023841858, 0.0, -0.3100000023841858) instanceof BlockStairs || getBlockAtPosC(mc.thePlayer, -0.3100000023841858, 0.0, 0.3100000023841858) instanceof BlockStairs || getBlockatPosSpeed(mc.thePlayer, 1.05f, 1.05f) instanceof BlockStairs;
    }

    public static Block getBlockatPosSpeed(final EntityPlayer inPlayer, final float x, final float z) {
        final double posX = inPlayer.posX + inPlayer.motionX * x;
        final double posZ = inPlayer.posZ + inPlayer.motionZ * z;
        return getBlockAtPos(new BlockPos(posX, inPlayer.posY, posZ));
    }

    public static Block getBlockAtPos(final BlockPos inBlockPos) {
        final IBlockState s = mc.theWorld.getBlockState(inBlockPos);
        return s.getBlock();
    }

    public static void placeBlock(BlockPos pos, EnumFacing facing) {
        ItemStack heldItem = mc.thePlayer.inventory.getCurrentItem();
        if (heldItem == null)
            return;

        mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, heldItem, pos, facing, new Vec3(pos.getX(), pos.getY(), pos.getZ()));
    }

    public static double getDistanceToFall() {
        double distance = 0.0;
        for (double i = mc.thePlayer.posY; i > 0.0; --i) {
            final Block block = BlockUtils.getBlock(new BlockPos(mc.thePlayer.posX, i, mc.thePlayer.posZ));
            if (block.getMaterial() != Material.air && block.isBlockNormalCube() && block.isCollidable()) {
                distance = i;
                break;
            }
            if (i < 0.0) {
                break;
            }
        }
        final double distancetofall = mc.thePlayer.posY - distance - 1.0;
        return distancetofall;
    }

    public static Block getBlockUnderPlayer(final EntityPlayer inPlayer, final double height) {
        return getBlock(new BlockPos(inPlayer.posX, inPlayer.posY - height, inPlayer.posZ));
    }

    public static Block getBlockAtPosC(final EntityPlayer inPlayer, final double x, final double y, final double z) {
        return getBlock(new BlockPos(inPlayer.posX - x, inPlayer.posY - y, inPlayer.posZ - z));
    }


    public static boolean isOnLiquidFull() {
        boolean onLiquid = false;
        if (getBlockAtPosC(mc.thePlayer, 0.30000001192092896, 0.10000000149011612, 0.30000001192092896).getMaterial().isLiquid() && getBlockAtPosC(mc.thePlayer, -0.30000001192092896, 0.10000000149011612, -0.30000001192092896).getMaterial().isLiquid()) {
            if (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX - 0.3, mc.thePlayer.posY - 0.1, mc.thePlayer.posZ - 0.3)).getValue(BlockLiquid.LEVEL) == 0 && mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX + 0.3, mc.thePlayer.posY - 0.1, mc.thePlayer.posZ + 0.3)).getValue(BlockLiquid.LEVEL) == 0) {
                onLiquid = true;
            }
        }
        return onLiquid;
    }

    public static boolean isReallyOnGround() {
        Entity entity = mc.thePlayer;
        double y = entity.getEntityBoundingBox().offset(0.0D, -0.01D, 0.0D).minY;
        Block block = mc.theWorld.getBlockState(new BlockPos(entity.posX, y, entity.posZ)).getBlock();
        if (block != null && !(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
            return entity.onGround;
        }

        return false;
    }


    public static Block getBlock(final double x, final double y, final double z) {
        return mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
    }

    public static Block getBlock(final BlockPos block) {
        return mc.theWorld.getBlockState(block).getBlock();
    }

    public static Block getBlock(final Entity entity, final double offsetY) {
        if (entity == null) {
            return null;
        }
        final int y = (int) entity.getEntityBoundingBox().offset(0.0, offsetY, 0.0).minY;
        for (int x = MathHelper.floor_double(entity.getEntityBoundingBox().minX); x < MathHelper.floor_double(entity.getEntityBoundingBox().maxX) + 1; ++x) {
            final int z = MathHelper.floor_double(entity.getEntityBoundingBox().minZ);
            if (z < MathHelper.floor_double(entity.getEntityBoundingBox().maxZ) + 1) {
                return mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
            }
        }
        return null;
    }

    public static boolean isOnLiquid() {
        AxisAlignedBB axisAlignedBB = mc.thePlayer.getEntityBoundingBox().offset(0.0, -0.1, 0.0).contract(0.001D, 0.0D, 0.001D);

        int xMin = MathHelper.floor_double(axisAlignedBB.minX);
        int xMax = MathHelper.floor_double(axisAlignedBB.maxX + 1.0);
        int yMin = MathHelper.floor_double(axisAlignedBB.minY);
        int yMax = MathHelper.floor_double(axisAlignedBB.maxY + 1.0);
        int zMin = MathHelper.floor_double(axisAlignedBB.minZ);
        int zMax = MathHelper.floor_double(axisAlignedBB.maxZ + 1.0);

        boolean gotcha = false;

        for (int y = yMin; y < yMax; y++) {
            for (int x = xMin; x < xMax; x++) {
                for (int z = zMin; z < zMax; z++) {
                    Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();

                    if (block instanceof BlockLiquid)
                        gotcha = true;

                    if (!(block instanceof BlockLiquid) && block.getCollisionBoundingBox(mc.theWorld, new BlockPos(x, y, z), mc.theWorld.getBlockState(new BlockPos(x, y, z))) != null) {
                        return false;
                    }
                }
            }
        }

        return gotcha;
    }

    public static boolean isOnLiquidFixed() {
        return mc.theWorld.handleMaterialAcceleration(mc.thePlayer.getEntityBoundingBox().expand(0.0D, -0.4000000059604645D, 0.0D).contract(0.001D, 0.001D, 0.001D), Material.water, mc.thePlayer);
    }


    public static boolean isInLiquid() {
        final AxisAlignedBB par1AxisAlignedBB = mc.thePlayer.getEntityBoundingBox().contract(0.001, 0.001,
                0.001);
        final int var4 = MathHelper.floor_double(par1AxisAlignedBB.minX);
        final int var5 = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0);
        final int var6 = MathHelper.floor_double(par1AxisAlignedBB.minY);
        final int var7 = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0);
        final int var8 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
        final int var9 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0);
        for (int var11 = var4; var11 < var5; ++var11) {
            for (int var12 = var6; var12 < var7; ++var12) {
                for (int var13 = var8; var13 < var9; ++var13) {
                    final BlockPos pos = new BlockPos(var11, var12, var13);
                    final Block var14 = mc.theWorld.getBlockState(pos).getBlock();
                    if (var14 instanceof BlockLiquid) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean CanStep() {
        final AxisAlignedBB par1AxisAlignedBB = mc.thePlayer.getEntityBoundingBox().contract(0, 0.001, 0);
        final int var6 = MathHelper.floor_double(par1AxisAlignedBB.minY);
        final int var7 = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0);
        for (int y = var6; y < var7; ++y) {
            final BlockPos pos = new BlockPos(mc.thePlayer.posX, y, mc.thePlayer.posZ);
            final Block var14 = mc.theWorld.getBlockState(pos).getBlock();
            if (var14.isFullBlock()) {
                return true;
            }
        }

        return false;
    }

    public static boolean isOnLadder() {
        boolean onLadder = false;
        final int y = (int) mc.thePlayer.getEntityBoundingBox().offset(0.0, 1.0, 0.0).minY;
        for (int x = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
                final Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (Objects.nonNull(block) && !(block instanceof BlockAir)) {
                    if (!(block instanceof BlockLadder) && !(block instanceof BlockVine)) {
                        return false;
                    }
                    onLadder = true;
                }
            }
        }
        return onLadder || mc.thePlayer.isOnLadder();
    }

    public static boolean isInsideBlock() {
        for (int x = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper
                .floor_double(mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int y = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minY); y < MathHelper
                    .floor_double(mc.thePlayer.getEntityBoundingBox().maxY) + 1; ++y) {
                for (int z = MathHelper
                        .floor_double(mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper
                        .floor_double(mc.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
                    final Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z))
                            .getBlock();
                    final AxisAlignedBB boundingBox;
                    if (block != null && !(block instanceof BlockAir)
                            && (boundingBox = block.getCollisionBoundingBox(mc.theWorld,
                            new BlockPos(x, y, z),
                            mc.theWorld.getBlockState(new BlockPos(x, y, z)))) != null
                            && mc.thePlayer.getEntityBoundingBox().intersectsWith(boundingBox)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean canBreak(BlockPos pos) {
        final IBlockState blockState = mc.theWorld.getBlockState(pos);
        final Block block = blockState.getBlock();
        return block.getBlockHardness(mc.theWorld, pos) != -1;
    }

    public static float[] getBlockRotations(double x, double y, double z) {
        double var4 = x - mc.thePlayer.posX + 0.5;
        double var6 = z - mc.thePlayer.posZ + 0.5;
        double var8 = y - (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight() - 1.0);
        double var14 = MathHelper.sqrt_double(var4 * var4 + var6 * var6);
        float var12 = (float) (Math.atan2(var6, var4) * 180.0 / 3.141592653589793) - 90.0f;
        return new float[]{var12, (float) (-Math.atan2(var8, var14) * 180.0 / 3.141592653589793)};
    }

    private static float[] getRotationsNeeded(Entity entity) {
        double posX = entity.posX - mc.thePlayer.posX;
        double posY = entity.posY - (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight());
        double posZ = entity.posZ - mc.thePlayer.posZ;
        double var14 = MathHelper.sqrt_double(posX * posX + posZ * posZ);
        float yaw = (float) (Math.atan2(posZ, posX) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) (-(Math.atan2(posY, var14) * 180.0D / Math.PI));
        return new float[]{yaw, pitch};
    }

    public static boolean isReplaceable(BlockPos pos) {
        return getBlock(pos).isReplaceable(mc.theWorld, pos);
    }

    public static boolean canBeClicked(BlockPos pos) {
        return getBlock(pos).canCollideCheck(mc.theWorld.getBlockState(pos), false);
    }
}
