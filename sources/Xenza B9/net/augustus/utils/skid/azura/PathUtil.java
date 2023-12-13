// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.utils.skid.azura;

import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import java.util.Iterator;
import net.minecraft.client.Minecraft;

public class PathUtil
{
    private static final Minecraft mc;
    
    public static Path getPath(final CustomVec3 start, final CustomVec3 end, final double offset) {
        Path path = findPath(start, end, offset);
        if (areBlocksBetween(path)) {
            final Path newPath = new Path(start, end);
            newPath.getAdditionalVectors().clear();
            CustomVec3 last = path.getStart();
            for (final CustomVec3 v : path.getAdditionalVectors()) {
                if (areBlocksBetween(last, v)) {
                    last = v;
                    newPath.addAdditionalVector(v);
                }
            }
            path = newPath;
        }
        return path;
    }
    
    public static Path findPath(final CustomVec3 start, final CustomVec3 end) {
        return findPath(start, end, 1.0);
    }
    
    public static Path findPath(final CustomVec3 start, final CustomVec3 end, final double offset) {
        final Path path = new Path(start, end);
        final double dist = Math.ceil(start.getDistance(end) / offset);
        final double dX = end.getX() - start.getX();
        final double dY = end.getY() - start.getY();
        final double dZ = end.getZ() - start.getZ();
        for (double d = 1.0; d <= dist; ++d) {
            final double x1 = dX * d / dist;
            final double y1 = dY * d / dist;
            final double z1 = dZ * d / dist;
            path.addAdditionalVector(start.offset(x1, y1, z1));
        }
        return path;
    }
    
    public static boolean areBlocksBetween(final Path p) {
        boolean blocks = false;
        CustomVec3 lastCustom = p.getStart();
        Vec3 last = new Vec3(p.getStart().getX(), p.getStart().getY(), p.getStart().getZ());
        for (final CustomVec3 v : p.getAdditionalVectors()) {
            if (v != p.getStart()) {
                if (v == p.getEnd()) {
                    continue;
                }
                final Vec3 current = new Vec3(v.getX(), v.getY(), v.getZ());
                if (lastCustom.equals(p.getStart())) {
                    last = current;
                    lastCustom = v;
                }
                final MovingObjectPosition pos = PathUtil.mc.theWorld.rayTraceBlocks(last, current, false, false, true);
                if (pos != null && pos.typeOfHit.equals(MovingObjectPosition.MovingObjectType.BLOCK)) {
                    blocks = true;
                }
                last = current;
            }
        }
        return blocks;
    }
    
    public static boolean areBlocksBetween(final CustomVec3 start, final CustomVec3 end) {
        return areBlocksBetween(findPath(start, end));
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
}
