/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 *  javafx.scene.image.ImageView
 */
package com.sun.javafx.scene;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.util.Utils;
import javafx.scene.Node;
import javafx.scene.image.ImageView;

public class ImageViewHelper
extends NodeHelper {
    private static final ImageViewHelper theInstance = new ImageViewHelper();
    private static ImageViewAccessor imageViewAccessor;

    private static ImageViewHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(ImageView imageView) {
        ImageViewHelper.setHelper((Node)imageView, ImageViewHelper.getInstance());
    }

    @Override
    protected NGNode createPeerImpl(Node node) {
        return imageViewAccessor.doCreatePeer(node);
    }

    @Override
    protected void updatePeerImpl(Node node) {
        super.updatePeerImpl(node);
        imageViewAccessor.doUpdatePeer(node);
    }

    @Override
    protected BaseBounds computeGeomBoundsImpl(Node node, BaseBounds baseBounds, BaseTransform baseTransform) {
        return imageViewAccessor.doComputeGeomBounds(node, baseBounds, baseTransform);
    }

    @Override
    protected boolean computeContainsImpl(Node node, double d, double d2) {
        return imageViewAccessor.doComputeContains(node, d, d2);
    }

    public static void setImageViewAccessor(ImageViewAccessor imageViewAccessor) {
        if (ImageViewHelper.imageViewAccessor != null) {
            throw new IllegalStateException();
        }
        ImageViewHelper.imageViewAccessor = imageViewAccessor;
    }

    static {
        Utils.forceInit(ImageView.class);
    }

    public static interface ImageViewAccessor {
        public NGNode doCreatePeer(Node var1);

        public void doUpdatePeer(Node var1);

        public BaseBounds doComputeGeomBounds(Node var1, BaseBounds var2, BaseTransform var3);

        public boolean doComputeContains(Node var1, double var2, double var4);
    }
}

