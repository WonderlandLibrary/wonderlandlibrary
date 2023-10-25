package me.rhys.client.module.player;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerUpdateEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.base.util.Timer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class AutoSwordSlot extends Module {
  private Timer timer = new Timer();
  
  public int delay = 100;
  
  public int slot = 0;
  
  @Name("Inventory Only")
  public boolean inventoryonly = false;
  
  public AutoSwordSlot(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
  }
  
  @EventTarget
  public void onUpdate(PlayerUpdateEvent event) {
    if (!this.timer.hasReached(this.delay) || ((getMc()).currentScreen != null && !((getMc()).currentScreen instanceof net.minecraft.client.gui.inventory.GuiInventory)) || (!((getMc()).currentScreen instanceof net.minecraft.client.gui.inventory.GuiInventory) && this.inventoryonly))
      return; 
    int best = -1;
    float swordDamage = 0.0F;
    for (int i = 9; i < 45; i++) {
      if ((getMc()).thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
        ItemStack is = (getMc()).thePlayer.inventoryContainer.getSlot(i).getStack();
        if (is.getItem() instanceof ItemSword) {
          float swordD = getItemDamage(is);
          if (swordD >= swordDamage) {
            swordDamage = swordD;
            best = i;
          } 
        } 
      } 
    } 
    ItemStack current = (getMc()).thePlayer.inventoryContainer.getSlot(36 + this.slot).getStack();
    if (best != -1 || current == null || (current.getItem() instanceof ItemSword && swordDamage > getItemDamage(current))) {
      float dmg = (current != null && current.getItem() != null && current.getItem() instanceof ItemSword) ? getItemDamage(current) : -83474.0F;
      if (best != 36 + this.slot && best != -1 && swordDamage > dmg) {
        (getMc()).playerController.windowClick((getMc()).thePlayer.inventoryContainer.windowId, best, this.slot, 2, (EntityPlayer)(getMc()).thePlayer);
        this.timer.reset();
      } 
    } 
  }
  
  private float getItemDamage(ItemStack itemStack) {
    float damage = ((ItemSword)itemStack.getItem()).getDamageVsEntity();
    damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25F;
    damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack) * 0.01F;
    return damage;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\AutoSwordSlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */