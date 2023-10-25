package net.minecraft.client.resources.model;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BreakingFour;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.util.EnumFacing;

public static class SimpleBakedModel.Builder {
    private final List<BakedQuad> builderGeneralQuads = Lists.newArrayList();
    private final List<List<BakedQuad>> builderFaceQuads = Lists.newArrayListWithCapacity((int)6);
    private final boolean builderAmbientOcclusion;
    private TextureAtlasSprite builderTexture;
    private boolean builderGui3d;
    private ItemCameraTransforms builderCameraTransforms;

    public SimpleBakedModel.Builder(ModelBlock model) {
        this(model.isAmbientOcclusion(), model.isGui3d(), model.getAllTransforms());
    }

    public SimpleBakedModel.Builder(IBakedModel bakedModel, TextureAtlasSprite texture) {
        this(bakedModel.isAmbientOcclusion(), bakedModel.isGui3d(), bakedModel.getItemCameraTransforms());
        this.builderTexture = bakedModel.getParticleTexture();
        for (EnumFacing enumfacing : EnumFacing.values()) {
            this.addFaceBreakingFours(bakedModel, texture, enumfacing);
        }
        this.addGeneralBreakingFours(bakedModel, texture);
    }

    private void addFaceBreakingFours(IBakedModel bakedModel, TextureAtlasSprite texture, EnumFacing facing) {
        for (BakedQuad bakedquad : bakedModel.getFaceQuads(facing)) {
            this.addFaceQuad(facing, (BakedQuad)new BreakingFour(bakedquad, texture));
        }
    }

    private void addGeneralBreakingFours(IBakedModel p_177647_1_, TextureAtlasSprite texture) {
        for (BakedQuad bakedquad : p_177647_1_.getGeneralQuads()) {
            this.addGeneralQuad((BakedQuad)new BreakingFour(bakedquad, texture));
        }
    }

    private SimpleBakedModel.Builder(boolean ambientOcclusion, boolean gui3d, ItemCameraTransforms cameraTransforms) {
        for (EnumFacing enumfacing : EnumFacing.values()) {
            this.builderFaceQuads.add((Object)Lists.newArrayList());
        }
        this.builderAmbientOcclusion = ambientOcclusion;
        this.builderGui3d = gui3d;
        this.builderCameraTransforms = cameraTransforms;
    }

    public SimpleBakedModel.Builder addFaceQuad(EnumFacing facing, BakedQuad quad) {
        ((List)this.builderFaceQuads.get(facing.ordinal())).add((Object)quad);
        return this;
    }

    public SimpleBakedModel.Builder addGeneralQuad(BakedQuad quad) {
        this.builderGeneralQuads.add((Object)quad);
        return this;
    }

    public SimpleBakedModel.Builder setTexture(TextureAtlasSprite texture) {
        this.builderTexture = texture;
        return this;
    }

    public IBakedModel makeBakedModel() {
        if (this.builderTexture == null) {
            throw new RuntimeException("Missing particle!");
        }
        return new SimpleBakedModel(this.builderGeneralQuads, this.builderFaceQuads, this.builderAmbientOcclusion, this.builderGui3d, this.builderTexture, this.builderCameraTransforms);
    }
}
