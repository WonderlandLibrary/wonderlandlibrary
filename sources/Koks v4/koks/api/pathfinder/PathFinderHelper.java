package koks.api.pathfinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

import koks.api.utils.TimeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

public class PathFinderHelper {
    private ArrayList<Vec3i> lookedUp = new ArrayList<Vec3i>();
    private ArrayList<Node> nodes = new ArrayList<Node>();
    private TreeSet<Node> open = null;
    private HashMap<String, Node> nodes_hashmap = new HashMap<String, Node>();
    private Vec3i start;
    private Vec3i finish;
    private boolean finished;
    private int max = 1000;
    private final TimeHelper timeHelper = new TimeHelper();

    public void clear() {
        lookedUp.clear();
        nodes.clear();
        open = new TreeSet<Node>(new The_Comparator());
        nodes_hashmap.clear();
        finished = false;
        max = 1000;
    }

    public ArrayList<Vec3i> findPath(Vec3 vec, Vec3 finish, int step, boolean allowGround) {
        return findPath(vec.floor(), finish.floor(), step, allowGround);
    }
    public ArrayList<Vec3i> findPath(Vec3i vec,Vec3i finish, int step, boolean allowGround) {
        open = new TreeSet<Node>(new The_Comparator());
        this.start = vec;
        this.finish = finish;
        timeHelper.reset();
        Node nstart = new Node(vec, vec, finish, null);
        nodes.add(nstart);
        lookup(nstart, step, allowGround);
        while (!finished && !timeHelper.hasReached(max)) {
            Node node = getBest();
            if (node == null) break;
            lookup(node);
        }
        if (!finished) return new ArrayList<Vec3i>();
        ArrayList<Vec3i> vecs = new ArrayList<Vec3i>();
        Node node = nodes.get(nodes.size()-1);
        vecs.add(node.getVec());
        while (true) {
            node = node.getParent();
            if (node != null && !vecs.contains(node.getVec())) {
                vecs.add(node.getVec());
            }else {
                break;
            }
        }
        Collections.reverse(vecs);
        return vecs;
    }
    private Node getBest() {
        return open.first();
    }
    private ArrayList<Node> lookup(Node n) {
        return lookup(n, 1, true);
    }

    private ArrayList<Node> lookup(Node n, int step, boolean allowGround) {
        //		System.out.println("Lookup " + n);
        Vec3i vec = n.getVec();
        n.setLookedUpFromThere(true);
        open.remove(n);
        ArrayList<Node> vectors = new ArrayList<Node>();
        vectors.add(lookupAndCheck(new Vec3i(vec.getX()+step, vec.getY(), vec.getZ()),n, allowGround));
        vectors.add(lookupAndCheck(new Vec3i(vec.getX()-step, vec.getY(), vec.getZ()),n, allowGround));
        vectors.add(lookupAndCheck(new Vec3i(vec.getX(), vec.getY()+step, vec.getZ()),n, allowGround));
        vectors.add(lookupAndCheck(new Vec3i(vec.getX(), vec.getY()-step, vec.getZ()),n, allowGround));
        vectors.add(lookupAndCheck(new Vec3i(vec.getX(), vec.getY(), vec.getZ()+step),n, allowGround));
        vectors.add(lookupAndCheck(new Vec3i(vec.getX(), vec.getY(), vec.getZ()-step),n, allowGround));
        Node node1 = null;
        double cost = Integer.MAX_VALUE;
        ArrayList<Node> cleaned = new ArrayList<Node>();
        //		for (Node node:vectors) {
        //			if (node == null) continue;
        //			cleaned.add(node);
        //			if (node.getHcost() == 0) {
        //				finished = true;
        //				break;
        //			}
        //		}
        for (Node node:vectors) {
            if (node == null) continue;
            if (node.getHcost() == 0) {
                finished = true;
                break;
            }
            if (node.getHcost() < cost) {
                node1 = node;
                cost = node.getHcost();
            }
        }
        if (node1 != null) cleaned.add(node1);
        nodes.addAll(cleaned);
        open.addAll(cleaned);
        for (Node n1:cleaned) {
            nodes_hashmap.put(n1.getVec().toString(), n1);
        }
        return null;
    }

    private Node lookupAndCheck(Vec3i vec,Node parent, boolean allowGround) {
        Node lup = lookedUp(vec);
        if (lup != null) {
            Node node1 = lup;
            if (parent.getPathCost()+1<node1.getPathCost()) {
                nodes.remove(node1);
                open.remove(node1);
                nodes_hashmap.remove(node1.getVec().toString());
            }else {
                return null;
            }
        }
        Node node = new Node(start, vec, finish,parent);
        Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(vec)).getBlock();
        Block block1 = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(new Vec3i(vec.getX(), vec.getY()+1, vec.getZ()))).getBlock();
        Block block2 = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(new Vec3i(vec.getX(), vec.getY()-1, vec.getZ()))).getBlock();
        if (block.getMaterial() != Material.air) return null;
        if (block1.getMaterial() != Material.air) return null;
        if (block2.getMaterial() != Material.air && !allowGround) return null;
        if (block2 instanceof BlockFence) return null;
        return node;
    }
    private Node lookedUp(Vec3i vec) {
        Node node = nodes_hashmap.get(vec.toString());
        return node;
    }
}
class The_Comparator implements Comparator<Node> {
    public int compare(Node n1, Node n2) {
        if (n1.getFcost() > n2.getFcost() || n1.getFcost() == n2.getFcost() && n1.getHcost() > n2.getHcost()) {
            return 1;
        }
        if (n1.getFcost() < n2.getFcost() || n1.getFcost() == n2.getFcost() && n1.getHcost() < n2.getHcost()) {
            return -1;
        }
        return 0;
    }
}
