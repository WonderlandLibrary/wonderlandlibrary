// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract class EntityAgeable extends EntityCreature
{
    protected int field_175504_a;
    protected int field_175502_b;
    protected int field_175503_c;
    private float field_98056_d;
    private float field_98057_e;
    private static final String __OBFID = "CL_00001530";
    
    public EntityAgeable(final World worldIn) {
        super(worldIn);
        this.field_98056_d = -1.0f;
    }
    
    public abstract EntityAgeable createChild(final EntityAgeable p0);
    
    public boolean interact(final EntityPlayer p_70085_1_) {
        final ItemStack var2 = p_70085_1_.inventory.getCurrentItem();
        if (var2 != null && var2.getItem() == Items.spawn_egg) {
            if (!this.worldObj.isRemote) {
                final Class var3 = EntityList.getClassFromID(var2.getMetadata());
                if (var3 != null && this.getClass() == var3) {
                    final EntityAgeable var4 = this.createChild(this);
                    if (var4 != null) {
                        var4.setGrowingAge(-24000);
                        var4.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0f, 0.0f);
                        this.worldObj.spawnEntityInWorld(var4);
                        if (var2.hasDisplayName()) {
                            var4.setCustomNameTag(var2.getDisplayName());
                        }
                        if (!p_70085_1_.capabilities.isCreativeMode) {
                            final ItemStack itemStack = var2;
                            --itemStack.stackSize;
                            if (var2.stackSize <= 0) {
                                p_70085_1_.inventory.setInventorySlotContents(p_70085_1_.inventory.currentItem, null);
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(12, (byte)0);
    }
    
    public int getGrowingAge() {
        return this.worldObj.isRemote ? this.dataWatcher.getWatchableObjectByte(12) : this.field_175504_a;
    }
    
    public void func_175501_a(final int p_175501_1_, final boolean p_175501_2_) {
        final int var4;
        int var3 = var4 = this.getGrowingAge();
        var3 += p_175501_1_ * 20;
        if (var3 > 0) {
            var3 = 0;
            if (var4 < 0) {
                this.func_175500_n();
            }
        }
        final int var5 = var3 - var4;
        this.setGrowingAge(var3);
        if (p_175501_2_) {
            this.field_175502_b += var5;
            if (this.field_175503_c == 0) {
                this.field_175503_c = 40;
            }
        }
        if (this.getGrowingAge() == 0) {
            this.setGrowingAge(this.field_175502_b);
        }
    }
    
    public void addGrowth(final int p_110195_1_) {
        this.func_175501_a(p_110195_1_, false);
    }
    
    public void setGrowingAge(final int p_70873_1_) {
        this.dataWatcher.updateObject(12, (byte)MathHelper.clamp_int(p_70873_1_, -1, 1));
        this.field_175504_a = p_70873_1_;
        this.setScaleForAge(this.isChild());
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("Age", this.getGrowingAge());
        tagCompound.setInteger("ForcedAge", this.field_175502_b);
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.setGrowingAge(tagCompund.getInteger("Age"));
        this.field_175502_b = tagCompund.getInteger("ForcedAge");
    }
    
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.worldObj.isRemote) {
            if (this.field_175503_c > 0) {
                if (this.field_175503_c % 4 == 0) {
                    this.worldObj.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, this.posX + this.rand.nextFloat() * this.width * 2.0f - this.width, this.posY + 0.5 + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0f - this.width, 0.0, 0.0, 0.0, new int[0]);
                }
                --this.field_175503_c;
            }
            this.setScaleForAge(this.isChild());
        }
        else {
            int var1 = this.getGrowingAge();
            if (var1 < 0) {
                ++var1;
                this.setGrowingAge(var1);
                if (var1 == 0) {
                    this.func_175500_n();
                }
            }
            else if (var1 > 0) {
                --var1;
                this.setGrowingAge(var1);
            }
        }
    }
    
    protected void func_175500_n() {
    }
    
    @Override
    public boolean isChild() {
        return this.getGrowingAge() < 0;
    }
    
    public void setScaleForAge(final boolean p_98054_1_) {
        this.setScale(p_98054_1_ ? 0.5f : 1.0f);
    }
    
    @Override
    protected final void setSize(final float width, final float height) {
        final boolean var3 = this.field_98056_d > 0.0f;
        this.field_98056_d = width;
        this.field_98057_e = height;
        if (!var3) {
            this.setScale(1.0f);
        }
    }
    
    protected final void setScale(final float p_98055_1_) {
        super.setSize(this.field_98056_d * p_98055_1_, this.field_98057_e * p_98055_1_);
    }
}
