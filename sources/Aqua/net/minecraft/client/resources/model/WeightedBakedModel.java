package net.minecraft.client.resources.model;

import java.util.List;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.WeightedBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandom;

public class WeightedBakedModel
implements IBakedModel {
    private final int totalWeight;
    private final List<MyWeighedRandomItem> models;
    private final IBakedModel baseModel;

    public WeightedBakedModel(List<MyWeighedRandomItem> p_i46073_1_) {
        this.models = p_i46073_1_;
        this.totalWeight = WeightedRandom.getTotalWeight(p_i46073_1_);
        this.baseModel = ((MyWeighedRandomItem)p_i46073_1_.get((int)0)).model;
    }

    public List<BakedQuad> getFaceQuads(EnumFacing facing) {
        return this.baseModel.getFaceQuads(facing);
    }

    public List<BakedQuad> getGeneralQuads() {
        return this.baseModel.getGeneralQuads();
    }

    public boolean isAmbientOcclusion() {
        return this.baseModel.isAmbientOcclusion();
    }

    public boolean isGui3d() {
        return this.baseModel.isGui3d();
    }

    public boolean isBuiltInRenderer() {
        return this.baseModel.isBuiltInRenderer();
    }

    public TextureAtlasSprite getParticleTexture() {
        return this.baseModel.getParticleTexture();
    }

    public ItemCameraTransforms getItemCameraTransforms() {
        return this.baseModel.getItemCameraTransforms();
    }

    public IBakedModel getAlternativeModel(long p_177564_1_) {
        return ((MyWeighedRandomItem)WeightedRandom.getRandomItem(this.models, (int)(Math.abs((int)((int)p_177564_1_ >> 16)) % this.totalWeight))).model;
    }
}
