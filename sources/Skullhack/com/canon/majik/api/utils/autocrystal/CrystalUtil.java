package com.canon.majik.api.utils.autocrystal;

import com.canon.majik.api.utils.Globals;
import net.minecraft.entity.player.EntityPlayer;

public class CrystalUtil implements Globals {

    public static float getHealth(EntityPlayer player) {
        return player.getHealth() + player.getAbsorptionAmount();
    }

    public static EntityPlayer getEntityPlayer(float range) {
        EntityPlayer lowest = null;
        for (EntityPlayer entityPlayer : mc.world.playerEntities) {
            if (entityPlayer.equals(mc.player)) {
                continue;
            }
            if (entityPlayer.isDead || entityPlayer.getHealth() <= 0.0f) {
                continue;
            }
            if (mc.player.getDistance(entityPlayer) > range) {
                continue;
            }
            if (lowest == null || mc.player.getDistance(entityPlayer) < mc.player.getDistance(lowest)) {
                lowest = entityPlayer;
            }
        }
        return lowest;
    }
}
