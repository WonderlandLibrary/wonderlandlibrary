/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.item;

import net.minecraft.block.BlockRailBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityMinecartTNT
extends EntityMinecart {
    private int minecartTNTFuse = -1;

    @Override
    public EntityMinecart.EnumMinecartType getMinecartType() {
        return EntityMinecart.EnumMinecartType.TNT;
    }

    public EntityMinecartTNT(World world, double d, double d2, double d3) {
        super(world, d, d2, d3);
    }

    protected void explodeCart(double d) {
        if (!this.worldObj.isRemote) {
            double d2 = Math.sqrt(d);
            if (d2 > 5.0) {
                d2 = 5.0;
            }
            this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, (float)(4.0 + this.rand.nextDouble() * 1.5 * d2), true);
            this.setDead();
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        nBTTagCompound.setInteger("TNTFuse", this.minecartTNTFuse);
    }

    public int getFuseTicks() {
        return this.minecartTNTFuse;
    }

    @Override
    public void handleStatusUpdate(byte by) {
        if (by == 10) {
            this.ignite();
        } else {
            super.handleStatusUpdate(by);
        }
    }

    @Override
    public void onActivatorRailPass(int n, int n2, int n3, boolean bl) {
        if (bl && this.minecartTNTFuse < 0) {
            this.ignite();
        }
    }

    public boolean isIgnited() {
        return this.minecartTNTFuse > -1;
    }

    public void ignite() {
        this.minecartTNTFuse = 80;
        if (!this.worldObj.isRemote) {
            this.worldObj.setEntityState(this, (byte)10);
            if (!this.isSilent()) {
                this.worldObj.playSoundAtEntity(this, "game.tnt.primed", 1.0f, 1.0f);
            }
        }
    }

    @Override
    public void onUpdate() {
        double d;
        super.onUpdate();
        if (this.minecartTNTFuse > 0) {
            --this.minecartTNTFuse;
            this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5, this.posZ, 0.0, 0.0, 0.0, new int[0]);
        } else if (this.minecartTNTFuse == 0) {
            this.explodeCart(this.motionX * this.motionX + this.motionZ * this.motionZ);
        }
        if (this.isCollidedHorizontally && (d = this.motionX * this.motionX + this.motionZ * this.motionZ) >= (double)0.01f) {
            this.explodeCart(d);
        }
    }

    @Override
    public boolean verifyExplosion(Explosion explosion, World world, BlockPos blockPos, IBlockState iBlockState, float f) {
        return !this.isIgnited() || !BlockRailBase.isRailBlock(iBlockState) && !BlockRailBase.isRailBlock(world, blockPos.up()) ? super.verifyExplosion(explosion, world, blockPos, iBlockState, f) : false;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        super.readEntityFromNBT(nBTTagCompound);
        if (nBTTagCompound.hasKey("TNTFuse", 99)) {
            this.minecartTNTFuse = nBTTagCompound.getInteger("TNTFuse");
        }
    }

    @Override
    public float getExplosionResistance(Explosion explosion, World world, BlockPos blockPos, IBlockState iBlockState) {
        return !this.isIgnited() || !BlockRailBase.isRailBlock(iBlockState) && !BlockRailBase.isRailBlock(world, blockPos.up()) ? super.getExplosionResistance(explosion, world, blockPos, iBlockState) : 0.0f;
    }

    @Override
    public void killMinecart(DamageSource damageSource) {
        super.killMinecart(damageSource);
        double d = this.motionX * this.motionX + this.motionZ * this.motionZ;
        if (!damageSource.isExplosion() && this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
            this.entityDropItem(new ItemStack(Blocks.tnt, 1), 0.0f);
        }
        if (damageSource.isFireDamage() || damageSource.isExplosion() || d >= (double)0.01f) {
            this.explodeCart(d);
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        EntityArrow entityArrow;
        Entity entity = damageSource.getSourceOfDamage();
        if (entity instanceof EntityArrow && (entityArrow = (EntityArrow)entity).isBurning()) {
            this.explodeCart(entityArrow.motionX * entityArrow.motionX + entityArrow.motionY * entityArrow.motionY + entityArrow.motionZ * entityArrow.motionZ);
        }
        return super.attackEntityFrom(damageSource, f);
    }

    public EntityMinecartTNT(World world) {
        super(world);
    }

    @Override
    public void fall(float f, float f2) {
        if (f >= 3.0f) {
            float f3 = f / 10.0f;
            this.explodeCart(f3 * f3);
        }
        super.fall(f, f2);
    }

    @Override
    public IBlockState getDefaultDisplayTile() {
        return Blocks.tnt.getDefaultState();
    }
}

