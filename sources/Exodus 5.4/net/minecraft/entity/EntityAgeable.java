/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class EntityAgeable
extends EntityCreature {
    protected int growingAge;
    private float ageHeight;
    private float ageWidth = -1.0f;
    protected int field_175502_b;
    protected int field_175503_c;

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        super.readEntityFromNBT(nBTTagCompound);
        this.setGrowingAge(nBTTagCompound.getInteger("Age"));
        this.field_175502_b = nBTTagCompound.getInteger("ForcedAge");
    }

    @Override
    public boolean isChild() {
        return this.getGrowingAge() < 0;
    }

    public EntityAgeable(World world) {
        super(world);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.worldObj.isRemote) {
            if (this.field_175503_c > 0) {
                if (this.field_175503_c % 4 == 0) {
                    this.worldObj.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width, this.posY + 0.5 + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width, 0.0, 0.0, 0.0, new int[0]);
                }
                --this.field_175503_c;
            }
            this.setScaleForAge(this.isChild());
        } else {
            int n = this.getGrowingAge();
            if (n < 0) {
                this.setGrowingAge(++n);
                if (n == 0) {
                    this.onGrowingAdult();
                }
            } else if (n > 0) {
                this.setGrowingAge(--n);
            }
        }
    }

    public void func_175501_a(int n, boolean bl) {
        int n2;
        int n3 = n2 = this.getGrowingAge();
        if ((n2 += n * 20) > 0) {
            n2 = 0;
            if (n3 < 0) {
                this.onGrowingAdult();
            }
        }
        int n4 = n2 - n3;
        this.setGrowingAge(n2);
        if (bl) {
            this.field_175502_b += n4;
            if (this.field_175503_c == 0) {
                this.field_175503_c = 40;
            }
        }
        if (this.getGrowingAge() == 0) {
            this.setGrowingAge(this.field_175502_b);
        }
    }

    @Override
    protected final void setSize(float f, float f2) {
        boolean bl = this.ageWidth > 0.0f;
        this.ageWidth = f;
        this.ageHeight = f2;
        if (!bl) {
            this.setScale(1.0f);
        }
    }

    protected final void setScale(float f) {
        super.setSize(this.ageWidth * f, this.ageHeight * f);
    }

    public void addGrowth(int n) {
        this.func_175501_a(n, false);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(12, (byte)0);
    }

    protected void onGrowingAdult() {
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        nBTTagCompound.setInteger("Age", this.getGrowingAge());
        nBTTagCompound.setInteger("ForcedAge", this.field_175502_b);
    }

    @Override
    public boolean interact(EntityPlayer entityPlayer) {
        ItemStack itemStack = entityPlayer.inventory.getCurrentItem();
        if (itemStack != null && itemStack.getItem() == Items.spawn_egg) {
            EntityAgeable entityAgeable;
            Class<? extends Entity> clazz;
            if (!this.worldObj.isRemote && (clazz = EntityList.getClassFromID(itemStack.getMetadata())) != null && this.getClass() == clazz && (entityAgeable = this.createChild(this)) != null) {
                entityAgeable.setGrowingAge(-24000);
                entityAgeable.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0f, 0.0f);
                this.worldObj.spawnEntityInWorld(entityAgeable);
                if (itemStack.hasDisplayName()) {
                    entityAgeable.setCustomNameTag(itemStack.getDisplayName());
                }
                if (!entityPlayer.capabilities.isCreativeMode) {
                    --itemStack.stackSize;
                    if (itemStack.stackSize <= 0) {
                        entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, null);
                    }
                }
            }
            return true;
        }
        return false;
    }

    public void setScaleForAge(boolean bl) {
        this.setScale(bl ? 0.5f : 1.0f);
    }

    public void setGrowingAge(int n) {
        this.dataWatcher.updateObject(12, (byte)MathHelper.clamp_int(n, -1, 1));
        this.growingAge = n;
        this.setScaleForAge(this.isChild());
    }

    public abstract EntityAgeable createChild(EntityAgeable var1);

    public int getGrowingAge() {
        return this.worldObj.isRemote ? (int)this.dataWatcher.getWatchableObjectByte(12) : this.growingAge;
    }
}

