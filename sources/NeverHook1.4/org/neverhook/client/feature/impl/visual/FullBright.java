/*    */ package org.neverhook.client.feature.impl.visual;
/*    */ 
/*    */ import net.minecraft.potion.Potion;
/*    */ import net.minecraft.potion.PotionEffect;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.ListSetting;
/*    */ 
/*    */ public class FullBright extends Feature {
/*    */   public static ListSetting brightMode;
/*    */   
/*    */   public FullBright() {
/* 16 */     super("FullBright", "Убирает темноту в игре", Type.Visuals);
/* 17 */     brightMode = new ListSetting("FullBright Mode", "Gamma", () -> Boolean.valueOf(true), new String[] { "Gamma", "Potion" });
/* 18 */     addSettings(new Setting[] { (Setting)brightMode });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 23 */     if (getState()) {
/* 24 */       String mode = brightMode.getOptions();
/* 25 */       if (mode.equalsIgnoreCase("Gamma")) {
/* 26 */         mc.gameSettings.gammaSetting = 1000.0F;
/*    */       }
/* 28 */       if (mode.equalsIgnoreCase("Potion")) {
/* 29 */         mc.player.addPotionEffect(new PotionEffect(Potion.getPotionById(16), 817, 1));
/*    */       } else {
/* 31 */         mc.player.removePotionEffect(Potion.getPotionById(16));
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 38 */     super.onDisable();
/* 39 */     mc.gameSettings.gammaSetting = 1.0F;
/* 40 */     mc.player.removePotionEffect(Potion.getPotionById(16));
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\visual\FullBright.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */