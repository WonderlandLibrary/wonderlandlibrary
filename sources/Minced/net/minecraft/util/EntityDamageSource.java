// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.entity.EntityLivingBase;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;

public class EntityDamageSource extends DamageSource
{
    @Nullable
    protected Entity damageSourceEntity;
    private boolean isThornsDamage;
    
    public EntityDamageSource(final String damageTypeIn, @Nullable final Entity damageSourceEntityIn) {
        super(damageTypeIn);
        this.damageSourceEntity = damageSourceEntityIn;
    }
    
    public EntityDamageSource setIsThornsDamage() {
        this.isThornsDamage = true;
        return this;
    }
    
    public boolean getIsThornsDamage() {
        return this.isThornsDamage;
    }
    
    @Nullable
    @Override
    public Entity getTrueSource() {
        return this.damageSourceEntity;
    }
    
    @Override
    public ITextComponent getDeathMessage(final EntityLivingBase entityLivingBaseIn) {
        final ItemStack itemstack = (this.damageSourceEntity instanceof EntityLivingBase) ? ((EntityLivingBase)this.damageSourceEntity).getHeldItemMainhand() : ItemStack.EMPTY;
        final String s = "death.attack." + this.damageType;
        final String s2 = s + ".item";
        return (!itemstack.isEmpty() && itemstack.hasDisplayName() && I18n.canTranslate(s2)) ? new TextComponentTranslation(s2, new Object[] { entityLivingBaseIn.getDisplayName(), this.damageSourceEntity.getDisplayName(), itemstack.getTextComponent() }) : new TextComponentTranslation(s, new Object[] { entityLivingBaseIn.getDisplayName(), this.damageSourceEntity.getDisplayName() });
    }
    
    @Override
    public boolean isDifficultyScaled() {
        return this.damageSourceEntity != null && this.damageSourceEntity instanceof EntityLivingBase && !(this.damageSourceEntity instanceof EntityPlayer);
    }
    
    @Nullable
    @Override
    public Vec3d getDamageLocation() {
        return new Vec3d(this.damageSourceEntity.posX, this.damageSourceEntity.posY, this.damageSourceEntity.posZ);
    }
}
