package net.minecraft.client.resources.model;

import java.util.List;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.EnumFacing;

public class SimpleBakedModel
implements IBakedModel {
    protected final List<BakedQuad> generalQuads;
    protected final List<List<BakedQuad>> faceQuads;
    protected final boolean ambientOcclusion;
    protected final boolean gui3d;
    protected final TextureAtlasSprite texture;
    protected final ItemCameraTransforms cameraTransforms;

    public SimpleBakedModel(List<BakedQuad> generalQuadsIn, List<List<BakedQuad>> faceQuadsIn, boolean ambientOcclusionIn, boolean gui3dIn, TextureAtlasSprite textureIn, ItemCameraTransforms cameraTransformsIn) {
        this.generalQuads = generalQuadsIn;
        this.faceQuads = faceQuadsIn;
        this.ambientOcclusion = ambientOcclusionIn;
        this.gui3d = gui3dIn;
        this.texture = textureIn;
        this.cameraTransforms = cameraTransformsIn;
    }

    public List<BakedQuad> getFaceQuads(EnumFacing facing) {
        return (List)this.faceQuads.get(facing.ordinal());
    }

    public List<BakedQuad> getGeneralQuads() {
        return this.generalQuads;
    }

    public boolean isAmbientOcclusion() {
        return this.ambientOcclusion;
    }

    public boolean isGui3d() {
        return this.gui3d;
    }

    public boolean isBuiltInRenderer() {
        return false;
    }

    public TextureAtlasSprite getParticleTexture() {
        return this.texture;
    }

    public ItemCameraTransforms getItemCameraTransforms() {
        return this.cameraTransforms;
    }
}
