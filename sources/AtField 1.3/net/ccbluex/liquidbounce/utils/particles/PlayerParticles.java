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

    public static boolean isInWater() {
        return PlayerParticles.mc.field_71441_e.func_180495_p(new BlockPos(PlayerParticles.mc.field_71439_g.field_70165_t, PlayerParticles.mc.field_71439_g.field_70163_u, PlayerParticles.mc.field_71439_g.field_70161_v)).func_185904_a() == Material.field_151586_h;
    }

    public static ArrayList vanillaTeleportPositions(double d, double d2, double d3, double d4) {
        double d5;
        ArrayList<Vector3f> arrayList = new ArrayList<Vector3f>();
        double d6 = d - PlayerParticles.mc.field_71439_g.field_70165_t;
        double d7 = d2 - (PlayerParticles.mc.field_71439_g.field_70163_u + (double)PlayerParticles.mc.field_71439_g.func_70047_e() + 1.1);
        double d8 = d3 - PlayerParticles.mc.field_71439_g.field_70161_v;
        float f = (float)(Math.atan2(d8, d6) * 180.0 / Math.PI - 90.0);
        float f2 = (float)(-Math.atan2(d7, Math.sqrt(d6 * d6 + d8 * d8)) * 180.0 / Math.PI);
        double d9 = PlayerParticles.mc.field_71439_g.field_70165_t;
        double d10 = PlayerParticles.mc.field_71439_g.field_70163_u;
        double d11 = PlayerParticles.mc.field_71439_g.field_70161_v;
        double d12 = 1.0;
        for (d5 = d4; d5 < PlayerParticles.getDistance(PlayerParticles.mc.field_71439_g.field_70165_t, PlayerParticles.mc.field_71439_g.field_70163_u, PlayerParticles.mc.field_71439_g.field_70161_v, d, d2, d3); d5 += d4) {
            d12 += 1.0;
        }
        for (d5 = d4; d5 < PlayerParticles.getDistance(PlayerParticles.mc.field_71439_g.field_70165_t, PlayerParticles.mc.field_71439_g.field_70163_u, PlayerParticles.mc.field_71439_g.field_70161_v, d, d2, d3); d5 += d4) {
            d9 = PlayerParticles.mc.field_71439_g.field_70165_t - Math.sin(PlayerParticles.getDirection(f)) * d5;
            d11 = PlayerParticles.mc.field_71439_g.field_70161_v + Math.cos(PlayerParticles.getDirection(f)) * d5;
            arrayList.add(new Vector3f((float)d9, (float)(d10 -= (PlayerParticles.mc.field_71439_g.field_70163_u - d2) / d12), (float)d11));
        }
        arrayList.add(new Vector3f((float)d, (float)d2, (float)d3));
        return arrayList;
    }

    public static Block getBlock(BlockPos blockPos) {
        return PlayerParticles.mc.field_71441_e.func_180495_p(blockPos).func_177230_c();
    }

    public static Block getBlock(double d, double d2, double d3) {
        return PlayerParticles.mc.field_71441_e.func_180495_p(new BlockPos(d, d2, d3)).func_177230_c();
    }

    public static double getIncremental(double d, double d2) {
        double d3 = 1.0 / d2;
        return (double)Math.round(d * d3) / d3;
    }

    public static boolean hotbarIsFull() {
        for (int i = 0; i <= 36; ++i) {
            ItemStack itemStack = PlayerParticles.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (itemStack != null) continue;
            return false;
        }
        return true;
    }

    public static float[] getRotations(Entity entity) {
        double d = entity.field_70165_t;
        double d2 = entity.field_70161_v;
        double d3 = entity.field_70163_u + (double)(entity.func_70047_e() / 4.0f);
        return PlayerParticles.getRotationFromPosition(d, d2, d3);
    }

    private static float[] getRotationFromPosition(double d, double d2, double d3) {
        double d4 = d - PlayerParticles.mc.field_71439_g.field_70165_t;
        double d5 = d2 - PlayerParticles.mc.field_71439_g.field_70161_v;
        double d6 = d3 - PlayerParticles.mc.field_71439_g.field_70163_u - 0.6;
        double d7 = MathHelper.func_76133_a((double)(d4 * d4 + d5 * d5));
        float f = (float)(Math.atan2(d5, d4) * 180.0 / Math.PI) - 90.0f;
        float f2 = (float)(-(Math.atan2(d6, d7) * 180.0 / Math.PI));
        return new float[]{f, f2};
    }

    public EntityLivingBase getEntity() {
        return null;
    }

    public static Block getBlockAtPosC(EntityPlayer entityPlayer, double d, double d2, double d3) {
        return PlayerParticles.getBlock(new BlockPos(entityPlayer.field_70165_t - d, entityPlayer.field_70163_u - d2, entityPlayer.field_70161_v - d3));
    }

    public static float getDirection() {
        float f = PlayerParticles.mc.field_71439_g.field_70759_as;
        float f2 = PlayerParticles.mc.field_71439_g.field_191988_bg;
        float f3 = PlayerParticles.mc.field_71439_g.field_70702_br;
        f += (float)(f2 < 0.0f ? 180 : 0);
        if (f3 < 0.0f) {
            f += (float)(f2 < 0.0f ? -45 : (f2 == 0.0f ? 90 : 45));
        }
        if (f3 > 0.0f) {
            f -= (float)(f2 < 0.0f ? -45 : (f2 == 0.0f ? 90 : 45));
        }
        return f * ((float)Math.PI / 180);
    }

    public static double getDistance(double d, double d2, double d3, double d4, double d5, double d6) {
        double d7 = d - d4;
        double d8 = d2 - d5;
        double d9 = d3 - d6;
        return MathHelper.func_76133_a((double)(d7 * d7 + d8 * d8 + d9 * d9));
    }

    public static Vec3 getLook(float f, float f2) {
        float f3 = MathHelper.func_76134_b((float)(-f2 * ((float)Math.PI / 180) - (float)Math.PI));
        float f4 = MathHelper.func_76126_a((float)(-f2 * ((float)Math.PI / 180) - (float)Math.PI));
        float f5 = -MathHelper.func_76134_b((float)(-f * ((float)Math.PI / 180)));
        float f6 = MathHelper.func_76126_a((float)(-f * ((float)Math.PI / 180)));
        return new Vec3(f4 * f5, f6, f3 * f5);
    }

    public static boolean isMoving() {
        if (!PlayerParticles.mc.field_71439_g.field_70123_F && !PlayerParticles.mc.field_71439_g.func_70093_af()) {
            return PlayerParticles.mc.field_71439_g.field_71158_b.field_192832_b != 0.0f || PlayerParticles.mc.field_71439_g.field_71158_b.field_78902_a != 0.0f;
        }
        return false;
    }

    public static float getDirection(float f) {
        if (PlayerParticles.mc.field_71439_g.field_191988_bg < 0.0f) {
            f += 180.0f;
        }
        float f2 = 1.0f;
        if (PlayerParticles.mc.field_71439_g.field_191988_bg < 0.0f) {
            f2 = -0.5f;
        } else if (PlayerParticles.mc.field_71439_g.field_191988_bg > 0.0f) {
            f2 = 0.5f;
        }
        if (PlayerParticles.mc.field_71439_g.field_70702_br > 0.0f) {
            f -= 90.0f * f2;
        }
        if (PlayerParticles.mc.field_71439_g.field_70702_br < 0.0f) {
            f += 90.0f * f2;
        }
        return f *= (float)Math.PI / 180;
    }
}

