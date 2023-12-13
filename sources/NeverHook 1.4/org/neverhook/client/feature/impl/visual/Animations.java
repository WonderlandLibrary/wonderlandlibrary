/*    */ package org.neverhook.client.feature.impl.visual;
/*    */ 
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.BooleanSetting;
/*    */ import org.neverhook.client.settings.impl.ListSetting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class Animations
/*    */   extends Feature {
/*    */   public static NumberSetting speed;
/*    */   public static NumberSetting spinSpeed;
/*    */   public static BooleanSetting animation;
/* 17 */   public static BooleanSetting itemAnimation = new BooleanSetting("Item Animation", false, () -> Boolean.valueOf(true));
/*    */   
/*    */   public static BooleanSetting smallItem;
/*    */   public static ListSetting swordAnim;
/*    */   public static ListSetting itemAnim;
/* 22 */   public static NumberSetting x = new NumberSetting("X", 0.0F, -1.0F, 1.0F, 0.01F, () -> Boolean.valueOf(swordAnim.currentMode.equals("Custom")));
/* 23 */   public static NumberSetting y = new NumberSetting("Y", 0.0F, -1.0F, 1.0F, 0.01F, () -> Boolean.valueOf(swordAnim.currentMode.equals("Custom")));
/* 24 */   public static NumberSetting z = new NumberSetting("Z", 0.0F, -1.0F, 1.0F, 0.01F, () -> Boolean.valueOf(swordAnim.currentMode.equals("Custom")));
/* 25 */   public static NumberSetting rotate = new NumberSetting("Rotate 1", 360.0F, -360.0F, 360.0F, 1.0F, () -> Boolean.valueOf(swordAnim.currentMode.equals("Custom")));
/* 26 */   public static NumberSetting rotate2 = new NumberSetting("Rotate 2", 0.0F, -360.0F, 360.0F, 1.0F, () -> Boolean.valueOf(swordAnim.currentMode.equals("Custom")));
/* 27 */   public static NumberSetting rotate3 = new NumberSetting("Rotate 3", 0.0F, -360.0F, 360.0F, 1.0F, () -> Boolean.valueOf(swordAnim.currentMode.equals("Custom")));
/* 28 */   public static NumberSetting angle = new NumberSetting("Angle", 0.0F, -50.0F, 100.0F, 1.0F, () -> Boolean.valueOf(swordAnim.currentMode.equals("Custom")));
/* 29 */   public static NumberSetting scale = new NumberSetting("Scale", 1.0F, -10.0F, 10.0F, 0.1F, () -> Boolean.valueOf(swordAnim.currentMode.equals("Custom")));
/* 30 */   public static NumberSetting smooth = new NumberSetting("Smooth", 3.0F, -10.0F, 10.0F, 0.1F, () -> Boolean.valueOf(swordAnim.currentMode.equals("Custom")));
/*    */   
/*    */   public Animations() {
/* 33 */     super("Animations", "Добавляет анимацию на меч", Type.Visuals);
/* 34 */     animation = new BooleanSetting("Blocking Animation", false, () -> Boolean.valueOf(true));
/* 35 */     speed = new NumberSetting("Smooth Attack", 8.0F, 1.0F, 20.0F, 1.0F, () -> Boolean.valueOf(!swordAnim.currentMode.equals("Neutral")));
/* 36 */     spinSpeed = new NumberSetting("Spin Speed", 4.0F, 1.0F, 10.0F, 1.0F, () -> Boolean.valueOf(((animation.getBoolValue() && swordAnim.currentMode.equals("Astolfo")) || swordAnim.currentMode.equals("Spin") || itemAnimation.getBoolValue())));
/* 37 */     smallItem = new BooleanSetting("Mini Item", false, () -> Boolean.valueOf(true));
/* 38 */     swordAnim = new ListSetting("Blocking Animation Mode", "NeverHook", () -> Boolean.valueOf(animation.getBoolValue()), new String[] { "NeverHook", "Spin", "Astolfo", "Custom", "Neutral" });
/* 39 */     itemAnim = new ListSetting("Item Animation Mode", "Spin", () -> Boolean.valueOf(itemAnimation.getBoolValue()), new String[] { "360", "Spin" });
/* 40 */     addSettings(new Setting[] { (Setting)animation, (Setting)swordAnim, (Setting)itemAnimation, (Setting)itemAnim, (Setting)speed, (Setting)spinSpeed, (Setting)x, (Setting)y, (Setting)z, (Setting)rotate, (Setting)rotate2, (Setting)rotate3, (Setting)angle, (Setting)scale, (Setting)smooth, (Setting)smallItem });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 45 */     setSuffix(swordAnim.getCurrentMode());
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\visual\Animations.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */