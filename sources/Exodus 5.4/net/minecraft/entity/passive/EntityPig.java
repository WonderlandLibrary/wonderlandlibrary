/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
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
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityPig
extends EntityAnimal {
    private final EntityAIControlledByPlayer aiControlledByPlayer;

    @Override
    public boolean interact(EntityPlayer entityPlayer) {
        if (super.interact(entityPlayer)) {
            return true;
        }
        if (!this.getSaddled() || this.worldObj.isRemote || this.riddenByEntity != null && this.riddenByEntity != entityPlayer) {
            return false;
        }
        entityPlayer.mountEntity(this);
        return true;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, (byte)0);
    }

    @Override
    protected Item getDropItem() {
        return this.isBurning() ? Items.cooked_porkchop : Items.porkchop;
    }

    @Override
    protected String getDeathSound() {
        return "mob.pig.death";
    }

    @Override
    protected void playStepSound(BlockPos blockPos, Block block) {
        this.playSound("mob.pig.step", 0.15f, 1.0f);
    }

    @Override
    public void onStruckByLightning(EntityLightningBolt entityLightningBolt) {
        if (!this.worldObj.isRemote && !this.isDead) {
            EntityPigZombie entityPigZombie = new EntityPigZombie(this.worldObj);
            entityPigZombie.setCurrentItemOrArmor(0, new ItemStack(Items.golden_sword));
            entityPigZombie.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            entityPigZombie.setNoAI(this.isAIDisabled());
            if (this.hasCustomName()) {
                entityPigZombie.setCustomNameTag(this.getCustomNameTag());
                entityPigZombie.setAlwaysRenderNameTag(this.getAlwaysRenderNameTag());
            }
            this.worldObj.spawnEntityInWorld(entityPigZombie);
            this.setDead();
        }
    }

    public void setSaddled(boolean bl) {
        if (bl) {
            this.dataWatcher.updateObject(16, (byte)1);
        } else {
            this.dataWatcher.updateObject(16, (byte)0);
        }
    }

    public EntityAIControlledByPlayer getAIControlledByPlayer() {
        return this.aiControlledByPlayer;
    }

    public boolean getSaddled() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        nBTTagCompound.setBoolean("Saddle", this.getSaddled());
    }

    @Override
    public void fall(float f, float f2) {
        super.fall(f, f2);
        if (f > 5.0f && this.riddenByEntity instanceof EntityPlayer) {
            ((EntityPlayer)this.riddenByEntity).triggerAchievement(AchievementList.flyPig);
        }
    }

    @Override
    protected String getLivingSound() {
        return "mob.pig.say";
    }

    @Override
    public boolean isBreedingItem(ItemStack itemStack) {
        return itemStack != null && itemStack.getItem() == Items.carrot;
    }

    public EntityPig(World world) {
        super(world);
        this.setSize(0.9f, 0.9f);
        ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.25));
        this.aiControlledByPlayer = new EntityAIControlledByPlayer(this, 0.3f);
        this.tasks.addTask(2, this.aiControlledByPlayer);
        this.tasks.addTask(3, new EntityAIMate(this, 1.0));
        this.tasks.addTask(4, new EntityAITempt(this, 1.2, Items.carrot_on_a_stick, false));
        this.tasks.addTask(4, new EntityAITempt(this, 1.2, Items.carrot, false));
        this.tasks.addTask(5, new EntityAIFollowParent(this, 1.1));
        this.tasks.addTask(6, new EntityAIWander(this, 1.0));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0f));
        this.tasks.addTask(8, new EntityAILookIdle(this));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25);
    }

    @Override
    public EntityPig createChild(EntityAgeable entityAgeable) {
        return new EntityPig(this.worldObj);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        super.readEntityFromNBT(nBTTagCompound);
        this.setSaddled(nBTTagCompound.getBoolean("Saddle"));
    }

    @Override
    protected void dropFewItems(boolean bl, int n) {
        int n2 = this.rand.nextInt(3) + 1 + this.rand.nextInt(1 + n);
        int n3 = 0;
        while (n3 < n2) {
            if (this.isBurning()) {
                this.dropItem(Items.cooked_porkchop, 1);
            } else {
                this.dropItem(Items.porkchop, 1);
            }
            ++n3;
        }
        if (this.getSaddled()) {
            this.dropItem(Items.saddle, 1);
        }
    }

    @Override
    public boolean canBeSteered() {
        ItemStack itemStack = ((EntityPlayer)this.riddenByEntity).getHeldItem();
        return itemStack != null && itemStack.getItem() == Items.carrot_on_a_stick;
    }

    @Override
    protected String getHurtSound() {
        return "mob.pig.say";
    }
}

