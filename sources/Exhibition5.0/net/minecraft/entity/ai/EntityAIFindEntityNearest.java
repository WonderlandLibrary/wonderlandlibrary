// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.ai;

import org.apache.logging.log4j.LogManager;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayerMP;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLiving;
import org.apache.logging.log4j.Logger;

public class EntityAIFindEntityNearest extends EntityAIBase
{
    private static final Logger field_179444_a;
    private EntityLiving field_179442_b;
    private final Predicate field_179443_c;
    private final EntityAINearestAttackableTarget.Sorter field_179440_d;
    private EntityLivingBase field_179441_e;
    private Class field_179439_f;
    private static final String __OBFID = "CL_00002250";
    
    public EntityAIFindEntityNearest(final EntityLiving p_i45884_1_, final Class p_i45884_2_) {
        this.field_179442_b = p_i45884_1_;
        this.field_179439_f = p_i45884_2_;
        if (p_i45884_1_ instanceof EntityCreature) {
            EntityAIFindEntityNearest.field_179444_a.warn("Use NearestAttackableTargetGoal.class for PathfinerMob mobs!");
        }
        this.field_179443_c = (Predicate)new Predicate() {
            private static final String __OBFID = "CL_00002249";
            
            public boolean func_179876_a(final EntityLivingBase p_179876_1_) {
                double var2 = EntityAIFindEntityNearest.this.func_179438_f();
                if (p_179876_1_.isSneaking()) {
                    var2 *= 0.800000011920929;
                }
                return !p_179876_1_.isInvisible() && p_179876_1_.getDistanceToEntity(EntityAIFindEntityNearest.this.field_179442_b) <= var2 && EntityAITarget.func_179445_a(EntityAIFindEntityNearest.this.field_179442_b, p_179876_1_, false, true);
            }
            
            public boolean apply(final Object p_apply_1_) {
                return this.func_179876_a((EntityLivingBase)p_apply_1_);
            }
        };
        this.field_179440_d = new EntityAINearestAttackableTarget.Sorter(p_i45884_1_);
    }
    
    @Override
    public boolean shouldExecute() {
        final double var1 = this.func_179438_f();
        final List var2 = this.field_179442_b.worldObj.func_175647_a(this.field_179439_f, this.field_179442_b.getEntityBoundingBox().expand(var1, 4.0, var1), this.field_179443_c);
        Collections.sort((List<Object>)var2, this.field_179440_d);
        if (var2.isEmpty()) {
            return false;
        }
        this.field_179441_e = var2.get(0);
        return true;
    }
    
    @Override
    public boolean continueExecuting() {
        final EntityLivingBase var1 = this.field_179442_b.getAttackTarget();
        if (var1 == null) {
            return false;
        }
        if (!var1.isEntityAlive()) {
            return false;
        }
        final double var2 = this.func_179438_f();
        return this.field_179442_b.getDistanceSqToEntity(var1) <= var2 * var2 && (!(var1 instanceof EntityPlayerMP) || !((EntityPlayerMP)var1).theItemInWorldManager.isCreative());
    }
    
    @Override
    public void startExecuting() {
        this.field_179442_b.setAttackTarget(this.field_179441_e);
        super.startExecuting();
    }
    
    @Override
    public void resetTask() {
        this.field_179442_b.setAttackTarget(null);
        super.startExecuting();
    }
    
    protected double func_179438_f() {
        final IAttributeInstance var1 = this.field_179442_b.getEntityAttribute(SharedMonsterAttributes.followRange);
        return (var1 == null) ? 16.0 : var1.getAttributeValue();
    }
    
    static {
        field_179444_a = LogManager.getLogger();
    }
}
