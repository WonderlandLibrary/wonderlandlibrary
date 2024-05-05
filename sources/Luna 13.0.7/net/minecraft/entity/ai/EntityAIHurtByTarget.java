package net.minecraft.entity.ai;

import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityAIHurtByTarget
  extends EntityAITarget
{
  private boolean entityCallsForHelp;
  private int revengeTimerOld;
  private final Class[] field_179447_c;
  private static final String __OBFID = "CL_00001619";
  
  public EntityAIHurtByTarget(EntityCreature p_i45885_1_, boolean p_i45885_2_, Class... p_i45885_3_)
  {
    super(p_i45885_1_, false);
    this.entityCallsForHelp = p_i45885_2_;
    this.field_179447_c = p_i45885_3_;
    setMutexBits(1);
  }
  
  public boolean shouldExecute()
  {
    int var1 = this.taskOwner.getRevengeTimer();
    return (var1 != this.revengeTimerOld) && (isSuitableTarget(this.taskOwner.getAITarget(), false));
  }
  
  public void startExecuting()
  {
    this.taskOwner.setAttackTarget(this.taskOwner.getAITarget());
    this.revengeTimerOld = this.taskOwner.getRevengeTimer();
    if (this.entityCallsForHelp)
    {
      double var1 = getTargetDistance();
      List var3 = this.taskOwner.worldObj.getEntitiesWithinAABB(this.taskOwner.getClass(), new AxisAlignedBB(this.taskOwner.posX, this.taskOwner.posY, this.taskOwner.posZ, this.taskOwner.posX + 1.0D, this.taskOwner.posY + 1.0D, this.taskOwner.posZ + 1.0D).expand(var1, 10.0D, var1));
      Iterator var4 = var3.iterator();
      while (var4.hasNext())
      {
        EntityCreature var5 = (EntityCreature)var4.next();
        if ((this.taskOwner != var5) && (var5.getAttackTarget() == null) && (!var5.isOnSameTeam(this.taskOwner.getAITarget())))
        {
          boolean var6 = false;
          Class[] var7 = this.field_179447_c;
          int var8 = var7.length;
          for (int var9 = 0; var9 < var8; var9++)
          {
            Class var10 = var7[var9];
            if (var5.getClass() == var10)
            {
              var6 = true;
              break;
            }
          }
          if (!var6) {
            func_179446_a(var5, this.taskOwner.getAITarget());
          }
        }
      }
    }
    super.startExecuting();
  }
  
  protected void func_179446_a(EntityCreature p_179446_1_, EntityLivingBase p_179446_2_)
  {
    p_179446_1_.setAttackTarget(p_179446_2_);
  }
}
