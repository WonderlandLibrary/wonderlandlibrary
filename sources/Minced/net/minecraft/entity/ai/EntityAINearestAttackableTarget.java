// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.util.math.AxisAlignedBB;
import java.util.List;
import java.util.Comparator;
import java.util.Collections;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import com.google.common.base.Function;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntitySelectors;
import net.minecraft.entity.Entity;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityCreature;
import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLivingBase;

public class EntityAINearestAttackableTarget<T extends EntityLivingBase> extends EntityAITarget
{
    protected final Class<T> targetClass;
    private final int targetChance;
    protected final Sorter sorter;
    protected final Predicate<? super T> targetEntitySelector;
    protected T targetEntity;
    
    public EntityAINearestAttackableTarget(final EntityCreature creature, final Class<T> classTarget, final boolean checkSight) {
        this(creature, classTarget, checkSight, false);
    }
    
    public EntityAINearestAttackableTarget(final EntityCreature creature, final Class<T> classTarget, final boolean checkSight, final boolean onlyNearby) {
        this(creature, classTarget, 10, checkSight, onlyNearby, null);
    }
    
    public EntityAINearestAttackableTarget(final EntityCreature creature, final Class<T> classTarget, final int chance, final boolean checkSight, final boolean onlyNearby, @Nullable final Predicate<? super T> targetSelector) {
        super(creature, checkSight, onlyNearby);
        this.targetClass = classTarget;
        this.targetChance = chance;
        this.sorter = new Sorter(creature);
        this.setMutexBits(1);
        this.targetEntitySelector = (Predicate<? super T>)new Predicate<T>() {
            public boolean apply(@Nullable final T p_apply_1_) {
                return p_apply_1_ != null && (targetSelector == null || targetSelector.apply((Object)p_apply_1_)) && EntitySelectors.NOT_SPECTATING.apply((Object)p_apply_1_) && EntityAINearestAttackableTarget.this.isSuitableTarget(p_apply_1_, false);
            }
        };
    }
    
    @Override
    public boolean shouldExecute() {
        if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0) {
            return false;
        }
        if (this.targetClass == EntityPlayer.class || this.targetClass == EntityPlayerMP.class) {
            this.targetEntity = (T)this.taskOwner.world.getNearestAttackablePlayer(this.taskOwner.posX, this.taskOwner.posY + this.taskOwner.getEyeHeight(), this.taskOwner.posZ, this.getTargetDistance(), this.getTargetDistance(), (Function<EntityPlayer, Double>)new Function<EntityPlayer, Double>() {
                @Nullable
                public Double apply(@Nullable final EntityPlayer p_apply_1_) {
                    final ItemStack itemstack = p_apply_1_.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                    if (itemstack.getItem() == Items.SKULL) {
                        final int i = itemstack.getItemDamage();
                        final boolean flag = EntityAINearestAttackableTarget.this.taskOwner instanceof EntitySkeleton && i == 0;
                        final boolean flag2 = EntityAINearestAttackableTarget.this.taskOwner instanceof EntityZombie && i == 2;
                        final boolean flag3 = EntityAINearestAttackableTarget.this.taskOwner instanceof EntityCreeper && i == 4;
                        if (flag || flag2 || flag3) {
                            return 0.5;
                        }
                    }
                    return 1.0;
                }
            }, (Predicate<EntityPlayer>)this.targetEntitySelector);
            return this.targetEntity != null;
        }
        final List<T> list = this.taskOwner.world.getEntitiesWithinAABB((Class<? extends T>)this.targetClass, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector);
        if (list.isEmpty()) {
            return false;
        }
        Collections.sort(list, this.sorter);
        this.targetEntity = list.get(0);
        return true;
    }
    
    protected AxisAlignedBB getTargetableArea(final double targetDistance) {
        return this.taskOwner.getEntityBoundingBox().grow(targetDistance, 4.0, targetDistance);
    }
    
    @Override
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.targetEntity);
        super.startExecuting();
    }
    
    public static class Sorter implements Comparator<Entity>
    {
        private final Entity entity;
        
        public Sorter(final Entity entityIn) {
            this.entity = entityIn;
        }
        
        @Override
        public int compare(final Entity p_compare_1_, final Entity p_compare_2_) {
            final double d0 = this.entity.getDistanceSq(p_compare_1_);
            final double d2 = this.entity.getDistanceSq(p_compare_2_);
            if (d0 < d2) {
                return -1;
            }
            return (d0 > d2) ? 1 : 0;
        }
    }
}
