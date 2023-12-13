/*    */ package org.neverhook.client.feature.impl.combat;
/*    */ 
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class PushAttack
/*    */   extends Feature {
/*    */   private final NumberSetting clickCoolDown;
/*    */   
/*    */   public PushAttack() {
/* 14 */     super("PushAttack", "Позволяет бить на ЛКМ не смотря на использование предметов", Type.Combat);
/* 15 */     this.clickCoolDown = new NumberSetting("Click CoolDown", 1.0F, 0.5F, 1.0F, 0.1F, () -> Boolean.valueOf(true));
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 20 */     if (mc.player.getCooledAttackStrength(0.0F) == this.clickCoolDown.getNumberValue() && mc.gameSettings.keyBindAttack.pressed)
/* 21 */       mc.clickMouse(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\combat\PushAttack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */