/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.shape.ClosePath
 *  javafx.scene.shape.PathElement
 */
package com.sun.javafx.scene.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.scene.shape.PathElementHelper;
import com.sun.javafx.util.Utils;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.PathElement;

public class ClosePathHelper
extends PathElementHelper {
    private static final ClosePathHelper theInstance = new ClosePathHelper();
    private static ClosePathAccessor closePathAccessor;

    private static ClosePathHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(ClosePath closePath) {
        ClosePathHelper.setHelper((PathElement)closePath, ClosePathHelper.getInstance());
    }

    @Override
    protected void addToImpl(PathElement pathElement, Path2D path2D) {
        closePathAccessor.doAddTo(pathElement, path2D);
    }

    public static void setClosePathAccessor(ClosePathAccessor closePathAccessor) {
        if (ClosePathHelper.closePathAccessor != null) {
            throw new IllegalStateException();
        }
        ClosePathHelper.closePathAccessor = closePathAccessor;
    }

    static {
        Utils.forceInit(ClosePath.class);
    }

    public static interface ClosePathAccessor {
        public void doAddTo(PathElement var1, Path2D var2);
    }
}

