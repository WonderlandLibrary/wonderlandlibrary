// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.EntityCreature;

public class EntityAIHurtByTarget extends EntityAITarget
{
    private boolean entityCallsForHelp;
    private int revengeTimerOld;
    private final Class[] field_179447_c;
    private static final String __OBFID = "CL_00001619";
    
    public EntityAIHurtByTarget(final EntityCreature p_i45885_1_, final boolean p_i45885_2_, final Class... p_i45885_3_) {
        super(p_i45885_1_, false);
        this.entityCallsForHelp = p_i45885_2_;
        this.field_179447_c = p_i45885_3_;
        this.setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        final int var1 = this.taskOwner.getRevengeTimer();
        return var1 != this.revengeTimerOld && this.isSuitableTarget(this.taskOwner.getAITarget(), false);
    }
    
    @Override
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.taskOwner.getAITarget());
        this.revengeTimerOld = this.taskOwner.getRevengeTimer();
        if (this.entityCallsForHelp) {
            final double var1 = this.getTargetDistance();
            final List var2 = this.taskOwner.worldObj.getEntitiesWithinAABB(this.taskOwner.getClass(), new AxisAlignedBB(this.taskOwner.posX, this.taskOwner.posY, this.taskOwner.posZ, this.taskOwner.posX + 1.0, this.taskOwner.posY + 1.0, this.taskOwner.posZ + 1.0).expand(var1, 10.0, var1));
            for (final EntityCreature var4 : var2) {
                if (this.taskOwner != var4 && var4.getAttackTarget() == null && !var4.isOnSameTeam(this.taskOwner.getAITarget())) {
                    boolean var5 = false;
                    for (final Class var9 : this.field_179447_c) {
                        if (var4.getClass() == var9) {
                            var5 = true;
                            break;
                        }
                    }
                    if (var5) {
                        continue;
                    }
                    this.func_179446_a(var4, this.taskOwner.getAITarget());
                }
            }
        }
        super.startExecuting();
    }
    
    protected void func_179446_a(final EntityCreature p_179446_1_, final EntityLivingBase p_179446_2_) {
        p_179446_1_.setAttackTarget(p_179446_2_);
    }
}
