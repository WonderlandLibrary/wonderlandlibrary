/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.vecmath.Vector3d
 */
package net.ccbluex.liquidbounce.utils;

import java.util.ArrayList;
import java.util.List;
import javax.vecmath.Vector3d;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

public final class PathUtils
extends MinecraftInstance {
    public static List<Vector3d> findBlinkPath(double tpX, double tpY, double tpZ) {
        ArrayList<Vector3d> positions = new ArrayList<Vector3d>();
        double curX = mc.getThePlayer().getPosX();
        double curY = mc.getThePlayer().getPosY();
        double curZ = mc.getThePlayer().getPosZ();
        double distance = Math.abs(curX - tpX) + Math.abs(curY - tpY) + Math.abs(curZ - tpZ);
        int count = 0;
        while (distance > 0.0) {
            distance = Math.abs(curX - tpX) + Math.abs(curY - tpY) + Math.abs(curZ - tpZ);
            double diffX = curX - tpX;
            double diffY = curY - tpY;
            double diffZ = curZ - tpZ;
            double offset = !(count & true) ? 0.4 : 0.1;
            double minX = Math.min(Math.abs(diffX), offset);
            if (diffX < 0.0) {
                curX += minX;
            }
            if (diffX > 0.0) {
                curX -= minX;
            }
            double minY = Math.min(Math.abs(diffY), 0.25);
            if (diffY < 0.0) {
                curY += minY;
            }
            if (diffY > 0.0) {
                curY -= minY;
            }
            double minZ = Math.min(Math.abs(diffZ), offset);
            if (diffZ < 0.0) {
                curZ += minZ;
            }
            if (diffZ > 0.0) {
                curZ -= minZ;
            }
            positions.add(new Vector3d(curX, curY, curZ));
            ++count;
        }
        return positions;
    }

    public static List<Vector3d> findPath(double tpX, double tpY, double tpZ, double offset) {
        ArrayList<Vector3d> positions = new ArrayList<Vector3d>();
        double steps = Math.ceil(PathUtils.getDistance(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY(), mc.getThePlayer().getPosZ(), tpX, tpY, tpZ) / offset);
        double dX = tpX - mc.getThePlayer().getPosX();
        double dY = tpY - mc.getThePlayer().getPosY();
        double dZ = tpZ - mc.getThePlayer().getPosZ();
        for (double d = 1.0; d <= steps; d += 1.0) {
            positions.add(new Vector3d(mc.getThePlayer().getPosX() + dX * d / steps, mc.getThePlayer().getPosY() + dY * d / steps, mc.getThePlayer().getPosZ() + dZ * d / steps));
        }
        return positions;
    }

    private static double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double xDiff = x1 - x2;
        double yDiff = y1 - y2;
        double zDiff = z1 - z2;
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
    }
}

