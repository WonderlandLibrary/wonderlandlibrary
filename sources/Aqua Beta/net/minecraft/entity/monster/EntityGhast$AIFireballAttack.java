package net.minecraft.entity.monster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

static class EntityGhast.AIFireballAttack
extends EntityAIBase {
    private EntityGhast parentEntity;
    public int attackTimer;

    public EntityGhast.AIFireballAttack(EntityGhast ghast) {
        this.parentEntity = ghast;
    }

    public boolean shouldExecute() {
        return this.parentEntity.getAttackTarget() != null;
    }

    public void startExecuting() {
        this.attackTimer = 0;
    }

    public void resetTask() {
        this.parentEntity.setAttacking(false);
    }

    public void updateTask() {
        EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
        double d0 = 64.0;
        if (entitylivingbase.getDistanceSqToEntity((Entity)this.parentEntity) < d0 * d0 && this.parentEntity.canEntityBeSeen((Entity)entitylivingbase)) {
            World world = this.parentEntity.worldObj;
            ++this.attackTimer;
            if (this.attackTimer == 10) {
                world.playAuxSFXAtEntity((EntityPlayer)null, 1007, new BlockPos((Entity)this.parentEntity), 0);
            }
            if (this.attackTimer == 20) {
                double d1 = 4.0;
                Vec3 vec3 = this.parentEntity.getLook(1.0f);
                double d2 = entitylivingbase.posX - (this.parentEntity.posX + vec3.xCoord * d1);
                double d3 = entitylivingbase.getEntityBoundingBox().minY + (double)(entitylivingbase.height / 2.0f) - (0.5 + this.parentEntity.posY + (double)(this.parentEntity.height / 2.0f));
                double d4 = entitylivingbase.posZ - (this.parentEntity.posZ + vec3.zCoord * d1);
                world.playAuxSFXAtEntity((EntityPlayer)null, 1008, new BlockPos((Entity)this.parentEntity), 0);
                EntityLargeFireball entitylargefireball = new EntityLargeFireball(world, (EntityLivingBase)this.parentEntity, d2, d3, d4);
                entitylargefireball.explosionPower = this.parentEntity.getFireballStrength();
                entitylargefireball.posX = this.parentEntity.posX + vec3.xCoord * d1;
                entitylargefireball.posY = this.parentEntity.posY + (double)(this.parentEntity.height / 2.0f) + 0.5;
                entitylargefireball.posZ = this.parentEntity.posZ + vec3.zCoord * d1;
                world.spawnEntityInWorld((Entity)entitylargefireball);
                this.attackTimer = -40;
            }
        } else if (this.attackTimer > 0) {
            --this.attackTimer;
        }
        this.parentEntity.setAttacking(this.attackTimer > 10);
    }
}
