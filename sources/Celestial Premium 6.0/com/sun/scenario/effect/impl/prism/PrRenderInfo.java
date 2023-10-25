/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.prism;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Graphics;
import com.sun.prism.Texture;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.ImageDataRenderer;
import com.sun.scenario.effect.impl.prism.PrTexture;

public class PrRenderInfo
implements ImageDataRenderer {
    private Graphics g;

    public PrRenderInfo(Graphics graphics) {
        this.g = graphics;
    }

    public Graphics getGraphics() {
        return this.g;
    }

    @Override
    public void renderImage(ImageData imageData, BaseTransform baseTransform, FilterContext filterContext) {
        if (imageData.validate(filterContext)) {
            BaseTransform baseTransform2;
            Rectangle rectangle = imageData.getUntransformedBounds();
            Object t = ((PrTexture)((Object)imageData.getUntransformedImage())).getTextureObject();
            BaseTransform baseTransform3 = null;
            if (!baseTransform.isIdentity()) {
                baseTransform3 = this.g.getTransformNoClone().copy();
                this.g.transform(baseTransform);
            }
            if (!(baseTransform2 = imageData.getTransform()).isIdentity()) {
                if (baseTransform3 == null) {
                    baseTransform3 = this.g.getTransformNoClone().copy();
                }
                this.g.transform(baseTransform2);
            }
            this.g.drawTexture((Texture)t, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            if (baseTransform3 != null) {
                this.g.setTransform(baseTransform3);
            }
        }
    }
}

