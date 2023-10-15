/*    */ package org.neverhook.client.feature.impl.visual;
/*    */ 
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.render.EventRenderScoreboard;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.BooleanSetting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class Scoreboard extends Feature {
/*    */   public static BooleanSetting noScore;
/* 14 */   public static BooleanSetting scoreboardPoints = new BooleanSetting("Points", false, () -> Boolean.valueOf(true));
/*    */   public NumberSetting x;
/*    */   public NumberSetting y;
/*    */   
/*    */   public Scoreboard() {
/* 19 */     super("Scoreboard", "Позволяет настроить скорборд на сервере", Type.Visuals);
/* 20 */     noScore = new BooleanSetting("No Scoreboard", false, () -> Boolean.valueOf(true));
/* 21 */     this.x = new NumberSetting("Scoreboard X", 0.0F, -1000.0F, 1000.0F, 1.0F, () -> Boolean.valueOf(!noScore.getBoolValue()));
/* 22 */     this.y = new NumberSetting("Scoreboard Y", 0.0F, -500.0F, 500.0F, 1.0F, () -> Boolean.valueOf(!noScore.getBoolValue()));
/* 23 */     addSettings(new Setting[] { (Setting)noScore, (Setting)scoreboardPoints, (Setting)this.x, (Setting)this.y });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onRenderScoreboard(EventRenderScoreboard event) {
/* 28 */     if (event.isPre()) {
/* 29 */       GlStateManager.translate(-this.x.getNumberValue(), this.y.getNumberValue(), 12.0F);
/*    */     } else {
/* 31 */       GlStateManager.translate(this.x.getNumberValue(), -this.y.getNumberValue(), 12.0F);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\visual\Scoreboard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */