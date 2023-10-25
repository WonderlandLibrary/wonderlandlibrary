/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.monster;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntitySpider
extends EntityMob {
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.worldObj.isRemote) {
            this.setBesideClimbableBlock(this.isCollidedHorizontally);
        }
    }

    @Override
    protected String getDeathSound() {
        return "mob.spider.death";
    }

    @Override
    public boolean isOnLadder() {
        return this.isBesideClimbableBlock();
    }

    @Override
    protected String getHurtSound() {
        return "mob.spider.say";
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte(0));
    }

    public EntitySpider(World world) {
        super(world);
        this.setSize(1.4f, 0.9f);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4f));
        this.tasks.addTask(4, new AISpiderAttack(this, EntityPlayer.class));
        this.tasks.addTask(4, new AISpiderAttack(this, EntityIronGolem.class));
        this.tasks.addTask(5, new EntityAIWander(this, 0.8));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget((EntityCreature)this, false, new Class[0]));
        this.targetTasks.addTask(2, new AISpiderTarget<EntityPlayer>(this, EntityPlayer.class));
        this.targetTasks.addTask(3, new AISpiderTarget<EntityIronGolem>(this, EntityIronGolem.class));
    }

    public boolean isBesideClimbableBlock() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.ARTHROPOD;
    }

    @Override
    protected Item getDropItem() {
        return Items.string;
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficultyInstance, IEntityLivingData iEntityLivingData) {
        int n;
        iEntityLivingData = super.onInitialSpawn(difficultyInstance, iEntityLivingData);
        if (this.worldObj.rand.nextInt(100) == 0) {
            EntitySkeleton entitySkeleton = new EntitySkeleton(this.worldObj);
            entitySkeleton.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0f);
            entitySkeleton.onInitialSpawn(difficultyInstance, null);
            this.worldObj.spawnEntityInWorld(entitySkeleton);
            entitySkeleton.mountEntity(this);
        }
        if (iEntityLivingData == null) {
            iEntityLivingData = new GroupData();
            if (this.worldObj.getDifficulty() == EnumDifficulty.HARD && this.worldObj.rand.nextFloat() < 0.1f * difficultyInstance.getClampedAdditionalDifficulty()) {
                ((GroupData)iEntityLivingData).func_111104_a(this.worldObj.rand);
            }
        }
        if (iEntityLivingData instanceof GroupData && (n = ((GroupData)iEntityLivingData).potionEffectId) > 0 && Potion.potionTypes[n] != null) {
            this.addPotionEffect(new PotionEffect(n, Integer.MAX_VALUE));
        }
        return iEntityLivingData;
    }

    public void setBesideClimbableBlock(boolean bl) {
        byte by = this.dataWatcher.getWatchableObjectByte(16);
        by = bl ? (byte)(by | 1) : (byte)(by & 0xFFFFFFFE);
        this.dataWatcher.updateObject(16, by);
    }

    @Override
    protected void dropFewItems(boolean bl, int n) {
        super.dropFewItems(bl, n);
        if (bl && (this.rand.nextInt(3) == 0 || this.rand.nextInt(1 + n) > 0)) {
            this.dropItem(Items.spider_eye, 1);
        }
    }

    @Override
    public float getEyeHeight() {
        return 0.65f;
    }

    @Override
    protected void playStepSound(BlockPos blockPos, Block block) {
        this.playSound("mob.spider.step", 0.15f, 1.0f);
    }

    @Override
    protected String getLivingSound() {
        return "mob.spider.say";
    }

    @Override
    public void setInWeb() {
    }

    @Override
    protected PathNavigate getNewNavigator(World world) {
        return new PathNavigateClimber(this, world);
    }

    @Override
    public double getMountedYOffset() {
        return this.height * 0.5f;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(16.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3f);
    }

    @Override
    public boolean isPotionApplicable(PotionEffect potionEffect) {
        return potionEffect.getPotionID() == Potion.poison.id ? false : super.isPotionApplicable(potionEffect);
    }

    public static class GroupData
    implements IEntityLivingData {
        public int potionEffectId;

        public void func_111104_a(Random random) {
            int n = random.nextInt(5);
            if (n <= 1) {
                this.potionEffectId = Potion.moveSpeed.id;
            } else if (n <= 2) {
                this.potionEffectId = Potion.damageBoost.id;
            } else if (n <= 3) {
                this.potionEffectId = Potion.regeneration.id;
            } else if (n <= 4) {
                this.potionEffectId = Potion.invisibility.id;
            }
        }
    }

    static class AISpiderTarget<T extends EntityLivingBase>
    extends EntityAINearestAttackableTarget {
        public AISpiderTarget(EntitySpider entitySpider, Class<T> clazz) {
            super((EntityCreature)entitySpider, clazz, true);
        }

        @Override
        public boolean shouldExecute() {
            float f = this.taskOwner.getBrightness(1.0f);
            return f >= 0.5f ? false : super.shouldExecute();
        }
    }

    static class AISpiderAttack
    extends EntityAIAttackOnCollide {
        public AISpiderAttack(EntitySpider entitySpider, Class<? extends Entity> clazz) {
            super(entitySpider, clazz, 1.0, true);
        }

        @Override
        protected double func_179512_a(EntityLivingBase entityLivingBase) {
            return 4.0f + entityLivingBase.width;
        }

        @Override
        public boolean continueExecuting() {
            float f = this.attacker.getBrightness(1.0f);
            if (f >= 0.5f && this.attacker.getRNG().nextInt(100) == 0) {
                this.attacker.setAttackTarget(null);
                return false;
            }
            return super.continueExecuting();
        }
    }
}

