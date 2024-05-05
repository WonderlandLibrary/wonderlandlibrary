package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;

class RenderItem.8
implements ItemMeshDefinition {
    RenderItem.8() {
    }

    public ModelResourceLocation getModelLocation(ItemStack stack) {
        return new ModelResourceLocation("enchanted_book", "inventory");
    }
}
