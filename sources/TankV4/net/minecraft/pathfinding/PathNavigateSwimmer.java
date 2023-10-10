package net.minecraft.pathfinding;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.pathfinder.SwimNodeProcessor;

public class PathNavigateSwimmer extends PathNavigate {
   public PathNavigateSwimmer(EntityLiving var1, World var2) {
      super(var1, var2);
   }

   protected PathFinder getPathFinder() {
      return new PathFinder(new SwimNodeProcessor());
   }

   protected void removeSunnyPath() {
      super.removeSunnyPath();
   }

   protected Vec3 getEntityPosition() {
      return new Vec3(this.theEntity.posX, this.theEntity.posY + (double)this.theEntity.height * 0.5D, this.theEntity.posZ);
   }

   protected void pathFollow() {
      Vec3 var1 = this.getEntityPosition();
      float var2 = this.theEntity.width * this.theEntity.width;
      byte var3 = 6;
      if (var1.squareDistanceTo(this.currentPath.getVectorFromIndex(this.theEntity, this.currentPath.getCurrentPathIndex())) < (double)var2) {
         this.currentPath.incrementPathIndex();
      }

      for(int var4 = Math.min(this.currentPath.getCurrentPathIndex() + var3, this.currentPath.getCurrentPathLength() - 1); var4 > this.currentPath.getCurrentPathIndex(); --var4) {
         Vec3 var5 = this.currentPath.getVectorFromIndex(this.theEntity, var4);
         if (var5.squareDistanceTo(var1) <= 36.0D) {
            boolean var10003 = false;
            boolean var10004 = false;
            if (0 != null) {
               this.currentPath.setCurrentPathIndex(var4);
               break;
            }
         }
      }

      this.checkForStuck(var1);
   }

   protected boolean canNavigate() {
      return this.isInLiquid();
   }
}
