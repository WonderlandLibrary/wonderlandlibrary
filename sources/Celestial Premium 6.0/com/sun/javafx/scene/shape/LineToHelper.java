/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.shape.LineTo
 *  javafx.scene.shape.PathElement
 */
package com.sun.javafx.scene.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.scene.shape.PathElementHelper;
import com.sun.javafx.util.Utils;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.PathElement;

public class LineToHelper
extends PathElementHelper {
    private static final LineToHelper theInstance = new LineToHelper();
    private static LineToAccessor lineToAccessor;

    private static LineToHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(LineTo lineTo) {
        LineToHelper.setHelper((PathElement)lineTo, LineToHelper.getInstance());
    }

    @Override
    protected void addToImpl(PathElement pathElement, Path2D path2D) {
        lineToAccessor.doAddTo(pathElement, path2D);
    }

    public static void setLineToAccessor(LineToAccessor lineToAccessor) {
        if (LineToHelper.lineToAccessor != null) {
            throw new IllegalStateException();
        }
        LineToHelper.lineToAccessor = lineToAccessor;
    }

    static {
        Utils.forceInit(LineTo.class);
    }

    public static interface LineToAccessor {
        public void doAddTo(PathElement var1, Path2D var2);
    }
}

