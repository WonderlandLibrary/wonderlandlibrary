/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.Validate
 */
package net.minecraft.entity;

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
    public EnumFacing facingDirection;
    private int tickCounter1;
    protected BlockPos hangingPosition;

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        if (this.isEntityInvulnerable(damageSource)) {
            return false;
        }
        if (!this.isDead && !this.worldObj.isRemote) {
            this.setDead();
            this.setBeenAttacked();
            this.onBroken(damageSource.getEntity());
        }
        return true;
    }

    @Override
    public boolean hitByEntity(Entity entity) {
        return entity instanceof EntityPlayer ? this.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)entity), 0.0f) : false;
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public EnumFacing getHorizontalFacing() {
        return this.facingDirection;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.tickCounter1++ == 100 && !this.worldObj.isRemote) {
            this.tickCounter1 = 0;
            if (!this.isDead && !this.onValidSurface()) {
                this.setDead();
                this.onBroken(null);
            }
        }
    }

    @Override
    protected boolean shouldSetPosAfterLoading() {
        return false;
    }

    public BlockPos getHangingPosition() {
        return this.hangingPosition;
    }

    @Override
    public void moveEntity(double d, double d2, double d3) {
        if (!this.worldObj.isRemote && !this.isDead && d * d + d2 * d2 + d3 * d3 > 0.0) {
            this.setDead();
            this.onBroken(null);
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    public abstract void onBroken(Entity var1);

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        nBTTagCompound.setByte("Facing", (byte)this.facingDirection.getHorizontalIndex());
        nBTTagCompound.setInteger("TileX", this.getHangingPosition().getX());
        nBTTagCompound.setInteger("TileY", this.getHangingPosition().getY());
        nBTTagCompound.setInteger("TileZ", this.getHangingPosition().getZ());
    }

    public abstract int getHeightPixels();

    public EntityHanging(World world, BlockPos blockPos) {
        this(world);
        this.hangingPosition = blockPos;
    }

    private void updateBoundingBox() {
        if (this.facingDirection != null) {
            double d = (double)this.hangingPosition.getX() + 0.5;
            double d2 = (double)this.hangingPosition.getY() + 0.5;
            double d3 = (double)this.hangingPosition.getZ() + 0.5;
            double d4 = 0.46875;
            double d5 = this.func_174858_a(this.getWidthPixels());
            double d6 = this.func_174858_a(this.getHeightPixels());
            d -= (double)this.facingDirection.getFrontOffsetX() * 0.46875;
            d3 -= (double)this.facingDirection.getFrontOffsetZ() * 0.46875;
            EnumFacing enumFacing = this.facingDirection.rotateYCCW();
            this.posX = d += d5 * (double)enumFacing.getFrontOffsetX();
            this.posY = d2 += d6;
            this.posZ = d3 += d5 * (double)enumFacing.getFrontOffsetZ();
            double d7 = this.getWidthPixels();
            double d8 = this.getHeightPixels();
            double d9 = this.getWidthPixels();
            if (this.facingDirection.getAxis() == EnumFacing.Axis.Z) {
                d9 = 1.0;
            } else {
                d7 = 1.0;
            }
            this.setEntityBoundingBox(new AxisAlignedBB(d - (d7 /= 32.0), d2 - (d8 /= 32.0), d3 - (d9 /= 32.0), d + d7, d2 + d8, d3 + d9));
        }
    }

    public boolean onValidSurface() {
        if (!this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty()) {
            return false;
        }
        int n = Math.max(1, this.getWidthPixels() / 16);
        int n2 = Math.max(1, this.getHeightPixels() / 16);
        BlockPos blockPos = this.hangingPosition.offset(this.facingDirection.getOpposite());
        EnumFacing enumFacing = this.facingDirection.rotateYCCW();
        int n3 = 0;
        while (n3 < n) {
            int n4 = 0;
            while (n4 < n2) {
                BlockPos blockPos2 = blockPos.offset(enumFacing, n3).up(n4);
                Block block = this.worldObj.getBlockState(blockPos2).getBlock();
                if (!block.getMaterial().isSolid() && !BlockRedstoneDiode.isRedstoneRepeaterBlockID(block)) {
                    return false;
                }
                ++n4;
            }
            ++n3;
        }
        for (Entity entity : this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox())) {
            if (!(entity instanceof EntityHanging)) continue;
            return false;
        }
        return true;
    }

    @Override
    public void setPosition(double d, double d2, double d3) {
        this.posX = d;
        this.posY = d2;
        this.posZ = d3;
        BlockPos blockPos = this.hangingPosition;
        this.hangingPosition = new BlockPos(d, d2, d3);
        if (!this.hangingPosition.equals(blockPos)) {
            this.updateBoundingBox();
            this.isAirBorne = true;
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        EnumFacing enumFacing;
        this.hangingPosition = new BlockPos(nBTTagCompound.getInteger("TileX"), nBTTagCompound.getInteger("TileY"), nBTTagCompound.getInteger("TileZ"));
        if (nBTTagCompound.hasKey("Direction", 99)) {
            enumFacing = EnumFacing.getHorizontal(nBTTagCompound.getByte("Direction"));
            this.hangingPosition = this.hangingPosition.offset(enumFacing);
        } else {
            enumFacing = nBTTagCompound.hasKey("Facing", 99) ? EnumFacing.getHorizontal(nBTTagCompound.getByte("Facing")) : EnumFacing.getHorizontal(nBTTagCompound.getByte("Dir"));
        }
        this.updateFacingWithBoundingBox(enumFacing);
    }

    private double func_174858_a(int n) {
        return n % 32 == 0 ? 0.5 : 0.0;
    }

    @Override
    public void addVelocity(double d, double d2, double d3) {
        if (!this.worldObj.isRemote && !this.isDead && d * d + d2 * d2 + d3 * d3 > 0.0) {
            this.setDead();
            this.onBroken(null);
        }
    }

    public EntityHanging(World world) {
        super(world);
        this.setSize(0.5f, 0.5f);
    }

    public abstract int getWidthPixels();

    protected void updateFacingWithBoundingBox(EnumFacing enumFacing) {
        Validate.notNull((Object)enumFacing);
        Validate.isTrue((boolean)enumFacing.getAxis().isHorizontal());
        this.facingDirection = enumFacing;
        this.prevRotationYaw = this.rotationYaw = (float)(this.facingDirection.getHorizontalIndex() * 90);
        this.updateBoundingBox();
    }
}

