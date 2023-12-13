package com.sun.javafx.jmx;

import javafx.scene.Node;
import javafx.scene.Parent;

public interface MXNodeAlgorithm {
   Object processLeafNode(Node var1, MXNodeAlgorithmContext var2);

   Object processContainerNode(Parent var1, MXNodeAlgorithmContext var2);
}
