/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.shape.CubicCurveTo
 *  javafx.scene.shape.PathElement
 */
package com.sun.javafx.scene.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.scene.shape.PathElementHelper;
import com.sun.javafx.util.Utils;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.PathElement;

public class CubicCurveToHelper
extends PathElementHelper {
    private static final CubicCurveToHelper theInstance = new CubicCurveToHelper();
    private static CubicCurveToAccessor cubicCurveToAccessor;

    private static CubicCurveToHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(CubicCurveTo cubicCurveTo) {
        CubicCurveToHelper.setHelper((PathElement)cubicCurveTo, CubicCurveToHelper.getInstance());
    }

    @Override
    protected void addToImpl(PathElement pathElement, Path2D path2D) {
        cubicCurveToAccessor.doAddTo(pathElement, path2D);
    }

    public static void setCubicCurveToAccessor(CubicCurveToAccessor cubicCurveToAccessor) {
        if (CubicCurveToHelper.cubicCurveToAccessor != null) {
            throw new IllegalStateException();
        }
        CubicCurveToHelper.cubicCurveToAccessor = cubicCurveToAccessor;
    }

    static {
        Utils.forceInit(CubicCurveTo.class);
    }

    public static interface CubicCurveToAccessor {
        public void doAddTo(PathElement var1, Path2D var2);
    }
}

