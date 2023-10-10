package net.minecraft.world.pathfinder;

import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

public abstract class NodeProcessor {
   protected int entitySizeZ;
   protected int entitySizeX;
   protected int entitySizeY;
   protected IBlockAccess blockaccess;
   protected IntHashMap pointMap = new IntHashMap();

   public abstract PathPoint getPathPointToCoords(Entity var1, double var2, double var4, double var6);

   public void initProcessor(IBlockAccess var1, Entity var2) {
      this.blockaccess = var1;
      this.pointMap.clearMap();
      this.entitySizeX = MathHelper.floor_float(var2.width + 1.0F);
      this.entitySizeY = MathHelper.floor_float(var2.height + 1.0F);
      this.entitySizeZ = MathHelper.floor_float(var2.width + 1.0F);
   }

   protected PathPoint openPoint(int var1, int var2, int var3) {
      int var4 = PathPoint.makeHash(var1, var2, var3);
      PathPoint var5 = (PathPoint)this.pointMap.lookup(var4);
      if (var5 == null) {
         var5 = new PathPoint(var1, var2, var3);
         this.pointMap.addKey(var4, var5);
      }

      return var5;
   }

   public abstract PathPoint getPathPointTo(Entity var1);

   public void postProcess() {
   }

   public abstract int findPathOptions(PathPoint[] var1, Entity var2, PathPoint var3, PathPoint var4, float var5);
}
