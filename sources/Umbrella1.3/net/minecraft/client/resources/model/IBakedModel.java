/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.resources.model;

import java.util.List;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public interface IBakedModel {
    public List func_177551_a(EnumFacing var1);

    public List func_177550_a();

    public boolean isGui3d();

    public boolean isAmbientOcclusionEnabled();

    public boolean isBuiltInRenderer();

    public TextureAtlasSprite getTexture();

    public ItemCameraTransforms getItemCameraTransforms();
}

