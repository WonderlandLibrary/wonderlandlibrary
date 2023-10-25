/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.shape.PathElement
 *  javafx.scene.shape.VLineTo
 */
package com.sun.javafx.scene.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.scene.shape.PathElementHelper;
import com.sun.javafx.util.Utils;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.VLineTo;

public class VLineToHelper
extends PathElementHelper {
    private static final VLineToHelper theInstance = new VLineToHelper();
    private static VLineToAccessor vLineToAccessor;

    private static VLineToHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(VLineTo vLineTo) {
        VLineToHelper.setHelper((PathElement)vLineTo, VLineToHelper.getInstance());
    }

    @Override
    protected void addToImpl(PathElement pathElement, Path2D path2D) {
        vLineToAccessor.doAddTo(pathElement, path2D);
    }

    public static void setVLineToAccessor(VLineToAccessor vLineToAccessor) {
        if (VLineToHelper.vLineToAccessor != null) {
            throw new IllegalStateException();
        }
        VLineToHelper.vLineToAccessor = vLineToAccessor;
    }

    static {
        Utils.forceInit(VLineTo.class);
    }

    public static interface VLineToAccessor {
        public void doAddTo(PathElement var1, Path2D var2);
    }
}

