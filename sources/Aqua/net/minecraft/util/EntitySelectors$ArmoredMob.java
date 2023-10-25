package net.minecraft.util;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public static class EntitySelectors.ArmoredMob
implements Predicate<Entity> {
    private final ItemStack armor;

    public EntitySelectors.ArmoredMob(ItemStack armor) {
        this.armor = armor;
    }

    public boolean apply(Entity p_apply_1_) {
        if (!p_apply_1_.isEntityAlive()) {
            return false;
        }
        if (!(p_apply_1_ instanceof EntityLivingBase)) {
            return false;
        }
        EntityLivingBase entitylivingbase = (EntityLivingBase)p_apply_1_;
        return entitylivingbase.getEquipmentInSlot(EntityLiving.getArmorPosition((ItemStack)this.armor)) != null ? false : (entitylivingbase instanceof EntityLiving ? ((EntityLiving)entitylivingbase).canPickUpLoot() : (entitylivingbase instanceof EntityArmorStand ? true : entitylivingbase instanceof EntityPlayer));
    }
}
