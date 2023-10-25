/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.resources.data;

import net.minecraft.client.resources.data.IMetadataSection;

public class TextureMetadataSection
implements IMetadataSection {
    private final boolean textureBlur;
    private final boolean textureClamp;

    public TextureMetadataSection(boolean textureBlurIn, boolean textureClampIn) {
        this.textureBlur = textureBlurIn;
        this.textureClamp = textureClampIn;
    }

    public boolean getTextureBlur() {
        return this.textureBlur;
    }

    public boolean getTextureClamp() {
        return this.textureClamp;
    }
}

