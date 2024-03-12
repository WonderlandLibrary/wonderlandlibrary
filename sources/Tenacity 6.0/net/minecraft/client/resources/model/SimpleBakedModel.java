// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources.model;

import com.google.common.collect.Lists;
import java.util.Iterator;
import net.minecraft.client.renderer.block.model.BreakingFour;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.block.model.BakedQuad;
import java.util.List;

public class SimpleBakedModel implements IBakedModel
{
    protected final List<BakedQuad> generalQuads;
    protected final List<List<BakedQuad>> faceQuads;
    protected final boolean ambientOcclusion;
    protected final boolean gui3d;
    protected final TextureAtlasSprite texture;
    protected final ItemCameraTransforms cameraTransforms;
    
    public SimpleBakedModel(final List<BakedQuad> generalQuadsIn, final List<List<BakedQuad>> faceQuadsIn, final boolean ambientOcclusionIn, final boolean gui3dIn, final TextureAtlasSprite textureIn, final ItemCameraTransforms cameraTransformsIn) {
        this.generalQuads = generalQuadsIn;
        this.faceQuads = faceQuadsIn;
        this.ambientOcclusion = ambientOcclusionIn;
        this.gui3d = gui3dIn;
        this.texture = textureIn;
        this.cameraTransforms = cameraTransformsIn;
    }
    
    @Override
    public List<BakedQuad> getFaceQuads(final EnumFacing facing) {
        return this.faceQuads.get(facing.ordinal());
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
    
    public static class Builder
    {
        private final List<BakedQuad> builderGeneralQuads;
        private final List<List<BakedQuad>> builderFaceQuads;
        private final boolean builderAmbientOcclusion;
        private TextureAtlasSprite builderTexture;
        private boolean builderGui3d;
        private ItemCameraTransforms builderCameraTransforms;
        
        public Builder(final ModelBlock model) {
            this(model.isAmbientOcclusion(), model.isGui3d(), model.getAllTransforms());
        }
        
        public Builder(final IBakedModel bakedModel, final TextureAtlasSprite texture) {
            this(bakedModel.isAmbientOcclusion(), bakedModel.isGui3d(), bakedModel.getItemCameraTransforms());
            this.builderTexture = bakedModel.getParticleTexture();
            for (final EnumFacing enumfacing : EnumFacing.values()) {
                this.addFaceBreakingFours(bakedModel, texture, enumfacing);
            }
            this.addGeneralBreakingFours(bakedModel, texture);
        }
        
        private void addFaceBreakingFours(final IBakedModel bakedModel, final TextureAtlasSprite texture, final EnumFacing facing) {
            for (final BakedQuad bakedquad : bakedModel.getFaceQuads(facing)) {
                this.addFaceQuad(facing, new BreakingFour(bakedquad, texture));
            }
        }
        
        private void addGeneralBreakingFours(final IBakedModel p_177647_1_, final TextureAtlasSprite texture) {
            for (final BakedQuad bakedquad : p_177647_1_.getGeneralQuads()) {
                this.addGeneralQuad(new BreakingFour(bakedquad, texture));
            }
        }
        
        private Builder(final boolean ambientOcclusion, final boolean gui3d, final ItemCameraTransforms cameraTransforms) {
            this.builderGeneralQuads = (List<BakedQuad>)Lists.newArrayList();
            this.builderFaceQuads = (List<List<BakedQuad>>)Lists.newArrayListWithCapacity(6);
            for (final EnumFacing enumfacing : EnumFacing.values()) {
                this.builderFaceQuads.add(Lists.newArrayList());
            }
            this.builderAmbientOcclusion = ambientOcclusion;
            this.builderGui3d = gui3d;
            this.builderCameraTransforms = cameraTransforms;
        }
        
        public Builder addFaceQuad(final EnumFacing facing, final BakedQuad quad) {
            this.builderFaceQuads.get(facing.ordinal()).add(quad);
            return this;
        }
        
        public Builder addGeneralQuad(final BakedQuad quad) {
            this.builderGeneralQuads.add(quad);
            return this;
        }
        
        public Builder setTexture(final TextureAtlasSprite texture) {
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
}
