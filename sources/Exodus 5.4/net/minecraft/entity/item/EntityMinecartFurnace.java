/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.item;

import net.minecraft.block.BlockFurnace;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityMinecartFurnace
extends EntityMinecart {
    private int fuel;
    public double pushX;
    public double pushZ;

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte(0));
    }

    @Override
    public IBlockState getDefaultDisplayTile() {
        return (this.isMinecartPowered() ? Blocks.lit_furnace : Blocks.furnace).getDefaultState().withProperty(BlockFurnace.FACING, EnumFacing.NORTH);
    }

    public EntityMinecartFurnace(World world) {
        super(world);
    }

    protected boolean isMinecartPowered() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    protected void setMinecartPowered(boolean bl) {
        if (bl) {
            this.dataWatcher.updateObject(16, (byte)(this.dataWatcher.getWatchableObjectByte(16) | 1));
        } else {
            this.dataWatcher.updateObject(16, (byte)(this.dataWatcher.getWatchableObjectByte(16) & 0xFFFFFFFE));
        }
    }

    @Override
    public void killMinecart(DamageSource damageSource) {
        super.killMinecart(damageSource);
        if (!damageSource.isExplosion() && this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
            this.entityDropItem(new ItemStack(Blocks.furnace, 1), 0.0f);
        }
    }

    public EntityMinecartFurnace(World world, double d, double d2, double d3) {
        super(world, d, d2, d3);
    }

    @Override
    protected double getMaximumSpeed() {
        return 0.2;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.fuel > 0) {
            --this.fuel;
        }
        if (this.fuel <= 0) {
            this.pushZ = 0.0;
            this.pushX = 0.0;
        }
        this.setMinecartPowered(this.fuel > 0);
        if (this.isMinecartPowered() && this.rand.nextInt(4) == 0) {
            this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX, this.posY + 0.8, this.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
    }

    @Override
    public boolean interactFirst(EntityPlayer entityPlayer) {
        ItemStack itemStack = entityPlayer.inventory.getCurrentItem();
        if (itemStack != null && itemStack.getItem() == Items.coal) {
            if (!entityPlayer.capabilities.isCreativeMode && --itemStack.stackSize == 0) {
                entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, null);
            }
            this.fuel += 3600;
        }
        this.pushX = this.posX - entityPlayer.posX;
        this.pushZ = this.posZ - entityPlayer.posZ;
        return true;
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        nBTTagCompound.setDouble("PushX", this.pushX);
        nBTTagCompound.setDouble("PushZ", this.pushZ);
        nBTTagCompound.setShort("Fuel", (short)this.fuel);
    }

    @Override
    public EntityMinecart.EnumMinecartType getMinecartType() {
        return EntityMinecart.EnumMinecartType.FURNACE;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        super.readEntityFromNBT(nBTTagCompound);
        this.pushX = nBTTagCompound.getDouble("PushX");
        this.pushZ = nBTTagCompound.getDouble("PushZ");
        this.fuel = nBTTagCompound.getShort("Fuel");
    }

    @Override
    protected void applyDrag() {
        double d = this.pushX * this.pushX + this.pushZ * this.pushZ;
        if (d > 1.0E-4) {
            d = MathHelper.sqrt_double(d);
            this.pushX /= d;
            this.pushZ /= d;
            double d2 = 1.0;
            this.motionX *= (double)0.8f;
            this.motionY *= 0.0;
            this.motionZ *= (double)0.8f;
            this.motionX += this.pushX * d2;
            this.motionZ += this.pushZ * d2;
        } else {
            this.motionX *= (double)0.98f;
            this.motionY *= 0.0;
            this.motionZ *= (double)0.98f;
        }
        super.applyDrag();
    }

    @Override
    protected void func_180460_a(BlockPos blockPos, IBlockState iBlockState) {
        super.func_180460_a(blockPos, iBlockState);
        double d = this.pushX * this.pushX + this.pushZ * this.pushZ;
        if (d > 1.0E-4 && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.001) {
            d = MathHelper.sqrt_double(d);
            this.pushX /= d;
            this.pushZ /= d;
            if (this.pushX * this.motionX + this.pushZ * this.motionZ < 0.0) {
                this.pushX = 0.0;
                this.pushZ = 0.0;
            } else {
                double d2 = d / this.getMaximumSpeed();
                this.pushX *= d2;
                this.pushZ *= d2;
            }
        }
    }
}

