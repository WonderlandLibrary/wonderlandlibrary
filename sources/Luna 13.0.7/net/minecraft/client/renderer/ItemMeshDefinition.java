package net.minecraft.client.renderer;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;

public abstract interface ItemMeshDefinition
{
  public abstract ModelResourceLocation getModelLocation(ItemStack paramItemStack);
}
