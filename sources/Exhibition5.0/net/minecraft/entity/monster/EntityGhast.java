// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.monster;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import java.util.Random;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.Vec3;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.AchievementList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;
import net.minecraft.entity.EntityFlying;

public class EntityGhast extends EntityFlying implements IMob
{
    private int explosionStrength;
    private static final String __OBFID = "CL_00001689";
    
    public EntityGhast(final World worldIn) {
        super(worldIn);
        this.explosionStrength = 1;
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
    
    public void func_175454_a(final boolean p_175454_1_) {
        this.dataWatcher.updateObject(16, (byte)(byte)(p_175454_1_ ? 1 : 0));
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
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
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
    protected void dropFewItems(final boolean p_70628_1_, final int p_70628_2_) {
        for (int var3 = this.rand.nextInt(2) + this.rand.nextInt(1 + p_70628_2_), var4 = 0; var4 < var3; ++var4) {
            this.dropItem(Items.ghast_tear, 1);
        }
        for (int var3 = this.rand.nextInt(3) + this.rand.nextInt(1 + p_70628_2_), var4 = 0; var4 < var3; ++var4) {
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
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("ExplosionPower", this.explosionStrength);
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        if (tagCompund.hasKey("ExplosionPower", 99)) {
            this.explosionStrength = tagCompund.getInteger("ExplosionPower");
        }
    }
    
    @Override
    public float getEyeHeight() {
        return 2.6f;
    }
    
    class AIFireballAttack extends EntityAIBase
    {
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
            final EntityLivingBase var1 = this.field_179470_b.getAttackTarget();
            final double var2 = 64.0;
            if (var1.getDistanceSqToEntity(this.field_179470_b) < var2 * var2 && this.field_179470_b.canEntityBeSeen(var1)) {
                final World var3 = this.field_179470_b.worldObj;
                ++this.field_179471_a;
                if (this.field_179471_a == 10) {
                    var3.playAuxSFXAtEntity(null, 1007, new BlockPos(this.field_179470_b), 0);
                }
                if (this.field_179471_a == 20) {
                    final double var4 = 4.0;
                    final Vec3 var5 = this.field_179470_b.getLook(1.0f);
                    final double var6 = var1.posX - (this.field_179470_b.posX + var5.xCoord * var4);
                    final double var7 = var1.getEntityBoundingBox().minY + var1.height / 2.0f - (0.5 + this.field_179470_b.posY + this.field_179470_b.height / 2.0f);
                    final double var8 = var1.posZ - (this.field_179470_b.posZ + var5.zCoord * var4);
                    var3.playAuxSFXAtEntity(null, 1008, new BlockPos(this.field_179470_b), 0);
                    final EntityLargeFireball var9 = new EntityLargeFireball(var3, this.field_179470_b, var6, var7, var8);
                    var9.field_92057_e = this.field_179470_b.func_175453_cd();
                    var9.posX = this.field_179470_b.posX + var5.xCoord * var4;
                    var9.posY = this.field_179470_b.posY + this.field_179470_b.height / 2.0f + 0.5;
                    var9.posZ = this.field_179470_b.posZ + var5.zCoord * var4;
                    var3.spawnEntityInWorld(var9);
                    this.field_179471_a = -40;
                }
            }
            else if (this.field_179471_a > 0) {
                --this.field_179471_a;
            }
            this.field_179470_b.func_175454_a(this.field_179471_a > 10);
        }
    }
    
    class AILookAround extends EntityAIBase
    {
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
                final EntityGhast field_179472_a = this.field_179472_a;
                final EntityGhast field_179472_a2 = this.field_179472_a;
                final float n = -(float)Math.atan2(this.field_179472_a.motionX, this.field_179472_a.motionZ) * 180.0f / 3.1415927f;
                field_179472_a2.rotationYaw = n;
                field_179472_a.renderYawOffset = n;
            }
            else {
                final EntityLivingBase var1 = this.field_179472_a.getAttackTarget();
                final double var2 = 64.0;
                if (var1.getDistanceSqToEntity(this.field_179472_a) < var2 * var2) {
                    final double var3 = var1.posX - this.field_179472_a.posX;
                    final double var4 = var1.posZ - this.field_179472_a.posZ;
                    final EntityGhast field_179472_a3 = this.field_179472_a;
                    final EntityGhast field_179472_a4 = this.field_179472_a;
                    final float n2 = -(float)Math.atan2(var3, var4) * 180.0f / 3.1415927f;
                    field_179472_a4.rotationYaw = n2;
                    field_179472_a3.renderYawOffset = n2;
                }
            }
        }
    }
    
    class AIRandomFly extends EntityAIBase
    {
        private EntityGhast field_179454_a;
        private static final String __OBFID = "CL_00002214";
        
        public AIRandomFly() {
            this.field_179454_a = EntityGhast.this;
            this.setMutexBits(1);
        }
        
        @Override
        public boolean shouldExecute() {
            final EntityMoveHelper var1 = this.field_179454_a.getMoveHelper();
            if (!var1.isUpdating()) {
                return true;
            }
            final double var2 = var1.func_179917_d() - this.field_179454_a.posX;
            final double var3 = var1.func_179919_e() - this.field_179454_a.posY;
            final double var4 = var1.func_179918_f() - this.field_179454_a.posZ;
            final double var5 = var2 * var2 + var3 * var3 + var4 * var4;
            return var5 < 1.0 || var5 > 3600.0;
        }
        
        @Override
        public boolean continueExecuting() {
            return false;
        }
        
        @Override
        public void startExecuting() {
            final Random var1 = this.field_179454_a.getRNG();
            final double var2 = this.field_179454_a.posX + (var1.nextFloat() * 2.0f - 1.0f) * 16.0f;
            final double var3 = this.field_179454_a.posY + (var1.nextFloat() * 2.0f - 1.0f) * 16.0f;
            final double var4 = this.field_179454_a.posZ + (var1.nextFloat() * 2.0f - 1.0f) * 16.0f;
            this.field_179454_a.getMoveHelper().setMoveTo(var2, var3, var4, 1.0);
        }
    }
    
    class GhastMoveHelper extends EntityMoveHelper
    {
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
                final double var1 = this.posX - this.field_179927_g.posX;
                final double var2 = this.posY - this.field_179927_g.posY;
                final double var3 = this.posZ - this.field_179927_g.posZ;
                double var4 = var1 * var1 + var2 * var2 + var3 * var3;
                if (this.field_179928_h-- <= 0) {
                    this.field_179928_h += this.field_179927_g.getRNG().nextInt(5) + 2;
                    var4 = MathHelper.sqrt_double(var4);
                    if (this.func_179926_b(this.posX, this.posY, this.posZ, var4)) {
                        final EntityGhast field_179927_g = this.field_179927_g;
                        field_179927_g.motionX += var1 / var4 * 0.1;
                        final EntityGhast field_179927_g2 = this.field_179927_g;
                        field_179927_g2.motionY += var2 / var4 * 0.1;
                        final EntityGhast field_179927_g3 = this.field_179927_g;
                        field_179927_g3.motionZ += var3 / var4 * 0.1;
                    }
                    else {
                        this.update = false;
                    }
                }
            }
        }
        
        private boolean func_179926_b(final double p_179926_1_, final double p_179926_3_, final double p_179926_5_, final double p_179926_7_) {
            final double var9 = (p_179926_1_ - this.field_179927_g.posX) / p_179926_7_;
            final double var10 = (p_179926_3_ - this.field_179927_g.posY) / p_179926_7_;
            final double var11 = (p_179926_5_ - this.field_179927_g.posZ) / p_179926_7_;
            AxisAlignedBB var12 = this.field_179927_g.getEntityBoundingBox();
            for (int var13 = 1; var13 < p_179926_7_; ++var13) {
                var12 = var12.offset(var9, var10, var11);
                if (!this.field_179927_g.worldObj.getCollidingBoundingBoxes(this.field_179927_g, var12).isEmpty()) {
                    return false;
                }
            }
            return true;
        }
    }
}
