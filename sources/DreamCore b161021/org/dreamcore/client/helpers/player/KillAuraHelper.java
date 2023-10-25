package org.dreamcore.client.helpers.player;

import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import org.dreamcore.client.dreamcore;
import org.dreamcore.client.feature.impl.combat.AntiBot;
import org.dreamcore.client.feature.impl.combat.KillAura;
import org.dreamcore.client.friend.Friend;
import org.dreamcore.client.helpers.Helper;
import org.dreamcore.client.helpers.math.RotationHelper;
import org.dreamcore.client.helpers.world.EntityHelper;
import org.dreamcore.client.ui.notification.NotificationManager;
import org.dreamcore.client.ui.notification.NotificationType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KillAuraHelper implements Helper {

    public static boolean canAttack(EntityLivingBase player) {
        for (Friend friend : dreamcore.instance.friendManager.getFriends()) {
            if (!player.getName().equals(friend.getName())) {
                continue;
            }
            return false;
        }
        if ((dreamcore.instance.featureManager.getFeatureByClass(AntiBot.class).getState() && AntiBot.isBotPlayer.contains(player))) {
            return false;
        }
        if (KillAura.nakedPlayer.getBoolValue() && EntityHelper.checkArmor(player)) {
            return false;
        }
        if (player instanceof EntitySlime && !KillAura.monsters.getBoolValue()) {
            return false;
        }
        if (player instanceof EntityMagmaCube && !KillAura.monsters.getBoolValue()) {
            return false;
        }
        if (player instanceof EntityDragon && !KillAura.monsters.getBoolValue()) {
            return false;
        }
        if (player instanceof EntityEnderman && !KillAura.monsters.getBoolValue()) {
            return false;
        }
        if (player.isInvisible() && !KillAura.invis.getBoolValue()) {
            return false;
        }
        if (player instanceof EntityPlayer || player instanceof EntityAnimal || player instanceof EntityMob || player instanceof EntityVillager) {
            if (player instanceof EntityPlayer && !KillAura.players.getBoolValue()) {
                return false;
            }
            if (player instanceof EntityAnimal && !KillAura.animals.getBoolValue()) {
                return false;
            }
            if (player instanceof EntityMob && !KillAura.monsters.getBoolValue()) {
                return false;
            }
            if (player instanceof EntityVillager && !KillAura.animals.getBoolValue()) {
                return false;
            }
            if (player instanceof EntityOcelot && !KillAura.pets.getBoolValue()) {
                return false;
            }
            if (player instanceof EntityWolf && !KillAura.pets.getBoolValue()) {
                return false;
            }
        }
        if (!canSeeEntityAtFov(player, KillAura.fov.getNumberValue() * 2)) {
            return false;
        }
        if (!range(player, KillAura.range.getNumberValue())) {
            return false;
        }

        if (!player.canEntityBeSeen(mc.player)) {
            return KillAura.walls.getBoolValue();
        }
        return player != mc.player;
    }

    public static boolean canSeeEntityAtFov(Entity entityLiving, float scope) {
        double diffX = entityLiving.posX - mc.player.posX;
        double diffZ = entityLiving.posZ - mc.player.posZ;
        float yaw = (float) (Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
        double difference = angleDifference(yaw, mc.player.rotationYaw);
        return difference <= scope;
    }

    public static double angleDifference(float oldYaw, float newYaw) {
        float yaw = Math.abs(oldYaw - newYaw) % 360;
        if (yaw > 180) {
            yaw = 360 - yaw;
        }
        return yaw;
    }

    private static boolean range(EntityLivingBase entity, float range) {
        return mc.player.getDistanceToEntity(entity) <= range;
    }

    public static void toggleOffChecks() {
        KillAura killAura = (KillAura) dreamcore.instance.featureManager.getFeatureByClass(KillAura.class);
        if (mc.currentScreen instanceof GuiGameOver && !mc.player.isEntityAlive() || mc.player.ticksExisted <= 1 && KillAura.autoDisable.getBoolValue()) {
            killAura.toggle();
            NotificationManager.publicity(killAura.getLabel(), "KillAura was toggled off!", 2, NotificationType.SUCCESS);
        }
    }

    public static EntityLivingBase getSortEntities() {
        List<EntityLivingBase> entity = new ArrayList<>();
        for (Entity e : mc.world.loadedEntityList) {
            if (e instanceof EntityLivingBase) {
                EntityLivingBase player = (EntityLivingBase) e;
                if (mc.player.getDistanceToEntity(player) < KillAura.range.getNumberValue() && (canAttack(player))) {
                    if (player.getHealth() > 0) {
                        entity.add(player);
                    } else {
                        entity.remove(player);
                    }
                }
            }
        }

        String sortMode = KillAura.sort.getOptions();
        if (sortMode.equalsIgnoreCase("Angle")) {
            entity.sort(((o1, o2) -> (int) (Math.abs(RotationHelper.getAngleEntity(o1) - mc.player.rotationYaw) - Math.abs(RotationHelper.getAngleEntity(o2) - mc.player.rotationYaw))));
        } else if (sortMode.equalsIgnoreCase("Higher Armor")) {
            entity.sort(Comparator.comparing(EntityLivingBase::getTotalArmorValue).reversed());
        } else if (sortMode.equalsIgnoreCase("Lowest Armor")) {
            entity.sort(Comparator.comparing(EntityLivingBase::getTotalArmorValue));
        } else if (sortMode.equalsIgnoreCase("Health")) {
            entity.sort((o1, o2) -> (int) (o1.getHealth() - o2.getHealth()));
        } else if (sortMode.equalsIgnoreCase("Distance")) {
            entity.sort(Comparator.comparingDouble(mc.player::getDistanceToEntity));
        } else if (sortMode.equalsIgnoreCase("HurtTime")) {
            entity.sort(Comparator.comparing(EntityLivingBase::getHurtTime).reversed());
        } else if (sortMode.equalsIgnoreCase("Blocking Status")) {
            if (KillAura.target != null) {
                entity.sort(KillAura.target.isBlocking() ? Comparator.comparing(EntityLivingBase::isBlocking) : Comparator.comparingDouble(mc.player::getDistanceToEntity));
            }
        }

        if (entity.isEmpty())
            return null;

        return entity.get(0);
    }
}
