/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.prism.Graphics;
import com.sun.prism.Image;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.image.CachingCompoundImage;
import com.sun.prism.image.CompoundCoords;
import com.sun.prism.image.Coords;
import com.sun.prism.image.ViewPort;

public class NGImageView
extends NGNode {
    private Image image;
    private CachingCompoundImage compoundImage;
    private CompoundCoords compoundCoords;
    private float x;
    private float y;
    private float w;
    private float h;
    private Coords coords;
    private ViewPort reqviewport;
    private ViewPort imgviewport;
    private boolean renderable = false;
    private boolean coordsOK = false;
    static final int MAX_SIZE_OVERRIDE = 0;

    private void invalidate() {
        this.coordsOK = false;
        this.coords = null;
        this.compoundCoords = null;
        this.imgviewport = null;
        this.geometryChanged();
    }

    public void setViewport(float f, float f2, float f3, float f4, float f5, float f6) {
        this.reqviewport = f3 > 0.0f && f4 > 0.0f ? new ViewPort(f, f2, f3, f4) : null;
        this.w = f5;
        this.h = f6;
        this.invalidate();
    }

    private void calculatePositionAndClipping() {
        this.renderable = false;
        this.coordsOK = true;
        if (this.reqviewport == null || this.image == null) {
            this.renderable = this.image != null;
            return;
        }
        float f = this.image.getWidth();
        float f2 = this.image.getHeight();
        if (f == 0.0f || f2 == 0.0f) {
            return;
        }
        this.imgviewport = this.reqviewport.getScaledVersion(this.image.getPixelScale());
        this.coords = this.imgviewport.getClippedCoords(f, f2, this.w, this.h);
        this.renderable = this.coords != null;
    }

    @Override
    protected void doRender(Graphics graphics) {
        if (!this.coordsOK) {
            this.calculatePositionAndClipping();
        }
        if (this.renderable) {
            super.doRender(graphics);
        }
    }

    private int maxSizeWrapper(ResourceFactory resourceFactory) {
        return resourceFactory.getMaximumTextureSize();
    }

    @Override
    protected void renderContent(Graphics graphics) {
        int n = this.image.getWidth();
        int n2 = this.image.getHeight();
        ResourceFactory resourceFactory = graphics.getResourceFactory();
        int n3 = this.maxSizeWrapper(resourceFactory);
        if (n <= n3 && n2 <= n3) {
            Texture texture = resourceFactory.getCachedTexture(this.image, Texture.WrapMode.CLAMP_TO_EDGE);
            if (this.coords == null) {
                graphics.drawTexture(texture, this.x, this.y, this.x + this.w, this.y + this.h, 0.0f, 0.0f, n, n2);
            } else {
                this.coords.draw(texture, graphics, this.x, this.y);
            }
            texture.unlock();
        } else {
            if (this.compoundImage == null) {
                this.compoundImage = new CachingCompoundImage(this.image, n3);
            }
            if (this.coords == null) {
                this.coords = new Coords(this.w, this.h, new ViewPort(0.0f, 0.0f, n, n2));
            }
            if (this.compoundCoords == null) {
                this.compoundCoords = new CompoundCoords(this.compoundImage, this.coords);
            }
            this.compoundCoords.draw(graphics, this.compoundImage, this.x, this.y);
        }
    }

    @Override
    protected boolean hasOverlappingContents() {
        return false;
    }

    public void setImage(Object object) {
        Image image = (Image)object;
        if (this.image == image) {
            return;
        }
        boolean bl = image == null || this.image == null || this.image.getPixelScale() != image.getPixelScale() || this.image.getHeight() != image.getHeight() || this.image.getWidth() != image.getWidth();
        this.image = image;
        this.compoundImage = null;
        if (bl) {
            this.invalidate();
        }
    }

    public void setX(float f) {
        if (this.x != f) {
            this.x = f;
            this.geometryChanged();
        }
    }

    public void setY(float f) {
        if (this.y != f) {
            this.y = f;
            this.geometryChanged();
        }
    }

    public void setSmooth(boolean bl) {
    }

    @Override
    protected boolean supportsOpaqueRegions() {
        return true;
    }

    @Override
    protected boolean hasOpaqueRegion() {
        assert (this.image == null || this.image.getWidth() >= 1 && this.image.getHeight() >= 1);
        return super.hasOpaqueRegion() && this.w >= 1.0f && this.h >= 1.0f && this.image != null && this.image.isOpaque();
    }

    @Override
    protected RectBounds computeOpaqueRegion(RectBounds rectBounds) {
        return (RectBounds)rectBounds.deriveWithNewBounds(this.x, this.y, 0.0f, this.x + this.w, this.y + this.h, 0.0f);
    }
}

