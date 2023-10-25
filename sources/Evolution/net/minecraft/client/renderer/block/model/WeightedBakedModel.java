package net.minecraft.client.renderer.block.model;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandom;

public class WeightedBakedModel implements IBakedModel
{
    private final int totalWeight;
    private final List<WeightedModel> models;
    private final IBakedModel baseModel;

    public WeightedBakedModel(List<WeightedModel> modelsIn)
    {
        this.models = modelsIn;
        this.totalWeight = WeightedRandom.getTotalWeight(modelsIn);
        this.baseModel = (modelsIn.get(0)).model;
    }

    private IBakedModel getRandomModel(long p_188627_1_)
    {
        return ((WeightedModel)WeightedRandom.getRandomItem(this.models, Math.abs((int)p_188627_1_ >> 16) % this.totalWeight)).model;
    }

    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
    {
        return this.getRandomModel(rand).getQuads(state, side, rand);
    }

    public boolean isAmbientOcclusion()
    {
        return this.baseModel.isAmbientOcclusion();
    }

    public boolean isGui3d()
    {
        return this.baseModel.isGui3d();
    }

    public boolean isBuiltInRenderer()
    {
        return this.baseModel.isBuiltInRenderer();
    }

    public TextureAtlasSprite getParticleTexture()
    {
        return this.baseModel.getParticleTexture();
    }

    public ItemCameraTransforms getItemCameraTransforms()
    {
        return this.baseModel.getItemCameraTransforms();
    }

    public ItemOverrideList getOverrides()
    {
        return this.baseModel.getOverrides();
    }

    public static class Builder
    {
        private final List<WeightedModel> listItems = Lists.<WeightedModel>newArrayList();

        public Builder add(IBakedModel model, int weight)
        {
            this.listItems.add(new WeightedModel(model, weight));
            return this;
        }

        public WeightedBakedModel build()
        {
            Collections.sort(this.listItems);
            return new WeightedBakedModel(this.listItems);
        }

        public IBakedModel first()
        {
            return (this.listItems.get(0)).model;
        }
    }

    static class WeightedModel extends WeightedRandom.Item implements Comparable<WeightedModel>
    {
        protected final IBakedModel model;

        public WeightedModel(IBakedModel modelIn, int itemWeightIn)
        {
            super(itemWeightIn);
            this.model = modelIn;
        }

        public int compareTo(WeightedModel p_compareTo_1_)
        {
            return ComparisonChain.start().compare(p_compareTo_1_.itemWeight, this.itemWeight).result();
        }

        public String toString()
        {
            return "MyWeighedRandomItem{weight=" + this.itemWeight + ", model=" + this.model + '}';
        }
    }
}
