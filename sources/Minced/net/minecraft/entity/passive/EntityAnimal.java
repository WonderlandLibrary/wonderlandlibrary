// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.passive;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.Entity;
import javax.annotation.Nullable;
import net.minecraft.util.EnumHand;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;

public abstract class EntityAnimal extends EntityAgeable implements IAnimals
{
    protected Block spawnableBlock;
    private int inLove;
    private UUID playerInLove;
    
    public EntityAnimal(final World worldIn) {
        super(worldIn);
        this.spawnableBlock = Blocks.GRASS;
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
                final double d0 = this.rand.nextGaussian() * 0.02;
                final double d2 = this.rand.nextGaussian() * 0.02;
                final double d3 = this.rand.nextGaussian() * 0.02;
                this.world.spawnParticle(EnumParticleTypes.HEART, this.posX + this.rand.nextFloat() * this.width * 2.0f - this.width, this.posY + 0.5 + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0f - this.width, d0, d2, d3, new int[0]);
            }
        }
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        this.inLove = 0;
        return super.attackEntityFrom(source, amount);
    }
    
    @Override
    public float getBlockPathWeight(final BlockPos pos) {
        return (this.world.getBlockState(pos.down()).getBlock() == this.spawnableBlock) ? 10.0f : (this.world.getLightBrightness(pos) - 0.5f);
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("InLove", this.inLove);
        if (this.playerInLove != null) {
            compound.setUniqueId("LoveCause", this.playerInLove);
        }
    }
    
    @Override
    public double getYOffset() {
        return 0.14;
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.inLove = compound.getInteger("InLove");
        this.playerInLove = (compound.hasUniqueId("LoveCause") ? compound.getUniqueId("LoveCause") : null);
    }
    
    @Override
    public boolean getCanSpawnHere() {
        final int i = MathHelper.floor(this.posX);
        final int j = MathHelper.floor(this.getEntityBoundingBox().minY);
        final int k = MathHelper.floor(this.posZ);
        final BlockPos blockpos = new BlockPos(i, j, k);
        return this.world.getBlockState(blockpos.down()).getBlock() == this.spawnableBlock && this.world.getLight(blockpos) > 8 && super.getCanSpawnHere();
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
    protected int getExperiencePoints(final EntityPlayer player) {
        return 1 + this.world.rand.nextInt(3);
    }
    
    public boolean isBreedingItem(final ItemStack stack) {
        return stack.getItem() == Items.WHEAT;
    }
    
    @Override
    public boolean processInteract(final EntityPlayer player, final EnumHand hand) {
        final ItemStack itemstack = player.getHeldItem(hand);
        if (!itemstack.isEmpty()) {
            if (this.isBreedingItem(itemstack) && this.getGrowingAge() == 0 && this.inLove <= 0) {
                this.consumeItemFromStack(player, itemstack);
                this.setInLove(player);
                return true;
            }
            if (this.isChild() && this.isBreedingItem(itemstack)) {
                this.consumeItemFromStack(player, itemstack);
                this.ageUp((int)(-this.getGrowingAge() / 20 * 0.1f), true);
                return true;
            }
        }
        return super.processInteract(player, hand);
    }
    
    protected void consumeItemFromStack(final EntityPlayer player, final ItemStack stack) {
        if (!player.capabilities.isCreativeMode) {
            stack.shrink(1);
        }
    }
    
    public void setInLove(@Nullable final EntityPlayer player) {
        this.inLove = 600;
        if (player != null) {
            this.playerInLove = player.getUniqueID();
        }
        this.world.setEntityState(this, (byte)18);
    }
    
    @Nullable
    public EntityPlayerMP getLoveCause() {
        if (this.playerInLove == null) {
            return null;
        }
        final EntityPlayer entityplayer = this.world.getPlayerEntityByUUID(this.playerInLove);
        return (entityplayer instanceof EntityPlayerMP) ? ((EntityPlayerMP)entityplayer) : null;
    }
    
    public boolean isInLove() {
        return this.inLove > 0;
    }
    
    public void resetInLove() {
        this.inLove = 0;
    }
    
    public boolean canMateWith(final EntityAnimal otherAnimal) {
        return otherAnimal != this && otherAnimal.getClass() == this.getClass() && this.isInLove() && otherAnimal.isInLove();
    }
    
    @Override
    public void handleStatusUpdate(final byte id) {
        if (id == 18) {
            for (int i = 0; i < 7; ++i) {
                final double d0 = this.rand.nextGaussian() * 0.02;
                final double d2 = this.rand.nextGaussian() * 0.02;
                final double d3 = this.rand.nextGaussian() * 0.02;
                this.world.spawnParticle(EnumParticleTypes.HEART, this.posX + this.rand.nextFloat() * this.width * 2.0f - this.width, this.posY + 0.5 + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0f - this.width, d0, d2, d3, new int[0]);
            }
        }
        else {
            super.handleStatusUpdate(id);
        }
    }
}
