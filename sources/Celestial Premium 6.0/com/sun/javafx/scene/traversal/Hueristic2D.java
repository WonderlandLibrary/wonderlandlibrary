/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.geometry.BoundingBox
 *  javafx.geometry.Bounds
 *  javafx.geometry.Point2D
 *  javafx.scene.Node
 */
package com.sun.javafx.scene.traversal;

import com.sun.javafx.scene.traversal.Algorithm;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.TabOrderHelper;
import com.sun.javafx.scene.traversal.TraversalContext;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;

public class Hueristic2D
implements Algorithm {
    protected Node cacheStartTraversalNode = null;
    protected Direction cacheStartTraversalDirection = null;
    protected boolean reverseDirection = false;
    protected Node cacheLastTraversalNode = null;
    protected Stack<Node> traversalNodeStack = new Stack();
    private static final Function<Bounds, Double> BOUNDS_TOP_SIDE = bounds -> bounds.getMinY();
    private static final Function<Bounds, Double> BOUNDS_BOTTOM_SIDE = bounds -> bounds.getMaxY();
    private static final Function<Bounds, Double> BOUNDS_LEFT_SIDE = bounds -> bounds.getMinX();
    private static final Function<Bounds, Double> BOUNDS_RIGHT_SIDE = bounds -> bounds.getMaxX();

    Hueristic2D() {
    }

    @Override
    public Node select(Node node, Direction direction, TraversalContext traversalContext) {
        Node node2 = null;
        this.cacheTraversal(node, direction);
        if (Direction.NEXT.equals((Object)direction) || Direction.NEXT_IN_LINE.equals((Object)direction)) {
            node2 = TabOrderHelper.findNextFocusablePeer(node, traversalContext.getRoot(), direction == Direction.NEXT);
        } else if (Direction.PREVIOUS.equals((Object)direction)) {
            node2 = TabOrderHelper.findPreviousFocusablePeer(node, traversalContext.getRoot());
        } else if (Direction.UP.equals((Object)direction) || Direction.DOWN.equals((Object)direction) || Direction.LEFT.equals((Object)direction) || Direction.RIGHT.equals((Object)direction)) {
            if (this.reverseDirection && !this.traversalNodeStack.empty()) {
                if (!this.traversalNodeStack.peek().isFocusTraversable()) {
                    this.traversalNodeStack.clear();
                } else {
                    node2 = this.traversalNodeStack.pop();
                }
            }
            if (node2 == null) {
                Bounds bounds = node.localToScene(node.getLayoutBounds());
                if (this.cacheStartTraversalNode != null) {
                    Bounds bounds2 = this.cacheStartTraversalNode.localToScene(this.cacheStartTraversalNode.getLayoutBounds());
                    switch (direction) {
                        case UP: 
                        case DOWN: {
                            node2 = this.getNearestNodeUpOrDown(bounds, bounds2, traversalContext, direction);
                            break;
                        }
                        case LEFT: 
                        case RIGHT: {
                            node2 = this.getNearestNodeLeftOrRight(bounds, bounds2, traversalContext, direction);
                            break;
                        }
                    }
                }
            }
        }
        if (node2 != null) {
            this.cacheLastTraversalNode = node2;
            if (!this.reverseDirection) {
                this.traversalNodeStack.push(node);
            }
        }
        return node2;
    }

    @Override
    public Node selectFirst(TraversalContext traversalContext) {
        return TabOrderHelper.getFirstTargetNode(traversalContext.getRoot());
    }

    @Override
    public Node selectLast(TraversalContext traversalContext) {
        return TabOrderHelper.getLastTargetNode(traversalContext.getRoot());
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

    private void cacheTraversal(Node node, Direction direction) {
        if (!this.traversalNodeStack.empty() && node != this.cacheLastTraversalNode) {
            this.traversalNodeStack.clear();
        }
        if (direction == Direction.NEXT || direction == Direction.PREVIOUS) {
            this.traversalNodeStack.clear();
            this.reverseDirection = false;
        } else if (this.cacheStartTraversalNode == null || direction != this.cacheStartTraversalDirection) {
            if (direction == Direction.UP && this.cacheStartTraversalDirection == Direction.DOWN || direction == Direction.DOWN && this.cacheStartTraversalDirection == Direction.UP || direction == Direction.LEFT && this.cacheStartTraversalDirection == Direction.RIGHT || direction == Direction.RIGHT && this.cacheStartTraversalDirection == Direction.LEFT && !this.traversalNodeStack.empty()) {
                this.reverseDirection = true;
            } else {
                this.cacheStartTraversalNode = node;
                this.cacheStartTraversalDirection = direction;
                this.reverseDirection = false;
                this.traversalNodeStack.clear();
            }
        } else {
            this.reverseDirection = false;
        }
    }

    protected Node getNearestNodeUpOrDown(Bounds bounds, Bounds bounds2, TraversalContext traversalContext, Direction direction) {
        List<Node> list = traversalContext.getAllTargetNodes();
        Function<Bounds, Double> function = direction == Direction.DOWN ? BOUNDS_BOTTOM_SIDE : BOUNDS_TOP_SIDE;
        Function<Bounds, Double> function2 = direction == Direction.DOWN ? BOUNDS_TOP_SIDE : BOUNDS_BOTTOM_SIDE;
        BoundingBox boundingBox = new BoundingBox(bounds2.getMinX(), bounds.getMinY(), bounds2.getWidth(), bounds.getHeight());
        Point2D point2D = new Point2D(bounds.getMinX() + bounds.getWidth() / 2.0, function.apply(bounds).doubleValue());
        Point2D point2D2 = new Point2D(bounds2.getMinX() + bounds2.getWidth() / 2.0, function.apply(bounds).doubleValue());
        Point2D point2D3 = new Point2D(bounds.getMinX(), function.apply(bounds).doubleValue());
        Point2D point2D4 = new Point2D(bounds2.getMinX(), function.apply(bounds).doubleValue());
        Point2D point2D5 = new Point2D(bounds.getMaxX(), function.apply(bounds).doubleValue());
        Point2D point2D6 = new Point2D(bounds2.getMaxX(), function.apply(bounds).doubleValue());
        Point2D point2D7 = new Point2D(bounds2.getMinX(), function.apply(bounds2).doubleValue());
        TargetNode targetNode = new TargetNode();
        TargetNode targetNode2 = null;
        TargetNode targetNode3 = null;
        TargetNode targetNode4 = null;
        TargetNode targetNode5 = null;
        TargetNode targetNode6 = null;
        TargetNode targetNode7 = null;
        TargetNode targetNode8 = null;
        for (int i = 0; i < list.size(); ++i) {
            double d;
            double d2;
            Node node = list.get(i);
            Bounds bounds3 = node.localToScene(node.getLayoutBounds());
            if (!(direction == Direction.UP ? bounds.getMinY() > bounds3.getMaxY() : bounds.getMaxY() < bounds3.getMinY())) continue;
            targetNode.node = node;
            targetNode.bounds = bounds3;
            double d3 = Math.max(0.0, this.outDistance(direction, (Bounds)boundingBox, bounds3));
            if (this.isOnAxis(direction, (Bounds)boundingBox, bounds3)) {
                targetNode.biased2DMetric = d3 + this.centerSideDistance(direction, (Bounds)boundingBox, bounds3) / 100.0;
            } else {
                d2 = this.cornerSideDistance(direction, (Bounds)boundingBox, bounds3);
                targetNode.biased2DMetric = 100000.0 + d3 * d3 + 9.0 * d2 * d2;
            }
            d2 = Math.max(0.0, this.outDistance(direction, bounds, bounds3));
            if (this.isOnAxis(direction, bounds, bounds3)) {
                targetNode.current2DMetric = d2 + this.centerSideDistance(direction, bounds, bounds3) / 100.0;
            } else {
                d = this.cornerSideDistance(direction, bounds, bounds3);
                targetNode.current2DMetric = 100000.0 + d2 * d2 + 9.0 * d * d;
            }
            targetNode.leftCornerDistance = point2D3.distance(bounds3.getMinX(), function2.apply(bounds3).doubleValue());
            targetNode.rightCornerDistance = point2D5.distance(bounds3.getMaxX(), function2.apply(bounds3).doubleValue());
            d = point2D.distance(bounds3.getMinX() + bounds3.getWidth() / 2.0, function2.apply(bounds3).doubleValue());
            double d4 = point2D3.distance(bounds3.getMinX() + bounds3.getWidth() / 2.0, function2.apply(bounds3).doubleValue());
            double d5 = point2D3.distance(bounds3.getMaxX(), function2.apply(bounds3).doubleValue());
            double d6 = point2D5.distance(bounds3.getMinX(), function2.apply(bounds3).doubleValue());
            double d7 = point2D5.distance(bounds3.getMinX() + bounds3.getWidth() / 2.0, function2.apply(bounds3).doubleValue());
            double d8 = point2D5.distance(bounds3.getMaxX(), function2.apply(bounds3).doubleValue());
            double d9 = point2D.distance(bounds3.getMinX(), function2.apply(bounds3).doubleValue());
            double d10 = point2D.distance(bounds3.getMinX() + bounds3.getWidth() / 2.0, function2.apply(bounds3).doubleValue());
            double d11 = point2D.distance(bounds3.getMaxX(), function2.apply(bounds3).doubleValue());
            double d12 = point2D4.distance(bounds3.getMinX() + bounds3.getWidth() / 2.0, function2.apply(bounds3).doubleValue());
            double d13 = point2D4.distance(bounds3.getMaxX(), function2.apply(bounds3).doubleValue());
            double d14 = point2D6.distance(bounds3.getMinX() + bounds3.getWidth() / 2.0, function2.apply(bounds3).doubleValue());
            double d15 = point2D2.distance(bounds3.getMaxX(), function2.apply(bounds3).doubleValue());
            targetNode.averageDistance = (targetNode.leftCornerDistance + d12 + d13 + d6 + targetNode.rightCornerDistance + d14 + d) / 7.0;
            targetNode.biasShortestDistance = Hueristic2D.findMin(targetNode.leftCornerDistance, d12, d13, d6, d14, targetNode.rightCornerDistance, d9, d, d15);
            targetNode.shortestDistance = Hueristic2D.findMin(targetNode.leftCornerDistance, d4, d5, d6, d7, d8, d9, d10, d11);
            if (d3 >= 0.0 && (targetNode3 == null || targetNode.biased2DMetric < targetNode3.biased2DMetric)) {
                if (targetNode3 == null) {
                    targetNode3 = new TargetNode();
                }
                targetNode3.copy(targetNode);
            }
            if (d2 >= 0.0 && (targetNode2 == null || targetNode.current2DMetric < targetNode2.current2DMetric)) {
                if (targetNode2 == null) {
                    targetNode2 = new TargetNode();
                }
                targetNode2.copy(targetNode);
            }
            if (bounds2.getMaxX() > bounds3.getMinX() && bounds3.getMaxX() > bounds2.getMinX() && (targetNode5 == null || targetNode5.biasShortestDistance > targetNode.biasShortestDistance)) {
                if (targetNode5 == null) {
                    targetNode5 = new TargetNode();
                }
                targetNode5.copy(targetNode);
            }
            if (bounds.getMaxX() > bounds3.getMinX() && bounds3.getMaxX() > bounds.getMinX() && (targetNode6 == null || targetNode6.biasShortestDistance > targetNode.biasShortestDistance)) {
                if (targetNode6 == null) {
                    targetNode6 = new TargetNode();
                }
                targetNode6.copy(targetNode);
            }
            if ((targetNode7 == null || targetNode7.leftCornerDistance > targetNode.leftCornerDistance) && (bounds2.getMinX() >= bounds.getMinX() && bounds3.getMinX() >= bounds.getMinX() || bounds2.getMinX() <= bounds.getMinX() && bounds3.getMinX() <= bounds.getMinX())) {
                if (targetNode7 == null) {
                    targetNode7 = new TargetNode();
                }
                targetNode7.copy(targetNode);
            }
            if ((targetNode4 == null || targetNode4.averageDistance > targetNode.averageDistance) && (bounds2.getMinX() >= bounds.getMinX() && bounds3.getMinX() >= bounds.getMinX() || bounds2.getMinX() <= bounds.getMinX() && bounds3.getMinX() <= bounds.getMinX())) {
                if (targetNode4 == null) {
                    targetNode4 = new TargetNode();
                }
                targetNode4.copy(targetNode);
            }
            if (targetNode8 != null && !(targetNode8.shortestDistance > targetNode.shortestDistance)) continue;
            if (targetNode8 == null) {
                targetNode8 = new TargetNode();
            }
            targetNode8.copy(targetNode);
        }
        list.clear();
        if (targetNode5 != null) {
            targetNode5.originLeftCornerDistance = point2D7.distance(targetNode5.bounds.getMinX(), function2.apply(targetNode5.bounds).doubleValue());
        }
        if (targetNode6 != null) {
            targetNode6.originLeftCornerDistance = point2D7.distance(targetNode6.bounds.getMinX(), function2.apply(targetNode6.bounds).doubleValue());
        }
        if (targetNode4 != null) {
            targetNode4.originLeftCornerDistance = point2D7.distance(targetNode4.bounds.getMinX(), function2.apply(targetNode4.bounds).doubleValue());
        }
        if (targetNode5 != null) {
            if (targetNode6 != null && targetNode5.node == targetNode6.node && (targetNode4 != null && targetNode5.node == targetNode4.node || targetNode3 != null && targetNode5.node == targetNode3.node || targetNode7 != null && targetNode5.node == targetNode7.node || targetNode8 != null && targetNode5.node == targetNode8.node)) {
                return targetNode5.node;
            }
            if (targetNode4 != null && targetNode5.node == targetNode4.node) {
                return targetNode5.node;
            }
            if (targetNode6 != null) {
                if (targetNode6.leftCornerDistance < targetNode5.leftCornerDistance && targetNode6.originLeftCornerDistance < targetNode5.originLeftCornerDistance && targetNode6.bounds.getMinX() - point2D3.getX() < targetNode5.bounds.getMinX() - point2D3.getX()) {
                    return targetNode6.node;
                }
                if (targetNode4 == null || targetNode5.averageDistance < targetNode4.averageDistance) {
                    return targetNode5.node;
                }
            }
        } else {
            if (targetNode6 == null && targetNode2 != null) {
                if (targetNode4 != null && targetNode7 != null && targetNode4.node == targetNode7.node && targetNode4.node == targetNode8.node) {
                    return targetNode4.node;
                }
                return targetNode2.node;
            }
            if (targetNode4 != null && targetNode7 != null && targetNode8 != null && targetNode4.biasShortestDistance == targetNode7.biasShortestDistance && targetNode4.biasShortestDistance == targetNode8.biasShortestDistance && targetNode4.biasShortestDistance < Double.MAX_VALUE) {
                return targetNode4.node;
            }
        }
        if (targetNode4 != null && (targetNode5 == null || targetNode4.biasShortestDistance < targetNode5.biasShortestDistance)) {
            if (targetNode5 != null && function2.apply(targetNode5.bounds) >= function2.apply(targetNode4.bounds)) {
                return targetNode5.node;
            }
            if (targetNode3 != null) {
                if (targetNode3.current2DMetric <= targetNode4.current2DMetric) {
                    return targetNode3.node;
                }
                if (function2.apply(targetNode3.bounds) >= function2.apply(targetNode4.bounds)) {
                    return targetNode3.node;
                }
            }
            return targetNode4.node;
        }
        if (targetNode2 != null && targetNode6 != null && targetNode4 != null && targetNode7 != null && targetNode8 != null && targetNode2.node == targetNode6.node && targetNode2.node == targetNode4.node && targetNode2.node == targetNode7.node && targetNode2.node == targetNode8.node) {
            return targetNode2.node;
        }
        if (targetNode5 != null && (targetNode6 == null || targetNode5.rightCornerDistance < targetNode6.rightCornerDistance)) {
            return targetNode5.node;
        }
        if (targetNode5 != null) {
            return targetNode5.node;
        }
        if (targetNode3 != null) {
            return targetNode3.node;
        }
        if (targetNode6 != null) {
            return targetNode6.node;
        }
        if (targetNode4 != null) {
            return targetNode4.node;
        }
        if (targetNode7 != null) {
            return targetNode7.node;
        }
        if (targetNode8 != null) {
            return targetNode8.node;
        }
        return null;
    }

    protected Node getNearestNodeLeftOrRight(Bounds bounds, Bounds bounds2, TraversalContext traversalContext, Direction direction) {
        List<Node> list = traversalContext.getAllTargetNodes();
        Function<Bounds, Double> function = direction == Direction.LEFT ? BOUNDS_LEFT_SIDE : BOUNDS_RIGHT_SIDE;
        Function<Bounds, Double> function2 = direction == Direction.LEFT ? BOUNDS_RIGHT_SIDE : BOUNDS_LEFT_SIDE;
        BoundingBox boundingBox = new BoundingBox(bounds.getMinX(), bounds2.getMinY(), bounds.getWidth(), bounds2.getHeight());
        Point2D point2D = new Point2D(function.apply(bounds).doubleValue(), bounds.getMinY() + bounds.getHeight() / 2.0);
        Point2D point2D2 = new Point2D(function.apply(bounds).doubleValue(), bounds2.getMinY() + bounds2.getHeight() / 2.0);
        Point2D point2D3 = new Point2D(function.apply(bounds).doubleValue(), bounds.getMinY());
        Point2D point2D4 = new Point2D(function.apply(bounds).doubleValue(), bounds2.getMinY());
        Point2D point2D5 = new Point2D(function.apply(bounds).doubleValue(), bounds.getMaxY());
        Point2D point2D6 = new Point2D(function.apply(bounds).doubleValue(), bounds2.getMaxY());
        Point2D point2D7 = new Point2D(function.apply(bounds2).doubleValue(), bounds2.getMinY());
        TargetNode targetNode = new TargetNode();
        TargetNode targetNode2 = null;
        TargetNode targetNode3 = null;
        TargetNode targetNode4 = null;
        TargetNode targetNode5 = null;
        TargetNode targetNode6 = null;
        TargetNode targetNode7 = null;
        TargetNode targetNode8 = null;
        for (int i = 0; i < list.size(); ++i) {
            double d;
            double d2;
            Node node = list.get(i);
            Bounds bounds3 = node.localToScene(node.getLayoutBounds());
            if (!(direction == Direction.LEFT ? bounds.getMinX() > bounds3.getMinX() : bounds.getMaxX() < bounds3.getMaxX())) continue;
            targetNode.node = node;
            targetNode.bounds = bounds3;
            double d3 = Math.max(0.0, this.outDistance(direction, (Bounds)boundingBox, bounds3));
            if (this.isOnAxis(direction, (Bounds)boundingBox, bounds3)) {
                targetNode.biased2DMetric = d3 + this.centerSideDistance(direction, (Bounds)boundingBox, bounds3) / 100.0;
            } else {
                d2 = this.cornerSideDistance(direction, (Bounds)boundingBox, bounds3);
                targetNode.biased2DMetric = 100000.0 + d3 * d3 + 9.0 * d2 * d2;
            }
            d2 = Math.max(0.0, this.outDistance(direction, bounds, bounds3));
            if (this.isOnAxis(direction, bounds, bounds3)) {
                targetNode.current2DMetric = d2 + this.centerSideDistance(direction, bounds, bounds3) / 100.0;
            } else {
                d = this.cornerSideDistance(direction, bounds, bounds3);
                targetNode.current2DMetric = 100000.0 + d2 * d2 + 9.0 * d * d;
            }
            targetNode.topCornerDistance = point2D3.distance(function2.apply(bounds3).doubleValue(), bounds3.getMinY());
            targetNode.bottomCornerDistance = point2D5.distance(function2.apply(bounds3).doubleValue(), bounds3.getMaxY());
            d = point2D.distance(function2.apply(bounds3).doubleValue(), bounds3.getMinY() + bounds3.getHeight() / 2.0);
            double d4 = point2D3.distance(function2.apply(bounds3).doubleValue(), bounds3.getMaxY());
            double d5 = point2D3.distance(function2.apply(bounds3).doubleValue(), bounds3.getMinY() + bounds3.getHeight() / 2.0);
            double d6 = point2D5.distance(function2.apply(bounds3).doubleValue(), bounds3.getMinY());
            double d7 = point2D5.distance(function2.apply(bounds3).doubleValue(), bounds3.getMaxY());
            double d8 = point2D5.distance(function2.apply(bounds3).doubleValue(), bounds3.getMinY() + bounds3.getHeight() / 2.0);
            double d9 = point2D.distance(function2.apply(bounds3).doubleValue(), bounds3.getMinY());
            double d10 = point2D.distance(function2.apply(bounds3).doubleValue(), bounds3.getMaxY());
            double d11 = point2D.distance(function2.apply(bounds3).doubleValue(), bounds3.getMinY() + bounds3.getHeight() / 2.0);
            double d12 = point2D4.distance(function2.apply(bounds3).doubleValue(), bounds3.getMaxY());
            double d13 = point2D4.distance(function2.apply(bounds3).doubleValue(), bounds3.getMinY() + bounds3.getHeight() / 2.0);
            double d14 = point2D6.distance(function2.apply(bounds3).doubleValue(), bounds3.getMinY() + bounds3.getHeight() / 2.0);
            double d15 = point2D2.distance(function2.apply(bounds3).doubleValue(), bounds3.getMaxY());
            targetNode.averageDistance = (targetNode.topCornerDistance + d12 + d13 + d6 + targetNode.bottomCornerDistance + d14 + d) / 7.0;
            targetNode.biasShortestDistance = Hueristic2D.findMin(targetNode.topCornerDistance, d12, d13, d6, targetNode.bottomCornerDistance, d14, d9, d15, d);
            targetNode.shortestDistance = Hueristic2D.findMin(targetNode.topCornerDistance, d4, d5, d6, d7, d8, d9, d10, d11);
            if (d3 >= 0.0 && (targetNode3 == null || targetNode.biased2DMetric < targetNode3.biased2DMetric)) {
                if (targetNode3 == null) {
                    targetNode3 = new TargetNode();
                }
                targetNode3.copy(targetNode);
            }
            if (d2 >= 0.0 && (targetNode2 == null || targetNode.current2DMetric < targetNode2.current2DMetric)) {
                if (targetNode2 == null) {
                    targetNode2 = new TargetNode();
                }
                targetNode2.copy(targetNode);
            }
            if (bounds2.getMaxY() > bounds3.getMinY() && bounds3.getMaxY() > bounds2.getMinY() && (targetNode5 == null || targetNode5.topCornerDistance > targetNode.topCornerDistance)) {
                if (targetNode5 == null) {
                    targetNode5 = new TargetNode();
                }
                targetNode5.copy(targetNode);
            }
            if (bounds.getMaxY() > bounds3.getMinY() && bounds3.getMaxY() > bounds.getMinY() && (targetNode6 == null || targetNode6.topCornerDistance > targetNode.topCornerDistance)) {
                if (targetNode6 == null) {
                    targetNode6 = new TargetNode();
                }
                targetNode6.copy(targetNode);
            }
            if (targetNode7 == null || targetNode7.topCornerDistance > targetNode.topCornerDistance) {
                if (targetNode7 == null) {
                    targetNode7 = new TargetNode();
                }
                targetNode7.copy(targetNode);
            }
            if (targetNode4 == null || targetNode4.averageDistance > targetNode.averageDistance) {
                if (targetNode4 == null) {
                    targetNode4 = new TargetNode();
                }
                targetNode4.copy(targetNode);
            }
            if (targetNode8 != null && !(targetNode8.shortestDistance > targetNode.shortestDistance)) continue;
            if (targetNode8 == null) {
                targetNode8 = new TargetNode();
            }
            targetNode8.copy(targetNode);
        }
        list.clear();
        if (targetNode5 != null) {
            targetNode5.originTopCornerDistance = point2D7.distance(function2.apply(targetNode5.bounds).doubleValue(), targetNode5.bounds.getMinY());
        }
        if (targetNode6 != null) {
            targetNode6.originTopCornerDistance = point2D7.distance(function2.apply(targetNode6.bounds).doubleValue(), targetNode6.bounds.getMinY());
        }
        if (targetNode4 != null) {
            targetNode4.originTopCornerDistance = point2D7.distance(function2.apply(targetNode4.bounds).doubleValue(), targetNode4.bounds.getMinY());
        }
        if (targetNode6 == null && targetNode5 == null) {
            this.cacheStartTraversalNode = null;
            this.cacheStartTraversalDirection = null;
            this.reverseDirection = false;
            this.traversalNodeStack.clear();
        }
        if (targetNode5 != null) {
            if (targetNode6 != null && targetNode5.node == targetNode6.node && (targetNode4 != null && targetNode5.node == targetNode4.node || targetNode7 != null && targetNode5.node == targetNode7.node || targetNode8 != null && targetNode5.node == targetNode8.node)) {
                return targetNode5.node;
            }
            if (targetNode4 != null && targetNode5.node == targetNode4.node) {
                return targetNode5.node;
            }
            if (targetNode6 != null) {
                if (targetNode6.bottomCornerDistance < targetNode5.bottomCornerDistance && targetNode6.originTopCornerDistance < targetNode5.originTopCornerDistance && targetNode6.bounds.getMinY() - point2D3.getY() < targetNode5.bounds.getMinY() - point2D3.getY()) {
                    return targetNode6.node;
                }
                if (targetNode4 == null || targetNode5.averageDistance < targetNode4.averageDistance) {
                    return targetNode5.node;
                }
            }
        } else {
            if (targetNode6 == null && targetNode2 != null) {
                if (targetNode4 != null && targetNode7 != null && targetNode4.node == targetNode7.node && targetNode4.node == targetNode8.node) {
                    return targetNode4.node;
                }
                return targetNode2.node;
            }
            if (targetNode4 != null && targetNode7 != null && targetNode8 != null && targetNode4.biasShortestDistance == targetNode7.biasShortestDistance && targetNode4.biasShortestDistance == targetNode8.biasShortestDistance && targetNode4.biasShortestDistance < Double.MAX_VALUE) {
                return targetNode4.node;
            }
        }
        if (targetNode4 != null && (targetNode5 == null || targetNode4.biasShortestDistance < targetNode5.biasShortestDistance)) {
            if (targetNode5 != null && function2.apply(targetNode5.bounds) >= function2.apply(targetNode4.bounds)) {
                return targetNode5.node;
            }
            if (targetNode5 != null && targetNode6 != null && targetNode5.biasShortestDistance < Double.MAX_VALUE && targetNode5.node == targetNode6.node) {
                return targetNode5.node;
            }
            if (targetNode6 != null && targetNode5 != null && targetNode6.biasShortestDistance < Double.MAX_VALUE && targetNode6.biasShortestDistance < targetNode5.biasShortestDistance) {
                return targetNode6.node;
            }
            if (targetNode5 != null && targetNode5.biasShortestDistance < Double.MAX_VALUE && targetNode5.originTopCornerDistance < targetNode4.originTopCornerDistance) {
                return targetNode5.node;
            }
            return targetNode4.node;
        }
        if (targetNode5 != null && targetNode6 != null && targetNode5.bottomCornerDistance < targetNode6.bottomCornerDistance) {
            return targetNode5.node;
        }
        if (targetNode6 != null && targetNode7 != null && targetNode6.biasShortestDistance < Double.MAX_VALUE && targetNode6.node == targetNode7.node) {
            return targetNode6.node;
        }
        if (targetNode5 != null) {
            return targetNode5.node;
        }
        if (targetNode3 != null) {
            return targetNode3.node;
        }
        if (targetNode6 != null) {
            return targetNode6.node;
        }
        if (targetNode4 != null) {
            return targetNode4.node;
        }
        if (targetNode7 != null) {
            return targetNode7.node;
        }
        if (targetNode8 != null) {
            return targetNode8.node;
        }
        return null;
    }

    public static double findMin(double ... arrd) {
        double d = Double.MAX_VALUE;
        for (int i = 0; i < arrd.length; ++i) {
            d = d < arrd[i] ? d : arrd[i];
        }
        return d;
    }

    static final class TargetNode {
        Node node = null;
        Bounds bounds = null;
        double biased2DMetric = Double.MAX_VALUE;
        double current2DMetric = Double.MAX_VALUE;
        double leftCornerDistance = Double.MAX_VALUE;
        double rightCornerDistance = Double.MAX_VALUE;
        double topCornerDistance = Double.MAX_VALUE;
        double bottomCornerDistance = Double.MAX_VALUE;
        double shortestDistance = Double.MAX_VALUE;
        double biasShortestDistance = Double.MAX_VALUE;
        double averageDistance = Double.MAX_VALUE;
        double originLeftCornerDistance = Double.MAX_VALUE;
        double originTopCornerDistance = Double.MAX_VALUE;

        TargetNode() {
        }

        void copy(TargetNode targetNode) {
            this.node = targetNode.node;
            this.bounds = targetNode.bounds;
            this.biased2DMetric = targetNode.biased2DMetric;
            this.current2DMetric = targetNode.current2DMetric;
            this.leftCornerDistance = targetNode.leftCornerDistance;
            this.rightCornerDistance = targetNode.rightCornerDistance;
            this.shortestDistance = targetNode.shortestDistance;
            this.biasShortestDistance = targetNode.biasShortestDistance;
            this.averageDistance = targetNode.averageDistance;
            this.topCornerDistance = targetNode.topCornerDistance;
            this.bottomCornerDistance = targetNode.bottomCornerDistance;
            this.originLeftCornerDistance = targetNode.originLeftCornerDistance;
            this.originTopCornerDistance = targetNode.originTopCornerDistance;
        }
    }
}

