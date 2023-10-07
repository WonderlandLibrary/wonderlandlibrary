// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.util.AxisAlignedBB;
import org.apache.commons.lang3.Validate;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;

public abstract class EntityHanging extends Entity
{
    private int tickCounter1;
    protected BlockPos field_174861_a;
    public EnumFacing field_174860_b;
    private static final String __OBFID = "CL_00001546";
    
    public EntityHanging(final World worldIn) {
        super(worldIn);
        this.setSize(0.5f, 0.5f);
    }
    
    public EntityHanging(final World worldIn, final BlockPos p_i45853_2_) {
        this(worldIn);
        this.field_174861_a = p_i45853_2_;
    }
    
    @Override
    protected void entityInit() {
    }
    
    protected void func_174859_a(final EnumFacing p_174859_1_) {
        Validate.notNull((Object)p_174859_1_);
        Validate.isTrue(p_174859_1_.getAxis().isHorizontal());
        this.field_174860_b = p_174859_1_;
        final float n = this.field_174860_b.getHorizontalIndex() * 90;
        this.rotationYaw = n;
        this.prevRotationYaw = n;
        this.func_174856_o();
    }
    
    private void func_174856_o() {
        if (this.field_174860_b != null) {
            double var1 = this.field_174861_a.getX() + 0.5;
            double var2 = this.field_174861_a.getY() + 0.5;
            double var3 = this.field_174861_a.getZ() + 0.5;
            final double var4 = 0.46875;
            final double var5 = this.func_174858_a(this.getWidthPixels());
            final double var6 = this.func_174858_a(this.getHeightPixels());
            var1 -= this.field_174860_b.getFrontOffsetX() * 0.46875;
            var3 -= this.field_174860_b.getFrontOffsetZ() * 0.46875;
            var2 += var6;
            final EnumFacing var7 = this.field_174860_b.rotateYCCW();
            var1 += var5 * var7.getFrontOffsetX();
            var3 += var5 * var7.getFrontOffsetZ();
            this.posX = var1;
            this.posY = var2;
            this.posZ = var3;
            double var8 = this.getWidthPixels();
            double var9 = this.getHeightPixels();
            double var10 = this.getWidthPixels();
            if (this.field_174860_b.getAxis() == EnumFacing.Axis.Z) {
                var10 = 1.0;
            }
            else {
                var8 = 1.0;
            }
            var8 /= 32.0;
            var9 /= 32.0;
            var10 /= 32.0;
            this.func_174826_a(new AxisAlignedBB(var1 - var8, var2 - var9, var3 - var10, var1 + var8, var2 + var9, var3 + var10));
        }
    }
    
    private double func_174858_a(final int p_174858_1_) {
        return (p_174858_1_ % 32 == 0) ? 0.5 : 0.0;
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
    
    public boolean onValidSurface() {
        if (!this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty()) {
            return false;
        }
        final int var1 = Math.max(1, this.getWidthPixels() / 16);
        final int var2 = Math.max(1, this.getHeightPixels() / 16);
        final BlockPos var3 = this.field_174861_a.offset(this.field_174860_b.getOpposite());
        final EnumFacing var4 = this.field_174860_b.rotateYCCW();
        for (int var5 = 0; var5 < var1; ++var5) {
            for (int var6 = 0; var6 < var2; ++var6) {
                final BlockPos var7 = var3.offset(var4, var5).offsetUp(var6);
                final Block var8 = this.worldObj.getBlockState(var7).getBlock();
                if (!var8.getMaterial().isSolid() && !BlockRedstoneDiode.isRedstoneRepeaterBlockID(var8)) {
                    return false;
                }
            }
        }
        final List var9 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox());
        for (final Entity var11 : var9) {
            if (var11 instanceof EntityHanging) {
                return false;
            }
        }
        return true;
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
    public EnumFacing func_174811_aO() {
        return this.field_174860_b;
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.func_180431_b(source)) {
            return false;
        }
        if (!this.isDead && !this.worldObj.isRemote) {
            this.setDead();
            this.setBeenAttacked();
            this.onBroken(source.getEntity());
        }
        return true;
    }
    
    @Override
    public void moveEntity(final double x, final double y, final double z) {
        if (!this.worldObj.isRemote && !this.isDead && x * x + y * y + z * z > 0.0) {
            this.setDead();
            this.onBroken(null);
        }
    }
    
    @Override
    public void addVelocity(final double x, final double y, final double z) {
        if (!this.worldObj.isRemote && !this.isDead && x * x + y * y + z * z > 0.0) {
            this.setDead();
            this.onBroken(null);
        }
    }
    
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        tagCompound.setByte("Facing", (byte)this.field_174860_b.getHorizontalIndex());
        tagCompound.setInteger("TileX", this.func_174857_n().getX());
        tagCompound.setInteger("TileY", this.func_174857_n().getY());
        tagCompound.setInteger("TileZ", this.func_174857_n().getZ());
    }
    
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        this.field_174861_a = new BlockPos(tagCompund.getInteger("TileX"), tagCompund.getInteger("TileY"), tagCompund.getInteger("TileZ"));
        EnumFacing var2;
        if (tagCompund.hasKey("Direction", 99)) {
            var2 = EnumFacing.getHorizontal(tagCompund.getByte("Direction"));
            this.field_174861_a = this.field_174861_a.offset(var2);
        }
        else if (tagCompund.hasKey("Facing", 99)) {
            var2 = EnumFacing.getHorizontal(tagCompund.getByte("Facing"));
        }
        else {
            var2 = EnumFacing.getHorizontal(tagCompund.getByte("Dir"));
        }
        this.func_174859_a(var2);
    }
    
    public abstract int getWidthPixels();
    
    public abstract int getHeightPixels();
    
    public abstract void onBroken(final Entity p0);
    
    @Override
    protected boolean shouldSetPosAfterLoading() {
        return false;
    }
    
    @Override
    public void setPosition(final double x, final double y, final double z) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        final BlockPos var7 = this.field_174861_a;
        this.field_174861_a = new BlockPos(x, y, z);
        if (!this.field_174861_a.equals(var7)) {
            this.func_174856_o();
            this.isAirBorne = true;
        }
    }
    
    public BlockPos func_174857_n() {
        return this.field_174861_a;
    }
}
