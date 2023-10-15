/*    */ package org.neverhook.client.feature.impl.player;
/*    */ 
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.BooleanSetting;
/*    */ 
/*    */ public class NoDelay extends Feature {
/* 11 */   public BooleanSetting rightClickDelay = new BooleanSetting("NoRightClickDelay", true, () -> Boolean.valueOf(true));
/* 12 */   public BooleanSetting leftClickDelay = new BooleanSetting("NoLeftClickDelay", false, () -> Boolean.valueOf(true));
/* 13 */   public BooleanSetting jumpDelay = new BooleanSetting("NoJumpDelay", true, () -> Boolean.valueOf(true));
/* 14 */   public BooleanSetting blockHitDelay = new BooleanSetting("NoBlockHitDelay", false, () -> Boolean.valueOf(true));
/*    */   
/*    */   public NoDelay() {
/* 17 */     super("NoDelay", "Убирает задержку", Type.Player);
/* 18 */     addSettings(new Setting[] { (Setting)this.rightClickDelay, (Setting)this.leftClickDelay, (Setting)this.jumpDelay, (Setting)this.blockHitDelay });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 24 */     mc.rightClickDelayTimer = 4;
/* 25 */     super.onDisable();
/*    */   }
/*    */ 
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 31 */     if (!getState()) {
/*    */       return;
/*    */     }
/* 34 */     if (this.rightClickDelay.getBoolValue()) {
/* 35 */       mc.rightClickDelayTimer = 0;
/*    */     }
/*    */     
/* 38 */     if (this.leftClickDelay.getBoolValue()) {
/* 39 */       mc.leftClickCounter = 0;
/*    */     }
/*    */     
/* 42 */     if (this.jumpDelay.getBoolValue()) {
/* 43 */       mc.player.jumpTicks = 0;
/*    */     }
/*    */     
/* 46 */     if (this.blockHitDelay.getBoolValue())
/* 47 */       mc.playerController.blockHitDelay = 0; 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\player\NoDelay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */