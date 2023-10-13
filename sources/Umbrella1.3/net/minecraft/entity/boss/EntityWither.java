/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.base.Predicates
 */
package net.minecraft.entity.boss;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.command.IEntitySelector;
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
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityWither
extends EntityMob
implements IBossDisplayData,
IRangedAttackMob {
    private float[] field_82220_d = new float[2];
    private float[] field_82221_e = new float[2];
    private float[] field_82217_f = new float[2];
    private float[] field_82218_g = new float[2];
    private int[] field_82223_h = new int[2];
    private int[] field_82224_i = new int[2];
    private int field_82222_j;
    private static final Predicate attackEntitySelector = new Predicate(){
        private static final String __OBFID = "CL_00001662";

        public boolean func_180027_a(Entity p_180027_1_) {
            return p_180027_1_ instanceof EntityLivingBase && ((EntityLivingBase)p_180027_1_).getCreatureAttribute() != EnumCreatureAttribute.UNDEAD;
        }

        public boolean apply(Object p_apply_1_) {
            return this.func_180027_a((Entity)p_apply_1_);
        }
    };
    private static final String __OBFID = "CL_00001661";

    public EntityWither(World worldIn) {
        super(worldIn);
        this.setHealth(this.getMaxHealth());
        this.setSize(0.9f, 3.5f);
        this.isImmuneToFire = true;
        ((PathNavigateGround)this.getNavigator()).func_179693_d(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIArrowAttack(this, 1.0, 40, 20.0f));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget((EntityCreature)this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, false, false, attackEntitySelector));
        this.experienceValue = 50;
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
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("Invul", this.getInvulTime());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.setInvulTime(tagCompund.getInteger("Invul"));
    }

    @Override
    protected String getLivingSound() {
        return "mob.wither.idle";
    }

    @Override
    protected String getHurtSound() {
        return "mob.wither.hurt";
    }

    @Override
    protected String getDeathSound() {
        return "mob.wither.death";
    }

    @Override
    public void onLivingUpdate() {
        int var22;
        int var20;
        double var8;
        double var4;
        double var6;
        Entity var1;
        this.motionY *= (double)0.6f;
        if (!this.worldObj.isRemote && this.getWatchedTargetId(0) > 0 && (var1 = this.worldObj.getEntityByID(this.getWatchedTargetId(0))) != null) {
            double var2;
            if (this.posY < var1.posY || !this.isArmored() && this.posY < var1.posY + 5.0) {
                if (this.motionY < 0.0) {
                    this.motionY = 0.0;
                }
                this.motionY += (0.5 - this.motionY) * (double)0.6f;
            }
            if ((var6 = (var2 = var1.posX - this.posX) * var2 + (var4 = var1.posZ - this.posZ) * var4) > 9.0) {
                var8 = MathHelper.sqrt_double(var6);
                this.motionX += (var2 / var8 * 0.5 - this.motionX) * (double)0.6f;
                this.motionZ += (var4 / var8 * 0.5 - this.motionZ) * (double)0.6f;
            }
        }
        if (this.motionX * this.motionX + this.motionZ * this.motionZ > (double)0.05f) {
            this.rotationYaw = (float)Math.atan2(this.motionZ, this.motionX) * 57.295776f - 90.0f;
        }
        super.onLivingUpdate();
        for (var20 = 0; var20 < 2; ++var20) {
            this.field_82218_g[var20] = this.field_82221_e[var20];
            this.field_82217_f[var20] = this.field_82220_d[var20];
        }
        for (var20 = 0; var20 < 2; ++var20) {
            int var222 = this.getWatchedTargetId(var20 + 1);
            Entity var3 = null;
            if (var222 > 0) {
                var3 = this.worldObj.getEntityByID(var222);
            }
            if (var3 != null) {
                var4 = this.func_82214_u(var20 + 1);
                var6 = this.func_82208_v(var20 + 1);
                var8 = this.func_82213_w(var20 + 1);
                double var10 = var3.posX - var4;
                double var12 = var3.posY + (double)var3.getEyeHeight() - var6;
                double var14 = var3.posZ - var8;
                double var16 = MathHelper.sqrt_double(var10 * var10 + var14 * var14);
                float var18 = (float)(Math.atan2(var14, var10) * 180.0 / Math.PI) - 90.0f;
                float var19 = (float)(-(Math.atan2(var12, var16) * 180.0 / Math.PI));
                this.field_82220_d[var20] = this.func_82204_b(this.field_82220_d[var20], var19, 40.0f);
                this.field_82221_e[var20] = this.func_82204_b(this.field_82221_e[var20], var18, 10.0f);
                continue;
            }
            this.field_82221_e[var20] = this.func_82204_b(this.field_82221_e[var20], this.renderYawOffset, 10.0f);
        }
        boolean var21 = this.isArmored();
        for (var22 = 0; var22 < 3; ++var22) {
            double var23 = this.func_82214_u(var22);
            double var5 = this.func_82208_v(var22);
            double var7 = this.func_82213_w(var22);
            this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var23 + this.rand.nextGaussian() * (double)0.3f, var5 + this.rand.nextGaussian() * (double)0.3f, var7 + this.rand.nextGaussian() * (double)0.3f, 0.0, 0.0, 0.0, new int[0]);
            if (!var21 || this.worldObj.rand.nextInt(4) != 0) continue;
            this.worldObj.spawnParticle(EnumParticleTypes.SPELL_MOB, var23 + this.rand.nextGaussian() * (double)0.3f, var5 + this.rand.nextGaussian() * (double)0.3f, var7 + this.rand.nextGaussian() * (double)0.3f, (double)0.7f, (double)0.7f, 0.5, new int[0]);
        }
        if (this.getInvulTime() > 0) {
            for (var22 = 0; var22 < 3; ++var22) {
                this.worldObj.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + this.rand.nextGaussian() * 1.0, this.posY + (double)(this.rand.nextFloat() * 3.3f), this.posZ + this.rand.nextGaussian() * 1.0, (double)0.7f, (double)0.7f, (double)0.9f, new int[0]);
            }
        }
    }

    @Override
    protected void updateAITasks() {
        if (this.getInvulTime() > 0) {
            int var1 = this.getInvulTime() - 1;
            if (var1 <= 0) {
                this.worldObj.newExplosion(this, this.posX, this.posY + (double)this.getEyeHeight(), this.posZ, 7.0f, false, this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"));
                this.worldObj.func_175669_a(1013, new BlockPos(this), 0);
            }
            this.setInvulTime(var1);
            if (this.ticksExisted % 10 == 0) {
                this.heal(10.0f);
            }
        } else {
            int var12;
            int var1;
            super.updateAITasks();
            block0: for (var1 = 1; var1 < 3; ++var1) {
                if (this.ticksExisted < this.field_82223_h[var1 - 1]) continue;
                this.field_82223_h[var1 - 1] = this.ticksExisted + 10 + this.rand.nextInt(10);
                if (this.worldObj.getDifficulty() == EnumDifficulty.NORMAL || this.worldObj.getDifficulty() == EnumDifficulty.HARD) {
                    int var10001 = var1 - 1;
                    int var10003 = this.field_82224_i[var1 - 1];
                    this.field_82224_i[var10001] = this.field_82224_i[var1 - 1] + 1;
                    if (var10003 > 15) {
                        float var2 = 10.0f;
                        float var3 = 5.0f;
                        double var4 = MathHelper.getRandomDoubleInRange(this.rand, this.posX - (double)var2, this.posX + (double)var2);
                        double var6 = MathHelper.getRandomDoubleInRange(this.rand, this.posY - (double)var3, this.posY + (double)var3);
                        double var8 = MathHelper.getRandomDoubleInRange(this.rand, this.posZ - (double)var2, this.posZ + (double)var2);
                        this.launchWitherSkullToCoords(var1 + 1, var4, var6, var8, true);
                        this.field_82224_i[var1 - 1] = 0;
                    }
                }
                if ((var12 = this.getWatchedTargetId(var1)) > 0) {
                    Entity var14 = this.worldObj.getEntityByID(var12);
                    if (var14 != null && var14.isEntityAlive() && this.getDistanceSqToEntity(var14) <= 900.0 && this.canEntityBeSeen(var14)) {
                        this.launchWitherSkullToEntity(var1 + 1, (EntityLivingBase)var14);
                        this.field_82223_h[var1 - 1] = this.ticksExisted + 40 + this.rand.nextInt(20);
                        this.field_82224_i[var1 - 1] = 0;
                        continue;
                    }
                    this.func_82211_c(var1, 0);
                    continue;
                }
                List var13 = this.worldObj.func_175647_a(EntityLivingBase.class, this.getEntityBoundingBox().expand(20.0, 8.0, 20.0), Predicates.and((Predicate)attackEntitySelector, (Predicate)IEntitySelector.field_180132_d));
                for (int var16 = 0; var16 < 10 && !var13.isEmpty(); ++var16) {
                    EntityLivingBase var5 = (EntityLivingBase)var13.get(this.rand.nextInt(var13.size()));
                    if (var5 != this && var5.isEntityAlive() && this.canEntityBeSeen(var5)) {
                        if (var5 instanceof EntityPlayer) {
                            if (((EntityPlayer)var5).capabilities.disableDamage) continue block0;
                            this.func_82211_c(var1, var5.getEntityId());
                            continue block0;
                        }
                        this.func_82211_c(var1, var5.getEntityId());
                        continue block0;
                    }
                    var13.remove(var5);
                }
            }
            if (this.getAttackTarget() != null) {
                this.func_82211_c(0, this.getAttackTarget().getEntityId());
            } else {
                this.func_82211_c(0, 0);
            }
            if (this.field_82222_j > 0) {
                --this.field_82222_j;
                if (this.field_82222_j == 0 && this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
                    var1 = MathHelper.floor_double(this.posY);
                    var12 = MathHelper.floor_double(this.posX);
                    int var15 = MathHelper.floor_double(this.posZ);
                    boolean var17 = false;
                    for (int var18 = -1; var18 <= 1; ++var18) {
                        for (int var19 = -1; var19 <= 1; ++var19) {
                            for (int var7 = 0; var7 <= 3; ++var7) {
                                int var20 = var12 + var18;
                                int var9 = var1 + var7;
                                int var10 = var15 + var19;
                                Block var11 = this.worldObj.getBlockState(new BlockPos(var20, var9, var10)).getBlock();
                                if (var11.getMaterial() == Material.air || var11 == Blocks.bedrock || var11 == Blocks.end_portal || var11 == Blocks.end_portal_frame || var11 == Blocks.command_block || var11 == Blocks.barrier) continue;
                                var17 = this.worldObj.destroyBlock(new BlockPos(var20, var9, var10), true) || var17;
                            }
                        }
                    }
                    if (var17) {
                        this.worldObj.playAuxSFXAtEntity(null, 1012, new BlockPos(this), 0);
                    }
                }
            }
            if (this.ticksExisted % 20 == 0) {
                this.heal(1.0f);
            }
        }
    }

    public void func_82206_m() {
        this.setInvulTime(220);
        this.setHealth(this.getMaxHealth() / 3.0f);
    }

    @Override
    public void setInWeb() {
    }

    @Override
    public int getTotalArmorValue() {
        return 4;
    }

    private double func_82214_u(int p_82214_1_) {
        if (p_82214_1_ <= 0) {
            return this.posX;
        }
        float var2 = (this.renderYawOffset + (float)(180 * (p_82214_1_ - 1))) / 180.0f * (float)Math.PI;
        float var3 = MathHelper.cos(var2);
        return this.posX + (double)var3 * 1.3;
    }

    private double func_82208_v(int p_82208_1_) {
        return p_82208_1_ <= 0 ? this.posY + 3.0 : this.posY + 2.2;
    }

    private double func_82213_w(int p_82213_1_) {
        if (p_82213_1_ <= 0) {
            return this.posZ;
        }
        float var2 = (this.renderYawOffset + (float)(180 * (p_82213_1_ - 1))) / 180.0f * (float)Math.PI;
        float var3 = MathHelper.sin(var2);
        return this.posZ + (double)var3 * 1.3;
    }

    private float func_82204_b(float p_82204_1_, float p_82204_2_, float p_82204_3_) {
        float var4 = MathHelper.wrapAngleTo180_float(p_82204_2_ - p_82204_1_);
        if (var4 > p_82204_3_) {
            var4 = p_82204_3_;
        }
        if (var4 < -p_82204_3_) {
            var4 = -p_82204_3_;
        }
        return p_82204_1_ + var4;
    }

    private void launchWitherSkullToEntity(int p_82216_1_, EntityLivingBase p_82216_2_) {
        this.launchWitherSkullToCoords(p_82216_1_, p_82216_2_.posX, p_82216_2_.posY + (double)p_82216_2_.getEyeHeight() * 0.5, p_82216_2_.posZ, p_82216_1_ == 0 && this.rand.nextFloat() < 0.001f);
    }

    private void launchWitherSkullToCoords(int p_82209_1_, double p_82209_2_, double p_82209_4_, double p_82209_6_, boolean p_82209_8_) {
        this.worldObj.playAuxSFXAtEntity(null, 1014, new BlockPos(this), 0);
        double var9 = this.func_82214_u(p_82209_1_);
        double var11 = this.func_82208_v(p_82209_1_);
        double var13 = this.func_82213_w(p_82209_1_);
        double var15 = p_82209_2_ - var9;
        double var17 = p_82209_4_ - var11;
        double var19 = p_82209_6_ - var13;
        EntityWitherSkull var21 = new EntityWitherSkull(this.worldObj, this, var15, var17, var19);
        if (p_82209_8_) {
            var21.setInvulnerable(true);
        }
        var21.posY = var11;
        var21.posX = var9;
        var21.posZ = var13;
        this.worldObj.spawnEntityInWorld(var21);
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_) {
        this.launchWitherSkullToEntity(0, p_82196_1_);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.func_180431_b(source)) {
            return false;
        }
        if (source != DamageSource.drown && !(source.getEntity() instanceof EntityWither)) {
            Entity var3;
            if (this.getInvulTime() > 0 && source != DamageSource.outOfWorld) {
                return false;
            }
            if (this.isArmored() && (var3 = source.getSourceOfDamage()) instanceof EntityArrow) {
                return false;
            }
            var3 = source.getEntity();
            if (var3 != null && !(var3 instanceof EntityPlayer) && var3 instanceof EntityLivingBase && ((EntityLivingBase)var3).getCreatureAttribute() == this.getCreatureAttribute()) {
                return false;
            }
            if (this.field_82222_j <= 0) {
                this.field_82222_j = 20;
            }
            int var4 = 0;
            while (var4 < this.field_82224_i.length) {
                int n = var4++;
                this.field_82224_i[n] = this.field_82224_i[n] + 3;
            }
            return super.attackEntityFrom(source, amount);
        }
        return false;
    }

    @Override
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
        EntityItem var3 = this.dropItem(Items.nether_star, 1);
        if (var3 != null) {
            var3.func_174873_u();
        }
        if (!this.worldObj.isRemote) {
            for (EntityPlayer var5 : this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().expand(50.0, 100.0, 50.0))) {
                var5.triggerAchievement(AchievementList.killWither);
            }
        }
    }

    @Override
    protected void despawnEntity() {
        this.entityAge = 0;
    }

    @Override
    public int getBrightnessForRender(float p_70070_1_) {
        return 0xF000F0;
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    public void addPotionEffect(PotionEffect p_70690_1_) {
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(300.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.6f);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(40.0);
    }

    public float func_82207_a(int p_82207_1_) {
        return this.field_82221_e[p_82207_1_];
    }

    public float func_82210_r(int p_82210_1_) {
        return this.field_82220_d[p_82210_1_];
    }

    public int getInvulTime() {
        return this.dataWatcher.getWatchableObjectInt(20);
    }

    public void setInvulTime(int p_82215_1_) {
        this.dataWatcher.updateObject(20, p_82215_1_);
    }

    public int getWatchedTargetId(int p_82203_1_) {
        return this.dataWatcher.getWatchableObjectInt(17 + p_82203_1_);
    }

    public void func_82211_c(int p_82211_1_, int p_82211_2_) {
        this.dataWatcher.updateObject(17 + p_82211_1_, p_82211_2_);
    }

    public boolean isArmored() {
        return this.getHealth() <= this.getMaxHealth() / 2.0f;
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    @Override
    public void mountEntity(Entity entityIn) {
        this.ridingEntity = null;
    }
}

