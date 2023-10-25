/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.apache.commons.lang3.Validate;

public abstract class EntityHanging
extends Entity {
    private static final Predicate<Entity> IS_HANGING_ENTITY = new Predicate<Entity>(){

        @Override
        public boolean apply(@Nullable Entity p_apply_1_) {
            return p_apply_1_ instanceof EntityHanging;
        }
    };
    private int tickCounter1;
    protected BlockPos hangingPosition;
    @Nullable
    public EnumFacing facingDirection;

    public EntityHanging(World worldIn) {
        super(worldIn);
        this.setSize(0.5f, 0.5f);
    }

    public EntityHanging(World worldIn, BlockPos hangingPositionIn) {
        this(worldIn);
        this.hangingPosition = hangingPositionIn;
    }

    @Override
    protected void entityInit() {
    }

    protected void updateFacingWithBoundingBox(EnumFacing facingDirectionIn) {
        Validate.notNull(facingDirectionIn);
        Validate.isTrue(facingDirectionIn.getAxis().isHorizontal());
        this.facingDirection = facingDirectionIn;
        this.prevRotationYaw = this.rotationYaw = (float)(this.facingDirection.getHorizontalIndex() * 90);
        this.updateBoundingBox();
    }

    protected void updateBoundingBox() {
        if (this.facingDirection != null) {
            double d0 = (double)this.hangingPosition.getX() + 0.5;
            double d1 = (double)this.hangingPosition.getY() + 0.5;
            double d2 = (double)this.hangingPosition.getZ() + 0.5;
            double d3 = 0.46875;
            double d4 = this.offs(this.getWidthPixels());
            double d5 = this.offs(this.getHeightPixels());
            d0 -= (double)this.facingDirection.getXOffset() * 0.46875;
            d2 -= (double)this.facingDirection.getZOffset() * 0.46875;
            EnumFacing enumfacing = this.facingDirection.rotateYCCW();
            this.posX = d0 += d4 * (double)enumfacing.getXOffset();
            this.posY = d1 += d5;
            this.posZ = d2 += d4 * (double)enumfacing.getZOffset();
            double d6 = this.getWidthPixels();
            double d7 = this.getHeightPixels();
            double d8 = this.getWidthPixels();
            if (this.facingDirection.getAxis() == EnumFacing.Axis.Z) {
                d8 = 1.0;
            } else {
                d6 = 1.0;
            }
            this.setEntityBoundingBox(new AxisAlignedBB(d0 - (d6 /= 32.0), d1 - (d7 /= 32.0), d2 - (d8 /= 32.0), d0 + d6, d1 + d7, d2 + d8));
        }
    }

    private double offs(int p_190202_1_) {
        return p_190202_1_ % 32 == 0 ? 0.5 : 0.0;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.tickCounter1++ == 100 && !this.world.isRemote) {
            this.tickCounter1 = 0;
            if (!this.isDead && !this.onValidSurface()) {
                this.setDead();
                this.onBroken(null);
            }
        }
    }

    public boolean onValidSurface() {
        if (!this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty()) {
            return false;
        }
        int i = Math.max(1, this.getWidthPixels() / 16);
        int j = Math.max(1, this.getHeightPixels() / 16);
        BlockPos blockpos = this.hangingPosition.offset(this.facingDirection.getOpposite());
        EnumFacing enumfacing = this.facingDirection.rotateYCCW();
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int k = 0; k < i; ++k) {
            for (int l = 0; l < j; ++l) {
                int i1 = (i - 1) / -2;
                int j1 = (j - 1) / -2;
                blockpos$mutableblockpos.setPos(blockpos).move(enumfacing, k + i1).move(EnumFacing.UP, l + j1);
                IBlockState iblockstate = this.world.getBlockState(blockpos$mutableblockpos);
                if (iblockstate.getMaterial().isSolid() || BlockRedstoneDiode.isDiode(iblockstate)) continue;
                return false;
            }
        }
        return this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox(), IS_HANGING_ENTITY).isEmpty();
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean hitByEntity(Entity entityIn) {
        return entityIn instanceof EntityPlayer ? this.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)entityIn), 0.0f) : false;
    }

    @Override
    public EnumFacing getHorizontalFacing() {
        return this.facingDirection;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (!this.isDead && !this.world.isRemote) {
            this.setDead();
            this.setBeenAttacked();
            this.onBroken(source.getEntity());
        }
        return true;
    }

    @Override
    public void moveEntity(MoverType x, double p_70091_2_, double p_70091_4_, double p_70091_6_) {
        if (!this.world.isRemote && !this.isDead && p_70091_2_ * p_70091_2_ + p_70091_4_ * p_70091_4_ + p_70091_6_ * p_70091_6_ > 0.0) {
            this.setDead();
            this.onBroken(null);
        }
    }

    @Override
    public void addVelocity(double x, double y, double z) {
        if (!this.world.isRemote && !this.isDead && x * x + y * y + z * z > 0.0) {
            this.setDead();
            this.onBroken(null);
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        compound.setByte("Facing", (byte)this.facingDirection.getHorizontalIndex());
        BlockPos blockpos = this.getHangingPosition();
        compound.setInteger("TileX", blockpos.getX());
        compound.setInteger("TileY", blockpos.getY());
        compound.setInteger("TileZ", blockpos.getZ());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        this.hangingPosition = new BlockPos(compound.getInteger("TileX"), compound.getInteger("TileY"), compound.getInteger("TileZ"));
        this.updateFacingWithBoundingBox(EnumFacing.byHorizontalIndex(compound.getByte("Facing")));
    }

    public abstract int getWidthPixels();

    public abstract int getHeightPixels();

    public abstract void onBroken(Entity var1);

    public abstract void playPlaceSound();

    @Override
    public EntityItem entityDropItem(ItemStack stack, float offsetY) {
        EntityItem entityitem = new EntityItem(this.world, this.posX + (double)((float)this.facingDirection.getXOffset() * 0.15f), this.posY + (double)offsetY, this.posZ + (double)((float)this.facingDirection.getZOffset() * 0.15f), stack);
        entityitem.setDefaultPickupDelay();
        this.world.spawnEntityInWorld(entityitem);
        return entityitem;
    }

    @Override
    protected boolean shouldSetPosAfterLoading() {
        return false;
    }

    @Override
    public void setPosition(double x, double y, double z) {
        this.hangingPosition = new BlockPos(x, y, z);
        this.updateBoundingBox();
        this.isAirBorne = true;
    }

    public BlockPos getHangingPosition() {
        return this.hangingPosition;
    }

    @Override
    public float getRotatedYaw(Rotation transformRotation) {
        if (this.facingDirection != null && this.facingDirection.getAxis() != EnumFacing.Axis.Y) {
            switch (transformRotation) {
                case CLOCKWISE_180: {
                    this.facingDirection = this.facingDirection.getOpposite();
                    break;
                }
                case COUNTERCLOCKWISE_90: {
                    this.facingDirection = this.facingDirection.rotateYCCW();
                    break;
                }
                case CLOCKWISE_90: {
                    this.facingDirection = this.facingDirection.rotateY();
                }
            }
        }
        float f = MathHelper.wrapDegrees(this.rotationYaw);
        switch (transformRotation) {
            case CLOCKWISE_180: {
                return f + 180.0f;
            }
            case COUNTERCLOCKWISE_90: {
                return f + 90.0f;
            }
            case CLOCKWISE_90: {
                return f + 270.0f;
            }
        }
        return f;
    }

    @Override
    public float getMirroredYaw(Mirror transformMirror) {
        return this.getRotatedYaw(transformMirror.toRotation(this.facingDirection));
    }

    @Override
    public void onStruckByLightning(EntityLightningBolt lightningBolt) {
    }
}

