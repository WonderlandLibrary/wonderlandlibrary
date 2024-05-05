package net.minecraft.client.resources.model;

import com.google.common.collect.ComparisonChain;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandom;

static class WeightedBakedModel.MyWeighedRandomItem
extends WeightedRandom.Item
implements Comparable<WeightedBakedModel.MyWeighedRandomItem> {
    protected final IBakedModel model;

    public WeightedBakedModel.MyWeighedRandomItem(IBakedModel p_i46072_1_, int p_i46072_2_) {
        super(p_i46072_2_);
        this.model = p_i46072_1_;
    }

    public int compareTo(WeightedBakedModel.MyWeighedRandomItem p_compareTo_1_) {
        return ComparisonChain.start().compare(p_compareTo_1_.itemWeight, this.itemWeight).compare(this.getCountQuads(), p_compareTo_1_.getCountQuads()).result();
    }

    protected int getCountQuads() {
        int i = this.model.getGeneralQuads().size();
        for (EnumFacing enumfacing : EnumFacing.values()) {
            i += this.model.getFaceQuads(enumfacing).size();
        }
        return i;
    }

    public String toString() {
        return "MyWeighedRandomItem{weight=" + this.itemWeight + ", model=" + this.model + '}';
    }
}
