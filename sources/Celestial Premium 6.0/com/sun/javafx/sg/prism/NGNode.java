/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.javafx.logging.PulseLogger
 *  javafx.scene.CacheHint
 */
package com.sun.javafx.sg.prism;

import com.sun.glass.ui.Screen;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.BoxBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.javafx.logging.PulseLogger;
import com.sun.javafx.sg.prism.CacheFilter;
import com.sun.javafx.sg.prism.DirtyHint;
import com.sun.javafx.sg.prism.EffectFilter;
import com.sun.javafx.sg.prism.NGGroup;
import com.sun.javafx.sg.prism.NGRectangle;
import com.sun.javafx.sg.prism.NGRegion;
import com.sun.javafx.sg.prism.NodeEffectInput;
import com.sun.javafx.sg.prism.NodePath;
import com.sun.prism.CompositeMode;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.PrinterGraphics;
import com.sun.prism.RTTexture;
import com.sun.prism.ReadbackGraphics;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.scenario.effect.Blend;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.prism.PrEffectHelper;
import com.sun.scenario.effect.impl.prism.PrFilterContext;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.CacheHint;

public abstract class NGNode {
    private static final GraphicsPipeline pipeline = GraphicsPipeline.getPipeline();
    private static final Boolean effectsSupported = pipeline == null ? false : pipeline.isEffectSupported();
    private String name;
    private static final BoxBounds TEMP_BOUNDS = new BoxBounds();
    private static final RectBounds TEMP_RECT_BOUNDS = new RectBounds();
    protected static final Affine3D TEMP_TRANSFORM = new Affine3D();
    static final int DIRTY_REGION_INTERSECTS_NODE_BOUNDS = 1;
    static final int DIRTY_REGION_CONTAINS_NODE_BOUNDS = 2;
    static final int DIRTY_REGION_CONTAINS_OR_INTERSECTS_NODE_BOUNDS = 3;
    private BaseTransform transform = BaseTransform.IDENTITY_TRANSFORM;
    protected BaseBounds transformedBounds = new RectBounds();
    protected BaseBounds contentBounds = new RectBounds();
    BaseBounds dirtyBounds = new RectBounds();
    private boolean visible = true;
    protected DirtyFlag dirty = DirtyFlag.DIRTY;
    private NGNode parent;
    private boolean isClip;
    private NGNode clipNode;
    private float opacity = 1.0f;
    private double viewOrder = 0.0;
    private Blend.Mode nodeBlendMode;
    private boolean depthTest = true;
    private CacheFilter cacheFilter;
    private EffectFilter effectFilter;
    protected boolean childDirty = false;
    protected int dirtyChildrenAccumulated = 0;
    protected static final int DIRTY_CHILDREN_ACCUMULATED_THRESHOLD = 12;
    protected int cullingBits = 0;
    private DirtyHint hint;
    private RectBounds opaqueRegion = null;
    private boolean opaqueRegionInvalid = true;
    private int painted = 0;
    private static Point2D[] TEMP_POINTS2D_4 = new Point2D[]{new Point2D(), new Point2D(), new Point2D(), new Point2D()};

    protected NGNode() {
    }

    public void setVisible(boolean bl) {
        if (this.visible != bl) {
            this.visible = bl;
            this.markDirty();
        }
    }

    public void setContentBounds(BaseBounds baseBounds) {
        this.contentBounds = this.contentBounds.deriveWithNewBounds(baseBounds);
    }

    public void setTransformedBounds(BaseBounds baseBounds, boolean bl) {
        if (this.transformedBounds.equals(baseBounds)) {
            return;
        }
        if (this.dirtyBounds.isEmpty()) {
            this.dirtyBounds = this.dirtyBounds.deriveWithNewBounds(this.transformedBounds);
            this.dirtyBounds = this.dirtyBounds.deriveWithUnion(baseBounds);
        } else {
            this.dirtyBounds = this.dirtyBounds.deriveWithUnion(this.transformedBounds);
        }
        this.transformedBounds = this.transformedBounds.deriveWithNewBounds(baseBounds);
        if (this.hasVisuals() && !bl) {
            this.markDirty();
        }
    }

    public void setTransformMatrix(BaseTransform baseTransform) {
        if (this.transform.equals(baseTransform)) {
            return;
        }
        boolean bl = false;
        if (this.parent != null && this.parent.cacheFilter != null && PrismSettings.scrollCacheOpt) {
            if (this.hint == null) {
                this.hint = new DirtyHint();
            } else if (this.transform.getMxx() == baseTransform.getMxx() && this.transform.getMxy() == baseTransform.getMxy() && this.transform.getMyy() == baseTransform.getMyy() && this.transform.getMyx() == baseTransform.getMyx() && this.transform.getMxz() == baseTransform.getMxz() && this.transform.getMyz() == baseTransform.getMyz() && this.transform.getMzx() == baseTransform.getMzx() && this.transform.getMzy() == baseTransform.getMzy() && this.transform.getMzz() == baseTransform.getMzz() && this.transform.getMzt() == baseTransform.getMzt()) {
                bl = true;
                this.hint.translateXDelta = baseTransform.getMxt() - this.transform.getMxt();
                this.hint.translateYDelta = baseTransform.getMyt() - this.transform.getMyt();
            }
        }
        this.transform = this.transform.deriveWithNewTransform(baseTransform);
        if (bl) {
            this.markDirtyByTranslation();
        } else {
            this.markDirty();
        }
        this.invalidateOpaqueRegion();
    }

    public void setClipNode(NGNode nGNode) {
        if (nGNode != this.clipNode) {
            if (this.clipNode != null) {
                this.clipNode.setParent(null);
            }
            if (nGNode != null) {
                nGNode.setParent(this, true);
            }
            this.clipNode = nGNode;
            this.visualsChanged();
            this.invalidateOpaqueRegion();
        }
    }

    public void setOpacity(float f) {
        if (f < 0.0f || f > 1.0f) {
            throw new IllegalArgumentException("Internal Error: The opacity must be between 0 and 1");
        }
        if (f != this.opacity) {
            float f2 = this.opacity;
            this.opacity = f;
            this.markDirty();
            if (f2 < 1.0f && (f == 1.0f || f == 0.0f) || f < 1.0f && (f2 == 1.0f || f2 == 0.0f)) {
                this.invalidateOpaqueRegion();
            }
        }
    }

    public void setViewOrder(double d) {
        if (d != this.viewOrder) {
            this.viewOrder = d;
            this.visualsChanged();
        }
    }

    public void setNodeBlendMode(Blend.Mode mode) {
        if (this.nodeBlendMode != mode) {
            this.nodeBlendMode = mode;
            this.markDirty();
            this.invalidateOpaqueRegion();
        }
    }

    public void setDepthTest(boolean bl) {
        if (bl != this.depthTest) {
            this.depthTest = bl;
            this.visualsChanged();
        }
    }

    public void setCachedAsBitmap(boolean bl, CacheHint cacheHint) {
        if (cacheHint == null) {
            throw new IllegalArgumentException("Internal Error: cacheHint must not be null");
        }
        if (bl) {
            if (this.cacheFilter == null) {
                this.cacheFilter = new CacheFilter(this, cacheHint);
                this.markDirty();
            } else if (!this.cacheFilter.matchesHint(cacheHint)) {
                this.cacheFilter.setHint(cacheHint);
                this.markDirty();
            }
        } else if (this.cacheFilter != null) {
            this.cacheFilter.dispose();
            this.cacheFilter = null;
            this.markDirty();
        }
    }

    public void setEffect(Effect effect) {
        Effect effect2 = this.getEffect();
        if (PrismSettings.disableEffects) {
            effect = null;
        }
        if (this.effectFilter == null && effect != null) {
            this.effectFilter = new EffectFilter(effect, this);
            this.visualsChanged();
        } else if (this.effectFilter != null && this.effectFilter.getEffect() != effect) {
            this.effectFilter.dispose();
            this.effectFilter = null;
            if (effect != null) {
                this.effectFilter = new EffectFilter(effect, this);
            }
            this.visualsChanged();
        }
        if (effect2 != effect && (effect2 == null || effect == null)) {
            this.invalidateOpaqueRegion();
        }
    }

    public void effectChanged() {
        this.visualsChanged();
    }

    public boolean isContentBounds2D() {
        return this.contentBounds.is2D();
    }

    public NGNode getParent() {
        return this.parent;
    }

    public void setParent(NGNode nGNode) {
        this.setParent(nGNode, false);
    }

    private void setParent(NGNode nGNode, boolean bl) {
        this.parent = nGNode;
        this.isClip = bl;
    }

    public final void setName(String string) {
        this.name = string;
    }

    public final String getName() {
        return this.name;
    }

    protected final Effect getEffect() {
        return this.effectFilter == null ? null : this.effectFilter.getEffect();
    }

    public boolean isVisible() {
        return this.visible;
    }

    public final BaseTransform getTransform() {
        return this.transform;
    }

    public final float getOpacity() {
        return this.opacity;
    }

    public final Blend.Mode getNodeBlendMode() {
        return this.nodeBlendMode;
    }

    public final boolean isDepthTest() {
        return this.depthTest;
    }

    public final CacheFilter getCacheFilter() {
        return this.cacheFilter;
    }

    public final EffectFilter getEffectFilter() {
        return this.effectFilter;
    }

    public final NGNode getClipNode() {
        return this.clipNode;
    }

    public BaseBounds getContentBounds(BaseBounds baseBounds, BaseTransform baseTransform) {
        if (baseTransform.isTranslateOrIdentity()) {
            baseBounds = baseBounds.deriveWithNewBounds(this.contentBounds);
            if (!baseTransform.isIdentity()) {
                float f = (float)baseTransform.getMxt();
                float f2 = (float)baseTransform.getMyt();
                float f3 = (float)baseTransform.getMzt();
                baseBounds = baseBounds.deriveWithNewBounds(baseBounds.getMinX() + f, baseBounds.getMinY() + f2, baseBounds.getMinZ() + f3, baseBounds.getMaxX() + f, baseBounds.getMaxY() + f2, baseBounds.getMaxZ() + f3);
            }
            return baseBounds;
        }
        return this.computeBounds(baseBounds, baseTransform);
    }

    private BaseBounds computeBounds(BaseBounds baseBounds, BaseTransform baseTransform) {
        baseBounds = baseBounds.deriveWithNewBounds(this.contentBounds);
        return baseTransform.transform(this.contentBounds, baseBounds);
    }

    public final BaseBounds getClippedBounds(BaseBounds baseBounds, BaseTransform baseTransform) {
        BaseBounds baseBounds2 = this.getEffectBounds(baseBounds, baseTransform);
        if (this.clipNode != null) {
            float f = baseBounds2.getMinX();
            float f2 = baseBounds2.getMinY();
            float f3 = baseBounds2.getMinZ();
            float f4 = baseBounds2.getMaxX();
            float f5 = baseBounds2.getMaxY();
            float f6 = baseBounds2.getMaxZ();
            baseBounds2 = this.clipNode.getCompleteBounds(baseBounds2, baseTransform);
            baseBounds2.intersectWith(f, f2, f3, f4, f5, f6);
        }
        return baseBounds2;
    }

    public final BaseBounds getEffectBounds(BaseBounds baseBounds, BaseTransform baseTransform) {
        if (this.effectFilter != null) {
            return this.effectFilter.getBounds(baseBounds, baseTransform);
        }
        return this.getContentBounds(baseBounds, baseTransform);
    }

    public final BaseBounds getCompleteBounds(BaseBounds baseBounds, BaseTransform baseTransform) {
        if (baseTransform.isIdentity()) {
            baseBounds = baseBounds.deriveWithNewBounds(this.transformedBounds);
            return baseBounds;
        }
        if (this.transform.isIdentity()) {
            return this.getClippedBounds(baseBounds, baseTransform);
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
        BaseTransform baseTransform2 = baseTransform.deriveWithConcatenation(this.transform);
        baseBounds = this.getClippedBounds(baseBounds, baseTransform);
        if (baseTransform2 == baseTransform) {
            baseTransform.restoreTransform(d, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12);
        }
        return baseBounds;
    }

    protected void visualsChanged() {
        this.invalidateCache();
        this.markDirty();
    }

    protected void geometryChanged() {
        this.invalidateCache();
        this.invalidateOpaqueRegion();
        if (this.hasVisuals()) {
            this.markDirty();
        }
    }

    public final void markDirty() {
        if (this.dirty != DirtyFlag.DIRTY) {
            this.dirty = DirtyFlag.DIRTY;
            this.markTreeDirty();
        }
    }

    private void markDirtyByTranslation() {
        if (this.dirty == DirtyFlag.CLEAN) {
            if (this.parent != null && this.parent.dirty == DirtyFlag.CLEAN && !this.parent.childDirty) {
                this.dirty = DirtyFlag.DIRTY_BY_TRANSLATION;
                this.parent.childDirty = true;
                ++this.parent.dirtyChildrenAccumulated;
                this.parent.invalidateCacheByTranslation(this.hint);
                this.parent.markTreeDirty();
            } else {
                this.markDirty();
            }
        }
    }

    protected final void markTreeDirtyNoIncrement() {
        if (!(this.parent == null || this.parent.childDirty && this.dirty != DirtyFlag.DIRTY_BY_TRANSLATION)) {
            this.markTreeDirty();
        }
    }

    protected final void markTreeDirty() {
        boolean bl;
        NGNode nGNode = this.parent;
        boolean bl2 = this.isClip;
        boolean bl3 = bl = this.dirty == DirtyFlag.DIRTY_BY_TRANSLATION;
        while (nGNode != null && nGNode.dirty != DirtyFlag.DIRTY && (!nGNode.childDirty || bl2 || bl)) {
            if (bl2) {
                nGNode.dirty = DirtyFlag.DIRTY;
            } else if (!bl) {
                nGNode.childDirty = true;
                ++nGNode.dirtyChildrenAccumulated;
            }
            nGNode.invalidateCache();
            bl2 = nGNode.isClip;
            bl = nGNode.dirty == DirtyFlag.DIRTY_BY_TRANSLATION;
            nGNode = nGNode.parent;
        }
        if (nGNode != null && nGNode.dirty == DirtyFlag.CLEAN && !bl2 && !bl) {
            ++nGNode.dirtyChildrenAccumulated;
        }
        if (nGNode != null) {
            nGNode.invalidateCache();
        }
    }

    public final boolean isClean() {
        return this.dirty == DirtyFlag.CLEAN && !this.childDirty;
    }

    protected void clearDirty() {
        this.dirty = DirtyFlag.CLEAN;
        this.childDirty = false;
        this.dirtyBounds.makeEmpty();
        this.dirtyChildrenAccumulated = 0;
    }

    public void clearPainted() {
        this.painted = 0;
        if (this instanceof NGGroup) {
            List<NGNode> list = ((NGGroup)this).getChildren();
            for (int i = 0; i < list.size(); ++i) {
                list.get(i).clearPainted();
            }
        }
    }

    public void clearDirtyTree() {
        this.clearDirty();
        if (this.getClipNode() != null) {
            this.getClipNode().clearDirtyTree();
        }
        if (this instanceof NGGroup) {
            List<NGNode> list = ((NGGroup)this).getChildren();
            for (int i = 0; i < list.size(); ++i) {
                NGNode nGNode = list.get(i);
                if (nGNode.dirty == DirtyFlag.CLEAN && !nGNode.childDirty) continue;
                nGNode.clearDirtyTree();
            }
        }
    }

    protected final void invalidateCache() {
        if (this.cacheFilter != null) {
            this.cacheFilter.invalidate();
        }
    }

    protected final void invalidateCacheByTranslation(DirtyHint dirtyHint) {
        if (this.cacheFilter != null) {
            this.cacheFilter.invalidateByTranslation(dirtyHint.translateXDelta, dirtyHint.translateYDelta);
        }
    }

    public int accumulateDirtyRegions(RectBounds rectBounds, RectBounds rectBounds2, DirtyRegionPool dirtyRegionPool, DirtyRegionContainer dirtyRegionContainer, BaseTransform baseTransform, GeneralTransform3D generalTransform3D) {
        if (rectBounds == null || rectBounds2 == null || dirtyRegionPool == null || dirtyRegionContainer == null || baseTransform == null || generalTransform3D == null) {
            throw new NullPointerException();
        }
        if (this.dirty == DirtyFlag.CLEAN && !this.childDirty) {
            return 1;
        }
        if (this.dirty != DirtyFlag.CLEAN) {
            return this.accumulateNodeDirtyRegion(rectBounds, rectBounds2, dirtyRegionContainer, baseTransform, generalTransform3D);
        }
        assert (this.childDirty);
        return this.accumulateGroupDirtyRegion(rectBounds, rectBounds2, dirtyRegionPool, dirtyRegionContainer, baseTransform, generalTransform3D);
    }

    int accumulateNodeDirtyRegion(RectBounds rectBounds, RectBounds rectBounds2, DirtyRegionContainer dirtyRegionContainer, BaseTransform baseTransform, GeneralTransform3D generalTransform3D) {
        BaseBounds baseBounds = this.computeDirtyRegion(rectBounds2, baseTransform, generalTransform3D);
        if (baseBounds != rectBounds2) {
            baseBounds.flattenInto(rectBounds2);
        }
        if (rectBounds2.isEmpty() || rectBounds.disjoint(rectBounds2)) {
            return 1;
        }
        if (rectBounds2.contains(rectBounds)) {
            return 0;
        }
        rectBounds2.intersectWith(rectBounds);
        dirtyRegionContainer.addDirtyRegion(rectBounds2);
        return 1;
    }

    int accumulateGroupDirtyRegion(RectBounds rectBounds, RectBounds rectBounds2, DirtyRegionPool dirtyRegionPool, DirtyRegionContainer dirtyRegionContainer, BaseTransform baseTransform, GeneralTransform3D generalTransform3D) {
        NGNode nGNode;
        Object object;
        int n;
        Object object2;
        assert (this.childDirty);
        assert (this.dirty == DirtyFlag.CLEAN);
        int n2 = 1;
        if (this.dirtyChildrenAccumulated > 12) {
            n2 = this.accumulateNodeDirtyRegion(rectBounds, rectBounds2, dirtyRegionContainer, baseTransform, generalTransform3D);
            return n2;
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
        BaseTransform baseTransform2 = baseTransform;
        if (this.transform != null) {
            baseTransform2 = baseTransform2.deriveWithConcatenation(this.transform);
        }
        RectBounds rectBounds3 = rectBounds;
        DirtyRegionContainer dirtyRegionContainer2 = null;
        BaseTransform baseTransform3 = null;
        if (this.effectFilter != null) {
            try {
                rectBounds3 = new RectBounds();
                object2 = baseTransform2.inverseTransform(rectBounds, TEMP_BOUNDS);
                ((BaseBounds)object2).flattenInto(rectBounds3);
            }
            catch (NoninvertibleTransformException noninvertibleTransformException) {
                return 1;
            }
            baseTransform3 = baseTransform2;
            baseTransform2 = BaseTransform.IDENTITY_TRANSFORM;
            dirtyRegionContainer2 = dirtyRegionContainer;
            dirtyRegionContainer = dirtyRegionPool.checkOut();
        } else if (this.clipNode != null) {
            dirtyRegionContainer2 = dirtyRegionContainer;
            rectBounds3 = new RectBounds();
            object2 = this.clipNode.getCompleteBounds(rectBounds3, baseTransform2);
            generalTransform3D.transform((BaseBounds)object2, (BaseBounds)object2);
            ((BaseBounds)object2).flattenInto(rectBounds3);
            rectBounds3.intersectWith(rectBounds);
            dirtyRegionContainer = dirtyRegionPool.checkOut();
        }
        object2 = ((NGGroup)this).getRemovedChildren();
        if (object2 != null) {
            for (n = object2.size() - 1; n >= 0; --n) {
                object = (NGNode)object2.get(n);
                ((NGNode)object).dirty = DirtyFlag.DIRTY;
                n2 = ((NGNode)object).accumulateDirtyRegions(rectBounds3, rectBounds2, dirtyRegionPool, dirtyRegionContainer, baseTransform2, generalTransform3D);
                if (n2 == 0) break;
            }
        }
        object = ((NGGroup)this).getChildren();
        n = object.size();
        for (int i = 0; i < n && n2 == 1 && (n2 = (nGNode = (NGNode)object.get(i)).accumulateDirtyRegions(rectBounds3, rectBounds2, dirtyRegionPool, dirtyRegionContainer, baseTransform2, generalTransform3D)) != 0; ++i) {
        }
        if (this.effectFilter != null && n2 == 1) {
            this.applyEffect(this.effectFilter, dirtyRegionContainer, dirtyRegionPool);
            if (this.clipNode != null) {
                rectBounds3 = new RectBounds();
                BaseBounds baseBounds = this.clipNode.getCompleteBounds(rectBounds3, baseTransform2);
                this.applyClip(baseBounds, dirtyRegionContainer);
            }
            this.applyTransform(baseTransform3, dirtyRegionContainer);
            baseTransform2 = baseTransform3;
            dirtyRegionContainer2.merge(dirtyRegionContainer);
            dirtyRegionPool.checkIn(dirtyRegionContainer);
        }
        if (baseTransform2 == baseTransform) {
            baseTransform.restoreTransform(d, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12);
        }
        if (this.clipNode != null && this.effectFilter == null) {
            if (n2 == 0) {
                n2 = this.accumulateNodeDirtyRegion(rectBounds, rectBounds2, dirtyRegionContainer2, baseTransform, generalTransform3D);
            } else {
                dirtyRegionContainer2.merge(dirtyRegionContainer);
            }
            dirtyRegionPool.checkIn(dirtyRegionContainer);
        }
        return n2;
    }

    private BaseBounds computeDirtyRegion(RectBounds rectBounds, BaseTransform baseTransform, GeneralTransform3D generalTransform3D) {
        if (this.cacheFilter != null) {
            return this.cacheFilter.computeDirtyBounds(rectBounds, baseTransform, generalTransform3D);
        }
        BaseBounds baseBounds = rectBounds;
        baseBounds = !this.dirtyBounds.isEmpty() ? baseBounds.deriveWithNewBounds(this.dirtyBounds) : baseBounds.deriveWithNewBounds(this.transformedBounds);
        if (!baseBounds.isEmpty()) {
            baseBounds = this.computePadding(baseBounds);
            baseBounds = baseTransform.transform(baseBounds, baseBounds);
            baseBounds = generalTransform3D.transform(baseBounds, baseBounds);
        }
        return baseBounds;
    }

    protected BaseBounds computePadding(BaseBounds baseBounds) {
        return baseBounds;
    }

    protected boolean hasVisuals() {
        return true;
    }

    public final void doPreCulling(DirtyRegionContainer dirtyRegionContainer, BaseTransform baseTransform, GeneralTransform3D generalTransform3D) {
        if (dirtyRegionContainer == null || baseTransform == null || generalTransform3D == null) {
            throw new NullPointerException();
        }
        this.markCullRegions(dirtyRegionContainer, -1, baseTransform, generalTransform3D);
    }

    void markCullRegions(DirtyRegionContainer dirtyRegionContainer, int n, BaseTransform baseTransform, GeneralTransform3D generalTransform3D) {
        RectBounds rectBounds;
        if (baseTransform.isIdentity()) {
            TEMP_BOUNDS.deriveWithNewBounds(this.transformedBounds);
        } else {
            baseTransform.transform(this.transformedBounds, TEMP_BOUNDS);
        }
        if (!generalTransform3D.isIdentity()) {
            generalTransform3D.transform(TEMP_BOUNDS, TEMP_BOUNDS);
        }
        TEMP_BOUNDS.flattenInto(TEMP_RECT_BOUNDS);
        this.cullingBits = 0;
        int n2 = 1;
        for (int i = 0; i < dirtyRegionContainer.size() && (rectBounds = dirtyRegionContainer.getDirtyRegion(i)) != null && !rectBounds.isEmpty(); ++i) {
            if ((n == -1 || (n & n2) != 0) && rectBounds.intersects(TEMP_RECT_BOUNDS)) {
                int n3 = 1;
                if (rectBounds.contains(TEMP_RECT_BOUNDS)) {
                    n3 = 2;
                }
                this.cullingBits |= n3 << 2 * i;
            }
            n2 <<= 2;
        }
        if (this.cullingBits == 0 && (this.dirty != DirtyFlag.CLEAN || this.childDirty)) {
            this.clearDirtyTree();
        }
    }

    public final void printDirtyOpts(StringBuilder stringBuilder, List<NGNode> list) {
        stringBuilder.append("\n*=Render Root\n");
        stringBuilder.append("d=Dirty\n");
        stringBuilder.append("dt=Dirty By Translation\n");
        stringBuilder.append("i=Dirty Region Intersects the NGNode\n");
        stringBuilder.append("c=Dirty Region Contains the NGNode\n");
        stringBuilder.append("ef=Effect Filter\n");
        stringBuilder.append("cf=Cache Filter\n");
        stringBuilder.append("cl=This node is a clip node\n");
        stringBuilder.append("b=Blend mode is set\n");
        stringBuilder.append("or=Opaque Region\n");
        this.printDirtyOpts(stringBuilder, this, BaseTransform.IDENTITY_TRANSFORM, "", list);
    }

    private final void printDirtyOpts(StringBuilder stringBuilder, NGNode nGNode, BaseTransform baseTransform, String string, List<NGNode> list) {
        RectBounds rectBounds;
        int n;
        int n2;
        if (!nGNode.isVisible() || nGNode.getOpacity() == 0.0f) {
            return;
        }
        BaseTransform baseTransform2 = baseTransform.copy();
        baseTransform2 = baseTransform2.deriveWithConcatenation(nGNode.getTransform());
        ArrayList<Object> arrayList = new ArrayList<Object>();
        for (n2 = 0; n2 < list.size(); ++n2) {
            NGNode nGNode2 = list.get(n2);
            if (nGNode != nGNode2) continue;
            arrayList.add("*" + n2);
        }
        if (nGNode.dirty != DirtyFlag.CLEAN) {
            arrayList.add(nGNode.dirty == DirtyFlag.DIRTY ? "d" : "dt");
        }
        if (nGNode.cullingBits != 0) {
            n2 = 17;
            for (int i = 0; i < 15; ++i) {
                n = nGNode.cullingBits & n2;
                if (n != 0) {
                    arrayList.add(n == 1 ? "i" + i : (n == 0 ? "c" + i : "ci" + i));
                }
                n2 <<= 2;
            }
        }
        if (nGNode.effectFilter != null) {
            arrayList.add("ef");
        }
        if (nGNode.cacheFilter != null) {
            arrayList.add("cf");
        }
        if (nGNode.nodeBlendMode != null) {
            arrayList.add("b");
        }
        if ((rectBounds = nGNode.getOpaqueRegion()) != null) {
            RectBounds rectBounds2 = new RectBounds();
            baseTransform2.transform(rectBounds, rectBounds2);
            arrayList.add("or=" + rectBounds2.getMinX() + ", " + rectBounds2.getMinY() + ", " + rectBounds2.getWidth() + ", " + rectBounds2.getHeight());
        }
        if (arrayList.isEmpty()) {
            stringBuilder.append(string + nGNode.name + "\n");
        } else {
            Object object = " [";
            for (n = 0; n < arrayList.size(); ++n) {
                object = (String)object + (String)arrayList.get(n);
                if (n >= arrayList.size() - 1) continue;
                object = (String)object + " ";
            }
            stringBuilder.append(string + nGNode.name + (String)object + "]\n");
        }
        if (nGNode.getClipNode() != null) {
            this.printDirtyOpts(stringBuilder, nGNode.getClipNode(), baseTransform2, string + "  cl ", list);
        }
        if (nGNode instanceof NGGroup) {
            NGGroup nGGroup = (NGGroup)nGNode;
            for (n = 0; n < nGGroup.getChildren().size(); ++n) {
                this.printDirtyOpts(stringBuilder, nGGroup.getChildren().get(n), baseTransform2, string + "  ", list);
            }
        }
    }

    public void drawDirtyOpts(BaseTransform baseTransform, GeneralTransform3D generalTransform3D, Rectangle rectangle, int[] arrn, int n) {
        if ((this.painted & 1 << n * 2) != 0) {
            baseTransform.copy().deriveWithConcatenation(this.getTransform()).transform(this.contentBounds, TEMP_BOUNDS);
            if (generalTransform3D != null) {
                generalTransform3D.transform(TEMP_BOUNDS, TEMP_BOUNDS);
            }
            RectBounds rectBounds = new RectBounds();
            TEMP_BOUNDS.flattenInto(rectBounds);
            assert (rectangle.width * rectangle.height == arrn.length);
            rectBounds.intersectWith(rectangle);
            int n2 = (int)rectBounds.getMinX() - rectangle.x;
            int n3 = (int)rectBounds.getMinY() - rectangle.y;
            int n4 = (int)((double)rectBounds.getWidth() + 0.5);
            int n5 = (int)((double)rectBounds.getHeight() + 0.5);
            if (n4 == 0 || n5 == 0) {
                return;
            }
            for (int i = n3; i < n3 + n5; ++i) {
                for (int j = n2; j < n2 + n4; ++j) {
                    int n6 = i * rectangle.width + j;
                    int n7 = arrn[n6];
                    if (n7 == 0) {
                        n7 = 134250240;
                    } else if ((this.painted & 3 << n * 2) == 3) {
                        switch (n7) {
                            case -2147451136: {
                                n7 = -2147450880;
                                break;
                            }
                            case -2147450880: {
                                n7 = -2139128064;
                                break;
                            }
                            case -2139128064: {
                                n7 = -2139062272;
                                break;
                            }
                            case -2139062272: {
                                n7 = -2139160576;
                                break;
                            }
                            default: {
                                n7 = -2139095040;
                            }
                        }
                    }
                    arrn[n6] = n7;
                }
            }
        }
    }

    public final void getRenderRoot(NodePath nodePath, RectBounds rectBounds, int n, BaseTransform baseTransform, GeneralTransform3D generalTransform3D) {
        if (nodePath == null || rectBounds == null || baseTransform == null || generalTransform3D == null) {
            throw new NullPointerException();
        }
        if (n < -1 || n > 15) {
            throw new IllegalArgumentException("cullingIndex cannot be < -1 or > 15");
        }
        RenderRootResult renderRootResult = this.computeRenderRoot(nodePath, rectBounds, n, baseTransform, generalTransform3D);
        if (renderRootResult == RenderRootResult.NO_RENDER_ROOT) {
            nodePath.add(this);
        } else if (renderRootResult == RenderRootResult.HAS_RENDER_ROOT_AND_IS_CLEAN) {
            nodePath.clear();
        }
    }

    RenderRootResult computeRenderRoot(NodePath nodePath, RectBounds rectBounds, int n, BaseTransform baseTransform, GeneralTransform3D generalTransform3D) {
        return this.computeNodeRenderRoot(nodePath, rectBounds, n, baseTransform, generalTransform3D);
    }

    private static int ccw(double d, double d2, Point2D point2D, Point2D point2D2) {
        return (int)Math.signum((double)(point2D2.x - point2D.x) * (d2 - (double)point2D.y) - (double)(point2D2.y - point2D.y) * (d - (double)point2D.x));
    }

    private static boolean pointInConvexQuad(double d, double d2, Point2D[] arrpoint2D) {
        int n;
        int n2 = NGNode.ccw(d, d2, arrpoint2D[0], arrpoint2D[1]);
        int n3 = NGNode.ccw(d, d2, arrpoint2D[1], arrpoint2D[2]);
        int n4 = NGNode.ccw(d, d2, arrpoint2D[2], arrpoint2D[3]);
        int n5 = NGNode.ccw(d, d2, arrpoint2D[3], arrpoint2D[0]);
        return (n = (n2 ^= n2 >>> 1) | (n3 ^= n3 >>> 1) | (n4 ^= n4 >>> 1) | (n5 ^= n5 >>> 1)) == Integer.MIN_VALUE || n == 1;
    }

    final RenderRootResult computeNodeRenderRoot(NodePath nodePath, RectBounds rectBounds, int n, BaseTransform baseTransform, GeneralTransform3D generalTransform3D) {
        int n2;
        if (n != -1 && ((n2 = this.cullingBits >> n * 2) & 3) == 0) {
            return RenderRootResult.NO_RENDER_ROOT;
        }
        if (!this.isVisible()) {
            return RenderRootResult.NO_RENDER_ROOT;
        }
        RectBounds rectBounds2 = this.getOpaqueRegion();
        if (rectBounds2 == null) {
            return RenderRootResult.NO_RENDER_ROOT;
        }
        BaseTransform baseTransform2 = this.getTransform();
        Affine3D affine3D = TEMP_TRANSFORM.deriveWithNewTransform(baseTransform).deriveWithConcatenation(baseTransform2);
        if (NGNode.checkBoundsInQuad(rectBounds2, rectBounds, affine3D, generalTransform3D)) {
            nodePath.add(this);
            return this.isClean() ? RenderRootResult.HAS_RENDER_ROOT_AND_IS_CLEAN : RenderRootResult.HAS_RENDER_ROOT;
        }
        return RenderRootResult.NO_RENDER_ROOT;
    }

    static boolean checkBoundsInQuad(RectBounds rectBounds, RectBounds rectBounds2, BaseTransform baseTransform, GeneralTransform3D generalTransform3D) {
        if (generalTransform3D.isIdentity() && (baseTransform.getType() & 0xFFFFFFF0) == 0) {
            if (baseTransform.isIdentity()) {
                TEMP_BOUNDS.deriveWithNewBounds(rectBounds);
            } else {
                baseTransform.transform(rectBounds, TEMP_BOUNDS);
            }
            TEMP_BOUNDS.flattenInto(TEMP_RECT_BOUNDS);
            return TEMP_RECT_BOUNDS.contains(rectBounds2);
        }
        TEMP_POINTS2D_4[0].setLocation(rectBounds.getMinX(), rectBounds.getMinY());
        TEMP_POINTS2D_4[1].setLocation(rectBounds.getMaxX(), rectBounds.getMinY());
        TEMP_POINTS2D_4[2].setLocation(rectBounds.getMaxX(), rectBounds.getMaxY());
        TEMP_POINTS2D_4[3].setLocation(rectBounds.getMinX(), rectBounds.getMaxY());
        for (Point2D point2D : TEMP_POINTS2D_4) {
            baseTransform.transform(point2D, point2D);
            if (generalTransform3D.isIdentity()) continue;
            generalTransform3D.transform(point2D, point2D);
        }
        return NGNode.pointInConvexQuad(rectBounds2.getMinX(), rectBounds2.getMinY(), TEMP_POINTS2D_4) && NGNode.pointInConvexQuad(rectBounds2.getMaxX(), rectBounds2.getMinY(), TEMP_POINTS2D_4) && NGNode.pointInConvexQuad(rectBounds2.getMaxX(), rectBounds2.getMaxY(), TEMP_POINTS2D_4) && NGNode.pointInConvexQuad(rectBounds2.getMinX(), rectBounds2.getMaxY(), TEMP_POINTS2D_4);
    }

    protected final void invalidateOpaqueRegion() {
        this.opaqueRegionInvalid = true;
        if (this.isClip) {
            this.parent.invalidateOpaqueRegion();
        }
    }

    final boolean isOpaqueRegionInvalid() {
        return this.opaqueRegionInvalid;
    }

    public final RectBounds getOpaqueRegion() {
        if (this.opaqueRegionInvalid || this.getEffect() != null) {
            this.opaqueRegionInvalid = false;
            if (this.supportsOpaqueRegions() && this.hasOpaqueRegion()) {
                this.opaqueRegion = this.computeOpaqueRegion(this.opaqueRegion == null ? new RectBounds() : this.opaqueRegion);
                assert (this.opaqueRegion != null);
                if (this.opaqueRegion == null) {
                    return null;
                }
                NGNode nGNode = this.getClipNode();
                if (nGNode != null) {
                    RectBounds rectBounds = nGNode.getOpaqueRegion();
                    if (rectBounds == null || (nGNode.getTransform().getType() & 0xFFFFFFF8) != 0) {
                        this.opaqueRegion = null;
                        return null;
                    }
                    BaseBounds baseBounds = nGNode.getTransform().transform(rectBounds, TEMP_BOUNDS);
                    baseBounds.flattenInto(TEMP_RECT_BOUNDS);
                    this.opaqueRegion.intersectWith(TEMP_RECT_BOUNDS);
                }
            } else {
                this.opaqueRegion = null;
            }
        }
        return this.opaqueRegion;
    }

    protected boolean supportsOpaqueRegions() {
        return false;
    }

    protected boolean hasOpaqueRegion() {
        NGNode nGNode = this.getClipNode();
        Effect effect = this.getEffect();
        return !(effect != null && effect.reducesOpaquePixels() || this.getOpacity() != 1.0f || this.nodeBlendMode != null && this.nodeBlendMode != Blend.Mode.SRC_OVER || nGNode != null && (!nGNode.supportsOpaqueRegions() || !nGNode.hasOpaqueRegion()));
    }

    protected RectBounds computeOpaqueRegion(RectBounds rectBounds) {
        return null;
    }

    protected boolean isRectClip(BaseTransform baseTransform, boolean bl) {
        return false;
    }

    public final void render(Graphics graphics) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.incrementCounter((String)"Nodes visited during render");
        }
        this.clearDirty();
        if (!this.visible || this.opacity == 0.0f) {
            return;
        }
        this.doRender(graphics);
    }

    public void renderForcedContent(Graphics graphics) {
    }

    boolean isShape3D() {
        return false;
    }

    protected void doRender(Graphics graphics) {
        int n;
        graphics.setState3D(this.isShape3D());
        boolean bl = false;
        if (PrismSettings.dirtyOptsEnabled && graphics.hasPreCullingBits()) {
            n = this.cullingBits >> graphics.getClipRectIndex() * 2;
            if ((n & 3) == 0) {
                return;
            }
            if ((n & 2) != 0) {
                graphics.setHasPreCullingBits(false);
                bl = true;
            }
        }
        n = graphics.isDepthTest();
        graphics.setDepthTest(this.isDepthTest());
        BaseTransform baseTransform = graphics.getTransformNoClone();
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
        graphics.transform(this.getTransform());
        boolean bl2 = false;
        if (!this.isShape3D() && graphics instanceof ReadbackGraphics && this.needsBlending()) {
            this.renderNodeBlendMode(graphics);
            bl2 = true;
        } else if (!this.isShape3D() && this.getOpacity() < 1.0f) {
            this.renderOpacity(graphics);
            bl2 = true;
        } else if (!this.isShape3D() && this.getCacheFilter() != null) {
            this.renderCached(graphics);
            bl2 = true;
        } else if (!this.isShape3D() && this.getClipNode() != null) {
            this.renderClip(graphics);
            bl2 = true;
        } else if (!this.isShape3D() && this.getEffectFilter() != null && effectsSupported.booleanValue()) {
            this.renderEffect(graphics);
            bl2 = true;
        } else {
            this.renderContent(graphics);
            if (PrismSettings.showOverdraw) {
                boolean bl3 = bl2 = this instanceof NGRegion || !(this instanceof NGGroup);
            }
        }
        if (bl) {
            graphics.setHasPreCullingBits(true);
        }
        graphics.setTransform3D(d, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12);
        graphics.setDepthTest(n != 0);
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.incrementCounter((String)"Nodes rendered");
        }
        if (PrismSettings.showOverdraw) {
            this.painted = bl2 ? (this.painted |= 3 << graphics.getClipRectIndex() * 2) : (this.painted |= 1 << graphics.getClipRectIndex() * 2);
        }
    }

    protected boolean needsBlending() {
        Blend.Mode mode = this.getNodeBlendMode();
        return mode != null && mode != Blend.Mode.SRC_OVER;
    }

    private void renderNodeBlendMode(Graphics graphics) {
        BaseTransform baseTransform = graphics.getTransformNoClone();
        BaseBounds baseBounds = this.getClippedBounds(new RectBounds(), baseTransform);
        if (baseBounds.isEmpty()) {
            this.clearDirtyTree();
            return;
        }
        if (!this.isReadbackSupported(graphics)) {
            if (this.getOpacity() < 1.0f) {
                this.renderOpacity(graphics);
            } else if (this.getClipNode() != null) {
                this.renderClip(graphics);
            } else {
                this.renderContent(graphics);
            }
            return;
        }
        Rectangle rectangle = new Rectangle(baseBounds);
        rectangle.intersectWith(PrEffectHelper.getGraphicsClipNoClone(graphics));
        FilterContext filterContext = NGNode.getFilterContext(graphics);
        PrDrawable prDrawable = (PrDrawable)Effect.getCompatibleImage(filterContext, rectangle.width, rectangle.height);
        if (prDrawable == null) {
            this.clearDirtyTree();
            return;
        }
        Graphics graphics2 = prDrawable.createGraphics();
        graphics2.setHasPreCullingBits(graphics.hasPreCullingBits());
        graphics2.setClipRectIndex(graphics.getClipRectIndex());
        graphics2.translate(-rectangle.x, -rectangle.y);
        graphics2.transform(baseTransform);
        if (this.getOpacity() < 1.0f) {
            this.renderOpacity(graphics2);
        } else if (this.getCacheFilter() != null) {
            this.renderCached(graphics2);
        } else if (this.getClipNode() != null) {
            this.renderClip(graphics);
        } else if (this.getEffectFilter() != null) {
            this.renderEffect(graphics2);
        } else {
            this.renderContent(graphics2);
        }
        RTTexture rTTexture = ((ReadbackGraphics)graphics).readBack(rectangle);
        PrDrawable prDrawable2 = PrDrawable.create(filterContext, rTTexture);
        Blend blend = new Blend(this.getNodeBlendMode(), new PassThrough(prDrawable2, rectangle), new PassThrough(prDrawable, rectangle));
        CompositeMode compositeMode = graphics.getCompositeMode();
        graphics.setTransform(null);
        graphics.setCompositeMode(CompositeMode.SRC);
        PrEffectHelper.render(blend, graphics, 0.0f, 0.0f, null);
        graphics.setCompositeMode(compositeMode);
        Effect.releaseCompatibleImage(filterContext, prDrawable);
        ((ReadbackGraphics)graphics).releaseReadBackBuffer(rTTexture);
    }

    private void renderRectClip(Graphics graphics, NGRectangle nGRectangle) {
        BaseBounds baseBounds = nGRectangle.getShape().getBounds();
        if (!nGRectangle.getTransform().isIdentity()) {
            baseBounds = nGRectangle.getTransform().transform(baseBounds, baseBounds);
        }
        BaseTransform baseTransform = graphics.getTransformNoClone();
        Rectangle rectangle = graphics.getClipRectNoClone();
        baseBounds = baseTransform.transform(baseBounds, baseBounds);
        baseBounds.intersectWith(PrEffectHelper.getGraphicsClipNoClone(graphics));
        if (baseBounds.isEmpty() || baseBounds.getWidth() == 0.0f || baseBounds.getHeight() == 0.0f) {
            this.clearDirtyTree();
            return;
        }
        graphics.setClipRect(new Rectangle(baseBounds));
        this.renderForClip(graphics);
        graphics.setClipRect(rectangle);
        nGRectangle.clearDirty();
    }

    void renderClip(Graphics graphics) {
        Object object;
        if ((double)this.getClipNode().getOpacity() == 0.0) {
            this.clearDirtyTree();
            return;
        }
        BaseTransform baseTransform = graphics.getTransformNoClone();
        BaseBounds baseBounds = this.getClippedBounds(new RectBounds(), baseTransform);
        if (baseBounds.isEmpty()) {
            this.clearDirtyTree();
            return;
        }
        if (this.getClipNode() instanceof NGRectangle && ((NGRectangle)(object = (NGRectangle)this.getClipNode())).isRectClip(baseTransform, false)) {
            this.renderRectClip(graphics, (NGRectangle)object);
            return;
        }
        object = new Rectangle(baseBounds);
        ((Rectangle)object).intersectWith(PrEffectHelper.getGraphicsClipNoClone(graphics));
        if (!baseTransform.is2D()) {
            Rectangle rectangle = graphics.getClipRect();
            graphics.setClipRect((Rectangle)object);
            NodeEffectInput nodeEffectInput = new NodeEffectInput(this.getClipNode(), NodeEffectInput.RenderType.FULL_CONTENT);
            NodeEffectInput nodeEffectInput2 = new NodeEffectInput(this, NodeEffectInput.RenderType.CLIPPED_CONTENT);
            Blend blend = new Blend(Blend.Mode.SRC_IN, nodeEffectInput, nodeEffectInput2);
            PrEffectHelper.render(blend, graphics, 0.0f, 0.0f, null);
            nodeEffectInput.flush();
            nodeEffectInput2.flush();
            graphics.setClipRect(rectangle);
            this.clearDirtyTree();
            return;
        }
        FilterContext filterContext = NGNode.getFilterContext(graphics);
        PrDrawable prDrawable = (PrDrawable)Effect.getCompatibleImage(filterContext, ((Rectangle)object).width, ((Rectangle)object).height);
        if (prDrawable == null) {
            this.clearDirtyTree();
            return;
        }
        Graphics graphics2 = prDrawable.createGraphics();
        graphics2.setExtraAlpha(graphics.getExtraAlpha());
        graphics2.setHasPreCullingBits(graphics.hasPreCullingBits());
        graphics2.setClipRectIndex(graphics.getClipRectIndex());
        graphics2.translate(-((Rectangle)object).x, -((Rectangle)object).y);
        graphics2.transform(baseTransform);
        this.renderForClip(graphics2);
        PrDrawable prDrawable2 = (PrDrawable)Effect.getCompatibleImage(filterContext, ((Rectangle)object).width, ((Rectangle)object).height);
        if (prDrawable2 == null) {
            this.getClipNode().clearDirtyTree();
            Effect.releaseCompatibleImage(filterContext, prDrawable);
            return;
        }
        Graphics graphics3 = prDrawable2.createGraphics();
        graphics3.translate(-((Rectangle)object).x, -((Rectangle)object).y);
        graphics3.transform(baseTransform);
        this.getClipNode().render(graphics3);
        graphics.setTransform(null);
        Blend blend = new Blend(Blend.Mode.SRC_IN, new PassThrough(prDrawable2, (Rectangle)object), new PassThrough(prDrawable, (Rectangle)object));
        PrEffectHelper.render(blend, graphics, 0.0f, 0.0f, null);
        Effect.releaseCompatibleImage(filterContext, prDrawable);
        Effect.releaseCompatibleImage(filterContext, prDrawable2);
    }

    void renderForClip(Graphics graphics) {
        if (this.getEffectFilter() != null) {
            this.renderEffect(graphics);
        } else {
            this.renderContent(graphics);
        }
    }

    private void renderOpacity(Graphics graphics) {
        if (this.getEffectFilter() != null || this.getCacheFilter() != null || this.getClipNode() != null || !this.hasOverlappingContents()) {
            float f = graphics.getExtraAlpha();
            graphics.setExtraAlpha(f * this.getOpacity());
            if (this.getCacheFilter() != null) {
                this.renderCached(graphics);
            } else if (this.getClipNode() != null) {
                this.renderClip(graphics);
            } else if (this.getEffectFilter() != null) {
                this.renderEffect(graphics);
            } else {
                this.renderContent(graphics);
            }
            graphics.setExtraAlpha(f);
            return;
        }
        FilterContext filterContext = NGNode.getFilterContext(graphics);
        BaseTransform baseTransform = graphics.getTransformNoClone();
        BaseBounds baseBounds = this.getContentBounds(new RectBounds(), baseTransform);
        Rectangle rectangle = new Rectangle(baseBounds);
        rectangle.intersectWith(PrEffectHelper.getGraphicsClipNoClone(graphics));
        PrDrawable prDrawable = (PrDrawable)Effect.getCompatibleImage(filterContext, rectangle.width, rectangle.height);
        if (prDrawable == null) {
            return;
        }
        Graphics graphics2 = prDrawable.createGraphics();
        graphics2.setHasPreCullingBits(graphics.hasPreCullingBits());
        graphics2.setClipRectIndex(graphics.getClipRectIndex());
        graphics2.translate(-rectangle.x, -rectangle.y);
        graphics2.transform(baseTransform);
        this.renderContent(graphics2);
        graphics.setTransform(null);
        float f = graphics.getExtraAlpha();
        graphics.setExtraAlpha(this.getOpacity() * f);
        graphics.drawTexture((Texture)prDrawable.getTextureObject(), rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        graphics.setExtraAlpha(f);
        Effect.releaseCompatibleImage(filterContext, prDrawable);
    }

    private void renderCached(Graphics graphics) {
        if (this.isContentBounds2D() && graphics.getTransformNoClone().is2D() && !(graphics instanceof PrinterGraphics)) {
            this.getCacheFilter().render(graphics);
        } else {
            this.renderContent(graphics);
        }
    }

    protected void renderEffect(Graphics graphics) {
        this.getEffectFilter().render(graphics);
    }

    protected abstract void renderContent(Graphics var1);

    protected abstract boolean hasOverlappingContents();

    boolean isReadbackSupported(Graphics graphics) {
        return graphics instanceof ReadbackGraphics && ((ReadbackGraphics)graphics).canReadBack();
    }

    static FilterContext getFilterContext(Graphics graphics) {
        Screen screen = graphics.getAssociatedScreen();
        if (screen == null) {
            return PrFilterContext.getPrinterContext(graphics.getResourceFactory());
        }
        return PrFilterContext.getInstance(screen);
    }

    public void release() {
    }

    public String toString() {
        return this.name == null ? super.toString() : this.name;
    }

    public void applyTransform(BaseTransform baseTransform, DirtyRegionContainer dirtyRegionContainer) {
        for (int i = 0; i < dirtyRegionContainer.size(); ++i) {
            dirtyRegionContainer.setDirtyRegion(i, (RectBounds)baseTransform.transform(dirtyRegionContainer.getDirtyRegion(i), dirtyRegionContainer.getDirtyRegion(i)));
            if (!dirtyRegionContainer.checkAndClearRegion(i)) continue;
            --i;
        }
    }

    public void applyClip(BaseBounds baseBounds, DirtyRegionContainer dirtyRegionContainer) {
        for (int i = 0; i < dirtyRegionContainer.size(); ++i) {
            dirtyRegionContainer.getDirtyRegion(i).intersectWith(baseBounds);
            if (!dirtyRegionContainer.checkAndClearRegion(i)) continue;
            --i;
        }
    }

    public void applyEffect(EffectFilter effectFilter, DirtyRegionContainer dirtyRegionContainer, DirtyRegionPool dirtyRegionPool) {
        Effect effect = effectFilter.getEffect();
        EffectDirtyBoundsHelper effectDirtyBoundsHelper = EffectDirtyBoundsHelper.getInstance();
        effectDirtyBoundsHelper.setInputBounds(this.contentBounds);
        effectDirtyBoundsHelper.setDirtyRegions(dirtyRegionContainer);
        DirtyRegionContainer dirtyRegionContainer2 = effect.getDirtyRegions(effectDirtyBoundsHelper, dirtyRegionPool);
        dirtyRegionContainer.deriveWithNewContainer(dirtyRegionContainer2);
        dirtyRegionPool.checkIn(dirtyRegionContainer2);
    }

    public static final class DirtyFlag
    extends Enum<DirtyFlag> {
        public static final /* enum */ DirtyFlag CLEAN = new DirtyFlag();
        public static final /* enum */ DirtyFlag DIRTY_BY_TRANSLATION = new DirtyFlag();
        public static final /* enum */ DirtyFlag DIRTY = new DirtyFlag();
        private static final /* synthetic */ DirtyFlag[] $VALUES;

        public static DirtyFlag[] values() {
            return (DirtyFlag[])$VALUES.clone();
        }

        public static DirtyFlag valueOf(String string) {
            return Enum.valueOf(DirtyFlag.class, string);
        }

        private static /* synthetic */ DirtyFlag[] $values() {
            return new DirtyFlag[]{CLEAN, DIRTY_BY_TRANSLATION, DIRTY};
        }

        static {
            $VALUES = DirtyFlag.$values();
        }
    }

    protected static final class RenderRootResult
    extends Enum<RenderRootResult> {
        public static final /* enum */ RenderRootResult NO_RENDER_ROOT = new RenderRootResult();
        public static final /* enum */ RenderRootResult HAS_RENDER_ROOT = new RenderRootResult();
        public static final /* enum */ RenderRootResult HAS_RENDER_ROOT_AND_IS_CLEAN = new RenderRootResult();
        private static final /* synthetic */ RenderRootResult[] $VALUES;

        public static RenderRootResult[] values() {
            return (RenderRootResult[])$VALUES.clone();
        }

        public static RenderRootResult valueOf(String string) {
            return Enum.valueOf(RenderRootResult.class, string);
        }

        private static /* synthetic */ RenderRootResult[] $values() {
            return new RenderRootResult[]{NO_RENDER_ROOT, HAS_RENDER_ROOT, HAS_RENDER_ROOT_AND_IS_CLEAN};
        }

        static {
            $VALUES = RenderRootResult.$values();
        }
    }

    private static class PassThrough
    extends Effect {
        private PrDrawable img;
        private Rectangle bounds;

        PassThrough(PrDrawable prDrawable, Rectangle rectangle) {
            this.img = prDrawable;
            this.bounds = rectangle;
        }

        @Override
        public ImageData filter(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, Object object, Effect effect) {
            this.img.lock();
            ImageData imageData = new ImageData(filterContext, this.img, new Rectangle(this.bounds));
            imageData.setReusable(true);
            return imageData;
        }

        @Override
        public RectBounds getBounds(BaseTransform baseTransform, Effect effect) {
            return new RectBounds(this.bounds);
        }

        @Override
        public Effect.AccelType getAccelType(FilterContext filterContext) {
            return Effect.AccelType.INTRINSIC;
        }

        @Override
        public boolean reducesOpaquePixels() {
            return false;
        }

        @Override
        public DirtyRegionContainer getDirtyRegions(Effect effect, DirtyRegionPool dirtyRegionPool) {
            return null;
        }
    }

    private static class EffectDirtyBoundsHelper
    extends Effect {
        private BaseBounds bounds;
        private static EffectDirtyBoundsHelper instance = null;
        private DirtyRegionContainer drc;

        private EffectDirtyBoundsHelper() {
        }

        public void setInputBounds(BaseBounds baseBounds) {
            this.bounds = baseBounds;
        }

        @Override
        public ImageData filter(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, Object object, Effect effect) {
            throw new UnsupportedOperationException();
        }

        @Override
        public BaseBounds getBounds(BaseTransform baseTransform, Effect effect) {
            if (this.bounds.getBoundsType() == BaseBounds.BoundsType.RECTANGLE) {
                return this.bounds;
            }
            return new RectBounds(this.bounds.getMinX(), this.bounds.getMinY(), this.bounds.getMaxX(), this.bounds.getMaxY());
        }

        @Override
        public Effect.AccelType getAccelType(FilterContext filterContext) {
            return null;
        }

        public static EffectDirtyBoundsHelper getInstance() {
            if (instance == null) {
                instance = new EffectDirtyBoundsHelper();
            }
            return instance;
        }

        @Override
        public boolean reducesOpaquePixels() {
            return true;
        }

        private void setDirtyRegions(DirtyRegionContainer dirtyRegionContainer) {
            this.drc = dirtyRegionContainer;
        }

        @Override
        public DirtyRegionContainer getDirtyRegions(Effect effect, DirtyRegionPool dirtyRegionPool) {
            DirtyRegionContainer dirtyRegionContainer = dirtyRegionPool.checkOut();
            dirtyRegionContainer.deriveWithNewContainer(this.drc);
            return dirtyRegionContainer;
        }
    }
}

