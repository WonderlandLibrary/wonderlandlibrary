/*    */ package org.neverhook.client.ui.clickgui;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ import org.neverhook.client.ui.clickgui.component.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SorterHelper
/*    */   implements Comparator<Component>
/*    */ {
/*    */   public int compare(Component component, Component component2) {
/* 12 */     if (component instanceof org.neverhook.client.ui.clickgui.component.impl.ModuleComponent && component2 instanceof org.neverhook.client.ui.clickgui.component.impl.ModuleComponent) {
/* 13 */       return component.getName().compareTo(component2.getName());
/*    */     }
/* 15 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\clickgui\SorterHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */