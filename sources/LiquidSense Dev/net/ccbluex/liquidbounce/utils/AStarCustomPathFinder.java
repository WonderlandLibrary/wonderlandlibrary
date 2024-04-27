package net.ccbluex.liquidbounce.utils;

import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.BlockPistonMoving;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.BlockWall;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

public class AStarCustomPathFinder {
    private static final Minecraft MC = Minecraft.getMinecraft();
    private static final CustomVec3[] flatCardinalDirections = new CustomVec3[]{new CustomVec3(1.0, 0.0, 0.0), new CustomVec3(-1.0, 0.0, 0.0), new CustomVec3(0.0, 0.0, 1.0), new CustomVec3(0.0, 0.0, -1.0)};
    private final CustomVec3 startCustomVec3;
    private final CustomVec3 endCustomVec3;
    private final ArrayList<Hub> hubs = new ArrayList();
    private final ArrayList<Hub> hubsToWork = new ArrayList();
    private ArrayList<CustomVec3> path = new ArrayList();

    public AStarCustomPathFinder(CustomVec3 startCustomVec3, CustomVec3 endCustomVec3) {
        this.startCustomVec3 = startCustomVec3.addVector(0.0, 0.0, 0.0).floor();
        this.endCustomVec3 = endCustomVec3.addVector(0.0, 0.0, 0.0).floor();
    }

  //  public static boolean checkPositionValidity(CustomVec3 vec3) {
      //  BlockPos pos = new BlockPos(vec3);
      //  if (AStarCustomPathFinder.isBlockSolid(pos) || AStarCustomPathFinder.isBlockSolid(pos.add(0, 1, 0))) {
      //      return false;
      //  }
      //  return AStarCustomPathFinder.isSafeToWalkOn(pos.add(0, -1, 0));
 //   }

    public static boolean checkPositionValidity(CustomVec3 loc, boolean checkGround) {
        return checkPositionValidity((int) loc.getX(), (int) loc.getY(), (int) loc.getZ(), checkGround);
    }

    public static boolean checkPositionValidity(int x, int y, int z, boolean checkGround) {
        BlockPos block1 = new BlockPos(x, y, z);
        BlockPos block2 = new BlockPos(x, y + 1, z);
        BlockPos block3 = new BlockPos(x, y - 1, z);
        return !isBlockSolid(block1) && !isBlockSolid(block2) && (isBlockSolid(block3) || !checkGround) && isSafeToWalkOn(block3);
    }

  public static boolean checkPositionValidity(CustomVec3 vec3) {
        return true;
         }
    private static boolean isBlockSolid(BlockPos pos) {
        Block block =  MC.theWorld.getBlockState(pos).getBlock();
        return  block instanceof BlockSlab || block instanceof BlockStairs || block instanceof BlockCactus || block instanceof BlockChest || block instanceof BlockEnderChest || block instanceof BlockSkull || block instanceof BlockPane || block instanceof BlockFence || block instanceof BlockWall || block instanceof BlockGlass || block instanceof BlockPistonBase || block instanceof BlockPistonExtension || block instanceof BlockPistonMoving || block instanceof BlockStainedGlass || block instanceof BlockTrapDoor;
        // return block.isSolidFullCube() || block instanceof BlockSlab || block instanceof BlockStairs || block instanceof BlockCactus || block instanceof BlockChest || block instanceof BlockEnderChest || block instanceof BlockSkull || block instanceof BlockPane || block instanceof BlockFence || block instanceof BlockWall || block instanceof BlockGlass || block instanceof BlockPistonBase || block instanceof BlockPistonExtension || block instanceof BlockPistonMoving || block instanceof BlockStainedGlass || block instanceof BlockTrapDoor;
    }
    public boolean isFullCube()
    {
        return true;
    }
    private static boolean isSafeToWalkOn(BlockPos pos) {
        Block block = AStarCustomPathFinder.MC.theWorld.getBlockState(pos).getBlock();
        return !(block instanceof BlockFence) && !(block instanceof BlockWall);
    }

    public ArrayList<CustomVec3> getPath() {
        return this.path;
    }

    public void compute() {
        this.compute(1000, 4);
    }

    public void compute(int loops, int depth) {
        this.path.clear();
        this.hubsToWork.clear();
        ArrayList<CustomVec3> initPath = new ArrayList<CustomVec3>();
        CustomVec3 startCustomVec3 = this.startCustomVec3;
        initPath.add(startCustomVec3);
        CustomVec3[] flatCardinalDirections = AStarCustomPathFinder.flatCardinalDirections;
        this.hubsToWork.add(new Hub(startCustomVec3, null, initPath, startCustomVec3.squareDistanceTo(this.endCustomVec3), 0.0, 0.0));
        block0: for (int i = 0; i < loops; ++i) {
            ArrayList<Hub> hubsToWork = this.hubsToWork;
            int hubsToWorkSize = hubsToWork.size();
            hubsToWork.sort(new CompareHub());
            int j = 0;
            if (hubsToWorkSize == 0) break;
            for (int i1 = 0; i1 < hubsToWorkSize; ++i1) {
                CustomVec3 loc2;
                Hub hub = hubsToWork.get(i1);
                if (++j > depth) continue block0;
                hubsToWork.remove(hub);
                this.hubs.add(hub);
                CustomVec3 hLoc = hub.getLoc();
                int flatCardinalDirectionsLength = flatCardinalDirections.length;
                for (int i2 = 0; i2 < flatCardinalDirectionsLength; ++i2) {
                    CustomVec3 loc = hLoc.add(flatCardinalDirections[i2]).floor();
                    if (AStarCustomPathFinder.checkPositionValidity(loc) && this.addHub(hub, loc, 0.0)) break block0;
                }
                CustomVec3 loc1 = hLoc.addVector(0.0, 1.0, 0.0).floor();
                if (AStarCustomPathFinder.checkPositionValidity(loc1) && this.addHub(hub, loc1, 0.0) || AStarCustomPathFinder.checkPositionValidity(loc2 = hLoc.addVector(0.0, -1.0, 0.0).floor()) && this.addHub(hub, loc2, 0.0)) break block0;
            }
        }
        this.hubs.sort(new CompareHub());
        this.path = this.hubs.get(0).getPath();
    }

    public Hub isHubExisting(CustomVec3 loc) {
        ArrayList<Hub> hubs = this.hubs;
        int hubsSize = hubs.size();
        for (int i = 0; i < hubsSize; ++i) {
            Hub hub = (Hub)hubs.get(i);
            CustomVec3 hubLoc = hub.getLoc();
            if (hubLoc.getX() != loc.getX() || hubLoc.getY() != loc.getY() || hubLoc.getZ() != loc.getZ()) continue;
            return hub;
        }
        ArrayList<Hub> hubsToWork = this.hubsToWork;
        int hubsToWorkSize = hubsToWork.size();
        for (int i = 0; i < hubsToWorkSize; ++i) {
            Hub hub = (Hub)hubsToWork.get(i);
            CustomVec3 hubLoc = hub.getLoc();
            if (hubLoc.getX() != loc.getX() || hubLoc.getY() != loc.getY() || hubLoc.getZ() != loc.getZ()) continue;
            return hub;
        }
        return null;
    }

    public boolean addHub(Hub parent, CustomVec3 loc, double cost) {
        Hub existingHub = this.isHubExisting(loc);
        double totalCost = cost;
        if (parent != null) {
            totalCost += parent.getTotalCost();
        }
        CustomVec3 endCustomVec3 = this.endCustomVec3;
        ArrayList<CustomVec3> parentPath = parent.getPath();
        if (existingHub == null) {
            if (loc.getX() == endCustomVec3.getX() && loc.getY() == endCustomVec3.getY() && loc.getZ() == endCustomVec3.getZ()) {
                this.path.clear();
                this.path = parentPath;
                this.path.add(loc);
                return true;
            }
            parentPath.add(loc);
            this.hubsToWork.add(new Hub(loc, parent, parentPath, loc.squareDistanceTo(endCustomVec3), cost, totalCost));
        } else if (existingHub.getCost() > cost) {
            parentPath.add(loc);
            existingHub.setLoc(loc);
            existingHub.setParent(parent);
            existingHub.setPath(parentPath);
            existingHub.setSquareDistanceToFromTarget(loc.squareDistanceTo(endCustomVec3));
            existingHub.setCost(cost);
            existingHub.setTotalCost(totalCost);
        }
        return false;
    }

    public static class CompareHub
            implements Comparator<Hub> {
        @Override
        public int compare(Hub o1, Hub o2) {
            return (int)(o1.getSquareDistanceToFromTarget() + o1.getTotalCost() - (o2.getSquareDistanceToFromTarget() + o2.getTotalCost()));
        }
    }

    private static class Hub {
        private CustomVec3 loc;
        private Hub parent;
        private ArrayList<CustomVec3> path;
        private double squareDistanceToFromTarget;
        private double cost;
        private double totalCost;

        public Hub(CustomVec3 loc, Hub parent, ArrayList<CustomVec3> path, double squareDistanceToFromTarget, double cost, double totalCost) {
            this.loc = loc;
            this.parent = parent;
            this.path = path;
            this.squareDistanceToFromTarget = squareDistanceToFromTarget;
            this.cost = cost;
            this.totalCost = totalCost;
        }

        public CustomVec3 getLoc() {
            return this.loc;
        }

        public void setLoc(CustomVec3 loc) {
            this.loc = loc;
        }

        public Hub getParent() {
            return this.parent;
        }

        public void setParent(Hub parent) {
            this.parent = parent;
        }

        public ArrayList<CustomVec3> getPath() {
            return this.path;
        }

        public void setPath(ArrayList<CustomVec3> path) {
            this.path = path;
        }

        public double getSquareDistanceToFromTarget() {
            return this.squareDistanceToFromTarget;
        }

        public void setSquareDistanceToFromTarget(double squareDistanceToFromTarget) {
            this.squareDistanceToFromTarget = squareDistanceToFromTarget;
        }

        public double getCost() {
            return this.cost;
        }

        public void setCost(double cost) {
            this.cost = cost;
        }

        public double getTotalCost() {
            return this.totalCost;
        }

        public void setTotalCost(double totalCost) {
            this.totalCost = totalCost;
        }
    }
}
