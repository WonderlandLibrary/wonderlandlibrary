package cc.novoline.utils.pathfinding;

import net.minecraft.util.BlockPos;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class AStar {

    public static AStar aStar;

    public static AStar getAStar() {
        return aStar;
    }

    public static List<Node> getPath(BlockPos pos1, BlockPos pos2) {
        List<Node> path = new CopyOnWriteArrayList<>();
        List<Node> nodes = new CopyOnWriteArrayList<>();
        Node currentNode = new Node(pos1.getX(), pos1.getZ(), 0, 0);
        Node startNode = new Node(pos1.getX(), pos1.getZ(), 0, getCostToATarget(new Node(pos1.getX(), pos1.getZ(), 0, 0), pos2));
        Node endNode = new Node(pos2.getX(), pos2.getZ(), getCostToATarget(new Node(pos2.getX(), pos2.getZ(), 0, 0), pos1), 0);
        for (int i = 0; i < 1000; i++) {
            List<Node> set = new CopyOnWriteArrayList<>();
            for (int x = -1; x < 2; x++) {
                for (int z = 1; z > -2; z--) {
                    if (!doesNodeExistHere(currentNode.x + x, currentNode.z + z, nodes)) {
                        set.add(new Node(currentNode.x + x, currentNode.z + z, getCostToATarget(startNode, new BlockPos(currentNode.x + x, 0, currentNode.z + z)), getCostToATarget(endNode, new BlockPos(currentNode.x + x, 0, currentNode.z + z))));
                    }
                }
            }
            nodes.addAll(set);
            currentNode = set.stream().sorted(Comparator.comparingDouble(node -> node.getFinalCost())).collect(Collectors.toList()).get(0);
            path.add(currentNode);
            if (currentNode.hCost == 0) {
                break;
            }
        }
        return path;
    }

    private static boolean doesNodeExistHere(int x, int z, List<Node> nodes) {
        for (Node node : nodes) {
            if (node.x == x && node.z == z) {
                return true;
            }
        }
        return false;
    }

    private static int getCostToATarget(Node node, BlockPos pos) {
        int absDifferenceX = Math.abs(node.getX() - pos.getX());
        int absDifferenceZ = Math.abs(node.getZ() - pos.getZ());
        int finalCost = 0;
        if (absDifferenceX > 0) {
            for (int i = absDifferenceX; i > 0; i--) {
                finalCost += 14;
            }
            if (absDifferenceZ > 1) {
                absDifferenceZ -= 1;
                finalCost += 10 * absDifferenceZ;
            }
            return finalCost;
        } else if (absDifferenceZ > 0) {
            for (int i = absDifferenceZ; i > 0; i--) {
                finalCost += 14;
            }
            if (absDifferenceX > 1) {
                absDifferenceX -= 1;
                finalCost += 10 * absDifferenceX;
            }
            return finalCost;
        }
        return 0;
    }

    private int getCostToATarget(Node node, Node pos) {
        int absDifferenceX = Math.abs(node.getX() - pos.getX());
        int absDifferenceZ = Math.abs(node.getZ() - pos.getZ());
        int finalCost = 0;
        if (absDifferenceX > 0) {
            for (int i = absDifferenceX; i > 0; i--) {
                finalCost += 14;
            }
            if (absDifferenceZ > 1) {
                absDifferenceZ -= 1;
                finalCost += 10 * absDifferenceZ;
            }
            return finalCost;
        } else if (absDifferenceZ > 0) {
            for (int i = absDifferenceZ; i > 0; i--) {
                finalCost += 14;
            }
            if (absDifferenceX > 1) {
                absDifferenceX -= 1;
                finalCost += 10 * absDifferenceX;
            }
            return finalCost;
        }
        return 0;
    }


    public static class Node {
        int x, y, z, gCost, hCost;

        public Node(int x, int z, int gCost, int hCost) {
            this.x = x;
            this.z = z;
            this.gCost = gCost;
            this.hCost = hCost;
        }

        public int getX() {
            return x;
        }

        public int getZ() {
            return z;
        }

        public int getFinalCost() {
            return gCost + hCost;
        }

        public BlockPos getBlockPos(int y) {
            return new BlockPos(x, y, z);
        }

    }

}
