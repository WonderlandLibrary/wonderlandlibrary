package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;

class RenderItem.6
implements ItemMeshDefinition {
    RenderItem.6() {
    }

    public ModelResourceLocation getModelLocation(ItemStack stack) {
        return new ModelResourceLocation("spawn_egg", "inventory");
    }
}
