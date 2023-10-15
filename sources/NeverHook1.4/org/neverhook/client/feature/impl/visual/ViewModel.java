/*    */ package org.neverhook.client.feature.impl.visual;
/*    */ 
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.util.EnumHandSide;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventTransformSideFirstPerson;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class ViewModel extends Feature {
/*    */   public static NumberSetting rightx;
/*    */   public static NumberSetting righty;
/*    */   public static NumberSetting rightz;
/*    */   public static NumberSetting leftx;
/*    */   public static NumberSetting lefty;
/*    */   public static NumberSetting leftz;
/*    */   
/*    */   public ViewModel() {
/* 21 */     super("ViewModel", "Позволяет редактировать позицию предметов в руке", Type.Visuals);
/* 22 */     rightx = new NumberSetting("RightX", 0.0F, -2.0F, 2.0F, 0.1F, () -> Boolean.valueOf(true));
/* 23 */     righty = new NumberSetting("RightY", 0.2F, -2.0F, 2.0F, 0.1F, () -> Boolean.valueOf(true));
/* 24 */     rightz = new NumberSetting("RightZ", 0.2F, -2.0F, 2.0F, 0.1F, () -> Boolean.valueOf(true));
/* 25 */     leftx = new NumberSetting("LeftX", 0.0F, -2.0F, 2.0F, 0.1F, () -> Boolean.valueOf(true));
/* 26 */     lefty = new NumberSetting("LeftY", 0.2F, -2.0F, 2.0F, 0.1F, () -> Boolean.valueOf(true));
/* 27 */     leftz = new NumberSetting("LeftZ", 0.2F, -2.0F, 2.0F, 0.1F, () -> Boolean.valueOf(true));
/* 28 */     addSettings(new Setting[] { (Setting)rightx, (Setting)righty, (Setting)rightz, (Setting)leftx, (Setting)lefty, (Setting)leftz });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onSidePerson(EventTransformSideFirstPerson event) {
/* 33 */     if (event.getEnumHandSide() == EnumHandSide.RIGHT) {
/* 34 */       GlStateManager.translate(rightx.getNumberValue(), righty.getNumberValue(), rightz.getNumberValue());
/*    */     }
/* 36 */     if (event.getEnumHandSide() == EnumHandSide.LEFT)
/* 37 */       GlStateManager.translate(-leftx.getNumberValue(), lefty.getNumberValue(), leftz.getNumberValue()); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\visual\ViewModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */