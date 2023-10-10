package rosegoldclient.utils.pathfinding;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class Pathfinder {
    private static AStarCustomPathfinder pathfinder;
    public static ArrayList<Vec3d> path;
    public static BlockPos goal;


    public static void setup(BlockPos from, BlockPos to, double minDistance) {
        pathfinder = new AStarCustomPathfinder(new Vec3d(from), new Vec3d(to), minDistance);
        pathfinder.compute();
        path = pathfinder.getPath();
        goal = to;
    }

    public static Vec3d getCurrent() {
        return path != null && !path.isEmpty() ? path.get(0) : null;
    }

    public static boolean hasNext() {
        return path != null && path.size() > 1;
    }

    public static Vec3d getNext() {
        return path.get(1);
    }

    public static boolean goNext() {
        if (path != null && path.size() > 1) {
            path.remove(0);
            return true;
        } else {
            path = null;
            return false;
        }
    }

    public static boolean hasPath() {
        return path != null && !path.isEmpty();
    }

    public static Vec3d getGoal() {
        return path.get(path.size() - 1);
    }

}
