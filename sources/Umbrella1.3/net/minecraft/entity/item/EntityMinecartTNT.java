/*
 * Decompiled with CFR 0.150.
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
    private static final String __OBFID = "CL_00001680";

    public EntityMinecartTNT(World worldIn) {
        super(worldIn);
    }

    public EntityMinecartTNT(World worldIn, double p_i1728_2_, double p_i1728_4_, double p_i1728_6_) {
        super(worldIn, p_i1728_2_, p_i1728_4_, p_i1728_6_);
    }

    @Override
    public EntityMinecart.EnumMinecartType func_180456_s() {
        return EntityMinecart.EnumMinecartType.TNT;
    }

    @Override
    public IBlockState func_180457_u() {
        return Blocks.tnt.getDefaultState();
    }

    @Override
    public void onUpdate() {
        double var1;
        super.onUpdate();
        if (this.minecartTNTFuse > 0) {
            --this.minecartTNTFuse;
            this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5, this.posZ, 0.0, 0.0, 0.0, new int[0]);
        } else if (this.minecartTNTFuse == 0) {
            this.explodeCart(this.motionX * this.motionX + this.motionZ * this.motionZ);
        }
        if (this.isCollidedHorizontally && (var1 = this.motionX * this.motionX + this.motionZ * this.motionZ) >= (double)0.01f) {
            this.explodeCart(var1);
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        EntityArrow var4;
        Entity var3 = source.getSourceOfDamage();
        if (var3 instanceof EntityArrow && (var4 = (EntityArrow)var3).isBurning()) {
            this.explodeCart(var4.motionX * var4.motionX + var4.motionY * var4.motionY + var4.motionZ * var4.motionZ);
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public void killMinecart(DamageSource p_94095_1_) {
        super.killMinecart(p_94095_1_);
        double var2 = this.motionX * this.motionX + this.motionZ * this.motionZ;
        if (!p_94095_1_.isExplosion()) {
            this.entityDropItem(new ItemStack(Blocks.tnt, 1), 0.0f);
        }
        if (p_94095_1_.isFireDamage() || p_94095_1_.isExplosion() || var2 >= (double)0.01f) {
            this.explodeCart(var2);
        }
    }

    protected void explodeCart(double p_94103_1_) {
        if (!this.worldObj.isRemote) {
            double var3 = Math.sqrt(p_94103_1_);
            if (var3 > 5.0) {
                var3 = 5.0;
            }
            this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, (float)(4.0 + this.rand.nextDouble() * 1.5 * var3), true);
            this.setDead();
        }
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        if (distance >= 3.0f) {
            float var3 = distance / 10.0f;
            this.explodeCart(var3 * var3);
        }
        super.fall(distance, damageMultiplier);
    }

    @Override
    public void onActivatorRailPass(int p_96095_1_, int p_96095_2_, int p_96095_3_, boolean p_96095_4_) {
        if (p_96095_4_ && this.minecartTNTFuse < 0) {
            this.ignite();
        }
    }

    @Override
    public void handleHealthUpdate(byte p_70103_1_) {
        if (p_70103_1_ == 10) {
            this.ignite();
        } else {
            super.handleHealthUpdate(p_70103_1_);
        }
    }

    public void ignite() {
        this.minecartTNTFuse = 80;
        if (!this.worldObj.isRemote) {
            this.worldObj.setEntityState(this, (byte)10);
            if (!this.isSlient()) {
                this.worldObj.playSoundAtEntity(this, "game.tnt.primed", 1.0f, 1.0f);
            }
        }
    }

    public int func_94104_d() {
        return this.minecartTNTFuse;
    }

    public boolean isIgnited() {
        return this.minecartTNTFuse > -1;
    }

    @Override
    public float getExplosionResistance(Explosion p_180428_1_, World worldIn, BlockPos p_180428_3_, IBlockState p_180428_4_) {
        return this.isIgnited() && (BlockRailBase.func_176563_d(p_180428_4_) || BlockRailBase.func_176562_d(worldIn, p_180428_3_.offsetUp())) ? 0.0f : super.getExplosionResistance(p_180428_1_, worldIn, p_180428_3_, p_180428_4_);
    }

    @Override
    public boolean func_174816_a(Explosion p_174816_1_, World worldIn, BlockPos p_174816_3_, IBlockState p_174816_4_, float p_174816_5_) {
        return this.isIgnited() && (BlockRailBase.func_176563_d(p_174816_4_) || BlockRailBase.func_176562_d(worldIn, p_174816_3_.offsetUp())) ? false : super.func_174816_a(p_174816_1_, worldIn, p_174816_3_, p_174816_4_, p_174816_5_);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        if (tagCompund.hasKey("TNTFuse", 99)) {
            this.minecartTNTFuse = tagCompund.getInteger("TNTFuse");
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("TNTFuse", this.minecartTNTFuse);
    }
}

