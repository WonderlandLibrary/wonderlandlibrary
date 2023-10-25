package me.rhys.client.module.combat;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerUpdateEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.base.util.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

public class AutoSoup extends Module {
  @Name("Health")
  @Clamp(min = 0.5D, max = 10.0D)
  private double health;
  
  @Name("Delay")
  @Clamp(min = 0.0D, max = 1000.0D)
  private double delay;
  
  private Timer time;
  
  public AutoSoup(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    this.health = 5.0D;
    this.delay = 300.0D;
    this.time = new Timer();
  }
  
  @EventTarget
  private void onPostUpdate(PlayerUpdateEvent event) {
    int soupSlot = getSoupFromInventory();
    if (this.mc.thePlayer.getHealth() < this.health * 2.0D && this.time.hasReached((float)this.delay) && soupSlot != -1) {
      int prevSlot = this.mc.thePlayer.inventory.currentItem;
      if (soupSlot < 9) {
        this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C09PacketHeldItemChange(soupSlot));
        this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.getCurrentItem()));
        this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C09PacketHeldItemChange(prevSlot));
        this.mc.playerController.syncCurrentPlayItem();
        this.mc.thePlayer.inventory.currentItem = prevSlot;
      } else {
        swap(soupSlot, this.mc.thePlayer.inventory.currentItem + ((this.mc.thePlayer.inventory.currentItem < 8) ? 1 : -1));
        this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C09PacketHeldItemChange(this.mc.thePlayer.inventory.currentItem + ((this.mc.thePlayer.inventory.currentItem < 8) ? 1 : -1)));
        this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.getCurrentItem()));
        this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C09PacketHeldItemChange(prevSlot));
      } 
      this.time.reset();
    } 
  }
  
  protected void swap(int slot, int hotbarNum) {
    this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, (EntityPlayer)this.mc.thePlayer);
  }
  
  private int getSoupFromInventory() {
    int i = 0;
    while (i < 36) {
      Item item;
      ItemStack is;
      if (this.mc.thePlayer.inventory.mainInventory[i] != null && Item.getIdFromItem(item = (is = this.mc.thePlayer.inventory.mainInventory[i]).getItem()) == 282)
        return i; 
      i++;
    } 
    return -1;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\combat\AutoSoup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */