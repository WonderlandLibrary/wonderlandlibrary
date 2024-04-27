package net.ccbluex.liquidbounce.utils;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;

public class PathfindingUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static ArrayList<CustomVec3> computePath(CustomVec3 topFrom, CustomVec3 to) {
        if (!PathfindingUtils.canPassThrow(new BlockPos(topFrom.mc()))) {
            topFrom = topFrom.addVector(0.0, 1.0, 0.0);
        }
        AStarCustomPathFinder pathfinder = new AStarCustomPathFinder(topFrom, to);
        pathfinder.compute();
        int i = 0;
        CustomVec3 lastLoc = null;
        CustomVec3 lastDashLoc = null;
        ArrayList<CustomVec3> path = new ArrayList<CustomVec3>();
        ArrayList<CustomVec3> pathFinderPath = pathfinder.getPath();
        for (CustomVec3 pathElm : pathFinderPath) {
            if (i == 0 || i == pathFinderPath.size() - 1) {
                if (lastLoc != null) {
                    path.add(lastLoc.addVector(0.5, 0.0, 0.5));
                }
                path.add(pathElm.addVector(0.5, 0.0, 0.5));
                lastDashLoc = pathElm;
            } else {
                boolean canContinue = true;
                if (pathElm.squareDistanceTo(lastDashLoc) > 100.0) {
                    canContinue = false;
                } else {
                    double smallX = Math.min(lastDashLoc.getX(), pathElm.getX());
                    double smallY = Math.min(lastDashLoc.getY(), pathElm.getY());
                    double smallZ = Math.min(lastDashLoc.getZ(), pathElm.getZ());
                    double bigX = Math.max(lastDashLoc.getX(), pathElm.getX());
                    double bigY = Math.max(lastDashLoc.getY(), pathElm.getY());
                    double bigZ = Math.max(lastDashLoc.getZ(), pathElm.getZ());
                    int x = (int) smallX;
                    block1: while ((double) x <= bigX) {
                        int y = (int) smallY;
                        while ((double) y <= bigY) {
                            int z = (int) smallZ;
                            while ((double) z <= bigZ) {
                                if (!AStarCustomPathFinder.checkPositionValidity(new CustomVec3(x, y, z))) {
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
                    path.add(lastLoc.addVector(0.5, 0.0, 0.5));
                    lastDashLoc = lastLoc;
                }
            }
            lastLoc = pathElm;
            ++i;
        }
        return path;
    }

    private static boolean canPassThrow(BlockPos pos) {
        Block block =  Minecraft.getMinecraft()
                .theWorld.getBlockState(pos).getBlock();
        return block.getMaterial() == Material.air || block.getMaterial() == Material.plants
                || block.getMaterial() == Material.vine || block == Blocks.ladder || block == Blocks.water
                || block == Blocks.flowing_water || block == Blocks.wall_sign || block == Blocks.standing_sign;
    }
}
