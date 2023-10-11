/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.utils;

import cow.milkgod.cheese.utils.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockHopper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;

public class EntityUtil {
    public static double[] getRotationToEntity(Entity entity) {
        double pX = Wrapper.getPlayer().posX;
        double pY = Wrapper.getPlayer().posY + (double)Wrapper.getPlayer().getEyeHeight();
        double pZ = Wrapper.getPlayer().posZ;
        double eX = entity.posX;
        double eY = entity.posY + (double)(entity.height / 2.0f);
        double eZ = entity.posZ;
        double dX = pX - eX;
        double dY = pY - eY;
        double dZ = pZ - eZ;
        double dH = Math.sqrt(Math.pow(dX, 2.0) + Math.pow(dZ, 2.0));
        double yaw = Math.toDegrees(Math.atan2(dZ, dX)) + 90.0;
        double pitch = Math.toDegrees(Math.atan2(dH, dY));
        return new double[]{yaw, 90.0 - pitch};
    }

    public static double getRotationDifference(double[] rotation) {
        return Math.sqrt(Math.pow(Math.abs(EntityUtil.angleDifference(Wrapper.mc.thePlayer.rotationYaw % 360.0f, rotation[0])), 2.0) + Math.pow(Math.abs(EntityUtil.angleDifference(Wrapper.mc.thePlayer.rotationPitch, rotation[1])), 2.0));
    }

    public static double getRotationDifference(double rotation1, double rotation2) {
        return ((rotation1 - rotation2) % 360.0 + 540.0) % 360.0 - 180.0;
    }

    public static boolean isAnimal(Entity entity) {
        return entity instanceof EntityAnimal;
    }

    public static boolean isMonster(Entity entity) {
        if (!(entity instanceof IMob || entity instanceof EntityDragon || entity instanceof EntityGolem)) {
            return false;
        }
        return true;
    }

    public static boolean isNeutral(Entity entity) {
        if (!(entity instanceof EntityBat || entity instanceof EntitySquid || entity instanceof EntityVillager)) {
            return false;
        }
        return true;
    }

    public static boolean isProjectile(Entity entity) {
        if (!(entity instanceof IProjectile) && !(entity instanceof EntityFishHook)) {
            return false;
        }
        return true;
    }

    public static String getEntityTypeName(Entity entity) {
        String type = null;
        if (EntityUtil.isAnimal(entity)) {
            type = "animals";
        } else if (EntityUtil.isMonster(entity)) {
            type = "monsters";
        } else if (EntityUtil.isNeutral(entity)) {
            type = "neutrals";
        } else if (EntityUtil.isProjectile(entity)) {
            type = "projectile";
        } else if (entity instanceof EntityPlayer) {
            type = "players";
        }
        return type;
    }

    public static String getTileEntityTypeName(TileEntity entity) {
        String type = null;
        Block block = entity.getBlockType();
        if (block == Blocks.chest || block == Blocks.trapped_chest) {
            type = "chests";
        } else if (block == Blocks.ender_chest) {
            type = "enderchests";
        } else if (block == Blocks.mob_spawner) {
            type = "mobspawners";
        } else if (block == Blocks.furnace || block == Blocks.lit_furnace || block == Blocks.dispenser || block == Blocks.dropper || block == Blocks.hopper) {
            type = "other";
        }
        return type;
    }

    public static double angleDifference(double a2, double b2) {
        return ((a2 - b2) % 360.0 + 540.0) % 360.0 - 180.0;
    }
}

