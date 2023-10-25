/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 */
package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIDefendVillage;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookAtVillager;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.world.World;

public class EntityIronGolem
extends EntityGolem {
    Village villageObj;
    private int homeCheckTimer;
    private int attackTimer;
    private int holdRoseTick;

    @Override
    protected void collideWithEntity(Entity entity) {
        if (entity instanceof IMob && !(entity instanceof EntityCreeper) && this.getRNG().nextInt(20) == 0) {
            this.setAttackTarget((EntityLivingBase)entity);
        }
        super.collideWithEntity(entity);
    }

    public boolean isPlayerCreated() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    @Override
    public void onLivingUpdate() {
        int n;
        int n2;
        int n3;
        IBlockState iBlockState;
        Block block;
        super.onLivingUpdate();
        if (this.attackTimer > 0) {
            --this.attackTimer;
        }
        if (this.holdRoseTick > 0) {
            --this.holdRoseTick;
        }
        if (this.motionX * this.motionX + this.motionZ * this.motionZ > 2.500000277905201E-7 && this.rand.nextInt(5) == 0 && (block = (iBlockState = this.worldObj.getBlockState(new BlockPos(n3 = MathHelper.floor_double(this.posX), n2 = MathHelper.floor_double(this.posY - (double)0.2f), n = MathHelper.floor_double(this.posZ)))).getBlock()).getMaterial() != Material.air) {
            this.worldObj.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + ((double)this.rand.nextFloat() - 0.5) * (double)this.width, this.getEntityBoundingBox().minY + 0.1, this.posZ + ((double)this.rand.nextFloat() - 0.5) * (double)this.width, 4.0 * ((double)this.rand.nextFloat() - 0.5), 0.5, ((double)this.rand.nextFloat() - 0.5) * 4.0, Block.getStateId(iBlockState));
        }
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25);
    }

    @Override
    protected String getDeathSound() {
        return "mob.irongolem.death";
    }

    @Override
    protected int decreaseAirSupply(int n) {
        return n;
    }

    @Override
    protected String getHurtSound() {
        return "mob.irongolem.hit";
    }

    public int getAttackTimer() {
        return this.attackTimer;
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        this.attackTimer = 10;
        this.worldObj.setEntityState(this, (byte)4);
        boolean bl = entity.attackEntityFrom(DamageSource.causeMobDamage(this), 7 + this.rand.nextInt(15));
        if (bl) {
            entity.motionY += (double)0.4f;
            this.applyEnchantments(this, entity);
        }
        this.playSound("mob.irongolem.throw", 1.0f, 1.0f);
        return bl;
    }

    @Override
    public void handleStatusUpdate(byte by) {
        if (by == 4) {
            this.attackTimer = 10;
            this.playSound("mob.irongolem.throw", 1.0f, 1.0f);
        } else if (by == 11) {
            this.holdRoseTick = 400;
        } else {
            super.handleStatusUpdate(by);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        super.readEntityFromNBT(nBTTagCompound);
        this.setPlayerCreated(nBTTagCompound.getBoolean("PlayerCreated"));
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        if (!this.isPlayerCreated() && this.attackingPlayer != null && this.villageObj != null) {
            this.villageObj.setReputationForPlayer(this.attackingPlayer.getName(), -5);
        }
        super.onDeath(damageSource);
    }

    @Override
    protected void dropFewItems(boolean bl, int n) {
        int n2 = this.rand.nextInt(3);
        int n3 = 0;
        while (n3 < n2) {
            this.dropItemWithOffset(Item.getItemFromBlock(Blocks.red_flower), 1, BlockFlower.EnumFlowerType.POPPY.getMeta());
            ++n3;
        }
        n3 = 3 + this.rand.nextInt(3);
        int n4 = 0;
        while (n4 < n3) {
            this.dropItem(Items.iron_ingot, 1);
            ++n4;
        }
    }

    public EntityIronGolem(World world) {
        super(world);
        this.setSize(1.4f, 2.9f);
        ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
        this.tasks.addTask(1, new EntityAIAttackOnCollide(this, 1.0, true));
        this.tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 0.9, 32.0f));
        this.tasks.addTask(3, new EntityAIMoveThroughVillage(this, 0.6, true));
        this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0));
        this.tasks.addTask(5, new EntityAILookAtVillager(this));
        this.tasks.addTask(6, new EntityAIWander(this, 0.6));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0f));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIDefendVillage(this));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget((EntityCreature)this, false, new Class[0]));
        this.targetTasks.addTask(3, new AINearestAttackableTargetNonCreeper<Entity>(this, EntityLiving.class, 10, false, true, IMob.VISIBLE_MOB_SELECTOR));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, (byte)0);
    }

    public Village getVillage() {
        return this.villageObj;
    }

    public int getHoldRoseTick() {
        return this.holdRoseTick;
    }

    public void setHoldingRose(boolean bl) {
        this.holdRoseTick = bl ? 400 : 0;
        this.worldObj.setEntityState(this, (byte)11);
    }

    public void setPlayerCreated(boolean bl) {
        byte by = this.dataWatcher.getWatchableObjectByte(16);
        if (bl) {
            this.dataWatcher.updateObject(16, (byte)(by | 1));
        } else {
            this.dataWatcher.updateObject(16, (byte)(by & 0xFFFFFFFE));
        }
    }

    @Override
    public boolean canAttackClass(Class<? extends EntityLivingBase> clazz) {
        return this.isPlayerCreated() && EntityPlayer.class.isAssignableFrom(clazz) ? false : (clazz == EntityCreeper.class ? false : super.canAttackClass(clazz));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        nBTTagCompound.setBoolean("PlayerCreated", this.isPlayerCreated());
    }

    @Override
    protected void playStepSound(BlockPos blockPos, Block block) {
        this.playSound("mob.irongolem.walk", 1.0f, 1.0f);
    }

    @Override
    protected void updateAITasks() {
        if (--this.homeCheckTimer <= 0) {
            this.homeCheckTimer = 70 + this.rand.nextInt(50);
            this.villageObj = this.worldObj.getVillageCollection().getNearestVillage(new BlockPos(this), 32);
            if (this.villageObj == null) {
                this.detachHome();
            } else {
                BlockPos blockPos = this.villageObj.getCenter();
                this.setHomePosAndDistance(blockPos, (int)((float)this.villageObj.getVillageRadius() * 0.6f));
            }
        }
        super.updateAITasks();
    }

    static class AINearestAttackableTargetNonCreeper<T extends EntityLivingBase>
    extends EntityAINearestAttackableTarget<T> {
        public AINearestAttackableTargetNonCreeper(final EntityCreature entityCreature, Class<T> clazz, int n, boolean bl, boolean bl2, final Predicate<? super T> predicate) {
            super(entityCreature, clazz, n, bl, bl2, predicate);
            this.targetEntitySelector = new Predicate<T>(){

                public boolean apply(T t) {
                    if (predicate != null && !predicate.apply(t)) {
                        return false;
                    }
                    if (t instanceof EntityCreeper) {
                        return false;
                    }
                    if (t instanceof EntityPlayer) {
                        double d = AINearestAttackableTargetNonCreeper.this.getTargetDistance();
                        if (((Entity)t).isSneaking()) {
                            d *= (double)0.8f;
                        }
                        if (((Entity)t).isInvisible()) {
                            float f = ((EntityPlayer)t).getArmorVisibility();
                            if (f < 0.1f) {
                                f = 0.1f;
                            }
                            d *= (double)(0.7f * f);
                        }
                        if ((double)((Entity)t).getDistanceToEntity(entityCreature) > d) {
                            return false;
                        }
                    }
                    return AINearestAttackableTargetNonCreeper.this.isSuitableTarget(t, false);
                }
            };
        }
    }
}

