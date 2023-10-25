/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.block.model;

import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public interface IBakedModel {
    public List<BakedQuad> getQuads(IBlockState var1, EnumFacing var2, long var3);

    public boolean isAmbientOcclusion();

    public boolean isGui3d();

    public boolean isBuiltInRenderer();

    public TextureAtlasSprite getParticleTexture();

    public ItemCameraTransforms getItemCameraTransforms();

    public ItemOverrideList getOverrides();
}

