/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 */
package com.sun.javafx.scene;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import javafx.scene.Node;

public interface BoundsAccessor {
    public BaseBounds getGeomBounds(BaseBounds var1, BaseTransform var2, Node var3);
}

