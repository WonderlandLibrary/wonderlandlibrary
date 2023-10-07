// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.passive;

import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;

public abstract class EntityAnimal extends EntityAgeable implements IAnimals
{
    protected Block field_175506_bl;
    private int inLove;
    private EntityPlayer playerInLove;
    private static final String __OBFID = "CL_00001638";
    
    public EntityAnimal(final World worldIn) {
        super(worldIn);
        this.field_175506_bl = Blocks.grass;
    }
    
    @Override
    protected void updateAITasks() {
        if (this.getGrowingAge() != 0) {
            this.inLove = 0;
        }
        super.updateAITasks();
    }
    
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.getGrowingAge() != 0) {
            this.inLove = 0;
        }
        if (this.inLove > 0) {
            --this.inLove;
            if (this.inLove % 10 == 0) {
                final double var1 = this.rand.nextGaussian() * 0.02;
                final double var2 = this.rand.nextGaussian() * 0.02;
                final double var3 = this.rand.nextGaussian() * 0.02;
                this.worldObj.spawnParticle(EnumParticleTypes.HEART, this.posX + this.rand.nextFloat() * this.width * 2.0f - this.width, this.posY + 0.5 + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0f - this.width, var1, var2, var3, new int[0]);
            }
        }
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.func_180431_b(source)) {
            return false;
        }
        this.inLove = 0;
        return super.attackEntityFrom(source, amount);
    }
    
    @Override
    public float func_180484_a(final BlockPos p_180484_1_) {
        return (this.worldObj.getBlockState(p_180484_1_.offsetDown()).getBlock() == Blocks.grass) ? 10.0f : (this.worldObj.getLightBrightness(p_180484_1_) - 0.5f);
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("InLove", this.inLove);
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.inLove = tagCompund.getInteger("InLove");
    }
    
    @Override
    public boolean getCanSpawnHere() {
        final int var1 = MathHelper.floor_double(this.posX);
        final int var2 = MathHelper.floor_double(this.getEntityBoundingBox().minY);
        final int var3 = MathHelper.floor_double(this.posZ);
        final BlockPos var4 = new BlockPos(var1, var2, var3);
        return this.worldObj.getBlockState(var4.offsetDown()).getBlock() == this.field_175506_bl && this.worldObj.getLight(var4) > 8 && super.getCanSpawnHere();
    }
    
    @Override
    public int getTalkInterval() {
        return 120;
    }
    
    @Override
    protected boolean canDespawn() {
        return false;
    }
    
    @Override
    protected int getExperiencePoints(final EntityPlayer p_70693_1_) {
        return 1 + this.worldObj.rand.nextInt(3);
    }
    
    public boolean isBreedingItem(final ItemStack p_70877_1_) {
        return p_70877_1_ != null && p_70877_1_.getItem() == Items.wheat;
    }
    
    @Override
    public boolean interact(final EntityPlayer p_70085_1_) {
        final ItemStack var2 = p_70085_1_.inventory.getCurrentItem();
        if (var2 != null) {
            if (this.isBreedingItem(var2) && this.getGrowingAge() == 0 && this.inLove <= 0) {
                this.func_175505_a(p_70085_1_, var2);
                this.setInLove(p_70085_1_);
                return true;
            }
            if (this.isChild() && this.isBreedingItem(var2)) {
                this.func_175505_a(p_70085_1_, var2);
                this.func_175501_a((int)(-this.getGrowingAge() / 20 * 0.1f), true);
                return true;
            }
        }
        return super.interact(p_70085_1_);
    }
    
    protected void func_175505_a(final EntityPlayer p_175505_1_, final ItemStack p_175505_2_) {
        if (!p_175505_1_.capabilities.isCreativeMode) {
            --p_175505_2_.stackSize;
            if (p_175505_2_.stackSize <= 0) {
                p_175505_1_.inventory.setInventorySlotContents(p_175505_1_.inventory.currentItem, null);
            }
        }
    }
    
    public void setInLove(final EntityPlayer p_146082_1_) {
        this.inLove = 600;
        this.playerInLove = p_146082_1_;
        this.worldObj.setEntityState(this, (byte)18);
    }
    
    public EntityPlayer func_146083_cb() {
        return this.playerInLove;
    }
    
    public boolean isInLove() {
        return this.inLove > 0;
    }
    
    public void resetInLove() {
        this.inLove = 0;
    }
    
    public boolean canMateWith(final EntityAnimal p_70878_1_) {
        return p_70878_1_ != this && p_70878_1_.getClass() == this.getClass() && (this.isInLove() && p_70878_1_.isInLove());
    }
    
    @Override
    public void handleHealthUpdate(final byte p_70103_1_) {
        if (p_70103_1_ == 18) {
            for (int var2 = 0; var2 < 7; ++var2) {
                final double var3 = this.rand.nextGaussian() * 0.02;
                final double var4 = this.rand.nextGaussian() * 0.02;
                final double var5 = this.rand.nextGaussian() * 0.02;
                this.worldObj.spawnParticle(EnumParticleTypes.HEART, this.posX + this.rand.nextFloat() * this.width * 2.0f - this.width, this.posY + 0.5 + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0f - this.width, var3, var4, var5, new int[0]);
            }
        }
        else {
            super.handleHealthUpdate(p_70103_1_);
        }
    }
}
