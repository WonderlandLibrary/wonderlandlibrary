/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.passive;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILlamaFollowCaravan;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAIRunAroundLikeCrazy;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityLlama
extends AbstractChestHorse
implements IRangedAttackMob {
    private static final DataParameter<Integer> field_190720_bG = EntityDataManager.createKey(EntityLlama.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> field_190721_bH = EntityDataManager.createKey(EntityLlama.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> field_190722_bI = EntityDataManager.createKey(EntityLlama.class, DataSerializers.VARINT);
    private boolean field_190723_bJ;
    @Nullable
    private EntityLlama field_190724_bK;
    @Nullable
    private EntityLlama field_190725_bL;

    public EntityLlama(World p_i47297_1_) {
        super(p_i47297_1_);
        this.setSize(0.9f, 1.87f);
    }

    private void func_190706_p(int p_190706_1_) {
        this.dataManager.set(field_190720_bG, Math.max(1, Math.min(5, p_190706_1_)));
    }

    private void func_190705_dT() {
        int i = this.rand.nextFloat() < 0.04f ? 5 : 3;
        this.func_190706_p(1 + this.rand.nextInt(i));
    }

    public int func_190707_dL() {
        return this.dataManager.get(field_190720_bG);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Variant", this.func_190719_dM());
        compound.setInteger("Strength", this.func_190707_dL());
        if (!this.horseChest.getStackInSlot(1).isEmpty()) {
            compound.setTag("DecorItem", this.horseChest.getStackInSlot(1).writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        this.func_190706_p(compound.getInteger("Strength"));
        super.readEntityFromNBT(compound);
        this.func_190710_o(compound.getInteger("Variant"));
        if (compound.hasKey("DecorItem", 10)) {
            this.horseChest.setInventorySlotContents(1, new ItemStack(compound.getCompoundTag("DecorItem")));
        }
        this.updateHorseSlots();
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIRunAroundLikeCrazy(this, 1.2));
        this.tasks.addTask(2, new EntityAILlamaFollowCaravan(this, 2.1f));
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
        this.dataManager.register(field_190720_bG, 0);
        this.dataManager.register(field_190721_bH, -1);
        this.dataManager.register(field_190722_bI, 0);
    }

    public int func_190719_dM() {
        return MathHelper.clamp(this.dataManager.get(field_190722_bI), 0, 3);
    }

    public void func_190710_o(int p_190710_1_) {
        this.dataManager.set(field_190722_bI, p_190710_1_);
    }

    @Override
    protected int func_190686_di() {
        return this.func_190695_dh() ? 2 + 3 * this.func_190696_dl() : super.func_190686_di();
    }

    @Override
    public void updatePassenger(Entity passenger) {
        if (this.isPassenger(passenger)) {
            float f = MathHelper.cos(this.renderYawOffset * ((float)Math.PI / 180));
            float f1 = MathHelper.sin(this.renderYawOffset * ((float)Math.PI / 180));
            float f2 = 0.3f;
            passenger.setPosition(this.posX + (double)(0.3f * f1), this.posY + this.getMountedYOffset() + passenger.getYOffset(), this.posZ - (double)(0.3f * f));
        }
    }

    @Override
    public double getMountedYOffset() {
        return (double)this.height * 0.67;
    }

    @Override
    public boolean canBeSteered() {
        return false;
    }

    @Override
    protected boolean func_190678_b(EntityPlayer p_190678_1_, ItemStack p_190678_2_) {
        int i = 0;
        int j = 0;
        float f = 0.0f;
        boolean flag = false;
        Item item = p_190678_2_.getItem();
        if (item == Items.WHEAT) {
            i = 10;
            j = 3;
            f = 2.0f;
        } else if (item == Item.getItemFromBlock(Blocks.HAY_BLOCK)) {
            i = 90;
            j = 6;
            f = 10.0f;
            if (this.isTame() && this.getGrowingAge() == 0) {
                flag = true;
                this.setInLove(p_190678_1_);
            }
        }
        if (this.getHealth() < this.getMaxHealth() && f > 0.0f) {
            this.heal(f);
            flag = true;
        }
        if (this.isChild() && i > 0) {
            this.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width, this.posY + 0.5 + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width, 0.0, 0.0, 0.0, new int[0]);
            if (!this.world.isRemote) {
                this.addGrowth(i);
            }
            flag = true;
        }
        if (j > 0 && (flag || !this.isTame()) && this.getTemper() < this.func_190676_dC()) {
            flag = true;
            if (!this.world.isRemote) {
                this.increaseTemper(j);
            }
        }
        if (flag && !this.isSilent()) {
            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.field_191253_dD, this.getSoundCategory(), 1.0f, 1.0f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f);
        }
        return flag;
    }

    @Override
    protected boolean isMovementBlocked() {
        return this.getHealth() <= 0.0f || this.isEatingHaystack();
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        int i;
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.func_190705_dT();
        if (livingdata instanceof GroupData) {
            i = ((GroupData)livingdata).field_190886_a;
        } else {
            i = this.rand.nextInt(4);
            livingdata = new GroupData(i);
        }
        this.func_190710_o(i);
        return livingdata;
    }

    public boolean func_190717_dN() {
        return this.func_190704_dO() != null;
    }

    @Override
    protected SoundEvent getAngrySound() {
        return SoundEvents.field_191250_dA;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.field_191260_dz;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.field_191254_dE;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.field_191252_dC;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(SoundEvents.field_191256_dG, 0.15f, 1.0f);
    }

    @Override
    protected void func_190697_dk() {
        this.playSound(SoundEvents.field_191251_dB, 1.0f, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
    }

    @Override
    public void func_190687_dF() {
        SoundEvent soundevent = this.getAngrySound();
        if (soundevent != null) {
            this.playSound(soundevent, this.getSoundVolume(), this.getSoundPitch());
        }
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.field_191187_aw;
    }

    @Override
    public int func_190696_dl() {
        return this.func_190707_dL();
    }

    @Override
    public boolean func_190677_dK() {
        return true;
    }

    @Override
    public boolean func_190682_f(ItemStack p_190682_1_) {
        return p_190682_1_.getItem() == Item.getItemFromBlock(Blocks.CARPET);
    }

    @Override
    public boolean func_190685_dA() {
        return false;
    }

    @Override
    public void onInventoryChanged(IInventory invBasic) {
        EnumDyeColor enumdyecolor = this.func_190704_dO();
        super.onInventoryChanged(invBasic);
        EnumDyeColor enumdyecolor1 = this.func_190704_dO();
        if (this.ticksExisted > 20 && enumdyecolor1 != null && enumdyecolor1 != enumdyecolor) {
            this.playSound(SoundEvents.field_191257_dH, 0.5f, 1.0f);
        }
    }

    @Override
    protected void updateHorseSlots() {
        if (!this.world.isRemote) {
            super.updateHorseSlots();
            this.func_190702_g(this.horseChest.getStackInSlot(1));
        }
    }

    private void func_190711_a(@Nullable EnumDyeColor p_190711_1_) {
        this.dataManager.set(field_190721_bH, p_190711_1_ == null ? -1 : p_190711_1_.getMetadata());
    }

    private void func_190702_g(ItemStack p_190702_1_) {
        if (this.func_190682_f(p_190702_1_)) {
            this.func_190711_a(EnumDyeColor.byMetadata(p_190702_1_.getMetadata()));
        } else {
            this.func_190711_a(null);
        }
    }

    @Nullable
    public EnumDyeColor func_190704_dO() {
        int i = this.dataManager.get(field_190721_bH);
        return i == -1 ? null : EnumDyeColor.byMetadata(i);
    }

    @Override
    public int func_190676_dC() {
        return 30;
    }

    @Override
    public boolean canMateWith(EntityAnimal otherAnimal) {
        return otherAnimal != this && otherAnimal instanceof EntityLlama && this.canMate() && ((EntityLlama)otherAnimal).canMate();
    }

    @Override
    public EntityLlama createChild(EntityAgeable ageable) {
        EntityLlama entityllama = new EntityLlama(this.world);
        this.func_190681_a(ageable, entityllama);
        EntityLlama entityllama1 = (EntityLlama)ageable;
        int i = this.rand.nextInt(Math.max(this.func_190707_dL(), entityllama1.func_190707_dL())) + 1;
        if (this.rand.nextFloat() < 0.03f) {
            ++i;
        }
        entityllama.func_190706_p(i);
        entityllama.func_190710_o(this.rand.nextBoolean() ? this.func_190719_dM() : entityllama1.func_190719_dM());
        return entityllama;
    }

    private void func_190713_e(EntityLivingBase p_190713_1_) {
        EntityLlamaSpit entityllamaspit = new EntityLlamaSpit(this.world, this);
        double d0 = p_190713_1_.posX - this.posX;
        double d1 = p_190713_1_.getEntityBoundingBox().minY + (double)(p_190713_1_.height / 3.0f) - entityllamaspit.posY;
        double d2 = p_190713_1_.posZ - this.posZ;
        float f = MathHelper.sqrt(d0 * d0 + d2 * d2) * 0.2f;
        entityllamaspit.setThrowableHeading(d0, d1 + (double)f, d2, 1.5f, 10.0f);
        this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.field_191255_dF, this.getSoundCategory(), 1.0f, 1.0f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f);
        this.world.spawnEntityInWorld(entityllamaspit);
        this.field_190723_bJ = true;
    }

    private void func_190714_x(boolean p_190714_1_) {
        this.field_190723_bJ = p_190714_1_;
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        int i = MathHelper.ceil((distance * 0.5f - 3.0f) * damageMultiplier);
        if (i > 0) {
            if (distance >= 6.0f) {
                this.attackEntityFrom(DamageSource.fall, i);
                if (this.isBeingRidden()) {
                    for (Entity entity : this.getRecursivePassengers()) {
                        entity.attackEntityFrom(DamageSource.fall, i);
                    }
                }
            }
            IBlockState iblockstate = this.world.getBlockState(new BlockPos(this.posX, this.posY - 0.2 - (double)this.prevRotationYaw, this.posZ));
            Block block = iblockstate.getBlock();
            if (iblockstate.getMaterial() != Material.AIR && !this.isSilent()) {
                SoundType soundtype = block.getSoundType();
                this.world.playSound(null, this.posX, this.posY, this.posZ, soundtype.getStepSound(), this.getSoundCategory(), soundtype.getVolume() * 0.5f, soundtype.getPitch() * 0.75f);
            }
        }
    }

    public void func_190709_dP() {
        if (this.field_190724_bK != null) {
            this.field_190724_bK.field_190725_bL = null;
        }
        this.field_190724_bK = null;
    }

    public void func_190715_a(EntityLlama p_190715_1_) {
        this.field_190724_bK = p_190715_1_;
        this.field_190724_bK.field_190725_bL = this;
    }

    public boolean func_190712_dQ() {
        return this.field_190725_bL != null;
    }

    public boolean func_190718_dR() {
        return this.field_190724_bK != null;
    }

    @Nullable
    public EntityLlama func_190716_dS() {
        return this.field_190724_bK;
    }

    @Override
    protected double func_190634_dg() {
        return 2.0;
    }

    @Override
    protected void func_190679_dD() {
        if (!this.func_190718_dR() && this.isChild()) {
            super.func_190679_dD();
        }
    }

    @Override
    public boolean func_190684_dE() {
        return false;
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
        this.func_190713_e(target);
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {
    }

    static class GroupData
    implements IEntityLivingData {
        public int field_190886_a;

        private GroupData(int p_i47283_1_) {
            this.field_190886_a = p_i47283_1_;
        }
    }

    static class AIHurtByTarget
    extends EntityAIHurtByTarget {
        public AIHurtByTarget(EntityLlama p_i47282_1_) {
            super((EntityCreature)p_i47282_1_, false, new Class[0]);
        }

        @Override
        public boolean continueExecuting() {
            EntityLlama entityllama;
            if (this.taskOwner instanceof EntityLlama && (entityllama = (EntityLlama)this.taskOwner).field_190723_bJ) {
                entityllama.func_190714_x(false);
                return false;
            }
            return super.continueExecuting();
        }
    }

    static class AIDefendTarget
    extends EntityAINearestAttackableTarget<EntityWolf> {
        public AIDefendTarget(EntityLlama p_i47285_1_) {
            super(p_i47285_1_, EntityWolf.class, 16, false, true, null);
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
}

