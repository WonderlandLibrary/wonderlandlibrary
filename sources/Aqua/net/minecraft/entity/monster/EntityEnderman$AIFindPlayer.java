package net.minecraft.entity.monster;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;

/*
 * Exception performing whole class analysis ignored.
 */
static class EntityEnderman.AIFindPlayer
extends EntityAINearestAttackableTarget {
    private EntityPlayer player;
    private int field_179450_h;
    private int field_179451_i;
    private EntityEnderman enderman;

    public EntityEnderman.AIFindPlayer(EntityEnderman p_i45842_1_) {
        super((EntityCreature)p_i45842_1_, EntityPlayer.class, true);
        this.enderman = p_i45842_1_;
    }

    public boolean shouldExecute() {
        double d0 = this.getTargetDistance();
        List list = this.taskOwner.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.taskOwner.getEntityBoundingBox().expand(d0, 4.0, d0), this.targetEntitySelector);
        Collections.sort((List)list, (Comparator)this.theNearestAttackableTargetSorter);
        if (list.isEmpty()) {
            return false;
        }
        this.player = (EntityPlayer)list.get(0);
        return true;
    }

    public void startExecuting() {
        this.field_179450_h = 5;
        this.field_179451_i = 0;
    }

    public void resetTask() {
        this.player = null;
        this.enderman.setScreaming(false);
        IAttributeInstance iattributeinstance = this.enderman.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
        iattributeinstance.removeModifier(EntityEnderman.access$000());
        super.resetTask();
    }

    public boolean continueExecuting() {
        if (this.player != null) {
            if (!EntityEnderman.access$100((EntityEnderman)this.enderman, (EntityPlayer)this.player)) {
                return false;
            }
            EntityEnderman.access$202((EntityEnderman)this.enderman, (boolean)true);
            this.enderman.faceEntity((Entity)this.player, 10.0f, 10.0f);
            return true;
        }
        return super.continueExecuting();
    }

    public void updateTask() {
        if (this.player != null) {
            if (--this.field_179450_h <= 0) {
                this.targetEntity = this.player;
                this.player = null;
                super.startExecuting();
                this.enderman.playSound("mob.endermen.stare", 1.0f, 1.0f);
                this.enderman.setScreaming(true);
                IAttributeInstance iattributeinstance = this.enderman.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
                iattributeinstance.applyModifier(EntityEnderman.access$000());
            }
        } else {
            if (this.targetEntity != null) {
                if (this.targetEntity instanceof EntityPlayer && EntityEnderman.access$100((EntityEnderman)this.enderman, (EntityPlayer)((EntityPlayer)this.targetEntity))) {
                    if (this.targetEntity.getDistanceSqToEntity((Entity)this.enderman) < 16.0) {
                        this.enderman.teleportRandomly();
                    }
                    this.field_179451_i = 0;
                } else if (this.targetEntity.getDistanceSqToEntity((Entity)this.enderman) > 256.0 && this.field_179451_i++ >= 30 && this.enderman.teleportToEntity((Entity)this.targetEntity)) {
                    this.field_179451_i = 0;
                }
            }
            super.updateTask();
        }
    }
}
