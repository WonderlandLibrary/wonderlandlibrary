package me.rhys.client.module.combat;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerUpdateEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.base.util.Timer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import org.lwjgl.input.Mouse;

public class AutoGapple extends Module {
  private boolean eatingApple;
  
  private int switched;
  
  public AutoGapple(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    this.switched = -1;
    this.timer = new Timer();
    this.eatApples = true;
    this.eatHeads = true;
    this.health = 8;
    this.delay = 300;
  }
  
  public static boolean doingStuff = false;
  
  private final Timer timer;
  
  @Name("Apples")
  private boolean eatApples;
  
  @Name("Hypixel Heads")
  private boolean eatHeads;
  
  @Name("Health")
  @Clamp(min = 1.0D, max = 20.0D)
  private int health;
  
  @Name("Delay")
  @Clamp(min = 25.0D, max = 1000.0D)
  private int delay;
  
  public void onEnable() {
    this.eatingApple = doingStuff = false;
    this.switched = -1;
    this.timer.reset();
    super.onEnable();
  }
  
  public void onDisable() {
    doingStuff = false;
    if (this.eatingApple) {
      repairItemPress();
      repairItemSwitch();
    } 
    super.onDisable();
  }
  
  private void repairItemPress() {
    if ((getMc()).gameSettings != null) {
      KeyBinding keyBindUseItem = (getMc()).gameSettings.keyBindUseItem;
      if (keyBindUseItem != null)
        keyBindUseItem.pressed = false; 
    } 
  }
  
  @EventTarget
  public void onUpdate(PlayerUpdateEvent event) {
    if ((getMc()).thePlayer == null)
      return; 
    InventoryPlayer inventory = (getMc()).thePlayer.inventory;
    if (inventory == null)
      return; 
    doingStuff = false;
    if (!Mouse.isButtonDown(0) && !Mouse.isButtonDown(1)) {
      KeyBinding useItem = (getMc()).gameSettings.keyBindUseItem;
      if (!this.timer.hasReached(this.delay)) {
        this.eatingApple = false;
        repairItemPress();
        repairItemSwitch();
        return;
      } 
      if ((getMc()).thePlayer.capabilities.isCreativeMode || (getMc()).thePlayer.isPotionActive(Potion.regeneration) || (getMc()).thePlayer.getHealth() >= this.health) {
        this.timer.reset();
        if (this.eatingApple) {
          this.eatingApple = false;
          repairItemPress();
          repairItemSwitch();
        } 
        return;
      } 
      for (int i = 0; i < 2; i++) {
        int slot;
        boolean doEatHeads = (i != 0);
        if (doEatHeads) {
          if (!this.eatHeads)
            continue; 
        } else if (!this.eatApples) {
          this.eatingApple = false;
          repairItemPress();
          repairItemSwitch();
          continue;
        } 
        if (doEatHeads) {
          slot = getItemFromHotbar(397);
        } else {
          slot = getItemFromHotbar(322);
        } 
        if (slot == -1)
          continue; 
        int tempSlot = inventory.currentItem;
        doingStuff = true;
        if (doEatHeads) {
          (getMc()).thePlayer.sendQueue.addToSendQueue((Packet)new C09PacketHeldItemChange(slot));
          (getMc()).thePlayer.sendQueue.addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(inventory.getCurrentItem()));
          (getMc()).thePlayer.sendQueue.addToSendQueue((Packet)new C09PacketHeldItemChange(tempSlot));
          this.timer.reset();
        } else {
          inventory.currentItem = slot;
          useItem.pressed = true;
          if (this.eatingApple)
            continue; 
          this.eatingApple = true;
          this.switched = tempSlot;
        } 
        this.mc.thePlayer.sendMessage(String.format("Automatically ate a %s", new Object[] { doEatHeads ? "player head" : "golden apple" }));
        continue;
      } 
    } 
  }
  
  private void repairItemSwitch() {
    EntityPlayerSP p = (getMc()).thePlayer;
    if (p == null)
      return; 
    InventoryPlayer inventory = p.inventory;
    if (inventory == null)
      return; 
    int switched = this.switched;
    if (switched == -1)
      return; 
    inventory.currentItem = switched;
    switched = -1;
    this.switched = switched;
  }
  
  private int getItemFromHotbar(int id) {
    for (int i = 0; i < 9; i++) {
      if ((getMc()).thePlayer.inventory.mainInventory[i] != null) {
        ItemStack is = (getMc()).thePlayer.inventory.mainInventory[i];
        Item item = is.getItem();
        if (Item.getIdFromItem(item) == id)
          return i; 
      } 
    } 
    return -1;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\combat\AutoGapple.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */