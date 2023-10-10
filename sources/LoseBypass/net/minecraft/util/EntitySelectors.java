/*
 * Decompiled with CFR 0.152.
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
    public static final Predicate<Entity> selectAnything = new Predicate<Entity>(){

        @Override
        public boolean apply(Entity p_apply_1_) {
            return p_apply_1_.isEntityAlive();
        }
    };
    public static final Predicate<Entity> IS_STANDALONE = new Predicate<Entity>(){

        @Override
        public boolean apply(Entity p_apply_1_) {
            if (!p_apply_1_.isEntityAlive()) return false;
            if (p_apply_1_.riddenByEntity != null) return false;
            if (p_apply_1_.ridingEntity != null) return false;
            return true;
        }
    };
    public static final Predicate<Entity> selectInventories = new Predicate<Entity>(){

        @Override
        public boolean apply(Entity p_apply_1_) {
            if (!(p_apply_1_ instanceof IInventory)) return false;
            if (!p_apply_1_.isEntityAlive()) return false;
            return true;
        }
    };
    public static final Predicate<Entity> NOT_SPECTATING = new Predicate<Entity>(){

        @Override
        public boolean apply(Entity p_apply_1_) {
            if (!(p_apply_1_ instanceof EntityPlayer)) return true;
            if (!((EntityPlayer)p_apply_1_).isSpectator()) return true;
            return false;
        }
    };

    public static class ArmoredMob
    implements Predicate<Entity> {
        private final ItemStack armor;

        public ArmoredMob(ItemStack armor) {
            this.armor = armor;
        }

        @Override
        public boolean apply(Entity p_apply_1_) {
            boolean bl;
            if (!p_apply_1_.isEntityAlive()) {
                return false;
            }
            if (!(p_apply_1_ instanceof EntityLivingBase)) {
                return false;
            }
            EntityLivingBase entitylivingbase = (EntityLivingBase)p_apply_1_;
            if (entitylivingbase.getEquipmentInSlot(EntityLiving.getArmorPosition(this.armor)) != null) {
                return false;
            }
            if (entitylivingbase instanceof EntityLiving) {
                bl = ((EntityLiving)entitylivingbase).canPickUpLoot();
                return bl;
            }
            if (entitylivingbase instanceof EntityArmorStand) {
                return true;
            }
            bl = entitylivingbase instanceof EntityPlayer;
            return bl;
        }
    }
}

