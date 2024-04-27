/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.math.AxisAlignedBB
 */
package liying.utils;

import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;

public class PlayerUtil {
    private static Minecraft mc = Minecraft.func_71410_x();

    public static boolean isMoving() {
        if (!PlayerUtil.mc.field_71439_g.field_70123_F && !PlayerUtil.mc.field_71439_g.func_70093_af()) {
            return PlayerUtil.mc.field_71439_g.field_71158_b.field_192832_b != 0.0f || PlayerUtil.mc.field_71439_g.field_71158_b.field_78902_a != 0.0f;
        }
        return false;
    }

    public static double getDistance(double d, double d2, double d3, double d4, double d5, double d6) {
        double d7 = d - d4;
        double d8 = d2 - d5;
        double d9 = d3 - d6;
        return Math.sqrt(d7 * d7 + d8 * d8 + d9 * d9);
    }

    public EntityLivingBase getEntity() {
        return null;
    }

    public static boolean isBlockUnder() {
        if (MinecraftInstance.mc2.field_71439_g.field_70163_u < 0.0) {
            return false;
        }
        for (int i = 0; i < (int)PlayerUtil.mc.field_71439_g.field_70163_u + 2; i += 2) {
            AxisAlignedBB axisAlignedBB = PlayerUtil.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, (double)(-i), 0.0);
            if (PlayerUtil.mc.field_71441_e.func_184144_a((Entity)PlayerUtil.mc.field_71439_g, axisAlignedBB).isEmpty()) continue;
            return true;
        }
        return false;
    }
}

