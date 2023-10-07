// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.projectile;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Iterator;
import net.minecraft.util.AxisAlignedBB;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;

public class EntityPotion extends EntityThrowable
{
    private ItemStack potionDamage;
    private static final String __OBFID = "CL_00001727";
    
    public EntityPotion(final World worldIn) {
        super(worldIn);
    }
    
    public EntityPotion(final World worldIn, final EntityLivingBase p_i1789_2_, final int p_i1789_3_) {
        this(worldIn, p_i1789_2_, new ItemStack(Items.potionitem, 1, p_i1789_3_));
    }
    
    public EntityPotion(final World worldIn, final EntityLivingBase p_i1790_2_, final ItemStack p_i1790_3_) {
        super(worldIn, p_i1790_2_);
        this.potionDamage = p_i1790_3_;
    }
    
    public EntityPotion(final World worldIn, final double p_i1791_2_, final double p_i1791_4_, final double p_i1791_6_, final int p_i1791_8_) {
        this(worldIn, p_i1791_2_, p_i1791_4_, p_i1791_6_, new ItemStack(Items.potionitem, 1, p_i1791_8_));
    }
    
    public EntityPotion(final World worldIn, final double p_i1792_2_, final double p_i1792_4_, final double p_i1792_6_, final ItemStack p_i1792_8_) {
        super(worldIn, p_i1792_2_, p_i1792_4_, p_i1792_6_);
        this.potionDamage = p_i1792_8_;
    }
    
    @Override
    protected float getGravityVelocity() {
        return 0.05f;
    }
    
    @Override
    protected float func_70182_d() {
        return 0.5f;
    }
    
    @Override
    protected float func_70183_g() {
        return -20.0f;
    }
    
    public void setPotionDamage(final int p_82340_1_) {
        if (this.potionDamage == null) {
            this.potionDamage = new ItemStack(Items.potionitem, 1, 0);
        }
        this.potionDamage.setItemDamage(p_82340_1_);
    }
    
    public int getPotionDamage() {
        if (this.potionDamage == null) {
            this.potionDamage = new ItemStack(Items.potionitem, 1, 0);
        }
        return this.potionDamage.getMetadata();
    }
    
    @Override
    protected void onImpact(final MovingObjectPosition p_70184_1_) {
        if (!this.worldObj.isRemote) {
            final List var2 = Items.potionitem.getEffects(this.potionDamage);
            if (var2 != null && !var2.isEmpty()) {
                final AxisAlignedBB var3 = this.getEntityBoundingBox().expand(4.0, 2.0, 4.0);
                final List var4 = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, var3);
                if (!var4.isEmpty()) {
                    for (final EntityLivingBase var6 : var4) {
                        final double var7 = this.getDistanceSqToEntity(var6);
                        if (var7 < 16.0) {
                            double var8 = 1.0 - Math.sqrt(var7) / 4.0;
                            if (var6 == p_70184_1_.entityHit) {
                                var8 = 1.0;
                            }
                            for (final PotionEffect var10 : var2) {
                                final int var11 = var10.getPotionID();
                                if (Potion.potionTypes[var11].isInstant()) {
                                    Potion.potionTypes[var11].func_180793_a(this, this.getThrower(), var6, var10.getAmplifier(), var8);
                                }
                                else {
                                    final int var12 = (int)(var8 * var10.getDuration() + 0.5);
                                    if (var12 <= 20) {
                                        continue;
                                    }
                                    var6.addPotionEffect(new PotionEffect(var11, var12, var10.getAmplifier()));
                                }
                            }
                        }
                    }
                }
            }
            this.worldObj.playAuxSFX(2002, new BlockPos(this), this.getPotionDamage());
            this.setDead();
        }
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        if (tagCompund.hasKey("Potion", 10)) {
            this.potionDamage = ItemStack.loadItemStackFromNBT(tagCompund.getCompoundTag("Potion"));
        }
        else {
            this.setPotionDamage(tagCompund.getInteger("potionValue"));
        }
        if (this.potionDamage == null) {
            this.setDead();
        }
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        if (this.potionDamage != null) {
            tagCompound.setTag("Potion", this.potionDamage.writeToNBT(new NBTTagCompound()));
        }
    }
}
