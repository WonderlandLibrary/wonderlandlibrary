/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.shape.PathElement
 */
package com.sun.javafx.scene.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.util.Utils;
import javafx.scene.shape.PathElement;

public abstract class PathElementHelper {
    private static PathElementAccessor pathElementAccessor;

    protected PathElementHelper() {
    }

    private static PathElementHelper getHelper(PathElement pathElement) {
        return pathElementAccessor.getHelper(pathElement);
    }

    protected static void setHelper(PathElement pathElement, PathElementHelper pathElementHelper) {
        pathElementAccessor.setHelper(pathElement, pathElementHelper);
    }

    public static void addTo(PathElement pathElement, Path2D path2D) {
        PathElementHelper.getHelper(pathElement).addToImpl(pathElement, path2D);
    }

    protected abstract void addToImpl(PathElement var1, Path2D var2);

    public static void setPathElementAccessor(PathElementAccessor pathElementAccessor) {
        if (PathElementHelper.pathElementAccessor != null) {
            throw new IllegalStateException();
        }
        PathElementHelper.pathElementAccessor = pathElementAccessor;
    }

    static {
        Utils.forceInit(PathElement.class);
    }

    public static interface PathElementAccessor {
        public PathElementHelper getHelper(PathElement var1);

        public void setHelper(PathElement var1, PathElementHelper var2);
    }
}

