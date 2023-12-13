/*    */ package org.neverhook.client.ui.components.altmanager.alt;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class AltManager
/*    */ {
/*    */   public static Alt lastAlt;
/*  8 */   public static ArrayList<Alt> registry = new ArrayList<>();
/*    */   
/*    */   public ArrayList<Alt> getRegistry() {
/* 11 */     return registry;
/*    */   }
/*    */   
/*    */   public void setLastAlt(Alt alt) {
/* 15 */     lastAlt = alt;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\components\altmanager\alt\AltManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */