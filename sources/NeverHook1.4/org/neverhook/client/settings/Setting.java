/*    */ package org.neverhook.client.settings;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class Setting
/*    */   extends Configurable {
/*    */   protected String name;
/*    */   protected Supplier<Boolean> visible;
/*    */   
/*    */   public boolean isVisible() {
/* 11 */     return ((Boolean)this.visible.get()).booleanValue();
/*    */   }
/*    */   
/*    */   public void setVisible(Supplier<Boolean> visible) {
/* 15 */     this.visible = visible;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 19 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\settings\Setting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */