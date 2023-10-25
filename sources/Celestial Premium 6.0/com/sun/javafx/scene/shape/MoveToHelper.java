/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.shape.MoveTo
 *  javafx.scene.shape.PathElement
 */
package com.sun.javafx.scene.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.scene.shape.PathElementHelper;
import com.sun.javafx.util.Utils;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;

public class MoveToHelper
extends PathElementHelper {
    private static final MoveToHelper theInstance = new MoveToHelper();
    private static MoveToAccessor moveToAccessor;

    private static MoveToHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(MoveTo moveTo) {
        MoveToHelper.setHelper((PathElement)moveTo, MoveToHelper.getInstance());
    }

    @Override
    protected void addToImpl(PathElement pathElement, Path2D path2D) {
        moveToAccessor.doAddTo(pathElement, path2D);
    }

    public static void setMoveToAccessor(MoveToAccessor moveToAccessor) {
        if (MoveToHelper.moveToAccessor != null) {
            throw new IllegalStateException();
        }
        MoveToHelper.moveToAccessor = moveToAccessor;
    }

    static {
        Utils.forceInit(MoveTo.class);
    }

    public static interface MoveToAccessor {
        public void doAddTo(PathElement var1, Path2D var2);
    }
}

