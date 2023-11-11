package me.nyan.flush.module.impl.combat.sigmertpaura;

import net.minecraft.block.*;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.Comparator;

public class TPAuraPathFinder {
    private static final TPAuraVec3[] flatCardinalDirections = {
            new TPAuraVec3(1, 0, 0),
            new TPAuraVec3(-1, 0, 0),
            new TPAuraVec3(0, 0, 1),
            new TPAuraVec3(0, 0, -1)
    };
    private final TPAuraVec3 startTPAuraVec3;
    private final TPAuraVec3 endTPAuraVec3;
    private ArrayList<TPAuraVec3> path = new ArrayList<>();
    private final ArrayList<Hub> hubs = new ArrayList<>();
    private final ArrayList<Hub> hubsToWork = new ArrayList<>();

    public TPAuraPathFinder(TPAuraVec3 startTPAuraVec3, TPAuraVec3 endTPAuraVec3) {
        this.startTPAuraVec3 = startTPAuraVec3.addVector(0, 0, 0).floor();
        this.endTPAuraVec3 = endTPAuraVec3.addVector(0, 0, 0).floor();
    }

    public static boolean checkPositionValidity(TPAuraVec3 loc, boolean checkGround) {
        return checkPositionValidity((int) loc.getX(), (int) loc.getY(), (int) loc.getZ(), checkGround);
    }

    public static boolean checkPositionValidity(int x, int y, int z, boolean checkGround) {
        BlockPos block1 = new BlockPos(x, y, z);
        BlockPos block2 = new BlockPos(x, y + 1, z);
        BlockPos block3 = new BlockPos(x, y - 1, z);
        return !isBlockSolid(block1) && !isBlockSolid(block2) && (isBlockSolid(block3) || !checkGround) && isSafeToWalkOn(block3);
    }

    private static boolean isBlockSolid(BlockPos block) {
        return block.getBlock().isFullBlock() ||
                (block.getBlock() instanceof BlockSlab) ||
                (block.getBlock() instanceof BlockStairs) ||
                (block.getBlock() instanceof BlockCactus) ||
                (block.getBlock() instanceof BlockChest) ||
                (block.getBlock() instanceof BlockEnderChest) ||
                (block.getBlock() instanceof BlockSkull) ||
                (block.getBlock() instanceof BlockPane) ||
                (block.getBlock() instanceof BlockFence) ||
                (block.getBlock() instanceof BlockWall) ||
                (block.getBlock() instanceof BlockGlass) ||
                (block.getBlock() instanceof BlockPistonBase) ||
                (block.getBlock() instanceof BlockPistonExtension) ||
                (block.getBlock() instanceof BlockPistonMoving) ||
                (block.getBlock() instanceof BlockStainedGlass) ||
                (block.getBlock() instanceof BlockTrapDoor);
    }

    private static boolean isSafeToWalkOn(BlockPos block) {
        return !(block.getBlock() instanceof BlockFence) && !(block.getBlock() instanceof BlockWall);
    }

    public ArrayList<TPAuraVec3> getPath() {
        return path;
    }

    public void compute() {
        compute(1000, 4);
    }

    public void compute(int loops, int depth) {
        path.clear();
        hubsToWork.clear();
        ArrayList<TPAuraVec3> initPath = new ArrayList<>();
        initPath.add(startTPAuraVec3);
        hubsToWork.add(new Hub(startTPAuraVec3, null, initPath, startTPAuraVec3.squareDistanceTo(endTPAuraVec3), 0, 0));

        search:
        for (int i = 0; i < loops; i++) {
            hubsToWork.sort(new CompareHub());

            int j = 0;

            if (hubsToWork.size() == 0)
                break;

            for (Hub hub : new ArrayList<>(hubsToWork)) {
                j++;

                if (j > depth)
                    break;
                else {
                    hubsToWork.remove(hub);
                    hubs.add(hub);

                    for (TPAuraVec3 direction : flatCardinalDirections) {
                        TPAuraVec3 loc = hub.getLoc().add(direction).floor();

                        if (checkPositionValidity(loc, false) && addHub(hub, loc, 0))
                            break search;
                    }

                    TPAuraVec3 loc1 = hub.getLoc().addVector(0, 1, 0).floor();

                    if (checkPositionValidity(loc1, false) && addHub(hub, loc1, 0))
                        break search;

                    TPAuraVec3 loc2 = hub.getLoc().addVector(0, -1, 0).floor();

                    if (checkPositionValidity(loc2, false) && addHub(hub, loc2, 0))
                        break search;
                }
            }
        }

        hubs.sort(new CompareHub());
        path = hubs.get(0).getPath();
    }

    public BlockPos getBlock(int x, int y, int z) {
        return new BlockPos(x, y, z);
    }

    public Hub isHubExisting(TPAuraVec3 loc) {
        for (Hub hub : hubs) {
            if (hub.getLoc().getX() == loc.getX() && hub.getLoc().getY() == loc.getY() && hub.getLoc().getZ() == loc.getZ())
                return hub;
        }

        for (Hub hub : hubsToWork) {
            if (hub.getLoc().getX() == loc.getX() && hub.getLoc().getY() == loc.getY() && hub.getLoc().getZ() == loc.getZ())
                return hub;
        }

        return null;
    }

    public boolean addHub(Hub parent, TPAuraVec3 loc, double cost) {
        Hub existingHub = isHubExisting(loc);
        double totalCost = cost;

        if (parent != null)
            totalCost += parent.getTotalCost();

        if (existingHub == null) {
            double minDistanceSquared = 9;
            if (loc.getX() == endTPAuraVec3.getX() && loc.getY() == endTPAuraVec3.getY() && loc.getZ() == endTPAuraVec3.getZ() || loc.squareDistanceTo(endTPAuraVec3) <= minDistanceSquared) {
                path.clear();
                path = parent.getPath();
                path.add(loc);
                return true;
            } else {
                ArrayList<TPAuraVec3> path = new ArrayList<>(parent.getPath());
                path.add(loc);
                hubsToWork.add(new Hub(loc, parent, path, loc.squareDistanceTo(endTPAuraVec3), cost, totalCost));
            }
        } else if (existingHub.getCost() > cost) {
            ArrayList<TPAuraVec3> path = new ArrayList<>(parent.getPath());
            path.add(loc);
            existingHub.setLoc(loc);
            existingHub.setParent(parent);
            existingHub.setPath(path);
            existingHub.setSquareDistanceToFromTarget(loc.squareDistanceTo(endTPAuraVec3));
            existingHub.setCost(cost);
            existingHub.setTotalCost(totalCost);
        }

        return false;
    }

    private static class Hub {
        private TPAuraVec3 loc;
        private Hub parent;
        private ArrayList<TPAuraVec3> path;
        private double squareDistanceToFromTarget;
        private double cost;
        private double totalCost;

        public Hub(TPAuraVec3 loc, Hub parent, ArrayList<TPAuraVec3> path, double squareDistanceToFromTarget, double cost, double totalCost) {
            this.loc = loc;
            this.parent = parent;
            this.path = path;
            this.squareDistanceToFromTarget = squareDistanceToFromTarget;
            this.cost = cost;
            this.totalCost = totalCost;
        }

        public TPAuraVec3 getLoc() {
            return loc;
        }

        public void setLoc(TPAuraVec3 loc) {
            this.loc = loc;
        }

        public Hub getParent() {
            return parent;
        }

        public void setParent(Hub parent) {
            this.parent = parent;
        }

        public ArrayList<TPAuraVec3> getPath() {
            return path;
        }

        public void setPath(ArrayList<TPAuraVec3> path) {
            this.path = path;
        }

        public double getSquareDistanceToFromTarget() {
            return squareDistanceToFromTarget;
        }

        public void setSquareDistanceToFromTarget(double squareDistanceToFromTarget) {
            this.squareDistanceToFromTarget = squareDistanceToFromTarget;
        }

        public double getCost() {
            return cost;
        }

        public void setCost(double cost) {
            this.cost = cost;
        }

        public double getTotalCost() {
            return totalCost;
        }

        public void setTotalCost(double totalCost) {
            this.totalCost = totalCost;
        }
    }

    public static class CompareHub implements Comparator<Hub> {
        @Override
        public int compare(Hub o1, Hub o2) {
            return (int) ((o1.getSquareDistanceToFromTarget() + o1.getTotalCost()) - (o2.getSquareDistanceToFromTarget() + o2.getTotalCost()));
        }
    }
}