// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.passive;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.util.StatCollector;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import com.google.common.base.Predicate;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIOcelotAttack;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.EntityCreature;
import net.minecraft.init.Items;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.world.World;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIAvoidEntity;

public class EntityOcelot extends EntityTameable
{
    private EntityAIAvoidEntity field_175545_bm;
    private EntityAITempt aiTempt;
    private static final String __OBFID = "CL_00001646";
    
    public EntityOcelot(final World worldIn) {
        super(worldIn);
        this.setSize(0.6f, 0.7f);
        ((PathNavigateGround)this.getNavigator()).func_179690_a(true);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(3, this.aiTempt = new EntityAITempt(this, 0.6, Items.fish, true));
        this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.0, 10.0f, 5.0f));
        this.tasks.addTask(6, new EntityAIOcelotSit(this, 0.8));
        this.tasks.addTask(7, new EntityAILeapAtTarget(this, 0.3f));
        this.tasks.addTask(8, new EntityAIOcelotAttack(this));
        this.tasks.addTask(9, new EntityAIMate(this, 0.8));
        this.tasks.addTask(10, new EntityAIWander(this, 0.8));
        this.tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0f));
        this.targetTasks.addTask(1, new EntityAITargetNonTamed(this, EntityChicken.class, false, null));
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(18, (byte)0);
    }
    
    public void updateAITasks() {
        if (this.getMoveHelper().isUpdating()) {
            final double var1 = this.getMoveHelper().getSpeed();
            if (var1 == 0.6) {
                this.setSneaking(true);
                this.setSprinting(false);
            }
            else if (var1 == 1.33) {
                this.setSneaking(false);
                this.setSprinting(true);
            }
            else {
                this.setSneaking(false);
                this.setSprinting(false);
            }
        }
        else {
            this.setSneaking(false);
            this.setSprinting(false);
        }
    }
    
    @Override
    protected boolean canDespawn() {
        return !this.isTamed() && this.ticksExisted > 2400;
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896);
    }
    
    @Override
    public void fall(final float distance, final float damageMultiplier) {
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("CatType", this.getTameSkin());
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.setTameSkin(tagCompund.getInteger("CatType"));
    }
    
    @Override
    protected String getLivingSound() {
        return this.isTamed() ? (this.isInLove() ? "mob.cat.purr" : ((this.rand.nextInt(4) == 0) ? "mob.cat.purreow" : "mob.cat.meow")) : "";
    }
    
    @Override
    protected String getHurtSound() {
        return "mob.cat.hitt";
    }
    
    @Override
    protected String getDeathSound() {
        return "mob.cat.hitt";
    }
    
    @Override
    protected float getSoundVolume() {
        return 0.4f;
    }
    
    @Override
    protected Item getDropItem() {
        return Items.leather;
    }
    
    @Override
    public boolean attackEntityAsMob(final Entity p_70652_1_) {
        return p_70652_1_.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0f);
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.func_180431_b(source)) {
            return false;
        }
        this.aiSit.setSitting(false);
        return super.attackEntityFrom(source, amount);
    }
    
    @Override
    protected void dropFewItems(final boolean p_70628_1_, final int p_70628_2_) {
    }
    
    @Override
    public boolean interact(final EntityPlayer p_70085_1_) {
        final ItemStack var2 = p_70085_1_.inventory.getCurrentItem();
        if (this.isTamed()) {
            if (this.func_152114_e(p_70085_1_) && !this.worldObj.isRemote && !this.isBreedingItem(var2)) {
                this.aiSit.setSitting(!this.isSitting());
            }
        }
        else if (this.aiTempt.isRunning() && var2 != null && var2.getItem() == Items.fish && p_70085_1_.getDistanceSqToEntity(this) < 9.0) {
            if (!p_70085_1_.capabilities.isCreativeMode) {
                final ItemStack itemStack = var2;
                --itemStack.stackSize;
            }
            if (var2.stackSize <= 0) {
                p_70085_1_.inventory.setInventorySlotContents(p_70085_1_.inventory.currentItem, null);
            }
            if (!this.worldObj.isRemote) {
                if (this.rand.nextInt(3) == 0) {
                    this.setTamed(true);
                    this.setTameSkin(1 + this.worldObj.rand.nextInt(3));
                    this.func_152115_b(p_70085_1_.getUniqueID().toString());
                    this.playTameEffect(true);
                    this.aiSit.setSitting(true);
                    this.worldObj.setEntityState(this, (byte)7);
                }
                else {
                    this.playTameEffect(false);
                    this.worldObj.setEntityState(this, (byte)6);
                }
            }
            return true;
        }
        return super.interact(p_70085_1_);
    }
    
    public EntityOcelot func_180493_b(final EntityAgeable p_180493_1_) {
        final EntityOcelot var2 = new EntityOcelot(this.worldObj);
        if (this.isTamed()) {
            var2.func_152115_b(this.func_152113_b());
            var2.setTamed(true);
            var2.setTameSkin(this.getTameSkin());
        }
        return var2;
    }
    
    @Override
    public boolean isBreedingItem(final ItemStack p_70877_1_) {
        return p_70877_1_ != null && p_70877_1_.getItem() == Items.fish;
    }
    
    @Override
    public boolean canMateWith(final EntityAnimal p_70878_1_) {
        if (p_70878_1_ == this) {
            return false;
        }
        if (!this.isTamed()) {
            return false;
        }
        if (!(p_70878_1_ instanceof EntityOcelot)) {
            return false;
        }
        final EntityOcelot var2 = (EntityOcelot)p_70878_1_;
        return var2.isTamed() && (this.isInLove() && var2.isInLove());
    }
    
    public int getTameSkin() {
        return this.dataWatcher.getWatchableObjectByte(18);
    }
    
    public void setTameSkin(final int p_70912_1_) {
        this.dataWatcher.updateObject(18, (byte)p_70912_1_);
    }
    
    @Override
    public boolean getCanSpawnHere() {
        return this.worldObj.rand.nextInt(3) != 0;
    }
    
    @Override
    public boolean handleLavaMovement() {
        if (this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(this.getEntityBoundingBox())) {
            final BlockPos var1 = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
            if (var1.getY() < 63) {
                return false;
            }
            final Block var2 = this.worldObj.getBlockState(var1.offsetDown()).getBlock();
            if (var2 == Blocks.grass || var2.getMaterial() == Material.leaves) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String getName() {
        return this.hasCustomName() ? this.getCustomNameTag() : (this.isTamed() ? StatCollector.translateToLocal("entity.Cat.name") : super.getName());
    }
    
    @Override
    public void setTamed(final boolean p_70903_1_) {
        super.setTamed(p_70903_1_);
    }
    
    @Override
    protected void func_175544_ck() {
        if (this.field_175545_bm == null) {
            this.field_175545_bm = new EntityAIAvoidEntity(this, (Predicate)new Predicate() {
                private static final String __OBFID = "CL_00002243";
                
                public boolean func_179874_a(final Entity p_179874_1_) {
                    return p_179874_1_ instanceof EntityPlayer;
                }
                
                public boolean apply(final Object p_apply_1_) {
                    return this.func_179874_a((Entity)p_apply_1_);
                }
            }, 16.0f, 0.8, 1.33);
        }
        this.tasks.removeTask(this.field_175545_bm);
        if (!this.isTamed()) {
            this.tasks.addTask(4, this.field_175545_bm);
        }
    }
    
    @Override
    public IEntityLivingData func_180482_a(final DifficultyInstance p_180482_1_, IEntityLivingData p_180482_2_) {
        p_180482_2_ = super.func_180482_a(p_180482_1_, p_180482_2_);
        if (this.worldObj.rand.nextInt(7) == 0) {
            for (int var3 = 0; var3 < 2; ++var3) {
                final EntityOcelot var4 = new EntityOcelot(this.worldObj);
                var4.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0f);
                var4.setGrowingAge(-24000);
                this.worldObj.spawnEntityInWorld(var4);
            }
        }
        return p_180482_2_;
    }
    
    @Override
    public EntityAgeable createChild(final EntityAgeable p_90011_1_) {
        return this.func_180493_b(p_90011_1_);
    }
}
