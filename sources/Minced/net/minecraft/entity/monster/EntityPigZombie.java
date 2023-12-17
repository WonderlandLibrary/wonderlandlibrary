// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.util.EnumHand;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.DamageSource;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.entity.Entity;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.init.SoundEvents;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import java.util.UUID;

public class EntityPigZombie extends EntityZombie
{
    private static final UUID ATTACK_SPEED_BOOST_MODIFIER_UUID;
    private static final AttributeModifier ATTACK_SPEED_BOOST_MODIFIER;
    private int angerLevel;
    private int randomSoundDelay;
    private UUID angerTargetUUID;
    
    public EntityPigZombie(final World worldIn) {
        super(worldIn);
        this.isImmuneToFire = true;
    }
    
    @Override
    public void setRevengeTarget(@Nullable final EntityLivingBase livingBase) {
        super.setRevengeTarget(livingBase);
        if (livingBase != null) {
            this.angerTargetUUID = livingBase.getUniqueID();
        }
    }
    
    @Override
    protected void applyEntityAI() {
        this.targetTasks.addTask(1, new AIHurtByAggressor(this));
        this.targetTasks.addTask(2, new AITargetAggressor(this));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(EntityPigZombie.SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(0.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0);
    }
    
    @Override
    protected void updateAITasks() {
        final IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        if (this.isAngry()) {
            if (!this.isChild() && !iattributeinstance.hasModifier(EntityPigZombie.ATTACK_SPEED_BOOST_MODIFIER)) {
                iattributeinstance.applyModifier(EntityPigZombie.ATTACK_SPEED_BOOST_MODIFIER);
            }
            --this.angerLevel;
        }
        else if (iattributeinstance.hasModifier(EntityPigZombie.ATTACK_SPEED_BOOST_MODIFIER)) {
            iattributeinstance.removeModifier(EntityPigZombie.ATTACK_SPEED_BOOST_MODIFIER);
        }
        if (this.randomSoundDelay > 0 && --this.randomSoundDelay == 0) {
            this.playSound(SoundEvents.ENTITY_ZOMBIE_PIG_ANGRY, this.getSoundVolume() * 2.0f, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f) * 1.8f);
        }
        if (this.angerLevel > 0 && this.angerTargetUUID != null && this.getRevengeTarget() == null) {
            final EntityPlayer entityplayer = this.world.getPlayerEntityByUUID(this.angerTargetUUID);
            this.setRevengeTarget(entityplayer);
            this.attackingPlayer = entityplayer;
            this.recentlyHit = this.getRevengeTimer();
        }
        super.updateAITasks();
    }
    
    @Override
    public boolean getCanSpawnHere() {
        return this.world.getDifficulty() != EnumDifficulty.PEACEFUL;
    }
    
    @Override
    public boolean isNotColliding() {
        return this.world.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.world.containsAnyLiquid(this.getEntityBoundingBox());
    }
    
    public static void registerFixesPigZombie(final DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityPigZombie.class);
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setShort("Anger", (short)this.angerLevel);
        if (this.angerTargetUUID != null) {
            compound.setString("HurtBy", this.angerTargetUUID.toString());
        }
        else {
            compound.setString("HurtBy", "");
        }
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.angerLevel = compound.getShort("Anger");
        final String s = compound.getString("HurtBy");
        if (!s.isEmpty()) {
            this.angerTargetUUID = UUID.fromString(s);
            final EntityPlayer entityplayer = this.world.getPlayerEntityByUUID(this.angerTargetUUID);
            this.setRevengeTarget(entityplayer);
            if (entityplayer != null) {
                this.attackingPlayer = entityplayer;
                this.recentlyHit = this.getRevengeTimer();
            }
        }
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        final Entity entity = source.getTrueSource();
        if (entity instanceof EntityPlayer) {
            this.becomeAngryAt(entity);
        }
        return super.attackEntityFrom(source, amount);
    }
    
    private void becomeAngryAt(final Entity p_70835_1_) {
        this.angerLevel = 400 + this.rand.nextInt(400);
        this.randomSoundDelay = this.rand.nextInt(40);
        if (p_70835_1_ instanceof EntityLivingBase) {
            this.setRevengeTarget((EntityLivingBase)p_70835_1_);
        }
    }
    
    public boolean isAngry() {
        return this.angerLevel > 0;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ZOMBIE_PIG_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_ZOMBIE_PIG_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ZOMBIE_PIG_DEATH;
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_ZOMBIE_PIGMAN;
    }
    
    public boolean processInteract(final EntityPlayer player, final EnumHand hand) {
        return false;
    }
    
    @Override
    protected void setEquipmentBasedOnDifficulty(final DifficultyInstance difficulty) {
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
    }
    
    @Override
    protected ItemStack getSkullDrop() {
        return ItemStack.EMPTY;
    }
    
    @Override
    public boolean isPreventingPlayerRest(final EntityPlayer playerIn) {
        return this.isAngry();
    }
    
    static {
        ATTACK_SPEED_BOOST_MODIFIER_UUID = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");
        ATTACK_SPEED_BOOST_MODIFIER = new AttributeModifier(EntityPigZombie.ATTACK_SPEED_BOOST_MODIFIER_UUID, "Attacking speed boost", 0.05, 0).setSaved(false);
    }
    
    static class AIHurtByAggressor extends EntityAIHurtByTarget
    {
        public AIHurtByAggressor(final EntityPigZombie p_i45828_1_) {
            super(p_i45828_1_, true, (Class<?>[])new Class[0]);
        }
        
        @Override
        protected void setEntityAttackTarget(final EntityCreature creatureIn, final EntityLivingBase entityLivingBaseIn) {
            super.setEntityAttackTarget(creatureIn, entityLivingBaseIn);
            if (creatureIn instanceof EntityPigZombie) {
                ((EntityPigZombie)creatureIn).becomeAngryAt(entityLivingBaseIn);
            }
        }
    }
    
    static class AITargetAggressor extends EntityAINearestAttackableTarget<EntityPlayer>
    {
        public AITargetAggressor(final EntityPigZombie p_i45829_1_) {
            super(p_i45829_1_, EntityPlayer.class, true);
        }
        
        @Override
        public boolean shouldExecute() {
            return ((EntityPigZombie)this.taskOwner).isAngry() && super.shouldExecute();
        }
    }
}
