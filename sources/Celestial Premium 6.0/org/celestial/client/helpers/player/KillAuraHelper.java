/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.helpers.player;

import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.combat.AntiBot;
import org.celestial.client.feature.impl.combat.KillAura;
import org.celestial.client.friend.Friend;
import org.celestial.client.helpers.Helper;
import org.celestial.client.helpers.math.RotationHelper;
import org.celestial.client.helpers.world.EntityHelper;

public class KillAuraHelper
implements Helper {
    public static boolean canAttack(EntityLivingBase player) {
        for (Friend friend : Celestial.instance.friendManager.getFriends()) {
            if (!player.getName().equals(friend.getName())) continue;
            return false;
        }
        if (Celestial.instance.featureManager.getFeatureByClass(AntiBot.class).getState() && !AntiBot.isRealPlayer.contains(player) && AntiBot.mode.currentMode.equals("Need Hit")) {
            return false;
        }
        if (Celestial.instance.featureManager.getFeatureByClass(AntiBot.class).getState() && AntiBot.isBotPlayer.contains(player) && (AntiBot.mode.currentMode.equals("Matrix New") || AntiBot.mode.currentMode.equals("Matrix"))) {
            return false;
        }
        if (KillAura.nakedPlayer.getCurrentValue() && player instanceof EntityPlayer && EntityHelper.checkArmor(player)) {
            return false;
        }
        if (player instanceof EntitySlime && !KillAura.mobs.getCurrentValue()) {
            return false;
        }
        if (player instanceof EntityMagmaCube && !KillAura.mobs.getCurrentValue()) {
            return false;
        }
        if (player instanceof EntityDragon && !KillAura.mobs.getCurrentValue()) {
            return false;
        }
        if (player instanceof EntityEnderman && !KillAura.mobs.getCurrentValue()) {
            return false;
        }
        if (player instanceof EntityWaterMob && !KillAura.mobs.getCurrentValue()) {
            return false;
        }
        if (player instanceof EntityPlayer || player instanceof EntityAnimal || player instanceof EntityMob || player instanceof EntityVillager) {
            if (player instanceof EntityPlayer && !KillAura.players.getCurrentValue()) {
                return false;
            }
            if (player instanceof EntityAnimal && !KillAura.mobs.getCurrentValue()) {
                return false;
            }
            if (player instanceof EntityMob && !KillAura.mobs.getCurrentValue()) {
                return false;
            }
            if (player instanceof EntityVillager && !KillAura.mobs.getCurrentValue()) {
                return false;
            }
        }
        if (player instanceof EntityArmorStand && !KillAura.armorStands.getCurrentValue()) {
            return false;
        }
        if (player.isInvisible() && !KillAura.invis.getCurrentValue()) {
            return false;
        }
        if (!KillAuraHelper.range(player, KillAura.range.getCurrentValue() + KillAura.preAimRange.getCurrentValue())) {
            return false;
        }
        if (!KillAuraHelper.canSeeEntityAtFov(player, KillAura.fov.getCurrentValue() * 2.0f)) {
            return false;
        }
        if (KillAura.team.getCurrentValue() && EntityHelper.isTeamWithYou(player)) {
            return false;
        }
        if (!player.canEntityBeSeen(KillAuraHelper.mc.player)) {
            return KillAura.walls.getCurrentValue();
        }
        return player != KillAuraHelper.mc.player && KillAuraHelper.mc.player != null && KillAuraHelper.mc.world != null;
    }

    public static boolean canSeeEntityAtFov(Entity entityLiving, float scope) {
        double diffZ = entityLiving.posZ - KillAuraHelper.mc.player.posZ;
        double diffX = entityLiving.posX - KillAuraHelper.mc.player.posX;
        float yaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
        double difference = KillAuraHelper.angleDifference(yaw, KillAuraHelper.mc.player.rotationYaw);
        return difference <= (double)scope;
    }

    public static double angleDifference(double a, double b) {
        float yaw360 = (float)(Math.abs(a - b) % 360.0);
        if (yaw360 > 180.0f) {
            yaw360 = 360.0f - yaw360;
        }
        return yaw360;
    }

    private static boolean range(EntityLivingBase entity, double range) {
        return (double)KillAuraHelper.mc.player.getDistanceToEntity(entity) <= range;
    }

    public static EntityLivingBase getSortEntities() {
        ArrayList<EntityLivingBase> entity = new ArrayList<EntityLivingBase>();
        for (Entity e : KillAuraHelper.mc.world.loadedEntityList) {
            if (e == null || !(e instanceof EntityLivingBase)) continue;
            EntityLivingBase player = (EntityLivingBase)e;
            if (player.getHealth() > 0.0f && !player.isDead) {
                if (!(KillAuraHelper.mc.player.getDistanceToEntity(player) <= KillAura.range.getCurrentValue() + KillAura.preAimRange.getCurrentValue()) || !KillAuraHelper.canAttack(player)) continue;
                entity.add(player);
                continue;
            }
            entity.remove(player);
        }
        String sortMode = KillAura.sort.getOptions();
        if (sortMode.equalsIgnoreCase("Angle")) {
            entity.sort((o1, o2) -> (int)(Math.abs(RotationHelper.getAngleEntity(o1) - KillAuraHelper.mc.player.rotationYaw) - Math.abs(RotationHelper.getAngleEntity(o2) - KillAuraHelper.mc.player.rotationYaw)));
        } else if (sortMode.equalsIgnoreCase("Higher Armor")) {
            entity.sort(Comparator.comparing(EntityLivingBase::getTotalArmorValue).reversed());
        } else if (sortMode.equalsIgnoreCase("Lowest Armor")) {
            entity.sort(Comparator.comparing(EntityLivingBase::getTotalArmorValue));
        } else if (sortMode.equalsIgnoreCase("Health")) {
            entity.sort((o1, o2) -> (int)(o1.getHealth() - o2.getHealth()));
        } else if (sortMode.equalsIgnoreCase("Distance")) {
            entity.sort(Comparator.comparingDouble(KillAuraHelper.mc.player::getDistanceToEntity));
        } else if (sortMode.equalsIgnoreCase("HurtTime")) {
            entity.sort(Comparator.comparing(EntityLivingBase::getHurtTime).reversed());
        } else if (sortMode.equalsIgnoreCase("Blocking Status")) {
            entity.sort(Comparator.comparing(EntityLivingBase::isUsingItem).reversed());
        }
        if (entity.isEmpty()) {
            return null;
        }
        return (EntityLivingBase)entity.get(0);
    }
}

