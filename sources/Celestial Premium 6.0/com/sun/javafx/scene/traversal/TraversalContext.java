/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.geometry.Bounds
 *  javafx.scene.Node
 *  javafx.scene.Parent
 */
package com.sun.javafx.scene.traversal;

import com.sun.javafx.scene.traversal.Direction;
import java.util.List;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;

public interface TraversalContext {
    public List<Node> getAllTargetNodes();

    public Bounds getSceneLayoutBounds(Node var1);

    public Parent getRoot();

    public Node selectFirstInParent(Parent var1);

    public Node selectLastInParent(Parent var1);

    public Node selectInSubtree(Parent var1, Node var2, Direction var3);
}

