package space.lunaclient.luna.impl.elements.player;

import com.google.common.collect.Multimap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
import space.lunaclient.luna.api.element.Category;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.element.ElementInfo;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.api.setting.ModeSetting;
import space.lunaclient.luna.api.setting.Setting;
import space.lunaclient.luna.impl.events.EventUpdate;

@ElementInfo(name="InvCleaner", category=Category.PLAYER)
public class InvCleaner
  extends Element
{
  @ModeSetting(name="Mode", currentOption="Normal", options={"Normal", "OpenINV"}, locked=false)
  public static Setting mode;
  
  public InvCleaner() {}
  
  @EventRegister
  public void onUpdate(EventUpdate eventUpdate)
  {
    if (isToggled())
    {
      if (!getMode().contains(mode.getValString()))
      {
        toggle();
        toggle();
      }
      if (mode.getValString().equalsIgnoreCase("Normal"))
      {
        setMode(mode.getValString());
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(Minecraft.thePlayer, C0BPacketEntityAction.Action.OPEN_INVENTORY));
        if (Minecraft.thePlayer.isUsingItem()) {
          return;
        }
        CopyOnWriteArrayList<Integer> uselessItems = new CopyOnWriteArrayList();
        
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(Minecraft.thePlayer, C0BPacketEntityAction.Action.OPEN_INVENTORY));
        for (int o = 0; o < 45; o++) {
          if (Minecraft.thePlayer.inventoryContainer.getSlot(o).getHasStack())
          {
            ItemStack item = Minecraft.thePlayer.inventoryContainer.getSlot(o).getStack();
            if ((Minecraft.thePlayer.inventory.armorItemInSlot(0) != item) && 
              (Minecraft.thePlayer.inventory.armorItemInSlot(1) != item) && 
              (Minecraft.thePlayer.inventory.armorItemInSlot(2) != item) && 
              (Minecraft.thePlayer.inventory.armorItemInSlot(3) != item)) {
              if ((item != null) && (item.getItem() != null) && (Item.getIdFromItem(item.getItem()) != 0) && 
                (!stackIsUseful(o))) {
                uselessItems.add(Integer.valueOf(o));
              }
            }
          }
        }
        if ((!uselessItems.isEmpty()) && 
          (Minecraft.thePlayer.ticksExisted % 2 == 0))
        {
          Minecraft.playerController.windowClick(Minecraft.thePlayer.inventoryContainer.windowId, 
            ((Integer)uselessItems.get(0)).intValue(), 1, 4, Minecraft.thePlayer);
          
          uselessItems.remove(0);
        }
      }
      else if (mode.getValString().equalsIgnoreCase("OpenINV"))
      {
        setMode(mode.getValString());
        if (Minecraft.thePlayer.isUsingItem()) {
          return;
        }
        if (((Minecraft.getMinecraft().currentScreen instanceof GuiInventory)) || ((Minecraft.getMinecraft().currentScreen instanceof GuiContainer)))
        {
          CopyOnWriteArrayList<Integer> uselessItems = new CopyOnWriteArrayList();
          for (int o = 0; o < 45; o++) {
            if (Minecraft.thePlayer.inventoryContainer.getSlot(o).getHasStack())
            {
              ItemStack item = Minecraft.thePlayer.inventoryContainer.getSlot(o).getStack();
              if ((Minecraft.thePlayer.inventory.armorItemInSlot(0) != item) && 
                (Minecraft.thePlayer.inventory.armorItemInSlot(1) != item) && 
                (Minecraft.thePlayer.inventory.armorItemInSlot(2) != item) && 
                (Minecraft.thePlayer.inventory.armorItemInSlot(3) != item)) {
                if ((item != null) && (item.getItem() != null) && (Item.getIdFromItem(item.getItem()) != 0) && 
                  (!stackIsUseful(o))) {
                  uselessItems.add(Integer.valueOf(o));
                }
              }
            }
          }
          if ((!uselessItems.isEmpty()) && 
            (Minecraft.thePlayer.ticksExisted % 2 == 0))
          {
            Minecraft.playerController.windowClick(Minecraft.thePlayer.inventoryContainer.windowId, 
              ((Integer)uselessItems.get(0)).intValue(), 1, 4, Minecraft.thePlayer);
            
            uselessItems.remove(0);
          }
        }
      }
    }
  }
  
  private boolean stackIsUseful(int i)
  {
    ItemStack itemStack = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
    
    boolean hasAlreadyOrBetter = false;
    if (((itemStack.getItem() instanceof ItemSword)) || ((itemStack.getItem() instanceof ItemPickaxe)) || 
      ((itemStack.getItem() instanceof ItemAxe)))
    {
      int o = 0;
      while (o < 45) {
        if (o == i)
        {
          o++;
        }
        else
        {
          if (Minecraft.thePlayer.inventoryContainer.getSlot(o).getHasStack())
          {
            ItemStack item = Minecraft.thePlayer.inventoryContainer.getSlot(o).getStack();
            if (((item != null) && ((item.getItem() instanceof ItemSword))) || ((((ItemStack)Objects.requireNonNull(item)).getItem() instanceof ItemAxe)) || 
              ((item.getItem() instanceof ItemPickaxe)))
            {
              float damageFound = getItemDamage(itemStack);
              damageFound += EnchantmentHelper.func_152377_a(itemStack, EnumCreatureAttribute.UNDEFINED);
              
              float damageCurrent = getItemDamage(item);
              damageCurrent += EnchantmentHelper.func_152377_a(item, EnumCreatureAttribute.UNDEFINED);
              if (damageCurrent > damageFound)
              {
                hasAlreadyOrBetter = true;
                break;
              }
            }
          }
          o++;
        }
      }
    }
    else if ((itemStack.getItem() instanceof ItemArmor))
    {
      for (int o = 0; o < 45; o++) {
        if (i != o) {
          if (Minecraft.thePlayer.inventoryContainer.getSlot(o).getHasStack())
          {
            ItemStack item = Minecraft.thePlayer.inventoryContainer.getSlot(o).getStack();
            if ((item != null) && ((item.getItem() instanceof ItemArmor)))
            {
              List<Integer> helmet = Arrays.asList(new Integer[] { Integer.valueOf(298), Integer.valueOf(314), Integer.valueOf(302), Integer.valueOf(306), Integer.valueOf(310) });
              List<Integer> chestPlate = Arrays.asList(new Integer[] { Integer.valueOf(299), Integer.valueOf(315), Integer.valueOf(303), Integer.valueOf(307), Integer.valueOf(311) });
              List<Integer> leggings = Arrays.asList(new Integer[] { Integer.valueOf(300), Integer.valueOf(316), Integer.valueOf(304), Integer.valueOf(308), Integer.valueOf(312) });
              List<Integer> boots = Arrays.asList(new Integer[] { Integer.valueOf(301), Integer.valueOf(317), Integer.valueOf(305), Integer.valueOf(309), Integer.valueOf(313) });
              if ((helmet.contains(Integer.valueOf(Item.getIdFromItem(item.getItem())))) && 
                (helmet.contains(Integer.valueOf(Item.getIdFromItem(itemStack.getItem())))))
              {
                if (helmet.indexOf(Integer.valueOf(Item.getIdFromItem(itemStack.getItem()))) < helmet.indexOf(Integer.valueOf(Item.getIdFromItem(item.getItem()))))
                {
                  hasAlreadyOrBetter = true;
                  break;
                }
              }
              else if ((chestPlate.contains(Integer.valueOf(Item.getIdFromItem(item.getItem())))) && 
                (chestPlate.contains(Integer.valueOf(Item.getIdFromItem(itemStack.getItem())))))
              {
                if (chestPlate.indexOf(Integer.valueOf(Item.getIdFromItem(itemStack.getItem()))) < chestPlate.indexOf(Integer.valueOf(Item.getIdFromItem(item.getItem()))))
                {
                  hasAlreadyOrBetter = true;
                  break;
                }
              }
              else if ((leggings.contains(Integer.valueOf(Item.getIdFromItem(item.getItem())))) && 
                (leggings.contains(Integer.valueOf(Item.getIdFromItem(itemStack.getItem())))))
              {
                if (leggings.indexOf(Integer.valueOf(Item.getIdFromItem(itemStack.getItem()))) < leggings.indexOf(Integer.valueOf(Item.getIdFromItem(item.getItem()))))
                {
                  hasAlreadyOrBetter = true;
                  break;
                }
              }
              else if ((boots.contains(Integer.valueOf(Item.getIdFromItem(item.getItem())))) && 
                (boots.contains(Integer.valueOf(Item.getIdFromItem(itemStack.getItem()))))) {
                if (boots.indexOf(Integer.valueOf(Item.getIdFromItem(itemStack.getItem()))) < boots.indexOf(Integer.valueOf(Item.getIdFromItem(item.getItem()))))
                {
                  hasAlreadyOrBetter = true;
                  break;
                }
              }
            }
          }
        }
      }
    }
    for (int o = 0; o < 45; o++) {
      if (i != o) {
        if (Minecraft.thePlayer.inventoryContainer.getSlot(o).getHasStack())
        {
          ItemStack item = Minecraft.thePlayer.inventoryContainer.getSlot(o).getStack();
          if ((item != null) && (((item.getItem() instanceof ItemSword)) || ((item.getItem() instanceof ItemAxe)) || ((item.getItem() instanceof ItemBow)) || ((item.getItem() instanceof ItemFishingRod)) || ((item.getItem() instanceof ItemArmor)) || ((item.getItem() instanceof ItemPickaxe)) || (Item.getIdFromItem(item.getItem()) == 346)) && 
            (Item.getIdFromItem(itemStack.getItem()) == Item.getIdFromItem(item.getItem())))
          {
            hasAlreadyOrBetter = true;
            break;
          }
        }
      }
    }
    if (Item.getIdFromItem(itemStack.getItem()) == 367) {
      return false;
    }
    if (Item.getIdFromItem(itemStack.getItem()) == 259) {
      return true;
    }
    if (Item.getIdFromItem(itemStack.getItem()) == 262) {
      return true;
    }
    if (Item.getIdFromItem(itemStack.getItem()) == 264) {
      return true;
    }
    if (Item.getIdFromItem(itemStack.getItem()) == 265) {
      return true;
    }
    if (Item.getIdFromItem(itemStack.getItem()) == 336) {
      return true;
    }
    if (Item.getIdFromItem(itemStack.getItem()) == 266) {
      return true;
    }
    if (Item.getIdFromItem(itemStack.getItem()) == 345) {
      return false;
    }
    if (Item.getIdFromItem(itemStack.getItem()) == 261) {
      return false;
    }
    if (Item.getIdFromItem(itemStack.getItem()) == 262) {
      return false;
    }
    if (((Item.getIdFromItem(itemStack.getItem()) == 54 ? 1 : 0) | (Item.getIdFromItem(itemStack.getItem()) == 54 ? 1 : 0)) != 0) {
      return false;
    }
    if (itemStack.hasDisplayName()) {
      return true;
    }
    if (hasAlreadyOrBetter) {
      return false;
    }
    if ((itemStack.getItem() instanceof ItemArmor)) {
      return true;
    }
    if ((itemStack.getItem() instanceof ItemAxe)) {
      return true;
    }
    if ((itemStack.getItem() instanceof ItemBow)) {
      return true;
    }
    if (Item.getIdFromItem(itemStack.getItem()) == 345) {
      return false;
    }
    if ((itemStack.getItem() instanceof ItemSword)) {
      return true;
    }
    if ((itemStack.getItem() instanceof ItemPotion)) {
      return true;
    }
    if ((itemStack.getItem() instanceof ItemFlintAndSteel)) {
      return true;
    }
    if ((itemStack.getItem() instanceof ItemEnderPearl)) {
      return true;
    }
    if ((itemStack.getItem() instanceof ItemBlock)) {
      return true;
    }
    if ((itemStack.getItem() instanceof ItemFood)) {
      return true;
    }
    return itemStack.getItem() instanceof ItemPickaxe;
  }
  
  private float getItemDamage(ItemStack itemStack)
  {
    Multimap multimap = itemStack.getAttributeModifiers();
    if (!multimap.isEmpty())
    {
      Iterator iterator = multimap.entries().iterator();
      if (iterator.hasNext())
      {
        Map.Entry entry = (Map.Entry)iterator.next();
        AttributeModifier attributeModifier = (AttributeModifier)entry.getValue();
        double damage;
        double damage;
        if ((attributeModifier.getOperation() != 1) && (attributeModifier.getOperation() != 2)) {
          damage = attributeModifier.getAmount();
        } else {
          damage = attributeModifier.getAmount() * 100.0D;
        }
        if (attributeModifier.getAmount() > 1.0D) {
          return 1.0F + (float)damage;
        }
        return 1.0F;
      }
    }
    return 1.0F;
  }
}
