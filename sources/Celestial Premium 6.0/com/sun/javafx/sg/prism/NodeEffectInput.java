/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.prism.Graphics;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.prism.PrRenderInfo;

public final class NodeEffectInput
extends Effect {
    private NGNode node;
    private RenderType renderType;
    private BaseBounds tempBounds = new RectBounds();
    private ImageData cachedIdentityImageData;
    private ImageData cachedTransformedImageData;
    private BaseTransform cachedTransform;

    public NodeEffectInput(NGNode nGNode) {
        this(nGNode, RenderType.EFFECT_CONTENT);
    }

    public NodeEffectInput(NGNode nGNode, RenderType renderType) {
        this.node = nGNode;
        this.renderType = renderType;
    }

    public NGNode getNode() {
        return this.node;
    }

    public void setNode(NGNode nGNode) {
        if (this.node != nGNode) {
            this.node = nGNode;
            this.flush();
        }
    }

    static boolean contains(ImageData imageData, Rectangle rectangle) {
        Rectangle rectangle2 = imageData.getUntransformedBounds();
        return rectangle2.contains(rectangle);
    }

    @Override
    public BaseBounds getBounds(BaseTransform baseTransform, Effect effect) {
        BaseTransform baseTransform2 = baseTransform == null ? BaseTransform.IDENTITY_TRANSFORM : baseTransform;
        this.tempBounds = this.node.getContentBounds(this.tempBounds, baseTransform2);
        return this.tempBounds.copy();
    }

    @Override
    public ImageData filter(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, Object object, Effect effect) {
        Object object2;
        if (object instanceof PrRenderInfo && (object2 = ((PrRenderInfo)object).getGraphics()) != null) {
            this.render((Graphics)object2, baseTransform);
            return null;
        }
        object2 = NodeEffectInput.getImageBoundsForNode(this.node, this.renderType, baseTransform, rectangle);
        if (baseTransform.isIdentity()) {
            if (this.cachedIdentityImageData != null && NodeEffectInput.contains(this.cachedIdentityImageData, (Rectangle)object2) && this.cachedIdentityImageData.validate(filterContext)) {
                this.cachedIdentityImageData.addref();
                return this.cachedIdentityImageData;
            }
        } else if (this.cachedTransformedImageData != null && NodeEffectInput.contains(this.cachedTransformedImageData, (Rectangle)object2) && this.cachedTransformedImageData.validate(filterContext) && this.cachedTransform.equals(baseTransform)) {
            this.cachedTransformedImageData.addref();
            return this.cachedTransformedImageData;
        }
        ImageData imageData = NodeEffectInput.getImageDataForBoundedNode(filterContext, this.node, this.renderType, baseTransform, (Rectangle)object2);
        if (baseTransform.isIdentity()) {
            this.flushIdentityImage();
            this.cachedIdentityImageData = imageData;
            this.cachedIdentityImageData.addref();
        } else {
            this.flushTransformedImage();
            this.cachedTransform = baseTransform.copy();
            this.cachedTransformedImageData = imageData;
            this.cachedTransformedImageData.addref();
        }
        return imageData;
    }

    @Override
    public Effect.AccelType getAccelType(FilterContext filterContext) {
        return Effect.AccelType.INTRINSIC;
    }

    public void flushIdentityImage() {
        if (this.cachedIdentityImageData != null) {
            this.cachedIdentityImageData.unref();
            this.cachedIdentityImageData = null;
        }
    }

    public void flushTransformedImage() {
        if (this.cachedTransformedImageData != null) {
            this.cachedTransformedImageData.unref();
            this.cachedTransformedImageData = null;
        }
        this.cachedTransform = null;
    }

    public void flush() {
        this.flushIdentityImage();
        this.flushTransformedImage();
    }

    public void render(Graphics graphics, BaseTransform baseTransform) {
        BaseTransform baseTransform2 = null;
        if (!baseTransform.isIdentity()) {
            baseTransform2 = graphics.getTransformNoClone().copy();
            graphics.transform(baseTransform);
        }
        this.node.renderContent(graphics);
        if (baseTransform2 != null) {
            graphics.setTransform(baseTransform2);
        }
    }

    static ImageData getImageDataForNode(FilterContext filterContext, NGNode nGNode, boolean bl, BaseTransform baseTransform, Rectangle rectangle) {
        RenderType renderType = bl ? RenderType.EFFECT_CONTENT : RenderType.FULL_CONTENT;
        Rectangle rectangle2 = NodeEffectInput.getImageBoundsForNode(nGNode, renderType, baseTransform, rectangle);
        return NodeEffectInput.getImageDataForBoundedNode(filterContext, nGNode, renderType, baseTransform, rectangle2);
    }

    static Rectangle getImageBoundsForNode(NGNode nGNode, RenderType renderType, BaseTransform baseTransform, Rectangle rectangle) {
        BaseBounds baseBounds = new RectBounds();
        switch (renderType) {
            case EFFECT_CONTENT: {
                baseBounds = nGNode.getContentBounds(baseBounds, baseTransform);
                break;
            }
            case FULL_CONTENT: {
                baseBounds = nGNode.getCompleteBounds(baseBounds, baseTransform);
                break;
            }
            case CLIPPED_CONTENT: {
                baseBounds = nGNode.getClippedBounds(baseBounds, baseTransform);
            }
        }
        Rectangle rectangle2 = new Rectangle(baseBounds);
        if (rectangle != null) {
            rectangle2.intersectWith(rectangle);
        }
        return rectangle2;
    }

    private static ImageData getImageDataForBoundedNode(FilterContext filterContext, NGNode nGNode, RenderType renderType, BaseTransform baseTransform, Rectangle rectangle) {
        PrDrawable prDrawable = (PrDrawable)Effect.getCompatibleImage(filterContext, rectangle.width, rectangle.height);
        if (prDrawable != null) {
            Graphics graphics = prDrawable.createGraphics();
            graphics.translate(-rectangle.x, -rectangle.y);
            if (baseTransform != null) {
                graphics.transform(baseTransform);
            }
            switch (renderType) {
                case EFFECT_CONTENT: {
                    nGNode.renderContent(graphics);
                    break;
                }
                case FULL_CONTENT: {
                    nGNode.render(graphics);
                    break;
                }
                case CLIPPED_CONTENT: {
                    nGNode.renderForClip(graphics);
                }
            }
        }
        return new ImageData(filterContext, prDrawable, rectangle);
    }

    @Override
    public boolean reducesOpaquePixels() {
        return false;
    }

    @Override
    public DirtyRegionContainer getDirtyRegions(Effect effect, DirtyRegionPool dirtyRegionPool) {
        return null;
    }

    public static final class RenderType
    extends Enum<RenderType> {
        public static final /* enum */ RenderType EFFECT_CONTENT = new RenderType();
        public static final /* enum */ RenderType CLIPPED_CONTENT = new RenderType();
        public static final /* enum */ RenderType FULL_CONTENT = new RenderType();
        private static final /* synthetic */ RenderType[] $VALUES;

        public static RenderType[] values() {
            return (RenderType[])$VALUES.clone();
        }

        public static RenderType valueOf(String string) {
            return Enum.valueOf(RenderType.class, string);
        }

        private static /* synthetic */ RenderType[] $values() {
            return new RenderType[]{EFFECT_CONTENT, CLIPPED_CONTENT, FULL_CONTENT};
        }

        static {
            $VALUES = RenderType.$values();
        }
    }
}

