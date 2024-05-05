package tech.drainwalk.utility.client;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.*;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.util.math.AxisAlignedBB;

public class ClientUtility extends tech.drainwalk.utility.Utility {
    private static final Frustum frustum = new Frustum();

    public static boolean isValid(Entity entity, boolean self, boolean players, boolean animals, boolean mobs, boolean pearls) {
        if (mc.gameSettings.thirdPersonView == 0 && entity == mc.player) {
            return false;
        }
        EntityLivingBase entityLivingBase = (EntityLivingBase) entity;

        if (entityLivingBase.getHealth() == 0) {
            return false;
        }
        if (entity == mc.player) {
            return self;
        }
        if (entity instanceof EntityAnimal || entity instanceof EntityGolem) {
            return animals;
        }
        if (entity instanceof EntityPlayer) {
            return players;
        }
        if (entity instanceof EntityEnderPearl) {
            return pearls;
        }
        if (entity instanceof EntityMob || entity instanceof EntitySlime) {
            return mobs;
        }
        if (entity.isDead) {
            return false;
        }
        if (entity instanceof EntityArmorStand) {
            return false;
        }
        if (entity instanceof IAnimals) {
            return false;
        }
        if (entity instanceof EntityItemFrame) {
            return false;
        }
        if (entity instanceof EntityArrow) {
            return false;
        }
        if (entity instanceof EntityMinecart) {
            return false;
        }
        if (entity instanceof EntityBoat) {
            return false;
        }
        if (entity instanceof EntityDragonFireball) {
            return false;
        }
        if (entity instanceof EntityXPOrb) {
            return false;
        }
        if (entity instanceof EntityTNTPrimed) {
            return false;
        }
        if (entity instanceof EntityExpBottle) {
            return false;
        }
        if (entity instanceof EntityLightningBolt) {
            return false;
        }
        if (entity instanceof EntityPotion) {
            return false;
        }
        if (entity instanceof Entity) {
            return false;
        }
        if (entity instanceof EntityDragon) {
            return false;
        }

        return false;
    }

    public static boolean isInViewFrustum(Entity entity) {
        return (isInViewFrustum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck);
    }

    private static boolean isInViewFrustum(AxisAlignedBB bb) {
        Entity current = mc.getRenderViewEntity();
        if (current != null) {
            frustum.setPosition(current.posX, current.posY, current.posZ);
        }
        return frustum.isBoundingBoxInFrustum(bb);
    }
}
