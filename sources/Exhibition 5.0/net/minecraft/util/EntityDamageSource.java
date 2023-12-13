// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;

public class EntityDamageSource extends DamageSource
{
    protected Entity damageSourceEntity;
    private boolean field_180140_r;
    private static final String __OBFID = "CL_00001522";
    
    public EntityDamageSource(final String p_i1567_1_, final Entity p_i1567_2_) {
        super(p_i1567_1_);
        this.field_180140_r = false;
        this.damageSourceEntity = p_i1567_2_;
    }
    
    public EntityDamageSource func_180138_v() {
        this.field_180140_r = true;
        return this;
    }
    
    public boolean func_180139_w() {
        return this.field_180140_r;
    }
    
    @Override
    public Entity getEntity() {
        return this.damageSourceEntity;
    }
    
    @Override
    public IChatComponent getDeathMessage(final EntityLivingBase p_151519_1_) {
        final ItemStack var2 = (this.damageSourceEntity instanceof EntityLivingBase) ? ((EntityLivingBase)this.damageSourceEntity).getHeldItem() : null;
        final String var3 = "death.attack." + this.damageType;
        final String var4 = var3 + ".item";
        return (var2 != null && var2.hasDisplayName() && StatCollector.canTranslate(var4)) ? new ChatComponentTranslation(var4, new Object[] { p_151519_1_.getDisplayName(), this.damageSourceEntity.getDisplayName(), var2.getChatComponent() }) : new ChatComponentTranslation(var3, new Object[] { p_151519_1_.getDisplayName(), this.damageSourceEntity.getDisplayName() });
    }
    
    @Override
    public boolean isDifficultyScaled() {
        return this.damageSourceEntity != null && this.damageSourceEntity instanceof EntityLivingBase && !(this.damageSourceEntity instanceof EntityPlayer);
    }
}
