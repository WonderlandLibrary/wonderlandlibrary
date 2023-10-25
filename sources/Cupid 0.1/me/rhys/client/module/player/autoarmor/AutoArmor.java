package me.rhys.client.module.player.autoarmor;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.TickEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.base.util.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class AutoArmor extends Module {
  long lastDelay = 0L;
  
  public Timer timer = new Timer();
  
  private Timer glitchFixer = new Timer();
  
  @Name("Only INV")
  public boolean checkInv = false;
  
  @Name("Delay")
  @Clamp(min = 0.0D, max = 200.0D)
  public float delay = 10.0F;
  
  public static boolean isDone = false;
  
  public AutoArmor(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
  }
  
  public boolean isWearing() {
    return !this.timer.hasReached(200.0D);
  }
  
  @EventTarget
  public void onTick(TickEvent event) {
    if (this.checkInv && 
      !(this.mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiInventory))
      return; 
    if (this.mc.thePlayer.openContainer != null && 
      this.mc.thePlayer.openContainer instanceof net.minecraft.inventory.ContainerChest)
      return; 
    if (!this.timer.hasReached(this.lastDelay))
      return; 
    this.lastDelay = (long)this.delay;
    if (this.mc.thePlayer.capabilities.isCreativeMode) {
      this.timer.reset();
      return;
    } 
    for (ArmorType armorType : ArmorType.values()) {
      int slot;
      if ((slot = findArmor(armorType, InvUtils.getArmorScore(this.mc.thePlayer.inventory.armorItemInSlot(armorType.ordinal())))) != -1) {
        isDone = false;
        if (this.mc.thePlayer.inventory.armorItemInSlot(armorType.ordinal()) != null) {
          dropArmor(armorType.ordinal());
          this.timer.reset();
          return;
        } 
        warmArmor(slot);
        this.timer.reset();
        return;
      } 
      isDone = true;
    } 
  }
  
  private int findArmor(ArmorType armorType, float minimum) {
    float best = 0.0F;
    int result = -1;
    for (int i = 0; i < this.mc.thePlayer.inventory.mainInventory.length; i++) {
      ItemStack itemStack = this.mc.thePlayer.inventory.mainInventory[i];
      if (InvUtils.getArmorScore(itemStack) >= 0.0F && InvUtils.getArmorScore(itemStack) > minimum && 
        InvUtils.getArmorScore(itemStack) >= best && isValid(armorType, itemStack)) {
        best = InvUtils.getArmorScore(itemStack);
        result = i;
      } 
    } 
    return result;
  }
  
  private boolean isValid(ArmorType type, ItemStack itemStack) {
    if (!(itemStack.getItem() instanceof ItemArmor))
      return false; 
    ItemArmor armor = (ItemArmor)itemStack.getItem();
    if (type == ArmorType.HELMET && armor.armorType == 0)
      return true; 
    if (type == ArmorType.CHEST_PLATE && armor.armorType == 1)
      return true; 
    if (type == ArmorType.LEGGINGS && armor.armorType == 2)
      return true; 
    if (type == ArmorType.BOOTS && armor.armorType == 3)
      return true; 
    return false;
  }
  
  private void warmArmor(int slot_In) {
    if (slot_In >= 0 && slot_In <= 8) {
      clickSlot(slot_In + 36, 0, true);
    } else {
      clickSlot(slot_In, 0, true);
    } 
  }
  
  private void clickSlot(int slot, int mouseButton, boolean shiftClick) {
    this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, mouseButton, shiftClick ? 1 : 0, (EntityPlayer)this.mc.thePlayer);
  }
  
  private void dropArmor(int armorSlot) {
    int slot = InvUtils.armorSlotToNormalSlot(armorSlot);
    if (!InvUtils.isFull()) {
      clickSlot(slot, 0, true);
    } else {
      this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, 1, 4, (EntityPlayer)this.mc.thePlayer);
    } 
  }
  
  enum ArmorType {
    BOOTS, LEGGINGS, CHEST_PLATE, HELMET;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\autoarmor\AutoArmor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */