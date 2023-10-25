/*
 * Decompiled with CFR 0.150.
 */
package baritone.pathing.calc.openset;

import baritone.pathing.calc.PathNode;
import baritone.pathing.calc.openset.IOpenSet;

class LinkedListOpenSet
implements IOpenSet {
    private Node first = null;

    LinkedListOpenSet() {
    }

    @Override
    public boolean isEmpty() {
        return this.first == null;
    }

    @Override
    public void insert(PathNode pathNode) {
        Node node = new Node();
        node.val = pathNode;
        node.nextOpen = this.first;
        this.first = node;
    }

    @Override
    public void update(PathNode node) {
    }

    @Override
    public PathNode removeLowest() {
        if (this.first == null) {
            return null;
        }
        Node current = this.first.nextOpen;
        if (current == null) {
            Node n = this.first;
            this.first = null;
            return n.val;
        }
        Node previous = this.first;
        double bestValue = ((Node)this.first).val.combinedCost;
        Node bestNode = this.first;
        Node beforeBest = null;
        while (current != null) {
            double comp = ((Node)current).val.combinedCost;
            if (comp < bestValue) {
                bestValue = comp;
                bestNode = current;
                beforeBest = previous;
            }
            previous = current;
            current = current.nextOpen;
        }
        if (beforeBest == null) {
            this.first = this.first.nextOpen;
            bestNode.nextOpen = null;
            return bestNode.val;
        }
        beforeBest.nextOpen = bestNode.nextOpen;
        bestNode.nextOpen = null;
        return bestNode.val;
    }

    public static class Node {
        private Node nextOpen;
        private PathNode val;
    }
}

