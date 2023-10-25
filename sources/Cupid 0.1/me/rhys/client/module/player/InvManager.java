package me.rhys.client.module.player;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerUpdateEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.base.util.Timer;
import me.rhys.base.util.entity.BlockUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.apache.commons.lang3.RandomUtils;

public class InvManager extends Module {
  @Name("Delay")
  @Clamp(min = 0.0D, max = 1000.0D)
  private float delay;
  
  @Name("RandomMax")
  @Clamp(min = 0.0D, max = 1000.0D)
  private int randomMax;
  
  @Name("RandomMin")
  @Clamp(min = 0.0D, max = 1000.0D)
  private int randomMin;
  
  @Name("Random")
  private boolean random;
  
  @Name("Archery")
  private boolean archery;
  
  @Name("Food")
  private boolean food;
  
  @Name("Sword")
  private boolean sword;
  
  @Name("KeepEmpty")
  private boolean keepEmpty;
  
  @Name("OpenInventory")
  private boolean openInventory;
  
  private static final int BLOCK_CAP = 1024;
  
  private static final int WEAPON_SLOT = 36;
  
  private static final int PICKAXE_SLOT = 37;
  
  private static final int AXE_SLOT = 38;
  
  private static final int SHOVEL_SLOT = 39;
  
  private final Timer timer;
  
  public long lastClean;
  
  public InvManager(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    this.delay = 50.0F;
    this.randomMax = 0;
    this.randomMin = 0;
    this.random = true;
    this.archery = true;
    this.food = true;
    this.sword = true;
    this.keepEmpty = true;
    this.openInventory = true;
    this.timer = new Timer();
  }
  
  @EventTarget
  void onUpdate(PlayerUpdateEvent event) {
    long time = (long)(this.delay + (this.random ? RandomUtils.nextInt(this.randomMin, this.randomMax) : false));
    if (this.openInventory && !(this.mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiInventory))
      return; 
    if (this.mc.currentScreen == null || this.mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiInventory || this.mc.currentScreen instanceof net.minecraft.client.gui.GuiChat) {
      if (this.timer.hasReached(time))
        if (!this.mc.thePlayer.inventoryContainer.getSlot(36).getHasStack()) {
          getBestWeapon(36);
        } else if (!isBestWeapon(this.mc.thePlayer.inventoryContainer.getSlot(36).getStack())) {
          getBestWeapon(36);
        }  
      if (this.timer.hasReached(time))
        getBestPickaxe(37); 
      if (this.timer.hasReached(time))
        getBestShovel(39); 
      if (this.timer.hasReached(time))
        getBestAxe(38); 
      if (this.timer.hasReached(time))
        for (int i = 9; i < 45; i++) {
          if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
            ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (shouldDrop(is, i)) {
              drop(i);
              this.timer.reset();
              this.lastClean = System.currentTimeMillis();
              if (time > 0L)
                break; 
            } 
          } 
        }  
    } 
  }
  
  public void shiftClick(int slot) {
    this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, 0, 1, (EntityPlayer)this.mc.thePlayer);
    this.lastClean = System.currentTimeMillis();
  }
  
  public void swap(int slot1, int hotbarSlot) {
    this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot1, hotbarSlot, 2, (EntityPlayer)this.mc.thePlayer);
    this.lastClean = System.currentTimeMillis();
  }
  
  public void drop(int slot) {
    this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, 1, 4, (EntityPlayer)this.mc.thePlayer);
    this.lastClean = System.currentTimeMillis();
  }
  
  public boolean isBestWeapon(ItemStack stack) {
    float damage = getDamage(stack);
    for (int i = 9; i < 45; i++) {
      if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
        ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
        if (getDamage(is) > damage && (is.getItem() instanceof ItemSword || !this.sword))
          return false; 
      } 
    } 
    return (stack.getItem() instanceof ItemSword || !this.sword);
  }
  
  public void getBestWeapon(int slot) {
    for (int i = 9; i < 45; i++) {
      if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
        ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
        if (isBestWeapon(is) && getDamage(is) > 0.0F && (is.getItem() instanceof ItemSword || !this.sword)) {
          swap(i, slot - 36);
          this.timer.reset();
          break;
        } 
      } 
    } 
  }
  
  private float getDamage(ItemStack stack) {
    float damage = 0.0F;
    Item item = stack.getItem();
    if (item instanceof ItemTool) {
      ItemTool tool = (ItemTool)item;
      damage += tool.getMaxDamage();
    } 
    if (item instanceof ItemSword) {
      ItemSword sword = (ItemSword)item;
      damage += sword.getAttackDamage();
    } 
    damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25F + 
      EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01F;
    return damage;
  }
  
  public boolean shouldDrop(ItemStack stack, int slot) {
    String itemName = stack.getItem().getUnlocalizedName().toLowerCase();
    if (stack.getDisplayName().toLowerCase().contains("(right click)"))
      return false; 
    if (stack.getDisplayName().toLowerCase().contains("§k||"))
      return false; 
    if ((slot == 36 && isBestWeapon(this.mc.thePlayer.inventoryContainer.getSlot(36).getStack())) || (slot == 37 && 
      isBestPickaxe(this.mc.thePlayer.inventoryContainer.getSlot(37).getStack())) || (slot == 38 && 
      isBestAxe(this.mc.thePlayer.inventoryContainer.getSlot(38).getStack())) || (slot == 39 && 
      isBestShovel(this.mc.thePlayer.inventoryContainer.getSlot(39).getStack())))
      return false; 
    if (stack.getItem() instanceof ItemArmor)
      for (int type = 1; type < 5; type++) {
        if (this.mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()) {
          ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
          if (isBestArmor(is, type))
            continue; 
        } 
        if (isBestArmor(stack, type))
          return false; 
        continue;
      }  
    if (stack.getItem() instanceof ItemBlock && (
      getBlockCount() > 1024 || BlockUtil.blacklistedBlocks
      .contains(((ItemBlock)stack.getItem()).getBlock())))
      return true; 
    if (stack.getItem() instanceof ItemPotion)
      return isBadPotion(stack); 
    if (stack.getItem() instanceof net.minecraft.item.ItemFood && this.food && !(stack.getItem() instanceof net.minecraft.item.ItemAppleGold))
      return true; 
    if (stack.getItem() instanceof net.minecraft.item.ItemHoe || stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemArmor)
      return true; 
    if ((stack.getItem() instanceof net.minecraft.item.ItemBow || itemName.contains("arrow")) && this.archery)
      return true; 
    if ((itemName.contains("bowl") || (itemName
      .contains("bucket") && !itemName.contains("water") && 
      !itemName.contains("lava") && !itemName.contains("milk")) || (stack
      .getItem() instanceof net.minecraft.item.ItemGlassBottle && !this.keepEmpty)) && !this.keepEmpty)
      return true; 
    return (itemName.contains("tnt") || itemName
      .contains("stick") || itemName
      .contains("egg") || itemName
      .contains("string") || itemName
      .contains("cake") || (itemName
      .contains("mushroom") && !itemName.contains("stew")) || itemName
      .contains("flint") || itemName
      .contains("dyepowder") || itemName
      .contains("feather") || (itemName
      .contains("chest") && !stack.getDisplayName().toLowerCase().contains("collect")) || itemName
      .contains("snow") || itemName
      .contains("fish") || itemName
      .contains("enchant") || itemName
      .contains("exp") || itemName
      .contains("shears") || itemName
      .contains("anvil") || itemName
      .contains("torch") || itemName
      .contains("seeds") || itemName
      .contains("leather") || itemName
      .contains("reeds") || itemName
      .contains("skull") || itemName
      .contains("record") || itemName
      .contains("snowball") || itemName
      .contains("piston"));
  }
  
  private int getBlockCount() {
    int blockCount = 0;
    for (int i = 0; i < 45; i++) {
      if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
        ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
        Item item = is.getItem();
        if (is.getItem() instanceof ItemBlock && !BlockUtil.blacklistedBlocks.contains(((ItemBlock)item).getBlock()))
          blockCount += is.stackSize; 
      } 
    } 
    return blockCount;
  }
  
  private void getBestPickaxe(int slot) {
    for (int i = 9; i < 45; i++) {
      if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
        ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
        if (isBestPickaxe(is) && 37 != i && 
          !isBestWeapon(is))
          if (!this.mc.thePlayer.inventoryContainer.getSlot(37).getHasStack()) {
            swap(i, 1);
            this.timer.reset();
            if (this.delay > 0.0F)
              return; 
          } else if (!isBestPickaxe(this.mc.thePlayer.inventoryContainer.getSlot(37).getStack())) {
            swap(i, 1);
            this.timer.reset();
            if (this.delay > 0.0F)
              return; 
          }  
      } 
    } 
  }
  
  private void getBestShovel(int slot) {
    for (int i = 9; i < 45; i++) {
      if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
        ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
        if (isBestShovel(is) && 39 != i && 
          !isBestWeapon(is))
          if (!this.mc.thePlayer.inventoryContainer.getSlot(39).getHasStack()) {
            swap(i, 3);
            this.timer.reset();
            if (this.delay > 0.0F)
              return; 
          } else if (!isBestShovel(this.mc.thePlayer.inventoryContainer.getSlot(39).getStack())) {
            swap(i, 3);
            this.timer.reset();
            if (this.delay > 0.0F)
              return; 
          }  
      } 
    } 
  }
  
  private void getBestAxe(int slot) {
    for (int i = 9; i < 45; i++) {
      if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
        ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
        if (isBestAxe(is) && 38 != i && 
          !isBestWeapon(is))
          if (!this.mc.thePlayer.inventoryContainer.getSlot(38).getHasStack()) {
            swap(i, 2);
            this.timer.reset();
            if (this.delay > 0.0F)
              return; 
          } else if (!isBestAxe(this.mc.thePlayer.inventoryContainer.getSlot(38).getStack())) {
            swap(i, 2);
            this.timer.reset();
            if (this.delay > 0.0F)
              return; 
          }  
      } 
    } 
  }
  
  private boolean isBestPickaxe(ItemStack stack) {
    Item item = stack.getItem();
    if (!(item instanceof net.minecraft.item.ItemPickaxe))
      return false; 
    float value = getToolEffect(stack);
    for (int i = 9; i < 45; i++) {
      if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
        ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
        if (getToolEffect(is) > value && is.getItem() instanceof net.minecraft.item.ItemPickaxe)
          return false; 
      } 
    } 
    return true;
  }
  
  private boolean isBestShovel(ItemStack stack) {
    Item item = stack.getItem();
    if (!(item instanceof net.minecraft.item.ItemSpade))
      return false; 
    float value = getToolEffect(stack);
    for (int i = 9; i < 45; i++) {
      if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
        ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
        if (getToolEffect(is) > value && is.getItem() instanceof net.minecraft.item.ItemSpade)
          return false; 
      } 
    } 
    return true;
  }
  
  private boolean isBestAxe(ItemStack stack) {
    Item item = stack.getItem();
    if (!(item instanceof net.minecraft.item.ItemAxe))
      return false; 
    float value = getToolEffect(stack);
    for (int i = 9; i < 45; i++) {
      if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
        ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
        if (getToolEffect(is) > value && is.getItem() instanceof net.minecraft.item.ItemAxe && !isBestWeapon(stack))
          return false; 
      } 
    } 
    return true;
  }
  
  private float getToolEffect(ItemStack stack) {
    Item item = stack.getItem();
    if (!(item instanceof ItemTool))
      return 0.0F; 
    String name = item.getUnlocalizedName();
    ItemTool tool = (ItemTool)item;
    if (item instanceof net.minecraft.item.ItemPickaxe) {
      value = tool.getStrVsBlock(stack, Blocks.stone);
      if (name.toLowerCase().contains("gold"))
        value -= 5.0F; 
    } else if (item instanceof net.minecraft.item.ItemSpade) {
      value = tool.getStrVsBlock(stack, Blocks.dirt);
      if (name.toLowerCase().contains("gold"))
        value -= 5.0F; 
    } else if (item instanceof net.minecraft.item.ItemAxe) {
      value = tool.getStrVsBlock(stack, Blocks.log);
      if (name.toLowerCase().contains("gold"))
        value -= 5.0F; 
    } else {
      return 1.0F;
    } 
    float value = (float)(value + EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) * 0.0075D);
    value = (float)(value + EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 100.0D);
    return value;
  }
  
  private boolean isBadPotion(ItemStack stack) {
    if (stack != null && stack.getItem() instanceof ItemPotion) {
      ItemPotion potion = (ItemPotion)stack.getItem();
      if (potion.getEffects(stack) == null && !potion.hasEffect(stack))
        return true; 
      for (Object o : potion.getEffects(stack)) {
        PotionEffect effect = (PotionEffect)o;
        if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.harm.getId() || effect.getPotionID() == Potion.moveSlowdown.getId() || effect.getPotionID() == Potion.weakness.getId())
          return true; 
      } 
    } 
    return false;
  }
  
  private boolean invContainsType(int type) {
    for (int i = 9; i < 45; i++) {
      if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
        ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
        Item item = is.getItem();
        if (item instanceof ItemArmor) {
          ItemArmor armor = (ItemArmor)item;
          if (type == armor.armorType)
            return true; 
        } 
      } 
    } 
    return false;
  }
  
  private void getBestArmor() {
    for (int type = 1; type < 5; type++) {
      if (this.mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()) {
        ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
        if (isBestArmor(is, type))
          continue; 
        drop(4 + type);
      } 
      for (int i = 9; i < 45; i++) {
        if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
          ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
          if (isBestArmor(is, type) && getProtection(is) > 0.0F) {
            shiftClick(i);
            this.timer.reset();
            if (this.delay > 0.0F)
              return; 
          } 
        } 
      } 
      continue;
    } 
  }
  
  private boolean isBestArmor(ItemStack stack, int type) {
    float prot = getProtection(stack);
    String strType = "";
    if (type == 1) {
      strType = "helmet";
    } else if (type == 2) {
      strType = "chestplate";
    } else if (type == 3) {
      strType = "leggings";
    } else if (type == 4) {
      strType = "boots";
    } 
    if (!stack.getUnlocalizedName().contains(strType))
      return false; 
    for (int i = 5; i < 45; i++) {
      if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
        ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
        if (getProtection(is) > prot && is.getUnlocalizedName().contains(strType))
          return false; 
      } 
    } 
    return true;
  }
  
  private float getProtection(ItemStack stack) {
    float prot = 0.0F;
    if (stack.getItem() instanceof ItemArmor) {
      ItemArmor armor = (ItemArmor)stack.getItem();
      prot = (float)(prot + armor.damageReduceAmount + ((100 - armor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack)) * 0.0075D);
      prot = (float)(prot + EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack) / 100.0D);
      prot = (float)(prot + EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack) / 100.0D);
      prot = (float)(prot + EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack) / 100.0D);
      prot = (float)(prot + EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 50.0D);
      prot = (float)(prot + EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack) / 100.0D);
    } 
    return prot;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\InvManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */