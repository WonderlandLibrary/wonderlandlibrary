/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.resources.model;

import java.util.List;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public interface IBakedModel {
    public ItemCameraTransforms getItemCameraTransforms();

    public List<BakedQuad> getGeneralQuads();

    public List<BakedQuad> getFaceQuads(EnumFacing var1);

    public boolean isBuiltInRenderer();

    public boolean isGui3d();

    public TextureAtlasSprite getParticleTexture();

    public boolean isAmbientOcclusion();
}

