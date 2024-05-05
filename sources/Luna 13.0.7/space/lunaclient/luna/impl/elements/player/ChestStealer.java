package space.lunaclient.luna.impl.elements.player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S30PacketWindowItems;
import space.lunaclient.luna.api.element.Category;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.element.ElementInfo;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.impl.events.EventUpdate;

@ElementInfo(name="ChestStealer", category=Category.PLAYER)
public class ChestStealer
  extends Element
{
  public static S30PacketWindowItems packet;
  private int delay;
  
  public ChestStealer() {}
  
  @EventRegister
  public void onUpdate(EventUpdate event)
  {
    this.delay += 1;
    if ((mc.currentScreen instanceof GuiChest))
    {
      GuiChest chest = (GuiChest)mc.currentScreen;
      if (isChestEmpty(chest))
      {
        Minecraft.thePlayer.closeScreen();
        packet = null;
      }
      for (int index = 0; index < chest.lowerChestInventory.getSizeInventory(); index++)
      {
        ItemStack stack = chest.lowerChestInventory.getStackInSlot(index);
        if ((stack != null) && (this.delay > 1.9D))
        {
          Minecraft.playerController.windowClick(chest.inventorySlots.windowId, index, 0, 1, Minecraft.thePlayer);
          this.delay = 0;
        }
      }
    }
  }
  
  private boolean isChestEmpty(GuiChest chest)
  {
    for (int index = 0; index <= chest.lowerChestInventory.getSizeInventory(); index++)
    {
      ItemStack stack = chest.lowerChestInventory.getStackInSlot(index);
      if (stack != null) {
        return false;
      }
    }
    return true;
  }
}
