/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;

public class EntityEnderCrystal
extends Entity {
    public int innerRotation;
    public int health;

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        ++this.innerRotation;
        this.dataWatcher.updateObject(8, this.health);
        int n = MathHelper.floor_double(this.posX);
        int n2 = MathHelper.floor_double(this.posY);
        int n3 = MathHelper.floor_double(this.posZ);
        if (this.worldObj.provider instanceof WorldProviderEnd && this.worldObj.getBlockState(new BlockPos(n, n2, n3)).getBlock() != Blocks.fire) {
            this.worldObj.setBlockState(new BlockPos(n, n2, n3), Blocks.fire.getDefaultState());
        }
    }

    @Override
    protected void entityInit() {
        this.dataWatcher.addObject(8, this.health);
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    public EntityEnderCrystal(World world) {
        super(world);
        this.preventEntitySpawning = true;
        this.setSize(2.0f, 2.0f);
        this.health = 5;
        this.innerRotation = this.rand.nextInt(100000);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
    }

    public EntityEnderCrystal(World world, double d, double d2, double d3) {
        this(world);
        this.setPosition(d, d2, d3);
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        if (this.isEntityInvulnerable(damageSource)) {
            return false;
        }
        if (!this.isDead && !this.worldObj.isRemote) {
            this.health = 0;
            if (this.health <= 0) {
                this.setDead();
                if (!this.worldObj.isRemote) {
                    this.worldObj.createExplosion(null, this.posX, this.posY, this.posZ, 6.0f, true);
                }
            }
        }
        return true;
    }
}

