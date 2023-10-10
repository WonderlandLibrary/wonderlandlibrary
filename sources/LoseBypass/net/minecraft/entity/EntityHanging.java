/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity;

import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.apache.commons.lang3.Validate;

public abstract class EntityHanging
extends Entity {
    private int tickCounter1;
    protected BlockPos hangingPosition;
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

    private void updateBoundingBox() {
        if (this.facingDirection == null) return;
        double d0 = (double)this.hangingPosition.getX() + 0.5;
        double d1 = (double)this.hangingPosition.getY() + 0.5;
        double d2 = (double)this.hangingPosition.getZ() + 0.5;
        double d3 = 0.46875;
        double d4 = this.func_174858_a(this.getWidthPixels());
        double d5 = this.func_174858_a(this.getHeightPixels());
        d0 -= (double)this.facingDirection.getFrontOffsetX() * 0.46875;
        d2 -= (double)this.facingDirection.getFrontOffsetZ() * 0.46875;
        EnumFacing enumfacing = this.facingDirection.rotateYCCW();
        this.posX = d0 += d4 * (double)enumfacing.getFrontOffsetX();
        this.posY = d1 += d5;
        this.posZ = d2 += d4 * (double)enumfacing.getFrontOffsetZ();
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

    private double func_174858_a(int p_174858_1_) {
        if (p_174858_1_ % 32 != 0) return 0.0;
        return 0.5;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.tickCounter1++ != 100) return;
        if (this.worldObj.isRemote) return;
        this.tickCounter1 = 0;
        if (this.isDead) return;
        if (this.onValidSurface()) return;
        this.setDead();
        this.onBroken(null);
    }

    public boolean onValidSurface() {
        Entity entity;
        if (!this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty()) {
            return false;
        }
        int i = Math.max(1, this.getWidthPixels() / 16);
        int j = Math.max(1, this.getHeightPixels() / 16);
        BlockPos blockpos = this.hangingPosition.offset(this.facingDirection.getOpposite());
        EnumFacing enumfacing = this.facingDirection.rotateYCCW();
        for (int k = 0; k < i; ++k) {
            for (int l = 0; l < j; ++l) {
                BlockPos blockpos1 = blockpos.offset(enumfacing, k).up(l);
                Block block = this.worldObj.getBlockState(blockpos1).getBlock();
                if (block.getMaterial().isSolid() || BlockRedstoneDiode.isRedstoneRepeaterBlockID(block)) continue;
                return false;
            }
        }
        Iterator<Entity> iterator = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox()).iterator();
        do {
            if (!iterator.hasNext()) return true;
        } while (!((entity = iterator.next()) instanceof EntityHanging));
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean hitByEntity(Entity entityIn) {
        if (!(entityIn instanceof EntityPlayer)) return false;
        boolean bl = this.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)entityIn), 0.0f);
        return bl;
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
        if (this.isDead) return true;
        if (this.worldObj.isRemote) return true;
        this.setDead();
        this.setBeenAttacked();
        this.onBroken(source.getEntity());
        return true;
    }

    @Override
    public void moveEntity(double x, double y, double z) {
        if (this.worldObj.isRemote) return;
        if (this.isDead) return;
        if (!(x * x + y * y + z * z > 0.0)) return;
        this.setDead();
        this.onBroken(null);
    }

    @Override
    public void addVelocity(double x, double y, double z) {
        if (this.worldObj.isRemote) return;
        if (this.isDead) return;
        if (!(x * x + y * y + z * z > 0.0)) return;
        this.setDead();
        this.onBroken(null);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        tagCompound.setByte("Facing", (byte)this.facingDirection.getHorizontalIndex());
        tagCompound.setInteger("TileX", this.getHangingPosition().getX());
        tagCompound.setInteger("TileY", this.getHangingPosition().getY());
        tagCompound.setInteger("TileZ", this.getHangingPosition().getZ());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        EnumFacing enumfacing;
        this.hangingPosition = new BlockPos(tagCompund.getInteger("TileX"), tagCompund.getInteger("TileY"), tagCompund.getInteger("TileZ"));
        if (tagCompund.hasKey("Direction", 99)) {
            enumfacing = EnumFacing.getHorizontal(tagCompund.getByte("Direction"));
            this.hangingPosition = this.hangingPosition.offset(enumfacing);
        } else {
            enumfacing = tagCompund.hasKey("Facing", 99) ? EnumFacing.getHorizontal(tagCompund.getByte("Facing")) : EnumFacing.getHorizontal(tagCompund.getByte("Dir"));
        }
        this.updateFacingWithBoundingBox(enumfacing);
    }

    public abstract int getWidthPixels();

    public abstract int getHeightPixels();

    public abstract void onBroken(Entity var1);

    @Override
    protected boolean shouldSetPosAfterLoading() {
        return false;
    }

    @Override
    public void setPosition(double x, double y, double z) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        BlockPos blockpos = this.hangingPosition;
        this.hangingPosition = new BlockPos(x, y, z);
        if (this.hangingPosition.equals(blockpos)) return;
        this.updateBoundingBox();
        this.isAirBorne = true;
    }

    public BlockPos getHangingPosition() {
        return this.hangingPosition;
    }
}

