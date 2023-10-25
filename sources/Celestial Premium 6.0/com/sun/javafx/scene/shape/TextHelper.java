/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.geometry.Bounds
 *  javafx.scene.Node
 *  javafx.scene.shape.Shape
 *  javafx.scene.text.Text
 */
package com.sun.javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.shape.ShapeHelper;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.util.Utils;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

public class TextHelper
extends ShapeHelper {
    private static final TextHelper theInstance = new TextHelper();
    private static TextAccessor textAccessor;

    private static TextHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(Text text) {
        TextHelper.setHelper((Node)text, TextHelper.getInstance());
    }

    public static BaseBounds superComputeGeomBounds(Node node, BaseBounds baseBounds, BaseTransform baseTransform) {
        return ((TextHelper)TextHelper.getHelper(node)).superComputeGeomBoundsImpl(node, baseBounds, baseTransform);
    }

    public static Bounds superComputeLayoutBounds(Node node) {
        return ((TextHelper)TextHelper.getHelper(node)).superComputeLayoutBoundsImpl(node);
    }

    @Override
    protected NGNode createPeerImpl(Node node) {
        return textAccessor.doCreatePeer(node);
    }

    @Override
    protected void updatePeerImpl(Node node) {
        super.updatePeerImpl(node);
        textAccessor.doUpdatePeer(node);
    }

    BaseBounds superComputeGeomBoundsImpl(Node node, BaseBounds baseBounds, BaseTransform baseTransform) {
        return super.computeGeomBoundsImpl(node, baseBounds, baseTransform);
    }

    Bounds superComputeLayoutBoundsImpl(Node node) {
        return super.computeLayoutBoundsImpl(node);
    }

    @Override
    protected BaseBounds computeGeomBoundsImpl(Node node, BaseBounds baseBounds, BaseTransform baseTransform) {
        return textAccessor.doComputeGeomBounds(node, baseBounds, baseTransform);
    }

    @Override
    protected Bounds computeLayoutBoundsImpl(Node node) {
        return textAccessor.doComputeLayoutBounds(node);
    }

    @Override
    protected boolean computeContainsImpl(Node node, double d, double d2) {
        return textAccessor.doComputeContains(node, d, d2);
    }

    @Override
    protected void geomChangedImpl(Node node) {
        super.geomChangedImpl(node);
        textAccessor.doGeomChanged(node);
    }

    @Override
    protected com.sun.javafx.geom.Shape configShapeImpl(Shape shape) {
        return textAccessor.doConfigShape(shape);
    }

    public static void setTextAccessor(TextAccessor textAccessor) {
        if (TextHelper.textAccessor != null) {
            throw new IllegalStateException();
        }
        TextHelper.textAccessor = textAccessor;
    }

    static {
        Utils.forceInit(Text.class);
    }

    public static interface TextAccessor {
        public NGNode doCreatePeer(Node var1);

        public void doUpdatePeer(Node var1);

        public BaseBounds doComputeGeomBounds(Node var1, BaseBounds var2, BaseTransform var3);

        public Bounds doComputeLayoutBounds(Node var1);

        public boolean doComputeContains(Node var1, double var2, double var4);

        public void doGeomChanged(Node var1);

        public com.sun.javafx.geom.Shape doConfigShape(Shape var1);
    }
}

