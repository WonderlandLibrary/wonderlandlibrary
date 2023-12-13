package me.aidanmees.trivia.client.tools;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class InvUtils
{
  public static void swapShift(int slot)
  {
    Minecraft.getMinecraft();
    Minecraft.getMinecraft();
    Minecraft.getMinecraft();Minecraft.playerController.windowClick(Minecraft.thePlayer.inventoryContainer.windowId, slot, 0, 1, Minecraft.thePlayer);
  }
  
  public static void swap(int slot, int hotbarNum)
  {
    Minecraft.getMinecraft();
    Minecraft.getMinecraft();
    Minecraft.getMinecraft();Minecraft.playerController.windowClick(Minecraft.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, Minecraft.thePlayer);
  }
  
  public static int getPotFromInventory()
  {
    int pot = -1;
    for (int i = 1; i < 45; i++)
    {
      Minecraft.getMinecraft();
      if (Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack())
      {
        Minecraft.getMinecraft();ItemStack is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
        Item item = is.getItem();
        if ((item instanceof ItemPotion))
        {
          ItemPotion potion = (ItemPotion)item;
          if (potion.getEffects(is) != null) {
            for (Object o : potion.getEffects(is))
            {
              PotionEffect effect = (PotionEffect)o;
              if ((effect.getPotionID() == Potion.heal.id) && (ItemPotion.isSplash(is.getItemDamage()))) {
                pot = i;
              }
            }
          }
        }
      }
    }
    return pot;
  }
  
  public static int getBlockFromInventory()
  {
    int pot = -1;
    for (int i = 1; i < 45; i++)
    {
      Minecraft.getMinecraft();
      if (Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack())
      {
        Minecraft.getMinecraft();ItemStack is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
        Item item = is.getItem();
        if ((item instanceof ItemBlock))
        {
          ItemBlock potion = (ItemBlock)item;
          
          pot = i;
        }
      }
    }
    return pot;
  }
  
  public static int getPotsInInventory()
  {
    int counter = 0;
    for (int i = 1; i < 45; i++)
    {
      Minecraft.getMinecraft();
      if (Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack())
      {
        Minecraft.getMinecraft();ItemStack is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
        Item item = is.getItem();
        if ((item instanceof ItemPotion))
        {
          ItemPotion potion = (ItemPotion)item;
          if (potion.getEffects(is) != null) {
            for (Object o : potion.getEffects(is))
            {
              PotionEffect effect = (PotionEffect)o;
              if ((effect.getPotionID() == Potion.heal.id) && (ItemPotion.isSplash(is.getItemDamage()))) {
                counter++;
              }
            }
          }
        }
      }
    }
    return counter;
  }
}
