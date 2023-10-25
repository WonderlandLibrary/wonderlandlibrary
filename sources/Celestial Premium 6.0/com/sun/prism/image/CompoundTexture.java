/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.image;

import com.sun.prism.GraphicsResource;
import com.sun.prism.Image;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.image.CompoundImage;

public class CompoundTexture
extends CompoundImage
implements GraphicsResource {
    protected Texture[] texTiles;

    public CompoundTexture(Image image, int n) {
        super(image, n);
        this.texTiles = new Texture[this.tiles.length];
    }

    @Override
    public Texture getTile(int n, int n2, ResourceFactory resourceFactory) {
        int n3 = n + n2 * this.uSections;
        Texture texture = this.texTiles[n3];
        if (texture != null) {
            texture.lock();
            if (texture.isSurfaceLost()) {
                texture = null;
                this.texTiles[n3] = null;
            }
        }
        if (texture == null) {
            this.texTiles[n3] = texture = resourceFactory.createTexture(this.tiles[n3], Texture.Usage.STATIC, Texture.WrapMode.CLAMP_TO_EDGE);
        }
        return texture;
    }

    @Override
    public void dispose() {
        for (int i = 0; i != this.texTiles.length; ++i) {
            if (this.texTiles[i] == null) continue;
            this.texTiles[i].dispose();
            this.texTiles[i] = null;
        }
    }
}

