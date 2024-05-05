/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.resources.model;

import com.google.common.collect.Lists;
import java.util.Iterator;
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
    protected final List<BakedQuad> generalQuads;
    protected final List<List<BakedQuad>> faceQuads;
    protected final boolean ambientOcclusion;
    protected final boolean gui3d;
    protected final TextureAtlasSprite texture;
    protected final ItemCameraTransforms cameraTransforms;

    public SimpleBakedModel(List<BakedQuad> p_i46077_1_, List<List<BakedQuad>> p_i46077_2_, boolean p_i46077_3_, boolean p_i46077_4_, TextureAtlasSprite p_i46077_5_, ItemCameraTransforms p_i46077_6_) {
        this.generalQuads = p_i46077_1_;
        this.faceQuads = p_i46077_2_;
        this.ambientOcclusion = p_i46077_3_;
        this.gui3d = p_i46077_4_;
        this.texture = p_i46077_5_;
        this.cameraTransforms = p_i46077_6_;
    }

    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing p_177551_1_) {
        return this.faceQuads.get(p_177551_1_.ordinal());
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        return this.generalQuads;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return this.ambientOcclusion;
    }

    @Override
    public boolean isGui3d() {
        return this.gui3d;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return this.texture;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return this.cameraTransforms;
    }

    public static class Builder {
        private final List<BakedQuad> builderGeneralQuads = Lists.newArrayList();
        private final List<List<BakedQuad>> builderFaceQuads = Lists.newArrayListWithCapacity(6);
        private final boolean builderAmbientOcclusion;
        private TextureAtlasSprite builderTexture;
        private boolean builderGui3d;
        private ItemCameraTransforms builderCameraTransforms;

        public Builder(ModelBlock p_i46074_1_) {
            this(p_i46074_1_.isAmbientOcclusion(), p_i46074_1_.isGui3d(), p_i46074_1_.func_181682_g());
        }

        public Builder(IBakedModel p_i46075_1_, TextureAtlasSprite p_i46075_2_) {
            this(p_i46075_1_.isAmbientOcclusion(), p_i46075_1_.isGui3d(), p_i46075_1_.getItemCameraTransforms());
            this.builderTexture = p_i46075_1_.getParticleTexture();
            EnumFacing[] enumFacingArray = EnumFacing.values();
            int n = enumFacingArray.length;
            int n2 = 0;
            while (true) {
                if (n2 >= n) {
                    this.addGeneralBreakingFours(p_i46075_1_, p_i46075_2_);
                    return;
                }
                EnumFacing enumfacing = enumFacingArray[n2];
                this.addFaceBreakingFours(p_i46075_1_, p_i46075_2_, enumfacing);
                ++n2;
            }
        }

        private void addFaceBreakingFours(IBakedModel p_177649_1_, TextureAtlasSprite p_177649_2_, EnumFacing p_177649_3_) {
            Iterator<BakedQuad> iterator = p_177649_1_.getFaceQuads(p_177649_3_).iterator();
            while (iterator.hasNext()) {
                BakedQuad bakedquad = iterator.next();
                this.addFaceQuad(p_177649_3_, new BreakingFour(bakedquad, p_177649_2_));
            }
        }

        private void addGeneralBreakingFours(IBakedModel p_177647_1_, TextureAtlasSprite p_177647_2_) {
            Iterator<BakedQuad> iterator = p_177647_1_.getGeneralQuads().iterator();
            while (iterator.hasNext()) {
                BakedQuad bakedquad = iterator.next();
                this.addGeneralQuad(new BreakingFour(bakedquad, p_177647_2_));
            }
        }

        private Builder(boolean p_i46076_1_, boolean p_i46076_2_, ItemCameraTransforms p_i46076_3_) {
            EnumFacing[] enumFacingArray = EnumFacing.values();
            int n = enumFacingArray.length;
            int n2 = 0;
            while (true) {
                if (n2 >= n) {
                    this.builderAmbientOcclusion = p_i46076_1_;
                    this.builderGui3d = p_i46076_2_;
                    this.builderCameraTransforms = p_i46076_3_;
                    return;
                }
                EnumFacing enumfacing = enumFacingArray[n2];
                this.builderFaceQuads.add(Lists.newArrayList());
                ++n2;
            }
        }

        public Builder addFaceQuad(EnumFacing p_177650_1_, BakedQuad p_177650_2_) {
            this.builderFaceQuads.get(p_177650_1_.ordinal()).add(p_177650_2_);
            return this;
        }

        public Builder addGeneralQuad(BakedQuad p_177648_1_) {
            this.builderGeneralQuads.add(p_177648_1_);
            return this;
        }

        public Builder setTexture(TextureAtlasSprite p_177646_1_) {
            this.builderTexture = p_177646_1_;
            return this;
        }

        public IBakedModel makeBakedModel() {
            if (this.builderTexture != null) return new SimpleBakedModel(this.builderGeneralQuads, this.builderFaceQuads, this.builderAmbientOcclusion, this.builderGui3d, this.builderTexture, this.builderCameraTransforms);
            throw new RuntimeException("Missing particle!");
        }
    }
}

