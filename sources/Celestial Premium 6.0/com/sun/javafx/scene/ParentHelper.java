/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 *  javafx.scene.Parent
 */
package com.sun.javafx.scene;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.util.Utils;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.Parent;

public class ParentHelper
extends NodeHelper {
    private static final ParentHelper theInstance = new ParentHelper();
    private static ParentAccessor parentAccessor;

    private static ParentHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(Parent parent) {
        ParentHelper.setHelper((Node)parent, ParentHelper.getInstance());
    }

    public static void superProcessCSS(Node node) {
        ((ParentHelper)ParentHelper.getHelper(node)).superProcessCSSImpl(node);
    }

    public static List<String> getAllParentStylesheets(Parent parent) {
        return ((ParentHelper)ParentHelper.getHelper((Node)parent)).getAllParentStylesheetsImpl(parent);
    }

    @Override
    protected NGNode createPeerImpl(Node node) {
        return parentAccessor.doCreatePeer(node);
    }

    @Override
    protected void updatePeerImpl(Node node) {
        super.updatePeerImpl(node);
        parentAccessor.doUpdatePeer(node);
    }

    @Override
    protected BaseBounds computeGeomBoundsImpl(Node node, BaseBounds baseBounds, BaseTransform baseTransform) {
        return parentAccessor.doComputeGeomBounds(node, baseBounds, baseTransform);
    }

    @Override
    protected boolean computeContainsImpl(Node node, double d, double d2) {
        return parentAccessor.doComputeContains(node, d, d2);
    }

    void superProcessCSSImpl(Node node) {
        super.processCSSImpl(node);
    }

    @Override
    protected void processCSSImpl(Node node) {
        parentAccessor.doProcessCSS(node);
    }

    protected List<String> getAllParentStylesheetsImpl(Parent parent) {
        return parentAccessor.doGetAllParentStylesheets(parent);
    }

    @Override
    protected void pickNodeLocalImpl(Node node, PickRay pickRay, PickResultChooser pickResultChooser) {
        parentAccessor.doPickNodeLocal(node, pickRay, pickResultChooser);
    }

    public static boolean pickChildrenNode(Parent parent, PickRay pickRay, PickResultChooser pickResultChooser) {
        return parentAccessor.pickChildrenNode(parent, pickRay, pickResultChooser);
    }

    public static void setTraversalEngine(Parent parent, ParentTraversalEngine parentTraversalEngine) {
        parentAccessor.setTraversalEngine(parent, parentTraversalEngine);
    }

    public static ParentTraversalEngine getTraversalEngine(Parent parent) {
        return parentAccessor.getTraversalEngine(parent);
    }

    public static void setParentAccessor(ParentAccessor parentAccessor) {
        if (ParentHelper.parentAccessor != null) {
            throw new IllegalStateException();
        }
        ParentHelper.parentAccessor = parentAccessor;
    }

    static {
        Utils.forceInit(Parent.class);
    }

    public static interface ParentAccessor {
        public NGNode doCreatePeer(Node var1);

        public void doUpdatePeer(Node var1);

        public boolean doComputeContains(Node var1, double var2, double var4);

        public BaseBounds doComputeGeomBounds(Node var1, BaseBounds var2, BaseTransform var3);

        public void doProcessCSS(Node var1);

        public void doPickNodeLocal(Node var1, PickRay var2, PickResultChooser var3);

        public boolean pickChildrenNode(Parent var1, PickRay var2, PickResultChooser var3);

        public void setTraversalEngine(Parent var1, ParentTraversalEngine var2);

        public ParentTraversalEngine getTraversalEngine(Parent var1);

        public List<String> doGetAllParentStylesheets(Parent var1);
    }
}

