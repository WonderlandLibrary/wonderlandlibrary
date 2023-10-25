package me.rhys.client.module.combat.autorod;

import com.google.common.collect.Multimap;
import java.util.Iterator;
import java.util.Map;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerUpdateEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.player.breaker.TimeHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class AutoRod extends Module {
  private final TimeHelper time = new TimeHelper();
  
  private final TimeHelper time2 = new TimeHelper();
  
  private Boolean switchBack = Boolean.valueOf(false);
  
  private Boolean useRod = Boolean.valueOf(false);
  
  @Name("Delay")
  @Clamp(min = 0.0D, max = 1000.0D)
  public float delay = 50.0F;
  
  @Name("AutoDisable")
  public boolean disable = false;
  
  public AutoRod(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
  }
  
  @EventTarget
  private void onUpdate(PlayerUpdateEvent event) {
    int item = Item.getIdFromItem(this.mc.thePlayer.getHeldItem().getItem());
    float rodDelay = this.delay;
    if (this.mc.currentScreen != null)
      return; 
    if (!this.disable) {
      if (!this.useRod.booleanValue() && item == 346) {
        Rod();
        this.useRod = Boolean.valueOf(true);
      } 
      if (this.time.isDelayComplete((rodDelay - 50.0F)) && this.switchBack.booleanValue()) {
        switchBack();
        this.switchBack = Boolean.valueOf(false);
      } 
      if (this.time.isDelayComplete(rodDelay) && this.useRod.booleanValue())
        this.useRod = Boolean.valueOf(false); 
    } else if (item == 346) {
      if (this.time2.isDelayComplete((rodDelay + 200.0F))) {
        Rod();
        this.time2.reset();
      } 
      if (this.time.isDelayComplete(rodDelay)) {
        this.mc.thePlayer.inventory.currentItem = bestWeapon((Entity)this.mc.thePlayer);
        this.time.reset();
        toggle();
      } 
    } else if (this.time.isDelayComplete(100.0D)) {
      switchToRod();
      this.time.reset();
    } 
  }
  
  private int findRod(int startSlot, int endSlot) {
    for (int i = startSlot; i < endSlot; i++) {
      ItemStack stack = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
      if (stack != null && stack.getItem() == Items.fishing_rod)
        return i; 
    } 
    return -1;
  }
  
  private void switchToRod() {
    for (int i = 36; i < 45; i++) {
      ItemStack itemstack = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
      if (itemstack != null && Item.getIdFromItem(itemstack.getItem()) == 346) {
        this.mc.thePlayer.inventory.currentItem = i - 36;
        break;
      } 
    } 
  }
  
  private void Rod() {
    int rod = findRod(36, 45);
    this.mc.playerController.sendUseItem((EntityPlayer)this.mc.thePlayer, (World)this.mc.theWorld, this.mc.thePlayer.inventoryContainer.getSlot(rod).getStack());
    this.switchBack = Boolean.valueOf(true);
    this.time.reset();
  }
  
  private void switchBack() {
    this.mc.thePlayer.inventory.currentItem = bestWeapon((Entity)this.mc.thePlayer);
  }
  
  public int bestWeapon(Entity target) {
    int firstSlot = this.mc.thePlayer.inventory.currentItem = 0;
    int bestWeapon = -1;
    int j = 1;
    byte i;
    for (i = 0; i < 9; i = (byte)(i + 1)) {
      this.mc.thePlayer.inventory.currentItem = i;
      ItemStack itemStack = this.mc.thePlayer.getHeldItem();
      if (itemStack != null) {
        int itemAtkDamage = (int)getItemAtkDamage(itemStack);
        itemAtkDamage = (int)(itemAtkDamage + EnchantmentHelper.getModifierForCreature(itemStack, EnumCreatureAttribute.UNDEFINED));
        if (itemAtkDamage > j) {
          j = itemAtkDamage;
          bestWeapon = i;
        } 
      } 
    } 
    if (bestWeapon != -1)
      return bestWeapon; 
    return firstSlot;
  }
  
  public static float getItemAtkDamage(ItemStack itemStack) {
    Multimap multimap = itemStack.getAttributeModifiers();
    if (!multimap.isEmpty()) {
      Iterator<Map.Entry> iterator = multimap.entries().iterator();
      if (iterator.hasNext()) {
        Map.Entry entry = iterator.next();
        AttributeModifier attributeModifier = (AttributeModifier)entry.getValue();
        double damage = (attributeModifier.getOperation() != 1 && attributeModifier.getOperation() != 2) ? attributeModifier.getAmount() : (attributeModifier.getAmount() * 100.0D);
        if (attributeModifier.getAmount() > 1.0D)
          return 1.0F + (float)damage; 
        return 1.0F;
      } 
    } 
    return 1.0F;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\combat\autorod\AutoRod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */