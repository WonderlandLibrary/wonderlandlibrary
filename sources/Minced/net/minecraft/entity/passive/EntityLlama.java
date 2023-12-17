// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.passive;

import net.minecraft.entity.ai.EntityAIHurtByTarget;
import com.google.common.base.Predicate;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import java.util.Iterator;
import net.minecraft.block.material.Material;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAILlamaFollowCaravan;
import net.minecraft.entity.ai.EntityAIRunAroundLikeCrazy;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.entity.IRangedAttackMob;

public class EntityLlama extends AbstractChestHorse implements IRangedAttackMob
{
    private static final DataParameter<Integer> DATA_STRENGTH_ID;
    private static final DataParameter<Integer> DATA_COLOR_ID;
    private static final DataParameter<Integer> DATA_VARIANT_ID;
    private boolean didSpit;
    @Nullable
    private EntityLlama caravanHead;
    @Nullable
    private EntityLlama caravanTail;
    
    public EntityLlama(final World worldIn) {
        super(worldIn);
        this.setSize(0.9f, 1.87f);
    }
    
    private void setStrength(final int strengthIn) {
        this.dataManager.set(EntityLlama.DATA_STRENGTH_ID, Math.max(1, Math.min(5, strengthIn)));
    }
    
    private void setRandomStrength() {
        final int i = (this.rand.nextFloat() < 0.04f) ? 5 : 3;
        this.setStrength(1 + this.rand.nextInt(i));
    }
    
    public int getStrength() {
        return this.dataManager.get(EntityLlama.DATA_STRENGTH_ID);
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Variant", this.getVariant());
        compound.setInteger("Strength", this.getStrength());
        if (!this.horseChest.getStackInSlot(1).isEmpty()) {
            compound.setTag("DecorItem", this.horseChest.getStackInSlot(1).writeToNBT(new NBTTagCompound()));
        }
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        this.setStrength(compound.getInteger("Strength"));
        super.readEntityFromNBT(compound);
        this.setVariant(compound.getInteger("Variant"));
        if (compound.hasKey("DecorItem", 10)) {
            this.horseChest.setInventorySlotContents(1, new ItemStack(compound.getCompoundTag("DecorItem")));
        }
        this.updateHorseSlots();
    }
    
    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIRunAroundLikeCrazy(this, 1.2));
        this.tasks.addTask(2, new EntityAILlamaFollowCaravan(this, 2.0999999046325684));
        this.tasks.addTask(3, new EntityAIAttackRanged(this, 1.25, 40, 20.0f));
        this.tasks.addTask(3, new EntityAIPanic(this, 1.2));
        this.tasks.addTask(4, new EntityAIMate(this, 1.0));
        this.tasks.addTask(5, new EntityAIFollowParent(this, 1.0));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.7));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0f));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new AIHurtByTarget(this));
        this.targetTasks.addTask(2, new AIDefendTarget(this));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityLlama.DATA_STRENGTH_ID, 0);
        this.dataManager.register(EntityLlama.DATA_COLOR_ID, -1);
        this.dataManager.register(EntityLlama.DATA_VARIANT_ID, 0);
    }
    
    public int getVariant() {
        return MathHelper.clamp(this.dataManager.get(EntityLlama.DATA_VARIANT_ID), 0, 3);
    }
    
    public void setVariant(final int variantIn) {
        this.dataManager.set(EntityLlama.DATA_VARIANT_ID, variantIn);
    }
    
    @Override
    protected int getInventorySize() {
        return this.hasChest() ? (2 + 3 * this.getInventoryColumns()) : super.getInventorySize();
    }
    
    @Override
    public void updatePassenger(final Entity passenger) {
        if (this.isPassenger(passenger)) {
            final float f = MathHelper.cos(this.renderYawOffset * 0.017453292f);
            final float f2 = MathHelper.sin(this.renderYawOffset * 0.017453292f);
            final float f3 = 0.3f;
            passenger.setPosition(this.posX + 0.3f * f2, this.posY + this.getMountedYOffset() + passenger.getYOffset(), this.posZ - 0.3f * f);
        }
    }
    
    @Override
    public double getMountedYOffset() {
        return this.height * 0.67;
    }
    
    @Override
    public boolean canBeSteered() {
        return false;
    }
    
    @Override
    protected boolean handleEating(final EntityPlayer player, final ItemStack stack) {
        int i = 0;
        int j = 0;
        float f = 0.0f;
        boolean flag = false;
        final Item item = stack.getItem();
        if (item == Items.WHEAT) {
            i = 10;
            j = 3;
            f = 2.0f;
        }
        else if (item == Item.getItemFromBlock(Blocks.HAY_BLOCK)) {
            i = 90;
            j = 6;
            f = 10.0f;
            if (this.isTame() && this.getGrowingAge() == 0) {
                flag = true;
                this.setInLove(player);
            }
        }
        if (this.getHealth() < this.getMaxHealth() && f > 0.0f) {
            this.heal(f);
            flag = true;
        }
        if (this.isChild() && i > 0) {
            this.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, this.posX + this.rand.nextFloat() * this.width * 2.0f - this.width, this.posY + 0.5 + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0f - this.width, 0.0, 0.0, 0.0, new int[0]);
            if (!this.world.isRemote) {
                this.addGrowth(i);
            }
            flag = true;
        }
        if (j > 0 && (flag || !this.isTame()) && this.getTemper() < this.getMaxTemper()) {
            flag = true;
            if (!this.world.isRemote) {
                this.increaseTemper(j);
            }
        }
        if (flag && !this.isSilent()) {
            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LLAMA_EAT, this.getSoundCategory(), 1.0f, 1.0f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f);
        }
        return flag;
    }
    
    @Override
    protected boolean isMovementBlocked() {
        return this.getHealth() <= 0.0f || this.isEatingHaystack();
    }
    
    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setRandomStrength();
        int i;
        if (livingdata instanceof GroupData) {
            i = ((GroupData)livingdata).variant;
        }
        else {
            i = this.rand.nextInt(4);
            livingdata = new GroupData(i);
        }
        this.setVariant(i);
        return livingdata;
    }
    
    public boolean hasColor() {
        return this.getColor() != null;
    }
    
    @Override
    protected SoundEvent getAngrySound() {
        return SoundEvents.ENTITY_LLAMA_ANGRY;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_LLAMA_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_LLAMA_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_LLAMA_DEATH;
    }
    
    @Override
    protected void playStepSound(final BlockPos pos, final Block blockIn) {
        this.playSound(SoundEvents.ENTITY_LLAMA_STEP, 0.15f, 1.0f);
    }
    
    @Override
    protected void playChestEquipSound() {
        this.playSound(SoundEvents.ENTITY_LLAMA_CHEST, 1.0f, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
    }
    
    @Override
    public void makeMad() {
        final SoundEvent soundevent = this.getAngrySound();
        if (soundevent != null) {
            this.playSound(soundevent, this.getSoundVolume(), this.getSoundPitch());
        }
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_LLAMA;
    }
    
    @Override
    public int getInventoryColumns() {
        return this.getStrength();
    }
    
    @Override
    public boolean wearsArmor() {
        return true;
    }
    
    @Override
    public boolean isArmor(final ItemStack stack) {
        return stack.getItem() == Item.getItemFromBlock(Blocks.CARPET);
    }
    
    @Override
    public boolean canBeSaddled() {
        return false;
    }
    
    @Override
    public void onInventoryChanged(final IInventory invBasic) {
        final EnumDyeColor enumdyecolor = this.getColor();
        super.onInventoryChanged(invBasic);
        final EnumDyeColor enumdyecolor2 = this.getColor();
        if (this.ticksExisted > 20 && enumdyecolor2 != null && enumdyecolor2 != enumdyecolor) {
            this.playSound(SoundEvents.ENTITY_LLAMA_SWAG, 0.5f, 1.0f);
        }
    }
    
    @Override
    protected void updateHorseSlots() {
        if (!this.world.isRemote) {
            super.updateHorseSlots();
            this.setColorByItem(this.horseChest.getStackInSlot(1));
        }
    }
    
    private void setColor(@Nullable final EnumDyeColor color) {
        this.dataManager.set(EntityLlama.DATA_COLOR_ID, (color == null) ? -1 : color.getMetadata());
    }
    
    private void setColorByItem(final ItemStack stack) {
        if (this.isArmor(stack)) {
            this.setColor(EnumDyeColor.byMetadata(stack.getMetadata()));
        }
        else {
            this.setColor(null);
        }
    }
    
    @Nullable
    public EnumDyeColor getColor() {
        final int i = this.dataManager.get(EntityLlama.DATA_COLOR_ID);
        return (i == -1) ? null : EnumDyeColor.byMetadata(i);
    }
    
    @Override
    public int getMaxTemper() {
        return 30;
    }
    
    @Override
    public boolean canMateWith(final EntityAnimal otherAnimal) {
        return otherAnimal != this && otherAnimal instanceof EntityLlama && this.canMate() && ((EntityLlama)otherAnimal).canMate();
    }
    
    @Override
    public EntityLlama createChild(final EntityAgeable ageable) {
        final EntityLlama entityllama = new EntityLlama(this.world);
        this.setOffspringAttributes(ageable, entityllama);
        final EntityLlama entityllama2 = (EntityLlama)ageable;
        int i = this.rand.nextInt(Math.max(this.getStrength(), entityllama2.getStrength())) + 1;
        if (this.rand.nextFloat() < 0.03f) {
            ++i;
        }
        entityllama.setStrength(i);
        entityllama.setVariant(this.rand.nextBoolean() ? this.getVariant() : entityllama2.getVariant());
        return entityllama;
    }
    
    private void spit(final EntityLivingBase target) {
        final EntityLlamaSpit entityllamaspit = new EntityLlamaSpit(this.world, this);
        final double d0 = target.posX - this.posX;
        final double d2 = target.getEntityBoundingBox().minY + target.height / 3.0f - entityllamaspit.posY;
        final double d3 = target.posZ - this.posZ;
        final float f = MathHelper.sqrt(d0 * d0 + d3 * d3) * 0.2f;
        entityllamaspit.shoot(d0, d2 + f, d3, 1.5f, 10.0f);
        this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LLAMA_SPIT, this.getSoundCategory(), 1.0f, 1.0f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f);
        this.world.spawnEntity(entityllamaspit);
        this.didSpit = true;
    }
    
    private void setDidSpit(final boolean didSpitIn) {
        this.didSpit = didSpitIn;
    }
    
    @Override
    public void fall(final float distance, final float damageMultiplier) {
        final int i = MathHelper.ceil((distance * 0.5f - 3.0f) * damageMultiplier);
        if (i > 0) {
            if (distance >= 6.0f) {
                this.attackEntityFrom(DamageSource.FALL, (float)i);
                if (this.isBeingRidden()) {
                    for (final Entity entity : this.getRecursivePassengers()) {
                        entity.attackEntityFrom(DamageSource.FALL, (float)i);
                    }
                }
            }
            final IBlockState iblockstate = this.world.getBlockState(new BlockPos(this.posX, this.posY - 0.2 - this.prevRotationYaw, this.posZ));
            final Block block = iblockstate.getBlock();
            if (iblockstate.getMaterial() != Material.AIR && !this.isSilent()) {
                final SoundType soundtype = block.getSoundType();
                this.world.playSound(null, this.posX, this.posY, this.posZ, soundtype.getStepSound(), this.getSoundCategory(), soundtype.getVolume() * 0.5f, soundtype.getPitch() * 0.75f);
            }
        }
    }
    
    public void leaveCaravan() {
        if (this.caravanHead != null) {
            this.caravanHead.caravanTail = null;
        }
        this.caravanHead = null;
    }
    
    public void joinCaravan(final EntityLlama caravanHeadIn) {
        this.caravanHead = caravanHeadIn;
        this.caravanHead.caravanTail = this;
    }
    
    public boolean hasCaravanTrail() {
        return this.caravanTail != null;
    }
    
    public boolean inCaravan() {
        return this.caravanHead != null;
    }
    
    @Nullable
    public EntityLlama getCaravanHead() {
        return this.caravanHead;
    }
    
    @Override
    protected double followLeashSpeed() {
        return 2.0;
    }
    
    @Override
    protected void followMother() {
        if (!this.inCaravan() && this.isChild()) {
            super.followMother();
        }
    }
    
    @Override
    public boolean canEatGrass() {
        return false;
    }
    
    @Override
    public void attackEntityWithRangedAttack(final EntityLivingBase target, final float distanceFactor) {
        this.spit(target);
    }
    
    @Override
    public void setSwingingArms(final boolean swingingArms) {
    }
    
    static {
        DATA_STRENGTH_ID = EntityDataManager.createKey(EntityLlama.class, DataSerializers.VARINT);
        DATA_COLOR_ID = EntityDataManager.createKey(EntityLlama.class, DataSerializers.VARINT);
        DATA_VARIANT_ID = EntityDataManager.createKey(EntityLlama.class, DataSerializers.VARINT);
    }
    
    static class AIDefendTarget extends EntityAINearestAttackableTarget<EntityWolf>
    {
        public AIDefendTarget(final EntityLlama llama) {
            super(llama, EntityWolf.class, 16, false, true, null);
        }
        
        @Override
        public boolean shouldExecute() {
            if (super.shouldExecute() && this.targetEntity != null && !((EntityWolf)this.targetEntity).isTamed()) {
                return true;
            }
            this.taskOwner.setAttackTarget(null);
            return false;
        }
        
        @Override
        protected double getTargetDistance() {
            return super.getTargetDistance() * 0.25;
        }
    }
    
    static class AIHurtByTarget extends EntityAIHurtByTarget
    {
        public AIHurtByTarget(final EntityLlama llama) {
            super(llama, false, (Class<?>[])new Class[0]);
        }
        
        @Override
        public boolean shouldContinueExecuting() {
            if (this.taskOwner instanceof EntityLlama) {
                final EntityLlama entityllama = (EntityLlama)this.taskOwner;
                if (entityllama.didSpit) {
                    entityllama.setDidSpit(false);
                    return false;
                }
            }
            return super.shouldContinueExecuting();
        }
    }
    
    static class GroupData implements IEntityLivingData
    {
        public int variant;
        
        private GroupData(final int variantIn) {
            this.variant = variantIn;
        }
    }
}
