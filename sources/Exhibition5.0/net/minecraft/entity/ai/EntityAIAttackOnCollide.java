// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.ai;

import net.minecraft.util.BlockPos;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.world.World;

public class EntityAIAttackOnCollide extends EntityAIBase
{
    World worldObj;
    protected EntityCreature attacker;
    int attackTick;
    double speedTowardsTarget;
    boolean longMemory;
    PathEntity entityPathEntity;
    Class classTarget;
    private int field_75445_i;
    private double field_151497_i;
    private double field_151495_j;
    private double field_151496_k;
    private static final String __OBFID = "CL_00001595";
    
    public EntityAIAttackOnCollide(final EntityCreature p_i1635_1_, final Class p_i1635_2_, final double p_i1635_3_, final boolean p_i1635_5_) {
        this(p_i1635_1_, p_i1635_3_, p_i1635_5_);
        this.classTarget = p_i1635_2_;
    }
    
    public EntityAIAttackOnCollide(final EntityCreature p_i1636_1_, final double p_i1636_2_, final boolean p_i1636_4_) {
        this.attacker = p_i1636_1_;
        this.worldObj = p_i1636_1_.worldObj;
        this.speedTowardsTarget = p_i1636_2_;
        this.longMemory = p_i1636_4_;
        this.setMutexBits(3);
    }
    
    @Override
    public boolean shouldExecute() {
        final EntityLivingBase var1 = this.attacker.getAttackTarget();
        if (var1 == null) {
            return false;
        }
        if (!var1.isEntityAlive()) {
            return false;
        }
        if (this.classTarget != null && !this.classTarget.isAssignableFrom(var1.getClass())) {
            return false;
        }
        this.entityPathEntity = this.attacker.getNavigator().getPathToEntityLiving(var1);
        return this.entityPathEntity != null;
    }
    
    @Override
    public boolean continueExecuting() {
        final EntityLivingBase var1 = this.attacker.getAttackTarget();
        return var1 != null && var1.isEntityAlive() && (this.longMemory ? this.attacker.func_180485_d(new BlockPos(var1)) : (!this.attacker.getNavigator().noPath()));
    }
    
    @Override
    public void startExecuting() {
        this.attacker.getNavigator().setPath(this.entityPathEntity, this.speedTowardsTarget);
        this.field_75445_i = 0;
    }
    
    @Override
    public void resetTask() {
        this.attacker.getNavigator().clearPathEntity();
    }
    
    @Override
    public void updateTask() {
        final EntityLivingBase var1 = this.attacker.getAttackTarget();
        this.attacker.getLookHelper().setLookPositionWithEntity(var1, 30.0f, 30.0f);
        final double var2 = this.attacker.getDistanceSq(var1.posX, var1.getEntityBoundingBox().minY, var1.posZ);
        final double var3 = this.func_179512_a(var1);
        --this.field_75445_i;
        if ((this.longMemory || this.attacker.getEntitySenses().canSee(var1)) && this.field_75445_i <= 0 && ((this.field_151497_i == 0.0 && this.field_151495_j == 0.0 && this.field_151496_k == 0.0) || var1.getDistanceSq(this.field_151497_i, this.field_151495_j, this.field_151496_k) >= 1.0 || this.attacker.getRNG().nextFloat() < 0.05f)) {
            this.field_151497_i = var1.posX;
            this.field_151495_j = var1.getEntityBoundingBox().minY;
            this.field_151496_k = var1.posZ;
            this.field_75445_i = 4 + this.attacker.getRNG().nextInt(7);
            if (var2 > 1024.0) {
                this.field_75445_i += 10;
            }
            else if (var2 > 256.0) {
                this.field_75445_i += 5;
            }
            if (!this.attacker.getNavigator().tryMoveToEntityLiving(var1, this.speedTowardsTarget)) {
                this.field_75445_i += 15;
            }
        }
        this.attackTick = Math.max(this.attackTick - 1, 0);
        if (var2 <= var3 && this.attackTick <= 0) {
            this.attackTick = 20;
            if (this.attacker.getHeldItem() != null) {
                this.attacker.swingItem();
            }
            this.attacker.attackEntityAsMob(var1);
        }
    }
    
    protected double func_179512_a(final EntityLivingBase p_179512_1_) {
        return this.attacker.width * 2.0f * this.attacker.width * 2.0f + p_179512_1_.width;
    }
}
