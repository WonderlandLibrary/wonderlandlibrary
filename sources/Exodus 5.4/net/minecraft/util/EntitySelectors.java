/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 */
package net.minecraft.util;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public final class EntitySelectors {
    public static final Predicate<Entity> selectInventories;
    public static final Predicate<Entity> selectAnything;
    public static final Predicate<Entity> IS_STANDALONE;
    public static final Predicate<Entity> NOT_SPECTATING;

    static {
        selectAnything = new Predicate<Entity>(){

            public boolean apply(Entity entity) {
                return entity.isEntityAlive();
            }
        };
        IS_STANDALONE = new Predicate<Entity>(){

            public boolean apply(Entity entity) {
                return entity.isEntityAlive() && entity.riddenByEntity == null && entity.ridingEntity == null;
            }
        };
        selectInventories = new Predicate<Entity>(){

            public boolean apply(Entity entity) {
                return entity instanceof IInventory && entity.isEntityAlive();
            }
        };
        NOT_SPECTATING = new Predicate<Entity>(){

            public boolean apply(Entity entity) {
                return !(entity instanceof EntityPlayer) || !((EntityPlayer)entity).isSpectator();
            }
        };
    }

    public static class ArmoredMob
    implements Predicate<Entity> {
        private final ItemStack armor;

        public ArmoredMob(ItemStack itemStack) {
            this.armor = itemStack;
        }

        public boolean apply(Entity entity) {
            if (!entity.isEntityAlive()) {
                return false;
            }
            if (!(entity instanceof EntityLivingBase)) {
                return false;
            }
            EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            return entityLivingBase.getEquipmentInSlot(EntityLiving.getArmorPosition(this.armor)) != null ? false : (entityLivingBase instanceof EntityLiving ? ((EntityLiving)entityLivingBase).canPickUpLoot() : (entityLivingBase instanceof EntityArmorStand ? true : entityLivingBase instanceof EntityPlayer));
        }
    }
}

