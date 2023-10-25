/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NodeEffectInput;
import com.sun.javafx.sg.prism.NodePath;
import com.sun.prism.Graphics;
import com.sun.prism.Texture;
import com.sun.scenario.effect.Blend;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.prism.PrEffectHelper;
import com.sun.scenario.effect.impl.prism.PrTexture;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.scene.Node;

public class NGGroup
extends NGNode {
    private Blend.Mode blendMode = Blend.Mode.SRC_OVER;
    private List<NGNode> children = new ArrayList<NGNode>(1);
    private List<NGNode> unmod = Collections.unmodifiableList(this.children);
    private List<NGNode> removed;
    private final List<NGNode> viewOrderChildren = new ArrayList<NGNode>(1);
    private static final int REGION_INTERSECTS_MASK = 0x15555555;

    public List<NGNode> getChildren() {
        return this.unmod;
    }

    public void add(int n, NGNode nGNode) {
        if (n < -1 || n > this.children.size()) {
            throw new IndexOutOfBoundsException("invalid index");
        }
        NGNode nGNode2 = nGNode;
        nGNode2.setParent(this);
        this.childDirty = true;
        if (n == -1) {
            this.children.add(nGNode);
        } else {
            this.children.add(n, nGNode);
        }
        nGNode2.markDirty();
        this.markTreeDirtyNoIncrement();
        this.geometryChanged();
    }

    public void clearFrom(int n) {
        if (n < this.children.size()) {
            this.children.subList(n, this.children.size()).clear();
            this.geometryChanged();
            this.childDirty = true;
            this.markTreeDirtyNoIncrement();
        }
    }

    public List<NGNode> getRemovedChildren() {
        return this.removed;
    }

    public void addToRemoved(NGNode nGNode) {
        if (this.removed == null) {
            this.removed = new ArrayList<NGNode>();
        }
        if (this.dirtyChildrenAccumulated > 12) {
            return;
        }
        this.removed.add(nGNode);
        ++this.dirtyChildrenAccumulated;
        if (this.dirtyChildrenAccumulated > 12) {
            this.removed.clear();
        }
    }

    @Override
    protected void clearDirty() {
        super.clearDirty();
        if (this.removed != null) {
            this.removed.clear();
        }
    }

    public void remove(NGNode nGNode) {
        this.children.remove(nGNode);
        this.geometryChanged();
        this.childDirty = true;
        this.markTreeDirtyNoIncrement();
    }

    public void remove(int n) {
        this.children.remove(n);
        this.geometryChanged();
        this.childDirty = true;
        this.markTreeDirtyNoIncrement();
    }

    public void clear() {
        this.children.clear();
        this.childDirty = false;
        this.geometryChanged();
        this.markTreeDirtyNoIncrement();
    }

    private List<NGNode> getOrderedChildren() {
        if (!this.viewOrderChildren.isEmpty()) {
            return this.viewOrderChildren;
        }
        return this.children;
    }

    public void setViewOrderChildren(List<Node> list) {
        this.viewOrderChildren.clear();
        for (Node node : list) {
            Object p = NodeHelper.getPeer(node);
            this.viewOrderChildren.add((NGNode)p);
        }
        this.visualsChanged();
    }

    public void setBlendMode(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("Mode must be non-null");
        }
        if (this.blendMode != object) {
            this.blendMode = (Blend.Mode)((Object)object);
            this.visualsChanged();
        }
    }

    @Override
    public void renderForcedContent(Graphics graphics) {
        List<NGNode> list = this.getOrderedChildren();
        if (list == null) {
            return;
        }
        for (int i = 0; i < list.size(); ++i) {
            list.get(i).renderForcedContent(graphics);
        }
    }

    @Override
    protected void renderContent(Graphics graphics) {
        int n;
        List<NGNode> list = this.getOrderedChildren();
        if (list == null) {
            return;
        }
        NodePath nodePath = graphics.getRenderRoot();
        int n2 = 0;
        if (nodePath != null) {
            if (nodePath.hasNext()) {
                nodePath.next();
                n2 = list.indexOf(nodePath.getCurrentNode());
                for (n = 0; n < n2; ++n) {
                    list.get(n).clearDirtyTree();
                }
            } else {
                graphics.setRenderRoot(null);
            }
        }
        if (this.blendMode == Blend.Mode.SRC_OVER || list.size() < 2) {
            for (n = n2; n < list.size(); ++n) {
                NGNode nGNode;
                try {
                    nGNode = list.get(n);
                }
                catch (Exception exception) {
                    nGNode = null;
                }
                if (nGNode == null) continue;
                nGNode.render(graphics);
            }
            return;
        }
        Blend blend = new Blend(this.blendMode, null, null);
        FilterContext filterContext = NGGroup.getFilterContext(graphics);
        ImageData imageData = null;
        boolean bl = true;
        do {
            Object object;
            BaseTransform baseTransform = graphics.getTransformNoClone().copy();
            if (imageData != null) {
                imageData.unref();
                imageData = null;
            }
            Rectangle rectangle = PrEffectHelper.getGraphicsClipNoClone(graphics);
            for (int i = n2; i < list.size(); ++i) {
                object = list.get(i);
                ImageData imageData2 = NodeEffectInput.getImageDataForNode(filterContext, (NGNode)object, false, baseTransform, rectangle);
                if (imageData == null) {
                    imageData = imageData2;
                    continue;
                }
                ImageData imageData3 = blend.filterImageDatas(filterContext, baseTransform, rectangle, null, new ImageData[]{imageData, imageData2});
                imageData.unref();
                imageData2.unref();
                imageData = imageData3;
            }
            if (imageData == null || !(bl = imageData.validate(filterContext))) continue;
            Rectangle rectangle2 = imageData.getUntransformedBounds();
            object = (PrDrawable)imageData.getUntransformedImage();
            graphics.setTransform(imageData.getTransform());
            graphics.drawTexture((Texture)((PrTexture)object).getTextureObject(), rectangle2.x, rectangle2.y, rectangle2.width, rectangle2.height);
        } while (imageData == null || !bl);
        if (imageData != null) {
            imageData.unref();
        }
    }

    @Override
    protected boolean hasOverlappingContents() {
        int n;
        if (this.blendMode != Blend.Mode.SRC_OVER) {
            return false;
        }
        List<NGNode> list = this.getOrderedChildren();
        int n2 = n = list == null ? 0 : list.size();
        if (n == 1) {
            return list.get(0).hasOverlappingContents();
        }
        return n != 0;
    }

    public boolean isEmpty() {
        return this.children == null || this.children.isEmpty();
    }

    @Override
    protected boolean hasVisuals() {
        return false;
    }

    @Override
    protected boolean needsBlending() {
        Blend.Mode mode = this.getNodeBlendMode();
        return mode != null;
    }

    @Override
    protected NGNode.RenderRootResult computeRenderRoot(NodePath nodePath, RectBounds rectBounds, int n, BaseTransform baseTransform, GeneralTransform3D generalTransform3D) {
        if (n != -1) {
            int n2 = this.cullingBits >> n * 2;
            if ((n2 & 3) == 0) {
                return NGNode.RenderRootResult.NO_RENDER_ROOT;
            }
            if ((n2 & 2) != 0) {
                n = -1;
            }
        }
        if (!this.isVisible()) {
            return NGNode.RenderRootResult.NO_RENDER_ROOT;
        }
        if ((double)this.getOpacity() != 1.0 || this.getEffect() != null && this.getEffect().reducesOpaquePixels() || this.needsBlending()) {
            return NGNode.RenderRootResult.NO_RENDER_ROOT;
        }
        if (this.getClipNode() != null) {
            NGNode nGNode = this.getClipNode();
            RectBounds rectBounds2 = nGNode.getOpaqueRegion();
            if (rectBounds2 == null) {
                return NGNode.RenderRootResult.NO_RENDER_ROOT;
            }
            TEMP_TRANSFORM.deriveWithNewTransform(baseTransform).deriveWithConcatenation(this.getTransform()).deriveWithConcatenation(nGNode.getTransform());
            if (!NGGroup.checkBoundsInQuad(rectBounds2, rectBounds, TEMP_TRANSFORM, generalTransform3D)) {
                return NGNode.RenderRootResult.NO_RENDER_ROOT;
            }
        }
        double d = baseTransform.getMxx();
        double d2 = baseTransform.getMxy();
        double d3 = baseTransform.getMxz();
        double d4 = baseTransform.getMxt();
        double d5 = baseTransform.getMyx();
        double d6 = baseTransform.getMyy();
        double d7 = baseTransform.getMyz();
        double d8 = baseTransform.getMyt();
        double d9 = baseTransform.getMzx();
        double d10 = baseTransform.getMzy();
        double d11 = baseTransform.getMzz();
        double d12 = baseTransform.getMzt();
        BaseTransform baseTransform2 = baseTransform.deriveWithConcatenation(this.getTransform());
        NGNode.RenderRootResult renderRootResult = NGNode.RenderRootResult.NO_RENDER_ROOT;
        boolean bl = true;
        List<NGNode> list = this.getOrderedChildren();
        for (int i = list.size() - 1; i >= 0; --i) {
            NGNode nGNode = list.get(i);
            renderRootResult = nGNode.computeRenderRoot(nodePath, rectBounds, n, baseTransform2, generalTransform3D);
            bl &= nGNode.isClean();
            if (renderRootResult == NGNode.RenderRootResult.HAS_RENDER_ROOT) {
                nodePath.add(this);
                break;
            }
            if (renderRootResult != NGNode.RenderRootResult.HAS_RENDER_ROOT_AND_IS_CLEAN) continue;
            nodePath.add(this);
            if (bl) break;
            renderRootResult = NGNode.RenderRootResult.HAS_RENDER_ROOT;
            break;
        }
        baseTransform.restoreTransform(d, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12);
        return renderRootResult;
    }

    @Override
    protected void markCullRegions(DirtyRegionContainer dirtyRegionContainer, int n, BaseTransform baseTransform, GeneralTransform3D generalTransform3D) {
        super.markCullRegions(dirtyRegionContainer, n, baseTransform, generalTransform3D);
        if (this.cullingBits == -1 || this.cullingBits != 0 && (this.cullingBits & 0x15555555) != 0) {
            double d = baseTransform.getMxx();
            double d2 = baseTransform.getMxy();
            double d3 = baseTransform.getMxz();
            double d4 = baseTransform.getMxt();
            double d5 = baseTransform.getMyx();
            double d6 = baseTransform.getMyy();
            double d7 = baseTransform.getMyz();
            double d8 = baseTransform.getMyt();
            double d9 = baseTransform.getMzx();
            double d10 = baseTransform.getMzy();
            double d11 = baseTransform.getMzz();
            double d12 = baseTransform.getMzt();
            BaseTransform baseTransform2 = baseTransform.deriveWithConcatenation(this.getTransform());
            List<NGNode> list = this.getOrderedChildren();
            for (int i = 0; i < list.size(); ++i) {
                NGNode nGNode = list.get(i);
                nGNode.markCullRegions(dirtyRegionContainer, this.cullingBits, baseTransform2, generalTransform3D);
            }
            baseTransform.restoreTransform(d, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12);
        }
    }

    @Override
    public void drawDirtyOpts(BaseTransform baseTransform, GeneralTransform3D generalTransform3D, Rectangle rectangle, int[] arrn, int n) {
        super.drawDirtyOpts(baseTransform, generalTransform3D, rectangle, arrn, n);
        BaseTransform baseTransform2 = baseTransform.copy();
        baseTransform2 = baseTransform2.deriveWithConcatenation(this.getTransform());
        List<NGNode> list = this.getOrderedChildren();
        for (int i = 0; i < list.size(); ++i) {
            NGNode nGNode = list.get(i);
            nGNode.drawDirtyOpts(baseTransform2, generalTransform3D, rectangle, arrn, n);
        }
    }
}

