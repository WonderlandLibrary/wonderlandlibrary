// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.item;

import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.BlockFurnace;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.init.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityMinecartFurnace extends EntityMinecart
{
    private int fuel;
    public double pushX;
    public double pushZ;
    private static final String __OBFID = "CL_00001675";
    
    public EntityMinecartFurnace(final World worldIn) {
        super(worldIn);
    }
    
    public EntityMinecartFurnace(final World worldIn, final double p_i1719_2_, final double p_i1719_4_, final double p_i1719_6_) {
        super(worldIn, p_i1719_2_, p_i1719_4_, p_i1719_6_);
    }
    
    @Override
    public EnumMinecartType func_180456_s() {
        return EnumMinecartType.FURNACE;
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte)0));
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.fuel > 0) {
            --this.fuel;
        }
        if (this.fuel <= 0) {
            final double n = 0.0;
            this.pushZ = n;
            this.pushX = n;
        }
        this.setMinecartPowered(this.fuel > 0);
        if (this.isMinecartPowered() && this.rand.nextInt(4) == 0) {
            this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX, this.posY + 0.8, this.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
    }
    
    @Override
    protected double func_174898_m() {
        return 0.2;
    }
    
    @Override
    public void killMinecart(final DamageSource p_94095_1_) {
        super.killMinecart(p_94095_1_);
        if (!p_94095_1_.isExplosion()) {
            this.entityDropItem(new ItemStack(Blocks.furnace, 1), 0.0f);
        }
    }
    
    @Override
    protected void func_180460_a(final BlockPos p_180460_1_, final IBlockState p_180460_2_) {
        super.func_180460_a(p_180460_1_, p_180460_2_);
        double var3 = this.pushX * this.pushX + this.pushZ * this.pushZ;
        if (var3 > 1.0E-4 && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.001) {
            var3 = MathHelper.sqrt_double(var3);
            this.pushX /= var3;
            this.pushZ /= var3;
            if (this.pushX * this.motionX + this.pushZ * this.motionZ < 0.0) {
                this.pushX = 0.0;
                this.pushZ = 0.0;
            }
            else {
                final double var4 = var3 / this.func_174898_m();
                this.pushX *= var4;
                this.pushZ *= var4;
            }
        }
    }
    
    @Override
    protected void applyDrag() {
        double var1 = this.pushX * this.pushX + this.pushZ * this.pushZ;
        if (var1 > 1.0E-4) {
            var1 = MathHelper.sqrt_double(var1);
            this.pushX /= var1;
            this.pushZ /= var1;
            final double var2 = 1.0;
            this.motionX *= 0.800000011920929;
            this.motionY *= 0.0;
            this.motionZ *= 0.800000011920929;
            this.motionX += this.pushX * var2;
            this.motionZ += this.pushZ * var2;
        }
        else {
            this.motionX *= 0.9800000190734863;
            this.motionY *= 0.0;
            this.motionZ *= 0.9800000190734863;
        }
        super.applyDrag();
    }
    
    @Override
    public boolean interactFirst(final EntityPlayer playerIn) {
        final ItemStack var2 = playerIn.inventory.getCurrentItem();
        if (var2 != null && var2.getItem() == Items.coal) {
            if (!playerIn.capabilities.isCreativeMode) {
                final ItemStack itemStack = var2;
                if (--itemStack.stackSize == 0) {
                    playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, null);
                }
            }
            this.fuel += 3600;
        }
        this.pushX = this.posX - playerIn.posX;
        this.pushZ = this.posZ - playerIn.posZ;
        return true;
    }
    
    @Override
    protected void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setDouble("PushX", this.pushX);
        tagCompound.setDouble("PushZ", this.pushZ);
        tagCompound.setShort("Fuel", (short)this.fuel);
    }
    
    @Override
    protected void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.pushX = tagCompund.getDouble("PushX");
        this.pushZ = tagCompund.getDouble("PushZ");
        this.fuel = tagCompund.getShort("Fuel");
    }
    
    protected boolean isMinecartPowered() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 0x1) != 0x0;
    }
    
    protected void setMinecartPowered(final boolean p_94107_1_) {
        if (p_94107_1_) {
            this.dataWatcher.updateObject(16, (byte)(this.dataWatcher.getWatchableObjectByte(16) | 0x1));
        }
        else {
            this.dataWatcher.updateObject(16, (byte)(this.dataWatcher.getWatchableObjectByte(16) & 0xFFFFFFFE));
        }
    }
    
    @Override
    public IBlockState func_180457_u() {
        return (this.isMinecartPowered() ? Blocks.lit_furnace : Blocks.furnace).getDefaultState().withProperty(BlockFurnace.FACING, EnumFacing.NORTH);
    }
}
