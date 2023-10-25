package net.minecraft.client.resources.model;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.WeightedBakedModel;

public static class WeightedBakedModel.Builder {
    private List<WeightedBakedModel.MyWeighedRandomItem> listItems = Lists.newArrayList();

    public WeightedBakedModel.Builder add(IBakedModel p_177677_1_, int p_177677_2_) {
        this.listItems.add((Object)new WeightedBakedModel.MyWeighedRandomItem(p_177677_1_, p_177677_2_));
        return this;
    }

    public WeightedBakedModel build() {
        Collections.sort(this.listItems);
        return new WeightedBakedModel(this.listItems);
    }

    public IBakedModel first() {
        return ((WeightedBakedModel.MyWeighedRandomItem)this.listItems.get((int)0)).model;
    }
}
