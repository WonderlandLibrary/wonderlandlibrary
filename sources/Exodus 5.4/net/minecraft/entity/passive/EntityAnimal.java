/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class EntityAnimal
extends EntityAgeable
implements IAnimals {
    private EntityPlayer playerInLove;
    protected Block spawnableBlock = Blocks.grass;
    private int inLove;

    public void resetInLove() {
        this.inLove = 0;
    }

    public boolean canMateWith(EntityAnimal entityAnimal) {
        return entityAnimal == this ? false : (entityAnimal.getClass() != this.getClass() ? false : this.isInLove() && entityAnimal.isInLove());
    }

    public EntityAnimal(World world) {
        super(world);
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        if (this.isEntityInvulnerable(damageSource)) {
            return false;
        }
        this.inLove = 0;
        return super.attackEntityFrom(damageSource, f);
    }

    @Override
    protected int getExperiencePoints(EntityPlayer entityPlayer) {
        return 1 + this.worldObj.rand.nextInt(3);
    }

    @Override
    public float getBlockPathWeight(BlockPos blockPos) {
        return this.worldObj.getBlockState(blockPos.down()).getBlock() == Blocks.grass ? 10.0f : this.worldObj.getLightBrightness(blockPos) - 0.5f;
    }

    @Override
    public void handleStatusUpdate(byte by) {
        if (by == 18) {
            int n = 0;
            while (n < 7) {
                double d = this.rand.nextGaussian() * 0.02;
                double d2 = this.rand.nextGaussian() * 0.02;
                double d3 = this.rand.nextGaussian() * 0.02;
                this.worldObj.spawnParticle(EnumParticleTypes.HEART, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width, this.posY + 0.5 + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width, d, d2, d3, new int[0]);
                ++n;
            }
        } else {
            super.handleStatusUpdate(by);
        }
    }

    public void setInLove(EntityPlayer entityPlayer) {
        this.inLove = 600;
        this.playerInLove = entityPlayer;
        this.worldObj.setEntityState(this, (byte)18);
    }

    @Override
    public boolean interact(EntityPlayer entityPlayer) {
        ItemStack itemStack = entityPlayer.inventory.getCurrentItem();
        if (itemStack != null) {
            if (this.isBreedingItem(itemStack) && this.getGrowingAge() == 0 && this.inLove <= 0) {
                this.consumeItemFromStack(entityPlayer, itemStack);
                this.setInLove(entityPlayer);
                return true;
            }
            if (this.isChild() && this.isBreedingItem(itemStack)) {
                this.consumeItemFromStack(entityPlayer, itemStack);
                this.func_175501_a((int)((float)(-this.getGrowingAge() / 20) * 0.1f), true);
                return true;
            }
        }
        return super.interact(entityPlayer);
    }

    @Override
    protected void updateAITasks() {
        if (this.getGrowingAge() != 0) {
            this.inLove = 0;
        }
        super.updateAITasks();
    }

    @Override
    protected boolean canDespawn() {
        return false;
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
                double d = this.rand.nextGaussian() * 0.02;
                double d2 = this.rand.nextGaussian() * 0.02;
                double d3 = this.rand.nextGaussian() * 0.02;
                this.worldObj.spawnParticle(EnumParticleTypes.HEART, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width, this.posY + 0.5 + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width, d, d2, d3, new int[0]);
            }
        }
    }

    @Override
    public int getTalkInterval() {
        return 120;
    }

    @Override
    public boolean getCanSpawnHere() {
        int n;
        int n2;
        int n3 = MathHelper.floor_double(this.posX);
        BlockPos blockPos = new BlockPos(n3, n2 = MathHelper.floor_double(this.getEntityBoundingBox().minY), n = MathHelper.floor_double(this.posZ));
        return this.worldObj.getBlockState(blockPos.down()).getBlock() == this.spawnableBlock && this.worldObj.getLight(blockPos) > 8 && super.getCanSpawnHere();
    }

    public EntityPlayer getPlayerInLove() {
        return this.playerInLove;
    }

    public boolean isBreedingItem(ItemStack itemStack) {
        return itemStack == null ? false : itemStack.getItem() == Items.wheat;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        nBTTagCompound.setInteger("InLove", this.inLove);
    }

    public boolean isInLove() {
        return this.inLove > 0;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        super.readEntityFromNBT(nBTTagCompound);
        this.inLove = nBTTagCompound.getInteger("InLove");
    }

    protected void consumeItemFromStack(EntityPlayer entityPlayer, ItemStack itemStack) {
        if (!entityPlayer.capabilities.isCreativeMode) {
            --itemStack.stackSize;
            if (itemStack.stackSize <= 0) {
                entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, null);
            }
        }
    }
}

