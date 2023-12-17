// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import java.util.List;
import net.minecraft.item.EnumDyeColor;
import com.google.common.base.Predicate;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.IBlockAccess;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.util.DamageSource;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.world.World;
import net.minecraft.entity.passive.EntitySheep;

public class EntityEvoker extends EntitySpellcasterIllager
{
    private EntitySheep wololoTarget;
    
    public EntityEvoker(final World worldIn) {
        super(worldIn);
        this.setSize(0.6f, 1.95f);
        this.experienceValue = 10;
    }
    
    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new AICastingSpell());
        this.tasks.addTask(2, new EntityAIAvoidEntity<Object>(this, EntityPlayer.class, 8.0f, 0.6, 1.0));
        this.tasks.addTask(4, new AISummonSpell());
        this.tasks.addTask(5, new AIAttackSpell());
        this.tasks.addTask(6, new AIWololoSpell());
        this.tasks.addTask(8, new EntityAIWander(this, 0.6));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0f, 1.0f));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0f));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, (Class<?>[])new Class[] { EntityEvoker.class }));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<Object>(this, EntityPlayer.class, true).setUnseenMemoryTicks(300));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<Object>(this, EntityVillager.class, false).setUnseenMemoryTicks(300));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<Object>(this, EntityIronGolem.class, false));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(12.0);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(24.0);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
    }
    
    public static void registerFixesEvoker(final DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityEvoker.class);
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
    }
    
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_EVOCATION_ILLAGER;
    }
    
    @Override
    protected void updateAITasks() {
        super.updateAITasks();
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
    }
    
    @Override
    public boolean isOnSameTeam(final Entity entityIn) {
        if (entityIn == null) {
            return false;
        }
        if (entityIn == this) {
            return true;
        }
        if (super.isOnSameTeam(entityIn)) {
            return true;
        }
        if (entityIn instanceof EntityVex) {
            return this.isOnSameTeam(((EntityVex)entityIn).getOwner());
        }
        return entityIn instanceof EntityLivingBase && ((EntityLivingBase)entityIn).getCreatureAttribute() == EnumCreatureAttribute.ILLAGER && this.getTeam() == null && entityIn.getTeam() == null;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_EVOCATION_ILLAGER_AMBIENT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.EVOCATION_ILLAGER_DEATH;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_EVOCATION_ILLAGER_HURT;
    }
    
    private void setWololoTarget(@Nullable final EntitySheep wololoTargetIn) {
        this.wololoTarget = wololoTargetIn;
    }
    
    @Nullable
    private EntitySheep getWololoTarget() {
        return this.wololoTarget;
    }
    
    @Override
    protected SoundEvent getSpellSound() {
        return SoundEvents.EVOCATION_ILLAGER_CAST_SPELL;
    }
    
    class AIAttackSpell extends AIUseSpell
    {
        private AIAttackSpell() {
        }
        
        @Override
        protected int getCastingTime() {
            return 40;
        }
        
        @Override
        protected int getCastingInterval() {
            return 100;
        }
        
        @Override
        protected void castSpell() {
            final EntityLivingBase entitylivingbase = EntityEvoker.this.getAttackTarget();
            final double d0 = Math.min(entitylivingbase.posY, EntityEvoker.this.posY);
            final double d2 = Math.max(entitylivingbase.posY, EntityEvoker.this.posY) + 1.0;
            final float f = (float)MathHelper.atan2(entitylivingbase.posZ - EntityEvoker.this.posZ, entitylivingbase.posX - EntityEvoker.this.posX);
            if (EntityEvoker.this.getDistanceSq(entitylivingbase) < 9.0) {
                for (int i = 0; i < 5; ++i) {
                    final float f2 = f + i * 3.1415927f * 0.4f;
                    this.spawnFangs(EntityEvoker.this.posX + MathHelper.cos(f2) * 1.5, EntityEvoker.this.posZ + MathHelper.sin(f2) * 1.5, d0, d2, f2, 0);
                }
                for (int k = 0; k < 8; ++k) {
                    final float f3 = f + k * 3.1415927f * 2.0f / 8.0f + 1.2566371f;
                    this.spawnFangs(EntityEvoker.this.posX + MathHelper.cos(f3) * 2.5, EntityEvoker.this.posZ + MathHelper.sin(f3) * 2.5, d0, d2, f3, 3);
                }
            }
            else {
                for (int l = 0; l < 16; ++l) {
                    final double d3 = 1.25 * (l + 1);
                    final int j = 1 * l;
                    this.spawnFangs(EntityEvoker.this.posX + MathHelper.cos(f) * d3, EntityEvoker.this.posZ + MathHelper.sin(f) * d3, d0, d2, f, j);
                }
            }
        }
        
        private void spawnFangs(final double p_190876_1_, final double p_190876_3_, final double p_190876_5_, final double p_190876_7_, final float p_190876_9_, final int p_190876_10_) {
            BlockPos blockpos = new BlockPos(p_190876_1_, p_190876_7_, p_190876_3_);
            boolean flag = false;
            double d0 = 0.0;
            while (true) {
                while (EntityEvoker.this.world.isBlockNormalCube(blockpos, true) || !EntityEvoker.this.world.isBlockNormalCube(blockpos.down(), true)) {
                    blockpos = blockpos.down();
                    if (blockpos.getY() < MathHelper.floor(p_190876_5_) - 1) {
                        if (flag) {
                            final EntityEvokerFangs entityevokerfangs = new EntityEvokerFangs(EntityEvoker.this.world, p_190876_1_, blockpos.getY() + d0, p_190876_3_, p_190876_9_, p_190876_10_, EntityEvoker.this);
                            EntityEvoker.this.world.spawnEntity(entityevokerfangs);
                        }
                        return;
                    }
                }
                if (!EntityEvoker.this.world.isAirBlock(blockpos)) {
                    final IBlockState iblockstate = EntityEvoker.this.world.getBlockState(blockpos);
                    final AxisAlignedBB axisalignedbb = iblockstate.getCollisionBoundingBox(EntityEvoker.this.world, blockpos);
                    if (axisalignedbb != null) {
                        d0 = axisalignedbb.maxY;
                    }
                }
                flag = true;
                continue;
            }
        }
        
        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOCATION_ILLAGER_PREPARE_ATTACK;
        }
        
        @Override
        protected SpellType getSpellType() {
            return SpellType.FANGS;
        }
    }
    
    class AICastingSpell extends AICastingApell
    {
        private AICastingSpell() {
        }
        
        @Override
        public void updateTask() {
            if (EntityEvoker.this.getAttackTarget() != null) {
                EntityEvoker.this.getLookHelper().setLookPositionWithEntity(EntityEvoker.this.getAttackTarget(), (float)EntityEvoker.this.getHorizontalFaceSpeed(), (float)EntityEvoker.this.getVerticalFaceSpeed());
            }
            else if (EntityEvoker.this.getWololoTarget() != null) {
                EntityEvoker.this.getLookHelper().setLookPositionWithEntity(EntityEvoker.this.getWololoTarget(), (float)EntityEvoker.this.getHorizontalFaceSpeed(), (float)EntityEvoker.this.getVerticalFaceSpeed());
            }
        }
    }
    
    class AISummonSpell extends AIUseSpell
    {
        private AISummonSpell() {
        }
        
        @Override
        public boolean shouldExecute() {
            if (!super.shouldExecute()) {
                return false;
            }
            final int i = EntityEvoker.this.world.getEntitiesWithinAABB((Class<? extends Entity>)EntityVex.class, EntityEvoker.this.getEntityBoundingBox().grow(16.0)).size();
            return EntityEvoker.this.rand.nextInt(8) + 1 > i;
        }
        
        @Override
        protected int getCastingTime() {
            return 100;
        }
        
        @Override
        protected int getCastingInterval() {
            return 340;
        }
        
        @Override
        protected void castSpell() {
            for (int i = 0; i < 3; ++i) {
                final BlockPos blockpos = new BlockPos(EntityEvoker.this).add(-2 + EntityEvoker.this.rand.nextInt(5), 1, -2 + EntityEvoker.this.rand.nextInt(5));
                final EntityVex entityvex = new EntityVex(EntityEvoker.this.world);
                entityvex.moveToBlockPosAndAngles(blockpos, 0.0f, 0.0f);
                entityvex.onInitialSpawn(EntityEvoker.this.world.getDifficultyForLocation(blockpos), null);
                entityvex.setOwner(EntityEvoker.this);
                entityvex.setBoundOrigin(blockpos);
                entityvex.setLimitedLife(20 * (30 + EntityEvoker.this.rand.nextInt(90)));
                EntityEvoker.this.world.spawnEntity(entityvex);
            }
        }
        
        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOCATION_ILLAGER_PREPARE_SUMMON;
        }
        
        @Override
        protected SpellType getSpellType() {
            return SpellType.SUMMON_VEX;
        }
    }
    
    public class AIWololoSpell extends AIUseSpell
    {
        final Predicate<EntitySheep> wololoSelector;
        
        public AIWololoSpell() {
            this.wololoSelector = (Predicate<EntitySheep>)new Predicate<EntitySheep>() {
                public boolean apply(final EntitySheep p_apply_1_) {
                    return p_apply_1_.getFleeceColor() == EnumDyeColor.BLUE;
                }
            };
        }
        
        @Override
        public boolean shouldExecute() {
            if (EntityEvoker.this.getAttackTarget() != null) {
                return false;
            }
            if (EntityEvoker.this.isSpellcasting()) {
                return false;
            }
            if (EntityEvoker.this.ticksExisted < this.spellCooldown) {
                return false;
            }
            if (!EntityEvoker.this.world.getGameRules().getBoolean("mobGriefing")) {
                return false;
            }
            final List<EntitySheep> list = EntityEvoker.this.world.getEntitiesWithinAABB((Class<? extends EntitySheep>)EntitySheep.class, EntityEvoker.this.getEntityBoundingBox().grow(16.0, 4.0, 16.0), (com.google.common.base.Predicate<? super EntitySheep>)this.wololoSelector);
            if (list.isEmpty()) {
                return false;
            }
            EntityEvoker.this.setWololoTarget(list.get(EntityEvoker.this.rand.nextInt(list.size())));
            return true;
        }
        
        @Override
        public boolean shouldContinueExecuting() {
            return EntityEvoker.this.getWololoTarget() != null && this.spellWarmup > 0;
        }
        
        @Override
        public void resetTask() {
            super.resetTask();
            EntityEvoker.this.setWololoTarget(null);
        }
        
        @Override
        protected void castSpell() {
            final EntitySheep entitysheep = EntityEvoker.this.getWololoTarget();
            if (entitysheep != null && entitysheep.isEntityAlive()) {
                entitysheep.setFleeceColor(EnumDyeColor.RED);
            }
        }
        
        @Override
        protected int getCastWarmupTime() {
            return 40;
        }
        
        @Override
        protected int getCastingTime() {
            return 60;
        }
        
        @Override
        protected int getCastingInterval() {
            return 140;
        }
        
        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOCATION_ILLAGER_PREPARE_WOLOLO;
        }
        
        @Override
        protected SpellType getSpellType() {
            return SpellType.WOLOLO;
        }
    }
}
