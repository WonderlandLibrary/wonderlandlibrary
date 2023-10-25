/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.collections.ObservableList
 *  javafx.geometry.BoundingBox
 *  javafx.geometry.Bounds
 *  javafx.scene.Node
 *  javafx.scene.Parent
 */
package com.sun.javafx.scene.traversal;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.scene.traversal.Algorithm;
import com.sun.javafx.scene.traversal.ContainerTabOrder;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.Hueristic2D;
import com.sun.javafx.scene.traversal.TraversalContext;
import com.sun.javafx.scene.traversal.TraverseListener;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;

public abstract class TraversalEngine {
    static final Algorithm DEFAULT_ALGORITHM = PlatformImpl.isContextual2DNavigation() ? new Hueristic2D() : new ContainerTabOrder();
    private final TraversalContext context = new EngineContext();
    private final TempEngineContext tempEngineContext = new TempEngineContext();
    protected final Algorithm algorithm;
    private final Bounds initialBounds = new BoundingBox(0.0, 0.0, 1.0, 1.0);
    private final ArrayList<TraverseListener> listeners = new ArrayList();

    protected TraversalEngine(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    protected TraversalEngine() {
        this.algorithm = null;
    }

    public final void addTraverseListener(TraverseListener traverseListener) {
        this.listeners.add(traverseListener);
    }

    final void notifyTraversedTo(Node node) {
        for (TraverseListener traverseListener : this.listeners) {
            traverseListener.onTraverse(node, this.getLayoutBounds(node, this.getRoot()));
        }
    }

    public final Node select(Node node, Direction direction) {
        return this.algorithm.select(node, direction, this.context);
    }

    public final Node selectFirst() {
        return this.algorithm.selectFirst(this.context);
    }

    public final Node selectLast() {
        return this.algorithm.selectLast(this.context);
    }

    protected abstract Parent getRoot();

    public final boolean canTraverse() {
        return this.algorithm != null;
    }

    private Bounds getLayoutBounds(Node node, Parent parent) {
        Bounds bounds = node != null ? (parent == null ? node.localToScene(node.getLayoutBounds()) : parent.sceneToLocal(node.localToScene(node.getLayoutBounds()))) : this.initialBounds;
        return bounds;
    }

    private final class EngineContext
    extends BaseEngineContext {
        private EngineContext() {
        }

        @Override
        public Parent getRoot() {
            return TraversalEngine.this.getRoot();
        }
    }

    private final class TempEngineContext
    extends BaseEngineContext {
        private Parent root;

        private TempEngineContext() {
        }

        @Override
        public Parent getRoot() {
            return this.root;
        }

        public void setRoot(Parent parent) {
            this.root = parent;
        }
    }

    private abstract class BaseEngineContext
    implements TraversalContext {
        private BaseEngineContext() {
        }

        @Override
        public List<Node> getAllTargetNodes() {
            ArrayList<Node> arrayList = new ArrayList<Node>();
            this.addFocusableChildrenToList(arrayList, this.getRoot());
            return arrayList;
        }

        @Override
        public Bounds getSceneLayoutBounds(Node node) {
            return TraversalEngine.this.getLayoutBounds(node, null);
        }

        private void addFocusableChildrenToList(List<Node> list, Parent parent) {
            ObservableList observableList = parent.getChildrenUnmodifiable();
            for (Node node : observableList) {
                if (node.isFocusTraversable() && !node.isFocused() && NodeHelper.isTreeVisible(node) && !node.isDisabled()) {
                    list.add(node);
                }
                if (!(node instanceof Parent)) continue;
                this.addFocusableChildrenToList(list, (Parent)node);
            }
        }

        @Override
        public Node selectFirstInParent(Parent parent) {
            TraversalEngine.this.tempEngineContext.setRoot(parent);
            return DEFAULT_ALGORITHM.selectFirst(TraversalEngine.this.tempEngineContext);
        }

        @Override
        public Node selectLastInParent(Parent parent) {
            TraversalEngine.this.tempEngineContext.setRoot(parent);
            return DEFAULT_ALGORITHM.selectLast(TraversalEngine.this.tempEngineContext);
        }

        @Override
        public Node selectInSubtree(Parent parent, Node node, Direction direction) {
            TraversalEngine.this.tempEngineContext.setRoot(parent);
            return DEFAULT_ALGORITHM.select(node, direction, TraversalEngine.this.tempEngineContext);
        }
    }
}

