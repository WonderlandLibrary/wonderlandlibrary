/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism;

import com.sun.prism.Image;
import com.sun.prism.PhongMaterial;
import com.sun.prism.Texture;

public class TextureMap {
    private final PhongMaterial.MapType type;
    private Image image;
    private Texture texture;
    private boolean dirty;

    public TextureMap(PhongMaterial.MapType mapType) {
        this.type = mapType;
    }

    public PhongMaterial.MapType getType() {
        return this.type;
    }

    public Image getImage() {
        return this.image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public void setDirty(boolean bl) {
        this.dirty = bl;
    }
}

