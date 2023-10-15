/*    */ package org.neverhook.client.feature.impl.movement;
/*    */ 
/*    */ import net.minecraft.init.MobEffects;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.player.MovementHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.BooleanSetting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class WaterSpeed extends Feature {
/*    */   public static NumberSetting speed;
/*    */   private final BooleanSetting speedCheck;
/*    */   
/*    */   public WaterSpeed() {
/* 18 */     super("WaterSpeed", "Делает вас быстрее в воде", Type.Movement);
/* 19 */     speed = new NumberSetting("Speed Amount", 1.0F, 0.1F, 4.0F, 0.01F, () -> Boolean.valueOf(true));
/* 20 */     this.speedCheck = new BooleanSetting("Speed Potion Check", false, () -> Boolean.valueOf(true));
/* 21 */     addSettings(new Setting[] { (Setting)this.speedCheck, (Setting)speed });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 26 */     if (!mc.player.isPotionActive(MobEffects.SPEED) && this.speedCheck.getBoolValue()) {
/*    */       return;
/*    */     }
/* 29 */     if (mc.player.isInLiquid())
/* 30 */       MovementHelper.setSpeed(speed.getNumberValue()); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\movement\WaterSpeed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */