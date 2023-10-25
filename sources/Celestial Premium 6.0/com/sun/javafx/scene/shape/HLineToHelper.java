/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.shape.HLineTo
 *  javafx.scene.shape.PathElement
 */
package com.sun.javafx.scene.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.scene.shape.PathElementHelper;
import com.sun.javafx.util.Utils;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.PathElement;

public class HLineToHelper
extends PathElementHelper {
    private static final HLineToHelper theInstance = new HLineToHelper();
    private static HLineToAccessor hLineToAccessor;

    private static HLineToHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(HLineTo hLineTo) {
        HLineToHelper.setHelper((PathElement)hLineTo, HLineToHelper.getInstance());
    }

    @Override
    protected void addToImpl(PathElement pathElement, Path2D path2D) {
        hLineToAccessor.doAddTo(pathElement, path2D);
    }

    public static void setHLineToAccessor(HLineToAccessor hLineToAccessor) {
        if (HLineToHelper.hLineToAccessor != null) {
            throw new IllegalStateException();
        }
        HLineToHelper.hLineToAccessor = hLineToAccessor;
    }

    static {
        Utils.forceInit(HLineTo.class);
    }

    public static interface HLineToAccessor {
        public void doAddTo(PathElement var1, Path2D var2);
    }
}

