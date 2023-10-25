/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.base.Predicates
 */
package net.minecraft.entity.boss;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityWither
extends EntityMob
implements IRangedAttackMob,
IBossDisplayData {
    private static final Predicate<Entity> attackEntitySelector = new Predicate<Entity>(){

        public boolean apply(Entity entity) {
            return entity instanceof EntityLivingBase && ((EntityLivingBase)entity).getCreatureAttribute() != EnumCreatureAttribute.UNDEAD;
        }
    };
    private float[] field_82217_f;
    private int[] field_82224_i;
    private int blockBreakCounter;
    private int[] field_82223_h;
    private float[] field_82218_g;
    private float[] field_82221_e;
    private float[] field_82220_d = new float[2];

    @Override
    protected String getLivingSound() {
        return "mob.wither.idle";
    }

    public boolean isArmored() {
        return this.getHealth() <= this.getMaxHealth() / 2.0f;
    }

    private void launchWitherSkullToEntity(int n, EntityLivingBase entityLivingBase) {
        this.launchWitherSkullToCoords(n, entityLivingBase.posX, entityLivingBase.posY + (double)entityLivingBase.getEyeHeight() * 0.5, entityLivingBase.posZ, n == 0 && this.rand.nextFloat() < 0.001f);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(17, new Integer(0));
        this.dataWatcher.addObject(18, new Integer(0));
        this.dataWatcher.addObject(19, new Integer(0));
        this.dataWatcher.addObject(20, new Integer(0));
    }

    @Override
    public void mountEntity(Entity entity) {
        this.ridingEntity = null;
    }

    @Override
    protected void despawnEntity() {
        this.entityAge = 0;
    }

    @Override
    public int getTotalArmorValue() {
        return 4;
    }

    @Override
    protected String getHurtSound() {
        return "mob.wither.hurt";
    }

    public void func_82206_m() {
        this.setInvulTime(220);
        this.setHealth(this.getMaxHealth() / 3.0f);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(300.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.6f);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(40.0);
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        if (this.isEntityInvulnerable(damageSource)) {
            return false;
        }
        if (damageSource != DamageSource.drown && !(damageSource.getEntity() instanceof EntityWither)) {
            Entity entity;
            if (this.getInvulTime() > 0 && damageSource != DamageSource.outOfWorld) {
                return false;
            }
            if (this.isArmored() && (entity = damageSource.getSourceOfDamage()) instanceof EntityArrow) {
                return false;
            }
            entity = damageSource.getEntity();
            if (entity != null && !(entity instanceof EntityPlayer) && entity instanceof EntityLivingBase && ((EntityLivingBase)entity).getCreatureAttribute() == this.getCreatureAttribute()) {
                return false;
            }
            if (this.blockBreakCounter <= 0) {
                this.blockBreakCounter = 20;
            }
            int n = 0;
            while (n < this.field_82224_i.length) {
                int n2 = n++;
                this.field_82224_i[n2] = this.field_82224_i[n2] + 3;
            }
            return super.attackEntityFrom(damageSource, f);
        }
        return false;
    }

    @Override
    public void addPotionEffect(PotionEffect potionEffect) {
    }

    public int getInvulTime() {
        return this.dataWatcher.getWatchableObjectInt(20);
    }

    @Override
    protected void dropFewItems(boolean bl, int n) {
        EntityItem entityItem = this.dropItem(Items.nether_star, 1);
        if (entityItem != null) {
            entityItem.setNoDespawn();
        }
        if (!this.worldObj.isRemote) {
            for (EntityPlayer entityPlayer : this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().expand(50.0, 100.0, 50.0))) {
                entityPlayer.triggerAchievement(AchievementList.killWither);
            }
        }
    }

    private void launchWitherSkullToCoords(int n, double d, double d2, double d3, boolean bl) {
        this.worldObj.playAuxSFXAtEntity(null, 1014, new BlockPos(this), 0);
        double d4 = this.func_82214_u(n);
        double d5 = this.func_82208_v(n);
        double d6 = this.func_82213_w(n);
        double d7 = d - d4;
        double d8 = d2 - d5;
        double d9 = d3 - d6;
        EntityWitherSkull entityWitherSkull = new EntityWitherSkull(this.worldObj, this, d7, d8, d9);
        if (bl) {
            entityWitherSkull.setInvulnerable(true);
        }
        entityWitherSkull.posY = d5;
        entityWitherSkull.posX = d4;
        entityWitherSkull.posZ = d6;
        this.worldObj.spawnEntityInWorld(entityWitherSkull);
    }

    public static boolean func_181033_a(Block block) {
        return block != Blocks.bedrock && block != Blocks.end_portal && block != Blocks.end_portal_frame && block != Blocks.command_block && block != Blocks.barrier;
    }

    private float func_82204_b(float f, float f2, float f3) {
        float f4 = MathHelper.wrapAngleTo180_float(f2 - f);
        if (f4 > f3) {
            f4 = f3;
        }
        if (f4 < -f3) {
            f4 = -f3;
        }
        return f + f4;
    }

    public void setInvulTime(int n) {
        this.dataWatcher.updateObject(20, n);
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    @Override
    public int getBrightnessForRender(float f) {
        return 0xF000F0;
    }

    public int getWatchedTargetId(int n) {
        return this.dataWatcher.getWatchableObjectInt(17 + n);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        super.readEntityFromNBT(nBTTagCompound);
        this.setInvulTime(nBTTagCompound.getInteger("Invul"));
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase entityLivingBase, float f) {
        this.launchWitherSkullToEntity(0, entityLivingBase);
    }

    private double func_82213_w(int n) {
        if (n <= 0) {
            return this.posZ;
        }
        float f = (this.renderYawOffset + (float)(180 * (n - 1))) / 180.0f * (float)Math.PI;
        float f2 = MathHelper.sin(f);
        return this.posZ + (double)f2 * 1.3;
    }

    public float func_82207_a(int n) {
        return this.field_82221_e[n];
    }

    @Override
    protected String getDeathSound() {
        return "mob.wither.death";
    }

    @Override
    public void fall(float f, float f2) {
    }

    public void updateWatchedTargetId(int n, int n2) {
        this.dataWatcher.updateObject(17 + n, n2);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        nBTTagCompound.setInteger("Invul", this.getInvulTime());
    }

    @Override
    public void setInWeb() {
    }

    public float func_82210_r(int n) {
        return this.field_82220_d[n];
    }

    @Override
    protected void updateAITasks() {
        if (this.getInvulTime() > 0) {
            int n = this.getInvulTime() - 1;
            if (n <= 0) {
                this.worldObj.newExplosion(this, this.posX, this.posY + (double)this.getEyeHeight(), this.posZ, 7.0f, false, this.worldObj.getGameRules().getBoolean("mobGriefing"));
                this.worldObj.playBroadcastSound(1013, new BlockPos(this), 0);
            }
            this.setInvulTime(n);
            if (this.ticksExisted % 10 == 0) {
                this.heal(10.0f);
            }
        } else {
            int n;
            super.updateAITasks();
            int n2 = 1;
            while (n2 < 3) {
                if (this.ticksExisted >= this.field_82223_h[n2 - 1]) {
                    Object object;
                    this.field_82223_h[n2 - 1] = this.ticksExisted + 10 + this.rand.nextInt(10);
                    if (this.worldObj.getDifficulty() == EnumDifficulty.NORMAL || this.worldObj.getDifficulty() == EnumDifficulty.HARD) {
                        n = n2 - 1;
                        int n3 = this.field_82224_i[n2 - 1];
                        this.field_82224_i[n] = this.field_82224_i[n2 - 1] + 1;
                        if (n3 > 15) {
                            float f = 10.0f;
                            float f2 = 5.0f;
                            double d = MathHelper.getRandomDoubleInRange(this.rand, this.posX - (double)f, this.posX + (double)f);
                            double d2 = MathHelper.getRandomDoubleInRange(this.rand, this.posY - (double)f2, this.posY + (double)f2);
                            double d3 = MathHelper.getRandomDoubleInRange(this.rand, this.posZ - (double)f, this.posZ + (double)f);
                            this.launchWitherSkullToCoords(n2 + 1, d, d2, d3, true);
                            this.field_82224_i[n2 - 1] = 0;
                        }
                    }
                    if ((n = this.getWatchedTargetId(n2)) > 0) {
                        object = this.worldObj.getEntityByID(n);
                        if (object != null && ((Entity)object).isEntityAlive() && this.getDistanceSqToEntity((Entity)object) <= 900.0 && this.canEntityBeSeen((Entity)object)) {
                            if (object instanceof EntityPlayer && ((EntityPlayer)object).capabilities.disableDamage) {
                                this.updateWatchedTargetId(n2, 0);
                            } else {
                                this.launchWitherSkullToEntity(n2 + 1, (EntityLivingBase)object);
                                this.field_82223_h[n2 - 1] = this.ticksExisted + 40 + this.rand.nextInt(20);
                                this.field_82224_i[n2 - 1] = 0;
                            }
                        } else {
                            this.updateWatchedTargetId(n2, 0);
                        }
                    } else {
                        object = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().expand(20.0, 8.0, 20.0), Predicates.and(attackEntitySelector, EntitySelectors.NOT_SPECTATING));
                        int n4 = 0;
                        while (n4 < 10 && !object.isEmpty()) {
                            EntityLivingBase entityLivingBase = (EntityLivingBase)object.get(this.rand.nextInt(object.size()));
                            if (entityLivingBase != this && entityLivingBase.isEntityAlive() && this.canEntityBeSeen(entityLivingBase)) {
                                if (entityLivingBase instanceof EntityPlayer) {
                                    if (((EntityPlayer)entityLivingBase).capabilities.disableDamage) break;
                                    this.updateWatchedTargetId(n2, entityLivingBase.getEntityId());
                                    break;
                                }
                                this.updateWatchedTargetId(n2, entityLivingBase.getEntityId());
                                break;
                            }
                            object.remove(entityLivingBase);
                            ++n4;
                        }
                    }
                }
                ++n2;
            }
            if (this.getAttackTarget() != null) {
                this.updateWatchedTargetId(0, this.getAttackTarget().getEntityId());
            } else {
                this.updateWatchedTargetId(0, 0);
            }
            if (this.blockBreakCounter > 0) {
                --this.blockBreakCounter;
                if (this.blockBreakCounter == 0 && this.worldObj.getGameRules().getBoolean("mobGriefing")) {
                    n2 = MathHelper.floor_double(this.posY);
                    n = MathHelper.floor_double(this.posX);
                    int n5 = MathHelper.floor_double(this.posZ);
                    boolean bl = false;
                    int n6 = -1;
                    while (n6 <= 1) {
                        int n7 = -1;
                        while (n7 <= 1) {
                            int n8 = 0;
                            while (n8 <= 3) {
                                int n9 = n + n6;
                                int n10 = n2 + n8;
                                int n11 = n5 + n7;
                                BlockPos blockPos = new BlockPos(n9, n10, n11);
                                Block block = this.worldObj.getBlockState(blockPos).getBlock();
                                if (block.getMaterial() != Material.air && EntityWither.func_181033_a(block)) {
                                    bl = this.worldObj.destroyBlock(blockPos, true) || bl;
                                }
                                ++n8;
                            }
                            ++n7;
                        }
                        ++n6;
                    }
                    if (bl) {
                        this.worldObj.playAuxSFXAtEntity(null, 1012, new BlockPos(this), 0);
                    }
                }
            }
            if (this.ticksExisted % 20 == 0) {
                this.heal(1.0f);
            }
        }
    }

    private double func_82208_v(int n) {
        return n <= 0 ? this.posY + 3.0 : this.posY + 2.2;
    }

    @Override
    public void onLivingUpdate() {
        double d;
        double d2;
        double d3;
        Entity entity;
        this.motionY *= (double)0.6f;
        if (!this.worldObj.isRemote && this.getWatchedTargetId(0) > 0 && (entity = this.worldObj.getEntityByID(this.getWatchedTargetId(0))) != null) {
            double d4;
            if (this.posY < entity.posY || !this.isArmored() && this.posY < entity.posY + 5.0) {
                if (this.motionY < 0.0) {
                    this.motionY = 0.0;
                }
                this.motionY += (0.5 - this.motionY) * (double)0.6f;
            }
            if ((d3 = (d4 = entity.posX - this.posX) * d4 + (d2 = entity.posZ - this.posZ) * d2) > 9.0) {
                d = MathHelper.sqrt_double(d3);
                this.motionX += (d4 / d * 0.5 - this.motionX) * (double)0.6f;
                this.motionZ += (d2 / d * 0.5 - this.motionZ) * (double)0.6f;
            }
        }
        if (this.motionX * this.motionX + this.motionZ * this.motionZ > (double)0.05f) {
            this.rotationYaw = (float)MathHelper.func_181159_b(this.motionZ, this.motionX) * 57.295776f - 90.0f;
        }
        super.onLivingUpdate();
        int n = 0;
        while (n < 2) {
            this.field_82218_g[n] = this.field_82221_e[n];
            this.field_82217_f[n] = this.field_82220_d[n];
            ++n;
        }
        n = 0;
        while (n < 2) {
            int n2 = this.getWatchedTargetId(n + 1);
            Entity entity2 = null;
            if (n2 > 0) {
                entity2 = this.worldObj.getEntityByID(n2);
            }
            if (entity2 != null) {
                d2 = this.func_82214_u(n + 1);
                d3 = this.func_82208_v(n + 1);
                d = this.func_82213_w(n + 1);
                double d5 = entity2.posX - d2;
                double d6 = entity2.posY + (double)entity2.getEyeHeight() - d3;
                double d7 = entity2.posZ - d;
                double d8 = MathHelper.sqrt_double(d5 * d5 + d7 * d7);
                float f = (float)(MathHelper.func_181159_b(d7, d5) * 180.0 / Math.PI) - 90.0f;
                float f2 = (float)(-(MathHelper.func_181159_b(d6, d8) * 180.0 / Math.PI));
                this.field_82220_d[n] = this.func_82204_b(this.field_82220_d[n], f2, 40.0f);
                this.field_82221_e[n] = this.func_82204_b(this.field_82221_e[n], f, 10.0f);
            } else {
                this.field_82221_e[n] = this.func_82204_b(this.field_82221_e[n], this.renderYawOffset, 10.0f);
            }
            ++n;
        }
        n = this.isArmored() ? 1 : 0;
        int n3 = 0;
        while (n3 < 3) {
            double d9 = this.func_82214_u(n3);
            double d10 = this.func_82208_v(n3);
            double d11 = this.func_82213_w(n3);
            this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d9 + this.rand.nextGaussian() * (double)0.3f, d10 + this.rand.nextGaussian() * (double)0.3f, d11 + this.rand.nextGaussian() * (double)0.3f, 0.0, 0.0, 0.0, new int[0]);
            if (n != 0 && this.worldObj.rand.nextInt(4) == 0) {
                this.worldObj.spawnParticle(EnumParticleTypes.SPELL_MOB, d9 + this.rand.nextGaussian() * (double)0.3f, d10 + this.rand.nextGaussian() * (double)0.3f, d11 + this.rand.nextGaussian() * (double)0.3f, (double)0.7f, (double)0.7f, 0.5, new int[0]);
            }
            ++n3;
        }
        if (this.getInvulTime() > 0) {
            n3 = 0;
            while (n3 < 3) {
                this.worldObj.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + this.rand.nextGaussian() * 1.0, this.posY + (double)(this.rand.nextFloat() * 3.3f), this.posZ + this.rand.nextGaussian() * 1.0, (double)0.7f, (double)0.7f, (double)0.9f, new int[0]);
                ++n3;
            }
        }
    }

    public EntityWither(World world) {
        super(world);
        this.field_82221_e = new float[2];
        this.field_82217_f = new float[2];
        this.field_82218_g = new float[2];
        this.field_82223_h = new int[2];
        this.field_82224_i = new int[2];
        this.setHealth(this.getMaxHealth());
        this.setSize(0.9f, 3.5f);
        this.isImmuneToFire = true;
        ((PathNavigateGround)this.getNavigator()).setCanSwim(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIArrowAttack(this, 1.0, 40, 20.0f));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget((EntityCreature)this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<Entity>(this, EntityLiving.class, 0, false, false, attackEntitySelector));
        this.experienceValue = 50;
    }

    private double func_82214_u(int n) {
        if (n <= 0) {
            return this.posX;
        }
        float f = (this.renderYawOffset + (float)(180 * (n - 1))) / 180.0f * (float)Math.PI;
        float f2 = MathHelper.cos(f);
        return this.posX + (double)f2 * 1.3;
    }
}

