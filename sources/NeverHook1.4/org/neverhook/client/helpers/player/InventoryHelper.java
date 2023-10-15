/*     */ package org.neverhook.client.helpers.player;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.minecraft.enchantment.Enchantment;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemArmor;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import org.neverhook.client.helpers.Helper;
/*     */ 
/*     */ public class InventoryHelper
/*     */   implements Helper {
/*     */   public static boolean doesHotbarHaveAxe() {
/*  14 */     for (int i = 0; i < 9; i++) {
/*  15 */       mc.player.inventory.getStackInSlot(i);
/*  16 */       if (mc.player.inventory.getStackInSlot(i).getItem() instanceof net.minecraft.item.ItemAxe) {
/*  17 */         return true;
/*     */       }
/*     */     } 
/*  20 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean doesHotbarHaveBlock() {
/*  24 */     for (int i = 0; i < 9; i++) {
/*  25 */       if (mc.player.inventory.getStackInSlot(i).getItem() instanceof net.minecraft.item.ItemBlock) {
/*  26 */         return true;
/*     */       }
/*     */     } 
/*  29 */     return false;
/*     */   }
/*     */   
/*     */   public static int getAxe() {
/*  33 */     for (int i = 0; i < 9; i++) {
/*  34 */       ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
/*  35 */       if (itemStack.getItem() instanceof net.minecraft.item.ItemAxe) {
/*  36 */         return i;
/*     */       }
/*     */     } 
/*  39 */     return 1;
/*     */   }
/*     */   
/*     */   public static int findWaterBucket() {
/*  43 */     for (int i = 0; i < 9; i++) {
/*  44 */       mc.player.inventory.getStackInSlot(i);
/*  45 */       if (mc.player.inventory.getStackInSlot(i).getItem() == Items.WATER_BUCKET) {
/*  46 */         return i;
/*     */       }
/*     */     } 
/*  49 */     return -1;
/*     */   }
/*     */   
/*     */   public static int getSwordAtHotbar() {
/*  53 */     for (int i = 0; i < 9; i++) {
/*  54 */       ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
/*  55 */       if (itemStack.getItem() instanceof net.minecraft.item.ItemSword) {
/*  56 */         return i;
/*     */       }
/*     */     } 
/*  59 */     return -1;
/*     */   }
/*     */   
/*     */   public static int getTotemAtHotbar() {
/*  63 */     for (int i = 0; i < 45; i++) {
/*  64 */       ItemStack itemStack = mc.player.inventoryContainer.getSlot(i).getStack();
/*  65 */       if (itemStack.getItem() == Items.TOTEM_OF_UNDYING) {
/*  66 */         return i;
/*     */       }
/*     */     } 
/*  69 */     return -1;
/*     */   }
/*     */   
/*     */   public static boolean isBestArmor(ItemStack stack, int type) {
/*  73 */     float prot = getProtection(stack);
/*  74 */     String armorType = "";
/*  75 */     if (type == 1) {
/*  76 */       armorType = "helmet";
/*  77 */     } else if (type == 2) {
/*  78 */       armorType = "chestplate";
/*  79 */     } else if (type == 3) {
/*  80 */       armorType = "leggings";
/*  81 */     } else if (type == 4) {
/*  82 */       armorType = "boots";
/*     */     } 
/*  84 */     if (!stack.getUnlocalizedName().contains(armorType)) {
/*  85 */       return false;
/*     */     }
/*  87 */     for (int i = 5; i < 45; i++) {
/*  88 */       if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
/*  89 */         ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
/*  90 */         if (getProtection(is) > prot && is.getUnlocalizedName().contains(armorType))
/*  91 */           return false; 
/*     */       } 
/*     */     } 
/*  94 */     return true;
/*     */   }
/*     */   
/*     */   public static float getProtection(ItemStack stack) {
/*  98 */     float prot = 0.0F;
/*  99 */     if (stack.getItem() instanceof ItemArmor) {
/* 100 */       ItemArmor armor = (ItemArmor)stack.getItem();
/* 101 */       prot = (float)(prot + armor.damageReduceAmount + ((100 - armor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Objects.<Enchantment>requireNonNull(Enchantment.getEnchantmentByID(0)), stack)) * 0.0075D);
/* 102 */       prot = (float)(prot + EnchantmentHelper.getEnchantmentLevel(Objects.<Enchantment>requireNonNull(Enchantment.getEnchantmentByID(3)), stack) / 100.0D);
/* 103 */       prot = (float)(prot + EnchantmentHelper.getEnchantmentLevel(Objects.<Enchantment>requireNonNull(Enchantment.getEnchantmentByID(1)), stack) / 100.0D);
/* 104 */       prot = (float)(prot + EnchantmentHelper.getEnchantmentLevel(Objects.<Enchantment>requireNonNull(Enchantment.getEnchantmentByID(7)), stack) / 100.0D);
/* 105 */       prot = (float)(prot + EnchantmentHelper.getEnchantmentLevel(Objects.<Enchantment>requireNonNull(Enchantment.getEnchantmentByID(34)), stack) / 50.0D);
/* 106 */       prot = (float)(prot + EnchantmentHelper.getEnchantmentLevel(Objects.<Enchantment>requireNonNull(Enchantment.getEnchantmentByID(4)), stack) / 100.0D);
/*     */     } 
/* 108 */     return prot;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\helpers\player\InventoryHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */