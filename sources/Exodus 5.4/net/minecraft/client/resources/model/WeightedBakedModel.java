/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ComparisonChain
 *  com.google.common.collect.Lists
 */
package net.minecraft.client.resources.model;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandom;

public class WeightedBakedModel
implements IBakedModel {
    private final List<MyWeighedRandomItem> models;
    private final int totalWeight;
    private final IBakedModel baseModel;

    @Override
    public boolean isGui3d() {
        return this.baseModel.isGui3d();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return this.baseModel.getParticleTexture();
    }

    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing enumFacing) {
        return this.baseModel.getFaceQuads(enumFacing);
    }

    public IBakedModel getAlternativeModel(long l) {
        return WeightedRandom.getRandomItem(this.models, (int)(Math.abs((int)((int)l >> 16)) % this.totalWeight)).model;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return this.baseModel.isBuiltInRenderer();
    }

    public WeightedBakedModel(List<MyWeighedRandomItem> list) {
        this.models = list;
        this.totalWeight = WeightedRandom.getTotalWeight(list);
        this.baseModel = list.get((int)0).model;
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        return this.baseModel.getGeneralQuads();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return this.baseModel.getItemCameraTransforms();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return this.baseModel.isAmbientOcclusion();
    }

    public static class Builder {
        private List<MyWeighedRandomItem> listItems = Lists.newArrayList();

        public Builder add(IBakedModel iBakedModel, int n) {
            this.listItems.add(new MyWeighedRandomItem(iBakedModel, n));
            return this;
        }

        public WeightedBakedModel build() {
            Collections.sort(this.listItems);
            return new WeightedBakedModel(this.listItems);
        }

        public IBakedModel first() {
            return this.listItems.get((int)0).model;
        }
    }

    static class MyWeighedRandomItem
    extends WeightedRandom.Item
    implements Comparable<MyWeighedRandomItem> {
        protected final IBakedModel model;

        public String toString() {
            return "MyWeighedRandomItem{weight=" + this.itemWeight + ", model=" + this.model + '}';
        }

        protected int getCountQuads() {
            int n = this.model.getGeneralQuads().size();
            EnumFacing[] enumFacingArray = EnumFacing.values();
            int n2 = enumFacingArray.length;
            int n3 = 0;
            while (n3 < n2) {
                EnumFacing enumFacing = enumFacingArray[n3];
                n += this.model.getFaceQuads(enumFacing).size();
                ++n3;
            }
            return n;
        }

        @Override
        public int compareTo(MyWeighedRandomItem myWeighedRandomItem) {
            return ComparisonChain.start().compare(myWeighedRandomItem.itemWeight, this.itemWeight).compare(this.getCountQuads(), myWeighedRandomItem.getCountQuads()).result();
        }

        public MyWeighedRandomItem(IBakedModel iBakedModel, int n) {
            super(n);
            this.model = iBakedModel;
        }
    }
}

