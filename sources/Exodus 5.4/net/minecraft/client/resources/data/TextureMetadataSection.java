/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.resources.data;

import java.util.Collections;
import java.util.List;
import net.minecraft.client.resources.data.IMetadataSection;

public class TextureMetadataSection
implements IMetadataSection {
    private final boolean textureBlur;
    private final boolean textureClamp;
    private final List<Integer> listMipmaps;

    public boolean getTextureClamp() {
        return this.textureClamp;
    }

    public List<Integer> getListMipmaps() {
        return Collections.unmodifiableList(this.listMipmaps);
    }

    public boolean getTextureBlur() {
        return this.textureBlur;
    }

    public TextureMetadataSection(boolean bl, boolean bl2, List<Integer> list) {
        this.textureBlur = bl;
        this.textureClamp = bl2;
        this.listMipmaps = list;
    }
}

