package org.newdawn.slick.util.pathfinding;

public interface PathFindingContext {
   Mover getMover();

   int getSourceX();

   int getSourceY();

   int getSearchDistance();
}
