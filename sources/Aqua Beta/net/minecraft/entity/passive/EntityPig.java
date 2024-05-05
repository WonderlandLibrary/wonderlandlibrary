package net.minecraft.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIControlledByPlayer;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityPig
extends EntityAnimal {
    private final EntityAIControlledByPlayer aiControlledByPlayer;

    public EntityPig(World worldIn) {
        super(worldIn);
        this.setSize(0.9f, 0.9f);
        ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
        this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
        this.tasks.addTask(1, (EntityAIBase)new EntityAIPanic((EntityCreature)this, 1.25));
        this.aiControlledByPlayer = new EntityAIControlledByPlayer((EntityLiving)this, 0.3f);
        this.tasks.addTask(2, (EntityAIBase)this.aiControlledByPlayer);
        this.tasks.addTask(3, (EntityAIBase)new EntityAIMate((EntityAnimal)this, 1.0));
        this.tasks.addTask(4, (EntityAIBase)new EntityAITempt((EntityCreature)this, 1.2, Items.carrot_on_a_stick, false));
        this.tasks.addTask(4, (EntityAIBase)new EntityAITempt((EntityCreature)this, 1.2, Items.carrot, false));
        this.tasks.addTask(5, (EntityAIBase)new EntityAIFollowParent((EntityAnimal)this, 1.1));
        this.tasks.addTask(6, (EntityAIBase)new EntityAIWander((EntityCreature)this, 1.0));
        this.tasks.addTask(7, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 6.0f));
        this.tasks.addTask(8, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25);
    }

    public boolean canBeSteered() {
        ItemStack itemstack = ((EntityPlayer)this.riddenByEntity).getHeldItem();
        return itemstack != null && itemstack.getItem() == Items.carrot_on_a_stick;
    }

    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, (Object)0);
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setBoolean("Saddle", this.getSaddled());
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.setSaddled(tagCompund.getBoolean("Saddle"));
    }

    protected String getLivingSound() {
        return "mob.pig.say";
    }

    protected String getHurtSound() {
        return "mob.pig.say";
    }

    protected String getDeathSound() {
        return "mob.pig.death";
    }

    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound("mob.pig.step", 0.15f, 1.0f);
    }

    public boolean interact(EntityPlayer player) {
        if (super.interact(player)) {
            return true;
        }
        if (!this.getSaddled() || this.worldObj.isRemote || this.riddenByEntity != null && this.riddenByEntity != player) {
            return false;
        }
        player.mountEntity((Entity)this);
        return true;
    }

    protected Item getDropItem() {
        return this.isBurning() ? Items.cooked_porkchop : Items.porkchop;
    }

    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
        int i = this.rand.nextInt(3) + 1 + this.rand.nextInt(1 + lootingModifier);
        for (int j = 0; j < i; ++j) {
            if (this.isBurning()) {
                this.dropItem(Items.cooked_porkchop, 1);
                continue;
            }
            this.dropItem(Items.porkchop, 1);
        }
        if (this.getSaddled()) {
            this.dropItem(Items.saddle, 1);
        }
    }

    public boolean getSaddled() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    public void setSaddled(boolean saddled) {
        if (saddled) {
            this.dataWatcher.updateObject(16, (Object)1);
        } else {
            this.dataWatcher.updateObject(16, (Object)0);
        }
    }

    public void onStruckByLightning(EntityLightningBolt lightningBolt) {
        if (!this.worldObj.isRemote && !this.isDead) {
            EntityPigZombie entitypigzombie = new EntityPigZombie(this.worldObj);
            entitypigzombie.setCurrentItemOrArmor(0, new ItemStack(Items.golden_sword));
            entitypigzombie.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            entitypigzombie.setNoAI(this.isAIDisabled());
            if (this.hasCustomName()) {
                entitypigzombie.setCustomNameTag(this.getCustomNameTag());
                entitypigzombie.setAlwaysRenderNameTag(this.getAlwaysRenderNameTag());
            }
            this.worldObj.spawnEntityInWorld((Entity)entitypigzombie);
            this.setDead();
        }
    }

    public void fall(float distance, float damageMultiplier) {
        super.fall(distance, damageMultiplier);
        if (distance > 5.0f && this.riddenByEntity instanceof EntityPlayer) {
            ((EntityPlayer)this.riddenByEntity).triggerAchievement((StatBase)AchievementList.flyPig);
        }
    }

    public EntityPig createChild(EntityAgeable ageable) {
        return new EntityPig(this.worldObj);
    }

    public boolean isBreedingItem(ItemStack stack) {
        return stack != null && stack.getItem() == Items.carrot;
    }

    public EntityAIControlledByPlayer getAIControlledByPlayer() {
        return this.aiControlledByPlayer;
    }
}
