/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.util.pathfinding;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.newdawn.slick.util.pathfinding.AStarHeuristic;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.PathFinder;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;
import org.newdawn.slick.util.pathfinding.heuristics.ClosestHeuristic;

public class AStarPathFinder
implements PathFinder,
PathFindingContext {
    private ArrayList closed = new ArrayList();
    private PriorityList open = new PriorityList();
    private TileBasedMap map;
    private int maxSearchDistance;
    private Node[][] nodes;
    private boolean allowDiagMovement;
    private AStarHeuristic heuristic;
    private Node current;
    private Mover mover;
    private int sourceX;
    private int sourceY;
    private int distance;

    public AStarPathFinder(TileBasedMap map, int maxSearchDistance, boolean allowDiagMovement) {
        this(map, maxSearchDistance, allowDiagMovement, new ClosestHeuristic());
    }

    public AStarPathFinder(TileBasedMap map, int maxSearchDistance, boolean allowDiagMovement, AStarHeuristic heuristic) {
        this.heuristic = heuristic;
        this.map = map;
        this.maxSearchDistance = maxSearchDistance;
        this.allowDiagMovement = allowDiagMovement;
        this.nodes = new Node[map.getWidthInTiles()][map.getHeightInTiles()];
        for (int x2 = 0; x2 < map.getWidthInTiles(); ++x2) {
            for (int y2 = 0; y2 < map.getHeightInTiles(); ++y2) {
                this.nodes[x2][y2] = new Node(x2, y2);
            }
        }
    }

    public Path findPath(Mover mover, int sx, int sy, int tx, int ty) {
        this.current = null;
        this.mover = mover;
        this.sourceX = tx;
        this.sourceY = ty;
        this.distance = 0;
        if (this.map.blocked(this, tx, ty)) {
            return null;
        }
        for (int x2 = 0; x2 < this.map.getWidthInTiles(); ++x2) {
            for (int y2 = 0; y2 < this.map.getHeightInTiles(); ++y2) {
                this.nodes[x2][y2].reset();
            }
        }
        this.nodes[sx][sy].cost = 0.0f;
        this.nodes[sx][sy].depth = 0;
        this.closed.clear();
        this.open.clear();
        this.addToOpen(this.nodes[sx][sy]);
        this.nodes[tx][ty].parent = null;
        int maxDepth = 0;
        while (maxDepth < this.maxSearchDistance && this.open.size() != 0) {
            int lx = sx;
            int ly = sy;
            if (this.current != null) {
                lx = this.current.x;
                ly = this.current.y;
            }
            this.current = this.getFirstInOpen();
            this.distance = this.current.depth;
            if (this.current == this.nodes[tx][ty] && this.isValidLocation(mover, lx, ly, tx, ty)) break;
            this.removeFromOpen(this.current);
            this.addToClosed(this.current);
            for (int x3 = -1; x3 < 2; ++x3) {
                for (int y3 = -1; y3 < 2; ++y3) {
                    if (x3 == 0 && y3 == 0 || !this.allowDiagMovement && x3 != 0 && y3 != 0) continue;
                    int xp = x3 + this.current.x;
                    int yp = y3 + this.current.y;
                    if (!this.isValidLocation(mover, this.current.x, this.current.y, xp, yp)) continue;
                    float nextStepCost = this.current.cost + this.getMovementCost(mover, this.current.x, this.current.y, xp, yp);
                    Node neighbour = this.nodes[xp][yp];
                    this.map.pathFinderVisited(xp, yp);
                    if (nextStepCost < neighbour.cost) {
                        if (this.inOpenList(neighbour)) {
                            this.removeFromOpen(neighbour);
                        }
                        if (this.inClosedList(neighbour)) {
                            this.removeFromClosed(neighbour);
                        }
                    }
                    if (this.inOpenList(neighbour) || this.inClosedList(neighbour)) continue;
                    neighbour.cost = nextStepCost;
                    neighbour.heuristic = this.getHeuristicCost(mover, xp, yp, tx, ty);
                    maxDepth = Math.max(maxDepth, neighbour.setParent(this.current));
                    this.addToOpen(neighbour);
                }
            }
        }
        if (this.nodes[tx][ty].parent == null) {
            return null;
        }
        Path path = new Path();
        Node target = this.nodes[tx][ty];
        while (target != this.nodes[sx][sy]) {
            path.prependStep(target.x, target.y);
            target = target.parent;
        }
        path.prependStep(sx, sy);
        return path;
    }

    public int getCurrentX() {
        if (this.current == null) {
            return -1;
        }
        return this.current.x;
    }

    public int getCurrentY() {
        if (this.current == null) {
            return -1;
        }
        return this.current.y;
    }

    protected Node getFirstInOpen() {
        return (Node)this.open.first();
    }

    protected void addToOpen(Node node) {
        node.setOpen(true);
        this.open.add(node);
    }

    protected boolean inOpenList(Node node) {
        return node.isOpen();
    }

    protected void removeFromOpen(Node node) {
        node.setOpen(false);
        this.open.remove(node);
    }

    protected void addToClosed(Node node) {
        node.setClosed(true);
        this.closed.add(node);
    }

    protected boolean inClosedList(Node node) {
        return node.isClosed();
    }

    protected void removeFromClosed(Node node) {
        node.setClosed(false);
        this.closed.remove(node);
    }

    protected boolean isValidLocation(Mover mover, int sx, int sy, int x2, int y2) {
        boolean invalid;
        boolean bl = invalid = x2 < 0 || y2 < 0 || x2 >= this.map.getWidthInTiles() || y2 >= this.map.getHeightInTiles();
        if (!(invalid || sx == x2 && sy == y2)) {
            this.mover = mover;
            this.sourceX = sx;
            this.sourceY = sy;
            invalid = this.map.blocked(this, x2, y2);
        }
        return !invalid;
    }

    public float getMovementCost(Mover mover, int sx, int sy, int tx, int ty) {
        this.mover = mover;
        this.sourceX = sx;
        this.sourceY = sy;
        return this.map.getCost(this, tx, ty);
    }

    public float getHeuristicCost(Mover mover, int x2, int y2, int tx, int ty) {
        return this.heuristic.getCost(this.map, mover, x2, y2, tx, ty);
    }

    public Mover getMover() {
        return this.mover;
    }

    public int getSearchDistance() {
        return this.distance;
    }

    public int getSourceX() {
        return this.sourceX;
    }

    public int getSourceY() {
        return this.sourceY;
    }

    private class Node
    implements Comparable {
        private int x;
        private int y;
        private float cost;
        private Node parent;
        private float heuristic;
        private int depth;
        private boolean open;
        private boolean closed;

        public Node(int x2, int y2) {
            this.x = x2;
            this.y = y2;
        }

        public int setParent(Node parent) {
            this.depth = parent.depth + 1;
            this.parent = parent;
            return this.depth;
        }

        public int compareTo(Object other) {
            Node o2 = (Node)other;
            float f2 = this.heuristic + this.cost;
            float of = o2.heuristic + o2.cost;
            if (f2 < of) {
                return -1;
            }
            if (f2 > of) {
                return 1;
            }
            return 0;
        }

        public void setOpen(boolean open) {
            this.open = open;
        }

        public boolean isOpen() {
            return this.open;
        }

        public void setClosed(boolean closed) {
            this.closed = closed;
        }

        public boolean isClosed() {
            return this.closed;
        }

        public void reset() {
            this.closed = false;
            this.open = false;
            this.cost = 0.0f;
            this.depth = 0;
        }

        public String toString() {
            return "[Node " + this.x + "," + this.y + "]";
        }
    }

    private class PriorityList {
        private List list = new LinkedList();

        private PriorityList() {
        }

        public Object first() {
            return this.list.get(0);
        }

        public void clear() {
            this.list.clear();
        }

        public void add(Object o2) {
            for (int i2 = 0; i2 < this.list.size(); ++i2) {
                if (((Comparable)this.list.get(i2)).compareTo(o2) <= 0) continue;
                this.list.add(i2, o2);
                break;
            }
            if (!this.list.contains(o2)) {
                this.list.add(o2);
            }
        }

        public void remove(Object o2) {
            this.list.remove(o2);
        }

        public int size() {
            return this.list.size();
        }

        public boolean contains(Object o2) {
            return this.list.contains(o2);
        }

        public String toString() {
            String temp = "{";
            for (int i2 = 0; i2 < this.size(); ++i2) {
                temp = temp + this.list.get(i2).toString() + ",";
            }
            temp = temp + "}";
            return temp;
        }
    }
}

