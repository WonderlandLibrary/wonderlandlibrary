// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.model;

import java.util.Iterator;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import java.util.Map;
import java.util.List;

public class SimpleBakedModel implements IBakedModel
{
    protected final List<BakedQuad> generalQuads;
    protected final Map<EnumFacing, List<BakedQuad>> faceQuads;
    protected final boolean ambientOcclusion;
    protected final boolean gui3d;
    protected final TextureAtlasSprite texture;
    protected final ItemCameraTransforms cameraTransforms;
    protected final ItemOverrideList itemOverrideList;
    
    public SimpleBakedModel(final List<BakedQuad> generalQuadsIn, final Map<EnumFacing, List<BakedQuad>> faceQuadsIn, final boolean ambientOcclusionIn, final boolean gui3dIn, final TextureAtlasSprite textureIn, final ItemCameraTransforms cameraTransformsIn, final ItemOverrideList itemOverrideListIn) {
        this.generalQuads = generalQuadsIn;
        this.faceQuads = faceQuadsIn;
        this.ambientOcclusion = ambientOcclusionIn;
        this.gui3d = gui3dIn;
        this.texture = textureIn;
        this.cameraTransforms = cameraTransformsIn;
        this.itemOverrideList = itemOverrideListIn;
    }
    
    @Override
    public List<BakedQuad> getQuads(@Nullable final IBlockState state, @Nullable final EnumFacing side, final long rand) {
        return (side == null) ? this.generalQuads : this.faceQuads.get(side);
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
    
    @Override
    public ItemOverrideList getOverrides() {
        return this.itemOverrideList;
    }
    
    public static class Builder
    {
        private final List<BakedQuad> builderGeneralQuads;
        private final Map<EnumFacing, List<BakedQuad>> builderFaceQuads;
        private final ItemOverrideList builderItemOverrideList;
        private final boolean builderAmbientOcclusion;
        private TextureAtlasSprite builderTexture;
        private final boolean builderGui3d;
        private final ItemCameraTransforms builderCameraTransforms;
        
        public Builder(final ModelBlock model, final ItemOverrideList overrides) {
            this(model.isAmbientOcclusion(), model.isGui3d(), model.getAllTransforms(), overrides);
        }
        
        public Builder(final IBlockState state, final IBakedModel model, final TextureAtlasSprite texture, final BlockPos pos) {
            this(model.isAmbientOcclusion(), model.isGui3d(), model.getItemCameraTransforms(), model.getOverrides());
            this.builderTexture = model.getParticleTexture();
            final long i = MathHelper.getPositionRandom(pos);
            for (final EnumFacing enumfacing : EnumFacing.values()) {
                this.addFaceQuads(state, model, texture, enumfacing, i);
            }
            this.addGeneralQuads(state, model, texture, i);
        }
        
        private Builder(final boolean ambientOcclusion, final boolean gui3d, final ItemCameraTransforms transforms, final ItemOverrideList overrides) {
            this.builderGeneralQuads = (List<BakedQuad>)Lists.newArrayList();
            this.builderFaceQuads = (Map<EnumFacing, List<BakedQuad>>)Maps.newEnumMap((Class)EnumFacing.class);
            for (final EnumFacing enumfacing : EnumFacing.values()) {
                this.builderFaceQuads.put(enumfacing, Lists.newArrayList());
            }
            this.builderItemOverrideList = overrides;
            this.builderAmbientOcclusion = ambientOcclusion;
            this.builderGui3d = gui3d;
            this.builderCameraTransforms = transforms;
        }
        
        private void addFaceQuads(final IBlockState p_188644_1_, final IBakedModel p_188644_2_, final TextureAtlasSprite p_188644_3_, final EnumFacing p_188644_4_, final long p_188644_5_) {
            for (final BakedQuad bakedquad : p_188644_2_.getQuads(p_188644_1_, p_188644_4_, p_188644_5_)) {
                this.addFaceQuad(p_188644_4_, new BakedQuadRetextured(bakedquad, p_188644_3_));
            }
        }
        
        private void addGeneralQuads(final IBlockState p_188645_1_, final IBakedModel p_188645_2_, final TextureAtlasSprite p_188645_3_, final long p_188645_4_) {
            for (final BakedQuad bakedquad : p_188645_2_.getQuads(p_188645_1_, null, p_188645_4_)) {
                this.addGeneralQuad(new BakedQuadRetextured(bakedquad, p_188645_3_));
            }
        }
        
        public Builder addFaceQuad(final EnumFacing facing, final BakedQuad quad) {
            this.builderFaceQuads.get(facing).add(quad);
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
            return new SimpleBakedModel(this.builderGeneralQuads, this.builderFaceQuads, this.builderAmbientOcclusion, this.builderGui3d, this.builderTexture, this.builderCameraTransforms, this.builderItemOverrideList);
        }
    }
}
