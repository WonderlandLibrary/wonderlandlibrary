package tech.atani.client.utility.player.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.utility.player.rotation.RotationUtil;
import tech.atani.client.feature.combat.CombatManager;

import java.util.ArrayList;
import java.util.List;

public class FightUtil implements Methods {

    public static boolean canHit(double chance) {
        return Math.random() <= chance;
    }

    public static List<EntityLivingBase> getMultipleTargets(double range, boolean players, boolean animals, boolean walls, boolean mobs, boolean invis) {
        List<EntityLivingBase> list = new ArrayList<>();
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (list.size() > 5)
                break;

            if (!(entity instanceof EntityLivingBase))
                continue;

            EntityLivingBase entityLivingBase = (EntityLivingBase) entity;

            if (entityLivingBase == mc.thePlayer ||
                    getRange(entityLivingBase) > range
                    || !entityLivingBase.canEntityBeSeen(mc.thePlayer) && !walls
                    || entityLivingBase.isDead
                    || entityLivingBase instanceof EntityArmorStand
                    || entityLivingBase instanceof EntityVillager
                    || entityLivingBase instanceof EntityAnimal && !animals
                    || entityLivingBase instanceof EntitySquid && !animals
                    || entityLivingBase instanceof EntityMob && !mobs
                    || entityLivingBase instanceof EntitySlime && !mobs
                    || CombatManager.getInstance().isIgnored(entity)
                    || entityLivingBase.isInvisible() && !invis){
                continue;
            }

            list.add(entityLivingBase);
        }
        return list;
    }

    public static boolean isValid(EntityLivingBase entityLivingBase, double range, boolean invis, boolean players, boolean animals, boolean mobs) {

        if (entityLivingBase == null || entityLivingBase.isDead)
            return false;

        return !(getRange(entityLivingBase) > range
                || entityLivingBase.isDead
                || entityLivingBase instanceof EntityArmorStand
                || entityLivingBase instanceof EntityVillager
                || entityLivingBase instanceof EntityPlayer && !players
                || entityLivingBase instanceof EntityAnimal && !animals
                || entityLivingBase instanceof EntityMob && !mobs
                || entityLivingBase.isInvisible() && !invis
                || CombatManager.getInstance().isIgnored(entityLivingBase)
                || mc.theWorld.getEntityByID(entityLivingBase.getEntityId()) != entityLivingBase
                || entityLivingBase == mc.thePlayer
                ||  entityLivingBase == null
                || entityLivingBase.getEntityId() == mc.thePlayer.getEntityId());
    }

    // For esp
    public static boolean isValidWithPlayer(Entity entity, boolean invis, boolean players, boolean animals, boolean mobs) {
        return !(entity.isDead
                || entity instanceof EntityArmorStand
                || entity instanceof EntityVillager
                || entity instanceof EntityPlayer && !players
                || entity instanceof EntityAnimal && !animals
                || entity instanceof EntityMob && !mobs
                || entity.isInvisible() && !invis
                || CombatManager.getInstance().isIgnored(entity)
                || mc.theWorld.getEntityByID(entity.getEntityId()) != entity
                || entity == null );
    }

    public static boolean isValidWithPlayer(Entity entity, float range, boolean invis, boolean players, boolean animals, boolean mobs) {
        return !(entity.isDead
                || entity == null
                || !(entity instanceof EntityLivingBase)
                || mc.thePlayer == entity && mc.gameSettings.thirdPersonView == 0
                || getRange(entity) > range
                || entity instanceof EntityArmorStand
                || entity instanceof EntityVillager
                || entity instanceof EntityPlayer && !players
                || entity instanceof EntityAnimal && !animals
                || entity instanceof EntityMob && !mobs
                || entity.isInvisible() && !invis
                || CombatManager.getInstance().isIgnored(entity)
                || mc.theWorld.getEntityByID(entity.getEntityId()) != entity
                || entity == null );
    }

    public static double yawDist(EntityLivingBase e) {
        final Vec3 difference = e.getPositionVector().addVector(0.0, e.getEyeHeight() / 2.0f, 0.0).subtract(mc.thePlayer.getPositionVector().addVector(0.0, mc.thePlayer.getEyeHeight(), 0.0));
        final double d = Math.abs(mc.thePlayer.rotationYaw - (Math.toDegrees(Math.atan2(difference.zCoord, difference.xCoord)) - 90.0f)) % 360.0f;
        return (d > 180.0f) ? (360.0f - d) : d;
    }

    public static double getRange(Entity entity) {
        if(mc.thePlayer == null)
            return 0;
        return mc.thePlayer.getPositionEyes(1.0f).distanceTo(RotationUtil.getBestVector(mc.thePlayer.getPositionEyes(1F),
                entity.getEntityBoundingBox()));
    }

    public static double getEffectiveHealth(EntityLivingBase entity) {
        return entity.getHealth() * (entity.getMaxHealth() / entity.getTotalArmorValue());
    }

}