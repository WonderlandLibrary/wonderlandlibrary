package club.dortware.client.util.impl.pathfinding.impl;

import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
/**
* Credits to Sigma for the path finder
**/
public class DortPathFinder {
    private final Vector startVec3;
    private final Vector endVec3;
    private ArrayList<Vector> path = new ArrayList();
    private final ArrayList<PathHub> pathHubs = new ArrayList();
    private final ArrayList<PathHub> workingPathHubList = new ArrayList();
    private final double minDistanceSquared = 9.0;
    private final boolean nearest = true;
    private static final Vector[] directions = new Vector[]{new Vector(1.0, 0.0, 0.0), new Vector(-1.0, 0.0, 0.0),
            new Vector(0.0, 0.0, 1.0), new Vector(0.0, 0.0, -1.0)};

    public DortPathFinder(Vector startVec3, Vector endVec3) {
        this.startVec3 = startVec3.addVector(0.0, 0.0, 0.0).floor();
        this.endVec3 = endVec3.addVector(0.0, 0.0, 0.0).floor();
    }

    public ArrayList<Vector> getPath() {
        return path;
    }

    public void compute() {
        compute(1000, 4);
    }

    public void compute(int loops, int depth) {
        path.clear();
        workingPathHubList.clear();
        ArrayList<Vector> initPath = new ArrayList<>();
        initPath.add(startVec3);
        workingPathHubList
                .add(new PathHub(startVec3, null, initPath, startVec3.squareDistanceTo(endVec3), 0.0, 0.0));
        block0:
        for (int i = 0; i < loops; ++i) {
            Collections.sort(workingPathHubList, new CompareHub());
            int j = 0;
            if (workingPathHubList.size() == 0) {
                break;
            }
            for (PathHub pathHub : new ArrayList<>(workingPathHubList)) {
                Vector loc2;
                if (++j > depth) {
                    continue block0;
                }
                workingPathHubList.remove(pathHub);
                pathHubs.add(pathHub);
                for (Vector direction : directions) {
                    Vector loc = pathHub.getLoc().add(direction).floor();
                    if (DortPathFinder.isValid(loc, false) && putHub(pathHub, loc, 0.0)) {
                        break block0;
                    }
                }
                Vector loc1 = pathHub.getLoc().addVector(0.0, 1.0, 0.0).floor();
                if (DortPathFinder.isValid(loc1, false) && putHub(pathHub, loc1, 0.0)
                        || DortPathFinder
                        .isValid(loc2 = pathHub.getLoc().addVector(0.0, -1.0, 0.0).floor(), false)
                        && putHub(pathHub, loc2, 0.0)) {
                    break block0;
                }
            }
        }
        if (nearest) {
            Collections.sort(pathHubs, new CompareHub());
            path = pathHubs.get(0).getPathway();
        }
    }

    public static boolean isValid(Vector loc, boolean checkGround) {
        return DortPathFinder.isValid((int) loc.getX(), (int) loc.getY(), (int) loc.getZ(),
                checkGround);
    }

    public static boolean isValid(int x, int y, int z, boolean checkGround) {
        BlockPos block1 = new BlockPos(x, y, z);
        BlockPos block2 = new BlockPos(x, y + 1, z);
        BlockPos block3 = new BlockPos(x, y - 1, z);
        return !DortPathFinder.isNotPassable(block1) && !DortPathFinder.isNotPassable(block2)
                && (DortPathFinder.isNotPassable(block3) || !checkGround)
                && DortPathFinder.canWalkOn(block3);
    }

    private static boolean isNotPassable(BlockPos block) {
        return Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(), block.getZ()).isSolidFullCube()
                || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(),
                block.getZ()) instanceof BlockSlab
                || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(),
                block.getZ()) instanceof BlockStairs
                || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(),
                block.getZ()) instanceof BlockCactus
                || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(),
                block.getZ()) instanceof BlockChest
                || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(),
                block.getZ()) instanceof BlockEnderChest
                || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(),
                block.getZ()) instanceof BlockSkull
                || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(),
                block.getZ()) instanceof BlockPane
                || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(),
                block.getZ()) instanceof BlockFence
                || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(),
                block.getZ()) instanceof BlockWall
                || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(),
                block.getZ()) instanceof BlockGlass
                || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(),
                block.getZ()) instanceof BlockPistonBase
                || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(),
                block.getZ()) instanceof BlockPistonExtension
                || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(),
                block.getZ()) instanceof BlockPistonMoving
                || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(),
                block.getZ()) instanceof BlockStainedGlass
                || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(),
                block.getZ()) instanceof BlockTrapDoor;
    }

    private static boolean canWalkOn(BlockPos block) {
        return !(Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(),
                block.getZ()) instanceof BlockFence)
                && !(Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(),
                block.getZ()) instanceof BlockWall);
    }

    public PathHub doesHubExistAt(Vector loc) {
        for (PathHub pathHub : pathHubs) {
            if (pathHub.getLoc().getX() != loc.getX() || pathHub.getLoc().getY() != loc.getY()
                    || pathHub.getLoc().getZ() != loc.getZ()) {
                continue;
            }
            return pathHub;
        }
        for (PathHub pathHub : workingPathHubList) {
            if (pathHub.getLoc().getX() != loc.getX() || pathHub.getLoc().getY() != loc.getY()
                    || pathHub.getLoc().getZ() != loc.getZ()) {
                continue;
            }
            return pathHub;
        }
        return null;
    }

    public boolean putHub(PathHub parent, Vector loc, double cost) {
        PathHub existingPathHub = doesHubExistAt(loc);
        double totalCost = cost;
        if (parent != null) {
            totalCost += parent.getMaxCost();
        }
        if (existingPathHub == null) {
            if (loc.getX() == endVec3.getX() && loc.getY() == endVec3.getY()
                    && loc.getZ() == endVec3.getZ()
                    || minDistanceSquared != 0.0
                    && loc.squareDistanceTo(endVec3) <= minDistanceSquared) {
                path.clear();
                path = parent.getPathway();
                path.add(loc);
                return true;
            }
            ArrayList<Vector> path = new ArrayList<>(parent.getPathway());
            path.add(loc);
            workingPathHubList.add(new PathHub(loc, parent, path, loc.squareDistanceTo(endVec3), cost, totalCost));
        } else if (existingPathHub.getCurrentCost() > cost) {
            ArrayList<Vector> path = new ArrayList<>(parent.getPathway());
            path.add(loc);
            existingPathHub.setLoc(loc);
            existingPathHub.setParentPathHub(parent);
            existingPathHub.setPathway(path);
            existingPathHub.setSqDist(loc.squareDistanceTo(endVec3));
            existingPathHub.setCurrentCost(cost);
            existingPathHub.setMaxCost(totalCost);
        }
        return false;
    }

    public class CompareHub implements Comparator<PathHub> {
        @Override
        public int compare(PathHub o1, PathHub o2) {
            return (int) (o1.getSqDist() + o1.getMaxCost()
                    - (o2.getSqDist() + o2.getMaxCost()));
        }
    }
}
