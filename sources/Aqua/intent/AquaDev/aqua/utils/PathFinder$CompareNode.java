package intent.AquaDev.aqua.utils;

import intent.AquaDev.aqua.utils.PathFinder;
import java.util.Comparator;

public class PathFinder.CompareNode
implements Comparator<PathFinder.Node> {
    public int compare(PathFinder.Node o1, PathFinder.Node o2) {
        return (int)(o1.getSquareDistanceToFromTarget() + o1.getFCost() - (o2.getSquareDistanceToFromTarget() + o2.getFCost()));
    }
}
