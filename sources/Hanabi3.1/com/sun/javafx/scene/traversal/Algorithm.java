package com.sun.javafx.scene.traversal;

import javafx.scene.Node;

public interface Algorithm {
   Node select(Node var1, Direction var2, TraversalContext var3);

   Node selectFirst(TraversalContext var1);

   Node selectLast(TraversalContext var1);
}
