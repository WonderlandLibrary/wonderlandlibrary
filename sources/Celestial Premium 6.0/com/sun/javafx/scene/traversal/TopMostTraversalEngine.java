/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 *  javafx.scene.Parent
 */
package com.sun.javafx.scene.traversal;

import com.sun.javafx.scene.ParentHelper;
import com.sun.javafx.scene.traversal.Algorithm;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import com.sun.javafx.scene.traversal.TraversalEngine;
import javafx.scene.Node;
import javafx.scene.Parent;

public abstract class TopMostTraversalEngine
extends TraversalEngine {
    protected TopMostTraversalEngine() {
        super(DEFAULT_ALGORITHM);
    }

    TopMostTraversalEngine(Algorithm algorithm) {
        super(algorithm);
    }

    public final Node trav(Node node, Direction direction) {
        Node node2 = null;
        Node node3 = node;
        for (Parent parent = node.getParent(); parent != null; parent = parent.getParent()) {
            ParentTraversalEngine parentTraversalEngine = ParentHelper.getTraversalEngine(parent);
            if (parentTraversalEngine == null || !parentTraversalEngine.canTraverse()) continue;
            node2 = parentTraversalEngine.select(node, direction);
            if (node2 != null) break;
            node3 = parent;
            if (direction != Direction.NEXT) continue;
            direction = Direction.NEXT_IN_LINE;
        }
        if (node2 == null) {
            node2 = this.select(node3, direction);
        }
        if (node2 == null) {
            if (direction == Direction.NEXT || direction == Direction.NEXT_IN_LINE) {
                node2 = this.selectFirst();
            } else if (direction == Direction.PREVIOUS) {
                node2 = this.selectLast();
            }
        }
        if (node2 != null) {
            this.focusAndNotify(node2);
        }
        return node2;
    }

    private void focusAndNotify(Node node) {
        node.requestFocus();
        this.notifyTreeTraversedTo(node);
    }

    private void notifyTreeTraversedTo(Node node) {
        for (Parent parent = node.getParent(); parent != null; parent = parent.getParent()) {
            ParentTraversalEngine parentTraversalEngine = ParentHelper.getTraversalEngine(parent);
            if (parentTraversalEngine == null) continue;
            parentTraversalEngine.notifyTraversedTo(node);
        }
        this.notifyTraversedTo(node);
    }

    public final Node traverseToFirst() {
        Node node = this.selectFirst();
        if (node != null) {
            this.focusAndNotify(node);
        }
        return node;
    }

    public final Node traverseToLast() {
        Node node = this.selectLast();
        if (node != null) {
            this.focusAndNotify(node);
        }
        return node;
    }
}

