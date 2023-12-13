/*    */ package org.neverhook.client.feature.impl.visual;
/*    */ 
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class ItemPhysics extends Feature {
/*    */   public static NumberSetting physicsSpeed;
/*    */   
/*    */   public ItemPhysics() {
/* 12 */     super("ItemPhysics", "Добавляет физику предметов при их выбрасивании", Type.Visuals);
/* 13 */     physicsSpeed = new NumberSetting("Physics Speed", 0.5F, 0.1F, 5.0F, 0.5F, () -> Boolean.valueOf(true));
/* 14 */     addSettings(new Setting[] { (Setting)physicsSpeed });
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\visual\ItemPhysics.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */