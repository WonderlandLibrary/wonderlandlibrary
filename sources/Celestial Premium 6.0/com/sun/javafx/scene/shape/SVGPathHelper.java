/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 *  javafx.scene.shape.SVGPath
 *  javafx.scene.shape.Shape
 */
package com.sun.javafx.scene.shape;

import com.sun.javafx.scene.shape.ShapeHelper;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.util.Utils;
import javafx.scene.Node;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;

public class SVGPathHelper
extends ShapeHelper {
    private static final SVGPathHelper theInstance = new SVGPathHelper();
    private static SVGPathAccessor svgPathAccessor;

    private static SVGPathHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(SVGPath sVGPath) {
        SVGPathHelper.setHelper((Node)sVGPath, SVGPathHelper.getInstance());
    }

    @Override
    protected NGNode createPeerImpl(Node node) {
        return svgPathAccessor.doCreatePeer(node);
    }

    @Override
    protected void updatePeerImpl(Node node) {
        super.updatePeerImpl(node);
        svgPathAccessor.doUpdatePeer(node);
    }

    @Override
    protected com.sun.javafx.geom.Shape configShapeImpl(Shape shape) {
        return svgPathAccessor.doConfigShape(shape);
    }

    public static void setSVGPathAccessor(SVGPathAccessor sVGPathAccessor) {
        if (svgPathAccessor != null) {
            throw new IllegalStateException();
        }
        svgPathAccessor = sVGPathAccessor;
    }

    static {
        Utils.forceInit(SVGPath.class);
    }

    public static interface SVGPathAccessor {
        public NGNode doCreatePeer(Node var1);

        public void doUpdatePeer(Node var1);

        public com.sun.javafx.geom.Shape doConfigShape(Shape var1);
    }
}

