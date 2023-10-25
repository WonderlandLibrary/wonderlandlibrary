/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.monster;

import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityPigZombie
extends EntityZombie {
    private static final AttributeModifier ATTACK_SPEED_BOOST_MODIFIER;
    private static final UUID ATTACK_SPEED_BOOST_MODIFIER_UUID;
    private int angerLevel;
    private int randomSoundDelay;
    private UUID angerTargetUUID;

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficultyInstance, IEntityLivingData iEntityLivingData) {
        super.onInitialSpawn(difficultyInstance, iEntityLivingData);
        this.setVillager(false);
        return iEntityLivingData;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        nBTTagCompound.setShort("Anger", (short)this.angerLevel);
        if (this.angerTargetUUID != null) {
            nBTTagCompound.setString("HurtBy", this.angerTargetUUID.toString());
        } else {
            nBTTagCompound.setString("HurtBy", "");
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        if (this.isEntityInvulnerable(damageSource)) {
            return false;
        }
        Entity entity = damageSource.getEntity();
        if (entity instanceof EntityPlayer) {
            this.becomeAngryAt(entity);
        }
        return super.attackEntityFrom(damageSource, f);
    }

    @Override
    public boolean getCanSpawnHere() {
        return this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL;
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficultyInstance) {
        this.setCurrentItemOrArmor(0, new ItemStack(Items.golden_sword));
    }

    @Override
    protected String getLivingSound() {
        return "mob.zombiepig.zpig";
    }

    @Override
    public boolean isNotColliding() {
        return this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(this.getEntityBoundingBox());
    }

    static {
        ATTACK_SPEED_BOOST_MODIFIER_UUID = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");
        ATTACK_SPEED_BOOST_MODIFIER = new AttributeModifier(ATTACK_SPEED_BOOST_MODIFIER_UUID, "Attacking speed boost", 0.05, 0).setSaved(false);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        super.readEntityFromNBT(nBTTagCompound);
        this.angerLevel = nBTTagCompound.getShort("Anger");
        String string = nBTTagCompound.getString("HurtBy");
        if (string.length() > 0) {
            this.angerTargetUUID = UUID.fromString(string);
            EntityPlayer entityPlayer = this.worldObj.getPlayerEntityByUUID(this.angerTargetUUID);
            this.setRevengeTarget(entityPlayer);
            if (entityPlayer != null) {
                this.attackingPlayer = entityPlayer;
                this.recentlyHit = this.getRevengeTimer();
            }
        }
    }

    public boolean isAngry() {
        return this.angerLevel > 0;
    }

    @Override
    protected void updateAITasks() {
        IAttributeInstance iAttributeInstance = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
        if (this.isAngry()) {
            if (!this.isChild() && !iAttributeInstance.hasModifier(ATTACK_SPEED_BOOST_MODIFIER)) {
                iAttributeInstance.applyModifier(ATTACK_SPEED_BOOST_MODIFIER);
            }
            --this.angerLevel;
        } else if (iAttributeInstance.hasModifier(ATTACK_SPEED_BOOST_MODIFIER)) {
            iAttributeInstance.removeModifier(ATTACK_SPEED_BOOST_MODIFIER);
        }
        if (this.randomSoundDelay > 0 && --this.randomSoundDelay == 0) {
            this.playSound("mob.zombiepig.zpigangry", this.getSoundVolume() * 2.0f, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f) * 1.8f);
        }
        if (this.angerLevel > 0 && this.angerTargetUUID != null && this.getAITarget() == null) {
            EntityPlayer entityPlayer = this.worldObj.getPlayerEntityByUUID(this.angerTargetUUID);
            this.setRevengeTarget(entityPlayer);
            this.attackingPlayer = entityPlayer;
            this.recentlyHit = this.getRevengeTimer();
        }
        super.updateAITasks();
    }

    @Override
    public void setRevengeTarget(EntityLivingBase entityLivingBase) {
        super.setRevengeTarget(entityLivingBase);
        if (entityLivingBase != null) {
            this.angerTargetUUID = entityLivingBase.getUniqueID();
        }
    }

    @Override
    protected void applyEntityAI() {
        this.targetTasks.addTask(1, new AIHurtByAggressor(this));
        this.targetTasks.addTask(2, new AITargetAggressor(this));
    }

    @Override
    protected String getDeathSound() {
        return "mob.zombiepig.zpigdeath";
    }

    private void becomeAngryAt(Entity entity) {
        this.angerLevel = 400 + this.rand.nextInt(400);
        this.randomSoundDelay = this.rand.nextInt(40);
        if (entity instanceof EntityLivingBase) {
            this.setRevengeTarget((EntityLivingBase)entity);
        }
    }

    @Override
    protected void dropFewItems(boolean bl, int n) {
        int n2 = this.rand.nextInt(2 + n);
        int n3 = 0;
        while (n3 < n2) {
            this.dropItem(Items.rotten_flesh, 1);
            ++n3;
        }
        n2 = this.rand.nextInt(2 + n);
        n3 = 0;
        while (n3 < n2) {
            this.dropItem(Items.gold_nugget, 1);
            ++n3;
        }
    }

    @Override
    protected void addRandomDrop() {
        this.dropItem(Items.gold_ingot, 1);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(reinforcementChance).setBaseValue(0.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23f);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(5.0);
    }

    @Override
    public boolean interact(EntityPlayer entityPlayer) {
        return false;
    }

    @Override
    protected String getHurtSound() {
        return "mob.zombiepig.zpighurt";
    }

    public EntityPigZombie(World world) {
        super(world);
        this.isImmuneToFire = true;
    }

    static class AITargetAggressor
    extends EntityAINearestAttackableTarget<EntityPlayer> {
        public AITargetAggressor(EntityPigZombie entityPigZombie) {
            super((EntityCreature)entityPigZombie, EntityPlayer.class, true);
        }

        @Override
        public boolean shouldExecute() {
            return ((EntityPigZombie)this.taskOwner).isAngry() && super.shouldExecute();
        }
    }

    static class AIHurtByAggressor
    extends EntityAIHurtByTarget {
        public AIHurtByAggressor(EntityPigZombie entityPigZombie) {
            super((EntityCreature)entityPigZombie, true, new Class[0]);
        }

        @Override
        protected void setEntityAttackTarget(EntityCreature entityCreature, EntityLivingBase entityLivingBase) {
            super.setEntityAttackTarget(entityCreature, entityLivingBase);
            if (entityCreature instanceof EntityPigZombie) {
                ((EntityPigZombie)entityCreature).becomeAngryAt(entityLivingBase);
            }
        }
    }
}

