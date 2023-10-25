/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.geometry.Bounds
 *  javafx.geometry.Point2D
 *  javafx.scene.Node
 */
package com.sun.javafx.scene.traversal;

import com.sun.javafx.scene.traversal.Algorithm;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.TraversalContext;
import java.util.List;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;

public class WeightedClosestCorner
implements Algorithm {
    WeightedClosestCorner() {
    }

    private boolean isOnAxis(Direction direction, Bounds bounds, Bounds bounds2) {
        double d;
        double d2;
        double d3;
        double d4;
        if (direction == Direction.UP || direction == Direction.DOWN) {
            d4 = bounds.getMinX();
            d3 = bounds.getMaxX();
            d2 = bounds2.getMinX();
            d = bounds2.getMaxX();
        } else {
            d4 = bounds.getMinY();
            d3 = bounds.getMaxY();
            d2 = bounds2.getMinY();
            d = bounds2.getMaxY();
        }
        return d2 <= d3 && d >= d4;
    }

    private double outDistance(Direction direction, Bounds bounds, Bounds bounds2) {
        double d = direction == Direction.UP ? bounds.getMinY() - bounds2.getMaxY() : (direction == Direction.DOWN ? bounds2.getMinY() - bounds.getMaxY() : (direction == Direction.LEFT ? bounds.getMinX() - bounds2.getMaxX() : bounds2.getMinX() - bounds.getMaxX()));
        return d;
    }

    private double centerSideDistance(Direction direction, Bounds bounds, Bounds bounds2) {
        double d;
        double d2;
        if (direction == Direction.UP || direction == Direction.DOWN) {
            d2 = bounds.getMinX() + bounds.getWidth() / 2.0;
            d = bounds2.getMinX() + bounds2.getWidth() / 2.0;
        } else {
            d2 = bounds.getMinY() + bounds.getHeight() / 2.0;
            d = bounds2.getMinY() + bounds2.getHeight() / 2.0;
        }
        return Math.abs(d - d2);
    }

    private double cornerSideDistance(Direction direction, Bounds bounds, Bounds bounds2) {
        double d = direction == Direction.UP || direction == Direction.DOWN ? (bounds2.getMinX() > bounds.getMaxX() ? bounds2.getMinX() - bounds.getMaxX() : bounds.getMinX() - bounds2.getMaxX()) : (bounds2.getMinY() > bounds.getMaxY() ? bounds2.getMinY() - bounds.getMaxY() : bounds.getMinY() - bounds2.getMaxY());
        return d;
    }

    @Override
    public Node select(Node node, Direction direction, TraversalContext traversalContext) {
        Node node2 = null;
        List<Node> list = traversalContext.getAllTargetNodes();
        int n = this.traverse(traversalContext.getSceneLayoutBounds(node), direction, list, traversalContext);
        if (n != -1) {
            node2 = list.get(n);
        }
        return node2;
    }

    @Override
    public Node selectFirst(TraversalContext traversalContext) {
        List<Node> list = traversalContext.getAllTargetNodes();
        Point2D point2D = new Point2D(0.0, 0.0);
        if (list.size() > 0) {
            Node node = list.get(0);
            double d = point2D.distance(traversalContext.getSceneLayoutBounds(list.get(0)).getMinX(), traversalContext.getSceneLayoutBounds(list.get(0)).getMinY());
            for (int i = 1; i < list.size(); ++i) {
                double d2 = point2D.distance(traversalContext.getSceneLayoutBounds(list.get(i)).getMinX(), traversalContext.getSceneLayoutBounds(list.get(i)).getMinY());
                if (!(d > d2)) continue;
                d = d2;
                node = list.get(i);
            }
            return node;
        }
        return null;
    }

    @Override
    public Node selectLast(TraversalContext traversalContext) {
        return null;
    }

    public int traverse(Bounds bounds, Direction direction, List<Node> list, TraversalContext traversalContext) {
        int n = direction == Direction.NEXT || direction == Direction.NEXT_IN_LINE || direction == Direction.PREVIOUS ? this.trav1D(bounds, direction, list, traversalContext) : this.trav2D(bounds, direction, list, traversalContext);
        return n;
    }

    private int trav2D(Bounds bounds, Direction direction, List<Node> list, TraversalContext traversalContext) {
        Bounds bounds2 = null;
        double d = 0.0;
        int n = -1;
        for (int i = 0; i < list.size(); ++i) {
            double d2;
            Bounds bounds3 = traversalContext.getSceneLayoutBounds(list.get(i));
            double d3 = this.outDistance(direction, bounds, bounds3);
            if (this.isOnAxis(direction, bounds, bounds3)) {
                d2 = d3 + this.centerSideDistance(direction, bounds, bounds3) / 100.0;
            } else {
                double d4 = this.cornerSideDistance(direction, bounds, bounds3);
                d2 = 100000.0 + d3 * d3 + 9.0 * d4 * d4;
            }
            if (d3 < 0.0 || bounds2 != null && !(d2 < d)) continue;
            bounds2 = bounds3;
            d = d2;
            n = i;
        }
        return n;
    }

    private int compare1D(Bounds bounds, Bounds bounds2) {
        int n = 0;
        double d = (bounds.getMinY() + bounds.getMaxY()) / 2.0;
        double d2 = (bounds2.getMinY() + bounds2.getMaxY()) / 2.0;
        double d3 = (bounds.getMinX() + bounds.getMaxX()) / 2.0;
        double d4 = (bounds2.getMinX() + bounds2.getMaxX()) / 2.0;
        double d5 = bounds.hashCode();
        double d6 = bounds2.hashCode();
        if (d < d2) {
            n = -1;
        } else if (d > d2) {
            n = 1;
        } else if (d3 < d4) {
            n = -1;
        } else if (d3 > d4) {
            n = 1;
        } else if (d5 < d6) {
            n = -1;
        } else if (d5 > d6) {
            n = 1;
        }
        return n;
    }

    private int compare1D(Bounds bounds, Bounds bounds2, Direction direction) {
        return direction != Direction.PREVIOUS ? -this.compare1D(bounds, bounds2) : this.compare1D(bounds, bounds2);
    }

    private int trav1D(Bounds bounds, Direction direction, List<Node> list, TraversalContext traversalContext) {
        int n = -1;
        int n2 = -1;
        for (int i = 0; i < list.size(); ++i) {
            if (n2 == -1 || this.compare1D(traversalContext.getSceneLayoutBounds(list.get(i)), traversalContext.getSceneLayoutBounds(list.get(n2)), direction) < 0) {
                n2 = i;
            }
            if (this.compare1D(traversalContext.getSceneLayoutBounds(list.get(i)), bounds, direction) < 0 || n != -1 && this.compare1D(traversalContext.getSceneLayoutBounds(list.get(i)), traversalContext.getSceneLayoutBounds(list.get(n)), direction) >= 0) continue;
            n = i;
        }
        return n == -1 ? n2 : n;
    }
}

