package club.dortware.client.util.impl.combat;

import club.dortware.client.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class FightUtil implements Util {

    public static EntityLivingBase getSingleTarget(double range, boolean players, boolean animals, boolean walls, boolean mobs, boolean invisibles) {
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (!(entity instanceof EntityLivingBase))
                continue;
            EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
            if (mc.thePlayer.getDistanceToEntity(entityLivingBase) > range
                    || (!entityLivingBase.canEntityBeSeen(mc.thePlayer) && !walls)
                    || entityLivingBase.isDead
                    || entityLivingBase.getHealth() == 0)
                continue;
            if (entityLivingBase instanceof EntityPlayer && !players) continue;
            if (entityLivingBase instanceof EntityAnimal && !animals) continue;
            if (entityLivingBase instanceof EntityMob && !mobs) continue;
            if (entityLivingBase.isInvisible() && !invisibles) continue;
            if (entityLivingBase == mc.thePlayer) continue;
            if (!entityLivingBase.isValid()) continue;
            return entityLivingBase;
        }
        return null;
    }

    public static boolean isNearEntity(double range, boolean players, boolean animals, boolean walls, boolean mobs, boolean invisibles) {
        return getSingleTarget(range, players, animals, walls, mobs, invisibles) != null;
    }

    public static List<EntityLivingBase> getMultipleTargets(int max, double range, boolean players, boolean animals, boolean walls, boolean mobs) {
        List<EntityLivingBase> list = new ArrayList<>();
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (!(entity instanceof EntityLivingBase))
                continue;
            EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
            if (mc.thePlayer.getDistanceToEntity(entityLivingBase) > range
                    || (entityLivingBase.canEntityBeSeen(mc.thePlayer) && !walls)
                    || entityLivingBase.isDead
                    || entityLivingBase.getHealth() == 0
                    || list.size() >= max)
                continue;
            if (entityLivingBase instanceof EntityPlayer && !players) continue;
            if (entityLivingBase instanceof EntityAnimal && !animals) continue;
            if (entityLivingBase instanceof EntityMob && !mobs) continue;
            if (entityLivingBase == mc.thePlayer) continue;
            if (!entityLivingBase.isValid()) continue;
            list.add(entityLivingBase);
        }
        return list;
    }

}
