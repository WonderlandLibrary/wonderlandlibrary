/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.geometry.Bounds
 *  javafx.scene.Node
 */
package com.sun.javafx.scene.traversal;

import com.sun.javafx.scene.traversal.Algorithm;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.TabOrderHelper;
import com.sun.javafx.scene.traversal.TraversalContext;
import java.util.List;
import javafx.geometry.Bounds;
import javafx.scene.Node;

public class ContainerTabOrder
implements Algorithm {
    ContainerTabOrder() {
    }

    @Override
    public Node select(Node node, Direction direction, TraversalContext traversalContext) {
        switch (direction) {
            case NEXT: 
            case NEXT_IN_LINE: {
                return TabOrderHelper.findNextFocusablePeer(node, traversalContext.getRoot(), direction == Direction.NEXT);
            }
            case PREVIOUS: {
                return TabOrderHelper.findPreviousFocusablePeer(node, traversalContext.getRoot());
            }
            case UP: 
            case DOWN: 
            case LEFT: 
            case RIGHT: {
                List<Node> list = traversalContext.getAllTargetNodes();
                int n = this.trav2D(traversalContext.getSceneLayoutBounds(node), direction, list, traversalContext);
                if (n == -1) break;
                return list.get(n);
            }
        }
        return null;
    }

    @Override
    public Node selectFirst(TraversalContext traversalContext) {
        return TabOrderHelper.getFirstTargetNode(traversalContext.getRoot());
    }

    @Override
    public Node selectLast(TraversalContext traversalContext) {
        return TabOrderHelper.getLastTargetNode(traversalContext.getRoot());
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
}

