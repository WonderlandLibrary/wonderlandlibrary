package com.masterof13fps.utils.render.slick.util.pathfinding.heuristics;

import com.masterof13fps.utils.render.slick.util.pathfinding.AStarHeuristic;
import com.masterof13fps.utils.render.slick.util.pathfinding.Mover;
import com.masterof13fps.utils.render.slick.util.pathfinding.TileBasedMap;

/**
 * A heuristic that uses the tile that is closest to the target
 * as the next best tile.
 * 
 * @author Kevin Glass
 */
public class ClosestHeuristic implements AStarHeuristic {
	/**
	 * @see AStarHeuristic#getCost(TileBasedMap, Mover, int, int, int, int)
	 */
	public float getCost(TileBasedMap map, Mover mover, int x, int y, int tx, int ty) {		
		float dx = tx - x;
		float dy = ty - y;
		
		float result = (float) (Math.sqrt((dx*dx)+(dy*dy)));
		
		return result;
	}

}
