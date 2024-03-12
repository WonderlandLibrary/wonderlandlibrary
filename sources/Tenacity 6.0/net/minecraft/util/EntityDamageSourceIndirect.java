// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;

public class EntityDamageSourceIndirect extends EntityDamageSource
{
    private Entity indirectEntity;
    
    public EntityDamageSourceIndirect(final String damageTypeIn, final Entity source, final Entity indirectEntityIn) {
        super(damageTypeIn, source);
        this.indirectEntity = indirectEntityIn;
    }
    
    @Override
    public Entity getSourceOfDamage() {
        return this.damageSourceEntity;
    }
    
    @Override
    public Entity getEntity() {
        return this.indirectEntity;
    }
    
    @Override
    public IChatComponent getDeathMessage(final EntityLivingBase entityLivingBaseIn) {
        final IChatComponent ichatcomponent = (this.indirectEntity == null) ? this.damageSourceEntity.getDisplayName() : this.indirectEntity.getDisplayName();
        final ItemStack itemstack = (this.indirectEntity instanceof EntityLivingBase) ? ((EntityLivingBase)this.indirectEntity).getHeldItem() : null;
        final String s = "death.attack." + this.damageType;
        final String s2 = s + ".item";
        return (itemstack != null && itemstack.hasDisplayName() && StatCollector.canTranslate(s2)) ? new ChatComponentTranslation(s2, new Object[] { entityLivingBaseIn.getDisplayName(), ichatcomponent, itemstack.getChatComponent() }) : new ChatComponentTranslation(s, new Object[] { entityLivingBaseIn.getDisplayName(), ichatcomponent });
    }
}
