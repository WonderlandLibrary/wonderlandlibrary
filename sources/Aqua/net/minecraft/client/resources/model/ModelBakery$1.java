package net.minecraft.client.resources.model;

import java.util.Comparator;
import net.minecraft.client.resources.model.ModelResourceLocation;

class ModelBakery.1
implements Comparator<ModelResourceLocation> {
    ModelBakery.1() {
    }

    public int compare(ModelResourceLocation p_compare_1_, ModelResourceLocation p_compare_2_) {
        return p_compare_1_.toString().compareTo(p_compare_2_.toString());
    }
}
