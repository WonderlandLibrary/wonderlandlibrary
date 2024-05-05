/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.math.AxisAlignedBB
 */
package jx.utils;

import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;

public class PlayerUtil {
    private static Minecraft mc = Minecraft.func_71410_x();

    public static boolean isBlockUnder() {
        if (MinecraftInstance.mc2.field_71439_g.field_70163_u < 0.0) {
            return false;
        }
        for (int off = 0; off < (int)PlayerUtil.mc.field_71439_g.field_70163_u + 2; off += 2) {
            AxisAlignedBB bb = PlayerUtil.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, (double)(-off), 0.0);
            if (PlayerUtil.mc.field_71441_e.func_184144_a((Entity)PlayerUtil.mc.field_71439_g, bb).isEmpty()) continue;
            return true;
        }
        return false;
    }

    public static double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double d0 = x1 - x2;
        double d2 = y1 - y2;
        double d3 = z1 - z2;
        return Math.sqrt(d0 * d0 + d2 * d2 + d3 * d3);
    }

    public static boolean isMoving() {
        if (!PlayerUtil.mc.field_71439_g.field_70123_F && !PlayerUtil.mc.field_71439_g.func_70093_af()) {
            return PlayerUtil.mc.field_71439_g.field_71158_b.field_192832_b != 0.0f || PlayerUtil.mc.field_71439_g.field_71158_b.field_78902_a != 0.0f;
        }
        return false;
    }

    public EntityLivingBase getEntity() {
        return null;
    }
}

