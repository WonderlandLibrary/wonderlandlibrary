package best.azura.client.util.pathing;

import best.azura.client.util.math.CustomVec3;
import net.minecraft.client.Minecraft;
import net.minecraft.util.*;

public class PathUtil {
    
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static Path getPath(CustomVec3 start, CustomVec3 end, double offset) {
        Path path = findPath(start, end, offset);
        if (areBlocksBetween(path)) {
            Path newPath = new Path(start, end);
            newPath.getAdditionalVectors().clear();
            CustomVec3 last = path.getStart();
            for (CustomVec3 v : path.getAdditionalVectors()) {
                if (areBlocksBetween(last, v)) {
                    last = v;
                    newPath.addAdditionalVector(v);
                }
            }
            path = newPath;
        }
        return path;
    }

    public static Path findPath(CustomVec3 start, CustomVec3 end) {
        return findPath(start, end, 1);
    }

    public static Path findPath(CustomVec3 start, CustomVec3 end, double offset) {
        Path path = new Path(start, end);
        double dist = Math.ceil(start.getDistance(end) / offset);
        double dX = end.getX() - start.getX();
        double dY = end.getY() - start.getY();
        double dZ = end.getZ() - start.getZ();
        for (double d = 1; d <= dist; d++) {
            double x1 = (dX * d) / dist;
            double y1 = (dY * d) / dist;
            double z1 = (dZ * d) / dist;
            path.addAdditionalVector(start.offset(x1, y1, z1));
        }
        return path;
    }

    public static boolean areBlocksBetween(Path p) {
        boolean blocks = false;
        CustomVec3 lastCustom = p.getStart();
        Vec3 last = new Vec3(p.getStart().getX(), p.getStart().getY(), p.getStart().getZ());
        for (CustomVec3 v : p.getAdditionalVectors()) {
            if (v == p.getStart() || v == p.getEnd()) continue;
            Vec3 current = new Vec3(v.getX(), v.getY(), v.getZ());
            if (lastCustom.equals(p.getStart())) {
                last = current;
                lastCustom = v;
            }
            MovingObjectPosition pos = mc.theWorld.rayTraceBlocks(last, current, false, false, true);
            if (pos != null && pos.typeOfHit.equals(MovingObjectPosition.MovingObjectType.BLOCK)) {
                blocks = true;
            }
            last = current;
        }
        return blocks;
    }

    public static boolean areBlocksBetween(CustomVec3 start, CustomVec3 end) {
        return areBlocksBetween(findPath(start, end));
    }

}