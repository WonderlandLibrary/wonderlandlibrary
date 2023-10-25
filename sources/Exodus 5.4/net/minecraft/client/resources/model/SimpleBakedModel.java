/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.client.resources.model;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BreakingFour;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.EnumFacing;

public class SimpleBakedModel
implements IBakedModel {
    protected final boolean gui3d;
    protected final boolean ambientOcclusion;
    protected final List<List<BakedQuad>> faceQuads;
    protected final ItemCameraTransforms cameraTransforms;
    protected final List<BakedQuad> generalQuads;
    protected final TextureAtlasSprite texture;

    public SimpleBakedModel(List<BakedQuad> list, List<List<BakedQuad>> list2, boolean bl, boolean bl2, TextureAtlasSprite textureAtlasSprite, ItemCameraTransforms itemCameraTransforms) {
        this.generalQuads = list;
        this.faceQuads = list2;
        this.ambientOcclusion = bl;
        this.gui3d = bl2;
        this.texture = textureAtlasSprite;
        this.cameraTransforms = itemCameraTransforms;
    }

    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing enumFacing) {
        return this.faceQuads.get(enumFacing.ordinal());
    }

    @Override
    public boolean isGui3d() {
        return this.gui3d;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return this.texture;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return this.cameraTransforms;
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        return this.generalQuads;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return this.ambientOcclusion;
    }

    public static class Builder {
        private final List<List<BakedQuad>> builderFaceQuads;
        private boolean builderGui3d;
        private TextureAtlasSprite builderTexture;
        private final boolean builderAmbientOcclusion;
        private final List<BakedQuad> builderGeneralQuads = Lists.newArrayList();
        private ItemCameraTransforms builderCameraTransforms;

        public Builder addFaceQuad(EnumFacing enumFacing, BakedQuad bakedQuad) {
            this.builderFaceQuads.get(enumFacing.ordinal()).add(bakedQuad);
            return this;
        }

        public Builder addGeneralQuad(BakedQuad bakedQuad) {
            this.builderGeneralQuads.add(bakedQuad);
            return this;
        }

        public Builder(IBakedModel iBakedModel, TextureAtlasSprite textureAtlasSprite) {
            this(iBakedModel.isAmbientOcclusion(), iBakedModel.isGui3d(), iBakedModel.getItemCameraTransforms());
            this.builderTexture = iBakedModel.getParticleTexture();
            EnumFacing[] enumFacingArray = EnumFacing.values();
            int n = enumFacingArray.length;
            int n2 = 0;
            while (n2 < n) {
                EnumFacing enumFacing = enumFacingArray[n2];
                this.addFaceBreakingFours(iBakedModel, textureAtlasSprite, enumFacing);
                ++n2;
            }
            this.addGeneralBreakingFours(iBakedModel, textureAtlasSprite);
        }

        private void addGeneralBreakingFours(IBakedModel iBakedModel, TextureAtlasSprite textureAtlasSprite) {
            for (BakedQuad bakedQuad : iBakedModel.getGeneralQuads()) {
                this.addGeneralQuad(new BreakingFour(bakedQuad, textureAtlasSprite));
            }
        }

        public IBakedModel makeBakedModel() {
            if (this.builderTexture == null) {
                throw new RuntimeException("Missing particle!");
            }
            return new SimpleBakedModel(this.builderGeneralQuads, this.builderFaceQuads, this.builderAmbientOcclusion, this.builderGui3d, this.builderTexture, this.builderCameraTransforms);
        }

        private void addFaceBreakingFours(IBakedModel iBakedModel, TextureAtlasSprite textureAtlasSprite, EnumFacing enumFacing) {
            for (BakedQuad bakedQuad : iBakedModel.getFaceQuads(enumFacing)) {
                this.addFaceQuad(enumFacing, new BreakingFour(bakedQuad, textureAtlasSprite));
            }
        }

        private Builder(boolean bl, boolean bl2, ItemCameraTransforms itemCameraTransforms) {
            this.builderFaceQuads = Lists.newArrayListWithCapacity((int)6);
            EnumFacing[] enumFacingArray = EnumFacing.values();
            int n = enumFacingArray.length;
            int n2 = 0;
            while (n2 < n) {
                EnumFacing enumFacing = enumFacingArray[n2];
                this.builderFaceQuads.add(Lists.newArrayList());
                ++n2;
            }
            this.builderAmbientOcclusion = bl;
            this.builderGui3d = bl2;
            this.builderCameraTransforms = itemCameraTransforms;
        }

        public Builder setTexture(TextureAtlasSprite textureAtlasSprite) {
            this.builderTexture = textureAtlasSprite;
            return this;
        }

        public Builder(ModelBlock modelBlock) {
            this(modelBlock.isAmbientOcclusion(), modelBlock.isGui3d(), modelBlock.func_181682_g());
        }
    }
}

