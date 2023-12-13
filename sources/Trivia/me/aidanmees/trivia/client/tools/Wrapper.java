package me.aidanmees.trivia.client.tools;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Wrapper {


public static int findHotbarItem(int itemId, int mode)
{
  if (mode == 0) {
    for (int slot = 36; slot <= 44; slot++)
    {
      ItemStack item = Minecraft.thePlayer.inventoryContainer.getSlot(slot).getStack();
      if ((item != null) && 
        (Item.getIdFromItem(item.getItem()) == itemId)) {
        return slot;
      }
    }
  } else if (mode == 1) {
    for (int slot = 36; slot <= 44; slot++)
    {
      ItemStack item = Minecraft.thePlayer.inventoryContainer.getSlot(slot).getStack();
      if ((item != null) && 
        (Item.getIdFromItem(item.getItem()) == itemId)) {
        return slot - 36;
      }
    }
  }
  return -1;
}
}
