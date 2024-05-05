package client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
public class Rotation {

        public float yaw, pitch;
        private BlockPos currentPos;
        private EnumFacing currentFacing;
        private final static Minecraft mc = Minecraft.getMinecraft();


        public Rotation(float yaw, float pitch) {
            this.yaw = yaw;
            this.pitch = pitch;
        }

        public float getYaw() {
            return yaw;
        }

        public void setYaw(float yaw) {
            this.yaw = yaw;
        }

        public float getPitch() {
            return pitch;
        }

        public void setPitch(float pitch) {
            this.pitch = pitch;
        }

        public void setBlockAndFacing(BlockPos var1) {

            //if(!shouldDownwards()) {
            if (this.mc.theWorld.getBlockState(var1.add(0, -1, 0)).getBlock() != Blocks.air) {
                currentPos = var1.add(0, -1, 0);
                currentFacing = EnumFacing.UP;
            } else if (mc.theWorld.getBlockState(var1.add(-1, 0, 0)).getBlock() != Blocks.air) {
                currentPos = var1.add(-1, 0, 0);
                currentFacing = EnumFacing.EAST;
            } else if (this.mc.theWorld.getBlockState(var1.add(1, 0, 0)).getBlock() != Blocks.air) {
                currentPos = var1.add(1, 0, 0);
                currentFacing = EnumFacing.WEST;
            } else if (this.mc.theWorld.getBlockState(var1.add(0, 0, -1)).getBlock() != Blocks.air) {

                currentPos = var1.add(0, 0, -1);
                currentFacing = EnumFacing.SOUTH;

            } else if (this.mc.theWorld.getBlockState(var1.add(0, 0, 1)).getBlock() != Blocks.air) {

                currentPos = var1.add(0, 0, 1);
                currentFacing = EnumFacing.NORTH;

            } else if (this.mc.theWorld.getBlockState(var1.add(-1, 0, -1)).getBlock() != Blocks.air) {
                currentPos = var1.add(-1, 0, -1);
                currentFacing = EnumFacing.EAST;
            } else if (this.mc.theWorld.getBlockState(var1.add(-1, 0, 1)).getBlock() != Blocks.air) {
                currentPos = var1.add(-1, 0, 1);
                currentFacing = EnumFacing.EAST;
            } else if (this.mc.theWorld.getBlockState(var1.add(1, 0, -1)).getBlock() != Blocks.air) {
                currentPos = var1.add(1, 0, -1);
                currentFacing = EnumFacing.WEST;
            } else if (this.mc.theWorld.getBlockState(var1.add(1, 0, 1)).getBlock() != Blocks.air) {
                currentPos = var1.add(1, 0, 1);
                currentFacing = EnumFacing.WEST;
            } else if (this.mc.theWorld.getBlockState(var1.add(-1, -1, 0)).getBlock() != Blocks.air) {
                currentPos = var1.add(-1, -1, 0);
                currentFacing = EnumFacing.EAST;
            } else if (this.mc.theWorld.getBlockState(var1.add(1, -1, 0)).getBlock() != Blocks.air) {
                currentPos = var1.add(1, -1, 0);
                currentFacing = EnumFacing.WEST;
            } else if (this.mc.theWorld.getBlockState(var1.add(0, -1, -1)).getBlock() != Blocks.air) {
                currentPos = var1.add(0, -1, -1);
                currentFacing = EnumFacing.SOUTH;
            } else if (this.mc.theWorld.getBlockState(var1.add(0, -1, 1)).getBlock() != Blocks.air) {
                currentPos = var1.add(0, -1, 1);
                currentFacing = EnumFacing.NORTH;
            } else if (this.mc.theWorld.getBlockState(var1.add(-1, -1, -1)).getBlock() != Blocks.air) {
                currentPos = var1.add(-1, -1, -1);
                currentFacing = EnumFacing.EAST;
            } else if (this.mc.theWorld.getBlockState(var1.add(-1, -1, 1)).getBlock() != Blocks.air) {
                currentPos = var1.add(-1, -1, 1);
                currentFacing = EnumFacing.EAST;
            } else if (this.mc.theWorld.getBlockState(var1.add(1, -1, -1)).getBlock() != Blocks.air) {
                currentPos = var1.add(1, -1, -1);
                currentFacing = EnumFacing.WEST;
            } else if (this.mc.theWorld.getBlockState(var1.add(1, -1, 1)).getBlock() != Blocks.air) {
                currentPos = var1.add(1, -1, 1);
                currentFacing = EnumFacing.WEST;
            } else if (this.mc.theWorld.getBlockState(var1.add(-2, 0, 0)).getBlock() != Blocks.air) {
                currentPos = var1.add(-2, 0, 0);
                currentFacing = EnumFacing.EAST;
            } else if (this.mc.theWorld.getBlockState(var1.add(2, 0, 0)).getBlock() != Blocks.air) {
                currentPos = var1.add(2, 0, 0);
                currentFacing = EnumFacing.WEST;
            } else if (this.mc.theWorld.getBlockState(var1.add(0, 0, -2)).getBlock() != Blocks.air) {
                currentPos = var1.add(0, 0, -2);
                currentFacing = EnumFacing.SOUTH;
            } else if (this.mc.theWorld.getBlockState(var1.add(0, 0, 2)).getBlock() != Blocks.air) {
                currentPos = var1.add(0, 0, 2);
                currentFacing = EnumFacing.NORTH;
            } else if (this.mc.theWorld.getBlockState(var1.add(-2, 0, -2)).getBlock() != Blocks.air) {
                currentPos = var1.add(-2, 0, -2);
                currentFacing = EnumFacing.EAST;
            } else if (this.mc.theWorld.getBlockState(var1.add(-2, 0, 2)).getBlock() != Blocks.air) {
                currentPos = var1.add(-2, 0, 2);
                currentFacing = EnumFacing.EAST;
            } else if (this.mc.theWorld.getBlockState(var1.add(2, 0, -2)).getBlock() != Blocks.air) {
                currentPos = var1.add(2, 0, -2);
                currentFacing = EnumFacing.WEST;
            } else if (this.mc.theWorld.getBlockState(var1.add(2, 0, 2)).getBlock() != Blocks.air) {
                currentPos = var1.add(2, 0, 2);
                currentFacing = EnumFacing.WEST;
            } else if (this.mc.theWorld.getBlockState(var1.add(0, 1, 0)).getBlock() != Blocks.air) {
                currentPos = var1.add(0, 1, 0);
                currentFacing = EnumFacing.DOWN;
            } else if (this.mc.theWorld.getBlockState(var1.add(-1, 1, 0)).getBlock() != Blocks.air) {
                currentPos = var1.add(-1, 1, 0);
                currentFacing = EnumFacing.EAST;
            } else if (this.mc.theWorld.getBlockState(var1.add(1, 1, 0)).getBlock() != Blocks.air) {
                currentPos = var1.add(1, 1, 0);
                currentFacing = EnumFacing.WEST;
            } else if (this.mc.theWorld.getBlockState(var1.add(0, 1, -1)).getBlock() != Blocks.air) {
                currentPos = var1.add(0, 1, -1);
                currentFacing = EnumFacing.SOUTH;
            } else if (this.mc.theWorld.getBlockState(var1.add(0, 1, 1)).getBlock() != Blocks.air) {
                currentPos = var1.add(0, 1, 1);
                currentFacing = EnumFacing.NORTH;
            } else if (this.mc.theWorld.getBlockState(var1.add(-1, 1, -1)).getBlock() != Blocks.air) {
                currentPos = var1.add(-1, 1, -1);
                currentFacing = EnumFacing.EAST;
            } else if (this.mc.theWorld.getBlockState(var1.add(-1, 1, 1)).getBlock() != Blocks.air) {
                currentPos = var1.add(-1, 1, 1);
                currentFacing = EnumFacing.EAST;
            } else if (this.mc.theWorld.getBlockState(var1.add(1, 1, -1)).getBlock() != Blocks.air) {
                currentPos = var1.add(1, 1, -1);
                currentFacing = EnumFacing.WEST;
            } else if (this.mc.theWorld.getBlockState(var1.add(1, 1, 1)).getBlock() != Blocks.air) {
                currentPos = var1.add(1, 1, 1);
                currentFacing = EnumFacing.WEST;
            }
        }
}
