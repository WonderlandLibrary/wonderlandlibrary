package me.utils.player;

import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;

public class PlayerUtil {
    private static Minecraft mc = Minecraft.getMinecraft();

    public static boolean isBlockUnder() {
        if (MinecraftInstance.mc2.player.field_70163_u < 0.0) {
            return false;
        }
        for (int off = 0; off < (int)PlayerUtil.mc.player.field_70163_u + 2; off += 2) {
            AxisAlignedBB bb = PlayerUtil.mc.player.func_174813_aQ().offset(0.0, (double)(-off), 0.0);
            if (PlayerUtil.mc.world.func_184144_a((Entity)PlayerUtil.mc.player, bb).isEmpty()) continue;
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
        if (!PlayerUtil.mc.player.field_70123_F && !PlayerUtil.mc.player.isSneaking()) {
            return PlayerUtil.mc.player.movementInput.moveForward != 0.0f || PlayerUtil.mc.player.movementInput.moveStrafe != 0.0f;
        }
        return false;
    }

    public EntityLivingBase getEntity() {
        return null;
    }
}
