package cn.hanabi.injection.interfaces;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;

public interface IEntity {
   int getNextStepDistance();

   void setNextStepDistance(int var1);

   int getFire();

   void setFire(int var1);

   AxisAlignedBB getBoundingBox();

   boolean canEntityBeSeenFixed(Entity var1);
}
