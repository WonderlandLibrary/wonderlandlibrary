/*    */ package org.neverhook.client.feature.impl.ghost;
/*    */ 
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.math.MathematicHelper;
/*    */ import org.neverhook.client.helpers.misc.TimerHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class AutoClicker extends Feature {
/* 13 */   public NumberSetting minCps = new NumberSetting("Min", 6.0F, 1.0F, 20.0F, 1.0F, () -> Boolean.valueOf(true), NumberSetting.NumberType.APS);
/* 14 */   public NumberSetting maxCps = new NumberSetting("Max", 10.0F, 1.0F, 20.0F, 1.0F, () -> Boolean.valueOf(true), NumberSetting.NumberType.APS);
/*    */   
/* 16 */   public TimerHelper timerHelper = new TimerHelper();
/*    */   
/*    */   public AutoClicker() {
/* 19 */     super("AutoClicker", "Кликает определенный cps", Type.Ghost);
/* 20 */     addSettings(new Setting[] { (Setting)this.minCps, (Setting)this.maxCps });
/*    */   }
/*    */ 
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 26 */     if (mc.gameSettings.keyBindAttack.isKeyDown() && !mc.player.isUsingItem()) {
/* 27 */       int cps = (int)MathematicHelper.randomizeFloat(this.maxCps.getNumberValue(), this.minCps.getNumberValue());
/* 28 */       if (this.timerHelper.hasReached((1000 / cps))) {
/* 29 */         mc.clickMouse();
/* 30 */         this.timerHelper.reset();
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\ghost\AutoClicker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */