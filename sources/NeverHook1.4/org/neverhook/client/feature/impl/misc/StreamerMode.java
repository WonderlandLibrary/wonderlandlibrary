/*    */ package org.neverhook.client.feature.impl.misc;
/*    */ 
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.BooleanSetting;
/*    */ 
/*    */ public class StreamerMode extends Feature {
/*    */   public static BooleanSetting ownName;
/*    */   public static BooleanSetting otherNames;
/*    */   public static BooleanSetting skinSpoof;
/*    */   public static BooleanSetting tabSpoof;
/*    */   public static BooleanSetting scoreBoardSpoof;
/*    */   public static BooleanSetting warpSpoof;
/*    */   public static BooleanSetting loginSpoof;
/*    */   
/*    */   public StreamerMode() {
/* 18 */     super("StreamerMode", "Позволяет скрывать информацию о себе и других игроках на видео или стриме", Type.Misc);
/* 19 */     ownName = new BooleanSetting("Own Name", true, () -> Boolean.valueOf(true));
/* 20 */     otherNames = new BooleanSetting("Other Names", true, () -> Boolean.valueOf(true));
/* 21 */     tabSpoof = new BooleanSetting("Tab Spoof", true, () -> Boolean.valueOf(true));
/* 22 */     skinSpoof = new BooleanSetting("Skin Spoof", true, () -> Boolean.valueOf(true));
/* 23 */     scoreBoardSpoof = new BooleanSetting("ScoreBoard Spoof", true, () -> Boolean.valueOf(true));
/* 24 */     warpSpoof = new BooleanSetting("Warp Spoof", true, () -> Boolean.valueOf(true));
/* 25 */     loginSpoof = new BooleanSetting("Login Spoof", true, () -> Boolean.valueOf(true));
/* 26 */     addSettings(new Setting[] { (Setting)ownName, (Setting)otherNames, (Setting)tabSpoof, (Setting)skinSpoof, (Setting)scoreBoardSpoof, (Setting)warpSpoof, (Setting)loginSpoof });
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\misc\StreamerMode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */