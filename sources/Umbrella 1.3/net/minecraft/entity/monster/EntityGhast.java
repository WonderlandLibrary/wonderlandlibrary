/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.monster;

import java.util.Random;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityGhast
extends EntityFlying
implements IMob {
    private int explosionStrength = 1;
    private static final String __OBFID = "CL_00001689";

    public EntityGhast(World worldIn) {
        super(worldIn);
        this.setSize(4.0f, 4.0f);
        this.isImmuneToFire = true;
        this.experienceValue = 5;
        this.moveHelper = new GhastMoveHelper();
        this.tasks.addTask(5, new AIRandomFly());
        this.tasks.addTask(7, new AILookAround());
        this.tasks.addTask(7, new AIFireballAttack());
        this.targetTasks.addTask(1, new EntityAIFindEntityNearestPlayer(this));
    }

    public boolean func_110182_bF() {
        return this.dataWatcher.getWatchableObjectByte(16) != 0;
    }

    public void func_175454_a(boolean p_175454_1_) {
        this.dataWatcher.updateObject(16, (byte)(p_175454_1_ ? 1 : 0));
    }

    public int func_175453_cd() {
        return this.explosionStrength;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.worldObj.isRemote && this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL) {
            this.setDead();
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.func_180431_b(source)) {
            return false;
        }
        if ("fireball".equals(source.getDamageType()) && source.getEntity() instanceof EntityPlayer) {
            super.attackEntityFrom(source, 1000.0f);
            ((EntityPlayer)source.getEntity()).triggerAchievement(AchievementList.ghast);
            return true;
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, (byte)0);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(100.0);
    }

    @Override
    protected String getLivingSound() {
        return "mob.ghast.moan";
    }

    @Override
    protected String getHurtSound() {
        return "mob.ghast.scream";
    }

    @Override
    protected String getDeathSound() {
        return "mob.ghast.death";
    }

    @Override
    protected Item getDropItem() {
        return Items.gunpowder;
    }

    @Override
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
        int var4;
        int var3 = this.rand.nextInt(2) + this.rand.nextInt(1 + p_70628_2_);
        for (var4 = 0; var4 < var3; ++var4) {
            this.dropItem(Items.ghast_tear, 1);
        }
        var3 = this.rand.nextInt(3) + this.rand.nextInt(1 + p_70628_2_);
        for (var4 = 0; var4 < var3; ++var4) {
            this.dropItem(Items.gunpowder, 1);
        }
    }

    @Override
    protected float getSoundVolume() {
        return 10.0f;
    }

    @Override
    public boolean getCanSpawnHere() {
        return this.rand.nextInt(20) == 0 && super.getCanSpawnHere() && this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL;
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 1;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("ExplosionPower", this.explosionStrength);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        if (tagCompund.hasKey("ExplosionPower", 99)) {
            this.explosionStrength = tagCompund.getInteger("ExplosionPower");
        }
    }

    @Override
    public float getEyeHeight() {
        return 2.6f;
    }

    class AIFireballAttack
    extends EntityAIBase {
        private EntityGhast field_179470_b;
        public int field_179471_a;
        private static final String __OBFID = "CL_00002215";

        AIFireballAttack() {
            this.field_179470_b = EntityGhast.this;
        }

        @Override
        public boolean shouldExecute() {
            return this.field_179470_b.getAttackTarget() != null;
        }

        @Override
        public void startExecuting() {
            this.field_179471_a = 0;
        }

        @Override
        public void resetTask() {
            this.field_179470_b.func_175454_a(false);
        }

        @Override
        public void updateTask() {
            EntityLivingBase var1 = this.field_179470_b.getAttackTarget();
            double var2 = 64.0;
            if (var1.getDistanceSqToEntity(this.field_179470_b) < var2 * var2 && this.field_179470_b.canEntityBeSeen(var1)) {
                World var4 = this.field_179470_b.worldObj;
                ++this.field_179471_a;
                if (this.field_179471_a == 10) {
                    var4.playAuxSFXAtEntity(null, 1007, new BlockPos(this.field_179470_b), 0);
                }
                if (this.field_179471_a == 20) {
                    double var5 = 4.0;
                    Vec3 var7 = this.field_179470_b.getLook(1.0f);
                    double var8 = var1.posX - (this.field_179470_b.posX + var7.xCoord * var5);
                    double var10 = var1.getEntityBoundingBox().minY + (double)(var1.height / 2.0f) - (0.5 + this.field_179470_b.posY + (double)(this.field_179470_b.height / 2.0f));
                    double var12 = var1.posZ - (this.field_179470_b.posZ + var7.zCoord * var5);
                    var4.playAuxSFXAtEntity(null, 1008, new BlockPos(this.field_179470_b), 0);
                    EntityLargeFireball var14 = new EntityLargeFireball(var4, this.field_179470_b, var8, var10, var12);
                    var14.field_92057_e = this.field_179470_b.func_175453_cd();
                    var14.posX = this.field_179470_b.posX + var7.xCoord * var5;
                    var14.posY = this.field_179470_b.posY + (double)(this.field_179470_b.height / 2.0f) + 0.5;
                    var14.posZ = this.field_179470_b.posZ + var7.zCoord * var5;
                    var4.spawnEntityInWorld(var14);
                    this.field_179471_a = -40;
                }
            } else if (this.field_179471_a > 0) {
                --this.field_179471_a;
            }
            this.field_179470_b.func_175454_a(this.field_179471_a > 10);
        }
    }

    class AILookAround
    extends EntityAIBase {
        private EntityGhast field_179472_a;
        private static final String __OBFID = "CL_00002217";

        public AILookAround() {
            this.field_179472_a = EntityGhast.this;
            this.setMutexBits(2);
        }

        @Override
        public boolean shouldExecute() {
            return true;
        }

        @Override
        public void updateTask() {
            if (this.field_179472_a.getAttackTarget() == null) {
                this.field_179472_a.renderYawOffset = this.field_179472_a.rotationYaw = -((float)Math.atan2(this.field_179472_a.motionX, this.field_179472_a.motionZ)) * 180.0f / (float)Math.PI;
            } else {
                EntityLivingBase var1 = this.field_179472_a.getAttackTarget();
                double var2 = 64.0;
                if (var1.getDistanceSqToEntity(this.field_179472_a) < var2 * var2) {
                    double var4 = var1.posX - this.field_179472_a.posX;
                    double var6 = var1.posZ - this.field_179472_a.posZ;
                    this.field_179472_a.renderYawOffset = this.field_179472_a.rotationYaw = -((float)Math.atan2(var4, var6)) * 180.0f / (float)Math.PI;
                }
            }
        }
    }

    class AIRandomFly
    extends EntityAIBase {
        private EntityGhast field_179454_a;
        private static final String __OBFID = "CL_00002214";

        public AIRandomFly() {
            this.field_179454_a = EntityGhast.this;
            this.setMutexBits(1);
        }

        @Override
        public boolean shouldExecute() {
            double var6;
            double var4;
            EntityMoveHelper var1 = this.field_179454_a.getMoveHelper();
            if (!var1.isUpdating()) {
                return true;
            }
            double var2 = var1.func_179917_d() - this.field_179454_a.posX;
            double var8 = var2 * var2 + (var4 = var1.func_179919_e() - this.field_179454_a.posY) * var4 + (var6 = var1.func_179918_f() - this.field_179454_a.posZ) * var6;
            return var8 < 1.0 || var8 > 3600.0;
        }

        @Override
        public boolean continueExecuting() {
            return false;
        }

        @Override
        public void startExecuting() {
            Random var1 = this.field_179454_a.getRNG();
            double var2 = this.field_179454_a.posX + (double)((var1.nextFloat() * 2.0f - 1.0f) * 16.0f);
            double var4 = this.field_179454_a.posY + (double)((var1.nextFloat() * 2.0f - 1.0f) * 16.0f);
            double var6 = this.field_179454_a.posZ + (double)((var1.nextFloat() * 2.0f - 1.0f) * 16.0f);
            this.field_179454_a.getMoveHelper().setMoveTo(var2, var4, var6, 1.0);
        }
    }

    class GhastMoveHelper
    extends EntityMoveHelper {
        private EntityGhast field_179927_g;
        private int field_179928_h;
        private static final String __OBFID = "CL_00002216";

        public GhastMoveHelper() {
            super(EntityGhast.this);
            this.field_179927_g = EntityGhast.this;
        }

        @Override
        public void onUpdateMoveHelper() {
            if (this.update) {
                double var1 = this.posX - this.field_179927_g.posX;
                double var3 = this.posY - this.field_179927_g.posY;
                double var5 = this.posZ - this.field_179927_g.posZ;
                double var7 = var1 * var1 + var3 * var3 + var5 * var5;
                if (this.field_179928_h-- <= 0) {
                    this.field_179928_h += this.field_179927_g.getRNG().nextInt(5) + 2;
                    if (this.func_179926_b(this.posX, this.posY, this.posZ, var7 = (double)MathHelper.sqrt_double(var7))) {
                        this.field_179927_g.motionX += var1 / var7 * 0.1;
                        this.field_179927_g.motionY += var3 / var7 * 0.1;
                        this.field_179927_g.motionZ += var5 / var7 * 0.1;
                    } else {
                        this.update = false;
                    }
                }
            }
        }

        private boolean func_179926_b(double p_179926_1_, double p_179926_3_, double p_179926_5_, double p_179926_7_) {
            double var9 = (p_179926_1_ - this.field_179927_g.posX) / p_179926_7_;
            double var11 = (p_179926_3_ - this.field_179927_g.posY) / p_179926_7_;
            double var13 = (p_179926_5_ - this.field_179927_g.posZ) / p_179926_7_;
            AxisAlignedBB var15 = this.field_179927_g.getEntityBoundingBox();
            int var16 = 1;
            while ((double)var16 < p_179926_7_) {
                if (!this.field_179927_g.worldObj.getCollidingBoundingBoxes(this.field_179927_g, var15 = var15.offset(var9, var11, var13)).isEmpty()) {
                    return false;
                }
                ++var16;
            }
            return true;
        }
    }
}

