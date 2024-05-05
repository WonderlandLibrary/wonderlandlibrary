package space.lunaclient.luna.impl.elements.player.autoarmor.structure;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.impl.events.EventUpdate;
import space.lunaclient.luna.util.Timer;

public class OpenINV
{
  private Timer delayTimer = new Timer();
  private int delay;
  
  public OpenINV() {}
  
  @EventRegister
  public void onUpdate(EventUpdate e)
  {
    if (!(Element.mc.currentScreen instanceof GuiInventory)) {
      return;
    }
    this.delay += 1;
    int[] bestArmor1 = new int[4];
    for (int i = 0; i < bestArmor1.length; i++) {
      bestArmor1[i] = -1;
    }
    for (int i = 0; i < 36; i++)
    {
      ItemStack itemstack = Minecraft.thePlayer.inventory.getStackInSlot(i);
      if ((itemstack != null) && ((itemstack.getItem() instanceof ItemArmor)))
      {
        ItemArmor armor = (ItemArmor)itemstack.getItem();
        if (armor.damageReduceAmount > bestArmor1[(3 - armor.armorType)]) {
          bestArmor1[(3 - armor.armorType)] = i;
        }
      }
    }
    for (int i = 0; i < 4; i++)
    {
      ItemStack itemstack = Minecraft.thePlayer.inventory.armorItemInSlot(i);
      ItemArmor currentArmor;
      ItemArmor currentArmor;
      if ((itemstack != null) && ((itemstack.getItem() instanceof ItemArmor))) {
        currentArmor = (ItemArmor)itemstack.getItem();
      } else {
        currentArmor = null;
      }
      ItemArmor bestArmor;
      try
      {
        bestArmor = (ItemArmor)Minecraft.thePlayer.inventory.getStackInSlot(bestArmor1[i]).getItem();
      }
      catch (Exception exception)
      {
        ItemArmor bestArmor;
        bestArmor = null;
      }
      if ((bestArmor != null) && ((currentArmor == null) || (bestArmor.damageReduceAmount > currentArmor.damageReduceAmount))) {
        if (((Minecraft.thePlayer.inventory.getFirstEmptyStack() != -1) || (currentArmor == null)) && 
          (this.delay >= 4))
        {
          Minecraft.playerController.windowClick(0, 8 - i, 0, 1, Minecraft.thePlayer);
          Minecraft.playerController.windowClick(0, bestArmor1[i] < 9 ? 36 + bestArmor1[i] : bestArmor1[i], 0, 1, Minecraft.thePlayer);
          
          this.delay = 0;
        }
      }
    }
  }
}
