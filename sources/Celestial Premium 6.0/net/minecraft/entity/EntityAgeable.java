/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public abstract class EntityAgeable
extends EntityCreature {
    private static final DataParameter<Boolean> BABY = EntityDataManager.createKey(EntityAgeable.class, DataSerializers.BOOLEAN);
    protected int growingAge;
    protected int forcedAge;
    protected int forcedAgeTimer;
    private float ageWidth = -1.0f;
    private float ageHeight;

    public EntityAgeable(World worldIn) {
        super(worldIn);
    }

    @Nullable
    public abstract EntityAgeable createChild(EntityAgeable var1);

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (itemstack.getItem() == Items.SPAWN_EGG) {
            EntityAgeable entityageable;
            Class<? extends Entity> oclass;
            if (!this.world.isRemote && (oclass = EntityList.REGISTRY.getObject(ItemMonsterPlacer.func_190908_h(itemstack))) != null && this.getClass() == oclass && (entityageable = this.createChild(this)) != null) {
                entityageable.setGrowingAge(-24000);
                entityageable.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0f, 0.0f);
                this.world.spawnEntityInWorld(entityageable);
                if (itemstack.hasDisplayName()) {
                    entityageable.setCustomNameTag(itemstack.getDisplayName());
                }
                if (!player.capabilities.isCreativeMode) {
                    itemstack.func_190918_g(1);
                }
            }
            return true;
        }
        return false;
    }

    protected boolean func_190669_a(ItemStack p_190669_1_, Class<? extends Entity> p_190669_2_) {
        if (p_190669_1_.getItem() != Items.SPAWN_EGG) {
            return false;
        }
        Class<? extends Entity> oclass = EntityList.REGISTRY.getObject(ItemMonsterPlacer.func_190908_h(p_190669_1_));
        return oclass != null && p_190669_2_ == oclass;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(BABY, false);
    }

    public int getGrowingAge() {
        if (this.world.isRemote) {
            return this.dataManager.get(BABY) != false ? -1 : 1;
        }
        return this.growingAge;
    }

    public void ageUp(int p_175501_1_, boolean p_175501_2_) {
        int i;
        int j = i = this.getGrowingAge();
        if ((i += p_175501_1_ * 20) > 0) {
            i = 0;
            if (j < 0) {
                this.onGrowingAdult();
            }
        }
        int k = i - j;
        this.setGrowingAge(i);
        if (p_175501_2_) {
            this.forcedAge += k;
            if (this.forcedAgeTimer == 0) {
                this.forcedAgeTimer = 40;
            }
        }
        if (this.getGrowingAge() == 0) {
            this.setGrowingAge(this.forcedAge);
        }
    }

    public void addGrowth(int growth) {
        this.ageUp(growth, false);
    }

    public void setGrowingAge(int age) {
        this.dataManager.set(BABY, age < 0);
        this.growingAge = age;
        this.setScaleForAge(this.isChild());
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Age", this.getGrowingAge());
        compound.setInteger("ForcedAge", this.forcedAge);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setGrowingAge(compound.getInteger("Age"));
        this.forcedAge = compound.getInteger("ForcedAge");
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        if (BABY.equals(key)) {
            this.setScaleForAge(this.isChild());
        }
        super.notifyDataManagerChange(key);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.world.isRemote) {
            if (this.forcedAgeTimer > 0) {
                if (this.forcedAgeTimer % 4 == 0) {
                    this.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width, this.posY + 0.5 + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width, 0.0, 0.0, 0.0, new int[0]);
                }
                --this.forcedAgeTimer;
            }
        } else {
            int i = this.getGrowingAge();
            if (i < 0) {
                this.setGrowingAge(++i);
                if (i == 0) {
                    this.onGrowingAdult();
                }
            } else if (i > 0) {
                this.setGrowingAge(--i);
            }
        }
    }

    protected void onGrowingAdult() {
    }

    @Override
    public boolean isChild() {
        return this.getGrowingAge() < 0;
    }

    public void setScaleForAge(boolean child) {
        this.setScale(child ? 0.5f : 1.0f);
    }

    @Override
    protected final void setSize(float width, float height) {
        boolean flag = this.ageWidth > 0.0f;
        this.ageWidth = width;
        this.ageHeight = height;
        if (!flag) {
            this.setScale(1.0f);
        }
    }

    protected final void setScale(float scale) {
        super.setSize(this.ageWidth * scale, this.ageHeight * scale);
    }
}

