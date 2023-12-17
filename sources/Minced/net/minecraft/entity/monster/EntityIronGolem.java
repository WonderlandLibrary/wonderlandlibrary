// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import com.google.common.base.Predicate;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIDefendVillage;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAILookAtVillager;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import net.minecraft.village.Village;
import net.minecraft.network.datasync.DataParameter;

public class EntityIronGolem extends EntityGolem
{
    protected static final DataParameter<Byte> PLAYER_CREATED;
    private int homeCheckTimer;
    @Nullable
    Village village;
    private int attackTimer;
    private int holdRoseTick;
    
    public EntityIronGolem(final World worldIn) {
        super(worldIn);
        this.setSize(1.4f, 2.7f);
    }
    
    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.0, true));
        this.tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 0.9, 32.0f));
        this.tasks.addTask(3, new EntityAIMoveThroughVillage(this, 0.6, true));
        this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0));
        this.tasks.addTask(5, new EntityAILookAtVillager(this));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.6));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0f));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIDefendVillage(this));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, (Class<?>[])new Class[0]));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<Object>(this, EntityLiving.class, 10, false, true, (com.google.common.base.Predicate<?>)new Predicate<EntityLiving>() {
            public boolean apply(@Nullable final EntityLiving p_apply_1_) {
                return p_apply_1_ != null && IMob.VISIBLE_MOB_SELECTOR.apply((Object)p_apply_1_) && !(p_apply_1_ instanceof EntityCreeper);
            }
        }));
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityIronGolem.PLAYER_CREATED, (Byte)0);
    }
    
    @Override
    protected void updateAITasks() {
        final int homeCheckTimer = this.homeCheckTimer - 1;
        this.homeCheckTimer = homeCheckTimer;
        if (homeCheckTimer <= 0) {
            this.homeCheckTimer = 70 + this.rand.nextInt(50);
            this.village = this.world.getVillageCollection().getNearestVillage(new BlockPos(this), 32);
            if (this.village == null) {
                this.detachHome();
            }
            else {
                final BlockPos blockpos = this.village.getCenter();
                this.setHomePosAndDistance(blockpos, (int)(this.village.getVillageRadius() * 0.6f));
            }
        }
        super.updateAITasks();
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0);
    }
    
    @Override
    protected int decreaseAirSupply(final int air) {
        return air;
    }
    
    @Override
    protected void collideWithEntity(final Entity entityIn) {
        if (entityIn instanceof IMob && !(entityIn instanceof EntityCreeper) && this.getRNG().nextInt(20) == 0) {
            this.setAttackTarget((EntityLivingBase)entityIn);
        }
        super.collideWithEntity(entityIn);
    }
    
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.attackTimer > 0) {
            --this.attackTimer;
        }
        if (this.holdRoseTick > 0) {
            --this.holdRoseTick;
        }
        if (this.motionX * this.motionX + this.motionZ * this.motionZ > 2.500000277905201E-7 && this.rand.nextInt(5) == 0) {
            final int i = MathHelper.floor(this.posX);
            final int j = MathHelper.floor(this.posY - 0.20000000298023224);
            final int k = MathHelper.floor(this.posZ);
            final IBlockState iblockstate = this.world.getBlockState(new BlockPos(i, j, k));
            if (iblockstate.getMaterial() != Material.AIR) {
                this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + (this.rand.nextFloat() - 0.5) * this.width, this.getEntityBoundingBox().minY + 0.1, this.posZ + (this.rand.nextFloat() - 0.5) * this.width, 4.0 * (this.rand.nextFloat() - 0.5), 0.5, (this.rand.nextFloat() - 0.5) * 4.0, Block.getStateId(iblockstate));
            }
        }
    }
    
    @Override
    public boolean canAttackClass(final Class<? extends EntityLivingBase> cls) {
        return (!this.isPlayerCreated() || !EntityPlayer.class.isAssignableFrom(cls)) && cls != EntityCreeper.class && super.canAttackClass(cls);
    }
    
    public static void registerFixesIronGolem(final DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityIronGolem.class);
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("PlayerCreated", this.isPlayerCreated());
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setPlayerCreated(compound.getBoolean("PlayerCreated"));
    }
    
    @Override
    public boolean attackEntityAsMob(final Entity entityIn) {
        this.attackTimer = 10;
        this.world.setEntityState(this, (byte)4);
        final boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)(7 + this.rand.nextInt(15)));
        if (flag) {
            entityIn.motionY += 0.4000000059604645;
            this.applyEnchantments(this, entityIn);
        }
        this.playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0f, 1.0f);
        return flag;
    }
    
    @Override
    public void handleStatusUpdate(final byte id) {
        if (id == 4) {
            this.attackTimer = 10;
            this.playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0f, 1.0f);
        }
        else if (id == 11) {
            this.holdRoseTick = 400;
        }
        else if (id == 34) {
            this.holdRoseTick = 0;
        }
        else {
            super.handleStatusUpdate(id);
        }
    }
    
    public Village getVillage() {
        return this.village;
    }
    
    public int getAttackTimer() {
        return this.attackTimer;
    }
    
    public void setHoldingRose(final boolean p_70851_1_) {
        if (p_70851_1_) {
            this.holdRoseTick = 400;
            this.world.setEntityState(this, (byte)11);
        }
        else {
            this.holdRoseTick = 0;
            this.world.setEntityState(this, (byte)34);
        }
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_IRONGOLEM_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_IRONGOLEM_DEATH;
    }
    
    @Override
    protected void playStepSound(final BlockPos pos, final Block blockIn) {
        this.playSound(SoundEvents.ENTITY_IRONGOLEM_STEP, 1.0f, 1.0f);
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_IRON_GOLEM;
    }
    
    public int getHoldRoseTick() {
        return this.holdRoseTick;
    }
    
    public boolean isPlayerCreated() {
        return (this.dataManager.get(EntityIronGolem.PLAYER_CREATED) & 0x1) != 0x0;
    }
    
    public void setPlayerCreated(final boolean playerCreated) {
        final byte b0 = this.dataManager.get(EntityIronGolem.PLAYER_CREATED);
        if (playerCreated) {
            this.dataManager.set(EntityIronGolem.PLAYER_CREATED, (byte)(b0 | 0x1));
        }
        else {
            this.dataManager.set(EntityIronGolem.PLAYER_CREATED, (byte)(b0 & 0xFFFFFFFE));
        }
    }
    
    @Override
    public void onDeath(final DamageSource cause) {
        if (!this.isPlayerCreated() && this.attackingPlayer != null && this.village != null) {
            this.village.modifyPlayerReputation(this.attackingPlayer.getName(), -5);
        }
        super.onDeath(cause);
    }
    
    static {
        PLAYER_CREATED = EntityDataManager.createKey(EntityIronGolem.class, DataSerializers.BYTE);
    }
}
