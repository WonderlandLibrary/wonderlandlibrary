package net.minecraft.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.pathfinder.NodeProcessor;

public class PathFinder {
   private Path path = new Path();
   private NodeProcessor nodeProcessor;
   private PathPoint[] pathOptions = new PathPoint[32];

   private PathEntity createEntityPathTo(IBlockAccess var1, Entity var2, double var3, double var5, double var7, float var9) {
      this.path.clearPath();
      this.nodeProcessor.initProcessor(var1, var2);
      PathPoint var10 = this.nodeProcessor.getPathPointTo(var2);
      PathPoint var11 = this.nodeProcessor.getPathPointToCoords(var2, var3, var5, var7);
      PathEntity var12 = this.addToPath(var2, var10, var11, var9);
      this.nodeProcessor.postProcess();
      return var12;
   }

   public PathFinder(NodeProcessor var1) {
      this.nodeProcessor = var1;
   }

   public PathEntity createEntityPathTo(IBlockAccess var1, Entity var2, Entity var3, float var4) {
      return this.createEntityPathTo(var1, var2, var3.posX, var3.getEntityBoundingBox().minY, var3.posZ, var4);
   }

   private PathEntity createEntityPath(PathPoint var1, PathPoint var2) {
      int var3 = 1;

      for(PathPoint var4 = var2; var4.previous != null; var4 = var4.previous) {
         ++var3;
      }

      PathPoint[] var6 = new PathPoint[var3];
      PathPoint var5 = var2;
      --var3;

      for(var6[var3] = var2; var5.previous != null; var6[var3] = var5) {
         var5 = var5.previous;
         --var3;
      }

      return new PathEntity(var6);
   }

   private PathEntity addToPath(Entity var1, PathPoint var2, PathPoint var3, float var4) {
      var2.totalPathDistance = 0.0F;
      var2.distanceToNext = var2.distanceToSquared(var3);
      var2.distanceToTarget = var2.distanceToNext;
      this.path.clearPath();
      this.path.addPoint(var2);
      PathPoint var5 = var2;

      while(!this.path.isPathEmpty()) {
         PathPoint var6 = this.path.dequeue();
         if (var6.equals(var3)) {
            return this.createEntityPath(var2, var3);
         }

         if (var6.distanceToSquared(var3) < var5.distanceToSquared(var3)) {
            var5 = var6;
         }

         var6.visited = true;
         int var7 = this.nodeProcessor.findPathOptions(this.pathOptions, var1, var6, var3, var4);

         for(int var8 = 0; var8 < var7; ++var8) {
            PathPoint var9 = this.pathOptions[var8];
            float var10 = var6.totalPathDistance + var6.distanceToSquared(var9);
            if (var10 < var4 * 2.0F && (!var9.isAssigned() || var10 < var9.totalPathDistance)) {
               var9.previous = var6;
               var9.totalPathDistance = var10;
               var9.distanceToNext = var9.distanceToSquared(var3);
               if (var9.isAssigned()) {
                  this.path.changeDistance(var9, var9.totalPathDistance + var9.distanceToNext);
               } else {
                  var9.distanceToTarget = var9.totalPathDistance + var9.distanceToNext;
                  this.path.addPoint(var9);
               }
            }
         }
      }

      if (var5 == var2) {
         return null;
      } else {
         return this.createEntityPath(var2, var5);
      }
   }

   public PathEntity createEntityPathTo(IBlockAccess var1, Entity var2, BlockPos var3, float var4) {
      return this.createEntityPathTo(var1, var2, (double)((float)var3.getX() + 0.5F), (double)((float)var3.getY() + 0.5F), (double)((float)var3.getZ() + 0.5F), var4);
   }
}
