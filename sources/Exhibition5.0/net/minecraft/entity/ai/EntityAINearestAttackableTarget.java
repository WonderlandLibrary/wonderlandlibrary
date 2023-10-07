// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.ai;

import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import com.google.common.base.Predicates;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import com.google.common.base.Predicate;

public class EntityAINearestAttackableTarget extends EntityAITarget
{
    protected final Class targetClass;
    private final int targetChance;
    protected final Sorter theNearestAttackableTargetSorter;
    protected Predicate targetEntitySelector;
    protected EntityLivingBase targetEntity;
    private static final String __OBFID = "CL_00001620";
    
    public EntityAINearestAttackableTarget(final EntityCreature p_i45878_1_, final Class p_i45878_2_, final boolean p_i45878_3_) {
        this(p_i45878_1_, p_i45878_2_, p_i45878_3_, false);
    }
    
    public EntityAINearestAttackableTarget(final EntityCreature p_i45879_1_, final Class p_i45879_2_, final boolean p_i45879_3_, final boolean p_i45879_4_) {
        this(p_i45879_1_, p_i45879_2_, 10, p_i45879_3_, p_i45879_4_, null);
    }
    
    public EntityAINearestAttackableTarget(final EntityCreature p_i45880_1_, final Class p_i45880_2_, final int p_i45880_3_, final boolean p_i45880_4_, final boolean p_i45880_5_, final Predicate p_i45880_6_) {
        super(p_i45880_1_, p_i45880_4_, p_i45880_5_);
        this.targetClass = p_i45880_2_;
        this.targetChance = p_i45880_3_;
        this.theNearestAttackableTargetSorter = new Sorter(p_i45880_1_);
        this.setMutexBits(1);
        this.targetEntitySelector = (Predicate)new Predicate() {
            private static final String __OBFID = "CL_00001621";
            
            public boolean func_179878_a(final EntityLivingBase p_179878_1_) {
                if (p_i45880_6_ != null && !p_i45880_6_.apply((Object)p_179878_1_)) {
                    return false;
                }
                if (p_179878_1_ instanceof EntityPlayer) {
                    double var2 = EntityAINearestAttackableTarget.this.getTargetDistance();
                    if (p_179878_1_.isSneaking()) {
                        var2 *= 0.800000011920929;
                    }
                    if (p_179878_1_.isInvisible()) {
                        float var3 = ((EntityPlayer)p_179878_1_).getArmorVisibility();
                        if (var3 < 0.1f) {
                            var3 = 0.1f;
                        }
                        var2 *= 0.7f * var3;
                    }
                    if (p_179878_1_.getDistanceToEntity(EntityAINearestAttackableTarget.this.taskOwner) > var2) {
                        return false;
                    }
                }
                return EntityAINearestAttackableTarget.this.isSuitableTarget(p_179878_1_, false);
            }
            
            public boolean apply(final Object p_apply_1_) {
                return this.func_179878_a((EntityLivingBase)p_apply_1_);
            }
        };
    }
    
    @Override
    public boolean shouldExecute() {
        if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0) {
            return false;
        }
        final double var1 = this.getTargetDistance();
        final List var2 = this.taskOwner.worldObj.func_175647_a(this.targetClass, this.taskOwner.getEntityBoundingBox().expand(var1, 4.0, var1), Predicates.and(this.targetEntitySelector, IEntitySelector.field_180132_d));
        Collections.sort((List<Object>)var2, this.theNearestAttackableTargetSorter);
        if (var2.isEmpty()) {
            return false;
        }
        this.targetEntity = var2.get(0);
        return true;
    }
    
    @Override
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.targetEntity);
        super.startExecuting();
    }
    
    public static class Sorter implements Comparator
    {
        private final Entity theEntity;
        private static final String __OBFID = "CL_00001622";
        
        public Sorter(final Entity p_i1662_1_) {
            this.theEntity = p_i1662_1_;
        }
        
        public int compare(final Entity p_compare_1_, final Entity p_compare_2_) {
            final double var3 = this.theEntity.getDistanceSqToEntity(p_compare_1_);
            final double var4 = this.theEntity.getDistanceSqToEntity(p_compare_2_);
            return (var3 < var4) ? -1 : ((var3 > var4) ? 1 : 0);
        }
        
        @Override
        public int compare(final Object p_compare_1_, final Object p_compare_2_) {
            return this.compare((Entity)p_compare_1_, (Entity)p_compare_2_);
        }
    }
}
