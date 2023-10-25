package net.minecraft.entity.monster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

static class EntityBlaze.AIFireballAttack
extends EntityAIBase {
    private EntityBlaze blaze;
    private int field_179467_b;
    private int field_179468_c;

    public EntityBlaze.AIFireballAttack(EntityBlaze p_i45846_1_) {
        this.blaze = p_i45846_1_;
        this.setMutexBits(3);
    }

    public boolean shouldExecute() {
        EntityLivingBase entitylivingbase = this.blaze.getAttackTarget();
        return entitylivingbase != null && entitylivingbase.isEntityAlive();
    }

    public void startExecuting() {
        this.field_179467_b = 0;
    }

    public void resetTask() {
        this.blaze.setOnFire(false);
    }

    public void updateTask() {
        --this.field_179468_c;
        EntityLivingBase entitylivingbase = this.blaze.getAttackTarget();
        double d0 = this.blaze.getDistanceSqToEntity((Entity)entitylivingbase);
        if (d0 < 4.0) {
            if (this.field_179468_c <= 0) {
                this.field_179468_c = 20;
                this.blaze.attackEntityAsMob((Entity)entitylivingbase);
            }
            this.blaze.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0);
        } else if (d0 < 256.0) {
            double d1 = entitylivingbase.posX - this.blaze.posX;
            double d2 = entitylivingbase.getEntityBoundingBox().minY + (double)(entitylivingbase.height / 2.0f) - (this.blaze.posY + (double)(this.blaze.height / 2.0f));
            double d3 = entitylivingbase.posZ - this.blaze.posZ;
            if (this.field_179468_c <= 0) {
                ++this.field_179467_b;
                if (this.field_179467_b == 1) {
                    this.field_179468_c = 60;
                    this.blaze.setOnFire(true);
                } else if (this.field_179467_b <= 4) {
                    this.field_179468_c = 6;
                } else {
                    this.field_179468_c = 100;
                    this.field_179467_b = 0;
                    this.blaze.setOnFire(false);
                }
                if (this.field_179467_b > 1) {
                    float f = MathHelper.sqrt_float((float)MathHelper.sqrt_double((double)d0)) * 0.5f;
                    this.blaze.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1009, new BlockPos((int)this.blaze.posX, (int)this.blaze.posY, (int)this.blaze.posZ), 0);
                    for (int i = 0; i < 1; ++i) {
                        EntitySmallFireball entitysmallfireball = new EntitySmallFireball(this.blaze.worldObj, (EntityLivingBase)this.blaze, d1 + this.blaze.getRNG().nextGaussian() * (double)f, d2, d3 + this.blaze.getRNG().nextGaussian() * (double)f);
                        entitysmallfireball.posY = this.blaze.posY + (double)(this.blaze.height / 2.0f) + 0.5;
                        this.blaze.worldObj.spawnEntityInWorld((Entity)entitysmallfireball);
                    }
                }
            }
            this.blaze.getLookHelper().setLookPositionWithEntity((Entity)entitylivingbase, 10.0f, 10.0f);
        } else {
            this.blaze.getNavigator().clearPathEntity();
            this.blaze.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0);
        }
        super.updateTask();
    }
}
