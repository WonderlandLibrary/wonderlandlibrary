/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.Team;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityAIFindEntityNearestPlayer
extends EntityAIBase {
    private final Predicate<Entity> field_179435_c;
    private EntityLiving field_179434_b;
    private final EntityAINearestAttackableTarget.Sorter field_179432_d;
    private static final Logger field_179436_a = LogManager.getLogger();
    private EntityLivingBase field_179433_e;

    protected double func_179431_f() {
        IAttributeInstance iAttributeInstance = this.field_179434_b.getEntityAttribute(SharedMonsterAttributes.followRange);
        return iAttributeInstance == null ? 16.0 : iAttributeInstance.getAttributeValue();
    }

    @Override
    public void startExecuting() {
        this.field_179434_b.setAttackTarget(this.field_179433_e);
        super.startExecuting();
    }

    @Override
    public void resetTask() {
        this.field_179434_b.setAttackTarget(null);
        super.startExecuting();
    }

    public EntityAIFindEntityNearestPlayer(EntityLiving entityLiving) {
        this.field_179434_b = entityLiving;
        if (entityLiving instanceof EntityCreature) {
            field_179436_a.warn("Use NearestAttackableTargetGoal.class for PathfinerMob mobs!");
        }
        this.field_179435_c = new Predicate<Entity>(){

            public boolean apply(Entity entity) {
                if (!(entity instanceof EntityPlayer)) {
                    return false;
                }
                if (((EntityPlayer)entity).capabilities.disableDamage) {
                    return false;
                }
                double d = EntityAIFindEntityNearestPlayer.this.func_179431_f();
                if (entity.isSneaking()) {
                    d *= (double)0.8f;
                }
                if (entity.isInvisible()) {
                    float f = ((EntityPlayer)entity).getArmorVisibility();
                    if (f < 0.1f) {
                        f = 0.1f;
                    }
                    d *= (double)(0.7f * f);
                }
                return (double)entity.getDistanceToEntity(EntityAIFindEntityNearestPlayer.this.field_179434_b) > d ? false : EntityAITarget.isSuitableTarget(EntityAIFindEntityNearestPlayer.this.field_179434_b, (EntityLivingBase)entity, false, true);
            }
        };
        this.field_179432_d = new EntityAINearestAttackableTarget.Sorter(entityLiving);
    }

    @Override
    public boolean shouldExecute() {
        double d = this.func_179431_f();
        List<Entity> list = this.field_179434_b.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.field_179434_b.getEntityBoundingBox().expand(d, 4.0, d), this.field_179435_c);
        Collections.sort(list, this.field_179432_d);
        if (list.isEmpty()) {
            return false;
        }
        this.field_179433_e = (EntityLivingBase)list.get(0);
        return true;
    }

    @Override
    public boolean continueExecuting() {
        EntityLivingBase entityLivingBase = this.field_179434_b.getAttackTarget();
        if (entityLivingBase == null) {
            return false;
        }
        if (!entityLivingBase.isEntityAlive()) {
            return false;
        }
        if (entityLivingBase instanceof EntityPlayer && ((EntityPlayer)entityLivingBase).capabilities.disableDamage) {
            return false;
        }
        Team team = this.field_179434_b.getTeam();
        Team team2 = entityLivingBase.getTeam();
        if (team != null && team2 == team) {
            return false;
        }
        double d = this.func_179431_f();
        return this.field_179434_b.getDistanceSqToEntity(entityLivingBase) > d * d ? false : !(entityLivingBase instanceof EntityPlayerMP) || !((EntityPlayerMP)entityLivingBase).theItemInWorldManager.isCreative();
    }
}

