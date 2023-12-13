/*    */ package org.neverhook.client.feature.impl.combat;
/*    */ 
/*    */ import net.minecraft.item.ItemStack;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class AutoGapple
/*    */   extends Feature {
/*    */   public static NumberSetting health;
/*    */   private boolean isActive;
/*    */   
/*    */   public AutoGapple() {
/* 17 */     super("AutoGApple", "Автоматически ест яблоко при опредленном здоровье", Type.Combat);
/* 18 */     health = new NumberSetting("Health Amount", 15.0F, 1.0F, 20.0F, 1.0F, () -> Boolean.valueOf(true));
/* 19 */     addSettings(new Setting[] { (Setting)health });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate e) {
/* 24 */     setSuffix("" + (int)health.getNumberValue());
/* 25 */     if (mc.player == null || mc.world == null)
/*    */       return; 
/* 27 */     if (isGoldenApple(mc.player.getHeldItemOffhand()) && mc.player.getHealth() <= health.getNumberValue()) {
/* 28 */       this.isActive = true;
/* 29 */       mc.gameSettings.keyBindUseItem.pressed = true;
/* 30 */     } else if (this.isActive) {
/* 31 */       mc.gameSettings.keyBindUseItem.pressed = false;
/* 32 */       this.isActive = false;
/*    */     } 
/*    */   }
/*    */   
/*    */   private boolean isGoldenApple(ItemStack itemStack) {
/* 37 */     return (itemStack != null && !itemStack.isEmpty() && itemStack.getItem() instanceof net.minecraft.item.ItemAppleGold);
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\combat\AutoGapple.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */