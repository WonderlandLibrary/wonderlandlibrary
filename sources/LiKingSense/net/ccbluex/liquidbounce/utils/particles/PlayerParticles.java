/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.vecmath.Vector3f
 *  net.minecraft.block.Block
 *  net.minecraft.block.material.Material
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 */
package net.ccbluex.liquidbounce.utils.particles;

import java.util.ArrayList;
import javax.vecmath.Vector3f;
import net.ccbluex.liquidbounce.utils.particles.Vec3;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class PlayerParticles {
    private static Minecraft mc = Minecraft.func_71410_x();

    public static float[] getRotations(Entity ent) {
        double x = ent.field_70165_t;
        double z = ent.field_70161_v;
        double y = ent.field_70163_u + (double)(ent.func_70047_e() / 4.0f);
        return PlayerParticles.getRotationFromPosition(x, z, y);
    }

    public static Block getBlock(double offsetX, double offsetY, double offsetZ) {
        return PlayerParticles.mc.field_71441_e.func_180495_p(new BlockPos(offsetX, offsetY, offsetZ)).func_177230_c();
    }

    private static float[] getRotationFromPosition(double x, double z, double y) {
        double xDiff = x - PlayerParticles.mc.field_71439_g.field_70165_t;
        double zDiff = z - PlayerParticles.mc.field_71439_g.field_70161_v;
        double yDiff = y - PlayerParticles.mc.field_71439_g.field_70163_u - 0.6;
        double dist = MathHelper.func_76133_a((double)(xDiff * xDiff + zDiff * zDiff));
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0 / Math.PI));
        return new float[]{yaw, pitch};
    }

    public static float getDirection() {
        float yaw = PlayerParticles.mc.field_71439_g.field_70759_as;
        float forward = PlayerParticles.mc.field_71439_g.field_191988_bg;
        float strafe = PlayerParticles.mc.field_71439_g.field_70702_br;
        yaw += (float)(forward < 0.0f ? 180 : 0);
        if (strafe < 0.0f) {
            yaw += (float)(forward < 0.0f ? -45 : (forward == 0.0f ? 90 : 45));
        }
        if (strafe > 0.0f) {
            yaw -= (float)(forward < 0.0f ? -45 : (forward == 0.0f ? 90 : 45));
        }
        return yaw * ((float)Math.PI / 180);
    }

    public static boolean isInWater() {
        return PlayerParticles.mc.field_71441_e.func_180495_p(new BlockPos(PlayerParticles.mc.field_71439_g.field_70165_t, PlayerParticles.mc.field_71439_g.field_70163_u, PlayerParticles.mc.field_71439_g.field_70161_v)).func_185904_a() == Material.field_151586_h;
    }

    public static Block getBlock(BlockPos pos) {
        return PlayerParticles.mc.field_71441_e.func_180495_p(pos).func_177230_c();
    }

    public static Block getBlockAtPosC(EntityPlayer inPlayer, double x, double y, double z) {
        return PlayerParticles.getBlock(new BlockPos(inPlayer.field_70165_t - x, inPlayer.field_70163_u - y, inPlayer.field_70161_v - z));
    }

    public static ArrayList<Vector3f> vanillaTeleportPositions(double tpX, double tpY, double tpZ, double speed) {
        double d;
        ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
        double posX = tpX - PlayerParticles.mc.field_71439_g.field_70165_t;
        double posY = tpY - (PlayerParticles.mc.field_71439_g.field_70163_u + (double)PlayerParticles.mc.field_71439_g.func_70047_e() + 1.1);
        double posZ = tpZ - PlayerParticles.mc.field_71439_g.field_70161_v;
        float yaw = (float)(Math.atan2(posZ, posX) * 180.0 / Math.PI - 90.0);
        float pitch = (float)(-Math.atan2(posY, Math.sqrt(posX * posX + posZ * posZ)) * 180.0 / Math.PI);
        double tmpX = PlayerParticles.mc.field_71439_g.field_70165_t;
        double tmpY = PlayerParticles.mc.field_71439_g.field_70163_u;
        double tmpZ = PlayerParticles.mc.field_71439_g.field_70161_v;
        double steps = 1.0;
        for (d = speed; d < PlayerParticles.getDistance(PlayerParticles.mc.field_71439_g.field_70165_t, PlayerParticles.mc.field_71439_g.field_70163_u, PlayerParticles.mc.field_71439_g.field_70161_v, tpX, tpY, tpZ); d += speed) {
            steps += 1.0;
        }
        for (d = speed; d < PlayerParticles.getDistance(PlayerParticles.mc.field_71439_g.field_70165_t, PlayerParticles.mc.field_71439_g.field_70163_u, PlayerParticles.mc.field_71439_g.field_70161_v, tpX, tpY, tpZ); d += speed) {
            tmpX = PlayerParticles.mc.field_71439_g.field_70165_t - Math.sin(PlayerParticles.getDirection(yaw)) * d;
            tmpZ = PlayerParticles.mc.field_71439_g.field_70161_v + Math.cos(PlayerParticles.getDirection(yaw)) * d;
            positions.add(new Vector3f((float)tmpX, (float)(tmpY -= (PlayerParticles.mc.field_71439_g.field_70163_u - tpY) / steps), (float)tmpZ));
        }
        positions.add(new Vector3f((float)tpX, (float)tpY, (float)tpZ));
        return positions;
    }

    public static float getDirection(float yaw) {
        if (PlayerParticles.mc.field_71439_g.field_191988_bg < 0.0f) {
            yaw += 180.0f;
        }
        float forward = 1.0f;
        if (PlayerParticles.mc.field_71439_g.field_191988_bg < 0.0f) {
            forward = -0.5f;
        } else if (PlayerParticles.mc.field_71439_g.field_191988_bg > 0.0f) {
            forward = 0.5f;
        }
        if (PlayerParticles.mc.field_71439_g.field_70702_br > 0.0f) {
            yaw -= 90.0f * forward;
        }
        if (PlayerParticles.mc.field_71439_g.field_70702_br < 0.0f) {
            yaw += 90.0f * forward;
        }
        return yaw *= (float)Math.PI / 180;
    }

    public static double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double d0 = x1 - x2;
        double d2 = y1 - y2;
        double d3 = z1 - z2;
        return MathHelper.func_76133_a((double)(d0 * d0 + d2 * d2 + d3 * d3));
    }

    public static boolean hotbarIsFull() {
        for (int i = 0; i <= 36; ++i) {
            ItemStack itemstack = PlayerParticles.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (itemstack != null) continue;
            return false;
        }
        return true;
    }

    public static Vec3 getLook(float p_174806_1_, float p_174806_2_) {
        float var3 = MathHelper.func_76134_b((float)(-p_174806_2_ * ((float)Math.PI / 180) - (float)Math.PI));
        float var4 = MathHelper.func_76126_a((float)(-p_174806_2_ * ((float)Math.PI / 180) - (float)Math.PI));
        float var5 = -MathHelper.func_76134_b((float)(-p_174806_1_ * ((float)Math.PI / 180)));
        float var6 = MathHelper.func_76126_a((float)(-p_174806_1_ * ((float)Math.PI / 180)));
        return new Vec3(var4 * var5, var6, var3 * var5);
    }

    public static boolean isMoving() {
        if (!PlayerParticles.mc.field_71439_g.field_70123_F && !PlayerParticles.mc.field_71439_g.func_70093_af()) {
            return PlayerParticles.mc.field_71439_g.field_71158_b.field_192832_b != 0.0f || PlayerParticles.mc.field_71439_g.field_71158_b.field_78902_a != 0.0f;
        }
        return false;
    }

    public EntityLivingBase getEntity() {
        return null;
    }

    public static double getIncremental(double val, double inc) {
        double one = 1.0 / inc;
        return (double)Math.round(val * one) / one;
    }
}

