// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;

public class EntityDamageSourceIndirect extends EntityDamageSource
{
    private Entity indirectEntity;
    private static final String __OBFID = "CL_00001523";
    
    public EntityDamageSourceIndirect(final String p_i1568_1_, final Entity p_i1568_2_, final Entity p_i1568_3_) {
        super(p_i1568_1_, p_i1568_2_);
        this.indirectEntity = p_i1568_3_;
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
    public IChatComponent getDeathMessage(final EntityLivingBase p_151519_1_) {
        final IChatComponent var2 = (this.indirectEntity == null) ? this.damageSourceEntity.getDisplayName() : this.indirectEntity.getDisplayName();
        final ItemStack var3 = (this.indirectEntity instanceof EntityLivingBase) ? ((EntityLivingBase)this.indirectEntity).getHeldItem() : null;
        final String var4 = "death.attack." + this.damageType;
        final String var5 = var4 + ".item";
        return (var3 != null && var3.hasDisplayName() && StatCollector.canTranslate(var5)) ? new ChatComponentTranslation(var5, new Object[] { p_151519_1_.getDisplayName(), var2, var3.getChatComponent() }) : new ChatComponentTranslation(var4, new Object[] { p_151519_1_.getDisplayName(), var2 });
    }
}
