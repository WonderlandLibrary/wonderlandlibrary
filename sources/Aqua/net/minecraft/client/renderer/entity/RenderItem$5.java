package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;

class RenderItem.5
implements ItemMeshDefinition {
    RenderItem.5() {
    }

    public ModelResourceLocation getModelLocation(ItemStack stack) {
        return ItemPotion.isSplash((int)stack.getMetadata()) ? new ModelResourceLocation("bottle_splash", "inventory") : new ModelResourceLocation("bottle_drinkable", "inventory");
    }
}
