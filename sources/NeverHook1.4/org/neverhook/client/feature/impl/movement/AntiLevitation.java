/*    */ package org.neverhook.client.feature.impl.movement;
/*    */ 
/*    */ import net.minecraft.init.MobEffects;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ 
/*    */ public class AntiLevitation
/*    */   extends Feature {
/*    */   public AntiLevitation() {
/* 12 */     super("AntiLevitation", "Убирает эффект левитации", Type.Player);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 17 */     if (mc.player.isPotionActive(MobEffects.LEVITATION))
/* 18 */       mc.player.removeActivePotionEffect(MobEffects.LEVITATION); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\movement\AntiLevitation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */