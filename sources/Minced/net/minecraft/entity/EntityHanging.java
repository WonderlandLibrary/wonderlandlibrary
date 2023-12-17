// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.Mirror;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.Rotation;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.AxisAlignedBB;
import org.apache.commons.lang3.Validate;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import com.google.common.base.Predicate;

public abstract class EntityHanging extends Entity
{
    private static final Predicate<Entity> IS_HANGING_ENTITY;
    private int tickCounter1;
    protected BlockPos hangingPosition;
    @Nullable
    public EnumFacing facingDirection;
    
    public EntityHanging(final World worldIn) {
        super(worldIn);
        this.setSize(0.5f, 0.5f);
    }
    
    public EntityHanging(final World worldIn, final BlockPos hangingPositionIn) {
        this(worldIn);
        this.hangingPosition = hangingPositionIn;
    }
    
    @Override
    protected void entityInit() {
    }
    
    protected void updateFacingWithBoundingBox(final EnumFacing facingDirectionIn) {
        Validate.notNull((Object)facingDirectionIn);
        Validate.isTrue(facingDirectionIn.getAxis().isHorizontal());
        this.facingDirection = facingDirectionIn;
        this.rotationYaw = (float)(this.facingDirection.getHorizontalIndex() * 90);
        this.prevRotationYaw = this.rotationYaw;
        this.updateBoundingBox();
    }
    
    protected void updateBoundingBox() {
        if (this.facingDirection != null) {
            double d0 = this.hangingPosition.getX() + 0.5;
            double d2 = this.hangingPosition.getY() + 0.5;
            double d3 = this.hangingPosition.getZ() + 0.5;
            final double d4 = 0.46875;
            final double d5 = this.offs(this.getWidthPixels());
            final double d6 = this.offs(this.getHeightPixels());
            d0 -= this.facingDirection.getXOffset() * 0.46875;
            d3 -= this.facingDirection.getZOffset() * 0.46875;
            d2 += d6;
            final EnumFacing enumfacing = this.facingDirection.rotateYCCW();
            d0 += d5 * enumfacing.getXOffset();
            d3 += d5 * enumfacing.getZOffset();
            this.posX = d0;
            this.posY = d2;
            this.posZ = d3;
            double d7 = this.getWidthPixels();
            double d8 = this.getHeightPixels();
            double d9 = this.getWidthPixels();
            if (this.facingDirection.getAxis() == EnumFacing.Axis.Z) {
                d9 = 1.0;
            }
            else {
                d7 = 1.0;
            }
            d7 /= 32.0;
            d8 /= 32.0;
            d9 /= 32.0;
            this.setEntityBoundingBox(new AxisAlignedBB(d0 - d7, d2 - d8, d3 - d9, d0 + d7, d2 + d8, d3 + d9));
        }
    }
    
    private double offs(final int p_190202_1_) {
        return (p_190202_1_ % 32 == 0) ? 0.5 : 0.0;
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
        final int i = Math.max(1, this.getWidthPixels() / 16);
        final int j = Math.max(1, this.getHeightPixels() / 16);
        final BlockPos blockpos = this.hangingPosition.offset(this.facingDirection.getOpposite());
        final EnumFacing enumfacing = this.facingDirection.rotateYCCW();
        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int k = 0; k < i; ++k) {
            for (int l = 0; l < j; ++l) {
                final int i2 = (i - 1) / -2;
                final int j2 = (j - 1) / -2;
                blockpos$mutableblockpos.setPos(blockpos).move(enumfacing, k + i2).move(EnumFacing.UP, l + j2);
                final IBlockState iblockstate = this.world.getBlockState(blockpos$mutableblockpos);
                if (!iblockstate.getMaterial().isSolid() && !BlockRedstoneDiode.isDiode(iblockstate)) {
                    return false;
                }
            }
        }
        return this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox(), EntityHanging.IS_HANGING_ENTITY).isEmpty();
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return true;
    }
    
    @Override
    public boolean hitByEntity(final Entity entityIn) {
        return entityIn instanceof EntityPlayer && this.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)entityIn), 0.0f);
    }
    
    @Override
    public EnumFacing getHorizontalFacing() {
        return this.facingDirection;
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (!this.isDead && !this.world.isRemote) {
            this.setDead();
            this.markVelocityChanged();
            this.onBroken(source.getTrueSource());
        }
        return true;
    }
    
    @Override
    public void move(final MoverType type, final double x, final double y, final double z) {
        if (!this.world.isRemote && !this.isDead && x * x + y * y + z * z > 0.0) {
            this.setDead();
            this.onBroken(null);
        }
    }
    
    @Override
    public void addVelocity(final double x, final double y, final double z) {
        if (!this.world.isRemote && !this.isDead && x * x + y * y + z * z > 0.0) {
            this.setDead();
            this.onBroken(null);
        }
    }
    
    public void writeEntityToNBT(final NBTTagCompound compound) {
        compound.setByte("Facing", (byte)this.facingDirection.getHorizontalIndex());
        final BlockPos blockpos = this.getHangingPosition();
        compound.setInteger("TileX", blockpos.getX());
        compound.setInteger("TileY", blockpos.getY());
        compound.setInteger("TileZ", blockpos.getZ());
    }
    
    public void readEntityFromNBT(final NBTTagCompound compound) {
        this.hangingPosition = new BlockPos(compound.getInteger("TileX"), compound.getInteger("TileY"), compound.getInteger("TileZ"));
        this.updateFacingWithBoundingBox(EnumFacing.byHorizontalIndex(compound.getByte("Facing")));
    }
    
    public abstract int getWidthPixels();
    
    public abstract int getHeightPixels();
    
    public abstract void onBroken(final Entity p0);
    
    public abstract void playPlaceSound();
    
    @Override
    public EntityItem entityDropItem(final ItemStack stack, final float offsetY) {
        final EntityItem entityitem = new EntityItem(this.world, this.posX + this.facingDirection.getXOffset() * 0.15f, this.posY + offsetY, this.posZ + this.facingDirection.getZOffset() * 0.15f, stack);
        entityitem.setDefaultPickupDelay();
        this.world.spawnEntity(entityitem);
        return entityitem;
    }
    
    @Override
    protected boolean shouldSetPosAfterLoading() {
        return false;
    }
    
    @Override
    public void setPosition(final double x, final double y, final double z) {
        this.hangingPosition = new BlockPos(x, y, z);
        this.updateBoundingBox();
        this.isAirBorne = true;
    }
    
    public BlockPos getHangingPosition() {
        return this.hangingPosition;
    }
    
    @Override
    public float getRotatedYaw(final Rotation transformRotation) {
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
                    break;
                }
            }
        }
        final float f = MathHelper.wrapDegrees(this.rotationYaw);
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
            default: {
                return f;
            }
        }
    }
    
    @Override
    public float getMirroredYaw(final Mirror transformMirror) {
        return this.getRotatedYaw(transformMirror.toRotation(this.facingDirection));
    }
    
    @Override
    public void onStruckByLightning(final EntityLightningBolt lightningBolt) {
    }
    
    static {
        IS_HANGING_ENTITY = (Predicate)new Predicate<Entity>() {
            public boolean apply(@Nullable final Entity p_apply_1_) {
                return p_apply_1_ instanceof EntityHanging;
            }
        };
    }
}
