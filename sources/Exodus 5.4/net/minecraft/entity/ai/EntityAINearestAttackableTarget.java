/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.base.Predicates
 */
package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntitySelectors;

public class EntityAINearestAttackableTarget<T extends EntityLivingBase>
extends EntityAITarget {
    private final int targetChance;
    protected Predicate<? super T> targetEntitySelector;
    protected final Class<T> targetClass;
    protected final Sorter theNearestAttackableTargetSorter;
    protected EntityLivingBase targetEntity;

    public EntityAINearestAttackableTarget(EntityCreature entityCreature, Class<T> clazz, boolean bl) {
        this(entityCreature, clazz, bl, false);
    }

    @Override
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.targetEntity);
        super.startExecuting();
    }

    public EntityAINearestAttackableTarget(EntityCreature entityCreature, Class<T> clazz, int n, boolean bl, boolean bl2, final Predicate<? super T> predicate) {
        super(entityCreature, bl, bl2);
        this.targetClass = clazz;
        this.targetChance = n;
        this.theNearestAttackableTargetSorter = new Sorter(entityCreature);
        this.setMutexBits(1);
        this.targetEntitySelector = new Predicate<T>(){

            public boolean apply(T t) {
                if (predicate != null && !predicate.apply(t)) {
                    return false;
                }
                if (t instanceof EntityPlayer) {
                    double d = EntityAINearestAttackableTarget.this.getTargetDistance();
                    if (((Entity)t).isSneaking()) {
                        d *= (double)0.8f;
                    }
                    if (((Entity)t).isInvisible()) {
                        float f = ((EntityPlayer)t).getArmorVisibility();
                        if (f < 0.1f) {
                            f = 0.1f;
                        }
                        d *= (double)(0.7f * f);
                    }
                    if ((double)((Entity)t).getDistanceToEntity(EntityAINearestAttackableTarget.this.taskOwner) > d) {
                        return false;
                    }
                }
                return EntityAINearestAttackableTarget.this.isSuitableTarget((EntityLivingBase)t, false);
            }
        };
    }

    @Override
    public boolean shouldExecute() {
        if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0) {
            return false;
        }
        double d = this.getTargetDistance();
        List<T> list = this.taskOwner.worldObj.getEntitiesWithinAABB(this.targetClass, this.taskOwner.getEntityBoundingBox().expand(d, 4.0, d), Predicates.and(this.targetEntitySelector, EntitySelectors.NOT_SPECTATING));
        Collections.sort(list, this.theNearestAttackableTargetSorter);
        if (list.isEmpty()) {
            return false;
        }
        this.targetEntity = (EntityLivingBase)list.get(0);
        return true;
    }

    public EntityAINearestAttackableTarget(EntityCreature entityCreature, Class<T> clazz, boolean bl, boolean bl2) {
        this(entityCreature, clazz, 10, bl, bl2, null);
    }

    public static class Sorter
    implements Comparator<Entity> {
        private final Entity theEntity;

        public Sorter(Entity entity) {
            this.theEntity = entity;
        }

        @Override
        public int compare(Entity entity, Entity entity2) {
            double d;
            double d2 = this.theEntity.getDistanceSqToEntity(entity);
            return d2 < (d = this.theEntity.getDistanceSqToEntity(entity2)) ? -1 : (d2 > d ? 1 : 0);
        }
    }
}

