/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.shape.PathElement
 */
package com.sun.javafx.scene.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.scene.shape.PathElementHelper;
import java.util.Collection;
import javafx.scene.shape.PathElement;

public final class PathUtils {
    private PathUtils() {
    }

    public static Path2D configShape(Collection<PathElement> collection, boolean bl) {
        Path2D path2D = new Path2D(bl ? 0 : 1, collection.size());
        for (PathElement pathElement : collection) {
            PathElementHelper.addTo(pathElement, path2D);
        }
        return path2D;
    }
}

