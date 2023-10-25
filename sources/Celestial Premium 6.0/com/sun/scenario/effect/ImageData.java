/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.impl.Renderer;
import java.security.AccessController;
import java.util.HashSet;

public class ImageData {
    private static HashSet<ImageData> alldatas;
    private ImageData sharedOwner;
    private FilterContext fctx;
    private int refcount;
    private Filterable image;
    private final Rectangle bounds;
    private BaseTransform transform;
    private Throwable fromwhere;
    private boolean reusable;

    public ImageData(FilterContext filterContext, Filterable filterable, Rectangle rectangle) {
        this(filterContext, filterable, rectangle, BaseTransform.IDENTITY_TRANSFORM);
    }

    public ImageData(FilterContext filterContext, Filterable filterable, Rectangle rectangle, BaseTransform baseTransform) {
        this.fctx = filterContext;
        this.refcount = 1;
        this.image = filterable;
        this.bounds = rectangle;
        this.transform = baseTransform;
        if (alldatas != null) {
            alldatas.add(this);
            this.fromwhere = new Throwable();
        }
    }

    public ImageData transform(BaseTransform baseTransform) {
        if (baseTransform.isIdentity()) {
            return this;
        }
        BaseTransform baseTransform2 = this.transform.isIdentity() ? baseTransform : baseTransform.copy().deriveWithConcatenation(this.transform);
        return new ImageData(this, baseTransform2, this.bounds);
    }

    private ImageData(ImageData imageData, BaseTransform baseTransform, Rectangle rectangle) {
        this(imageData.fctx, imageData.image, rectangle, baseTransform);
        this.sharedOwner = imageData;
    }

    public void setReusable(boolean bl) {
        if (this.sharedOwner != null) {
            throw new InternalError("cannot make a shared ImageData reusable");
        }
        this.reusable = bl;
    }

    public FilterContext getFilterContext() {
        return this.fctx;
    }

    public Filterable getUntransformedImage() {
        return this.image;
    }

    public Rectangle getUntransformedBounds() {
        return this.bounds;
    }

    public BaseTransform getTransform() {
        return this.transform;
    }

    public Filterable getTransformedImage(Rectangle rectangle) {
        if (this.image == null || this.fctx == null) {
            return null;
        }
        if (this.transform.isIdentity()) {
            return this.image;
        }
        Rectangle rectangle2 = this.getTransformedBounds(rectangle);
        return Renderer.getRenderer(this.fctx).transform(this.fctx, this.image, this.transform, this.bounds, rectangle2);
    }

    public void releaseTransformedImage(Filterable filterable) {
        if (this.fctx != null && filterable != null && filterable != this.image) {
            Effect.releaseCompatibleImage(this.fctx, filterable);
        }
    }

    public Rectangle getTransformedBounds(Rectangle rectangle) {
        if (this.transform.isIdentity()) {
            return this.bounds;
        }
        Rectangle rectangle2 = new Rectangle();
        this.transform.transform(this.bounds, rectangle2);
        if (rectangle != null) {
            rectangle2.intersectWith(rectangle);
        }
        return rectangle2;
    }

    public int getReferenceCount() {
        return this.refcount;
    }

    public boolean addref() {
        if (this.reusable && this.refcount == 0 && this.image != null) {
            this.image.lock();
        }
        ++this.refcount;
        return this.image != null && !this.image.isLost();
    }

    public void unref() {
        if (--this.refcount == 0) {
            if (this.sharedOwner != null) {
                this.sharedOwner.unref();
                this.sharedOwner = null;
            } else if (this.fctx != null && this.image != null) {
                if (this.reusable) {
                    this.image.unlock();
                    return;
                }
                Effect.releaseCompatibleImage(this.fctx, this.image);
            }
            this.fctx = null;
            this.image = null;
            if (alldatas != null) {
                alldatas.remove(this);
            }
        }
    }

    public boolean validate(FilterContext filterContext) {
        return this.image != null && Renderer.getRenderer(filterContext).isImageDataCompatible(this);
    }

    public String toString() {
        return "ImageData{sharedOwner=" + this.sharedOwner + ", fctx=" + this.fctx + ", refcount=" + this.refcount + ", image=" + this.image + ", bounds=" + this.bounds + ", transform=" + this.transform + ", reusable=" + this.reusable + "}";
    }

    static {
        Object object = AccessController.doPrivileged(() -> {
            if (System.getProperty("decora.showleaks") != null) {
                alldatas = new HashSet();
                Runtime.getRuntime().addShutdownHook(new Thread(){

                    @Override
                    public void run() {
                        for (ImageData imageData : alldatas) {
                            Rectangle rectangle = imageData.getUntransformedBounds();
                            System.out.println("id[" + rectangle.width + "x" + rectangle.height + ", refcount=" + imageData.refcount + "] leaked from:");
                            imageData.fromwhere.printStackTrace(System.out);
                        }
                    }
                });
            }
            return null;
        });
    }
}

