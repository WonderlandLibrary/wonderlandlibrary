package net.minecraft.entity.monster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.DamageSource;
import net.minecraft.world.EnumDifficulty;

/*
 * Exception performing whole class analysis ignored.
 */
static class EntityGuardian.AIGuardianAttack
extends EntityAIBase {
    private EntityGuardian theEntity;
    private int tickCounter;

    public EntityGuardian.AIGuardianAttack(EntityGuardian guardian) {
        this.theEntity = guardian;
        this.setMutexBits(3);
    }

    public boolean shouldExecute() {
        EntityLivingBase entitylivingbase = this.theEntity.getAttackTarget();
        return entitylivingbase != null && entitylivingbase.isEntityAlive();
    }

    public boolean continueExecuting() {
        return super.continueExecuting() && (this.theEntity.isElder() || this.theEntity.getDistanceSqToEntity((Entity)this.theEntity.getAttackTarget()) > 9.0);
    }

    public void startExecuting() {
        this.tickCounter = -10;
        this.theEntity.getNavigator().clearPathEntity();
        this.theEntity.getLookHelper().setLookPositionWithEntity((Entity)this.theEntity.getAttackTarget(), 90.0f, 90.0f);
        this.theEntity.isAirBorne = true;
    }

    public void resetTask() {
        EntityGuardian.access$000((EntityGuardian)this.theEntity, (int)0);
        this.theEntity.setAttackTarget((EntityLivingBase)null);
        EntityGuardian.access$100((EntityGuardian)this.theEntity).makeUpdate();
    }

    public void updateTask() {
        EntityLivingBase entitylivingbase = this.theEntity.getAttackTarget();
        this.theEntity.getNavigator().clearPathEntity();
        this.theEntity.getLookHelper().setLookPositionWithEntity((Entity)entitylivingbase, 90.0f, 90.0f);
        if (!this.theEntity.canEntityBeSeen((Entity)entitylivingbase)) {
            this.theEntity.setAttackTarget((EntityLivingBase)null);
        } else {
            ++this.tickCounter;
            if (this.tickCounter == 0) {
                EntityGuardian.access$000((EntityGuardian)this.theEntity, (int)this.theEntity.getAttackTarget().getEntityId());
                this.theEntity.worldObj.setEntityState((Entity)this.theEntity, (byte)21);
            } else if (this.tickCounter >= this.theEntity.func_175464_ck()) {
                float f = 1.0f;
                if (this.theEntity.worldObj.getDifficulty() == EnumDifficulty.HARD) {
                    f += 2.0f;
                }
                if (this.theEntity.isElder()) {
                    f += 2.0f;
                }
                entitylivingbase.attackEntityFrom(DamageSource.causeIndirectMagicDamage((Entity)this.theEntity, (Entity)this.theEntity), f);
                entitylivingbase.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this.theEntity), (float)this.theEntity.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue());
                this.theEntity.setAttackTarget((EntityLivingBase)null);
            } else if (this.tickCounter < 60 || this.tickCounter % 20 == 0) {
                // empty if block
            }
            super.updateTask();
        }
    }
}
