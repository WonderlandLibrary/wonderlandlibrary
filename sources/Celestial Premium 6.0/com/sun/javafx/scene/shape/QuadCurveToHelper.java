/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.shape.PathElement
 *  javafx.scene.shape.QuadCurveTo
 */
package com.sun.javafx.scene.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.scene.shape.PathElementHelper;
import com.sun.javafx.util.Utils;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.QuadCurveTo;

public class QuadCurveToHelper
extends PathElementHelper {
    private static final QuadCurveToHelper theInstance = new QuadCurveToHelper();
    private static QuadCurveToAccessor quadCurveToAccessor;

    private static QuadCurveToHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(QuadCurveTo quadCurveTo) {
        QuadCurveToHelper.setHelper((PathElement)quadCurveTo, QuadCurveToHelper.getInstance());
    }

    @Override
    protected void addToImpl(PathElement pathElement, Path2D path2D) {
        quadCurveToAccessor.doAddTo(pathElement, path2D);
    }

    public static void setQuadCurveToAccessor(QuadCurveToAccessor quadCurveToAccessor) {
        if (QuadCurveToHelper.quadCurveToAccessor != null) {
            throw new IllegalStateException();
        }
        QuadCurveToHelper.quadCurveToAccessor = quadCurveToAccessor;
    }

    static {
        Utils.forceInit(QuadCurveTo.class);
    }

    public static interface QuadCurveToAccessor {
        public void doAddTo(PathElement var1, Path2D var2);
    }
}

