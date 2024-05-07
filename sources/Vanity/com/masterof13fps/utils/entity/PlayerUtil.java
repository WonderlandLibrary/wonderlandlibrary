package com.masterof13fps.utils.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class PlayerUtil {
    private static final Minecraft mc = Minecraft.mc();

    public static void toFwd(double speed) {
        float f = mc.thePlayer.rotationYaw * 0.017453292F;
        mc.thePlayer.motionX -= (double) MathHelper.sin(f) * speed;
        mc.thePlayer.motionZ += (double) MathHelper.cos(f) * speed;
    }

    public static double getSpeed() {
        return Math.sqrt(Minecraft.mc().thePlayer.motionX * Minecraft.mc().thePlayer.motionX + Minecraft.mc().thePlayer.motionZ * Minecraft.mc().thePlayer.motionZ);
    }

    public static void setSpeed(double speed) {
        mc.thePlayer.motionX = -(Math.sin(getDirection()) * speed);
        mc.thePlayer.motionZ = Math.cos(getDirection()) * speed;
    }

    public static float getDirection() {
        float var1 = mc.thePlayer.rotationYaw;
        if (mc.thePlayer.moveForward < 0.0F) {
            var1 += 180.0F;
        }

        float forward = 1.0F;
        if (mc.thePlayer.moveForward < 0.0F) {
            forward = -0.5F;
        } else if (mc.thePlayer.moveForward > 0.0F) {
            forward = 0.5F;
        }

        if (mc.thePlayer.moveStrafing > 0.0F) {
            var1 -= 90.0F * forward;
        }

        if (mc.thePlayer.moveStrafing < 0.0F) {
            var1 += 90.0F * forward;
        }

        var1 *= 0.017453292F;
        return var1;
    }

    public static void damagePlayer(String mode) {
        if (mode.equalsIgnoreCase("old")) {
            for (int i = 0; i < 60; i++) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
                        Minecraft.mc().thePlayer.posX, Minecraft.mc().thePlayer.posY + 0.2D,
                        Minecraft.mc().thePlayer.posZ, false));
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
                        Minecraft.mc().thePlayer.posX, Minecraft.mc().thePlayer.posY + 0.26D,
                        Minecraft.mc().thePlayer.posZ, false));
            }
        } else if (mode.equalsIgnoreCase("new")) {
            for (int i = 0; i < 32; i++) {
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                        mc.thePlayer.posY - 0.05D, mc.thePlayer.posZ, false));
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                        mc.thePlayer.posY + 0.05D, mc.thePlayer.posZ, false));
            }
            mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer(true));
        } else if (mode.equalsIgnoreCase("newnodmg")) {
            for (int i = 0; i < 16; i++) {
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                        mc.thePlayer.posY - 0.05D, mc.thePlayer.posZ, false));
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                        mc.thePlayer.posY + 0.05D, mc.thePlayer.posZ, false));
            }
            mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer(true));
        } else if (mode.equalsIgnoreCase("other")) {
            for (int i = 0; i < 64; i++) {
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                        mc.thePlayer.posY + 0.05D, mc.thePlayer.posZ, false));
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                        mc.thePlayer.posY, mc.thePlayer.posZ, false));
            }
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + 2.147483647E9D,
                    mc.thePlayer.posY + 2.147483647E9D, mc.thePlayer.posZ + 2.147483647E9D,
                    mc.thePlayer.onGround));
        } else if (mode.equalsIgnoreCase("suicide")) {
            for (int x = 0; x < 21; x++) {
                for (int i = 0; i < 60; i++) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
                            Minecraft.mc().thePlayer.posX, Minecraft.mc().thePlayer.posY + 0.2D,
                            Minecraft.mc().thePlayer.posZ, false));
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
                            Minecraft.mc().thePlayer.posX, Minecraft.mc().thePlayer.posY + 0.26D,
                            Minecraft.mc().thePlayer.posZ, false));
                }
            }
        } else {
        }
    }

    public static boolean isOnLiquid() {
        AxisAlignedBB boundingBox = mc.thePlayer.getEntityBoundingBox().contract(0.01D, 0.0D, 0.01D).offset(0.0D, -0.01D, 0.0D);
        boolean onLiquid = false;
        int y = (int) boundingBox.minY;
        for (int x = MathHelper.floor_double(boundingBox.minX); x <
                MathHelper.floor_double(boundingBox.maxX + 1.0D); x++) {
            for (int z = MathHelper.floor_double(boundingBox.minZ); z <
                    MathHelper.floor_double(boundingBox.maxZ + 1.0D); z++) {
                Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block != Blocks.air) {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                    onLiquid = true;
                }
            }
        }
        return onLiquid;
    }

    public static boolean isInLiquid() {
        AxisAlignedBB par1AxisAlignedBB = mc.thePlayer.getEntityBoundingBox().contract(0.001D, 0.001D, 0.001D);
        int var4 = MathHelper.floor_double(par1AxisAlignedBB.minX);
        int var5 = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0D);
        int var6 = MathHelper.floor_double(par1AxisAlignedBB.minY);
        int var7 = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0D);
        int var8 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
        int var9 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0D);
        if (mc.theWorld.getChunkFromBlockCoords(
                new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ)) == null) {
            return false;
        }
        Vec3 var11 = new Vec3(0.0D, 0.0D, 0.0D);
        for (int var12 = var4; var12 < var5; var12++) {
            for (int var13 = var6; var13 < var7; var13++) {
                for (int var14 = var8; var14 < var9; var14++) {
                    Block var15 = mc.theWorld.getBlockState(new BlockPos(var12, var13, var14)).getBlock();
                    if ((var15 instanceof BlockLiquid)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean getColliding(int i) {
        int mx = i;
        int mz = i;
        int max = i;
        int maz = i;
        if (mc.thePlayer.motionX > 0.01D) {
            mx = 0;
        } else if (mc.thePlayer.motionX < -0.01D) {
            max = 0;
        }

        if (mc.thePlayer.motionZ > 0.01D) {
            mz = 0;
        } else if (mc.thePlayer.motionZ < -0.01D) {
            maz = 0;
        }

        int xmin = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX - (double) mx);
        int ymin = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minY - 1.0D);
        int zmin = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minZ - (double) mz);
        int xmax = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX + (double) max);
        int ymax = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minY + 1.0D);
        int zmax = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minZ + (double) maz);

        for (int x = xmin; x <= xmax; ++x) {
            for (int y = ymin; y <= ymax; ++y) {
                for (int z = zmin; z <= zmax; ++z) {
                    Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block instanceof BlockLiquid && !mc.thePlayer.isInsideOfMaterial(Material.lava) && !mc.thePlayer.isInsideOfMaterial(Material.water)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
