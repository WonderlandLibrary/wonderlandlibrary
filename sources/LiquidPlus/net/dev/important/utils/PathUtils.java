/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.vecmath.Vector3d
 *  net.minecraft.block.Block
 *  net.minecraft.block.material.Material
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.Vec3
 */
package net.dev.important.utils;

import java.util.ArrayList;
import java.util.List;
import javax.vecmath.Vector3d;
import net.dev.important.utils.AStarCustomPathFinder;
import net.dev.important.utils.MinecraftInstance;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public final class PathUtils
extends MinecraftInstance {
    public static ArrayList<Vec3> findTeleportPath(EntityLivingBase current, EntityLivingBase target, double dashDistance) {
        double curX = current.field_70165_t;
        double curY = current.field_70163_u;
        double curZ = current.field_70161_v;
        double tpX = target.field_70165_t;
        double tpY = target.field_70163_u;
        double tpZ = target.field_70161_v;
        Vec3 topFrom = new Vec3(curX, curY, curZ);
        Vec3 to = new Vec3(tpX, tpY, tpZ);
        if (!PathUtils.canPassThrow(new BlockPos(topFrom))) {
            topFrom = topFrom.func_72441_c(0.0, 1.0, 0.0);
        }
        AStarCustomPathFinder pathfinder = new AStarCustomPathFinder(topFrom, to);
        pathfinder.compute();
        int i = 0;
        Vec3 lastLoc = null;
        Vec3 lastDashLoc = null;
        ArrayList<Vec3> path = new ArrayList<Vec3>();
        ArrayList<Vec3> pathFinderPath = pathfinder.getPath();
        for (Vec3 pathElm : pathFinderPath) {
            if (i == 0 || i == pathFinderPath.size() - 1) {
                if (lastLoc != null) {
                    path.add(lastLoc.func_72441_c(0.5, 0.0, 0.5));
                }
                path.add(pathElm.func_72441_c(0.5, 0.0, 0.5));
                lastDashLoc = pathElm;
            } else {
                boolean canContinue = true;
                if (pathElm.func_72436_e(lastDashLoc) > dashDistance * dashDistance) {
                    canContinue = false;
                } else {
                    double smallX = Math.min(lastDashLoc.field_72450_a, pathElm.field_72450_a);
                    double smallY = Math.min(lastDashLoc.field_72448_b, pathElm.field_72448_b);
                    double smallZ = Math.min(lastDashLoc.field_72449_c, pathElm.field_72449_c);
                    double bigX = Math.max(lastDashLoc.field_72450_a, pathElm.field_72450_a);
                    double bigY = Math.max(lastDashLoc.field_72448_b, pathElm.field_72448_b);
                    double bigZ = Math.max(lastDashLoc.field_72449_c, pathElm.field_72449_c);
                    int x = (int)smallX;
                    block1: while ((double)x <= bigX) {
                        int y = (int)smallY;
                        while ((double)y <= bigY) {
                            int z = (int)smallZ;
                            while ((double)z <= bigZ) {
                                if (!AStarCustomPathFinder.checkPositionValidity(x, y, z, false)) {
                                    canContinue = false;
                                    break block1;
                                }
                                ++z;
                            }
                            ++y;
                        }
                        ++x;
                    }
                }
                if (!canContinue) {
                    path.add(lastLoc.func_72441_c(0.5, 0.0, 0.5));
                    lastDashLoc = lastLoc;
                }
            }
            lastLoc = pathElm;
            ++i;
        }
        return path;
    }

    private static boolean canPassThrow(BlockPos pos) {
        Block block = Minecraft.func_71410_x().field_71441_e.func_180495_p(new BlockPos(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p())).func_177230_c();
        return block.func_149688_o() == Material.field_151579_a || block.func_149688_o() == Material.field_151585_k || block.func_149688_o() == Material.field_151582_l || block == Blocks.field_150468_ap || block == Blocks.field_150355_j || block == Blocks.field_150358_i || block == Blocks.field_150444_as || block == Blocks.field_150472_an;
    }

    public static List<Vector3d> findBlinkPath(double tpX, double tpY, double tpZ) {
        ArrayList<Vector3d> positions = new ArrayList<Vector3d>();
        double curX = PathUtils.mc.field_71439_g.field_70165_t;
        double curY = PathUtils.mc.field_71439_g.field_70163_u;
        double curZ = PathUtils.mc.field_71439_g.field_70161_v;
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
        double steps = Math.ceil(PathUtils.getDistance(PathUtils.mc.field_71439_g.field_70165_t, PathUtils.mc.field_71439_g.field_70163_u, PathUtils.mc.field_71439_g.field_70161_v, tpX, tpY, tpZ) / offset);
        double dX = tpX - PathUtils.mc.field_71439_g.field_70165_t;
        double dY = tpY - PathUtils.mc.field_71439_g.field_70163_u;
        double dZ = tpZ - PathUtils.mc.field_71439_g.field_70161_v;
        for (double d = 1.0; d <= steps; d += 1.0) {
            positions.add(new Vector3d(PathUtils.mc.field_71439_g.field_70165_t + dX * d / steps, PathUtils.mc.field_71439_g.field_70163_u + dY * d / steps, PathUtils.mc.field_71439_g.field_70161_v + dZ * d / steps));
        }
        return positions;
    }

    private static double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double xDiff = x1 - x2;
        double yDiff = y1 - y2;
        double zDiff = z1 - z2;
        return MathHelper.func_76133_a((double)(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff));
    }
}

