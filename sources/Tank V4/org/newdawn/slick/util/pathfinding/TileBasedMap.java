package org.newdawn.slick.util.pathfinding;

public interface TileBasedMap {
   int getWidthInTiles();

   int getHeightInTiles();

   void pathFinderVisited(int var1, int var2);

   boolean blocked(PathFindingContext var1, int var2, int var3);

   float getCost(PathFindingContext var1, int var2, int var3);
}
