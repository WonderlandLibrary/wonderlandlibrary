// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.passive;

import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import javax.annotation.Nullable;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIOcelotAttack;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.EntityCreature;
import net.minecraft.init.Items;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.world.World;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.network.datasync.DataParameter;

public class EntityOcelot extends EntityTameable
{
    private static final DataParameter<Integer> OCELOT_VARIANT;
    private EntityAIAvoidEntity<EntityPlayer> avoidEntity;
    private EntityAITempt aiTempt;
    
    public EntityOcelot(final World worldIn) {
        super(worldIn);
        this.setSize(0.6f, 0.7f);
    }
    
    @Override
    protected void initEntityAI() {
        this.aiSit = new EntityAISit(this);
        this.aiTempt = new EntityAITempt(this, 0.6, Items.FISH, true);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(3, this.aiTempt);
        this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.0, 10.0f, 5.0f));
        this.tasks.addTask(6, new EntityAIOcelotSit(this, 0.8));
        this.tasks.addTask(7, new EntityAILeapAtTarget(this, 0.3f));
        this.tasks.addTask(8, new EntityAIOcelotAttack(this));
        this.tasks.addTask(9, new EntityAIMate(this, 0.8));
        this.tasks.addTask(10, new EntityAIWanderAvoidWater(this, 0.8, 1.0000001E-5f));
        this.tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0f));
        this.targetTasks.addTask(1, new EntityAITargetNonTamed<Object>(this, EntityChicken.class, false, null));
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityOcelot.OCELOT_VARIANT, 0);
    }
    
    public void updateAITasks() {
        if (this.getMoveHelper().isUpdating()) {
            final double d0 = this.getMoveHelper().getSpeed();
            if (d0 == 0.6) {
                this.setSneaking(true);
                this.setSprinting(false);
            }
            else if (d0 == 1.33) {
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
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896);
    }
    
    @Override
    public void fall(final float distance, final float damageMultiplier) {
    }
    
    public static void registerFixesOcelot(final DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityOcelot.class);
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("CatType", this.getTameSkin());
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setTameSkin(compound.getInteger("CatType"));
    }
    
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        if (!this.isTamed()) {
            return null;
        }
        if (this.isInLove()) {
            return SoundEvents.ENTITY_CAT_PURR;
        }
        return (this.rand.nextInt(4) == 0) ? SoundEvents.ENTITY_CAT_PURREOW : SoundEvents.ENTITY_CAT_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_CAT_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_CAT_DEATH;
    }
    
    @Override
    protected float getSoundVolume() {
        return 0.4f;
    }
    
    @Override
    public boolean attackEntityAsMob(final Entity entityIn) {
        return entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0f);
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (this.aiSit != null) {
            this.aiSit.setSitting(false);
        }
        return super.attackEntityFrom(source, amount);
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_OCELOT;
    }
    
    @Override
    public boolean processInteract(final EntityPlayer player, final EnumHand hand) {
        final ItemStack itemstack = player.getHeldItem(hand);
        if (this.isTamed()) {
            if (this.isOwner(player) && !this.world.isRemote && !this.isBreedingItem(itemstack)) {
                this.aiSit.setSitting(!this.isSitting());
            }
        }
        else if ((this.aiTempt == null || this.aiTempt.isRunning()) && itemstack.getItem() == Items.FISH && player.getDistanceSq(this) < 9.0) {
            if (!player.capabilities.isCreativeMode) {
                itemstack.shrink(1);
            }
            if (!this.world.isRemote) {
                if (this.rand.nextInt(3) == 0) {
                    this.setTamedBy(player);
                    this.setTameSkin(1 + this.world.rand.nextInt(3));
                    this.playTameEffect(true);
                    this.aiSit.setSitting(true);
                    this.world.setEntityState(this, (byte)7);
                }
                else {
                    this.playTameEffect(false);
                    this.world.setEntityState(this, (byte)6);
                }
            }
            return true;
        }
        return super.processInteract(player, hand);
    }
    
    @Override
    public EntityOcelot createChild(final EntityAgeable ageable) {
        final EntityOcelot entityocelot = new EntityOcelot(this.world);
        if (this.isTamed()) {
            entityocelot.setOwnerId(this.getOwnerId());
            entityocelot.setTamed(true);
            entityocelot.setTameSkin(this.getTameSkin());
        }
        return entityocelot;
    }
    
    @Override
    public boolean isBreedingItem(final ItemStack stack) {
        return stack.getItem() == Items.FISH;
    }
    
    @Override
    public boolean canMateWith(final EntityAnimal otherAnimal) {
        if (otherAnimal == this) {
            return false;
        }
        if (!this.isTamed()) {
            return false;
        }
        if (!(otherAnimal instanceof EntityOcelot)) {
            return false;
        }
        final EntityOcelot entityocelot = (EntityOcelot)otherAnimal;
        return entityocelot.isTamed() && this.isInLove() && entityocelot.isInLove();
    }
    
    public int getTameSkin() {
        return this.dataManager.get(EntityOcelot.OCELOT_VARIANT);
    }
    
    public void setTameSkin(final int skinId) {
        this.dataManager.set(EntityOcelot.OCELOT_VARIANT, skinId);
    }
    
    @Override
    public boolean getCanSpawnHere() {
        return this.world.rand.nextInt(3) != 0;
    }
    
    @Override
    public boolean isNotColliding() {
        if (this.world.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.world.containsAnyLiquid(this.getEntityBoundingBox())) {
            final BlockPos blockpos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
            if (blockpos.getY() < this.world.getSeaLevel()) {
                return false;
            }
            final IBlockState iblockstate = this.world.getBlockState(blockpos.down());
            final Block block = iblockstate.getBlock();
            if (block == Blocks.GRASS || iblockstate.getMaterial() == Material.LEAVES) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String getName() {
        if (this.hasCustomName()) {
            return this.getCustomNameTag();
        }
        return this.isTamed() ? I18n.translateToLocal("entity.Cat.name") : super.getName();
    }
    
    @Override
    protected void setupTamedAI() {
        if (this.avoidEntity == null) {
            this.avoidEntity = new EntityAIAvoidEntity<EntityPlayer>(this, EntityPlayer.class, 16.0f, 0.8, 1.33);
        }
        this.tasks.removeTask(this.avoidEntity);
        if (!this.isTamed()) {
            this.tasks.addTask(4, this.avoidEntity);
        }
    }
    
    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        if (this.getTameSkin() == 0 && this.world.rand.nextInt(7) == 0) {
            for (int i = 0; i < 2; ++i) {
                final EntityOcelot entityocelot = new EntityOcelot(this.world);
                entityocelot.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0f);
                entityocelot.setGrowingAge(-24000);
                this.world.spawnEntity(entityocelot);
            }
        }
        return livingdata;
    }
    
    static {
        OCELOT_VARIANT = EntityDataManager.createKey(EntityOcelot.class, DataSerializers.VARINT);
    }
}
