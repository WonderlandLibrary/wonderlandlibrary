package org.newdawn.slick.util.pathfinding.heuristics;

import org.newdawn.slick.util.pathfinding.AStarHeuristic;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

public class ClosestHeuristic implements AStarHeuristic {
   public float getCost(TileBasedMap var1, Mover var2, int var3, int var4, int var5, int var6) {
      float var7 = (float)(var5 - var3);
      float var8 = (float)(var6 - var4);
      float var9 = (float)Math.sqrt((double)(var7 * var7 + var8 * var8));
      return var9;
   }
}
