// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.model;

import com.google.common.collect.ComparisonChain;
import java.util.Collections;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.WeightedRandom;
import java.util.List;

public class WeightedBakedModel implements IBakedModel
{
    private final int totalWeight;
    private final List<WeightedModel> models;
    private final IBakedModel baseModel;
    
    public WeightedBakedModel(final List<WeightedModel> modelsIn) {
        this.models = modelsIn;
        this.totalWeight = WeightedRandom.getTotalWeight(modelsIn);
        this.baseModel = modelsIn.get(0).model;
    }
    
    private IBakedModel getRandomModel(final long p_188627_1_) {
        return WeightedRandom.getRandomItem(this.models, Math.abs((int)p_188627_1_ >> 16) % this.totalWeight).model;
    }
    
    @Override
    public List<BakedQuad> getQuads(@Nullable final IBlockState state, @Nullable final EnumFacing side, final long rand) {
        return this.getRandomModel(rand).getQuads(state, side, rand);
    }
    
    @Override
    public boolean isAmbientOcclusion() {
        return this.baseModel.isAmbientOcclusion();
    }
    
    @Override
    public boolean isGui3d() {
        return this.baseModel.isGui3d();
    }
    
    @Override
    public boolean isBuiltInRenderer() {
        return this.baseModel.isBuiltInRenderer();
    }
    
    @Override
    public TextureAtlasSprite getParticleTexture() {
        return this.baseModel.getParticleTexture();
    }
    
    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return this.baseModel.getItemCameraTransforms();
    }
    
    @Override
    public ItemOverrideList getOverrides() {
        return this.baseModel.getOverrides();
    }
    
    public static class Builder
    {
        private final List<WeightedModel> listItems;
        
        public Builder() {
            this.listItems = (List<WeightedModel>)Lists.newArrayList();
        }
        
        public Builder add(final IBakedModel model, final int weight) {
            this.listItems.add(new WeightedModel(model, weight));
            return this;
        }
        
        public WeightedBakedModel build() {
            Collections.sort(this.listItems);
            return new WeightedBakedModel(this.listItems);
        }
        
        public IBakedModel first() {
            return this.listItems.get(0).model;
        }
    }
    
    static class WeightedModel extends WeightedRandom.Item implements Comparable<WeightedModel>
    {
        protected final IBakedModel model;
        
        public WeightedModel(final IBakedModel modelIn, final int itemWeightIn) {
            super(itemWeightIn);
            this.model = modelIn;
        }
        
        @Override
        public int compareTo(final WeightedModel p_compareTo_1_) {
            return ComparisonChain.start().compare(p_compareTo_1_.itemWeight, this.itemWeight).result();
        }
        
        @Override
        public String toString() {
            return "MyWeighedRandomItem{weight=" + this.itemWeight + ", model=" + this.model + '}';
        }
    }
}
