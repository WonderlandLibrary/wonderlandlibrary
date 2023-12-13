/*    */ package org.neverhook.client.feature.impl.player;
/*    */ 
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventWebSolid;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ 
/*    */ public class SolidWeb
/*    */   extends Feature {
/*    */   public SolidWeb() {
/* 11 */     super("SolidWeb", "Делает паутину полноценным блоком", Type.Misc);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onWebSolid(EventWebSolid event) {
/* 16 */     event.setCancelled(true);
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\player\SolidWeb.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */