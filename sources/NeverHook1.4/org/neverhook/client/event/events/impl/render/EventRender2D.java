/*    */ package org.neverhook.client.event.events.impl.render;
/*    */ 
/*    */ import net.minecraft.client.gui.ScaledResolution;
/*    */ import org.neverhook.client.event.events.Event;
/*    */ 
/*    */ public class EventRender2D
/*    */   implements Event {
/*    */   private final ScaledResolution resolution;
/*    */   
/*    */   public EventRender2D(ScaledResolution resolution) {
/* 11 */     this.resolution = resolution;
/*    */   }
/*    */   
/*    */   public ScaledResolution getResolution() {
/* 15 */     return this.resolution;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\event\events\impl\render\EventRender2D.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */