package fun.rich.client.utils.math;

import fun.rich.client.Rich;
import fun.rich.client.feature.impl.combat.AntiBot;
import fun.rich.client.feature.impl.combat.KillAura;
import fun.rich.client.friend.Friend;
import fun.rich.client.ui.notification.NotificationMode;
import fun.rich.client.ui.notification.NotificationRenderer;
import fun.rich.client.utils.Helper;
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
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KillauraUtils implements Helper {
    public static TimerHelper timerHelper = new TimerHelper();

    public static boolean canAttack(EntityLivingBase player) {
        for (Friend friend : Rich.instance.friendManager.getFriends()) {
            if (!player.getName().equals(friend.getName())) {
                continue;
            }
            return false;
        }
        if (player instanceof EntitySlime && !KillAura.targetsSetting.getSetting("Mobs").getBoolValue()) {
            return false;
        }
        if (player instanceof EntityMagmaCube && !KillAura.targetsSetting.getSetting("Mobs").getBoolValue()) {
            return false;
        }
        if (player instanceof EntityDragon && !KillAura.targetsSetting.getSetting("Mobs").getBoolValue()) {
            return false;
        }
        if (player instanceof EntityArmorStand) {
            return false;
        }
        if ((Rich.instance.featureManager.getFeature(AntiBot.class).isEnabled() && AntiBot.isBotPlayer.contains(player))) {
            return false;
        }
        if (player instanceof EntityPlayer || player instanceof EntityAnimal || player instanceof EntityMob || player instanceof EntityVillager) {
            if (player instanceof EntityPlayer && !KillAura.targetsSetting.getSetting("Players").getBoolValue()) {
                return false;
            }

            if (player instanceof EntityAnimal && !KillAura.targetsSetting.getSetting("Animals").getBoolValue()) {
                return false;
            }
            if (player instanceof EntityMob && !KillAura.targetsSetting.getSetting("Mobs").getBoolValue()) {
                return false;
            }
            if (player instanceof EntityVillager && !KillAura.targetsSetting.getSetting("Villagers").getBoolValue()) {
                return false;
            }
            if (player instanceof EntityOcelot && !KillAura.targetsSetting.getSetting("Animals").getBoolValue()) {
                return false;
            }
            if (player instanceof EntityWolf && !KillAura.targetsSetting.getSetting("Animals").getBoolValue()) {
                return false;
            }
            if (player instanceof EntityEnderman && !KillAura.targetsSetting.getSetting("Mobs").getBoolValue()) {
                return false;
            }
            if (player.isInvisible() && !KillAura.targetsSetting.getSetting("Invisibles").getBoolValue()) {
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

    public static void attackEntity(EntityLivingBase target) {
        if (target == null || mc.player.getHealth() < 0.0f) {
            return;
        }
        if (mc.player.getDistanceToEntity(target) > KillAura.range.getNumberValue()) {
            return;
        }
        if (!target.isDead) {
            float attackDelay = KillAura.attackCoolDown.getNumberValue() * TPSUtils.getTickRate() / 20.0F;
            if (mc.player.getCooledAttackStrength(attackDelay) == 1) {
                mc.playerController.attackEntity(mc.player, rayCast(target, KillAura.range.getNumberValue()));
                mc.player.swingArm(EnumHand.MAIN_HAND);
                KillAura.BreakShield(target);
            }
        }
    }

    public static EntityLivingBase getSortEntities() {
        List<EntityLivingBase> entity = new ArrayList<>();

        for (Entity e : mc.world.loadedEntityList) {
            if (e instanceof EntityLivingBase) {
                EntityLivingBase player = (EntityLivingBase) e;
                if (mc.player.getDistanceToEntity(player) < KillAura.range.getNumberValue() && (canAttack(player)))
                    if (player.getHealth() > 0 && !player.isDead)
                        entity.add(player);
                    else {
                        entity.remove(player);
                    }

            }
        }
        String mode = KillAura.sortMode.getOptions();
        if (mode.equalsIgnoreCase("Distance")) {
            entity.sort(Comparator.comparingDouble(mc.player::getDistanceToEntity));
        } else if (mode.equalsIgnoreCase("Crosshair")) {
            entity.sort(Comparator.comparingDouble(KillauraUtils::Angle));
        } else if (mode.equalsIgnoreCase("Health")) {
            entity.sort((o1, o2) -> (int) (o1.getHealth() - o2.getHealth()));
        } else if (mode.equalsIgnoreCase("Higher Armor")) {
            entity.sort(Comparator.comparing(EntityLivingBase::getTotalArmorValue).reversed());
        } else if (mode.equalsIgnoreCase("Lowest Armor")) {
            entity.sort(Comparator.comparing(EntityLivingBase::getTotalArmorValue));
        }
        if (KillAura.typeMode.currentMode.equalsIgnoreCase("Single") && KillAura.target != null && entity.contains(KillAura.target)) {
            entity.removeIf(x -> x != KillAura.target);
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
