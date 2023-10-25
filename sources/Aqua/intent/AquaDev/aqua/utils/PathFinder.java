package intent.AquaDev.aqua.utils;

import intent.AquaDev.aqua.utils.PathFinder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import net.minecraft.block.BlockAir;
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
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class PathFinder {
    public static Vec3 startPos;
    public static Vec3 endPos;
    private ArrayList<Vec3> path = new ArrayList();
    private ArrayList<Node> nodes = new ArrayList();
    private ArrayList<Node> activeNodes = new ArrayList();
    private boolean nearest = true;
    private static Vec3[] coords;

    public PathFinder(Vec3 startPos, Vec3 endPos) {
        PathFinder.startPos = startPos;
        PathFinder.endPos = endPos;
    }

    public ArrayList<Vec3> getPath() {
        return this.path;
    }

    public void calculatePath(int iterations) {
        this.path.clear();
        this.activeNodes.clear();
        ArrayList initPath = new ArrayList();
        initPath.add((Object)startPos);
        this.activeNodes.add((Object)new Node(this, startPos, null, initPath, startPos.squareDistanceTo(endPos), 0.0, 0.0));
        block0: for (int i = 0; i < iterations; ++i) {
            Collections.sort(this.activeNodes, (Comparator)new CompareNode(this));
            Iterator iterator = new ArrayList(this.activeNodes).iterator();
            if (!iterator.hasNext()) continue;
            Node node = (Node)iterator.next();
            this.activeNodes.remove((Object)node);
            this.nodes.add((Object)node);
            for (Vec3 direction : coords) {
                Vec3 loc = node.getLoc().add(direction);
                if (PathFinder.checkPositionValidity(loc, false) && this.addHub(node, loc, 0.0)) break block0;
            }
        }
        Collections.sort(this.nodes, (Comparator)new CompareNode(this));
        this.path = ((Node)this.nodes.get(0)).getPath();
    }

    public static boolean checkPositionValidity(Vec3 loc, boolean checkGround) {
        return PathFinder.checkPositionValidity((int)loc.xCoord, (int)loc.yCoord, (int)loc.zCoord, checkGround);
    }

    public static boolean checkPositionValidity(int x, int y, int z, boolean checkGround) {
        BlockPos block1 = new BlockPos(x, y, z);
        BlockPos block2 = new BlockPos(x, y + 1, z);
        BlockPos block3 = new BlockPos(x, y - 1, z);
        return !PathFinder.isBlockSolid(block1) && !PathFinder.isBlockSolid(block2) && (PathFinder.isBlockSolid(block3) || !checkGround) && PathFinder.isSafeToWalkOn(block3) && block1.getBlock() == Blocks.air;
    }

    private static boolean isBlockSolid(BlockPos block) {
        return block.getBlock().isFullBlock() || block.getBlock() instanceof BlockSlab || block.getBlock() instanceof BlockStairs || block.getBlock() instanceof BlockCactus || block.getBlock() instanceof BlockChest || block.getBlock() instanceof BlockEnderChest || block.getBlock() instanceof BlockSkull || block.getBlock() instanceof BlockPane || block.getBlock() instanceof BlockFence || block.getBlock() instanceof BlockWall || block.getBlock() instanceof BlockGlass || block.getBlock() instanceof BlockPistonBase || block.getBlock() instanceof BlockPistonExtension || block.getBlock() instanceof BlockPistonMoving || block.getBlock() instanceof BlockStainedGlass || block.getBlock() instanceof BlockTrapDoor || !(block.getBlock() instanceof BlockAir);
    }

    private static boolean isSafeToWalkOn(BlockPos block) {
        return !(Minecraft.getMinecraft().theWorld.getBlockState(block) instanceof BlockFence) && !(Minecraft.getMinecraft().theWorld.getBlockState(block) instanceof BlockWall);
    }

    public Node isHubExisting(Vec3 loc) {
        for (Node hub : this.nodes) {
            if (hub.getLoc().xCoord != loc.xCoord || hub.getLoc().yCoord != loc.yCoord || hub.getLoc().zCoord != loc.zCoord) continue;
            return hub;
        }
        for (Node hub : this.activeNodes) {
            if (hub.getLoc().xCoord != loc.xCoord || hub.getLoc().yCoord != loc.yCoord || hub.getLoc().zCoord != loc.zCoord) continue;
            return hub;
        }
        return null;
    }

    public boolean addHub(Node parent, Vec3 loc, double cost) {
        Node existingNode = this.isHubExisting(loc);
        double totalCost = cost;
        if (parent != null) {
            totalCost += parent.getFCost();
        }
        if (existingNode == null) {
            if (loc.xCoord == PathFinder.endPos.xCoord && loc.yCoord == PathFinder.endPos.yCoord && loc.zCoord == PathFinder.endPos.zCoord || loc.squareDistanceTo(endPos) <= 3.0) {
                this.path.clear();
                this.path = parent.getPath();
                this.path.add((Object)loc);
                return true;
            }
            ArrayList path = new ArrayList((Collection)parent.getPath());
            path.add((Object)loc);
            this.activeNodes.add((Object)new Node(this, loc, parent, path, loc.squareDistanceTo(endPos), cost, totalCost));
        }
        return false;
    }

    static {
        coords = new Vec3[]{new Vec3(1.0, 0.0, 0.0), new Vec3(-1.0, 0.0, 0.0), new Vec3(0.0, 0.0, 1.0), new Vec3(0.0, 0.0, -1.0), new Vec3(0.0, 1.0, 0.0), new Vec3(0.0, -1.0, 0.0)};
    }
}
