package org.newdawn.slick.util.pathfinding.heuristics;

import org.newdawn.slick.util.pathfinding.AStarHeuristic;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

public class ManhattanHeuristic implements AStarHeuristic {
   private int minimumCost;

   public ManhattanHeuristic(int var1) {
      this.minimumCost = var1;
   }

   public float getCost(TileBasedMap var1, Mover var2, int var3, int var4, int var5, int var6) {
      return (float)(this.minimumCost * (Math.abs(var3 - var5) + Math.abs(var4 - var6)));
   }
}
