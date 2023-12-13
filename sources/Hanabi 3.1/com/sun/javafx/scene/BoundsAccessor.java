package com.sun.javafx.scene;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import javafx.scene.Node;

public interface BoundsAccessor {
   BaseBounds getGeomBounds(BaseBounds var1, BaseTransform var2, Node var3);
}
