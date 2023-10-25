/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.paint;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Image;
import com.sun.prism.paint.Paint;

public final class ImagePattern
extends Paint {
    private final Image image;
    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private final BaseTransform patternTransform;

    public ImagePattern(Image image, float f, float f2, float f3, float f4, BaseTransform baseTransform, boolean bl, boolean bl2) {
        super(Paint.Type.IMAGE_PATTERN, bl, bl2);
        if (image == null) {
            throw new IllegalArgumentException("Image must be non-null");
        }
        this.image = image;
        this.x = f;
        this.y = f2;
        this.width = f3;
        this.height = f4;
        this.patternTransform = baseTransform != null ? baseTransform.copy() : BaseTransform.IDENTITY_TRANSFORM;
    }

    public ImagePattern(Image image, float f, float f2, float f3, float f4, boolean bl, boolean bl2) {
        this(image, f, f2, f3, f4, null, bl, bl2);
    }

    public Image getImage() {
        return this.image;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public BaseTransform getPatternTransformNoClone() {
        return this.patternTransform;
    }

    @Override
    public boolean isOpaque() {
        return this.image.isOpaque();
    }
}

