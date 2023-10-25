/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.monster;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntitySilverfish
extends EntityMob {
    private AISummonSilverfish summonSilverfish;

    public EntitySilverfish(World worldIn) {
        super(worldIn);
        this.setSize(0.4f, 0.3f);
    }

    public static void registerFixesSilverfish(DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntitySilverfish.class);
    }

    @Override
    protected void initEntityAI() {
        this.summonSilverfish = new AISummonSilverfish(this);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(3, this.summonSilverfish);
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0, false));
        this.tasks.addTask(5, new AIHideInStone(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget((EntityCreature)this, true, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityPlayer>((EntityCreature)this, EntityPlayer.class, true));
    }

    @Override
    public double getYOffset() {
        return 0.1;
    }

    @Override
    public float getEyeHeight() {
        return 0.1f;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0);
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SILVERFISH_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_SILVERFISH_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SILVERFISH_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(SoundEvents.ENTITY_SILVERFISH_STEP, 0.15f, 1.0f);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if ((source instanceof EntityDamageSource || source == DamageSource.magic) && this.summonSilverfish != null) {
            this.summonSilverfish.notifyHurt();
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_SILVERFISH;
    }

    @Override
    public void onUpdate() {
        this.renderYawOffset = this.rotationYaw;
        super.onUpdate();
    }

    @Override
    public void setRenderYawOffset(float offset) {
        this.rotationYaw = offset;
        super.setRenderYawOffset(offset);
    }

    @Override
    public float getBlockPathWeight(BlockPos pos) {
        return this.world.getBlockState(pos.down()).getBlock() == Blocks.STONE ? 10.0f : super.getBlockPathWeight(pos);
    }

    @Override
    protected boolean isValidLightLevel() {
        return true;
    }

    @Override
    public boolean getCanSpawnHere() {
        if (super.getCanSpawnHere()) {
            EntityPlayer entityplayer = this.world.getNearestPlayerNotCreative(this, 5.0);
            return entityplayer == null;
        }
        return false;
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.ARTHROPOD;
    }

    static class AISummonSilverfish
    extends EntityAIBase {
        private final EntitySilverfish silverfish;
        private int lookForFriends;

        public AISummonSilverfish(EntitySilverfish silverfishIn) {
            this.silverfish = silverfishIn;
        }

        public void notifyHurt() {
            if (this.lookForFriends == 0) {
                this.lookForFriends = 20;
            }
        }

        @Override
        public boolean shouldExecute() {
            return this.lookForFriends > 0;
        }

        @Override
        public void updateTask() {
            --this.lookForFriends;
            if (this.lookForFriends <= 0) {
                World world = this.silverfish.world;
                Random random = this.silverfish.getRNG();
                BlockPos blockpos = new BlockPos(this.silverfish);
                int i = 0;
                while (i <= 5 && i >= -5) {
                    int j = 0;
                    while (j <= 10 && j >= -10) {
                        int k = 0;
                        while (k <= 10 && k >= -10) {
                            BlockPos blockpos1 = blockpos.add(j, i, k);
                            IBlockState iblockstate = world.getBlockState(blockpos1);
                            if (iblockstate.getBlock() == Blocks.MONSTER_EGG) {
                                if (world.getGameRules().getBoolean("mobGriefing")) {
                                    world.destroyBlock(blockpos1, true);
                                } else {
                                    world.setBlockState(blockpos1, iblockstate.getValue(BlockSilverfish.VARIANT).getModelBlock(), 3);
                                }
                                if (random.nextBoolean()) {
                                    return;
                                }
                            }
                            k = (k <= 0 ? 1 : 0) - k;
                        }
                        j = (j <= 0 ? 1 : 0) - j;
                    }
                    i = (i <= 0 ? 1 : 0) - i;
                }
            }
        }
    }

    static class AIHideInStone
    extends EntityAIWander {
        private EnumFacing facing;
        private boolean doMerge;

        public AIHideInStone(EntitySilverfish silverfishIn) {
            super(silverfishIn, 1.0, 10);
            this.setMutexBits(1);
        }

        @Override
        public boolean shouldExecute() {
            if (this.entity.getAttackTarget() != null) {
                return false;
            }
            if (!this.entity.getNavigator().noPath()) {
                return false;
            }
            Random random = this.entity.getRNG();
            if (this.entity.world.getGameRules().getBoolean("mobGriefing") && random.nextInt(10) == 0) {
                this.facing = EnumFacing.random(random);
                BlockPos blockpos = new BlockPos(this.entity.posX, this.entity.posY + 0.5, this.entity.posZ).offset(this.facing);
                IBlockState iblockstate = this.entity.world.getBlockState(blockpos);
                if (BlockSilverfish.canContainSilverfish(iblockstate)) {
                    this.doMerge = true;
                    return true;
                }
            }
            this.doMerge = false;
            return super.shouldExecute();
        }

        @Override
        public boolean continueExecuting() {
            return this.doMerge ? false : super.continueExecuting();
        }

        @Override
        public void startExecuting() {
            if (!this.doMerge) {
                super.startExecuting();
            } else {
                World world = this.entity.world;
                BlockPos blockpos = new BlockPos(this.entity.posX, this.entity.posY + 0.5, this.entity.posZ).offset(this.facing);
                IBlockState iblockstate = world.getBlockState(blockpos);
                if (BlockSilverfish.canContainSilverfish(iblockstate)) {
                    world.setBlockState(blockpos, Blocks.MONSTER_EGG.getDefaultState().withProperty(BlockSilverfish.VARIANT, BlockSilverfish.EnumType.forModelBlock(iblockstate)), 3);
                    this.entity.spawnExplosionParticle();
                    this.entity.setDead();
                }
            }
        }
    }
}

