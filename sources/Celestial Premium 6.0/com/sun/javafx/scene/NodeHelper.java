/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.binding.BooleanExpression
 *  javafx.beans.property.BooleanProperty
 *  javafx.css.CssMetaData
 *  javafx.css.Style
 *  javafx.css.Styleable
 *  javafx.css.StyleableProperty
 *  javafx.geometry.Bounds
 *  javafx.scene.Node
 *  javafx.scene.SubScene
 *  javafx.scene.shape.Shape
 *  javafx.scene.shape.Shape3D
 *  javafx.scene.text.Font
 */
package com.sun.javafx.scene;

import com.sun.glass.ui.Accessible;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.util.Utils;
import java.util.List;
import java.util.Map;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.BooleanProperty;
import javafx.css.CssMetaData;
import javafx.css.Style;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.shape.Shape;
import javafx.scene.shape.Shape3D;
import javafx.scene.text.Font;

public abstract class NodeHelper {
    private static NodeAccessor nodeAccessor;

    protected NodeHelper() {
    }

    protected static NodeHelper getHelper(Node node) {
        NodeHelper nodeHelper = nodeAccessor.getHelper(node);
        if (nodeHelper == null) {
            String string = node instanceof Shape ? "Shape" : (node instanceof Shape3D ? "Shape3D" : "Node");
            throw new UnsupportedOperationException("Applications should not extend the " + string + " class directly.");
        }
        return nodeHelper;
    }

    protected static void setHelper(Node node, NodeHelper nodeHelper) {
        nodeAccessor.setHelper(node, nodeHelper);
    }

    public static NGNode createPeer(Node node) {
        return NodeHelper.getHelper(node).createPeerImpl(node);
    }

    public static void markDirty(Node node, DirtyBits dirtyBits) {
        NodeHelper.getHelper(node).markDirtyImpl(node, dirtyBits);
    }

    public static void updatePeer(Node node) {
        NodeHelper.getHelper(node).updatePeerImpl(node);
    }

    public static Bounds computeLayoutBounds(Node node) {
        return NodeHelper.getHelper(node).computeLayoutBoundsImpl(node);
    }

    public static BaseBounds computeGeomBounds(Node node, BaseBounds baseBounds, BaseTransform baseTransform) {
        return NodeHelper.getHelper(node).computeGeomBoundsImpl(node, baseBounds, baseTransform);
    }

    public static void transformsChanged(Node node) {
        NodeHelper.getHelper(node).transformsChangedImpl(node);
    }

    public static boolean computeContains(Node node, double d, double d2) {
        return NodeHelper.getHelper(node).computeContainsImpl(node, d, d2);
    }

    public static void pickNodeLocal(Node node, PickRay pickRay, PickResultChooser pickResultChooser) {
        NodeHelper.getHelper(node).pickNodeLocalImpl(node, pickRay, pickResultChooser);
    }

    public static boolean computeIntersects(Node node, PickRay pickRay, PickResultChooser pickResultChooser) {
        return NodeHelper.getHelper(node).computeIntersectsImpl(node, pickRay, pickResultChooser);
    }

    public static void geomChanged(Node node) {
        NodeHelper.getHelper(node).geomChangedImpl(node);
    }

    public static void notifyLayoutBoundsChanged(Node node) {
        NodeHelper.getHelper(node).notifyLayoutBoundsChangedImpl(node);
    }

    public static void processCSS(Node node) {
        NodeHelper.getHelper(node).processCSSImpl(node);
    }

    protected abstract NGNode createPeerImpl(Node var1);

    protected abstract boolean computeContainsImpl(Node var1, double var2, double var4);

    protected abstract BaseBounds computeGeomBoundsImpl(Node var1, BaseBounds var2, BaseTransform var3);

    protected void markDirtyImpl(Node node, DirtyBits dirtyBits) {
        nodeAccessor.doMarkDirty(node, dirtyBits);
    }

    protected void updatePeerImpl(Node node) {
        nodeAccessor.doUpdatePeer(node);
    }

    protected Bounds computeLayoutBoundsImpl(Node node) {
        return nodeAccessor.doComputeLayoutBounds(node);
    }

    protected void transformsChangedImpl(Node node) {
        nodeAccessor.doTransformsChanged(node);
    }

    protected void pickNodeLocalImpl(Node node, PickRay pickRay, PickResultChooser pickResultChooser) {
        nodeAccessor.doPickNodeLocal(node, pickRay, pickResultChooser);
    }

    protected boolean computeIntersectsImpl(Node node, PickRay pickRay, PickResultChooser pickResultChooser) {
        return nodeAccessor.doComputeIntersects(node, pickRay, pickResultChooser);
    }

    protected void geomChangedImpl(Node node) {
        nodeAccessor.doGeomChanged(node);
    }

    protected void notifyLayoutBoundsChangedImpl(Node node) {
        nodeAccessor.doNotifyLayoutBoundsChanged(node);
    }

    protected void processCSSImpl(Node node) {
        nodeAccessor.doProcessCSS(node);
    }

    public static boolean isDirty(Node node, DirtyBits dirtyBits) {
        return nodeAccessor.isDirty(node, dirtyBits);
    }

    public static boolean isDirtyEmpty(Node node) {
        return nodeAccessor.isDirtyEmpty(node);
    }

    public static void syncPeer(Node node) {
        nodeAccessor.syncPeer(node);
    }

    public static <P extends NGNode> P getPeer(Node node) {
        return nodeAccessor.getPeer(node);
    }

    public static BaseTransform getLeafTransform(Node node) {
        return nodeAccessor.getLeafTransform(node);
    }

    public static void layoutBoundsChanged(Node node) {
        nodeAccessor.layoutBoundsChanged(node);
    }

    public static void setShowMnemonics(Node node, boolean bl) {
        nodeAccessor.setShowMnemonics(node, bl);
    }

    public static boolean isShowMnemonics(Node node) {
        return nodeAccessor.isShowMnemonics(node);
    }

    public static BooleanProperty showMnemonicsProperty(Node node) {
        return nodeAccessor.showMnemonicsProperty(node);
    }

    public static boolean traverse(Node node, Direction direction) {
        return nodeAccessor.traverse(node, direction);
    }

    public static double getPivotX(Node node) {
        return nodeAccessor.getPivotX(node);
    }

    public static double getPivotY(Node node) {
        return nodeAccessor.getPivotY(node);
    }

    public static double getPivotZ(Node node) {
        return nodeAccessor.getPivotZ(node);
    }

    public static void pickNode(Node node, PickRay pickRay, PickResultChooser pickResultChooser) {
        nodeAccessor.pickNode(node, pickRay, pickResultChooser);
    }

    public static boolean intersects(Node node, PickRay pickRay, PickResultChooser pickResultChooser) {
        return nodeAccessor.intersects(node, pickRay, pickResultChooser);
    }

    public static double intersectsBounds(Node node, PickRay pickRay) {
        return nodeAccessor.intersectsBounds(node, pickRay);
    }

    public static void layoutNodeForPrinting(Node node) {
        nodeAccessor.layoutNodeForPrinting(node);
    }

    public static boolean isDerivedDepthTest(Node node) {
        return nodeAccessor.isDerivedDepthTest(node);
    }

    public static SubScene getSubScene(Node node) {
        return nodeAccessor.getSubScene(node);
    }

    public static Accessible getAccessible(Node node) {
        return nodeAccessor.getAccessible(node);
    }

    public static void reapplyCSS(Node node) {
        nodeAccessor.reapplyCSS(node);
    }

    public static void recalculateRelativeSizeProperties(Node node, Font font) {
        nodeAccessor.recalculateRelativeSizeProperties(node, font);
    }

    public static boolean isTreeVisible(Node node) {
        return nodeAccessor.isTreeVisible(node);
    }

    public static BooleanExpression treeVisibleProperty(Node node) {
        return nodeAccessor.treeVisibleProperty(node);
    }

    public static boolean isTreeShowing(Node node) {
        return nodeAccessor.isTreeShowing(node);
    }

    public static List<Style> getMatchingStyles(CssMetaData cssMetaData, Styleable styleable) {
        return nodeAccessor.getMatchingStyles(cssMetaData, styleable);
    }

    public static Map<StyleableProperty<?>, List<Style>> findStyles(Node node, Map<StyleableProperty<?>, List<Style>> map) {
        return nodeAccessor.findStyles(node, map);
    }

    public static void setNodeAccessor(NodeAccessor nodeAccessor) {
        if (NodeHelper.nodeAccessor != null) {
            throw new IllegalStateException();
        }
        NodeHelper.nodeAccessor = nodeAccessor;
    }

    public static NodeAccessor getNodeAccessor() {
        if (nodeAccessor == null) {
            throw new IllegalStateException();
        }
        return nodeAccessor;
    }

    static {
        Utils.forceInit(Node.class);
    }

    public static interface NodeAccessor {
        public NodeHelper getHelper(Node var1);

        public void setHelper(Node var1, NodeHelper var2);

        public void doMarkDirty(Node var1, DirtyBits var2);

        public void doUpdatePeer(Node var1);

        public BaseTransform getLeafTransform(Node var1);

        public Bounds doComputeLayoutBounds(Node var1);

        public void doTransformsChanged(Node var1);

        public void doPickNodeLocal(Node var1, PickRay var2, PickResultChooser var3);

        public boolean doComputeIntersects(Node var1, PickRay var2, PickResultChooser var3);

        public void doGeomChanged(Node var1);

        public void doNotifyLayoutBoundsChanged(Node var1);

        public void doProcessCSS(Node var1);

        public boolean isDirty(Node var1, DirtyBits var2);

        public boolean isDirtyEmpty(Node var1);

        public void syncPeer(Node var1);

        public <P extends NGNode> P getPeer(Node var1);

        public void layoutBoundsChanged(Node var1);

        public void setShowMnemonics(Node var1, boolean var2);

        public boolean isShowMnemonics(Node var1);

        public BooleanProperty showMnemonicsProperty(Node var1);

        public boolean traverse(Node var1, Direction var2);

        public double getPivotX(Node var1);

        public double getPivotY(Node var1);

        public double getPivotZ(Node var1);

        public void pickNode(Node var1, PickRay var2, PickResultChooser var3);

        public boolean intersects(Node var1, PickRay var2, PickResultChooser var3);

        public double intersectsBounds(Node var1, PickRay var2);

        public void layoutNodeForPrinting(Node var1);

        public boolean isDerivedDepthTest(Node var1);

        public SubScene getSubScene(Node var1);

        public void setLabeledBy(Node var1, Node var2);

        public Accessible getAccessible(Node var1);

        public void reapplyCSS(Node var1);

        public void recalculateRelativeSizeProperties(Node var1, Font var2);

        public boolean isTreeVisible(Node var1);

        public BooleanExpression treeVisibleProperty(Node var1);

        public boolean isTreeShowing(Node var1);

        public List<Style> getMatchingStyles(CssMetaData var1, Styleable var2);

        public Map<StyleableProperty<?>, List<Style>> findStyles(Node var1, Map<StyleableProperty<?>, List<Style>> var2);
    }
}

