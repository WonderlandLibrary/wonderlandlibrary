/*    */ package org.neverhook.client.feature.impl.misc;
/*    */ 
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.misc.ChatHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class ItemScroller extends Feature {
/*    */   public static NumberSetting scrollerDelay;
/*    */   
/*    */   public ItemScroller() {
/* 13 */     super("ItemScroller", "Позволяет быстро лутать сундуки при нажатии на шифт и ЛКМ", Type.Misc);
/* 14 */     scrollerDelay = new NumberSetting("Scroller Delay", 0.0F, 0.0F, 1000.0F, 50.0F, () -> Boolean.valueOf(true));
/* 15 */     addSettings(new Setting[] { (Setting)scrollerDelay });
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 20 */     for (int i = 0; i < 3; i++) {
/* 21 */       ChatHelper.addChatMessage("Зажмите шифт и левую кнопку мыши, что бы быстро лутать сундуки!");
/*    */     }
/* 23 */     super.onEnable();
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\misc\ItemScroller.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */