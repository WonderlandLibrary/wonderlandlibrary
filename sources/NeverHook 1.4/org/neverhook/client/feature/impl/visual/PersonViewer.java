/*    */ package org.neverhook.client.feature.impl.visual;
/*    */ 
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class PersonViewer extends Feature {
/*    */   public static NumberSetting viewerYaw;
/*    */   public static NumberSetting fovModifier;
/*    */   public static NumberSetting viewerPitch;
/*    */   
/*    */   public PersonViewer() {
/* 14 */     super("PersonViewer", "Повозяляет изменять положение камеры второго и третьего лица", Type.Visuals);
/* 15 */     fovModifier = new NumberSetting("FOV Modifier", 4.0F, 1.0F, 50.0F, 1.0F, () -> Boolean.valueOf(true));
/* 16 */     viewerYaw = new NumberSetting("Viewer Yaw", 10.0F, -50.0F, 50.0F, 5.0F, () -> Boolean.valueOf(true));
/* 17 */     viewerPitch = new NumberSetting("Viewer Pitch", 10.0F, -50.0F, 50.0F, 5.0F, () -> Boolean.valueOf(true));
/* 18 */     addSettings(new Setting[] { (Setting)fovModifier, (Setting)viewerYaw, (Setting)viewerPitch });
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\visual\PersonViewer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */