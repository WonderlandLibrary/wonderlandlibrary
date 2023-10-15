/*    */ package org.neverhook.client.feature.impl.combat;
/*    */ 
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.inventory.ClickType;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.event.events.impl.render.EventRender2D;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.player.InventoryHelper;
/*    */ import org.neverhook.client.helpers.player.MovementHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.BooleanSetting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class AutoTotem
/*    */   extends Feature {
/*    */   public static BooleanSetting definiteHealth;
/*    */   public static NumberSetting health;
/*    */   public static BooleanSetting countTotem;
/*    */   public static BooleanSetting checkCrystal;
/*    */   public static BooleanSetting inventoryOnly;
/*    */   public static BooleanSetting noMoving;
/*    */   public static NumberSetting radiusCrystal;
/*    */   
/*    */   public AutoTotem() {
/* 31 */     super("AutoTotem", "Автоматически берет в руку тотем при опредленном здоровье", Type.Combat);
/* 32 */     definiteHealth = new BooleanSetting("Definite Health", false, () -> Boolean.valueOf(true));
/* 33 */     health = new NumberSetting("Health Amount", 10.0F, 1.0F, 20.0F, 0.5F, () -> Boolean.valueOf(definiteHealth.getBoolValue()));
/* 34 */     inventoryOnly = new BooleanSetting("Only Inventory", false, () -> Boolean.valueOf(true));
/* 35 */     noMoving = new BooleanSetting("No Moving Swap", false, () -> Boolean.valueOf(true));
/* 36 */     countTotem = new BooleanSetting("Count Totem", true, () -> Boolean.valueOf(true));
/* 37 */     checkCrystal = new BooleanSetting("Check Crystal", true, () -> Boolean.valueOf(true));
/* 38 */     radiusCrystal = new NumberSetting("Distance to Crystal", 6.0F, 1.0F, 8.0F, 1.0F, () -> Boolean.valueOf(checkCrystal.getBoolValue()));
/* 39 */     addSettings(new Setting[] { (Setting)definiteHealth, (Setting)health, (Setting)inventoryOnly, (Setting)noMoving, (Setting)countTotem, (Setting)checkCrystal, (Setting)radiusCrystal });
/*    */   }
/*    */   
/*    */   private int fountTotemCount() {
/* 43 */     int count = 0;
/* 44 */     for (int i = 0; i < mc.player.inventory.getSizeInventory(); i++) {
/* 45 */       ItemStack stack = mc.player.inventory.getStackInSlot(i);
/* 46 */       if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
/* 47 */         count++;
/*    */       }
/*    */     } 
/* 50 */     return count;
/*    */   }
/*    */   
/*    */   private boolean checkCrystal() {
/* 54 */     for (Entity entity : mc.world.loadedEntityList) {
/* 55 */       if (entity instanceof net.minecraft.entity.item.EntityEnderCrystal && mc.player.getDistanceToEntity(entity) <= radiusCrystal.getNumberValue()) {
/* 56 */         return true;
/*    */       }
/*    */     } 
/* 59 */     return false;
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onRender2D(EventRender2D event) {
/* 64 */     if (fountTotemCount() > 0 && countTotem.getBoolValue()) {
/* 65 */       mc.sfuiFontRender.drawStringWithShadow(fountTotemCount() + "", (event.getResolution().getScaledWidth() / 2.0F + 19.0F), (event.getResolution().getScaledHeight() / 2.0F), -1);
/* 66 */       for (int i = 0; i < mc.player.inventory.getSizeInventory(); i++) {
/* 67 */         ItemStack stack = mc.player.inventory.getStackInSlot(i);
/* 68 */         if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
/* 69 */           GlStateManager.pushMatrix();
/* 70 */           GlStateManager.disableBlend();
/* 71 */           mc.getRenderItem().renderItemAndEffectIntoGUI(stack, event.getResolution().getScaledWidth() / 2 + 4, event.getResolution().getScaledHeight() / 2 - 7);
/* 72 */           GlStateManager.popMatrix();
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 80 */     if (noMoving.getBoolValue() && MovementHelper.isMoving()) {
/*    */       return;
/*    */     }
/* 83 */     if (inventoryOnly.getBoolValue() && !(mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiInventory)) {
/*    */       return;
/*    */     }
/* 86 */     if (definiteHealth.getBoolValue() && mc.player.getHealth() > health.getNumberValue()) {
/*    */       return;
/*    */     }
/* 89 */     if (mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING && InventoryHelper.getTotemAtHotbar() != -1) {
/* 90 */       mc.playerController.windowClick(0, InventoryHelper.getTotemAtHotbar(), 1, ClickType.PICKUP, (EntityPlayer)mc.player);
/* 91 */       mc.playerController.windowClick(0, 45, 1, ClickType.PICKUP, (EntityPlayer)mc.player);
/*    */     } 
/* 93 */     if (checkCrystal() && checkCrystal.getBoolValue() && 
/* 94 */       mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING && InventoryHelper.getTotemAtHotbar() != -1) {
/* 95 */       mc.playerController.windowClick(0, InventoryHelper.getTotemAtHotbar(), 1, ClickType.PICKUP, (EntityPlayer)mc.player);
/* 96 */       mc.playerController.windowClick(0, 45, 1, ClickType.PICKUP, (EntityPlayer)mc.player);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\combat\AutoTotem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */