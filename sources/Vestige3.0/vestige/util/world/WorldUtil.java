package vestige.util.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.*;
import vestige.util.IMinecraft;
import vestige.util.misc.LogUtil;
import vestige.util.player.MovementUtil;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class WorldUtil implements IMinecraft {

    public static BlockInfo getBlockUnder(double y, int maxRange) {
        return getBlockInfo(mc.thePlayer.posX, y - 1, mc.thePlayer.posZ, maxRange);
    }

    public static BlockInfo getBlockInfo(double x, double y, double z, int maxRange) {
        BlockPos pos = new BlockPos(x, y, z);

        // To add the facing of the opposite direction of the player as a priority
        EnumFacing playerDirectionFacing = getHorizontalFacing(MovementUtil.getPlayerDirection()).getOpposite();

        ArrayList<EnumFacing> facingValues = new ArrayList<>();
        facingValues.add(playerDirectionFacing);

        for(EnumFacing facing : EnumFacing.values()) {
            if(facing != playerDirectionFacing && facing != EnumFacing.UP) {
                facingValues.add(facing);
            }
        }

        CopyOnWriteArrayList<BlockPos> aaa = new CopyOnWriteArrayList<>();

        aaa.add(pos);

        int i = 0;

        while(i < maxRange) {
            ArrayList<BlockPos> ccc = new ArrayList<>(aaa);

            if(!aaa.isEmpty()) {
                for(BlockPos bbbb : aaa) {
                    for(EnumFacing facing : facingValues) {
                        BlockPos n = bbbb.offset(facing);

                        if(isAirOrLiquid(n)) {
                            aaa.add(n);
                        } else {
                            return new BlockInfo(n, facing.getOpposite());
                        }
                    }
                }
            }

            //LogUtil.addChatMessage("" + aaa.size());

            for(BlockPos dddd : ccc) {
                aaa.remove(dddd);
            }

            ccc.clear();

            i++;
        }

        return null;
    }

    public static Vec3 getVec3(BlockPos pos, EnumFacing facing, boolean randomised) {
        Vec3 vec3 = new Vec3(pos);

        double amount1 = 0.5;
        double amount2 = 0.5;

        if(randomised) {
            amount1 = 0.45 + Math.random() * 0.1;
            amount2 = 0.45 + Math.random() * 0.1;
        }

        if(facing == EnumFacing.UP) {
            vec3 = vec3.addVector(amount1, 1, amount2);
        } else if(facing == EnumFacing.DOWN) {
            vec3 = vec3.addVector(amount1, 0, amount2);
        } else if(facing == EnumFacing.EAST) {
            vec3 = vec3.addVector(1, amount1, amount2);
        } else if(facing == EnumFacing.WEST) {
            vec3 = vec3.addVector(0, amount1, amount2);
        } else if(facing == EnumFacing.NORTH) {
            vec3 = vec3.addVector(amount1, amount2, 0);
        } else if(facing == EnumFacing.SOUTH) {
            vec3 = vec3.addVector(amount1, amount2, 1);
        }

        return vec3;
    }

    public static EnumFacing getHorizontalFacing(float yaw) {
        return EnumFacing.getHorizontal(MathHelper.floor_double((double)(yaw * 4.0F / 360.0F) + 0.5D) & 3);
    }

    public static boolean isAir(BlockPos pos) {
        Block block = mc.theWorld.getBlockState(pos).getBlock();

        return block instanceof BlockAir;
    }

    public static boolean isAirOrLiquid(BlockPos pos) {
        Block block = mc.theWorld.getBlockState(pos).getBlock();

        return block instanceof BlockAir || block instanceof BlockLiquid;
    }

    public static MovingObjectPosition raytrace(float yaw, float pitch) {
        float partialTicks = mc.timer.renderPartialTicks;
        float blockReachDistance = mc.playerController.getBlockReachDistance();

        Vec3 vec3 = mc.thePlayer.getPositionEyes(partialTicks);

        Vec3 vec31 = mc.thePlayer.getVectorForRotation(pitch, yaw);

        Vec3 vec32 = vec3.addVector(vec31.xCoord * blockReachDistance, vec31.yCoord * blockReachDistance, vec31.zCoord * blockReachDistance);

        return mc.theWorld.rayTraceBlocks(vec3, vec32, false, false, true);
    }

    public static MovingObjectPosition raytraceLegit(float yaw, float pitch, float lastYaw, float lastPitch) {
        float partialTicks = mc.timer.renderPartialTicks;
        float blockReachDistance = mc.playerController.getBlockReachDistance();

        Vec3 vec3 = mc.thePlayer.getPositionEyes(partialTicks);

        float f = lastPitch + (pitch - lastPitch) * partialTicks;
        float f1 = lastYaw + (yaw - lastYaw) * partialTicks;
        Vec3 vec31 = mc.thePlayer.getVectorForRotation(f, f1);

        Vec3 vec32 = vec3.addVector(vec31.xCoord * blockReachDistance, vec31.yCoord * blockReachDistance, vec31.zCoord * blockReachDistance);

        return mc.theWorld.rayTraceBlocks(vec3, vec32, false, false, true);
    }

    public static boolean isBlockUnder() {
        for(int y = (int) mc.thePlayer.posY; y >= 0; y--) {
            if(!(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, y, mc.thePlayer.posZ)).getBlock() instanceof BlockAir)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBlockUnder(int distance) {
        for(int y = (int) mc.thePlayer.posY; y >= (int) mc.thePlayer.posY - distance; y--) {
            if(!(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, y, mc.thePlayer.posZ)).getBlock() instanceof BlockAir)) {
                return true;
            }
        }
        return false;
    }

    public static boolean negativeExpand(double negativeExpandValue) {
        return mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX + negativeExpandValue, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ + negativeExpandValue)).getBlock() instanceof BlockAir && mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX - negativeExpandValue, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ - negativeExpandValue)).getBlock() instanceof BlockAir && mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX - negativeExpandValue, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ)).getBlock() instanceof BlockAir && mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX + negativeExpandValue, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ)).getBlock() instanceof BlockAir && mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ + negativeExpandValue)).getBlock() instanceof BlockAir && mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ - negativeExpandValue)).getBlock() instanceof BlockAir;
    }

}