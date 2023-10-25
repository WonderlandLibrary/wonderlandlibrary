package me.rhys.client.module.player;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.TickEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;

public class BoatCrafter extends Module {
  public BoatCrafter(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
  }
  
  @EventTarget
  public void onWorldTick(TickEvent event) {
    if (this.mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiCrafting) {
      int windowId = this.mc.thePlayer.openContainer.windowId;
      for (Slot inventorySlot : this.mc.thePlayer.inventoryContainer.inventorySlots) {
        if (inventorySlot.getHasStack() && 
          (inventorySlot.getStack()).stackSize >= 6 && 
          inventorySlot.getStack().getItem() instanceof ItemBlock && (
          (ItemBlock)inventorySlot.getStack().getItem()).getBlock() == Blocks.log) {
          this.mc.playerController.windowClick(windowId, inventorySlot.slotNumber + 1, 0, 0, (EntityPlayer)this.mc.thePlayer);
          this.mc.playerController.windowClick(windowId, 1, 0, 0, (EntityPlayer)this.mc.thePlayer);
          this.mc.playerController.windowClick(windowId, 0, 0, 1, (EntityPlayer)this.mc.thePlayer);
          return;
        } 
      } 
      for (Slot inventorySlot : this.mc.thePlayer.inventoryContainer.inventorySlots) {
        if (inventorySlot.getHasStack() && 
          (inventorySlot.getStack()).stackSize >= 6 && 
          inventorySlot.getStack().getItem() instanceof ItemBlock && (
          (ItemBlock)inventorySlot.getStack().getItem()).getBlock() == Blocks.planks) {
          this.mc.playerController.windowClick(windowId, inventorySlot.slotNumber + 1, 0, 0, (EntityPlayer)this.mc.thePlayer);
          this.mc.playerController.windowClick(windowId, 4, 1, 0, (EntityPlayer)this.mc.thePlayer);
          this.mc.playerController.windowClick(windowId, 7, 1, 0, (EntityPlayer)this.mc.thePlayer);
          this.mc.playerController.windowClick(windowId, 8, 1, 0, (EntityPlayer)this.mc.thePlayer);
          this.mc.playerController.windowClick(windowId, 9, 1, 0, (EntityPlayer)this.mc.thePlayer);
          this.mc.playerController.windowClick(windowId, 6, 1, 0, (EntityPlayer)this.mc.thePlayer);
          this.mc.playerController.windowClick(windowId, inventorySlot.slotNumber + 1, 0, 0, (EntityPlayer)this.mc.thePlayer);
          this.mc.playerController.windowClick(windowId, 0, 0, 1, (EntityPlayer)this.mc.thePlayer);
          for (Slot slot : this.mc.thePlayer.inventoryContainer.inventorySlots) {
            if (slot.getHasStack() && 
              (slot.getStack()).stackSize >= 6 && 
              slot.getStack().getItem() instanceof ItemBlock && (
              (ItemBlock)slot.getStack().getItem()).getBlock() == Blocks.planks) {
              this.mc.playerController.windowClick(windowId, slot.slotNumber + 1, 0, 0, (EntityPlayer)this.mc.thePlayer);
              this.mc.playerController.windowClick(windowId, 4, 1, 0, (EntityPlayer)this.mc.thePlayer);
              this.mc.playerController.windowClick(windowId, 7, 1, 0, (EntityPlayer)this.mc.thePlayer);
              this.mc.playerController.windowClick(windowId, 8, 1, 0, (EntityPlayer)this.mc.thePlayer);
              this.mc.playerController.windowClick(windowId, 9, 1, 0, (EntityPlayer)this.mc.thePlayer);
              this.mc.playerController.windowClick(windowId, 6, 1, 0, (EntityPlayer)this.mc.thePlayer);
              this.mc.playerController.windowClick(windowId, slot.slotNumber + 1, 0, 0, (EntityPlayer)this.mc.thePlayer);
              this.mc.playerController.windowClick(windowId, 0, 0, 1, (EntityPlayer)this.mc.thePlayer);
              this.mc.thePlayer.closeScreen();
              return;
            } 
          } 
          this.mc.thePlayer.closeScreen();
          return;
        } 
      } 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\BoatCrafter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */