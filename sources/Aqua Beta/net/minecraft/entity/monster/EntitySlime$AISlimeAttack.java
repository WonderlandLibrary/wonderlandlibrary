package net.minecraft.entity.monster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;

static class EntitySlime.AISlimeAttack
extends EntityAIBase {
    private EntitySlime slime;
    private int field_179465_b;

    public EntitySlime.AISlimeAttack(EntitySlime slimeIn) {
        this.slime = slimeIn;
        this.setMutexBits(2);
    }

    public boolean shouldExecute() {
        EntityLivingBase entitylivingbase = this.slime.getAttackTarget();
        return entitylivingbase == null ? false : (!entitylivingbase.isEntityAlive() ? false : !(entitylivingbase instanceof EntityPlayer) || !((EntityPlayer)entitylivingbase).capabilities.disableDamage);
    }

    public void startExecuting() {
        this.field_179465_b = 300;
        super.startExecuting();
    }

    public boolean continueExecuting() {
        EntityLivingBase entitylivingbase = this.slime.getAttackTarget();
        return entitylivingbase == null ? false : (!entitylivingbase.isEntityAlive() ? false : (entitylivingbase instanceof EntityPlayer && ((EntityPlayer)entitylivingbase).capabilities.disableDamage ? false : --this.field_179465_b > 0));
    }

    public void updateTask() {
        this.slime.faceEntity((Entity)this.slime.getAttackTarget(), 10.0f, 10.0f);
        ((EntitySlime.SlimeMoveHelper)this.slime.getMoveHelper()).func_179920_a(this.slime.rotationYaw, this.slime.canDamagePlayer());
    }
}
