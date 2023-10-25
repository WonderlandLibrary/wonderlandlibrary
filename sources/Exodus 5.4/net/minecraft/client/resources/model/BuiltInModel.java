/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.resources.model;

import java.util.List;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.EnumFacing;

public class BuiltInModel
implements IBakedModel {
    private ItemCameraTransforms cameraTransforms;

    @Override
    public boolean isGui3d() {
        return true;
    }

    public BuiltInModel(ItemCameraTransforms itemCameraTransforms) {
        this.cameraTransforms = itemCameraTransforms;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return this.cameraTransforms;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing enumFacing) {
        return null;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return true;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return null;
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        return null;
    }
}

