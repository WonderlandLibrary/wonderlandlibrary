// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity;

import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import javax.annotation.Nullable;
import net.minecraft.world.World;
import net.minecraft.network.datasync.DataParameter;

public abstract class EntityAgeable extends EntityCreature
{
    private static final DataParameter<Boolean> BABY;
    protected int growingAge;
    protected int forcedAge;
    protected int forcedAgeTimer;
    private float ageWidth;
    private float ageHeight;
    
    public EntityAgeable(final World worldIn) {
        super(worldIn);
        this.ageWidth = -1.0f;
    }
    
    @Nullable
    public abstract EntityAgeable createChild(final EntityAgeable p0);
    
    public boolean processInteract(final EntityPlayer player, final EnumHand hand) {
        final ItemStack itemstack = player.getHeldItem(hand);
        if (itemstack.getItem() == Items.SPAWN_EGG) {
            if (!this.world.isRemote) {
                final Class<? extends Entity> oclass = EntityList.REGISTRY.getObject(ItemMonsterPlacer.getNamedIdFrom(itemstack));
                if (oclass != null && this.getClass() == oclass) {
                    final EntityAgeable entityageable = this.createChild(this);
                    if (entityageable != null) {
                        entityageable.setGrowingAge(-24000);
                        entityageable.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0f, 0.0f);
                        this.world.spawnEntity(entityageable);
                        if (itemstack.hasDisplayName()) {
                            entityageable.setCustomNameTag(itemstack.getDisplayName());
                        }
                        if (!player.capabilities.isCreativeMode) {
                            itemstack.shrink(1);
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    protected boolean holdingSpawnEggOfClass(final ItemStack stack, final Class<? extends Entity> entityClass) {
        if (stack.getItem() != Items.SPAWN_EGG) {
            return false;
        }
        final Class<? extends Entity> oclass = EntityList.REGISTRY.getObject(ItemMonsterPlacer.getNamedIdFrom(stack));
        return oclass != null && entityClass == oclass;
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityAgeable.BABY, false);
    }
    
    public int getGrowingAge() {
        if (this.world.isRemote) {
            return this.dataManager.get(EntityAgeable.BABY) ? -1 : 1;
        }
        return this.growingAge;
    }
    
    public void ageUp(final int growthSeconds, final boolean updateForcedAge) {
        final int j;
        int i = j = this.getGrowingAge();
        i += growthSeconds * 20;
        if (i > 0) {
            i = 0;
            if (j < 0) {
                this.onGrowingAdult();
            }
        }
        final int k = i - j;
        this.setGrowingAge(i);
        if (updateForcedAge) {
            this.forcedAge += k;
            if (this.forcedAgeTimer == 0) {
                this.forcedAgeTimer = 40;
            }
        }
        if (this.getGrowingAge() == 0) {
            this.setGrowingAge(this.forcedAge);
        }
    }
    
    public void addGrowth(final int growth) {
        this.ageUp(growth, false);
    }
    
    public void setGrowingAge(final int age) {
        this.dataManager.set(EntityAgeable.BABY, age < 0);
        this.growingAge = age;
        this.setScaleForAge(this.isChild());
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Age", this.getGrowingAge());
        compound.setInteger("ForcedAge", this.forcedAge);
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setGrowingAge(compound.getInteger("Age"));
        this.forcedAge = compound.getInteger("ForcedAge");
    }
    
    @Override
    public void notifyDataManagerChange(final DataParameter<?> key) {
        if (EntityAgeable.BABY.equals(key)) {
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
                    this.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, this.posX + this.rand.nextFloat() * this.width * 2.0f - this.width, this.posY + 0.5 + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0f - this.width, 0.0, 0.0, 0.0, new int[0]);
                }
                --this.forcedAgeTimer;
            }
        }
        else {
            int i = this.getGrowingAge();
            if (i < 0) {
                ++i;
                this.setGrowingAge(i);
                if (i == 0) {
                    this.onGrowingAdult();
                }
            }
            else if (i > 0) {
                --i;
                this.setGrowingAge(i);
            }
        }
    }
    
    protected void onGrowingAdult() {
    }
    
    @Override
    public boolean isChild() {
        return this.getGrowingAge() < 0;
    }
    
    public void setScaleForAge(final boolean child) {
        this.setScale(child ? 0.5f : 1.0f);
    }
    
    @Override
    protected final void setSize(final float width, final float height) {
        final boolean flag = this.ageWidth > 0.0f;
        this.ageWidth = width;
        this.ageHeight = height;
        if (!flag) {
            this.setScale(1.0f);
        }
    }
    
    protected final void setScale(final float scale) {
        super.setSize(this.ageWidth * scale, this.ageHeight * scale);
    }
    
    static {
        BABY = EntityDataManager.createKey(EntityAgeable.class, DataSerializers.BOOLEAN);
    }
}
