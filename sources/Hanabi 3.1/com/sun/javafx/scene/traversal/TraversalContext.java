package com.sun.javafx.scene.traversal;

import java.util.List;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;

public interface TraversalContext {
   List getAllTargetNodes();

   Bounds getSceneLayoutBounds(Node var1);

   Parent getRoot();

   Node selectFirstInParent(Parent var1);

   Node selectLastInParent(Parent var1);

   Node selectInSubtree(Parent var1, Node var2, Direction var3);
}
