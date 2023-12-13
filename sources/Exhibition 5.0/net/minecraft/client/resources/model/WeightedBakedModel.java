// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.resources.model;

import com.google.common.collect.ComparisonChain;
import java.util.Collections;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import java.util.Collection;
import net.minecraft.util.WeightedRandom;
import java.util.List;

public class WeightedBakedModel implements IBakedModel
{
    private final int totalWeight;
    private final List models;
    private final IBakedModel baseModel;
    private static final String __OBFID = "CL_00002384";
    
    public WeightedBakedModel(final List p_i46073_1_) {
        this.models = p_i46073_1_;
        this.totalWeight = WeightedRandom.getTotalWeight(p_i46073_1_);
        this.baseModel = p_i46073_1_.get(0).model;
    }
    
    @Override
    public List func_177551_a(final EnumFacing p_177551_1_) {
        return this.baseModel.func_177551_a(p_177551_1_);
    }
    
    @Override
    public List func_177550_a() {
        return this.baseModel.func_177550_a();
    }
    
    @Override
    public boolean isGui3d() {
        return this.baseModel.isGui3d();
    }
    
    @Override
    public boolean isAmbientOcclusionEnabled() {
        return this.baseModel.isAmbientOcclusionEnabled();
    }
    
    @Override
    public boolean isBuiltInRenderer() {
        return this.baseModel.isBuiltInRenderer();
    }
    
    @Override
    public TextureAtlasSprite getTexture() {
        return this.baseModel.getTexture();
    }
    
    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return this.baseModel.getItemCameraTransforms();
    }
    
    public IBakedModel func_177564_a(final long p_177564_1_) {
        return ((MyWeighedRandomItem)WeightedRandom.func_180166_a(this.models, Math.abs((int)p_177564_1_ >> 16) % this.totalWeight)).model;
    }
    
    public static class Builder
    {
        private List field_177678_a;
        private static final String __OBFID = "CL_00002383";
        
        public Builder() {
            this.field_177678_a = Lists.newArrayList();
        }
        
        public Builder add(final IBakedModel p_177677_1_, final int p_177677_2_) {
            this.field_177678_a.add(new MyWeighedRandomItem(p_177677_1_, p_177677_2_));
            return this;
        }
        
        public WeightedBakedModel build() {
            Collections.sort((List<Comparable>)this.field_177678_a);
            return new WeightedBakedModel(this.field_177678_a);
        }
        
        public IBakedModel first() {
            return this.field_177678_a.get(0).model;
        }
    }
    
    static class MyWeighedRandomItem extends WeightedRandom.Item implements Comparable
    {
        protected final IBakedModel model;
        private static final String __OBFID = "CL_00002382";
        
        public MyWeighedRandomItem(final IBakedModel p_i46072_1_, final int p_i46072_2_) {
            super(p_i46072_2_);
            this.model = p_i46072_1_;
        }
        
        public int func_177634_a(final MyWeighedRandomItem p_177634_1_) {
            return ComparisonChain.start().compare(p_177634_1_.itemWeight, this.itemWeight).compare(this.func_177635_a(), p_177634_1_.func_177635_a()).result();
        }
        
        protected int func_177635_a() {
            int var1 = this.model.func_177550_a().size();
            for (final EnumFacing var5 : EnumFacing.values()) {
                var1 += this.model.func_177551_a(var5).size();
            }
            return var1;
        }
        
        @Override
        public String toString() {
            return "MyWeighedRandomItem{weight=" + this.itemWeight + ", model=" + this.model + '}';
        }
        
        @Override
        public int compareTo(final Object p_compareTo_1_) {
            return this.func_177634_a((MyWeighedRandomItem)p_compareTo_1_);
        }
    }
}
