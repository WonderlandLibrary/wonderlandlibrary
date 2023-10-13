/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ComparisonChain
 *  com.google.common.collect.Lists
 */
package net.minecraft.client.resources.model;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandom;

public class WeightedBakedModel
implements IBakedModel {
    private final int totalWeight;
    private final List models;
    private final IBakedModel baseModel;
    private static final String __OBFID = "CL_00002384";

    public WeightedBakedModel(List p_i46073_1_) {
        this.models = p_i46073_1_;
        this.totalWeight = WeightedRandom.getTotalWeight(p_i46073_1_);
        this.baseModel = ((MyWeighedRandomItem)p_i46073_1_.get((int)0)).model;
    }

    @Override
    public List func_177551_a(EnumFacing p_177551_1_) {
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

    public IBakedModel func_177564_a(long p_177564_1_) {
        return ((MyWeighedRandomItem)WeightedRandom.func_180166_a((Collection)this.models, (int)(Math.abs((int)((int)p_177564_1_ >> 16)) % this.totalWeight))).model;
    }

    public static class Builder {
        private List field_177678_a = Lists.newArrayList();
        private static final String __OBFID = "CL_00002383";

        public Builder add(IBakedModel p_177677_1_, int p_177677_2_) {
            this.field_177678_a.add(new MyWeighedRandomItem(p_177677_1_, p_177677_2_));
            return this;
        }

        public WeightedBakedModel build() {
            Collections.sort(this.field_177678_a);
            return new WeightedBakedModel(this.field_177678_a);
        }

        public IBakedModel first() {
            return ((MyWeighedRandomItem)this.field_177678_a.get((int)0)).model;
        }
    }

    static class MyWeighedRandomItem
    extends WeightedRandom.Item
    implements Comparable {
        protected final IBakedModel model;
        private static final String __OBFID = "CL_00002382";

        public MyWeighedRandomItem(IBakedModel p_i46072_1_, int p_i46072_2_) {
            super(p_i46072_2_);
            this.model = p_i46072_1_;
        }

        public int func_177634_a(MyWeighedRandomItem p_177634_1_) {
            return ComparisonChain.start().compare(p_177634_1_.itemWeight, this.itemWeight).compare(this.func_177635_a(), p_177634_1_.func_177635_a()).result();
        }

        protected int func_177635_a() {
            int var1 = this.model.func_177550_a().size();
            for (EnumFacing var5 : EnumFacing.values()) {
                var1 += this.model.func_177551_a(var5).size();
            }
            return var1;
        }

        public String toString() {
            return "MyWeighedRandomItem{weight=" + this.itemWeight + ", model=" + this.model + '}';
        }

        public int compareTo(Object p_compareTo_1_) {
            return this.func_177634_a((MyWeighedRandomItem)p_compareTo_1_);
        }
    }
}

