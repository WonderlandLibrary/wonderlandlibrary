package rosegoldclient.utils.pathfinding;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import rosegoldclient.Main;
import rosegoldclient.features.Pathfinding;
import rosegoldclient.utils.Utils;
import rosegoldclient.utils.VecUtils;

import java.util.ArrayList;
import java.util.Comparator;

public class AStarCustomPathfinder {
    private Vec3d startVec3;
    private Vec3d endVec3;
    private ArrayList<Vec3d> path = new ArrayList<>();
    private ArrayList<Hub> hubs = new ArrayList<>();
    private ArrayList<Hub> hubsToWork = new ArrayList<>();
    private double minDistanceSquared;
    private boolean nearest = true;

    private static final Vec3d[] flatCardinalDirections = {
            new Vec3d(1, 0, 0),
            new Vec3d(-1, 0, 0),
            new Vec3d(0, 0, 1),
            new Vec3d(0, 0, -1)
    };

    public AStarCustomPathfinder(Vec3d startVec3, Vec3d endVec3, double minDistanceSquared) {
        this.startVec3 = VecUtils.floorVec(startVec3);
        this.endVec3 = VecUtils.floorVec(endVec3);
        this.minDistanceSquared = minDistanceSquared;
    }

    public ArrayList<Vec3d> getPath() {
        return path;
    }

    public void compute() {
        compute(1000, 4);
    }

    public void compute(int loops, int depth) {
        path.clear();
        hubsToWork.clear();
        ArrayList<Vec3d> initPath = new ArrayList<>();
        initPath.add(startVec3);
        hubsToWork.add(new Hub(startVec3, null, initPath, startVec3.squareDistanceTo(endVec3), 0.0, 0.0));
        search:
        for (int i = 0; i < loops; ++i) {
            hubsToWork.sort(new CompareHub());
            int j = 0;
            if (hubsToWork.size() == 0) {
                break;
            }
            for (Hub hub : new ArrayList<>(hubsToWork)) {
                if (++j > depth) {
                    break;
                }

                hubsToWork.remove(hub);
                hubs.add(hub);

                for (Vec3d direction : flatCardinalDirections) {
                    Vec3d loc = VecUtils.ceilVec(hub.getLoc().add(direction));
                    if (checkPositionValidity(loc, true)) {
                        if (addHub(hub, loc, 0)) {
                            break search;
                        }
                    }
                }

                if(checkPositionValidity(hub.getLoc().addVector(0, 1, 0), false)) {
                    for (Vec3d direction : flatCardinalDirections) {
                        Vec3d loc = VecUtils.ceilVec(hub.getLoc().add(direction).addVector(0, 1, 0));
                        if (checkPositionValidity(loc, true) && fenceCheck(loc)) {
                            if (addHub(hub, loc, 0)) {
                                break search;
                            }
                        }
                    }
                }

                for (Vec3d direction : flatCardinalDirections) {
                    for(int k = 0; k < 256; k++) {
                        Vec3d forward = VecUtils.ceilVec(hub.getLoc().add(direction).addVector(0, -k, 0));
                        if (checkPositionValidity(forward, false)) {
                            Vec3d loc = VecUtils.ceilVec(hub.getLoc().add(direction).addVector(0, -(k + 1), 0));
                            if (checkPositionValidity(loc, true)) {
                                if (addHub(hub, loc, 0)) {
                                    break search;
                                }
                            }
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        if (nearest) {
            hubs.sort(new CompareHub());
            path = hubs.get(0).getPath();
        }
    }

    public static boolean fenceCheck(Vec3d loc) {
        return fenceCheck((int) loc.x, (int) loc.y, (int) loc.z);
    }

    public static boolean fenceCheck(int x, int y, int z) {
        BlockPos block1 = new BlockPos(x, y, z);
        return !isFence(block1);
    }

    public static boolean checkPositionValidity(Vec3d loc, boolean checkGround) {
        return checkPositionValidity((int) loc.x, (int) loc.y, (int) loc.z, checkGround);
    }

    public static boolean checkPositionValidity(int x, int y, int z, boolean checkGround) {
        BlockPos block1 = new BlockPos(x, y, z);
        if (isBlockSolid(block1)) return false;

        BlockPos block2 = new BlockPos(x, y + 1, z);
        if (isBlockSolid(block2)) return false;

        if (!checkGround) return true;

        BlockPos block3 = new BlockPos(x, y - 1, z);

        return isBlockSolid(block3) && isSafeToWalkOn(block3);
    }

    private static boolean isFence(BlockPos block) {
        IBlockState bs = Main.mc.world.getBlockState(block);
        Block b = bs.getBlock();
        return b instanceof BlockFence || b instanceof BlockWall;
    }

    private static boolean isBlockSolid(BlockPos block) {
        IBlockState bs = Main.mc.world.getBlockState(block);
        Block b = bs.getBlock();
        return Main.mc.world.isBlockFullCube(block) || b instanceof BlockSlab || b instanceof BlockStairs || b instanceof BlockCactus || b instanceof BlockChest || b instanceof BlockEnderChest || b instanceof BlockSkull || b instanceof BlockPane || b instanceof BlockFence || b instanceof BlockWall || b instanceof BlockGlass || b instanceof BlockPistonBase || b instanceof BlockPistonExtension || b instanceof BlockPistonMoving || b instanceof BlockStainedGlass || b instanceof BlockTrapDoor;
    }

    private static boolean isSafeToWalkOn(BlockPos block) {
        IBlockState bs = Main.mc.world.getBlockState(block);
        IBlockState above = Main.mc.world.getBlockState(block.add(0, 1, 0));
        Block b = bs.getBlock();
        if(above.getBlock() instanceof BlockLiquid) return false;
        return !(b instanceof BlockFence) && !(b instanceof BlockWall);
    }

    public Hub isHubExisting(Vec3d loc) {
        for (Hub hub : hubs) {
            if (hub.getLoc().x == loc.x && hub.getLoc().y == loc.y && hub.getLoc().z == loc.z) {
                return hub;
            }
        }
        for (Hub hub : hubsToWork) {
            if (hub.getLoc().x == loc.x && hub.getLoc().y == loc.y && hub.getLoc().z == loc.z) {
                return hub;
            }
        }
        return null;
    }

    public boolean addHub(Hub parent, Vec3d loc, double cost) {
        Hub existingHub = isHubExisting(loc);
        double totalCost = cost;
        if (parent != null) {
            totalCost += parent.getTotalCost();
        }
        if (existingHub == null) {
            if ((loc.x == endVec3.x && loc.y == endVec3.y && loc.z == endVec3.z) || (minDistanceSquared != 0.0 && loc.squareDistanceTo(endVec3) <= minDistanceSquared)) {
                Utils.sendModMessage("finished at "+loc);
                path.clear();
                (path = parent.getPath()).add(loc);
                return true;
            }
            ArrayList<Vec3d> path = new ArrayList<>(parent.getPath());
            path.add(loc);
            hubsToWork.add(new Hub(loc, parent, path, loc.squareDistanceTo(endVec3), cost, totalCost));
        } else if (existingHub.getCost() > cost) {
            ArrayList<Vec3d> path = new ArrayList<>(parent.getPath());
            path.add(loc);
            existingHub.setLoc(loc);
            existingHub.setParent(parent);
            existingHub.setPath(path);
            existingHub.setSquareDistanceToFromTarget(loc.squareDistanceTo(endVec3));
            existingHub.setCost(cost);
            existingHub.setTotalCost(totalCost);
        }
        return false;
    }

    private static class Hub {
        private Vec3d loc;
        private Hub parent;
        private ArrayList<Vec3d> path;
        private double squareDistanceToFromTarget;
        private double cost;
        private double totalCost;

        public Hub(Vec3d loc, Hub parent, ArrayList<Vec3d> path, double squareDistanceToFromTarget, double cost, double totalCost) {
            this.loc = null;
            this.parent = null;
            this.loc = loc;
            this.parent = parent;
            this.path = path;
            this.squareDistanceToFromTarget = squareDistanceToFromTarget;
            this.cost = cost;
            this.totalCost = totalCost;
        }

        public Vec3d getLoc() {
            return loc;
        }

        public Hub getParent() {
            return parent;
        }

        public ArrayList<Vec3d> getPath() {
            return path;
        }

        public double getSquareDistanceToFromTarget() {
            return squareDistanceToFromTarget;
        }

        public double getCost() {
            return cost;
        }

        public void setLoc(Vec3d loc) {
            this.loc = loc;
        }

        public void setParent(Hub parent) {
            this.parent = parent;
        }

        public void setPath(ArrayList<Vec3d> path) {
            this.path = path;
        }

        public void setSquareDistanceToFromTarget(double squareDistanceToFromTarget) {
            this.squareDistanceToFromTarget = squareDistanceToFromTarget;
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
            return (int) (o1.getSquareDistanceToFromTarget() + o1.getTotalCost() - (o2.getSquareDistanceToFromTarget() + o2.getTotalCost()));
        }
    }

}
