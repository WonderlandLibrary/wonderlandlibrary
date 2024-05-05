package ru.smertnix.celestial.utils.math;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.feature.impl.combat.AttackAura;
import ru.smertnix.celestial.feature.impl.combat.Criticals;
import ru.smertnix.celestial.friend.Friend;
import ru.smertnix.celestial.ui.notification.NotificationMode;
import ru.smertnix.celestial.ui.notification.NotificationRenderer;
import ru.smertnix.celestial.utils.Helper;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class KillauraUtils implements Helper {
    public static TimerHelper timerHelper = new TimerHelper();

    public static boolean canAttack(EntityLivingBase player) {
        for (Friend friend : Celestial.instance.friendManager.getFriends()) {
            if (!player.getName().equals(friend.getName())) {
                continue;
            }
            return false;
        }
        if (player.getEntityId() == -10) {
            return false;
        }
        if (player instanceof EntitySlime && !AttackAura.targetsSetting.getSetting("Mobs").getBoolValue()) {
            return false;
        }
        if (player instanceof EntityMagmaCube && !AttackAura.targetsSetting.getSetting("Mobs").getBoolValue()) {
            return false;
        }
        if (player instanceof EntityDragon && !AttackAura.targetsSetting.getSetting("Mobs").getBoolValue()) {
            return false;
        }
        if (player instanceof EntityArmorStand) {
            return false;
        }
        if (!AttackAura.targetsSetting.getSetting("Players no armor").getBoolValue() && !InventoryHelper.isArmorPlayer(player))
            return false;

        if (player instanceof EntityPlayer || player instanceof EntityAnimal || player instanceof EntityMob || player instanceof EntityVillager) {
            if (player instanceof EntityPlayer && !AttackAura.targetsSetting.getSetting("Players").getBoolValue()) {
                return false;
            }

            if (player instanceof EntityAnimal && !AttackAura.targetsSetting.getSetting("Mobs").getBoolValue()) {
                return false;
            }
            if (player instanceof EntityMob && !AttackAura.targetsSetting.getSetting("Mobs").getBoolValue()) {
                return false;
            }
            if (player instanceof EntityVillager && !AttackAura.targetsSetting.getSetting("Mobs").getBoolValue()) {
                return false;
            }
            if (player instanceof EntityOcelot && !AttackAura.targetsSetting.getSetting("Mobs").getBoolValue()) {
                return false;
            }
            if (player instanceof EntityWolf && !AttackAura.targetsSetting.getSetting("Mobs").getBoolValue()) {
                return false;
            }
            if (player instanceof EntityEnderman && !AttackAura.targetsSetting.getSetting("Mobs").getBoolValue()) {
                return false;
            }
        }
        if (!range(player, AttackAura.range.getNumberValue() + AttackAura.rangeRot.getNumberValue())) {
            return false;
        }
        if (AttackAura.targetsSetting.getSetting("NPC / Bots").getBoolValue()) {
        	return player != mc.player;
        } else {
        	if ((!player.getUniqueID().equals(UUID.nameUUIDFromBytes(("OfflinePlayer:" + player.getName()).getBytes(StandardCharsets.UTF_8))) && (player instanceof EntityOtherPlayerMP))) {
            	return false;
            } else{
            	return player != mc.player;
            }
        }
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
    	 
    	if (AttackAura.targetp !=null) {
    		if (mc.player.connection.getPlayerInfo(mc.player.getUniqueID()).getResponseTime() < 70) { 
                	   return AttackAura.targetp.getDistanceToEntity(mc.player) <= range;
            } else {
   	        	   return AttackAura.targetp.getDistanceToEntity(mc.player) <= range + 10/3;
   	        }
    	}
    	   return mc.player.getDistanceToEntity(entity) <= range;
    }

    public static void attackEntity(EntityLivingBase target) {
        if (target == null || mc.player.getHealth() < 0.0f) {
            return;
        }
        if (AttackAura.targetp.getDistanceToEntity(mc.player) > AttackAura.range.getNumberValue()) {
            return;
        }
        if (!target.isDead) {
            String mode = AttackAura.rotationMode.getOptions();
            if (mode.equalsIgnoreCase("Vulcan")) {
                float[] matrix = RotationHelper.getRotations(target);
                mc.player.renderYawOffset = matrix[0];
                mc.player.rotationYaw = matrix[0];
                mc.player.rotationPitch = matrix[1];
            }
            float attackDelay = 0.85f * TPSUtils.getTickRate() / 20.0F / mc.timer.timerSpeed;
            if (mc.player.getCooledAttackStrength(attackDelay) == 1) {
                if (Celestial.instance.featureManager.getFeature(Criticals.class).isEnabled()) {
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.0645, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                }
                mc.playerController.attackEntity(mc.player, target);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                AttackAura.BreakShield(target);
            }
        }
    }

    public static EntityLivingBase getSortEntities() {
        List<EntityLivingBase> entity = new ArrayList<>();
        
        //float maxTicks = AttackAura.maxticks.getNumberValue();
        for (Entity e : mc.world.loadedEntityList) {
            if (e instanceof EntityLivingBase) {
                EntityLivingBase player = (EntityLivingBase) e;
               // if (mc.player.connection.getPlayerInfo(mc.player.getUniqueID()).getResponseTime() < 70) { 
                    if (mc.player.getDistanceToEntity(player) < AttackAura.range.getNumberValue() + AttackAura.rangeRot.getNumberValue() && (canAttack(player))) {
                    	if (player.getHealth() > 0 && !player.isDead)
                            entity.add(player);
                        else {
                            entity.remove(player);
                        }
                    }
               /* } else {
    	        	if (mc.player.getDistanceToEntity(player) < AttackAura.range.getNumberValue() + AttackAura.rangeRot.getNumberValue() + maxTicks / 3 && (canAttack(player))) {
                    	if (player.getHealth() > 0 && !player.isDead)
                            entity.add(player);
                        else {
                            entity.remove(player);
                        }
                    }
    	        }*/

            }
        }
        String mode = AttackAura.sortMode.getOptions();
        if (mode.equalsIgnoreCase("Distance")) {
            entity.sort(Comparator.comparingDouble(mc.player::getDistanceToEntity));
        } else if (mode.equalsIgnoreCase("FOV")) {
            entity.sort(Comparator.comparingDouble(KillauraUtils::Angle));
        } else if (mode.equalsIgnoreCase("Health")) {
            entity.sort((o1, o2) -> (int) (o1.getHealth() - o2.getHealth()));
        } else if (mode.equalsIgnoreCase("Single")) {
            entity.sort(Comparator.comparing(EntityLivingBase::getTotalArmorValue).reversed());
        }
        if (AttackAura.target != null && entity.contains(AttackAura.target)) {
            entity.removeIf(x -> x != AttackAura.target);
        }
        if (entity.isEmpty())
            return null;

        return entity.get(0);
    }

    public static float Angle(EntityLivingBase entity) {
        double diffX = entity.posX - mc.player.posX;
        double diffZ = entity.posZ - mc.player.posZ;
        return (float) Math.abs(MathHelper.wrapDegrees((Math.toDegrees(Math.atan2(diffZ, diffX)) - 90) - mc.player.rotationYaw));
    }

    public static Entity rayCast(Entity entityIn, double range) {
        Vec3d vec = entityIn.getPositionVector().add(new Vec3d(0, entityIn.getEyeHeight(), 0));
        Vec3d vecPositionVector = mc.player.getPositionVector().add(new Vec3d(0, mc.player.getEyeHeight(), 0));
        AxisAlignedBB axis = mc.player.getEntityBoundingBox().addCoord(vec.xCoord - vecPositionVector.xCoord, vec.yCoord - vecPositionVector.yCoord, vec.zCoord - vecPositionVector.zCoord).expand(1, 1, 1);
        Entity entityRayCast = null;
        for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(mc.player, axis)) {
            if (entity.canBeCollidedWith() && entity instanceof EntityLivingBase) {
                float size = entity.getCollisionBorderSize();
                AxisAlignedBB axis1 = entity.getEntityBoundingBox().expand(size, size, size);
                RayTraceResult rayTrace = axis1.calculateIntercept(vecPositionVector, vec);
                if (axis1.isVecInside(vecPositionVector)) {
                    if (range >= 0) {
                        entityRayCast = entity;
                        range = 0;
                    }
                } else if (rayTrace != null) {
                    double dist = vecPositionVector.distanceTo(rayTrace.hitVec);
                    if (range == 0 || dist < range) {
                        entityRayCast = entity;
                        range = dist;
                    }
                }
            }
        }

        return entityRayCast;
    }

}
