/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySpellcasterIllager;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityEvoker
extends EntitySpellcasterIllager {
    private EntitySheep field_190763_bw;

    public EntityEvoker(World p_i47287_1_) {
        super(p_i47287_1_);
        this.setSize(0.6f, 1.95f);
        this.experienceValue = 10;
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new AICastingSpell());
        this.tasks.addTask(2, new EntityAIAvoidEntity<EntityPlayer>(this, EntityPlayer.class, 8.0f, 0.6, 1.0));
        this.tasks.addTask(4, new AISummonSpell());
        this.tasks.addTask(5, new AIAttackSpell());
        this.tasks.addTask(6, new AIWololoSpell());
        this.tasks.addTask(8, new EntityAIWander(this, 0.6));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0f, 1.0f));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0f));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget((EntityCreature)this, true, EntityEvoker.class));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityPlayer>((EntityCreature)this, EntityPlayer.class, true).func_190882_b(300));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityVillager>((EntityCreature)this, EntityVillager.class, false).func_190882_b(300));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityIronGolem>((EntityCreature)this, EntityIronGolem.class, false));
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

    public static void func_190759_b(DataFixer p_190759_0_) {
        EntityLiving.registerFixesMob(p_190759_0_, EntityEvoker.class);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
    }

    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.field_191185_au;
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
    public boolean isOnSameTeam(Entity entityIn) {
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
            return this.isOnSameTeam(((EntityVex)entityIn).func_190645_o());
        }
        if (entityIn instanceof EntityLivingBase && ((EntityLivingBase)entityIn).getCreatureAttribute() == EnumCreatureAttribute.ILLAGER) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        }
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.field_191243_bm;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.field_191245_bo;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.field_191246_bp;
    }

    private void func_190748_a(@Nullable EntitySheep p_190748_1_) {
        this.field_190763_bw = p_190748_1_;
    }

    @Nullable
    private EntitySheep func_190751_dj() {
        return this.field_190763_bw;
    }

    @Override
    protected SoundEvent func_193086_dk() {
        return SoundEvents.field_191244_bn;
    }

    public class AIWololoSpell
    extends EntitySpellcasterIllager.AIUseSpell {
        final Predicate<EntitySheep> field_190879_a;

        public AIWololoSpell() {
            super(EntityEvoker.this);
            this.field_190879_a = new Predicate<EntitySheep>(){

                @Override
                public boolean apply(EntitySheep p_apply_1_) {
                    return p_apply_1_.getFleeceColor() == EnumDyeColor.BLUE;
                }
            };
        }

        @Override
        public boolean shouldExecute() {
            if (EntityEvoker.this.getAttackTarget() != null) {
                return false;
            }
            if (EntityEvoker.this.func_193082_dl()) {
                return false;
            }
            if (EntityEvoker.this.ticksExisted < this.field_193322_d) {
                return false;
            }
            if (!EntityEvoker.this.world.getGameRules().getBoolean("mobGriefing")) {
                return false;
            }
            List<EntitySheep> list = EntityEvoker.this.world.getEntitiesWithinAABB(EntitySheep.class, EntityEvoker.this.getEntityBoundingBox().expand(16.0, 4.0, 16.0), this.field_190879_a);
            if (list.isEmpty()) {
                return false;
            }
            EntityEvoker.this.func_190748_a(list.get(EntityEvoker.this.rand.nextInt(list.size())));
            return true;
        }

        @Override
        public boolean continueExecuting() {
            return EntityEvoker.this.func_190751_dj() != null && this.field_193321_c > 0;
        }

        @Override
        public void resetTask() {
            super.resetTask();
            EntityEvoker.this.func_190748_a(null);
        }

        @Override
        protected void func_190868_j() {
            EntitySheep entitysheep = EntityEvoker.this.func_190751_dj();
            if (entitysheep != null && entitysheep.isEntityAlive()) {
                entitysheep.setFleeceColor(EnumDyeColor.RED);
            }
        }

        @Override
        protected int func_190867_m() {
            return 40;
        }

        @Override
        protected int func_190869_f() {
            return 60;
        }

        @Override
        protected int func_190872_i() {
            return 140;
        }

        @Override
        protected SoundEvent func_190871_k() {
            return SoundEvents.field_191249_bs;
        }

        @Override
        protected EntitySpellcasterIllager.SpellType func_193320_l() {
            return EntitySpellcasterIllager.SpellType.WOLOLO;
        }
    }

    class AISummonSpell
    extends EntitySpellcasterIllager.AIUseSpell {
        private AISummonSpell() {
            super(EntityEvoker.this);
        }

        @Override
        public boolean shouldExecute() {
            if (!super.shouldExecute()) {
                return false;
            }
            int i = EntityEvoker.this.world.getEntitiesWithinAABB(EntityVex.class, EntityEvoker.this.getEntityBoundingBox().expandXyz(16.0)).size();
            return EntityEvoker.this.rand.nextInt(8) + 1 > i;
        }

        @Override
        protected int func_190869_f() {
            return 100;
        }

        @Override
        protected int func_190872_i() {
            return 340;
        }

        @Override
        protected void func_190868_j() {
            for (int i = 0; i < 3; ++i) {
                BlockPos blockpos = new BlockPos(EntityEvoker.this).add(-2 + EntityEvoker.this.rand.nextInt(5), 1, -2 + EntityEvoker.this.rand.nextInt(5));
                EntityVex entityvex = new EntityVex(EntityEvoker.this.world);
                entityvex.moveToBlockPosAndAngles(blockpos, 0.0f, 0.0f);
                entityvex.onInitialSpawn(EntityEvoker.this.world.getDifficultyForLocation(blockpos), null);
                entityvex.func_190658_a(EntityEvoker.this);
                entityvex.func_190651_g(blockpos);
                entityvex.func_190653_a(20 * (30 + EntityEvoker.this.rand.nextInt(90)));
                EntityEvoker.this.world.spawnEntityInWorld(entityvex);
            }
        }

        @Override
        protected SoundEvent func_190871_k() {
            return SoundEvents.field_191248_br;
        }

        @Override
        protected EntitySpellcasterIllager.SpellType func_193320_l() {
            return EntitySpellcasterIllager.SpellType.SUMMON_VEX;
        }
    }

    class AICastingSpell
    extends EntitySpellcasterIllager.AICastingApell {
        private AICastingSpell() {
            super(EntityEvoker.this);
        }

        @Override
        public void updateTask() {
            if (EntityEvoker.this.getAttackTarget() != null) {
                EntityEvoker.this.getLookHelper().setLookPositionWithEntity(EntityEvoker.this.getAttackTarget(), EntityEvoker.this.getHorizontalFaceSpeed(), EntityEvoker.this.getVerticalFaceSpeed());
            } else if (EntityEvoker.this.func_190751_dj() != null) {
                EntityEvoker.this.getLookHelper().setLookPositionWithEntity(EntityEvoker.this.func_190751_dj(), EntityEvoker.this.getHorizontalFaceSpeed(), EntityEvoker.this.getVerticalFaceSpeed());
            }
        }
    }

    class AIAttackSpell
    extends EntitySpellcasterIllager.AIUseSpell {
        private AIAttackSpell() {
            super(EntityEvoker.this);
        }

        @Override
        protected int func_190869_f() {
            return 40;
        }

        @Override
        protected int func_190872_i() {
            return 100;
        }

        @Override
        protected void func_190868_j() {
            EntityLivingBase entitylivingbase = EntityEvoker.this.getAttackTarget();
            double d0 = Math.min(entitylivingbase.posY, EntityEvoker.this.posY);
            double d1 = Math.max(entitylivingbase.posY, EntityEvoker.this.posY) + 1.0;
            float f = (float)MathHelper.atan2(entitylivingbase.posZ - EntityEvoker.this.posZ, entitylivingbase.posX - EntityEvoker.this.posX);
            if (EntityEvoker.this.getDistanceSqToEntity(entitylivingbase) < 9.0) {
                for (int i = 0; i < 5; ++i) {
                    float f1 = f + (float)i * (float)Math.PI * 0.4f;
                    this.func_190876_a(EntityEvoker.this.posX + (double)MathHelper.cos(f1) * 1.5, EntityEvoker.this.posZ + (double)MathHelper.sin(f1) * 1.5, d0, d1, f1, 0);
                }
                for (int k = 0; k < 8; ++k) {
                    float f2 = f + (float)k * (float)Math.PI * 2.0f / 8.0f + 1.2566371f;
                    this.func_190876_a(EntityEvoker.this.posX + (double)MathHelper.cos(f2) * 2.5, EntityEvoker.this.posZ + (double)MathHelper.sin(f2) * 2.5, d0, d1, f2, 3);
                }
            } else {
                for (int l = 0; l < 16; ++l) {
                    double d2 = 1.25 * (double)(l + 1);
                    int j = 1 * l;
                    this.func_190876_a(EntityEvoker.this.posX + (double)MathHelper.cos(f) * d2, EntityEvoker.this.posZ + (double)MathHelper.sin(f) * d2, d0, d1, f, j);
                }
            }
        }

        private void func_190876_a(double p_190876_1_, double p_190876_3_, double p_190876_5_, double p_190876_7_, float p_190876_9_, int p_190876_10_) {
            BlockPos blockpos = new BlockPos(p_190876_1_, p_190876_7_, p_190876_3_);
            boolean flag = false;
            double d0 = 0.0;
            do {
                IBlockState iblockstate;
                AxisAlignedBB axisalignedbb;
                if (EntityEvoker.this.world.isBlockNormalCube(blockpos, true) || !EntityEvoker.this.world.isBlockNormalCube(blockpos.down(), true)) continue;
                if (!EntityEvoker.this.world.isAirBlock(blockpos) && (axisalignedbb = (iblockstate = EntityEvoker.this.world.getBlockState(blockpos)).getCollisionBoundingBox(EntityEvoker.this.world, blockpos)) != null) {
                    d0 = axisalignedbb.maxY;
                }
                flag = true;
                break;
            } while ((blockpos = blockpos.down()).getY() >= MathHelper.floor(p_190876_5_) - 1);
            if (flag) {
                EntityEvokerFangs entityevokerfangs = new EntityEvokerFangs(EntityEvoker.this.world, p_190876_1_, (double)blockpos.getY() + d0, p_190876_3_, p_190876_9_, p_190876_10_, EntityEvoker.this);
                EntityEvoker.this.world.spawnEntityInWorld(entityevokerfangs);
            }
        }

        @Override
        protected SoundEvent func_190871_k() {
            return SoundEvents.field_191247_bq;
        }

        @Override
        protected EntitySpellcasterIllager.SpellType func_193320_l() {
            return EntitySpellcasterIllager.SpellType.FANGS;
        }
    }
}

