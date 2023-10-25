/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.shape.ArcTo
 *  javafx.scene.shape.PathElement
 */
package com.sun.javafx.scene.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.scene.shape.PathElementHelper;
import com.sun.javafx.util.Utils;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.PathElement;

public class ArcToHelper
extends PathElementHelper {
    private static final ArcToHelper theInstance = new ArcToHelper();
    private static ArcToAccessor arcToAccessor;

    private static ArcToHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(ArcTo arcTo) {
        ArcToHelper.setHelper((PathElement)arcTo, ArcToHelper.getInstance());
    }

    @Override
    protected void addToImpl(PathElement pathElement, Path2D path2D) {
        arcToAccessor.doAddTo(pathElement, path2D);
    }

    public static void setArcToAccessor(ArcToAccessor arcToAccessor) {
        if (ArcToHelper.arcToAccessor != null) {
            throw new IllegalStateException();
        }
        ArcToHelper.arcToAccessor = arcToAccessor;
    }

    static {
        Utils.forceInit(ArcTo.class);
    }

    public static interface ArcToAccessor {
        public void doAddTo(PathElement var1, Path2D var2);
    }
}

