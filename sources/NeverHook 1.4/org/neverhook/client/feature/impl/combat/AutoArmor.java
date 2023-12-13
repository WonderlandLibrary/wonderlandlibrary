/*     */ package org.neverhook.client.feature.impl.combat;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import net.minecraft.enchantment.Enchantment;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.init.Enchantments;
/*     */ import net.minecraft.inventory.ClickType;
/*     */ import net.minecraft.inventory.EntityEquipmentSlot;
/*     */ import net.minecraft.item.ItemArmor;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import org.neverhook.client.event.EventTarget;
/*     */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.helpers.misc.TimerHelper;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.BooleanSetting;
/*     */ import org.neverhook.client.settings.impl.NumberSetting;
/*     */ 
/*     */ public class AutoArmor
/*     */   extends Feature
/*     */ {
/*     */   public static BooleanSetting openInventory;
/*     */   private final NumberSetting delay;
/*  31 */   public TimerHelper timerUtils = new TimerHelper();
/*     */   
/*     */   public AutoArmor() {
/*  34 */     super("AutoArmor", "Автоматически одевает лучшую броню находящиеся в инвентаре", Type.Combat);
/*  35 */     this.delay = new NumberSetting("Equip Delay", 1.0F, 0.0F, 10.0F, 1.0F, () -> Boolean.valueOf(true));
/*  36 */     openInventory = new BooleanSetting("Open Inventory", true, () -> Boolean.valueOf(true));
/*  37 */     addSettings(new Setting[] { (Setting)this.delay, (Setting)openInventory });
/*     */   }
/*     */   
/*     */   public static boolean isNullOrEmpty(ItemStack stack) {
/*  41 */     return (stack == null || stack.isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   @EventTarget
/*     */   public void onUpdate(EventUpdate event) {
/*  47 */     setSuffix("" + this.delay.getNumberValue());
/*     */     
/*  49 */     if (!(mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiInventory) && openInventory.getBoolValue()) {
/*     */       return;
/*     */     }
/*  52 */     if (mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiContainer && !(mc.currentScreen instanceof net.minecraft.client.renderer.InventoryEffectRenderer)) {
/*     */       return;
/*     */     }
/*     */     
/*  56 */     InventoryPlayer inventory = mc.player.inventory;
/*     */     
/*  58 */     int[] bestArmorSlots = new int[4];
/*  59 */     int[] bestArmorValues = new int[4];
/*     */     
/*  61 */     for (int type = 0; type < 4; type++) {
/*     */       
/*  63 */       bestArmorSlots[type] = -1;
/*     */       
/*  65 */       ItemStack stack = inventory.armorItemInSlot(type);
/*  66 */       if (!isNullOrEmpty(stack) && stack.getItem() instanceof ItemArmor) {
/*     */         
/*  68 */         ItemArmor item = (ItemArmor)stack.getItem();
/*  69 */         bestArmorValues[type] = getArmorValue(item, stack);
/*     */       } 
/*     */     } 
/*     */     
/*  73 */     for (int slot = 0; slot < 36; slot++) {
/*     */       
/*  75 */       ItemStack stack = inventory.getStackInSlot(slot);
/*     */       
/*  77 */       if (!isNullOrEmpty(stack) && stack.getItem() instanceof ItemArmor) {
/*     */         
/*  79 */         ItemArmor item = (ItemArmor)stack.getItem();
/*  80 */         int armorType = item.armorType.getIndex();
/*  81 */         int armorValue = getArmorValue(item, stack);
/*     */         
/*  83 */         if (armorValue > bestArmorValues[armorType]) {
/*     */           
/*  85 */           bestArmorSlots[armorType] = slot;
/*  86 */           bestArmorValues[armorType] = armorValue;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  91 */     ArrayList<Integer> types = new ArrayList<>(Arrays.asList(new Integer[] { Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3) }));
/*  92 */     Collections.shuffle(types);
/*     */     
/*  94 */     for (Iterator<Integer> iterator = types.iterator(); iterator.hasNext(); ) { int i = ((Integer)iterator.next()).intValue();
/*  95 */       int j = bestArmorSlots[i];
/*  96 */       if (j == -1) {
/*     */         continue;
/*     */       }
/*  99 */       ItemStack oldArmor = inventory.armorItemInSlot(i);
/* 100 */       if (!isNullOrEmpty(oldArmor) && inventory.getFirstEmptyStack() == -1) {
/*     */         continue;
/*     */       }
/* 103 */       if (j < 9) {
/* 104 */         j += 36;
/*     */       }
/* 106 */       if (this.timerUtils.hasReached(this.delay.getNumberValue() * 100.0F)) {
/* 107 */         if (!isNullOrEmpty(oldArmor)) {
/* 108 */           mc.playerController.windowClick(0, 8 - i, 0, ClickType.QUICK_MOVE, (EntityPlayer)mc.player);
/*     */         }
/* 110 */         mc.playerController.windowClick(0, j, 0, ClickType.QUICK_MOVE, (EntityPlayer)mc.player);
/* 111 */         this.timerUtils.reset();
/*     */       }  }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   private int getArmorValue(ItemArmor item, ItemStack stack) {
/* 118 */     int armorPoints = item.damageReduceAmount;
/* 119 */     int prtPoints = 0;
/* 120 */     int armorToughness = (int)item.toughness;
/* 121 */     int armorType = item.getArmorMaterial().getDamageReductionAmount(EntityEquipmentSlot.LEGS);
/* 122 */     Enchantment protection = Enchantments.PROTECTION;
/* 123 */     int prtLvl = EnchantmentHelper.getEnchantmentLevel(protection, stack);
/* 124 */     DamageSource dmgSource = DamageSource.causePlayerDamage((EntityPlayer)mc.player);
/* 125 */     prtPoints = protection.calcModifierDamage(prtLvl, dmgSource);
/* 126 */     return armorPoints * 5 + prtPoints * 3 + armorToughness + armorType;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\combat\AutoArmor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */