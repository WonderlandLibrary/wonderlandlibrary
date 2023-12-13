/*    */ package org.neverhook.client.macro;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class MacroManager
/*    */ {
/*  8 */   public List<Macro> macros = new ArrayList<>();
/*    */   
/*    */   public List<Macro> getMacros() {
/* 11 */     return this.macros;
/*    */   }
/*    */   
/*    */   public void addMacro(Macro macro) {
/* 15 */     this.macros.add(macro);
/*    */   }
/*    */   
/*    */   public void deleteMacroByKey(int key) {
/* 19 */     this.macros.removeIf(macro -> (macro.getKey() == key));
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\macro\MacroManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */